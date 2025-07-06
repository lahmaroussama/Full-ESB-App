import { Injectable } from '@angular/core';
import { Observable, Subject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class WebSocketService {
  private socket: WebSocket;
  private messageSubject: Subject<string> = new Subject<string>();
  private transformedMessageSubject: Subject<string> = new Subject<string>();

  public connect(url: string): void {
    this.socket = new WebSocket(url);

    this.socket.onmessage = (event) => {
      const data = event.data;
      if (this.isTransformedMessage(data)) {
        this.transformedMessageSubject.next(data);
      } else {
        this.messageSubject.next(data);
      }
    };
  }

  public onMessage(): Observable<string> {
    return this.messageSubject.asObservable();
  }

  public onTransformedMessage(): Observable<string> {
    return this.transformedMessageSubject.asObservable();
  }

  private isTransformedMessage(message: string): boolean {
    // Add logic to differentiate between normal and transformed messages if needed
    return message.startsWith('Transformed:');
  }
}
