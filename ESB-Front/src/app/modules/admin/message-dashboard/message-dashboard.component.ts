import { Component } from '@angular/core';
import { MessageDashboardService } from './message-dashboard.service';
import { MessageTableRefreshService } from 'app/modules/admin/dashboards/project/message-table-refresh.service';

interface AppCard {
  id: number;
  name: string;
  icon: string;
  color: string;
  status: string;
  statusColor: string;
}

@Component({
  selector: 'app-message-dashboard',
  templateUrl: './message-dashboard.component.html',
  styleUrls: ['./message-dashboard.component.scss']
})
export class MessageDashboardComponent {
  apps: AppCard[] = [
    {
      id: 1,
      name: 'Text',
      icon: 'person',
      color: '#FEE2E2', // light red
      status: 'Message',
      statusColor: '#EF4444',
    },
    {
      id: 2,
      name: 'XML',
      icon: 'person',
      color: '#DCFCE7', // light green
      status: 'Message',
      statusColor: '#22C55E',
    },
    {
      id: 3,
      name: 'JSON',
      icon: 'person',
      color: '#DBEAFE', // light blue
      status: 'Message',
      statusColor: '#3B82F6',
    }
  ];

  expandedAppId: number | null = null;

  appMessages = ['', '', ''];
  appResponses = ['', '', ''];
  isLoading = [false, false, false];

  constructor(
    private messageService: MessageDashboardService,
    private tableRefreshService: MessageTableRefreshService
  ) {}

  expandCard(appId: number) {
    this.expandedAppId = this.expandedAppId === appId ? null : appId;
  }

  sendMessage(appIndex: number) {
    this.isLoading[appIndex] = true;
    this.appResponses[appIndex] = '';
    let send$;
    if (appIndex === 0) {
      send$ = this.messageService.sendToApp1(this.appMessages[appIndex]);
    } else if (appIndex === 1) {
      send$ = this.messageService.sendToApp2(this.appMessages[appIndex]);
    } else {
      send$ = this.messageService.sendToApp3(this.appMessages[appIndex]);
    }
    const appName = this.apps[appIndex].name;
    const sentMessage = this.appMessages[appIndex];
    send$.subscribe({
      next: (response) => {
        this.appResponses[appIndex] = response;
        this.isLoading[appIndex] = false;
        this.tableRefreshService.triggerRefresh(); // Trigger table refresh
      },
      error: (err) => {
        const errorMsg = 'Error: ' + (err?.error || err?.message || 'Unknown error');
        this.appResponses[appIndex] = errorMsg;
        this.isLoading[appIndex] = false;
      }
    });
  }
} 