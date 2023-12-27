import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterOutlet } from '@angular/router';
import {TaskAddComponent} from "./task-add/task-add.component";
import {TaskListComponent} from "./task-list/task-list.component";
import {AlertService} from "./services/alert.service";

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, RouterOutlet, TaskAddComponent, TaskListComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss'
})
export class AppComponent {
  title = 'taskmanager';

  constructor(public alertService: AlertService) {}
}
