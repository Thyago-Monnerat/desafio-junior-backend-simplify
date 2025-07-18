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

@Slf4j
@AllArgsConstructor
@Service
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

    @Transactional(readOnly = true)
    protected void checkDupeTask(String name, Long id) {
        log.info("Checking duplicate task names. Name: {}", name);

        this.repository.findByName(name).ifPresent((task) -> {
            if(id == null || !id.equals(task.getId())){
                log.warn("The task with the name {} already exists.", task.getName());
                throw new TaskAlreadyExistsException("Task name already exists");
            }
        });
        log.info("No tasks with the name: {} was found", name);
    }

    @Transactional(readOnly = true)
    public List<TaskDto> getAll() {
        log.info("Getting all tasks");
        return this.repository.findAll().stream().map(mapper::fromModelToDto).toList();
    }

    @Transactional
    public TaskDto addTask(TaskDto dto) {
        log.info("Attempting to create new task");

        try {
            TaskModel model = mapper.fromDtoToModel(dto);
            checkDupeTask(dto.name(), null);
            TaskModel savedTask = this.repository.save(model);
            log.info("New task was saved successfully");

            return mapper.fromModelToDto(savedTask);
        } catch (Exception e) {
            log.error("Couldn't create a new task", e);
            throw new RuntimeException("Couldn't create a new task", e);
        }
    }

    @Transactional
    public TaskDto updateTask(long id, TaskDto dto) {
        log.info("Attempting to update the task with the id: {}", id);

        try {
            TaskModel model = getTask(id);

            checkDupeTask(dto.name(), id);
            mapper.updateModelFromDto(dto, model);

            log.info("The task {} was updated successfully", id);

            return mapper.fromModelToDto(model);
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
