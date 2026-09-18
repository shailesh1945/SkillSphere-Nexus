import { Component, inject } from '@angular/core';
import { RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';

import { KeycloakAuthService } from './services/keycloak-auth.service';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [
    RouterLink,
    RouterLinkActive,
    RouterOutlet
  ],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss'
})
export class AppComponent {

  authService = inject(KeycloakAuthService);

  get username(): string {
    return this.authService.getUsername() ?? 'Guest';
  }

  get isAdmin(): boolean {
    return this.authService.hasRole('ROLE_ADMIN');
  }

  get isHr(): boolean {
    return this.authService.hasRole('ROLE_HR');
  }

  get isTrainingManager(): boolean {
    return this.authService.hasRole('ROLE_TRAINING_MANAGER');
  }

  async logout(): Promise<void> {
    await this.authService.logout();
  }
}