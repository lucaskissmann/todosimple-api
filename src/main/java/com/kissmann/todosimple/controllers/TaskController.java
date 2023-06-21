package com.kissmann.todosimple.controllers;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.kissmann.todosimple.models.Task;
import com.kissmann.todosimple.services.TaskService;

@RestController
@RequestMapping("/task")
@Validated
public class TaskController {
    
    @Autowired
    private TaskService taskService;

    @GetMapping("/{id}")
    public ResponseEntity<Task> getById( @PathVariable Long id ){
        Task task = this.taskService.getById( id );
        return ResponseEntity.ok().body( task );
    }

    @GetMapping("/user")
    public ResponseEntity<List<Task>> getAllByUser(){
        List<Task> tasks = this.taskService.getAllByUser();
        return ResponseEntity.ok().body( tasks );
    }

    @PostMapping
    @Validated
    public ResponseEntity<Void> create( @Valid @RequestBody Task task ){
        this.taskService.createTask( task );
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand( task.getId()).toUri();
        return ResponseEntity.created( uri ).build();
    }

    @PutMapping("/{id}")
    @Validated
    public ResponseEntity<Void> update( @Valid @RequestBody Task task, @PathVariable Long id ){
        task.setId( id );
        this.taskService.updateTask( task );
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete( @PathVariable Long id )
    {
        this.taskService.deleteTask( id );
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<List<Task>> getAllTasks()
    {
        List<Task> tasks = this.taskService.getAllTasks();
        return ResponseEntity.ok().body( tasks );
    }

    
}
