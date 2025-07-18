package org.thyagomonnerat.desafiojunior;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.thyagomonnerat.desafiojunior.dtos.TaskDto;
import org.thyagomonnerat.desafiojunior.exceptions.TaskAlreadyExistsException;
import org.thyagomonnerat.desafiojunior.exceptions.TaskNotFoundException;
import org.thyagomonnerat.desafiojunior.mappers.TaskMapper;
import org.thyagomonnerat.desafiojunior.models.TaskModel;
import org.thyagomonnerat.desafiojunior.repositories.TaskRepository;
import org.thyagomonnerat.desafiojunior.services.TaskService;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DesafiojuniorApplicationTests {

    @Mock
    TaskRepository repository;

    @Mock
    TaskMapper mapper;

    @InjectMocks
    TaskService service;

    TaskModel task;
    TaskDto dto;

    @BeforeEach
    void setup() {
        task = new TaskModel(1L, "test", "description test", false, 2);
        dto = new TaskDto(1L, "testDto", "Description dto", false, 3);
    }

    @Nested
    @DisplayName("Add a task")
    class whenAddingTask {
        @Test
        @DisplayName("Should create and save a new task in database")
        void shouldCreateANewTask() {
            when(repository.findByName(dto.name())).thenReturn(Optional.empty());
            when(repository.save(task)).thenReturn(task);
            when(mapper.fromDtoToModel(dto)).thenReturn(task);
            when(mapper.fromModelToDto(task)).thenReturn(dto);

            TaskDto result = service.addTask(dto);

            verify(repository).save(task);
            verify(mapper).fromDtoToModel(dto);
            verify(mapper).fromModelToDto(task);

            Assertions.assertNotNull(result);
            Assertions.assertEquals(dto.name(), result.name());
        }

        @Test
        @DisplayName("Should throw a TaskAlreadyExistsException when task name is duplicated")
        void shouldThrowExceptionWhenAddingDuplicatedTask() {
            when(repository.findByName(dto.name())).thenReturn(Optional.of(task));

            Assertions.assertThrows(TaskAlreadyExistsException.class, () -> {
                service.addTask(dto);
            });

            verify(repository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("Update a task")
    class whenUpdatingTask {
        @Test
        @DisplayName("Should update an existent task")
        void shouldUpdateATask() {
            when(repository.findByName(dto.name())).thenReturn(Optional.of(task));
            when(repository.findById(dto.id())).thenReturn(Optional.of(task));
            when(mapper.fromModelToDto(task)).thenReturn(dto);
            doNothing().when(mapper).updateModelFromDto(dto, task);

            TaskDto result = service.updateTask(1L, dto);

            verify(mapper).updateModelFromDto(dto, task);
            verify(mapper).fromModelToDto(task);

            Assertions.assertNotNull(result);
            Assertions.assertEquals(dto.name(), result.name());
        }

        @Test
        @DisplayName("Should throw a TaskAlreadyExistsException when task name is duplicated")
        void shouldThrowExceptionWhenUpdatingToDuplicatedName() {
            when(repository.findByName(dto.name())).thenReturn(Optional.of(task));

            Assertions.assertThrows(TaskAlreadyExistsException.class, () -> {
                service.updateTask(2L, dto);
            });

            verify(repository, never()).save(any());
        }

        @Test
        @DisplayName("Should throw a TaskNotFoundException when task id is not found")
        void shouldThrowExceptionWhenUpdatingToInexistentId() {
            when(repository.findById(anyLong())).thenReturn(Optional.empty());

            Assertions.assertThrows(TaskNotFoundException.class, () -> {
                service.updateTask(1L, dto);
            });

            verify(repository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("Delete a task")
    class whenDeletingTask{
        @Test
        @DisplayName("Should delete an existent task")
        void shouldDeleteATask() {
            when(repository.findById(dto.id())).thenReturn(Optional.of(task));
            when(mapper.fromModelToDto(task)).thenReturn(dto);

            service.deleteTask(1L);

            verify(mapper).fromModelToDto(task);
            verify(repository).delete(task);
            verify(repository).findById(anyLong());
        }

        @Test
        @DisplayName("Should throw a TaskNotFoundException when task id is not found")
        void shouldThrowExceptionWhenDeletingToInexistentId() {
            when(repository.findById(anyLong())).thenReturn(Optional.empty());

            Assertions.assertThrows(TaskNotFoundException.class, () -> {
                service.deleteTask(1L);
            });

            verify(repository, never()).delete(any());
        }

    }


}
