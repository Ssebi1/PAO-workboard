package workspace.task;

import workspace.task.properties.Comment;
import workspace.task.properties.CustomField;
import workspace.task.properties.Status;
import workspace.task.properties.Tag;

import java.util.Date;
import java.util.List;
import java.util.Set;

abstract public class Task {
    private Integer id;
    private String title;
    private Date dueDate;
    private Status status;
    private Set<Tag> tags;
    private List<Comment> comments;
    private List<CustomField> customFields;

    public Task(Integer id, String title) {
        this.id = id;
        this.title = title;
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

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public List<CustomField> getCustomFields() {
        return customFields;
    }

    public void setCustomFields(List<CustomField> customFields) {
        this.customFields = customFields;
    }

    abstract public Integer getParentTaskId();

    abstract public void setParentTaskId(Integer parentTaskId);
}
