import { Component, inject, signal, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { TransactionService } from '../../../core/services/transaction.service';
import { AccountService } from '../../../core/services/account.service';
import { Account } from '../../../shared/models/account.model';
import { NavbarComponent } from '../../../shared/components/navbar/navbar.component';

/**
 * Fund transfer component.
 * Angular 17 standalone with signals.
 */
@Component({
  selector: 'app-transfer',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink, NavbarComponent],
  template: `
    <div class="min-h-screen bg-gray-50">
      <app-navbar></app-navbar>

      <main class="max-w-lg mx-auto px-4 py-8">
        <div class="mb-6">
          <h1 class="text-2xl font-bold text-slate-800">Fund Transfer</h1>
          <p class="text-slate-500 mt-1">Transfer money between accounts instantly</p>
        </div>

        <div class="bg-white rounded-xl shadow-sm border border-slate-100 p-6">
          @if (successMessage()) {
            <div class="mb-4 p-4 bg-green-50 border border-green-200 rounded-lg">
              <p class="text-green-700 font-medium">Transfer Successful!</p>
              <p class="text-green-600 text-sm mt-1">{{ successMessage() }}</p>
            </div>
          }

          @if (errorMessage()) {
            <div class="mb-4 p-4 bg-red-50 border border-red-200 rounded-lg">
              <p class="text-red-600 text-sm">{{ errorMessage() }}</p>
            </div>
          }

          <form [formGroup]="form" (ngSubmit)="onSubmit()">
            <div class="mb-4">
              <label class="block text-sm font-medium text-slate-700 mb-2">From Account</label>
              <select formControlName="fromAccountId"
                class="w-full px-4 py-3 border border-slate-300 rounded-lg focus:ring-2 focus:ring-blue-500 outline-none">
                <option value="">Select source account</option>
                @for (acc of accounts(); track acc.id) {
                  <option [value]="acc.id">
                    {{ acc.accountNumber }} — &#36;{{ acc.balance | number:'1.2-2' }} ({{ acc.accountType }})
                  </option>
                }
              </select>
            </div>

            <div class="mb-4">
              <label class="block text-sm font-medium text-slate-700 mb-2">To Account ID</label>
              <input type="number" formControlName="toAccountId" placeholder="Destination account ID"
                class="w-full px-4 py-3 border border-slate-300 rounded-lg focus:ring-2 focus:ring-blue-500 outline-none" />
            </div>

            <div class="mb-4">
              <label class="block text-sm font-medium text-slate-700 mb-2">Amount (&#36;)</label>
              <input type="number" formControlName="amount" placeholder="0.00" min="0.01" step="0.01"
                class="w-full px-4 py-3 border border-slate-300 rounded-lg focus:ring-2 focus:ring-blue-500 outline-none" />
            </div>

            <div class="mb-6">
              <label class="block text-sm font-medium text-slate-700 mb-2">Description (optional)</label>
              <input type="text" formControlName="description" placeholder="e.g. Rent payment"
                class="w-full px-4 py-3 border border-slate-300 rounded-lg focus:ring-2 focus:ring-blue-500 outline-none" />
            </div>

            <button type="submit" [disabled]="isLoading() || form.invalid"
              class="w-full py-3 bg-blue-600 hover:bg-blue-700 disabled:bg-blue-300 text-white font-semibold rounded-lg transition">
              {{ isLoading() ? 'Processing...' : 'Transfer Funds' }}
            </button>
          </form>
        </div>
      </main>
    </div>
  `
})
export class TransferComponent implements OnInit {
  private transactionService = inject(TransactionService);
  private accountService = inject(AccountService);
  private fb = inject(FormBuilder);

  accounts = signal<Account[]>([]);
  isLoading = signal(false);
  errorMessage = signal('');
  successMessage = signal('');

  form = this.fb.group({
    fromAccountId: ['', Validators.required],
    toAccountId: ['', Validators.required],
    amount: [null, [Validators.required, Validators.min(0.01)]],
    description: ['']
  });

  ngOnInit(): void {
    this.accountService.getMyAccounts().subscribe({
      next: res => this.accounts.set(res.data?.filter(a => a.status === 'ACTIVE') || [])
    });
  }

  onSubmit(): void {
    if (this.form.invalid) return;
    this.isLoading.set(true);
    this.errorMessage.set('');
    this.successMessage.set('');

    const { fromAccountId, toAccountId, amount, description } = this.form.value;
    this.transactionService.transfer({
      fromAccountId: Number(fromAccountId),
      toAccountId: Number(toAccountId),
      amount: Number(amount),
      description: description || ''
    }).subscribe({
      next: (res) => {
        this.isLoading.set(false);
        this.successMessage.set(`Transferred &#36;${amount} successfully.`);
        this.form.reset();
      },
      error: (err) => {
        this.isLoading.set(false);
        this.errorMessage.set(err.error?.message || 'Transfer failed. Please try again.');
      }
    });
  }
}
