import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Task {
    private static int serialNum = 0;
    private int id;
    private String description;
    private Status status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private Task(int id, String description, Status status, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.description = description;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;

        // sync the serial number when program is rebooted
        if (serialNum < id)
            serialNum = id;
    }

    public Task(String description) {
        this.id = ++serialNum;
        this.description = description;
        this.status = Status.TODO;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public int getId() {
        return id;
    }

    public Status getStatus() {
        return status;
    }

    public String getCreatedAt() {
        return createdAt.format(formatter);
    }

    public String getUpdatedAt() {
        return updatedAt.format(formatter);
    }

    public void setDescription(String description) {
        this.description = description;
        this.updatedAt = LocalDateTime.now();
    }

    public void setStatus(Status status) {
        this.status = status;
        this.updatedAt = LocalDateTime.now();
    }

    public String toJson() {
        return "  {\n"
                + "    \"id\": " + "\"" + this.id + "\",\n"
                + "    \"description\": " + "\"" + this.description + "\",\n"
                + "    \"status\": " + "\"" + this.status + "\",\n"
                + "    \"createdAt\": " + "\"" + this.createdAt + "\",\n"
                + "    \"updatedAt\": " + "\"" + this.updatedAt + "\"\n"
                + "  }";
    }

    public static Task fromJson(String json) {
        String[] data = json
                .replace("{", "")
                .replace("}", "")
                .replace("\n", "")
                .replace("\"", "")
                .trim()
                .split(",");

        int id = Integer.parseInt(data[0].split(":")[1].trim());
        String description = data[1].split(":")[1].trim();
        String statusStr = data[2].split(":")[1].trim();
        String createdAtStr = data[3].split(":", 2)[1].trim();
        String updatedAtStr = data[4].split(":", 2)[1].trim();

        Status status = Status.valueOf(statusStr.replace("-", "_").toUpperCase());
        LocalDateTime createdAt = LocalDateTime.parse(createdAtStr);
        LocalDateTime updatedAt = LocalDateTime.parse(updatedAtStr);

        return new Task(id, description, status, createdAt, updatedAt);
    }

    public String toString() {
        return "{ "
                + "id: " + this.id + ", "
                + "description: " + this.description + ", "
                + "status: " + this.status + ", "
                + "createdAt: " + this.getCreatedAt() + ", "
                + "updatedAt: " + this.getUpdatedAt()  + " }";
    }
}
