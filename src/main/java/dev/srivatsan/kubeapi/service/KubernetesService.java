package dev.srivatsan.kubeapi.service;

import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.openapi.apis.AppsV1Api;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.V1Deployment;
import io.kubernetes.client.openapi.models.V1Pod;
import io.kubernetes.client.openapi.models.V1Service;
import io.kubernetes.client.util.Config;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Slf4j
@Service
public class KubernetesService {

    private final CoreV1Api coreV1Api;
    private final AppsV1Api appsV1Api;

    public KubernetesService() throws IOException {
        ApiClient client = Config.defaultClient();
        Configuration.setDefaultApiClient(client);
        this.coreV1Api = new CoreV1Api();
        this.appsV1Api = new AppsV1Api();
    }

    public void getAllPodIpsInCurrentNamespace() throws ApiException {

        String namespace = System.getenv().getOrDefault("KUBERNETES_NAMESPACE", "default");

        CoreV1Api.APIlistNamespacedPodRequest apIlistNamespacedPodRequest = coreV1Api.listNamespacedPod(namespace);
        List<V1Pod> podList = apIlistNamespacedPodRequest.execute().getItems();
        log.info("Listing Pods in Namespace: {}", podList);

        CoreV1Api.APIlistNamespacedServiceRequest serviceNames = coreV1Api.listNamespacedService(namespace);
        List<V1Service> serviceList = serviceNames.execute().getItems();
        log.debug("Listing Services in Namespace: {}", serviceList);

        AppsV1Api.APIlistNamespacedDeploymentRequest deployments = appsV1Api.listNamespacedDeployment(namespace);
        List<V1Deployment> deploymentList = deployments.execute().getItems();
        log.debug("Listing Deployments in Namespace: {}", deploymentList);

        podList.forEach(pod ->
                        log.info(
                                "podName: '{}', status: '{}', podIp: '{}'",
                                pod.getMetadata().getName(), pod.getStatus().getPhase(), pod.getStatus().getPodIP()
                        )
                );
    }
}
