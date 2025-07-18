package org.thyagomonnerat.desafiojunior.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.thyagomonnerat.desafiojunior.dtos.TaskDto;
import org.thyagomonnerat.desafiojunior.services.TaskService;

import java.util.List;

@RestController
@RequestMapping("task")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService service;

    @GetMapping
    @Operation(operationId = "getTasks", summary = "Retrieve all tasks", responses = {
            @ApiResponse(responseCode = "200", description = "Retrieve all tasks successfully"),
            @ApiResponse(responseCode = "500", description = "Unexpected error")
    })
    public ResponseEntity<List<TaskDto>> getTasks() {
        return ResponseEntity.ok(service.getAll());
    }

    @Operation(operationId = "addTask", summary = "Create and save a new task in the database", requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            required = true,
            description = "Task informations to be created"
    ), responses = {
            @ApiResponse(responseCode = "201", description = "Created a new task successfully"),
            @ApiResponse(responseCode = "409", description = "A task with the duplicate name already exists"),
            @ApiResponse(responseCode = "500", description = "Unexpected error")
    })
    @PostMapping
    public ResponseEntity<TaskDto> addTask(@RequestBody TaskDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.addTask(dto));
    }

    @Operation(operationId = "updateTask", summary = "Update a task field by the task id", requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            required = true,
            description = "The values to update the task"
    ), parameters = {
            @Parameter(in = ParameterIn.PATH, name = "id", description = "Task id")
    }, responses = {
            @ApiResponse(responseCode = "200", description = "Created a new task successfully"),
            @ApiResponse(responseCode = "401", description = "The task was not found"),
            @ApiResponse(responseCode = "409", description = "A task with the duplicate name already exists"),
            @ApiResponse(responseCode = "500", description = "Unexpected error")
    })
    @PatchMapping("{id}")
    public ResponseEntity<TaskDto> updateTask(@PathVariable long id, @RequestBody TaskDto dto) {
        return ResponseEntity.ok(service.updateTask(id, dto));
    }

    @Operation(operationId = "deleteTask", summary = "Delete a task by its id",
            parameters = {
                    @Parameter(in = ParameterIn.PATH, name = "id", description = "Task id")
            }, responses = {
            @ApiResponse(responseCode = "200", description = "Deleted the task successfully"),
            @ApiResponse(responseCode = "404", description = "The task to delete was not found by its id"),
            @ApiResponse(responseCode = "500", description = "Unexpected error")
    })
    @DeleteMapping("{id}")
    public ResponseEntity<TaskDto> deleteTask(@PathVariable long id) {
        return ResponseEntity.ok(service.deleteTask(id));
    }
}
