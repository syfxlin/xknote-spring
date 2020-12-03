package me.ixk.xknote.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "xknote")
@Data
public class XkNoteConfig {

    private String storagePath;
    private String key;
}
