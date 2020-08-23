import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Duke {
  
    public static String process(String input, List<Task> list, Storage storage) throws DukeException {
        String output;
        String[] command = input.split(" ", 2);

        if (input.equals("bye")) {
            output = "\tBye. Hope to see you again soon!";
        } else if (input.equals("list")) {
            if (list.isEmpty()) {
                throw new DukeException("\t☹ OOPS!!! There is no task in the list.");
            }
            StringBuilder concat = new StringBuilder();

            for (int i = 0; i < list.size(); i++) {
                concat.append(String.format("\n\t%d. %s", i + 1, list.get(i)));
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

            if (inputNumber > list.size()) {
                throw new DukeException("\t☹ OOPS!!! There is only " + list.size() + " tasks in the list.");
            }

            int index = inputNumber - 1;
            Task targetTask = list.get(index);

            if (targetTask.getStatus()) {
                throw new DukeException("\t☹ OOPS!!! You've already done that task.");
            }

            targetTask.setDone();
            storage.update(list);
            output = "\tNice! I've marked this task as done: \n\t\t" + targetTask;
        } else if (command[0].equals("todo")) {
            if (command.length < 2 || command[1].isBlank()) {
                throw new DukeException("\t☹ OOPS!!! The description of a todo cannot be empty.");
            }

            ToDo newToDo = new ToDo(command[1]);

            list.add(newToDo);
            storage.update(list);
            output = "\tGot it. I've added this task: \n\t\t" + newToDo + "\n\tNow you have " + list.size() + " tasks in the list.";
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

            list.add(newDeadline);
            storage.update(list);
            output = "\tGot it. I've added this task: \n\t\t" + newDeadline + "\n\tNow you have " + list.size() + " tasks in the list.";
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

            if (!at.matches("\\d{4}-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01]) ([1-9]|1[012]):([0-5][0-9]) [AP]M")) {
                throw new DukeException("\t☹ OOPS!!! The date-time format must be yyyy-mm-dd h:mm AM/PM.");
            }

            Event newEvent = new Event(description, at);

            list.add(newEvent);
            storage.update(list);
            output = "\tGot it. I've added this task: \n\t\t" + newEvent + "\n\tNow you have " + list.size() + " tasks in the list.";
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

            if (inputNumber > list.size()) {
                throw new DukeException("\t☹ OOPS!!! There is only " + list.size() + " tasks in the list.");
            }

            int index = inputNumber - 1;
            Task targetTask = list.remove(index);
            storage.update(list);
            output = "\tNoted. I've removed this task: \n\t\t" + targetTask + "\n\tNow you have " + list.size() + " tasks in the list.";
        } else {
            throw new DukeException("\t☹ OOPS!!! I'm sorry, but I don't know what that means :-(");
        }

        return output;
    }

    public static void main(String[] args) {
        Ui ui = new Ui();
        Storage storage = new Storage("data/storage/duke.txt");
        String input = "";
        List<Task> list = storage.load();
        String output;

        ui.showWelcome();
        

        while (!input.equals("bye")) {
            input = ui.readInput();

            try {
                output = process(input, list, storage);
            } catch (DukeException e) {
                output = e.getMessage();
            }

            ui.showOutput(output);
        }
    }
}
