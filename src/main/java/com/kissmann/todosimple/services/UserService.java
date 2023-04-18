package com.kissmann.todosimple.services;

import java.util.Optional;
import java.lang.RuntimeException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kissmann.todosimple.models.User;
import com.kissmann.todosimple.repositories.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User getById( Long userId ) 
    {
        Optional<User> user = this.userRepository.findById( userId );
        
        return user.orElseThrow( () -> new RuntimeException( "User not found for id " +userId ));
    }

    @Transactional
    public User createUser( User user )
    {
        user.setId( null );
        user = this.userRepository.save( user );
        return user;
    }    

    @Transactional
    public User updateUser( User user )
    {
        User newUser = getById( user.getId() );
        newUser.setPassword( user.getPassword());
        return this.userRepository.save( newUser );
    }

    public void delete( Long userId )
    {
        getById( userId );
        try
        {
            this.userRepository.deleteById( userId );
        }
        catch( Exception e )
        {
            throw new RuntimeException( "Não é possível excluir pois há tarefas relacionadas" );
        }

    }
}
