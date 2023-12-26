import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {BehaviorSubject, Observable} from 'rxjs';
import {environment} from "../../environments/environment";
import {tap} from "rxjs/operators";

export enum TaskPriority {
  LOW = 'LOW',
  NORMAL = 'NORMAL',
  URGENT = 'URGENT'
}

export interface Task {
  id?: number;
  name: string;
  done: boolean;
  created: Date;
  priority: TaskPriority;
}

@Injectable({
  providedIn: 'root'
})
export class TaskService {
  private _apiUrl = environment.apiUrl + '/tasks';
  private _tasks = new BehaviorSubject<Task[]>([]);

  constructor(private http: HttpClient) {
    this.loadInitialTasks();
  }

  private loadInitialTasks() {
    this.http.get<Task[]>(this._apiUrl).subscribe(tasks => {
      this._tasks.next(tasks);
    });
  }

  get tasks$(): Observable<Task[]> {
    return this._tasks.asObservable();
  }

  addTask(task: Task): Observable<Task> {
    return this.http.post<Task>(this._apiUrl, task).pipe(
        tap(newTask => {
          const currentTasks = this._tasks.value;
          this._tasks.next([...currentTasks, newTask]);
        })
    );
  }

  editTask(task: Task): Observable<Task> {
    return this.http.put<Task>(`${this._apiUrl}/${task.id}`, task).pipe(
        tap(updatedTask => {
          const currentTasks = this._tasks.value;
          const taskIndex = currentTasks.findIndex(t => t.id === updatedTask.id);
          currentTasks[taskIndex] = updatedTask;
          this._tasks.next(currentTasks);
        })
    );
  }

  removeTask(taskId: number): Observable<void> {
    return this.http.delete<void>(`${this._apiUrl}/${taskId}`).pipe(
        tap(() => {
          const currentTasks = this._tasks.value.filter(task => task.id !== taskId);
          this._tasks.next(currentTasks);
        })
    );
  }

  get apiUrl(): string {
    return this._apiUrl;
  }

  getTaskPriorities(): string[] {
    return Object.values(TaskPriority);
  }
}
