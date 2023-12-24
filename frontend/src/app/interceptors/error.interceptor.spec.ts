import { TestBed } from '@angular/core/testing';
import { HTTP_INTERCEPTORS, HttpClient } from '@angular/common/http';
import {HttpClientTestingModule, HttpTestingController} from '@angular/common/http/testing';
import { ErrorInterceptor } from './error.interceptor';
import {ErrorHandlerService} from "../services/error-handler.service";

describe('ErrorInterceptor', () => {
  let httpClient: HttpClient;
  let httpMock: HttpTestingController;
  let errorHandlerService: ErrorHandlerService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [
        ErrorHandlerService,
        {
          provide: HTTP_INTERCEPTORS,
          useClass: ErrorInterceptor,
          multi: true
        }
      ]
    });

    httpClient = TestBed.inject(HttpClient);
    httpMock = TestBed.inject(HttpTestingController);
    errorHandlerService = TestBed.inject(ErrorHandlerService);
  });

  it('should catch HTTP errors and delegate to ErrorHandlerService', () => {
    const spy = spyOn(errorHandlerService, 'handleError').and.callThrough();
    const testUrl = '/test';

    httpClient.get(testUrl).subscribe({
      error: (error) => {
        expect(spy).toHaveBeenCalled();
        expect(error).toBeTruthy();
      }
    });

    const req = httpMock.expectOne(testUrl);
    req.flush('Error', { status: 500, statusText: 'Internal Server Error' });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
