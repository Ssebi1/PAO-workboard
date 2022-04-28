package workspace.task;

import workspace.task.Task;

public class Subtask extends Task {
    private Integer parentTaskId;

    public Subtask(Integer parentTaskId, Integer id, String title) {
        super(id, title);
        this.parentTaskId = parentTaskId;
    }

    public Integer getParentTaskId() {
        return parentTaskId;
    }

    public void setParentTaskId(Integer parentTaskId) {
        this.parentTaskId = parentTaskId;
    }
}
