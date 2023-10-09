package com.quickstart.storage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class FileSystemStorageService implements StorageService {

  private final Path rootLocation;

  public FileSystemStorageService() {
    // TODO: folder name should read from application.yml
    this.rootLocation = Paths.get(System.getProperty("user.dir"), "upload-dir");
    try {
      Files.createDirectories(this.rootLocation);
      log.info("Directories created successfully.");
    } catch (IOException e) {
      e.printStackTrace();
      log.info("Failed to create directories.");
    }
  }

  @Override
  public void init() {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'init'");
  }

  @Override
  public void store(MultipartFile file) {
    log.info("Storing file: {} to {}", file.getOriginalFilename(),
        this.rootLocation.resolve(file.getOriginalFilename()));
    try {
      if (file.isEmpty()) {
        throw new StorageException("Failed to store empty file " + file.getOriginalFilename());
      }
      Files.copy(file.getInputStream(), this.rootLocation.resolve(file.getOriginalFilename()));
    } catch (IOException e) {
      throw new StorageException("Failed to store file " + file.getOriginalFilename(), e);
    }
  }

  @Override
  public Stream<Path> loadAll() {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'loadAll'");
  }

  @Override
  public Path load(String filename) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'load'");
  }

  @Override
  public Resource loadAsResource(String filename) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'loadAsResource'");
  }

  @Override
  public void deleteAll() {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'deleteAll'");
  }

}
