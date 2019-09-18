package com.example.demo.DataBase.im;

import com.example.demo.DataBase.ToDoNote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface AddToDoNote extends JpaRepository<ToDoNote,Long> {
    @Query("select count(n.id) from ToDoNote n where n.parent_id = ?1")
    int findToDoNoteCount(String parent_id);
    @Query("update ToDoNote t set t.content=?1 where t.parent_id=?2")
    @Modifying
    @Transactional
    int updateTodDoItem(String content,String parent_id);
    @Query("select n from ToDoNote n where n.parent_id=?1")
    ToDoNote findByParent_id(String parent_id);
}
