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
}