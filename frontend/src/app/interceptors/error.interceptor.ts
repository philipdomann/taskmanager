import { Injectable } from '@angular/core';
import {HttpInterceptor, HttpRequest, HttpHandler, HttpErrorResponse, HttpEvent} from '@angular/common/http';
import {catchError, tap} from 'rxjs/operators';
import { ErrorHandlerService } from "../services/error-handler.service";

@Injectable()
export class ErrorInterceptor implements HttpInterceptor {
  constructor(private errorHandlerService: ErrorHandlerService) {}

  intercept(req: HttpRequest<any>, next: HttpHandler) {
    console.log('Outgoing HTTP request', req);
    return next.handle(req).pipe(
      tap((event: HttpEvent<any>) => {
        console.log('Incoming HTTP response', event);
      }),
      catchError(error => {
        if (error instanceof HttpErrorResponse) {
          // Handle HTTP errors
          return this.errorHandlerService.handleError(error);
        } else {
          // Handle non-HTTP errors
          return this.errorHandlerService.handleError(new Error(error.message));
        }
      })
    );
  }
}
