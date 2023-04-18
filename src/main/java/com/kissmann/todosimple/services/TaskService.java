package com.kissmann.todosimple.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kissmann.todosimple.models.Task;
import com.kissmann.todosimple.models.User;
import com.kissmann.todosimple.repositories.TaskRepository;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserService userService;

    public Task getById( Long id ) {
        Optional<Task> task = this.taskRepository.findById( id );

        return task.orElseThrow( () -> new RuntimeException(" Task not found for id " + id ));
    }

    @Transactional
    public Task create( Task task ) {
        User user = this.userService.getById( task.getUser().getId() );
        task.setId( (Long) null );
        task.setUser( user );
        return this.taskRepository.save( task );
    }

    @Transactional
    public Task update( Task task ) {
        Task newTask = getById( task.getId() );
        newTask.setDescription( task.getDescription() );
        return this.taskRepository.save( newTask );
    }

    public void delete( Long id ) {
        getById( id );
        try
        {
            this.taskRepository.deleteById( id );
        }
        catch( Exception e )
        {
            throw new RuntimeException( "Não é possível excluir pois há tarefas relacionadas" );
        }
}
