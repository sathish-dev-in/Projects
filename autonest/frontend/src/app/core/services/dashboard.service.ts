import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { DashboardStats, GenericResponse } from '../models';

@Injectable({ providedIn: 'root' })
export class DashboardService {
  private readonly http = inject(HttpClient);
  private readonly apiUrl = 'http://localhost:8080/api/dashboard';

  getStats(): Observable<GenericResponse<DashboardStats>> {
    return this.http.get<GenericResponse<DashboardStats>>(`${this.apiUrl}/stats`);
  }
}
