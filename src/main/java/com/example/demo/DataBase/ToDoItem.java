package com.example.demo.DataBase;

import javax.annotation.sql.DataSourceDefinition;
import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "todoItem")
public class ToDoItem implements Serializable {
    @Id
    @Column(name = "id",unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "title")
    private String title;
    @Column(name = "date")
    private String date;
    @Column(name = "create_at")
    private String create_at;
    @Column(name = "uuid")
    private String uuid;
    @Column(name = "state")
    private Integer state;
    @Column(name = "delete_state")
    private Integer delete_state;

    public Integer getDelete_state() {
        return delete_state;
    }

    public void setDelete_state(Integer delete_state) {
        this.delete_state = delete_state;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCreate_at() {
        return create_at;
    }

    public void setCreate_at(String create_at) {
        this.create_at = create_at;
    }
}
