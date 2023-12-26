import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {environment} from "../../environments/environment";

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

  constructor(private http: HttpClient) {
  }

  getTasks(): Observable<Task[]> {
    return this.http.get<Task[]>(this._apiUrl);
  }

  addTask(task: Task): Observable<Task> {
    return this.http.post<Task>(this._apiUrl, task);
  }

  removeTask(taskId: number): Observable<void> {
    return this.http.delete<void>(`${this._apiUrl}/${taskId}`);
  }

  editTask(task: Task): Observable<Task> {
    return this.http.put<Task>(`${this._apiUrl}/${task.id}`, task);
  }

  get apiUrl(): string {
    return this._apiUrl;
  }

  getTaskPriorities(): string[] {
    return Object.values(TaskPriority);
  }
}
