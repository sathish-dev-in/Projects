import { Component, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AccountService } from '../../../core/services/account.service';
import { NavbarComponent } from '../../../shared/components/navbar/navbar.component';

/**
 * Account creation component.
 * Angular 17 standalone with reactive forms.
 */
@Component({
  selector: 'app-account-create',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink, NavbarComponent],
  template: `
    <div class="min-h-screen bg-gray-50">
      <app-navbar></app-navbar>

      <main class="max-w-lg mx-auto px-4 py-8">
        <div class="mb-6">
          <a routerLink="/accounts" class="text-blue-600 hover:underline text-sm">&larr; Back to accounts</a>
          <h1 class="text-2xl font-bold text-slate-800 mt-2">Open New Account</h1>
        </div>

        <div class="bg-white rounded-xl shadow-sm border border-slate-100 p-6">
          @if (successMessage()) {
            <div class="mb-4 p-3 bg-green-50 border border-green-200 rounded-lg">
              <p class="text-green-700 text-sm">{{ successMessage() }}</p>
            </div>
          }

          @if (errorMessage()) {
            <div class="mb-4 p-3 bg-red-50 border border-red-200 rounded-lg">
              <p class="text-red-600 text-sm">{{ errorMessage() }}</p>
            </div>
          }

          <form [formGroup]="form" (ngSubmit)="onSubmit()">
            <div class="mb-4">
              <label class="block text-sm font-medium text-slate-700 mb-2">Account Type</label>
              <select formControlName="accountType"
                class="w-full px-4 py-3 border border-slate-300 rounded-lg focus:ring-2 focus:ring-blue-500 outline-none">
                <option value="">Select account type</option>
                <option value="SAVINGS">Savings Account (3.5% p.a.)</option>
                <option value="CURRENT">Current Account (Business)</option>
                <option value="FIXED_DEPOSIT">Fixed Deposit (6.5% p.a.)</option>
              </select>
            </div>

            <div class="mb-6">
              <label class="block text-sm font-medium text-slate-700 mb-2">Initial Deposit (&#36;)</label>
              <input type="number" formControlName="initialDeposit" placeholder="0.00" min="0" step="0.01"
                class="w-full px-4 py-3 border border-slate-300 rounded-lg focus:ring-2 focus:ring-blue-500 outline-none" />
            </div>

            <button type="submit" [disabled]="isLoading() || form.invalid"
              class="w-full py-3 bg-blue-600 hover:bg-blue-700 disabled:bg-blue-300 text-white font-semibold rounded-lg transition">
              {{ isLoading() ? 'Opening account...' : 'Open Account' }}
            </button>
          </form>
        </div>
      </main>
    </div>
  `
})
export class AccountCreateComponent {
  private accountService = inject(AccountService);
  private router = inject(Router);
  private fb = inject(FormBuilder);

  form = this.fb.group({
    accountType: ['', Validators.required],
    initialDeposit: [0, [Validators.required, Validators.min(0)]]
  });

  isLoading = signal(false);
  errorMessage = signal('');
  successMessage = signal('');

  onSubmit(): void {
    if (this.form.invalid) return;
    this.isLoading.set(true);

    this.accountService.createAccount(this.form.value as any).subscribe({
      next: (res) => {
        this.isLoading.set(false);
        this.successMessage.set(`Account ${res.data.accountNumber} created successfully!`);
        setTimeout(() => this.router.navigate(['/accounts']), 1500);
      },
      error: (err) => {
        this.isLoading.set(false);
        this.errorMessage.set(err.error?.message || 'Failed to create account.');
      }
    });
  }
}
