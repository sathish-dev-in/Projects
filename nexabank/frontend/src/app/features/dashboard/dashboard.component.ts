import { Component, inject, signal, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { AccountService } from '../../core/services/account.service';
import { AuthService } from '../../core/services/auth.service';
import { Account } from '../../shared/models/account.model';
import { NavbarComponent } from '../../shared/components/navbar/navbar.component';

/**
 * Dashboard component showing account summary.
 * Uses Angular 17 signals for reactive state management.
 */
@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, RouterLink, NavbarComponent],
  template: `
    <div class="min-h-screen bg-gray-50">
      <app-navbar></app-navbar>

      <main class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        <!-- Welcome header -->
        <div class="mb-8">
          <h1 class="text-2xl font-bold text-slate-800">Welcome back!</h1>
          <p class="text-slate-500 mt-1">Here's an overview of your accounts</p>
        </div>

        <!-- Summary cards -->
        <div class="grid grid-cols-1 md:grid-cols-3 gap-6 mb-8">
          <div class="bg-white rounded-xl shadow-sm p-6 border border-slate-100">
            <p class="text-slate-500 text-sm">Total Balance</p>
            <p class="text-3xl font-bold text-slate-800 mt-1">
              &#36;{{ totalBalance() | number:'1.2-2' }}
            </p>
            <p class="text-green-600 text-sm mt-2">Across all accounts</p>
          </div>

          <div class="bg-white rounded-xl shadow-sm p-6 border border-slate-100">
            <p class="text-slate-500 text-sm">Active Accounts</p>
            <p class="text-3xl font-bold text-slate-800 mt-1">{{ activeAccountCount() }}</p>
            <a routerLink="/accounts/new" class="text-blue-600 text-sm mt-2 hover:underline block">+ Open new account</a>
          </div>

          <div class="bg-white rounded-xl shadow-sm p-6 border border-slate-100">
            <p class="text-slate-500 text-sm">Quick Transfer</p>
            <p class="text-slate-800 mt-1 font-medium">Send money instantly</p>
            <a routerLink="/transactions/transfer" class="text-blue-600 text-sm mt-2 hover:underline block">Transfer now &rarr;</a>
          </div>
        </div>

        <!-- Accounts list -->
        <div class="bg-white rounded-xl shadow-sm border border-slate-100">
          <div class="flex items-center justify-between p-6 border-b border-slate-100">
            <h2 class="text-lg font-semibold text-slate-800">Your Accounts</h2>
            <a routerLink="/accounts/new"
              class="px-4 py-2 bg-blue-600 text-white text-sm font-medium rounded-lg hover:bg-blue-700 transition">
              + New Account
            </a>
          </div>

          @if (isLoading()) {
            <div class="p-8 text-center text-slate-500">Loading accounts...</div>
          } @else if (accounts().length === 0) {
            <div class="p-8 text-center">
              <p class="text-slate-500">No accounts yet.</p>
              <a routerLink="/accounts/new" class="text-blue-600 hover:underline mt-2 block">Open your first account</a>
            </div>
          } @else {
            <div class="divide-y divide-slate-100">
              @for (account of accounts(); track account.id) {
                <div class="p-6 flex items-center justify-between hover:bg-slate-50 transition">
                  <div class="flex items-center gap-4">
                    <div class="w-12 h-12 rounded-xl flex items-center justify-center"
                      [class]="getAccountIconClass(account.accountType)">
                      <span class="text-white font-bold text-lg">{{ account.accountType[0] }}</span>
                    </div>
                    <div>
                      <p class="font-medium text-slate-800">{{ account.accountTypeDescription }}</p>
                      <p class="text-sm text-slate-500">{{ account.accountNumber }}</p>
                    </div>
                  </div>
                  <div class="text-right">
                    <p class="text-xl font-bold"
                      [class]="account.balance >= 0 ? 'text-green-600' : 'text-red-600'">
                      &#36;{{ account.balance | number:'1.2-2' }}
                    </p>
                    <span class="text-xs px-2 py-1 rounded-full"
                      [class]="account.status === 'ACTIVE' ? 'bg-green-100 text-green-700' : 'bg-red-100 text-red-700'">
                      {{ account.status }}
                    </span>
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
export class DashboardComponent implements OnInit {
  private accountService = inject(AccountService);
  private authService = inject(AuthService);

  accounts = signal<Account[]>([]);
  isLoading = signal(true);

  totalBalance = signal(0);
  activeAccountCount = signal(0);

  ngOnInit(): void {
    this.loadAccounts();
  }

  loadAccounts(): void {
    this.accountService.getMyAccounts().subscribe({
      next: (res) => {
        this.accounts.set(res.data || []);
        this.totalBalance.set(
          res.data?.reduce((sum, a) => sum + a.balance, 0) || 0
        );
        this.activeAccountCount.set(
          res.data?.filter(a => a.status === 'ACTIVE').length || 0
        );
        this.isLoading.set(false);
      },
      error: () => this.isLoading.set(false)
    });
  }

  getAccountIconClass(type: string): string {
    const classes: Record<string, string> = {
      'SAVINGS': 'bg-blue-500',
      'CURRENT': 'bg-purple-500',
      'FIXED_DEPOSIT': 'bg-green-600'
    };
    return classes[type] || 'bg-slate-500';
  }
}
