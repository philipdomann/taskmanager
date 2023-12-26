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

  it('should stream tasks from BehaviorSubject', () => {
    const mockTasks: Task[] = [
      { id: 1, name: 'Test Task 1', done: false, created: new Date(), priority: TaskPriority.LOW },
      { id: 2, name: 'Test Task 2', done: false, created: new Date(), priority: TaskPriority.URGENT }
    ];

    const req = httpTestingController.expectOne(service.apiUrl);
    expect(req.request.method).toEqual('GET');
    req.flush(mockTasks);

    service.tasks$.subscribe(tasks => {
      expect(tasks.length).toBe(2);
      expect(tasks).toEqual(mockTasks);
    });

    httpTestingController.verify();
  });

  it('should add a new task and update BehaviorSubject', () => {
    const newTask: Task = { name: 'New Task', done: false, created: new Date(), priority: TaskPriority.NORMAL };
    const reqInit = httpTestingController.expectOne(service.apiUrl);
    expect(reqInit.request.method).toEqual('GET');
    reqInit.flush([]);

    service.addTask(newTask).subscribe(task => {
      expect(task).toEqual(jasmine.objectContaining(newTask));
    });

    const reqAdd = httpTestingController.expectOne(service.apiUrl);
    expect(reqAdd.request.method).toEqual('POST');
    reqAdd.flush({ ...newTask, id: 3 });

    service.tasks$.subscribe(tasks => {
      expect(tasks.some(task => task.id === 3)).toBeTruthy();
    });
  });


  it('should remove a task and update BehaviorSubject', () => {
    const taskId = 1;

    const reqInit = httpTestingController.expectOne(service.apiUrl);
    expect(reqInit.request.method).toEqual('GET');
    reqInit.flush([{ id: 1, name: 'Task', done: false, created: new Date(), priority: TaskPriority.NORMAL }]);

    service.removeTask(taskId).subscribe(response => {
      expect(response).toBeNull();
    });

    const reqRemove = httpTestingController.expectOne(`${service.apiUrl}/${taskId}`);
    expect(reqRemove.request.method).toEqual('DELETE');
    reqRemove.flush(null);

    service.tasks$.subscribe(tasks => {
      expect(tasks.some(task => task.id === taskId)).toBeFalsy();
    });
  });


  it('should edit a task and update BehaviorSubject', () => {
    const updatedTask: Task = { id: 1, name: 'Updated Task', done: true, created: new Date(), priority: TaskPriority.URGENT };

    const reqInit = httpTestingController.expectOne(service.apiUrl);
    expect(reqInit.request.method).toEqual('GET');
    reqInit.flush([updatedTask]);

    service.editTask(updatedTask).subscribe(task => {
      expect(task).toEqual(updatedTask);
    });

    const reqEdit = httpTestingController.expectOne(`${service.apiUrl}/${updatedTask.id}`);
    expect(reqEdit.request.method).toEqual('PUT');
    reqEdit.flush(updatedTask);

    service.tasks$.subscribe(tasks => {
      expect(tasks.some(task => task.id === updatedTask.id && task.done)).toBeTruthy();
    });
  });

  afterEach(() => {
    httpTestingController.verify();
  });

});
