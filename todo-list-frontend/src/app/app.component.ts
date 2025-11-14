import { Component } from '@angular/core';
import { Todo, TodoService } from './todo.service';
import { Observable, BehaviorSubject, combineLatest } from 'rxjs';
import { map, tap } from 'rxjs/operators';

@Component({
  selector: 'app-root',
  template: `
    <div class="title">
      <h1>A list of TODOs</h1>
    </div>
    <div class="list">
      <label for="search">Search...</label>
      <input
        #searchBox
        id="search"
        type="text"
        (input)="onSearch(searchBox.value)"
        placeholder="Type to filter..."
      >
      <app-progress-bar [loading]="loading"></app-progress-bar>
      <app-todo-item
        *ngFor="let todo of filteredTodos$ | async"
        [item]="todo"
        (remove)="deleteTodo($event)">
      </app-todo-item>
    </div>
  `,
  styleUrls: ['app.component.scss']
})
export class AppComponent {

  private search$ = new BehaviorSubject<string>('');
  private todosLoaded$ = new BehaviorSubject<Todo[]>([]);

  filteredTodos$: Observable<Todo[]>;
  loading: boolean = true;

  constructor(private todoService: TodoService) {
    this.todoService.getAll().pipe(
      tap(todos => {
        this.todosLoaded$.next(todos);
        this.loading = false;
      })
    ).subscribe();

    this.filteredTodos$ = combineLatest([this.todosLoaded$, this.search$]).pipe(
      map(([todos, searchTerm]) =>
        todos.filter(todo =>
          todo.task.toLowerCase().includes(searchTerm.toLowerCase())
        )
      )
    );
  }

  onSearch(value: string) {
    this.search$.next(value);
  }

deleteTodo(id: number) {
  this.loading = true;
  this.todoService.remove(id).subscribe({
    next: () => {
      const current = this.todosLoaded$.getValue();
      this.todosLoaded$.next(current.filter(todo => todo.id !== id));
      this.loading = false;
    },
    error: (err) => {
      this.loading = false;
      alert('Error trying to remove a TODO: ' + err);
    }
  });
}
}
