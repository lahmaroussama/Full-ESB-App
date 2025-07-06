import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, catchError, throwError } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class MessageDashboardService {
  // Backend URLs now point to the gateway
  private app1Url = 'http://localhost:8222/appDemo_1/send';
  private app2Url = 'http://localhost:8222/appDemo_2/send';
  private app3Url = 'http://localhost:8222/appDemo_3/send';

  constructor(private http: HttpClient) {}

  sendToApp1(message: string): Observable<any> {
    return this.http.post(this.app1Url, message, { responseType: 'text' });
  }
  sendToApp2(message: string): Observable<any> {
    return this.http.post(this.app2Url, message, { responseType: 'text' });
  }
  sendToApp3(message: string): Observable<any> {
    return this.http.post(this.app3Url, message, { responseType: 'text' });
  }
} 