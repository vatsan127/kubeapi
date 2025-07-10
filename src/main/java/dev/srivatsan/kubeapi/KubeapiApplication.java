package dev.srivatsan.kubeapi;

import dev.srivatsan.kubeapi.service.KubernetesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@SpringBootApplication
public class KubeapiApplication implements CommandLineRunner {

    private final KubernetesService kubernetesService;

    public KubeapiApplication(KubernetesService kubernetesService) {
        this.kubernetesService = kubernetesService;
    }


    public static void main(String[] args) {
        SpringApplication.run(KubeapiApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        while(true) {
            Map<String, String> allPodIpsInCurrentNamespace = kubernetesService.getAllPodIpsInCurrentNamespace();
            log.info(allPodIpsInCurrentNamespace.toString());

            TimeUnit.SECONDS.sleep(10);

        }
    }
}
