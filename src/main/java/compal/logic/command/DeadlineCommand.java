package compal.logic.command;

import compal.logic.command.exceptions.CommandException;
import compal.model.tasks.Deadline;
import compal.model.tasks.Task;
import compal.model.tasks.TaskList;

public class DeadlineCommand extends Command {

    private String description;
    private Task.Priority priority;
    private String date;
    private String endTime;

    public DeadlineCommand(String description, Task.Priority priority, String date, String endTime) {
        this.description = description;
        this.priority = priority;
        this.date = date;
        this.endTime = endTime;
    }

    @Override
    public CommandResult commandExecute(TaskList task) throws CommandException {
        task.addTask(new Deadline(description, priority, date, endTime));
        return new CommandResult(new Deadline(description, priority, date, endTime).toString(),true);
    }
}
