import {ComponentFixture, TestBed} from '@angular/core/testing';
import {TaskListComponent} from './task-list.component';
import {BehaviorSubject, of} from 'rxjs';
import {Task, TaskPriority, TaskService} from "../services/task.service";
import {tap} from "rxjs/operators";

describe('TaskListComponent', () => {
  let component: TaskListComponent;
  let fixture: ComponentFixture<TaskListComponent>;
  let taskService: TaskService;
  let mockTasks: Task[];
  let tasksSubject: BehaviorSubject<Task[]>;

  beforeEach(async () => {
    mockTasks = [
      { id: 1, name: 'Task 1', done: false, created: new Date(), priority: TaskPriority.NORMAL },
      { id: 2, name: 'Task 2', done: true, created: new Date(), priority: TaskPriority.URGENT }
    ];
    tasksSubject = new BehaviorSubject(mockTasks);

    const taskServiceStub = {
      tasks$: tasksSubject.asObservable(),
      removeTask: (id: number) => of({}).pipe(
        tap(() => {
          mockTasks = mockTasks.filter(task => task.id !== id);
          tasksSubject.next(mockTasks);
        })
      ),
      editTask: (task: Task) => of(task)
    };

    await TestBed.configureTestingModule({
      imports: [ TaskListComponent ],
      providers: [ { provide: TaskService, useValue: taskServiceStub } ]
    }).compileComponents();

    fixture = TestBed.createComponent(TaskListComponent);
    component = fixture.componentInstance;
    taskService = TestBed.inject(TaskService);
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should load tasks on init', () => {
    expect(component.tasks.length).toBe(2);
  });

  it('should call removeTask and update task list', () => {
    // Arrange
    const taskToRemove = mockTasks[0];

    // Spy on removeTask method
    spyOn(taskService, 'removeTask').and.callThrough();

    // Act
    component.removeTask(taskToRemove);

    // Assert
    if (taskToRemove.id !== undefined) {
      expect(taskService.removeTask).toHaveBeenCalledWith(taskToRemove.id);
    }
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
