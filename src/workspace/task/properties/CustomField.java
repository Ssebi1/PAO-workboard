package workspace.task.properties;

public class CustomField {
    private Integer id;
    private String title;
    private String value;

    public CustomField(Integer id, String title, String value) {
        this.id = id;
        this.title = title;
        this.value = value;
    }

    public CustomField(Integer id, String title) {
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

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
