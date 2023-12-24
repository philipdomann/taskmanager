import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import {Task, TaskService} from "../services/task.service";

@Component({
  selector: 'app-task-list',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './task-list.component.html',
  styleUrls: ['./task-list.component.scss']
})
export class TaskListComponent implements OnInit {
  tasks: Task[] = [];

  constructor(private taskService: TaskService) {}

  ngOnInit() {
    this.loadTasks();
  }

  loadTasks() {
    this.taskService.getTasks().subscribe((data: Task[]) => {
      this.tasks = data;
    });
  }

  editTask(task: Task) {
    this.taskService.editTask(task).subscribe(updatedTask => {
      const index = this.tasks.findIndex(t => t.id === updatedTask.id);
      if (index !== -1) {
        this.tasks[index] = updatedTask;
      }
    });
  }

  removeTask(taskId: number) {
    this.taskService.removeTask(taskId).subscribe(() => {
      this.tasks = this.tasks.filter(task => task.id !== taskId);
    });
  }
}
