import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { Account, CreateAccountRequest, DepositRequest } from '../../shared/models/account.model';

export interface ApiResponse<T> {
  success: boolean;
  message: string;
  data: T;
}

@Injectable({ providedIn: 'root' })
export class AccountService {
  private http = inject(HttpClient);

  getMyAccounts(): Observable<ApiResponse<Account[]>> {
    return this.http.get<ApiResponse<Account[]>>(`${environment.apiUrl}/api/accounts`);
  }

  getAccount(id: number): Observable<ApiResponse<Account>> {
    return this.http.get<ApiResponse<Account>>(`${environment.apiUrl}/api/accounts/${id}`);
  }

  createAccount(request: CreateAccountRequest): Observable<ApiResponse<Account>> {
    return this.http.post<ApiResponse<Account>>(`${environment.apiUrl}/api/accounts`, request);
  }

  deposit(accountId: number, request: DepositRequest): Observable<ApiResponse<Account>> {
    return this.http.post<ApiResponse<Account>>(
      `${environment.apiUrl}/api/accounts/${accountId}/deposit`,
      request
    );
  }
}
