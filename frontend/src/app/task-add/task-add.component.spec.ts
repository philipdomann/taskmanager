import { ComponentFixture, TestBed } from '@angular/core/testing';
import { TaskAddComponent } from './task-add.component';
import { of } from 'rxjs';
import { FormsModule } from '@angular/forms';
import {TaskPriority, TaskService} from "../services/task.service";

describe('TaskAddComponent', () => {
  let component: TaskAddComponent;
  let fixture: ComponentFixture<TaskAddComponent>;
  let taskService: TaskService;

  beforeEach(async () => {
    const taskServiceStub = {
      addTask: (task: any) => of(task)
    };

    await TestBed.configureTestingModule({
      imports: [ TaskAddComponent, FormsModule ],
      providers: [ { provide: TaskService, useValue: taskServiceStub } ]
    }).compileComponents();

    fixture = TestBed.createComponent(TaskAddComponent);
    component = fixture.componentInstance;
    taskService = TestBed.inject(TaskService);
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should call addTask on submit', () => {
    const currentDate = new Date();
    const newTask = { name: 'New Task', priority: TaskPriority.NORMAL, done: false, created: currentDate };
    const addTaskSpy = spyOn(taskService, 'addTask').and.returnValue(of(newTask));

    component.addTask(newTask.name, newTask.priority);

    // Verify that addTask has been called
    expect(addTaskSpy).toHaveBeenCalled();

    // Get the arguments of the first call to the spy
    const actualArgs = addTaskSpy.calls.first().args[0];

    // Compare properties individually
    expect(actualArgs.name).toEqual(newTask.name);
    expect(actualArgs.priority).toEqual(newTask.priority);
    expect(actualArgs.done).toEqual(newTask.done);
    expect(actualArgs.created).toEqual(currentDate);
  });
});
