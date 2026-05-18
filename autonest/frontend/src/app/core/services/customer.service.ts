import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Customer, CustomerRequest, GenericResponse } from '../models';

@Injectable({ providedIn: 'root' })
export class CustomerService {
  private readonly http = inject(HttpClient);
  private readonly apiUrl = 'http://localhost:8080/api/customers';

  getAll(): Observable<GenericResponse<Customer[]>> {
    return this.http.get<GenericResponse<Customer[]>>(this.apiUrl);
  }

  getById(id: number): Observable<GenericResponse<Customer>> {
    return this.http.get<GenericResponse<Customer>>(`${this.apiUrl}/${id}`);
  }

  create(request: CustomerRequest): Observable<GenericResponse<Customer>> {
    return this.http.post<GenericResponse<Customer>>(this.apiUrl, request);
  }

  update(id: number, request: CustomerRequest): Observable<GenericResponse<Customer>> {
    return this.http.put<GenericResponse<Customer>>(`${this.apiUrl}/${id}`, request);
  }

  delete(id: number): Observable<GenericResponse<void>> {
    return this.http.delete<GenericResponse<void>>(`${this.apiUrl}/${id}`);
  }

  search(name: string): Observable<GenericResponse<Customer[]>> {
    const params = new HttpParams().set('name', name);
    return this.http.get<GenericResponse<Customer[]>>(`${this.apiUrl}/search`, { params });
  }
}
