import { Component, inject, signal, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink, ActivatedRoute } from '@angular/router';
import { TransactionService } from '../../../core/services/transaction.service';
import { Transaction } from '../../../shared/models/transaction.model';
import { NavbarComponent } from '../../../shared/components/navbar/navbar.component';

/**
 * Transaction history component.
 * Angular 17 standalone with signals and reactive state.
 */
@Component({
  selector: 'app-transaction-history',
  standalone: true,
  imports: [CommonModule, RouterLink, NavbarComponent],
  template: `
    <div class="min-h-screen bg-gray-50">
      <app-navbar></app-navbar>

      <main class="max-w-4xl mx-auto px-4 py-8">
        <div class="flex items-center justify-between mb-6">
          <div>
            <a routerLink="/accounts" class="text-blue-600 hover:underline text-sm">&larr; Back to accounts</a>
            <h1 class="text-2xl font-bold text-slate-800 mt-2">Transaction History</h1>
          </div>
        </div>

        <div class="bg-white rounded-xl shadow-sm border border-slate-100">
          @if (isLoading()) {
            <div class="p-8 text-center text-slate-500">Loading transactions...</div>
          } @else if (transactions().length === 0) {
            <div class="p-8 text-center text-slate-500">No transactions found.</div>
          } @else {
            <div class="divide-y divide-slate-100">
              @for (tx of transactions(); track tx.id) {
                <div class="p-6 flex items-center justify-between">
                  <div class="flex items-center gap-4">
                    <div class="w-10 h-10 rounded-full flex items-center justify-center"
                      [class]="tx.status === 'COMPLETED' ? 'bg-green-100' : 'bg-red-100'">
                      <span [class]="tx.status === 'COMPLETED' ? 'text-green-600' : 'text-red-600'"
                        class="text-sm font-bold">
                        {{ tx.type[0] }}
                      </span>
                    </div>
                    <div>
                      <p class="font-medium text-slate-800">{{ tx.typeDescription }}</p>
                      <p class="text-sm text-slate-500">
                        {{ tx.fromAccountNumber }} &rarr; {{ tx.toAccountNumber }}
                      </p>
                      <p class="text-xs text-slate-400 mt-0.5">{{ tx.description }}</p>
                    </div>
                  </div>
                  <div class="text-right">
                    <p class="font-bold text-lg"
                      [class]="tx.status === 'COMPLETED' ? 'text-slate-800' : 'text-red-500'">
                      &#36;{{ tx.amount | number:'1.2-2' }}
                    </p>
                    <span class="text-xs px-2 py-1 rounded-full"
                      [class]="getStatusClass(tx.status)">
                      {{ tx.status }}
                    </span>
                    <p class="text-xs text-slate-400 mt-1">{{ tx.createdAt | date:'short' }}</p>
                  </div>
                </div>
              }
            </div>
          }
        </div>
      </main>
    </div>
  `
})
export class TransactionHistoryComponent implements OnInit {
  private transactionService = inject(TransactionService);
  private route = inject(ActivatedRoute);

  transactions = signal<Transaction[]>([]);
  isLoading = signal(true);

  ngOnInit(): void {
    const accountId = this.route.snapshot.paramMap.get('accountId');
    if (accountId) {
      this.transactionService.getTransactionsByAccount(Number(accountId)).subscribe({
        next: res => {
          this.transactions.set(res.data || []);
          this.isLoading.set(false);
        },
        error: () => this.isLoading.set(false)
      });
    }
  }

  getStatusClass(status: string): string {
    const map: Record<string, string> = {
      COMPLETED: 'bg-green-100 text-green-700',
      FAILED: 'bg-red-100 text-red-700',
      PENDING: 'bg-yellow-100 text-yellow-700',
      REVERSED: 'bg-slate-100 text-slate-700'
    };
    return map[status] || 'bg-slate-100 text-slate-700';
  }
}
