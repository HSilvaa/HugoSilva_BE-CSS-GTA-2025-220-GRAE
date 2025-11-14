package com.todo.domain.DTO;

public class TodoRequest {
    private Long id;
    private String task;
    private Integer priority;

    public TodoRequest(Long id, String task, Integer priority){
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
