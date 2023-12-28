import { Injectable } from '@angular/core';
import { HttpInterceptor, HttpRequest, HttpHandler, HttpEvent, HttpErrorResponse } from '@angular/common/http';
import { catchError, tap } from 'rxjs/operators';
import { ErrorHandlerService } from "../services/error-handler.service";
import { Observable } from "rxjs";
import { AlertService } from "../services/alert.service";

@Injectable()
export class ErrorInterceptor implements HttpInterceptor {
    constructor(private errorHandlerService: ErrorHandlerService, private alertService: AlertService) {}

    intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        console.log('Outgoing HTTP request', req);
        return next.handle(req).pipe(
            tap((event: HttpEvent<any>) => {
                console.log('Incoming HTTP response', event);
            }),
            catchError((error: HttpErrorResponse) => {
                if (error.status === 500) {
                    this.alertService.showError("Server error happened.");
                } else if (error.status === 400) {
                    this.alertService.showError("Saving task failed.");
                } else {
                    this.alertService.showError("An unexpected error occurred.");
                }
                return this.errorHandlerService.handleError(error);
            })
        );
    }
}
