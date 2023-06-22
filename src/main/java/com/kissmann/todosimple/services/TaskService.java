package com.kissmann.todosimple.services;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kissmann.todosimple.models.Task;
import com.kissmann.todosimple.models.User;
import com.kissmann.todosimple.models.enums.ProfileEnum;
import com.kissmann.todosimple.models.projection.TaskProjection;
import com.kissmann.todosimple.repositories.TaskRepository;
import com.kissmann.todosimple.security.UserSpringSecurity;
import com.kissmann.todosimple.services.exceptions.AuthorizationException;
import com.kissmann.todosimple.services.exceptions.DataBindingViolationException;
import com.kissmann.todosimple.services.exceptions.ObjectNotFoundException;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserService userService;

    public Task getById( Long id ) 
    {
        Task task = this.taskRepository.findById( id ).orElseThrow( () -> new ObjectNotFoundException(" Task not found for id " + id + ", Tipo: " + Task.class.getName() ));

        UserSpringSecurity userSpringSecurity = UserService.authenticated();
        if( Objects.isNull( userSpringSecurity ) || !userSpringSecurity.hasRole( ProfileEnum.ADMIN ) && !userHasTask( userSpringSecurity, task ))
            throw new AuthorizationException( "Access denied!" );

        return task;
    }

    public List<TaskProjection> getAllByUser() 
    {
        UserSpringSecurity userSpringSecurity = UserService.authenticated();
        if( Objects.isNull( userSpringSecurity ) )
            throw new AuthorizationException( "Access denied!" );

        List<TaskProjection> tasks = this.taskRepository.findByUser_Id( userSpringSecurity.getId() );
        return tasks;
    }

    @Transactional
    public Task createTask( Task task ) 
    {

        UserSpringSecurity userSpringSecurity = UserService.authenticated();
        if( Objects.isNull( userSpringSecurity ) )
            throw new AuthorizationException( "Access denied!" );

        User user = this.userService.getById( userSpringSecurity.getId() );
        task.setId( null );
        task.setUser( user );
        task = this.taskRepository.save( task );

        return task;
    }

    @Transactional
    public Task updateTask( Task task ) 
    {
        Task newTask = getById( task.getId() );
        newTask.setDescription( task.getDescription() );
        return this.taskRepository.save( newTask );
    }

    public void deleteTask( Long id ) 
    {
        getById( id );
        try
        {
            this.taskRepository.deleteById( id );
        }
        catch( Exception e )
        {
            throw new DataBindingViolationException( "Não é possível excluir pois há tarefas relacionadas" );
        }
    }

    private boolean userHasTask( UserSpringSecurity userSpringSecurity, Task task )
    {
        return  task.getUser().getId().equals(userSpringSecurity.getId() );
    }

    public List<Task> getAllTasks() 
    {
        List<Task> tasks = this.taskRepository.findAll();

        return tasks;
    }

}
