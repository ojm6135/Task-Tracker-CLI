public enum Status {
    TODO("todo"),
    IN_PROGRESS("in-progress"),
    DONE("done");

    private String description;

    Status(String description) {
        this.description = description;
    }

    public String toString() {
        return this.description;
    }
}
