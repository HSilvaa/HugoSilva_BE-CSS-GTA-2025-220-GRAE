import { Component } from '@angular/core';
import { Todo, TodoService } from './todo.service';
import { Observable } from 'rxjs';
import { tap, startWith } from 'rxjs/operators';

@Component({
  selector: 'app-root',
  template: `
    <div class="title">
      <h1>A list of TODOs</h1>
    </div>
    <div class="list">
      <label for="search">Search...</label>
      <input id="search" type="text">

      <!-- Barra de progreso -->
      <app-progress-bar [loading]="loading"></app-progress-bar>

      <app-todo-item *ngFor="let todo of todos$ | async" [item]="todo"></app-todo-item>
    </div>
  `,
  styleUrls: ['app.component.scss']
})
export class AppComponent {

  todos$: Observable<Todo[]>;
  loading: boolean = true;

constructor(todoService: TodoService) {
  this.loading = true;
  this.todos$ = todoService.getAll().pipe(
    tap(() => this.loading = false)
  );
  }
}
