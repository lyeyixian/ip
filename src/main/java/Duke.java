import duke.exception.DukeException;
import duke.parser.Parser;
import duke.storage.Storage;
import duke.task.TaskList;
import duke.ui.Ui;

/**
 * Represent the main class to run the Duke program.
 */
public class Duke {
    private Ui ui;
    private Storage storage;
    private Parser parser;
    private TaskList tasks;
    
    public Duke(String filePath) {
        this.ui = new Ui();
        this.storage = new Storage(filePath);
        this.parser = new Parser();
        this.tasks = new TaskList(storage.load());
    }

    /**
     * Start the Duke program.
     */
    public void run() {
        String input = "";
        String output;
        ui.showWelcome();

        while (!input.equals("bye")) {
            input = ui.readInput();

            try {
                output = parser.parse(input, tasks, storage);
            } catch (DukeException e) {
                output = e.getMessage();
            }

            ui.showOutput(output);
        }
    }

    /**
     * The main method for Duke class.
     * @param args unused
     */
    public static void main(String[] args) {
        Duke duke = new Duke("data/storage/duke.txt");
        duke.run();
    }
}
