package dev.srivatsan.kubeapi.controller;

import dev.srivatsan.kubeapi.api.KubeAPI;
import dev.srivatsan.kubeapi.model.PodDetails;
import dev.srivatsan.kubeapi.service.KubernetesService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class KubeAPIController implements KubeAPI {

    private final KubernetesService kubernetesService;

    public KubeAPIController(KubernetesService kubernetesService) {
        this.kubernetesService = kubernetesService;
    }

    @GetMapping("/failed-pods")
    public ResponseEntity<List<PodDetails>> getFailedPods() {
        List<PodDetails> failedPods = kubernetesService.getPodDetailsByCriteria(p -> !p.isRunning());
        return ResponseEntity.ok(failedPods);
    }

    @GetMapping("/running-pods")
    public ResponseEntity<List<PodDetails>> getRunningPods() {
        List<PodDetails> runningPods = kubernetesService.getPodDetailsByCriteria(p -> p.isRunning());
        return ResponseEntity.ok(runningPods);
    }

    @GetMapping("/all-pods")
    public ResponseEntity<List<PodDetails>> getAllPods() {
        List<PodDetails> allPods = kubernetesService.getPodDetailsByCriteria(p -> true);
        return ResponseEntity.ok(allPods);
    }

}
