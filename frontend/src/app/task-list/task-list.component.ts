import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import {Task, TaskService} from "../services/task.service";
import {FormsModule} from "@angular/forms";

@Component({
  selector: 'app-task-list',
  standalone: true,
  imports: [CommonModule, FormsModule ],
  templateUrl: './task-list.component.html',
  styleUrls: ['./task-list.component.scss']
})
export class TaskListComponent implements OnInit {
  tasks: Task[] = [];
  editingTaskId: number | null = null;
  editedTask: Task | null = null;

  constructor(public taskService: TaskService) {}

  ngOnInit() {
    this.loadTasks();
  }

  loadTasks() {
    this.taskService.tasks$.subscribe((tasks: Task[]) => {
      this.tasks = tasks;
    });
  }

  startEdit(task: Task) {
    if (typeof task.id !== 'undefined'){
      this.editingTaskId = task.id;
      this.editedTask = { ...task, name: task.name || '', priority: task.priority || 'LOW' };
    }
  }

  saveEdit() {
    if (this.editedTask && this.editingTaskId) {
      this.taskService.editTask(this.editedTask).subscribe(updatedTask => {
        const index = this.tasks.findIndex(t => t.id === updatedTask.id);
        if (index !== -1) {
          this.tasks[index] = updatedTask;
        }
        this.cancelEdit();
      });
    }
  }

  cancelEdit() {
    this.editingTaskId = null;
    this.editedTask = null;
  }

  removeTask(task: Task) {
    if (task.id !== undefined) {
        this.taskService.removeTask(task.id).subscribe();
    } else {
        console.error('Cannot remove task without an ID', task);
    }
  }
}
