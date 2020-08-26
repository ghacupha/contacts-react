package io.github.contacts.internal.fileProcessing;

import io.github.contacts.internal.model.FileNotification;

public interface FileUploadProcessor<T> {

    T processFileUpload(T fileUpload, FileNotification fileNotification);
}
