/**
 * Represent a parser to parse user input.
 */
public class Parser {
    /**
     * Return the output of Duke program after parsing the user input.
     * @param input user input
     * @param tasks task list
     * @param storage storage of the data of the program
     * @return output of the program as String
     * @throws DukeException exception thrown when input is invalid
     */
    public String parse(String input, TaskList tasks, Storage storage) throws DukeException {
        String output;
        String[] command = input.split(" ", 2);
                                                                                                                                                   
        if (input.equals("bye")) {
            output = "\tBye. Hope to see you again soon!";
        } else if (input.equals("list")) {
            if (tasks.isEmpty()) {
                throw new DukeException("\t☹ OOPS!!! There is no task in the list.");
            }
            StringBuilder concat = new StringBuilder();

            for (int i = 0; i < tasks.size(); i++) {
                concat.append(String.format("\n\t%d. %s", i + 1, tasks.get(i)));
            }

            output = "\tHere are the tasks in your list: " + concat;
        } else if (command[0].equals("done")){
            if (command.length < 2) {
                throw new DukeException("\t☹ OOPS!!! The description of a done cannot be empty.");
            }

            int inputNumber;

            try {
                inputNumber = Integer.parseInt(command[1]);
            } catch(NumberFormatException e) {
                throw new DukeException("\t☹ OOPS!!! Argument must be an integer.");
            }

            if (inputNumber <= 0) {
                throw new DukeException(("\t☹ OOPS!!! Invalid argument."));
            }

            if (inputNumber > tasks.size()) {
                throw new DukeException("\t☹ OOPS!!! There is only " + tasks.size() + " tasks in the list.");
            }

            int index = inputNumber - 1;
            Task targetTask = tasks.get(index);

            if (targetTask.getStatus()) {
                throw new DukeException("\t☹ OOPS!!! You've already done that task.");
            }

            targetTask.setDone();
            tasks.updateStorage(storage);
            output = "\tNice! I've marked this task as done: \n\t\t" + targetTask;
        } else if (command[0].equals("todo")) {
            if (command.length < 2 || command[1].isBlank()) {
                throw new DukeException("\t☹ OOPS!!! The description of a todo cannot be empty.");
            }

            ToDo newToDo = new ToDo(command[1]);

            tasks.add(newToDo,storage);
            output = "\tGot it. I've added this task: \n\t\t" + newToDo + "\n\tNow you have " + tasks.size() 
                    + " tasks in the list.";
        } else if (command[0].equals("deadline")) {
            if (command.length < 2) {
                throw new DukeException("\t☹ OOPS!!! The description of a deadline cannot be empty.");
            }

            String[] commandParam = command[1].split("/by");

            if (commandParam.length < 2) {
                throw new DukeException("\t☹ OOPS!!! Invalid Argument (\"/by\" String not found)");
            }

            String description = commandParam[0].trim();
            String by = commandParam[1].trim();

            if (description.isBlank()) {
                throw new DukeException("\t☹ OOPS!!! The description of a deadline cannot be empty.");
            }

            if (by.isBlank()) {
                throw new DukeException("\t☹ OOPS!!! The /by description of a deadline cannot be empty.");
            }

            if (!by.matches("\\d{4}-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01])")) {
                throw new DukeException("\t☹ OOPS!!! The date format must be yyyy-mm-dd.");
            }

            Deadline newDeadline = new Deadline(description, by);

            tasks.add(newDeadline, storage);
            output = "\tGot it. I've added this task: \n\t\t" + newDeadline + "\n\tNow you have " + tasks.size() 
                    + " tasks in the list.";
        } else if (command[0].equals("event")) {
            if (command.length < 2) {
                throw new DukeException("\t☹ OOPS!!! The description of a event cannot be empty.");
            }

            String[] commandParam = command[1].split("/at");

            if (commandParam.length < 2) {
                throw new DukeException("\t☹ OOPS!!! Invalid Argument (\"/at\" String not found)");
            }

            String description = commandParam[0].trim();
            String at = commandParam[1].trim();

            if (description.isBlank()) {
                throw new DukeException("\t☹ OOPS!!! The description of a event cannot be empty.");
            }

            if (at.isBlank()) {
                throw new DukeException("\t☹ OOPS!!! The /at description of a event cannot be empty.");
            }

            if (!at.matches(
                    "\\d{4}-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01]) ([1-9]|1[012]):([0-5][0-9]) [AP]M")) {
                throw new DukeException("\t☹ OOPS!!! The date-time format must be yyyy-mm-dd h:mm AM/PM.");
            }

            Event newEvent = new Event(description, at);

            tasks.add(newEvent, storage);
            output = "\tGot it. I've added this task: \n\t\t" + newEvent + "\n\tNow you have " + tasks.size() 
                    + " tasks in the list.";
        } else if (command[0].equals("delete")) {
            if (command.length < 2) {
                throw new DukeException("\t☹ OOPS!!! The description of a delete cannot be empty.");
            }

            int inputNumber;

            try {
                inputNumber = Integer.parseInt(command[1]);
            } catch(NumberFormatException e) {
                throw new DukeException("\t☹ OOPS!!! Argument must be an integer.");
            }

            if (inputNumber <= 0) {
                throw new DukeException(("\t☹ OOPS!!! Invalid argument."));
            }

            if (inputNumber > tasks.size()) {
                throw new DukeException("\t☹ OOPS!!! There is only " + tasks.size() + " tasks in the list.");
            }

            int index = inputNumber - 1;
            Task targetTask = tasks.remove(index, storage);
            output = "\tNoted. I've removed this task: \n\t\t" + targetTask + "\n\tNow you have " + tasks.size() 
                    + " tasks in the list.";
        } else if (command[0].equals("find")) {
            if (command.length < 2) {
                throw new DukeException("\t☹ OOPS!!! The description of a find cannot be empty.");
            }
            
            String searchString = command[1];
            TaskList matchedTasks = tasks.find(searchString);
            
            if (matchedTasks.isEmpty()) {
                throw new DukeException("\t☹ OOPS!!! There is no such task in the list.");
            }

            StringBuilder concat = new StringBuilder();

            for (int i = 0; i < matchedTasks.size(); i++) {
                concat.append(String.format("\n\t%d. %s", i + 1, matchedTasks.get(i)));
            }
            
            output = "\tHere are the matching tasks in your list: " + concat;
        } else {
            throw new DukeException("\t☹ OOPS!!! I'm sorry, but I don't know what that means :-(");
        }

        return output;
    }
}
