import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Part, PartRequest, GenericResponse } from '../models';

@Injectable({ providedIn: 'root' })
export class PartService {
  private readonly http = inject(HttpClient);
  private readonly apiUrl = 'http://localhost:8080/api/parts';

  getAll(): Observable<GenericResponse<Part[]>> {
    return this.http.get<GenericResponse<Part[]>>(this.apiUrl);
  }

  getById(id: number): Observable<GenericResponse<Part>> {
    return this.http.get<GenericResponse<Part>>(`${this.apiUrl}/${id}`);
  }

  getLowStock(): Observable<GenericResponse<Part[]>> {
    return this.http.get<GenericResponse<Part[]>>(`${this.apiUrl}/low-stock`);
  }

  create(request: PartRequest): Observable<GenericResponse<Part>> {
    return this.http.post<GenericResponse<Part>>(this.apiUrl, request);
  }

  update(id: number, request: PartRequest): Observable<GenericResponse<Part>> {
    return this.http.put<GenericResponse<Part>>(`${this.apiUrl}/${id}`, request);
  }

  adjustStock(id: number, quantity: number, operation: string): Observable<GenericResponse<Part>> {
    const params = new HttpParams()
      .set('quantity', quantity.toString())
      .set('operation', operation);
    return this.http.patch<GenericResponse<Part>>(`${this.apiUrl}/${id}/stock`, null, { params });
  }

  delete(id: number): Observable<GenericResponse<void>> {
    return this.http.delete<GenericResponse<void>>(`${this.apiUrl}/${id}`);
  }
}
