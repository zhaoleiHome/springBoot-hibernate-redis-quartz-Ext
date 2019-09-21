package com.example.demo.DataBase.im;

import com.example.demo.DataBase.TaskTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TaskNote extends JpaRepository<TaskTable,Long> {
    @Query("select t from TaskTable t where t.task_name =?1")
    TaskTable findTaskNote(String task_name);
}
