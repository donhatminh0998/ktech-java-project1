package project;

import java.io.BufferedReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class TodoService {
    private final BufferedReader reader;
    private final List<Todo> todos;

    public TodoService(BufferedReader reader, List<Todo> todos) {
        this.reader = reader;
        this.todos = todos;
        this.sortTodos();
    }

    public void createTodo() throws IOException {
        System.out.print("Title: ");
        String title = reader.readLine();
        LocalDate dueDate = autoRetryUntil(false).orElseThrow();
        todos.add(new Todo(title, dueDate));
        sortTodos();
        System.out.println("Saved!!!\n");
    }

    public void editTodo() throws IOException {
        int id = autoRetryIndex("Edit TODO number: ");
        if (!(id < todos.size())) {
            System.out.println("No TODO by that number");
            System.out.println();
            return;
        }
        Todo todo = todos.get(id);
        System.out.print("Title: ");
        String title = reader.readLine();
        if (!title.isBlank()) todo.setTitle(title);
        Optional<LocalDate> until = autoRetryUntil(true);
        if (until.isPresent()) todo.setDueDate(until.get());
        sortTodos();
        System.out.println("Saved!!!\n");
    }

    public void finishTodo() throws IOException {
        int id = autoRetryIndex("Finish TODO number: ");
        if (!(id < todos.size())) {
            System.out.println("No TODO by that number");
            System.out.println();
            return;
        }

        Todo todo = todos.get(id);
        todo.complete();
    }

    public void deleteTodo() throws IOException {
        int id = autoRetryIndex("Delete TODO number: ");
        if (!(id < todos.size())) {
            System.out.println("No TODO by that number");
            System.out.println();
            return;
        }
        todos.remove(id);
    }

    private void sortTodos() {
        this.todos.sort(Comparator.comparing(Todo::getDueDate));
    }

    private int autoRetryIndex(String prompt) throws IOException {
        int id;
        while (true) {
            try {
                System.out.print(prompt);
                id = Integer.parseInt(reader.readLine()) - 1;
                break;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input, input a integer");
            }
        }
        return id;
    }

    private Optional<LocalDate> autoRetryUntil(boolean allowEmpty) throws IOException {
        while (true) {
            try {
                System.out.print("Until: ");
                String untilString = reader.readLine();
                if (!untilString.isEmpty()) {
                    return Optional.of(LocalDate.parse(untilString));
                }
                else if(allowEmpty) return Optional.empty();
                System.out.println("No input, try again");
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format, try again");
            }
        }
    }
}