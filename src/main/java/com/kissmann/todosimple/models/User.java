package com.kissmann.todosimple.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

@Entity
@Table( name = User.TABLE_NAME )
public class User 
{
    public interface CreateUser{}
    public interface UpdateUser{}
    public static final String TABLE_NAME = "user";
    
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    @Column( name = "id", unique = true )
    private Long id;

    
    @Column( name = "username", unique = true, nullable = false, length = 100)
    @NotNull( groups = CreateUser.class )
    @NotEmpty( groups = CreateUser.class )
    @Size( groups = CreateUser.class, min = 2, max = 100)
    private String username;

    @JsonProperty( access = Access.WRITE_ONLY)
    @Column( name = "password", nullable = false, length = 60)
    @NotNull( groups = { CreateUser.class, UpdateUser.class} )
    @NotEmpty( groups = { CreateUser.class, UpdateUser.class}  )
    @Size( groups = { CreateUser.class, UpdateUser.class}, min = 8, max =  60)
    private String password;
    
    // private List<Task> tasks = new ArrayList<Task>();
    
    public User(){

    }

    public User( Long id, String username, String password ){
        this.id = id;
        this.username = username;
        this.password = password;
    }
    
    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return this.username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public boolean equals( Object obj ){
        if( obj == this) return true;
        if( obj == null ) return false;
        if( !(obj instanceof User)) return false;

        User user = ( User ) obj; 

        if( this.id == null )
        {
            if( user.id != null ) return false;
            else if( !this.id.equals(user.id)) return false;
        }
        return Objects.equals( id, user.id) && Objects.equals(username, user.username) && Objects.equals( password, user.password );
    }

    public int hashCode(){
        final int prime = 31;
        int result = 1;
        result = prime * result + ( this.id == null ? 0 : this.id.hashCode());
        return result;
    }
    

}
