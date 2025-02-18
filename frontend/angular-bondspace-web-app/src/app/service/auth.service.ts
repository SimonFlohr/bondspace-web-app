import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BehaviorSubject, catchError, map, Observable, of, tap } from 'rxjs';

const httpOptions = {
  headers: new HttpHeaders({ 'Content-Type': 'application/json' }),
  withCredentials: true  // Add this
};

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  
  // FIELDS

  private readonly API_URL = '/api/auth';
  private isAuthenticatedSubject = new BehaviorSubject<boolean>(false);

  // CONSTRUCTORS

  constructor(private http: HttpClient) {
    // Check the initial authentication state
    this.checkInitialAuthStatus();
  }

  // METHODS

  private checkInitialAuthStatus(): void {
    this.http.get<void>(`${this.API_URL}/status`, { withCredentials: true }).pipe(
      map(() => true),
      catchError(() => of(false))
    ).subscribe(isAuthenticated => {
      this.isAuthenticatedSubject.next(isAuthenticated);
    });
  }

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

  checkAuthStatus(): Observable<boolean> {
    return this.http.get<void>(`${this.API_URL}/status`, { withCredentials: true }).pipe(
      map(() => true),
      catchError(() => of(false)),
      tap(isAuthenticated => {
        this.isAuthenticatedSubject.next(isAuthenticated);
      })
    );
  }
  
}
