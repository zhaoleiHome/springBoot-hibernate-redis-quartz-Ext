package com.example.demo.DataBase.im;

import com.example.demo.DataBase.ToDoItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

public interface AddToDoItem extends JpaRepository<ToDoItem,Long> {
    @Query("select t from ToDoItem t where t.uuid = ?1")
    ToDoItem findById(String uuid);
    @Query(nativeQuery = true,value =("select * from todo_item where TO_DAYS(date) = TO_DAYS(?1) and state=?2 and delete_state=0"))
    List<ToDoItem> findAllByDate(String date,Integer state);
    @Query("update ToDoItem t set t.state=?2 where t.uuid=?1")
    @Modifying
    @Transactional
    int changeToDoItemState(String uuid,Integer state);
    @Query("select t from ToDoItem t where t.state=?1 and t.delete_state = 0")
    List<ToDoItem> findAllByState(Integer state);
    @Query("update ToDoItem t set t.delete_state = 1 where t.uuid=?1")
    @Modifying
    @Transactional
    int deleteToDoItemByUUID(String uuid);

    // TODO: 2019/9/18 搜索框使用
    List<ToDoItem> searchToDoItem(String text,String state);
}
