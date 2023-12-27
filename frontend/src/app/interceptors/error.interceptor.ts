import { Injectable } from '@angular/core';
import {HttpInterceptor, HttpRequest, HttpHandler, HttpEvent} from '@angular/common/http';
import {catchError, tap} from 'rxjs/operators';
import { ErrorHandlerService } from "../services/error-handler.service";
import {Observable} from "rxjs";

@Injectable()
export class ErrorInterceptor implements HttpInterceptor {
  constructor(private errorHandlerService: ErrorHandlerService) {}

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    console.log('Outgoing HTTP request', req);
    return next.handle(req).pipe(
      tap((event: HttpEvent<any>) => {
        console.log('Incoming HTTP response', event);
      }),
      catchError(error => {
          return this.errorHandlerService.handleError(error);
      })
    );
  }
}
