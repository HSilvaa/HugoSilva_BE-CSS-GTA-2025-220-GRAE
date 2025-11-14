package com.todo.infrastructure.persistence.entity;

import javax.persistence.*;

@Entity
@Table(name = "TODO")
public class TodoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String task;
    private Integer priority;

    public TodoEntity() {}

    public TodoEntity(Long id, String task, Integer priority){
        this.id = id;
        this.task = task;
        this.priority = priority;
    }

    public Long getId() {return id; }
    public void setId(Long idToSet) {id = idToSet; }

    public String getTask() {return task; }
    public void setTask(String taskToSet) {task = taskToSet; }

    public Integer getPriority() {return priority; }
    public void setPriority(Integer priorityToSet) {priority = priorityToSet; }
}
