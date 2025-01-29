import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class SpaceService {
  private readonly API_URL = 'http://localhost:8080/api/spaces';

  constructor(private http: HttpClient) {}

  createSpace(spaceName: string, spaceDescription: string): Observable<any> {
    return this.http.post(`${this.API_URL}/create`, {
      spaceName,
      spaceDescription
    }, { withCredentials: true });
  }

  deleteSpace(spaceId: number): Observable<any> {
    return this.http.delete(`${this.API_URL}/${spaceId}`, { withCredentials: true });
  }

  getUserSpaces(): Observable<any> {
    return this.http.get(`${this.API_URL}/user-spaces`, { withCredentials: true });
  }

  getSpaceDetails(spaceId: number): Observable<any> {
    return this.http.get(`${this.API_URL}/${spaceId}`, { withCredentials: true });
  }

  getSpaceMembers(spaceId: number): Observable<any> {
    return this.http.get(`${this.API_URL}/${spaceId}/members`, { withCredentials: true });
  }

  getSpaceMemories(spaceId: number): Observable<any> {
    return this.http.get(`${this.API_URL}/${spaceId}/memories`, { withCredentials: true });
  }

  inviteUser(spaceId: number, emailAddress: string): Observable<any> {
    return this.http.post(`${this.API_URL}/${spaceId}/invite`, {
      emailAddress
    }, { withCredentials: true });
  }

  getSpaceNotifications(spaceId: number): Observable<any> {
    return this.http.get(`${this.API_URL}/${spaceId}/notifications`, { withCredentials: true });
  }
}