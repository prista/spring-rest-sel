package drm.sel.showcase;

import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("api/tasks")
public class TasksRestController {

    private final TaskRepository taskRepository;

    private final MessageSource messageSource; // managed by Spring, to get localized messages


    public TasksRestController(TaskRepository taskRepository,
                               MessageSource messageSource) {
        this.taskRepository = taskRepository;
        this.messageSource = messageSource;
    }

    @GetMapping
    public ResponseEntity<List<Task>> handleGetAllTasks() {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(this.taskRepository.findAll());
    }

    @GetMapping("{id}")
    // all parameters we get from the request are Strings, but Spring can convert them
    public ResponseEntity<Task> handleGetTaskById(@PathVariable("id") UUID id) {
        // ResponseEntity.of() - takes Optional.
        // 200 if present, 404 if the value is empty
        return ResponseEntity.of(this.taskRepository.findById(id));
    }

    @PostMapping
    public ResponseEntity<?> handleCreateTask( // ? - because response can be either Task or ErrorsPresentation
            @RequestBody NewTaskPayload payload,
            UriComponentsBuilder uriComponentsBuilder,
            Locale locale) { // locale is needed to get localized messages from the message source
        // validation
        if (payload.details() == null || payload.details().isBlank()) {
            var l10nMessage = this.messageSource.getMessage(
                    "tasks.create.details.errors.not_set",
                    new Object[0], locale);
            return ResponseEntity.badRequest()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new ErrorsPresentation(
                            List.of(l10nMessage)
                    ));
        }
        var task = new Task(payload.details());
        this.taskRepository.save(task);
        return ResponseEntity.created(uriComponentsBuilder
                        .path("api/tasks/{taskId}")
                        .build(Map.of("taskId", task.id()))) // location header with the URL of the created resource
                .contentType(MediaType.APPLICATION_JSON)
                .body(task);
    }

}
