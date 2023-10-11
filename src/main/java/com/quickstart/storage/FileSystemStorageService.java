package com.quickstart.storage;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class FileSystemStorageService implements StorageService {

  private final Path rootLocation;

  public FileSystemStorageService() {
    // TODO: folder name should read from application.yml
    this.rootLocation = Paths.get(System.getProperty("user.dir"), "upload-dir");
  }

  @Override
  public void init() {
    try {
      Files.createDirectories(this.rootLocation);
      log.info("Directories created successfully.");
    } catch (IOException e) {
      e.printStackTrace();
      log.info("Failed to create directories.");
    }
  }

  @Override
  public void store(MultipartFile file) throws StorageHandleException {
    log.info("Storing file: {} to {}", file.getOriginalFilename(),
        this.rootLocation.resolve(file.getOriginalFilename()));
    try {
      if (file.isEmpty()) {
        throw new StorageException("Failed to store empty file " + file.getOriginalFilename());
      }
      Path target = this.rootLocation.resolve(file.getOriginalFilename());
      if (Files.exists(target)) {
        throw new StorageHandleException("File " + file.getOriginalFilename() + " already exists!");
      }
      Files.copy(file.getInputStream(), target);
    } catch (IOException e) {
      throw new StorageException("Failed to store file " + file.getOriginalFilename(), e);
    }
  }

  @Override
  public Stream<Path> loadAll() {
    try {
      return Files.walk(this.rootLocation, 1).filter(path -> !path.equals(this.rootLocation))
          .map(path -> this.rootLocation.relativize(path));
    } catch (IOException e) {
      throw new StorageException("Failed to read stored files", e);
    }

  }

  @Override
  public Path load(String filename) {

    return rootLocation.resolve(filename);
  }

  @Override
  public Resource loadAsResource(String filename) {

    try {
      Path file = load(filename);
      Resource resource = new UrlResource(file.toUri());
      if (resource.exists() || resource.isReadable()) {
        return resource;
      } else {
        throw new StorageException("Could not read file: " + filename);

      }
    } catch (MalformedURLException e) {
      throw new StorageException("Could not read file: " + filename, e);
    }
  }

  @Override
  public void deleteAll() {
    FileSystemUtils.deleteRecursively(rootLocation.toFile());

  }
}
