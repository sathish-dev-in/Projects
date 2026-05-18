import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { Transaction, TransferRequest } from '../../shared/models/transaction.model';

export interface ApiResponse<T> {
  success: boolean;
  message: string;
  data: T;
}

@Injectable({ providedIn: 'root' })
export class TransactionService {
  private http = inject(HttpClient);

  transfer(request: TransferRequest): Observable<ApiResponse<Transaction>> {
    return this.http.post<ApiResponse<Transaction>>(
      `${environment.apiUrl}/api/transactions/transfer`,
      request
    );
  }

  getTransactionsByAccount(accountId: number): Observable<ApiResponse<Transaction[]>> {
    return this.http.get<ApiResponse<Transaction[]>>(
      `${environment.apiUrl}/api/transactions/account/${accountId}`
    );
  }

  getTransaction(id: number): Observable<ApiResponse<Transaction>> {
    return this.http.get<ApiResponse<Transaction>>(
      `${environment.apiUrl}/api/transactions/${id}`
    );
  }
}
