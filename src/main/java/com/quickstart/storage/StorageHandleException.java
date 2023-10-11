package com.quickstart.storage;

public class StorageHandleException extends Exception {

  public StorageHandleException(String message) {
    super(message);
  }

  public StorageHandleException(String message, Throwable cause) {
    super(message, cause);
  }
}
