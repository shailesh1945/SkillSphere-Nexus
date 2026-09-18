import {
  ApplicationConfig,
  inject,
   provideAppInitializer,
  provideZoneChangeDetection
} from '@angular/core';

import {
  provideHttpClient,
  withInterceptors
} from '@angular/common/http';

import {
  provideRouter
} from '@angular/router';

import { routes } from './app.routes';
import { KeycloakAuthService } from './services/keycloak-auth.service';
import { authInterceptor } from './interceptors/auth.interceptor';

// export function initializeKeycloak(): () => Promise<boolean> {
//   const authService = inject(KeycloakAuthService);

//   return () => authService.init();
// }

export const appConfig: ApplicationConfig = {
  providers: [

    provideZoneChangeDetection({
      eventCoalescing: true
    }),

    provideHttpClient(
      withInterceptors([
        authInterceptor
      ])
    ),

     provideAppInitializer(() => {
      const keycloakAuthService =
        inject(KeycloakAuthService);

      return keycloakAuthService.init();
    }),

    provideRouter(routes)
  ]
};