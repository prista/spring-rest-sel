package drm.sel.showcase;

import org.springframework.stereotype.Repository;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository // to register as a bean in the application context
public class InMemoryTaskRepository implements TaskRepository {

    // rest-service is a multithreaded env,
    // better to use a thread-safe collection instead
    private final List<Task> tasks = new LinkedList<>() {{
        this.add(new Task("Первая задача"));
        this.add(new Task("Вторая задача"));
    }};

    @Override
    public List<Task> findAll() {
        return this.tasks;
    }

    @Override
    public void save(Task task) {
        this.tasks.add(task);
    }

    @Override
    public Optional<Task> findById(UUID id) {
        return this.tasks.stream()
                .filter(t -> t.id().equals(id))
                .findFirst();
    }

}
