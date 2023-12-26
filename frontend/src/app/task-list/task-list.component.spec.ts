import { ComponentFixture, TestBed } from '@angular/core/testing';
import { TaskListComponent } from './task-list.component';
import { of } from 'rxjs';
import { TaskPriority, TaskService, Task } from "../services/task.service";

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
      tasks$: of(mockTasks),
      removeTask: (id: number) => of({}),
      editTask: (task: Task) => of(task)
    };

    await TestBed.configureTestingModule({
      imports: [ TaskListComponent ],
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
    fixture.detectChanges(); // ngOnInit() gets called here
    expect(component.tasks.length).toBe(2);
  });

  it('should call removeTask and update task list', () => {
    // Arrange
    const taskToRemove = mockTasks[0];

    // Spy on remove task
    spyOn(taskService, 'removeTask')
        .and.returnValue(of(undefined));

    // Act
    component.removeTask(taskToRemove);
    fixture.detectChanges();

    // Assert
    expect(component.tasks.length).toBe(1);
    expect(component.tasks).not.toContain(taskToRemove);
  });

  it('should update a task', () => {
    const updatedTask: Task = { id: 1, name: 'Updated Task', done: true, created: new Date(), priority: TaskPriority.NORMAL };
    spyOn(taskService, 'editTask').and.returnValue(of(updatedTask));
    component.startEdit(mockTasks[0]);
    component.editedTask = updatedTask;
    component.saveEdit();
    fixture.detectChanges();
    const task = component.tasks.find(task => task.id === 1);
    expect(task).toBeDefined();
    expect(task?.name).toEqual('Updated Task');
    expect(task?.done).toBeTrue();
  });
});
