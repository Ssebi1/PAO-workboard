package workspace.task.properties;

import java.util.Date;

public class Comment {
    private Integer id;
    private Date date;
    private Integer userId;
    private String content;

    public Comment(Integer id, Date date, Integer userId, String content) {
        this.id = id;
        this.date = date;
        this.userId = userId;
        this.content = content;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
