import { Component } from '@angular/core';
import {Task, TaskPriority, TaskService} from "../services/task.service";
import {FormsModule} from "@angular/forms";
import {CommonModule} from "@angular/common";

@Component({
  selector: 'app-task-add',
  standalone: true,
  imports: [
    FormsModule,
    CommonModule
  ],
  templateUrl: './task-add.component.html',
  styleUrl: './task-add.component.scss'
})
export class TaskAddComponent {
  name!: string;
  priority!: TaskPriority;
  priorities: string[] = [];

  constructor(private taskService: TaskService) {}

  ngOnInit() {
    this.priorities = this.taskService.getTaskPriorities();
    console.log(this.priorities);
  }

  addTask(name: string, priority: TaskPriority) {
    const newTask: Task = { name, done: false, created: new Date(), priority };
    this.taskService.addTask(newTask).subscribe();
  }

}
