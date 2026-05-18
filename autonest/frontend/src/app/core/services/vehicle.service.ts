import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Vehicle, VehicleRequest, GenericResponse } from '../models';

@Injectable({ providedIn: 'root' })
export class VehicleService {
  private readonly http = inject(HttpClient);
  private readonly apiUrl = 'http://localhost:8080/api/vehicles';

  getAll(): Observable<GenericResponse<Vehicle[]>> {
    return this.http.get<GenericResponse<Vehicle[]>>(this.apiUrl);
  }

  getById(id: number): Observable<GenericResponse<Vehicle>> {
    return this.http.get<GenericResponse<Vehicle>>(`${this.apiUrl}/${id}`);
  }

  getByCustomer(customerId: number): Observable<GenericResponse<Vehicle[]>> {
    return this.http.get<GenericResponse<Vehicle[]>>(`${this.apiUrl}/customer/${customerId}`);
  }

  create(request: VehicleRequest): Observable<GenericResponse<Vehicle>> {
    return this.http.post<GenericResponse<Vehicle>>(this.apiUrl, request);
  }

  update(id: number, request: VehicleRequest): Observable<GenericResponse<Vehicle>> {
    return this.http.put<GenericResponse<Vehicle>>(`${this.apiUrl}/${id}`, request);
  }

  delete(id: number): Observable<GenericResponse<void>> {
    return this.http.delete<GenericResponse<void>>(`${this.apiUrl}/${id}`);
  }
}
