package com.autonest.enums;

import java.util.Set;

/**
 * ServiceStatus enum with abstract canTransitionTo() method.
 * Each constant defines its own valid transitions, enforcing the state machine.
 */
public enum ServiceStatus {

    PENDING {
        @Override
        public boolean canTransitionTo(ServiceStatus target) {
            return Set.of(IN_PROGRESS, CANCELLED).contains(target);
        }

        @Override
        public String getDisplayLabel() {
            return "Pending";
        }
    },

    IN_PROGRESS {
        @Override
        public boolean canTransitionTo(ServiceStatus target) {
            return Set.of(COMPLETED, CANCELLED).contains(target);
        }

        @Override
        public String getDisplayLabel() {
            return "In Progress";
        }
    },

    COMPLETED {
        @Override
        public boolean canTransitionTo(ServiceStatus target) {
            return false; // Terminal state
        }

        @Override
        public String getDisplayLabel() {
            return "Completed";
        }
    },

    CANCELLED {
        @Override
        public boolean canTransitionTo(ServiceStatus target) {
            return false; // Terminal state
        }

        @Override
        public String getDisplayLabel() {
            return "Cancelled";
        }
    };

    public abstract boolean canTransitionTo(ServiceStatus target);

    public abstract String getDisplayLabel();

    /**
     * Uses switch expression to return a CSS class name for the given status.
     */
    public String getCssClass() {
        return switch (this) {
            case PENDING -> "badge-yellow";
            case IN_PROGRESS -> "badge-blue";
            case COMPLETED -> "badge-green";
            case CANCELLED -> "badge-red";
        };
    }

    public boolean isTerminal() {
        return this == COMPLETED || this == CANCELLED;
    }
}
