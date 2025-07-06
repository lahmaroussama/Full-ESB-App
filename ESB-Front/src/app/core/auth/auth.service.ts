import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { AuthUtils } from 'app/core/auth/auth.utils';
import { UserService } from 'app/core/user/user.service';
import { catchError, Observable, of, switchMap, throwError } from 'rxjs';

@Injectable({providedIn: 'root'})
export class AuthService
{
    private _authenticated: boolean = false;
    private _httpClient = inject(HttpClient);
    private _userService = inject(UserService);
    
    // Backend API base URL
    private readonly API_BASE_URL = 'http://localhost:8090/api/v1/users';

    // -----------------------------------------------------------------------------------------------------
    // @ Accessors
    // -----------------------------------------------------------------------------------------------------

    /**
     * Setter & getter for access token
     */
    set accessToken(token: string)
    {
        localStorage.setItem('accessToken', token);
    }

    get accessToken(): string
    {
        return localStorage.getItem('accessToken') ?? '';
    }

    // -----------------------------------------------------------------------------------------------------
    // @ Public methods
    // -----------------------------------------------------------------------------------------------------

    /**
     * Forgot password
     *
     * @param email
     */
    forgotPassword(email: string): Observable<any>
    {
        return this._httpClient.post('api/auth/forgot-password', email);
    }

    /**
     * Reset password
     *
     * @param password
     */
    resetPassword(password: string): Observable<any>
    {
        return this._httpClient.post('api/auth/reset-password', password);
    }

    /**
     * Sign in
     *
     * @param credentials
     */
    signIn(credentials: { username: string; password: string }): Observable<any>
    {
        // Throw error, if the user is already logged in
        if ( this._authenticated )
        {
            return throwError('User is already logged in.');
        }

        return this._httpClient.post(`${this.API_BASE_URL}/signin`, credentials).pipe(
            switchMap((response: any) =>
            {
                // Store the access token in the local storage
                // Backend returns 'token' not 'accessToken'
                this.accessToken = response.token || response.accessToken;

                // Set the authenticated flag to true
                this._authenticated = true;

                // Store the user on the user service
                // Backend response includes user info directly
                const user = {
                    id: response.id.toString(),
                    name: response.username,
                    email: response.email,
                    avatar: undefined,
                    status: 'online'
                };
                this._userService.user = user;

                // Return a new observable with the response
                return of(response);
            }),
        );
    }

    /**
     * Sign in using the access token
     */
    signInUsingToken(): Observable<any>
    {
        // For now, we'll use a simple approach to validate the token
        // In a real implementation, you might want to call a backend endpoint to validate the token
        if (!this.accessToken) {
            return of(false);
        }

        // Check if token is expired
        if (AuthUtils.isTokenExpired(this.accessToken)) {
            return of(false);
        }  

        // For now, we'll assume the token is valid if it exists and isn't expired
        // You might want to call your backend to validate the token
        this._authenticated = true;
        return of(true);
    }

    /**
     * Sign out
     */
    signOut(): Observable<any>
    {
        // Remove the access token from the local storage
        localStorage.removeItem('accessToken');

        // Set the authenticated flag to false
        this._authenticated = false;

        // Return the observable
        return of(true);
    }

    /**
     * Sign up
     *
     * @param user - matches backend SignupRequest format
     */
    signUp(user: { username: string; email: string; password: string; role?: string[] }): Observable<any>
    {
        return this._httpClient.post(`${this.API_BASE_URL}/signup`, user);
    }

    /**
     * Unlock session
     *
     * @param credentials
     */
    unlockSession(credentials: { username: string; password: string }): Observable<any>
    {
        // Use the signin endpoint for unlocking session since backend doesn't have specific unlock endpoint
        return this._httpClient.post(`${this.API_BASE_URL}/signin`, credentials).pipe(
            switchMap((response: any) =>
            {
                // Store the access token in the local storage
                this.accessToken = response.token || response.accessToken;

                // Set the authenticated flag to true
                this._authenticated = true;

                // Store the user on the user service
                const user = {
                    id: response.id.toString(),
                    name: response.username,
                    email: response.email,
                    avatar: undefined,
                    status: 'online'
                };
                this._userService.user = user;

                // Return a new observable with the response
                return of(response);
            }),
        );
    }

    /**
     * Check the authentication status
     */
    check(): Observable<boolean>
    {
        // Check if the user is logged in
        if ( this._authenticated )
        {
            return of(true);
        }

        // Check the access token availability
        if ( !this.accessToken )
        {
            return of(false);
        }

        // Check the access token expire date
        if ( AuthUtils.isTokenExpired(this.accessToken) )
        {
            return of(false);
        }

        // If the access token exists, and it didn't expire, sign in using it
        return this.signInUsingToken();
    }
}