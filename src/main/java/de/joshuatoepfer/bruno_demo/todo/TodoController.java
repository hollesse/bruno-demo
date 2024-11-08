package de.joshuatoepfer.bruno_demo.todo;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/todos")
public class TodoController {

  private final TodoRepository todoRepository;

  public TodoController(TodoRepository todoRepository) {
    this.todoRepository = todoRepository;
  }

  // Todos anlegen
  @PostMapping
  public Todo createTodo(@RequestBody Todo todo) {
    return todoRepository.save(todo);
  }

  // Liste von Todos anzeigen
  @GetMapping
  public List<Todo> getAllTodos(@RequestParam(name = "open", required = false) String open) {
    if (open != null) {
      return todoRepository.findByCompletedFalse();
    }
    return todoRepository.findAll();
  }

  // Todo löschen
  @DeleteMapping("/{id}")
  public ResponseEntity<?> deleteTodo(@PathVariable Long id) {
    return todoRepository
        .findById(id)
        .map(
            todo -> {
              todoRepository.delete(todo);
              return ResponseEntity.ok().build();
            })
        .orElse(ResponseEntity.notFound().build());
  }

  // Todo als erledigt markieren
  @PutMapping("/{id}/complete")
  public ResponseEntity<Todo> completeTodo(@PathVariable Long id) {
    return todoRepository
        .findById(id)
        .map(
            todo -> {
              todo.setCompleted(true);
              Todo updatedTodo = todoRepository.save(todo);
              return ResponseEntity.ok(updatedTodo);
            })
        .orElse(ResponseEntity.notFound().build());
  }
}
