// memory.service.ts
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class MemoryService {
  private readonly API_URL = 'http://localhost:8080/api/memories';

  constructor(private http: HttpClient) {}

  createMemory(spaceId: number, name: string, tags: string[], textContent: string): Observable<any> {
    return this.http.post(`${this.API_URL}/create`, {
        spaceId,
        name,
        tags,
        textContent
    }, { withCredentials: true });
  }

  getMemoryById(memoryId: number): Observable<any> {
    return this.http.get(`${this.API_URL}/${memoryId}`, { withCredentials: true });
  }

  deleteMemory(memoryId: number): Observable<any> {
    return this.http.delete(`${this.API_URL}/${memoryId}`, { withCredentials: true });
  }

  getSpaceTags(spaceId: number): Observable<string[]> {
    return this.http.get<string[]>(`${this.API_URL}/space/${spaceId}/tags`, { withCredentials: true });
  }
}