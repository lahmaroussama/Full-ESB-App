import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Notification } from 'app/layout/common/notifications/notifications.types';
import { map, Observable, ReplaySubject, switchMap, take, tap } from 'rxjs';

@Injectable({providedIn: 'root'})
export class NotificationsService
{
    private _notifications: ReplaySubject<Notification[]> = new ReplaySubject<Notification[]>(1);

    /**
     * Constructor
     */
    constructor(private _httpClient: HttpClient)
    {
    }

    // -----------------------------------------------------------------------------------------------------
    // @ Accessors
    // -----------------------------------------------------------------------------------------------------

    /**
     * Getter for notifications
     */
    get notifications$(): Observable<Notification[]>
    {
        return this._notifications.asObservable();
    }

    // -----------------------------------------------------------------------------------------------------
    // @ Public methods
    // -----------------------------------------------------------------------------------------------------

    /**
     * Get all notifications
     */
    getAll(): Observable<Notification[]>
    {
        return this._httpClient.get<Notification[]>('api/common/notifications').pipe(
            tap((notifications) =>
            {
                this._notifications.next(notifications);
            }),
        );
    }

    /**
     * Create a notification
     *
     * @param notification
     */
    create(notification: Notification): Observable<Notification>
    {
        return this.notifications$.pipe(
            take(1),
            switchMap(notifications => this._httpClient.post<Notification>('api/common/notifications', {notification}).pipe(
                map((newNotification) =>
                {
                    // Update the notifications with the new notification
                    this._notifications.next([...notifications, newNotification]);

                    // Return the new notification from observable
                    return newNotification;
                }),
            )),
        );
    }

    /**
     * Create a login notification
     *
     * @param username
     */
    createLoginNotification(username: string): Observable<Notification>
    {
        const loginNotification: Notification = {
            id: this._generateId(),
            icon: 'heroicons_mini:check-circle',
            title: 'Login Successful',
            description: `Welcome back, <strong>${username}</strong>! You have successfully logged in.`,
            time: new Date().toISOString(),
            read: false,
            link: '/dashboards/project',
            useRouter: true,
        };

        return this.create(loginNotification);
    }

    /**
     * Mark all notifications as read
     */
    markAllAsRead(): Observable<boolean>
    {
        return this._httpClient.get<boolean>('api/common/notifications/mark-all-as-read').pipe(
            tap(() =>
            {
                // Update all notifications to read
                this.notifications$.pipe(take(1)).subscribe(notifications =>
                {
                    const updatedNotifications = notifications.map(notification => ({
                        ...notification,
                        read: true
                    }));
                    this._notifications.next(updatedNotifications);
                });
            }),
        );
    }

    /**
     * Update the notification
     *
     * @param id
     * @param notification
     */
    update(id: string, notification: Notification): Observable<Notification>
    {
        return this.notifications$.pipe(
            take(1),
            switchMap(notifications => this._httpClient.patch<Notification>('api/common/notifications', {
                id,
                notification,
            }).pipe(
                map((updatedNotification: Notification) =>
                {
                    // Find the index of the updated notification
                    const index = notifications.findIndex(item => item.id === id);

                    // Update the notification
                    notifications[index] = updatedNotification;

                    // Update the notifications
                    this._notifications.next(notifications);

                    // Return the updated notification
                    return updatedNotification;
                }),
            )),
        );
    }

    /**
     * Delete the notification
     *
     * @param id
     */
    delete(id: string): Observable<boolean>
    {
        return this.notifications$.pipe(
            take(1),
            switchMap(notifications => this._httpClient.delete<boolean>('api/common/notifications', {params: {id}}).pipe(
                map((isDeleted: boolean) =>
                {
                    // Find the index of the deleted notification
                    const index = notifications.findIndex(item => item.id === id);

                    // Delete the notification
                    notifications.splice(index, 1);

                    // Update the notifications
                    this._notifications.next(notifications);

                    // Return the deleted status
                    return isDeleted;
                }),
            )),
        );
    }

    /**
     * Generate a unique ID
     */
    private _generateId(): string
    {
        return Math.random().toString(36).substring(2) + Date.now().toString(36);
    }
}
