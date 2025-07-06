import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class FileUploadService {

  private uploadUrl = 'http://localhost:8222/messages/uploadFile'; 

  constructor(private http: HttpClient) { }

  uploadFile(file: File, uploadDir: string): Observable<any> {
    const formData: FormData = new FormData();
    formData.append('file', file);
    formData.append('uploadDir', uploadDir);

    return this.http.post(this.uploadUrl, formData, {
      headers: new HttpHeaders({
        // 'Content-Type': 'multipart/form-data' // Not needed, Angular will set this automatically
      }),
      responseType: 'text' // Adjust this if your backend returns a different type
    });
  }
}
