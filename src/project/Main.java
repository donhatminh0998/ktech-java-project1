package project;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.List;

public class Main {
    private static List<Todo> todos;
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        TodoRepository repository = new TodoRepository();
        todos = repository.getTodos();
        TodoService service = new TodoService(reader, repository.getTodos());

        while (true) {
            printFirstScreen();
            System.out.print("Input: ");
            int selection;
            try {
                selection = Integer.parseInt(reader.readLine());
            } catch (NumberFormatException e) {
                System.out.println("Input must be an integer (1 ~ 5)");
                continue;
            }
            if (selection == 5) break;
            switch (selection) {
                case 1 -> service.createTodo();
                case 2 -> service.editTodo();
                case 3 -> service.finishTodo();
                case 4 -> service.deleteTodo();
                default -> System.out.println("Invalid selection, select from 1 to 5");
            }
        }

        try {
            repository.writeToFile();
        } catch (IOException e) {
            System.err.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private static void printFirstScreen() {
        System.out.println("Welcome!!!\n");
        int todosLeft = todos.stream()
                .filter(todo -> !todo.isCompleted())
                .mapToInt(todo -> 1)
                .sum();
        switch (todosLeft) {
            case 0 -> System.out.println("You have no more TODOs left!!!");
            case 1 -> System.out.println("You have 1 TODO left.\n");
            default -> System.out.println("You have " + todosLeft + " TODOs left.\n");
        }

        StringBuilder todoBuilder = new StringBuilder();
        for (int i = 0; i < todos.size(); i++) {
            Todo todo = todos.get(i);
            if (todo.isCompleted() && todo.getDueDate().isBefore(LocalDate.now())) continue;
            todoBuilder.append(i + 1).append(". ");
            todoBuilder.append(todo.getTitle());
            if (todo.isCompleted()) todoBuilder.append(" (Done)");
            todoBuilder.append('\n');
        }
        System.out.println(todoBuilder);
        System.out.println("1. Create TODO");
        System.out.println("2. Edit TODO");
        System.out.println("3. Finish TODO");
        System.out.println("4. Delete TODO");
        System.out.println("5. Exit");
        System.out.println();
    }
}