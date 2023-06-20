package com.kissmann.todosimple.services;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kissmann.todosimple.models.User;
import com.kissmann.todosimple.models.enums.ProfileEnum;
import com.kissmann.todosimple.repositories.UserRepository;
import com.kissmann.todosimple.security.UserSpringSecurity;
import com.kissmann.todosimple.services.exceptions.AuthorizationException;
import com.kissmann.todosimple.services.exceptions.DataBindingViolationException;
import com.kissmann.todosimple.services.exceptions.ObjectNotFoundException;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bcryptPasswordEncoder;

    public User getById( Long userId ) 
    {
        UserSpringSecurity userSpringSecurity = authenticated();
        if (!Objects.nonNull(userSpringSecurity)
                || !userSpringSecurity.hasRole(ProfileEnum.ADMIN) && !userId.equals(userSpringSecurity.getId()))
            throw new AuthorizationException("Access denied!");

        Optional<User> user = this.userRepository.findById( userId );
        
        return user.orElseThrow( () -> new ObjectNotFoundException( "User not found for id " +userId ));
    }

    @Transactional
    public User createUser( User user )
    {
        user.setId( null );
        user.setPassword( this.bcryptPasswordEncoder.encode( user.getPassword() ));
        user.setProfiles(Stream.of( ProfileEnum.USER.getCode() ).collect( Collectors.toSet() ));
        user = this.userRepository.save( user );
        return user;
    }    

    @Transactional
    public User updateUser( User user )
    {
        User newUser = getById( user.getId() );
        newUser.setPassword( user.getPassword());
        newUser.setPassword( this.bcryptPasswordEncoder.encode( user.getPassword() ));
        return this.userRepository.save( newUser );
    }

    public void deleteUser( Long userId )
    {
        getById( userId );
        try
        {
            this.userRepository.deleteById( userId );
        }
        catch( Exception e )
        {
            throw new DataBindingViolationException( "Não é possível excluir pois há tarefas relacionadas" );
        }

    }

    public static UserSpringSecurity authenticated() {
        try
        {
            return (UserSpringSecurity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        }
        catch( Exception e )
        {
            return null;
        }
    }
}
