package drm.sel.showcase.controller;

import drm.sel.showcase.model.ErrorsPresentation;
import drm.sel.showcase.model.NewTaskPayload;
import drm.sel.showcase.model.Task;
import drm.sel.showcase.repository.TaskRepository;
import drm.sel.showcase.security.ApplicationUser;
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
import java.util.Optional;
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
        var user = new ApplicationUser(UUID.randomUUID(), "user1", "password1");

        var tasks = List.of(
                new Task(UUID.randomUUID(), "Первая задача", false, user.id()),
                new Task(UUID.randomUUID(), "Вторая задача", true, user.id())
        );
        doReturn(tasks).when(taskRepository).findByApplicationUserId(user.id());

        // when
        var responseEntity = underTest.handleGetAllTasks(user);

        // then
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, responseEntity.getHeaders().getContentType());
        assertEquals(tasks, responseEntity.getBody());
    }

    @Test
    void handleCreateTask_PayloadIsValid_ReturnsValidResponseEntity() {
        // given
        var user = new ApplicationUser(UUID.randomUUID(), "user1", "password1");
        var details = "Третья задача";

        // when
        var responseEntity = underTest.handleCreateTask(
                user,
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
            assertEquals(user.id(), task.applicantUserId());

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
        var user = new ApplicationUser(UUID.randomUUID(), "user1", "password1");
        var details = "   "; // blank
        var locale = Locale.US;
        var errorMessage = "Details must be set";

        doReturn(errorMessage).when(this.messageSource).getMessage(
                "tasks.create.details.errors.not_set",
                new Object[0],
                locale);

        // when
        var responseEntity = underTest.handleCreateTask(
                user,
                new NewTaskPayload(details),
                UriComponentsBuilder.fromUriString("http://localhost:8080"),
                locale);

        // then
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, responseEntity.getHeaders().getContentType());
        assertEquals(new ErrorsPresentation(List.of(errorMessage)), responseEntity.getBody());

        verifyNoInteractions(this.taskRepository);
    }

    @Test
    void handleGetTaskById_ReturnsValidResponseEntity() {
        // given
        var user = new ApplicationUser(UUID.randomUUID(), "user1", "password1");
        var id = UUID.randomUUID();
        var details = "Task";
        var task = new Task(id, details, false, user.id());
        doReturn(Optional.of(task)).when(this.taskRepository).findById(id);

        // when
        var responseEntity = underTest.handleGetTaskById(id);

        // then
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(task, responseEntity.getBody());
    }
}