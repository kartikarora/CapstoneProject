package me.kartikarora.transfersh.models;

/**
 * Developer: chipset
 * Package : me.kartikarora.transfersh
 * Project : Transfer.sh
 * Date : 14/6/16
 */
public class FileModel {

    private String fileName;
    private String fileType;
    private String fileUrl;
    private String fileSize;
    private String fileCreated;

    public String getFileCreated() {
        return fileCreated;
    }

    public FileModel setFileCreated(String fileCreated) {
        this.fileCreated = fileCreated;
        return this;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public FileModel setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
        return this;
    }

    public String getFileSize() {
        return fileSize;
    }

    public FileModel setFileSize(String fileSize) {
        this.fileSize = fileSize;
        return this;
    }

    public String getFileName() {
        return fileName;
    }

    public FileModel setFileName(String fileName) {
        this.fileName = fileName;
        return this;
    }

    public String getFileType() {
        return fileType;
    }

    public FileModel setFileType(String fileType) {
        this.fileType = fileType;
        return this;
    }
}
