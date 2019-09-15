package com.example.demo.DataBase.im;

import com.example.demo.DataBase.ToDoItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface AddToDoItem extends JpaRepository<ToDoItem,Long> {
    @Query("select t from ToDoItem t where t.id = ?1")
    ToDoItem findById(Integer id);
    @Query(nativeQuery = true,value =("select * from todo_item where TO_DAYS(date) = TO_DAYS(?1)"))
    List<ToDoItem> findAllByDete(Date date);
}
