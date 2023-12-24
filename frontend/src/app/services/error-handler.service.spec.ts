import { ErrorHandlerService } from './error-handler.service';

describe('ErrorHandlerService', () => {
  let service: ErrorHandlerService;

  beforeEach(() => {
    service = new ErrorHandlerService();
  });

  it('should log and rethrow the error', (done) => {
    const testError = new Error('Test error');
    spyOn(console, 'error');

    service.handleError(testError).subscribe({
      error: (error: any) => {
        expect(console.error).toHaveBeenCalledWith('Global error handling:', testError);
        expect(error).toBe(testError);
        done();
      }
    });
  });
});
