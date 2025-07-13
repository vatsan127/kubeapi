package dev.srivatsan.kubeapi.api;


import dev.srivatsan.kubeapi.model.PodDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Tag(name = "K8s", description = "${global.namespace} API Call Manager")
public interface KubeAPI {

    @Operation(summary = "Failed Pods", description = "Gets pods that are not running in the namespace")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Successfully retrieved pods")})
    ResponseEntity<List<PodDetails>> getFailedPods();

    @Operation(summary = "Running Pods", description = "Gets pods that are running in the namespace")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Successfully retrieved pods")})
    ResponseEntity<List<PodDetails>> getRunningPods();

    @Operation(summary = "All Pods", description = "Gets all pods from the namespace")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Successfully retrieved pods")})
    ResponseEntity<List<PodDetails>> getAllPods();

}

