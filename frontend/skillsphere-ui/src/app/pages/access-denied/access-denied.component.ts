import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-access-denied',
  standalone: true,
  imports: [RouterLink],
  template: `
    <div>
      <h1>Access Denied</h1>

      <p>
        You do not have permission to access this page.
      </p>

      <a routerLink="/">
        Go to Dashboard
      </a>
    </div>
  `
})
export class AccessDeniedComponent {
}