import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { jwtDecode } from 'jwt-decode';
import { Observable, tap } from 'rxjs';

interface LoginResponse {
  token: string;
  refreshToken: string;
}

interface JwtPayload {
  sub: string; // username
  exp: number;
  iat: number;
}

@Injectable({ providedIn: 'root' })
export class AuthService {
  private http = inject(HttpClient);
  private router = inject(Router);

  private LOGIN_URL = 'http://localhost:8080/api/auth/login';
  private tokenKey = 'authToken';

  login(username: string, password: string): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(this.LOGIN_URL, { username, password }).pipe(
      tap((response) => {
        this.setToken(response.token);
        this.router.navigate(['/dashboard']);
      })
    );
  }

  private setToken(token: string): void {
    localStorage.setItem(this.tokenKey, token);
  }

  private getToken(): string | null {
    return localStorage.getItem(this.tokenKey);
  }

  logout(): void {
    localStorage.removeItem(this.tokenKey);
    this.router.navigate(['/login']);
  }

  isAuthenticated(): boolean {
    return !!this.getToken();
  }
  getUsername(): string | null {
    const token = this.getToken();
    if (!token) return null;

    try {
      const payload = jwtDecode<JwtPayload>(token);
      return payload.sub; // el username que pusimos en el JWT
    } catch (err) {
      return null;
    }
  }
}
