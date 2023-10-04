package com.quickstart.storage;

import java.nio.file.Path;
import java.util.stream.Stream;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface StorageService {

    // void init() method is used to initialize the storage service.
    void init();

    // store() method is used to store a file.
    void store(MultipartFile file);

    // loadAll() method is used to load all files as a Stream<Path> object.
    Stream<Path> loadAll();

    // load() method is used to load a file.
    Path load(String filename);

    // loadAsResource() method is used to load a file as a Resource object.
    Resource loadAsResource(String filename);

    // deleteAll() method is used to delete all files.
    void deleteAll();
}
