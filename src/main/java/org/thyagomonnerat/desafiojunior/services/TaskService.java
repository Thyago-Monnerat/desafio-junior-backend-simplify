package org.thyagomonnerat.desafiojunior.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thyagomonnerat.desafiojunior.dtos.TaskDto;
import org.thyagomonnerat.desafiojunior.exceptions.TaskAlreadyExistsException;
import org.thyagomonnerat.desafiojunior.exceptions.TaskNotFoundException;
import org.thyagomonnerat.desafiojunior.mappers.TaskMapper;
import org.thyagomonnerat.desafiojunior.models.TaskModel;
import org.thyagomonnerat.desafiojunior.repositories.TaskRepository;

import java.util.List;

@AllArgsConstructor
@Service
@Slf4j
public class TaskService {
    private final TaskRepository repository;
    private final TaskMapper mapper;

    @Transactional(readOnly = true)
    protected TaskModel getTask(long id) {
        log.info("Attempting to get the task with the id: {}", id);

        try {
            TaskModel task = this.repository.findById(id).orElseThrow(() -> new TaskNotFoundException("Task not found"));
            log.info("Got the task with id {} successfully", id);

            return task;
        } catch (TaskNotFoundException e) {
            log.warn("Attempted to get an inexistent task by id. The id was {}", id);
            throw e;
        } catch (Exception e) {
            log.error("Error to get task by id: {};", id, e);
            throw new RuntimeException("Couldn't get the task with the id " + id, e);
        }
    }

    @Transactional
    protected TaskModel saveTask(TaskModel model) {
        log.info("Attempting to create new task");

        try {
            TaskModel savedTask = this.repository.save(model);
            log.info("New task was saved successfully");

            return savedTask;
        } catch (Exception e) {
            log.error("Couldn't create a new task", e);
            throw new RuntimeException("Couldn't create a new task", e);
        }
    }

    @Transactional(readOnly = true)
    public List<TaskDto> getAll() {
        log.info("Getting all tasks");
        return this.repository.findAll().stream().map(mapper::fromModelToDto).toList();
    }

    @Transactional
    public TaskDto addTask(TaskDto dto) {
        this.repository.findByName(dto.name()).ifPresent((task) -> {
            log.warn("The task with the name {} already exists.", task.getName());
            throw new TaskAlreadyExistsException("Task name already exists");
        });

        TaskModel savedTask = saveTask(mapper.fromDtoToModel(dto));
        return mapper.fromModelToDto(savedTask);
    }

    @Transactional
    public TaskDto updateTask(long id, TaskDto dto) {
        log.info("Attempting to update the task with the id: {}", id);

        try {
            TaskModel model = getTask(id);

            mapper.updateModelFromDto(dto, model);
            log.info("The task {} was updated successfully", id);

            TaskModel savedTask = saveTask(model);

            return mapper.fromModelToDto(savedTask);
        } catch (Exception e) {
            log.error("Failed to update the task with the id {}", id);
            throw new RuntimeException("Couldn't update the task " + id, e);
        }
    }

    @Transactional
    public TaskDto deleteTask(long id) {
        log.info("Attempting to delete the task with the id {}", id);

        try {
            TaskModel model = getTask(id);

            this.repository.delete(model);
            log.info("The task with the id {} was deleted successfully", id);

            return mapper.fromModelToDto(model);
        } catch (Exception e) {
            log.error("Failed to delete the task with the id {}", id);
            throw new RuntimeException("Couldn't delete the task " + id, e);
        }
    }

}
