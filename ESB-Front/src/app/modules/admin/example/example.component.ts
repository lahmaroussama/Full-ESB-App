// app.component.ts
import { Component, OnInit ,ViewEncapsulation } from '@angular/core';
import { WebSocketService } from './example..service';

@Component({
  selector     : 'example',
    standalone   : true,
    templateUrl  : './example.component.html',
    encapsulation: ViewEncapsulation.None,
})
export class ExampleComponent implements OnInit {
  originalMessage: string = '';
  transformedMessage: string = '';

  constructor(private webSocketService: WebSocketService) {}

  ngOnInit(): void {
    this.webSocketService.connect('ws://localhost:8091/ws/messages');
    this.webSocketService.onMessage().subscribe((message: string) => {
      this.originalMessage = message;
      console.log('Original message:', message);
    });
    this.webSocketService.onTransformedMessage().subscribe((message: string) => {
      this.transformedMessage = message;
      console.log('Transformed message:', message);
    });
  }
}