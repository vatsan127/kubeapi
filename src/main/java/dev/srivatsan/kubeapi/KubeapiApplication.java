package dev.srivatsan.kubeapi;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class KubeapiApplication {

    public static void main(String[] args) {
        SpringApplication.run(KubeapiApplication.class, args);
    }

}
