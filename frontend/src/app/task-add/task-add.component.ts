import { Component } from '@angular/core';
import {Task, TaskPriority, TaskService} from "../services/task.service";
import {FormsModule} from "@angular/forms";

@Component({
  selector: 'app-task-add',
  standalone: true,
  imports: [
    FormsModule
  ],
  templateUrl: './task-add.component.html',
  styleUrl: './task-add.component.scss'
})
export class TaskAddComponent {
  constructor(private taskService: TaskService) {}

  addTask(name: string, priority: TaskPriority) {
    const newTask: Task = { name, done: false, created: new Date(), priority };
    this.taskService.addTask(newTask).subscribe();
  }

}
