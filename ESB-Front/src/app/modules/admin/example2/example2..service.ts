import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ElementsService {
  private apiUrl = 'http://localhost:8222/api/messages/getElements';

  constructor(private http: HttpClient) {}

  getElements(): Observable<Set<string>> {
    return this.http.get<Set<string>>(this.apiUrl);
  }

  
}