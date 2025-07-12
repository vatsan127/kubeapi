package dev.srivatsan.kubeapi.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

@Slf4j
@Data
@Component
@ConfigurationProperties(prefix = "global")
public class AppConfig {
    private String namespace;
    private String debugModeEnabled;
    private List<String> exclusionList;

    @PostConstruct
    private void init() {
        log.info("Initializing AppConfig : {}", this);
    }

}
