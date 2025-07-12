package dev.srivatsan.kubeapi.model;

import lombok.Data;

@Data
public class ErrorMessage {
    private String message;
    private String reason;
}
