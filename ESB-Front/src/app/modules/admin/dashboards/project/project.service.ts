import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable, tap } from 'rxjs';

@Injectable({providedIn: 'root'})
export class ProjectService
{
    private _data: BehaviorSubject<any> = new BehaviorSubject(null);
    private baseUrl = 'http://localhost:8222';

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
     * Getter for data
     */
    get data$(): Observable<any>
    {
        return this._data.asObservable();
    }

    // -----------------------------------------------------------------------------------------------------
    // @ Public methods
    // -----------------------------------------------------------------------------------------------------

    /**
     * Get data
     */
    getData(): Observable<any>
    {
        return this._httpClient.get('api/dashboards/project').pipe(
            tap((response: any) =>
            {
                this._data.next(response);
            }),
        );
    }

    getMessageNumber(): Observable<any> {
        return this._httpClient.get(`${this.baseUrl}/messages/countAllMessages`); 
      }

    getCountsByContentType(): Observable<Map<string, number>> {
        return this._httpClient.get<Map<string, number>>(`${this.baseUrl}/messages/count-by-content-type`);
      }
    
      getTableData(): Observable<any[]> {
        return this._httpClient.get<any[]>(`${this.baseUrl}/messages/getAllMessages`);
    }
}
