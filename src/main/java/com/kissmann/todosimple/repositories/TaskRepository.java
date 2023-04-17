package com.kissmann.todosimple.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.kissmann.todosimple.models.Task;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    
    List<Task> findByUser_Id( Long id );

    // @Query( value =  "SELECT t from Task t where t.user.id = :id ")
    // List<Task> findByUser_Id( @Param "id" Long id );

    // @Query( value = "Select * from task t where t.user_id = :id ", nativeQuery = true)
    // List<Task> findByUser_Id (@Param "id" Long id );
}
