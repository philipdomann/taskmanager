import { Injectable } from '@angular/core';
import {HttpInterceptor, HttpRequest, HttpHandler, HttpEvent} from '@angular/common/http';
import {catchError, tap} from 'rxjs/operators';
import { ErrorHandlerService } from "../services/error-handler.service";
import {Observable} from "rxjs";
import {AlertService} from "../services/alert.service";

@Injectable()
export class ErrorInterceptor implements HttpInterceptor {
  constructor(private errorHandlerService: ErrorHandlerService, private alertService: AlertService) {}

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    console.log('Outgoing HTTP request', req);
    return next.handle(req).pipe(
      tap((event: HttpEvent<any>) => {
        console.log('Incoming HTTP response', event);
      }),
      catchError(error => {
          this.alertService.showError("Server is not reachable.");
          return this.errorHandlerService.handleError(error);
      })
    );
  }
}
