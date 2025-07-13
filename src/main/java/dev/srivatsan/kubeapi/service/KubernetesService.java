package dev.srivatsan.kubeapi.service;

import dev.srivatsan.kubeapi.config.AppConfig;
import dev.srivatsan.kubeapi.model.FailureReason;
import dev.srivatsan.kubeapi.model.PodDetails;
import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.openapi.apis.AppsV1Api;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.V1ContainerStateWaiting;
import io.kubernetes.client.openapi.models.V1ContainerStatus;
import io.kubernetes.client.openapi.models.V1Pod;
import io.kubernetes.client.util.Config;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

@Slf4j
@Service
public class KubernetesService {

    private final CoreV1Api coreV1Api;
    private final AppsV1Api appsV1Api;
    private final AppConfig appConfig;

    public KubernetesService(AppConfig appConfig) throws IOException {
        ApiClient client = Config.defaultClient();
        Configuration.setDefaultApiClient(client);
        this.coreV1Api = new CoreV1Api();
        this.appsV1Api = new AppsV1Api();
        this.appConfig = appConfig;
    }

    /**
     * Fetches the details of all pods in the configured namespace.
     */

    public List<PodDetails> getPodDetails() throws ApiException {

        CoreV1Api.APIlistNamespacedPodRequest apIlistNamespacedPodRequest = coreV1Api.listNamespacedPod(appConfig.getNamespace());
        List<V1Pod> podList = apIlistNamespacedPodRequest.execute().getItems();

        Predicate<V1ContainerStatus> checkPodStatusPredicate = containerStatus -> !containerStatus.getReady();
        List<PodDetails> podDetailsDetailsList = new ArrayList<>(20);
        for (V1Pod v1Pod : podList) {

            List<V1ContainerStatus> notReadyContainers = v1Pod.getStatus().getContainerStatuses()
                    .stream()
                    .filter(checkPodStatusPredicate)
                    .toList();

            boolean isReady = notReadyContainers.isEmpty();
            List<FailureReason> failureReasonList = isReady ? Collections.emptyList() : notReadyContainers.stream()
                    .map(containerStatus -> {
                        V1ContainerStateWaiting waitingState = containerStatus.getState().getWaiting();
                        if (waitingState != null) {
                            return new FailureReason(
                                    containerStatus.getName(),
                                    waitingState.getReason(),
                                    waitingState.getMessage()
                            );
                        }
                        return null;
                    })
                    .filter(reason -> reason != null)
                    .toList();

            PodDetails podDetails = new PodDetails();
            podDetails.setName(v1Pod.getMetadata().getName());
            podDetails.setIp(v1Pod.getStatus().getPodIP());
            podDetails.setRunning(isReady);
            podDetails.setFailureReason(failureReasonList);

            podDetailsDetailsList.add(podDetails);
        }

        log.info("Pod Details List: {}", podDetailsDetailsList);
        return podDetailsDetailsList;
    }

    /**
     * Filters the pod details based on the provided predicate.
     */

    public List<PodDetails> getPodDetailsByCriteria(Predicate<PodDetails> checkPodStatusPredicate) {
        try {
            List<PodDetails> podDetailsList = getPodDetails();
            return podDetailsList.stream().filter(checkPodStatusPredicate).toList();
        } catch (ApiException e) {
            throw new RuntimeException("Exception While Fetching Pod Details" + e);
        }
    }

}
