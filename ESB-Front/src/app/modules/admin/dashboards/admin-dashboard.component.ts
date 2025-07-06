import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import { MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatTooltipModule } from '@angular/material/tooltip';
import { NgFor, NgIf } from '@angular/common';

@Component({
    selector: 'stack-admin-dashboard',
    templateUrl: './admin-dashboard.component.html',
    styleUrls: ['./admin-dashboard.component.scss'],
    encapsulation: ViewEncapsulation.None,
    standalone: true,
    imports: [MatTableModule, MatButtonModule, MatIconModule, MatTooltipModule, NgFor, NgIf],
})
export class AdminDashboardComponent implements OnInit {
    displayedColumns: string[] = ['name', 'email', 'role', 'status', 'actions'];
    users = [
        { name: 'Alice Smith', email: 'alice@stack.com', role: 'Admin', status: 'Active' },
        { name: 'Bob Johnson', email: 'bob@stack.com', role: 'User', status: 'Active' },
        { name: 'Carol Lee', email: 'carol@stack.com', role: 'User', status: 'Inactive' },
        { name: 'David Kim', email: 'david@stack.com', role: 'Admin', status: 'Active' },
    ];

    constructor() {}
    ngOnInit(): void {}

    editUser(user: any) {
        // Placeholder for edit action
        alert('Edit user: ' + user.name);
    }
    deleteUser(user: any) {
        // Placeholder for delete action
        alert('Delete user: ' + user.name);
    }
    toggleRole(user: any) {
        // Placeholder for promote/demote action
        alert('Toggle role for: ' + user.name);
    }
} 