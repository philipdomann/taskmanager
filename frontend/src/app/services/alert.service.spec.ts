import { TestBed } from '@angular/core/testing';
import { AlertService } from './alert.service';

describe('AlertService', () => {
  let service: AlertService;
  let originalTimeout: number;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AlertService);

    // Extend default Jasmine timeout to accommodate the delay in showError
    originalTimeout = jasmine.DEFAULT_TIMEOUT_INTERVAL;
    jasmine.DEFAULT_TIMEOUT_INTERVAL = 7000;
  });

  afterEach(() => {
    // Reset the original Jasmine timeout after the tests
    jasmine.DEFAULT_TIMEOUT_INTERVAL = originalTimeout;
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should display an error and hide it after 5 seconds', (done: DoneFn) => {
    const testMessage = 'Test error message';
    service.showError(testMessage);

    expect(service.showAlert).toBeTrue();
    expect(service.alertMessage).toBe(testMessage);

    setTimeout(() => {
      expect(service.showAlert).toBeFalse();
      done();
    }, 5000);
  });
});
