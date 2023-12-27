import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class AlertService {
  public showAlert = false;
  public alertMessage = '';

  showError(message: string) {
    this.alertMessage = message;
    this.showAlert = true;
    setTimeout(() => this.showAlert = false, 5000);
  }
}
