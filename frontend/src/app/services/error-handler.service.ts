import { Injectable } from '@angular/core';
import { Observable, throwError } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ErrorHandlerService {
  constructor() {}

  public handleError(error: any): Observable<never> {
    // Log the error, send it to a monitoring service, etc.
    console.error('Global error handling:', error);

    // Forward the original error
    return throwError(() => error);
  }
}
