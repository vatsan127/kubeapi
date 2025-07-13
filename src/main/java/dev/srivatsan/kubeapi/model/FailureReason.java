package dev.srivatsan.kubeapi.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FailureReason {

    private String containerName;
    private String status;
    private String message;

}
