package org.example;

import java.time.LocalDateTime;
import java.util.List;

public class AuditLog {
    private Long logId;
    private String action;
    private Long userId;
    private String entityType;
    private Long entityId;
    private String oldValue;
    private String newValue;
    private LocalDateTime timestamp;

    public static void logAction(String action, Long userId, String entityType, Long entityId) {

    }

    public List<AuditLog> getLogsByUser(Long userId, LocalDateTime start, LocalDateTime end) {
        return null;
    }

    public List<AuditLog> getLogsByEntity(String entityType, Long entityId) {
        return null;
    }
}
