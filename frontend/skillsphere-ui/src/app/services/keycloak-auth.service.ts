import { Injectable } from '@angular/core';
import Keycloak from 'keycloak-js';

@Injectable({
  providedIn: 'root'
})
export class KeycloakAuthService {

  private keycloak = new Keycloak({
    url: 'http://localhost:8081',
    realm: 'skillsphere',
    clientId: 'skillsphere-frontend'
  });

  async init(): Promise<boolean> {
    return this.keycloak.init({
      onLoad: 'login-required',
      checkLoginIframe: false
    });
  }

  async updateToken(): Promise<boolean> {
    return this.keycloak.updateToken(30);
  }

  getToken(): string | undefined {
    return this.keycloak.token;
  }

  getUsername(): string | undefined {
    return this.keycloak.tokenParsed?.['preferred_username'];
  }

  getRoles(): string[] {
    return this.keycloak.realmAccess?.roles ?? [];
  }

  hasRole(role: string): boolean {
    return this.getRoles().includes(role);
  }

  isLoggedIn(): boolean {
    return !!this.keycloak.authenticated;
  }

  async logout(): Promise<void> {
    await this.keycloak.logout({
      redirectUri: window.location.origin
    });
  }
}