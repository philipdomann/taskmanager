import { bootstrapApplication } from '@angular/platform-browser';
import {HTTP_INTERCEPTORS, HttpClientModule} from '@angular/common/http';
import { importProvidersFrom } from '@angular/core';
import {AppComponent} from "./app/app.component";
import {ErrorInterceptor} from "./app/interceptors/error.interceptor";

export const httpInterceptorProviders = [
    { provide: HTTP_INTERCEPTORS, useClass: ErrorInterceptor, multi: true }
];

bootstrapApplication(AppComponent, {
    providers: [
        importProvidersFrom(HttpClientModule),
        httpInterceptorProviders
    ]
});
