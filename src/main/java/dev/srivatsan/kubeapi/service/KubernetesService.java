package dev.srivatsan.kubeapi.service;

import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.V1Pod;
import io.kubernetes.client.util.Config;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class KubernetesService {

    private final CoreV1Api coreV1Api;

    public KubernetesService() throws IOException {
        ApiClient client = Config.defaultClient();
        Configuration.setDefaultApiClient(client);
        this.coreV1Api = new CoreV1Api();
    }

    public @NotNull Map<String, String> getAllPodIpsInCurrentNamespace() throws ApiException {

        String namespace = System.getenv().getOrDefault("KUBERNETES_NAMESPACE", "default");
        CoreV1Api.APIlistNamespacedPodRequest apIlistNamespacedPodRequest = coreV1Api.listNamespacedPod(
                namespace
        );

        List<V1Pod> items = apIlistNamespacedPodRequest.execute().getItems();
        log.info("All Pod Items - {}",items);

        return items.stream()
                .filter(item -> item.getStatus() != null && item.getStatus().getPodIP() != null)
                .collect(Collectors.toMap(
                        item -> item.getStatus().getPodIP(),
                        item -> item.getStatus().getPhase()
                ));
    }
}
