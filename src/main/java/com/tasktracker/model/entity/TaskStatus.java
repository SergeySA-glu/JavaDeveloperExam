package com.tasktracker.model.entity;

public enum TaskStatus {
    PLANNED("Запланирована"),
    IN_PROGRESS("В работе"),
    COMPLETED("Сделана");

    private final String label;

    TaskStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
