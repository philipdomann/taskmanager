import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { TaskService, Task, TaskPriority } from './task.service';

describe('TaskService', () => {
  let service: TaskService;
  let httpTestingController: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [TaskService]
    });

    service = TestBed.inject(TaskService);
    httpTestingController = TestBed.inject(HttpTestingController);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should retrieve all tasks', () => {
    const mockTasks: Task[] = [
      { id: 1, name: 'Test Task 1', done: false, created: new Date(), priority: TaskPriority.LOW },
      { id: 2, name: 'Test Task 2', done: false, created: new Date(), priority: TaskPriority.URGENT }
    ];

    service.getTasks().subscribe(tasks => {
      expect(tasks.length).toBe(2);
      expect(tasks).toEqual(mockTasks);
    });

    const req = httpTestingController.expectOne(service.apiUrl);
    expect(req.request.method).toEqual('GET');
    req.flush(mockTasks);
  });

  it('should add a new task', () => {
    const newTask: Task = { name: 'New Task', done: false, created: new Date(), priority: TaskPriority.NORMAL };

    service.addTask(newTask).subscribe(task => {
      expect(task).toEqual(jasmine.objectContaining(newTask));
    });

    const req = httpTestingController.expectOne(service.apiUrl);
    expect(req.request.method).toEqual('POST');
    req.flush({ ...newTask, id: 1 });
  });

  it('should remove a task', () => {
    const taskId = 1;

    service.removeTask(taskId).subscribe(response => {
      expect(response).toBeNull();
    });

    const req = httpTestingController.expectOne(`${service.apiUrl}/${taskId}`);
    expect(req.request.method).toEqual('DELETE');
    req.flush(null);
  });

  it('should edit a task', () => {
    const updatedTask: Task = { id: 1, name: 'Updated Task', done: true, created: new Date(), priority: TaskPriority.URGENT };

    service.editTask(updatedTask).subscribe(task => {
      expect(task).toEqual(updatedTask);
    });

    const req = httpTestingController.expectOne(`${service.apiUrl}/${updatedTask.id}`);
    expect(req.request.method).toEqual('PUT');
    req.flush(updatedTask);
  });

  afterEach(() => {
    httpTestingController.verify();
  });
});
