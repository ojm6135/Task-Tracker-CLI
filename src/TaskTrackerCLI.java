import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class TaskTrackerCLI {
    public static void run() {
        String menu = null;
        int targetId = 0;
        String taskDescrip = "";
        String statusStr = "";
        Status status = null;
        Task task = null;
        ArrayList<Task> tasks = null;
        TaskManager tm = new TaskManager();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\nMenu: add, update, delete, mark, list, exit");
            menu = scanner.next().toLowerCase().trim();

            if (menu.equals("exit")) {
                break;
            }

            switch (menu) {
                case "add":
                    taskDescrip = scanner.nextLine().trim();
                    task = new Task(taskDescrip);
                    tm.addTask(task);
                    System.out.println("Task added successfully (ID: " + task.getId() + ")");
                    break;

                case "update":
                    try {
                        targetId = scanner.nextInt();
                        taskDescrip = scanner.nextLine().trim();
                        tm.updateTask(targetId, taskDescrip);
                        System.out.println("Task updated successfully (ID: " + targetId + ")");
                    } catch (InputMismatchException e) {
                        System.out.println("Wrong input (first argument type must be Integer)");
                        scanner.nextLine();  // clear the buffer
                    } catch (IndexOutOfBoundsException e) {
                        System.out.println("No task (ID: " + targetId + ")");
                    }
                    break;

                case "delete":
                    try {
                        targetId = scanner.nextInt();
                        tm.deleteTask(targetId);
                        System.out.println("Task deleted successfully (ID: " + targetId + ")");
                    } catch (InputMismatchException e) {
                        System.out.println("Wrong input (the argument type must be Integer)");
                        scanner.nextLine();  // clear the buffer
                    } catch (IndexOutOfBoundsException e) {
                        System.out.println("No task (ID: " + targetId + ")");
                    }
                    break;

                case "mark":
                    try {
                        statusStr = scanner.next().trim();
                        status = Status.valueOf(statusStr.replace("-", "_").toUpperCase());
                        targetId = scanner.nextInt();
                        tm.mark(targetId, status);
                        System.out.println("Task marked '" + status + "' successfully (ID: " + targetId + ")");
                    } catch (InputMismatchException e) {
                        System.out.println("Wrong input (second argument type must be Integer)");
                        scanner.nextLine();  // clear the buffer
                    } catch (IndexOutOfBoundsException e) {
                        System.out.println("No task (ID: " + targetId + ")");
                    } catch (IllegalArgumentException e) {
                        System.out.println("Wrong input (status can be 'todo', 'in-progress' or 'done')");
                        scanner.nextLine();  // clear the buffer
                    }
                    break;

                case "list":
                    statusStr = scanner.nextLine().trim();

                    if (statusStr.isEmpty()) {
                        tasks = tm.listTasks();
                        showTasks(tasks);
                        break;
                    }

                    try {
                        status = Status.valueOf(statusStr.replace("-", "_").toUpperCase());
                        tasks = tm.listTasks(status);
                        showTasks(tasks);
                    } catch (IllegalArgumentException e) {
                        System.out.println("Wrong input (status can be 'todo', 'in-progress' or 'done')");
                        scanner.nextLine();  // clear the buffer
                    }
                    break;

                default:
                    System.out.println("Wrong input");
                    scanner.nextLine();  // clear the buffer
            }
        }

        scanner.close();
    }

    private static void showTasks(ArrayList<Task> tasks) {
        for (Task t : tasks) {
            System.out.println(t);
        }
    }
}
