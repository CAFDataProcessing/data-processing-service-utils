/*
 * Copyright 2015-2017 EntIT Software LLC, a Micro Focus company.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.cafdataprocessing.utilities.tasksubmitter.taskmessage;

import com.github.cafdataprocessing.utilities.tasksubmitter.services.Services;
import com.hpe.caf.worker.document.DocumentWorkerDocument;
import com.hpe.caf.api.ConfigurationException;
import com.hpe.caf.api.worker.DataStoreException;
import com.hpe.caf.api.worker.ManagedDataStore;
import com.github.cafdataprocessing.utilities.tasksubmitter.FileAccessChecker;
import com.hpe.caf.worker.document.DocumentWorkerFieldEncoding;
import com.hpe.caf.worker.document.DocumentWorkerFieldValue;
import org.apache.commons.vfs2.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Tasked with providing Documnet Worker Documents representing provided folder or file location
 */
public class DocumentWorkerDocumentsBuilder
{
    private static final Logger LOGGER = LoggerFactory.getLogger(PolicyDocumentsBuilder.class);

    private static final ManagedDataStore DATASTORE = Services.getInstance().getStorageDataStore();
    private static final String DATA_DIR = Services.getInstance().getStorageConfiguration().getDataDir();

    /**
     * Constructs a Document Worker Document with basic metadata for specified document path.
     *
     * @param docPath Path to a document file to base Policy Worker Document on.
     * @return Constructed Document Worker Document and associated file.
     * @throws IOException If error occurs accessing file at provided path.
     * @throws DataStoreException If error occurs storing data for file.
     */
    public static DocumentAndFile getDocument(final String docPath) throws IOException, DataStoreException
    {
        if (docPath == null) {
            throw new NullPointerException("Path passed to build document worker document cannot be null.");
        }
        FileSystemManager fsManager = VFS.getManager();
        FileObject file = fsManager.resolveFile(docPath);
        // get the data store reference
        String dataStoreReference = getDataStoreReferenceForFile(file);
        //Construct Document Worker Document object, and put on the list of data processing documents to be sent.
        DocumentWorkerDocument builtDocument = buildDocument(dataStoreReference, getDocumentReference(file));
        builtDocument.fields.put("FILESIZE_BYTES", getAsListOfValues(String.valueOf(new File(file.getName().getPath()).length()),
                                                                     DocumentWorkerFieldEncoding.utf8));
        return new DocumentAndFile(null, file, builtDocument);
    }

    /**
     * Constructs a list of Document Worker Documents with basic metadata for each file filesToSendLocation directory.
     *
     * Uses fields.... dataStore The DataStore to store the file to. filesToSendLocation The directory of files to process
     *
     * @param directory Directory containing files to construct Policy Documents from.
     *
     * @return List of Documents and associated files.
     * @throws ConfigurationException If path provided is not a directory.
     * @throws IOException If error occurs accessing files in directory.
     * @throws DataStoreException If error occurs storing data for file.
     * @throws InterruptedException If error accessing data for a file.
     */
    public static List<DocumentAndFile> getDocuments(final String directory)
        throws ConfigurationException, IOException, DataStoreException, InterruptedException
    {
        if (directory == null) {
            throw new NullPointerException("Directory passed to read documents from should not be null.");
        }
        // final list of Documents to be send for data processing.
        final ArrayList<DocumentAndFile> dataProcessingDocumentsList = new ArrayList<>();

        final FileSystemManager fsManager = VFS.getManager();
        final FileObject folder = fsManager.resolveFile(directory);

        if (!folder.isFolder()) {
            throw new ConfigurationException(
                "Location to read documents from is not a directory." + directory);
        }

        for (final FileObject fileObject : folder.getChildren()) {
            //if the file is a directory then add all documents in that folder
            if (fileObject.isFolder()) {
                dataProcessingDocumentsList.addAll(getDocuments(fileObject.getName().getURI()));
                continue;
            }
            //verify that we can access the file (i.e. it is not in use)
            if (!FileAccessChecker.verifyFileReadyForUse(fileObject)) {
                //unable to access file, do not try to build a message for it
                LOGGER.warn("Unable to access detected file " + fileObject.getPublicURIString() + ". "
                    + "Another process may be using it or it may have been deleted. File will not be used.");
                continue;
            }

            // get the data store reference
            final String dataStoreReference = getDataStoreReferenceForFile(fileObject);
            //Construct Policy Worker Document object, and put on the list of data processing documents to be sent.
            final DocumentWorkerDocument builtDocument = buildDocument(dataStoreReference, getDocumentReference(fileObject));
            builtDocument.fields.put("FILESIZE_BYTES", getAsListOfValues(String.valueOf(new File(fileObject.getName().getPath()).length()),
                                                                         DocumentWorkerFieldEncoding.utf8));

            dataProcessingDocumentsList.add(new DocumentAndFile(null, fileObject, builtDocument));
        }

        return dataProcessingDocumentsList;
    }

    private static DocumentWorkerDocument buildDocument(final String dataStoreReference, final String documentReference)
    {
        String filename;

        //adding the name of the file. If we were provided the full path to the file then omit everything except the
        //text after last backslash (expecting name of the file from that point).
        int lastBackslashIndex = documentReference.lastIndexOf("/");
        if (lastBackslashIndex == -1) {
            filename = documentReference;
        } else {
            filename = documentReference.substring(lastBackslashIndex + 1);
        }
        final DocumentWorkerDocument document = new DocumentWorkerDocument();
        document.fields = new HashMap<>();
        document.fields.put("FILENAME", getAsListOfValues(filename, DocumentWorkerFieldEncoding.utf8));
        document.fields.put("storageReference", getAsListOfValues(dataStoreReference, DocumentWorkerFieldEncoding.storage_ref));
        document.reference = dataStoreReference;
        return document;
    }

    private static String getDataStoreReferenceForFile(final FileObject file) throws DataStoreException, IOException
    {
        try {
            final File f = new File(file.getName().getPath());
            return DATASTORE.store(f.toPath(), DATA_DIR);
        } catch (final Exception e) {
            throw new DataStoreException("Failure storing file. File path: " + file.getName().getPath(), e);
        }
    }

    private static String getDocumentReference(final FileObject file)
    {
        return file.getName().getPath();
    }

    private static List<DocumentWorkerFieldValue> getAsListOfValues(final String value, final DocumentWorkerFieldEncoding encoding)
    {
        final DocumentWorkerFieldValue fieldValue = new DocumentWorkerFieldValue();
        fieldValue.data = value;
        fieldValue.encoding = encoding;

        List<DocumentWorkerFieldValue> fieldValues = new ArrayList<>();
        fieldValues.add(fieldValue);
        return fieldValues;
    }
}
