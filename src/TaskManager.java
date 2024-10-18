import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class TaskManager {
    private final Path FILE_PATH = Path.of("tasks.json");
    private ArrayList<Task> tasks = null;

    public TaskManager() {
        tasks = loadTasks();
    }

    // load all tasks from json file
    private ArrayList<Task> loadTasks() {
        tasks = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        String json = null;

        try {
            json = Files.readString(FILE_PATH);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (json.isEmpty()) {
            return tasks;
        }

        String[] data = json
                .replace("[", "")
                .replace("]", "")
                .trim()
                .split(",");

        for (String d : data) {
            if (d.trim().endsWith("}")) {
                sb.append(d);
                tasks.add(Task.fromJson(sb.toString()));
                sb.setLength(0);
                continue;
            }
            sb.append(d + ", ");
        }

        return tasks;
    }

    private void updateJson() {
        StringBuilder sb = new StringBuilder();

        sb.append("[\n");
        for (int i = 0; i < tasks.size(); i++) {
            sb.append(tasks.get(i).toJson());
            if (i == tasks.size()-1) {
                break;
            }
            sb.append(",\n");
        }
        sb.append("\n]");

        try {
            Files.writeString(FILE_PATH, sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addTask(Task task) {
        tasks.add(task);
        updateJson();
    }

    public void updateTask(int id, String description) throws IndexOutOfBoundsException {
        for (Task t : tasks) {
            if (t.getId() == id) {
                t.setDescription(description);
                updateJson();
                return;
            }
        }

        throw new IndexOutOfBoundsException();
    }

    public void deleteTask(int id) throws IndexOutOfBoundsException {
        if (tasks.removeIf(task -> task.getId() == id)) {
            updateJson();
        }

        throw new IndexOutOfBoundsException();
    }

    public void mark(int id, Status status) throws IndexOutOfBoundsException {
        for (Task t : tasks) {
            if (t.getId() == id) {
                t.setStatus(status);
                updateJson();
                return;
            }
        }

        throw new IndexOutOfBoundsException();
    }

    public ArrayList<Task> listTasks() {
        return tasks;
    }

    public ArrayList<Task> listTasks(Status status) {
        return (ArrayList<Task>) tasks.stream()
                .filter(task -> task.getStatus() == status)
                .collect(Collectors.toList());
    }
}
