package org.thyagomonnerat.desafiojunior.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thyagomonnerat.desafiojunior.dtos.TaskDto;
import org.thyagomonnerat.desafiojunior.mappers.TaskMapper;
import org.thyagomonnerat.desafiojunior.models.TaskModel;
import org.thyagomonnerat.desafiojunior.repositories.TaskRepository;

import java.util.List;

@AllArgsConstructor
@Service
public class TaskService {
    private final TaskRepository repository;
    private final TaskMapper mapper;

    @Transactional(readOnly = true)
    public TaskModel getTask(long id) {
        return this.repository.findById(id).orElseThrow(() -> new RuntimeException("Task not found"));
    }

    @Transactional(readOnly = true)
    public List<TaskDto> getAll() {
        return this.repository.findAll().stream().map(mapper::fromModelToDto).toList();
    }

    @Transactional
    public TaskDto addTask(TaskDto dto) {
        try {
            TaskModel model = this.repository.save(mapper.fromDtoToModel(dto));
            return mapper.fromModelToDto(model);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public TaskDto updateTask(long id, TaskDto dto) {
        try {
            TaskModel model = getTask(id);

            mapper.updateModelFromDto(dto, model);

            TaskModel newModel = this.repository.save(model);

            return mapper.fromModelToDto(newModel);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public TaskDto deleteTask(long id) {
        try {
            TaskModel model = getTask(id);

            this.repository.delete(model);

            return mapper.fromModelToDto(model);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
