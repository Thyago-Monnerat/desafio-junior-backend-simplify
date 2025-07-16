package org.thyagomonnerat.desafiojunior.controllers;

import lombok.RequiredArgsConstructor;
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
    public ResponseEntity<List<TaskDto>> getTasks() {
        return ResponseEntity.ok(service.getAll());
    }

    @PostMapping
    public ResponseEntity<TaskDto> addTask(@RequestBody TaskDto dto) {
        return ResponseEntity.ok(service.addTask(dto));
    }

    @PatchMapping("{id}")
    public ResponseEntity<TaskDto> getTasksById(@PathVariable long id, @RequestBody TaskDto dto) {
        return ResponseEntity.ok(service.updateTask(id, dto));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<TaskDto> deleteTask(@PathVariable long id) {
        return ResponseEntity.ok(service.deleteTask(id));
    }
}
