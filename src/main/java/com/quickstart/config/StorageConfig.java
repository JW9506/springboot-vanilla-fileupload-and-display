package com.quickstart.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ConfigurationProperties("storage")
public class StorageConfig {

    /**
     * Folder location for storing files
     */
    private String location;
}
