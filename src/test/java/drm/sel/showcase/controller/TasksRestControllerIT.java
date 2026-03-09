package drm.sel.showcase.controller;

import drm.sel.showcase.model.Task;
import drm.sel.showcase.repository.InMemoryTaskRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(printOnlyOnFailure = false)
class TasksRestControllerIT {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    InMemoryTaskRepository taskRepository;

    @Test
    void handleGetAllTasks_ReturnsValidResponseEntity() throws Exception {
        // given
        var requestBuilder = get("/api/tasks");
        this.taskRepository.getTasks().addAll(
                List.of(
                        new Task(
                                UUID.fromString("b2715f0d-8459-4256-a8bc-078ceb6f04fd"),
                                "Первая задача",
                                false
                        ),
                        new Task(
                                UUID.fromString("e2ac4591-51a0-424d-94a3-0b930201589f"),
                                "Вторая задача",
                                true
                        )
                )
        );

        // when
        this.mockMvc.perform(requestBuilder)
        // then
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().json("""
                                [
                                    {
                                        "id": "b2715f0d-8459-4256-a8bc-078ceb6f04fd",
                                        "details": "Первая задача",
                                        "completed": false
                                    },
                                    {
                                        "id": "e2ac4591-51a0-424d-94a3-0b930201589f",
                                        "details": "Вторая задача",
                                        "completed": true
                                    }
                                ]
                                """)
                );
    }

    @Test
    void handleCreateTask_PayloadIsValid_ReturnsValidResponseEntity() throws Exception {

    }

}