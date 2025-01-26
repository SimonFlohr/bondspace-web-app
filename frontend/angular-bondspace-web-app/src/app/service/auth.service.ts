import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable, tap } from 'rxjs';

const httpOptions = {
  headers: new HttpHeaders({ 'Content-Type': 'application/json' }),
  withCredentials: true  // Add this
};

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  
  // FIELDS

  private readonly API_URL = 'http://localhost:8080/api/auth';
  private isAuthenticatedSubject = new BehaviorSubject<boolean>(false);

  // CONSTRUCTORS

  constructor(private http: HttpClient) {
    // Check the initial authentication state
    this.checkAuthStatus();
  }

  // METHODS

  login(email: string, password: string): Observable<any> {
    return this.http.post(`${this.API_URL}/login`, { emailAddress: email, password }, httpOptions)
      .pipe(
        tap(() => {
          this.isAuthenticatedSubject.next(true)
        })
      );
  }

  logout(): Observable<any> {
    return this.http.post(`${this.API_URL}/logout`, {}, httpOptions)
      .pipe(
        tap(() => {
          this.isAuthenticatedSubject.next(false);
        })
      );
  }

  register(email: string, password: string, firstName: string, lastName: string): Observable<any> {
    return this.http.post(`${this.API_URL}/register`, { emailAddress: email, password, firstName, lastName });
  }

  getCurrentUser(): Observable<{firstName: string}> {
    return this.http.get<{firstName: string}>(`${this.API_URL}/user`, httpOptions);
  }

  isAuthenticated(): Observable<boolean> {
    return this.isAuthenticatedSubject.asObservable();
  }

  private checkAuthStatus() {
    // Make call to the backend to verify this session
    this.http.get(`${this.API_URL}/status`, httpOptions).subscribe({
      next: () => this.isAuthenticatedSubject.next(true),
      error: () => this.isAuthenticatedSubject.next(false)
    });
  }
  
}
