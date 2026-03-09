package drm.sel.showcase.controller;

import drm.sel.showcase.model.NewTaskPayload;
import drm.sel.showcase.model.Task;
import drm.sel.showcase.repository.TaskRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TasksRestControllerTest {

    @Mock
    TaskRepository taskRepository;
    @Mock
    MessageSource messageSource;
    @InjectMocks
    TasksRestController underTest;

    @Test
    @DisplayName("GET /api/tasks - returns HTTP-response with status 200 and body with all tasks")
    void handleGetAllTasks_ReturnsValidResponseEntity() {
        // given
        var tasks = List.of(
                new Task(UUID.randomUUID(), "Первая задача", false),
                new Task(UUID.randomUUID(), "Вторая задача", true)
        );
        doReturn(tasks).when(taskRepository).findAll();

        // when
        var responseEntity = underTest.handleGetAllTasks();

        // then
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, responseEntity.getHeaders().getContentType());
        assertEquals(tasks, responseEntity.getBody());
    }

    @Test
    void handleCreateTask_PayloadIsValid_ReturnsValidResponseEntity() {
        // given
        var details = "Третья задача";

        // when
        var responseEntity = underTest.handleCreateTask(
                new NewTaskPayload(details),
                // no matter what the value is
                UriComponentsBuilder.fromUriString("http://localhost:8080"),
                Locale.ENGLISH);

        // then
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, responseEntity.getHeaders().getContentType());
        if (responseEntity.getBody() instanceof Task task) {
            assertNotNull(task.id());
            assertEquals(details, task.details());
            assertFalse(task.completed());

            assertEquals(URI.create("http://localhost:8080/api/tasks/" + task.id()),
                    responseEntity.getHeaders().getLocation());

            verify(this.taskRepository).save(task);
        } else {
            assertInstanceOf(Task.class, responseEntity.getBody());
        }
        verifyNoMoreInteractions(this.taskRepository);
    }

    @Test
    void handleCreateTask_PayloadIsInvalid_ReturnsValidResponseEntity() {
        // given
        var details = "   "; // blank
        var local = Locale.US;
        var errorMessage = "Details must be set";

        doReturn(errorMessage).when(this.messageSource).getMessage(
                "tasks.create.details.errors.not_set",
                new Object[0],
                local);

    }
}