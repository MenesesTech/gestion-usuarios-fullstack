import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { User } from '../../../models/user.model';

@Injectable({ providedIn: 'root' })
export class UserService {
  private http = inject(HttpClient);
  private API_URL = 'http://localhost:8080/api/users';

  getUsers(): Observable<User[]> {
    return this.http.get<User[]>(this.API_URL);
  }
  createUser(data: { username: string; email: string; password: string; estado: boolean }) {
    return this.http.post(`${this.API_URL}`, data);
  }

  updateUser(
    id: number,
    data: { username: string; email: string; password?: string; estado?: boolean }
  ) {
    return this.http.put(`${this.API_URL}/${id}`, data);
  }

  deleteUser(id: number): Observable<void> {
    return this.http.delete<void>(`${this.API_URL}/${id}`);
  }
}
