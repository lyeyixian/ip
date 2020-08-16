public class Task {
  private String description;
  private boolean isDone;

  public Task(String description) {
    this.description = description;
    this.isDone = false;
  }

  public String getStatusIcon() {
    return isDone
        ? "✓"
        : "✘";
  }

  public void setDone() {
    this.isDone = true;
  }

  @Override
  public String toString() {
    return "[" + getStatusIcon() + "] " + this.description;
  }
}