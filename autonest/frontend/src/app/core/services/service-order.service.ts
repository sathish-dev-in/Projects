import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ServiceOrder, ServiceOrderRequest, ServiceStatus, GenericResponse } from '../models';

@Injectable({ providedIn: 'root' })
export class ServiceOrderService {
  private readonly http = inject(HttpClient);
  private readonly apiUrl = 'http://localhost:8080/api/service-orders';

  getAll(): Observable<GenericResponse<ServiceOrder[]>> {
    return this.http.get<GenericResponse<ServiceOrder[]>>(this.apiUrl);
  }

  getById(id: number): Observable<GenericResponse<ServiceOrder>> {
    return this.http.get<GenericResponse<ServiceOrder>>(`${this.apiUrl}/${id}`);
  }

  getActive(): Observable<GenericResponse<ServiceOrder[]>> {
    return this.http.get<GenericResponse<ServiceOrder[]>>(`${this.apiUrl}/active`);
  }

  getByStatus(status: ServiceStatus): Observable<GenericResponse<ServiceOrder[]>> {
    return this.http.get<GenericResponse<ServiceOrder[]>>(`${this.apiUrl}/status/${status}`);
  }

  create(request: ServiceOrderRequest): Observable<GenericResponse<ServiceOrder>> {
    return this.http.post<GenericResponse<ServiceOrder>>(this.apiUrl, request);
  }

  update(id: number, request: ServiceOrderRequest): Observable<GenericResponse<ServiceOrder>> {
    return this.http.put<GenericResponse<ServiceOrder>>(`${this.apiUrl}/${id}`, request);
  }

  updateStatus(id: number, status: ServiceStatus): Observable<GenericResponse<ServiceOrder>> {
    const params = new HttpParams().set('status', status);
    return this.http.patch<GenericResponse<ServiceOrder>>(`${this.apiUrl}/${id}/status`, null, { params });
  }

  delete(id: number): Observable<GenericResponse<void>> {
    return this.http.delete<GenericResponse<void>>(`${this.apiUrl}/${id}`);
  }
}
