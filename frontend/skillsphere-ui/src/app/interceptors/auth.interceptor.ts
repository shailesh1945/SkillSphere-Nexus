import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { from } from 'rxjs';
import { switchMap } from 'rxjs/operators';

import { KeycloakAuthService } from '../services/keycloak-auth.service';

export const authInterceptor: HttpInterceptorFn = (req, next) => {

  const authService = inject(KeycloakAuthService);

  const isKeycloakRequest =
    req.url.startsWith('http://localhost:8081');

  const isGatewayRequest =
    req.url.startsWith('http://localhost:8090');

  if (!isGatewayRequest || isKeycloakRequest) {
    return next(req);
  }

  return from(authService.updateToken()).pipe(
    switchMap(() => {

      const token = authService.getToken();

      if (!token) {
        return next(req);
      }

      const authenticatedRequest = req.clone({
        setHeaders: {
          Authorization: `Bearer ${token}`
        }
      });

      return next(authenticatedRequest);
    })
  );
};