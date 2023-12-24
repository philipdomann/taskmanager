import { ComponentFixture, TestBed } from '@angular/core/testing';
import { TaskListComponent } from './task-list.component';
import { of } from 'rxjs';
import {TaskPriority, TaskService, Task} from "../services/task.service";

describe('TaskListComponent', () => {
  let component: TaskListComponent;
  let fixture: ComponentFixture<TaskListComponent>;
  let taskService: TaskService;
  let mockTasks: Task[];

  beforeEach(async () => {
    mockTasks = [
      { id: 1, name: 'Task 1', done: false, created: new Date(), priority: TaskPriority.NORMAL },
      { id: 2, name: 'Task 2', done: true, created: new Date(), priority: TaskPriority.URGENT }
    ];

    const taskServiceStub = {
      getTasks: () => of(mockTasks),
      removeTask: (id: number) => of({})
    };

    await TestBed.configureTestingModule({
      declarations: [ TaskListComponent ],
      providers: [ { provide: TaskService, useValue: taskServiceStub } ]
    })
      .compileComponents();

    fixture = TestBed.createComponent(TaskListComponent);
    component = fixture.componentInstance;
    taskService = TestBed.inject(TaskService);
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should load tasks on init', () => {
    spyOn(taskService, 'getTasks').and.callThrough();
    fixture.detectChanges(); // ngOnInit() gets called here
    expect(taskService.getTasks).toHaveBeenCalled();
    expect(component.tasks.length).toBe(2);
  });

  it('should call removeTask and update task list', () => {
    spyOn(taskService, 'removeTask').and.returnValue(of(null));
    component.removeTask(1);
    expect(taskService.removeTask).toHaveBeenCalledWith(1);
    expect(component.tasks.length).toBe(1);
  });

  it('should update a task', () => {
    const updatedTask: Task = { id: 1, name: 'Updated Task', done: true, created: new Date(), priority: TaskPriority.NORMAL };
    spyOn(taskService, 'editTask').and.returnValue(of(updatedTask));
    component.tasks = [
      { id: 1, name: 'Task 1', done: false, created: new Date(), priority: TaskPriority.NORMAL },
      { id: 2, name: 'Task 2', done: true, created: new Date(), priority: TaskPriority.URGENT }
    ];

    component.editTask(updatedTask);

    expect(component.tasks.find(task => task.id === 1).name).toEqual('Updated Task');
    expect(component.tasks.find(task => task.id === 1).done).toBeTrue();
  });
});
