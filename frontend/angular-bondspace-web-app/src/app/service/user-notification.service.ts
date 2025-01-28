import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class UserNotificationService {
  private readonly API_URL = 'http://localhost:8080/api/notifications';

  constructor(private http: HttpClient) {}

  getUserNotifications(): Observable<any> {
    return this.http.get(`${this.API_URL}/user`, { withCredentials: true });
  }

  acceptInvite(notificationId: number): Observable<any> {
    return this.http.post(`${this.API_URL}/${notificationId}/accept`, {}, { withCredentials: true });
  }

  denyInvite(notificationId: number): Observable<any> {
    return this.http.post(`${this.API_URL}/${notificationId}/deny`, {}, { withCredentials: true });
  }
}
