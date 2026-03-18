package drm.sel.showcase.model;

import java.util.UUID;

public record Task(UUID id, String details, boolean completed, UUID applicantUserId) {

    public Task(String details, UUID id) {
        this(UUID.randomUUID(), details, false, id);
    }
}
