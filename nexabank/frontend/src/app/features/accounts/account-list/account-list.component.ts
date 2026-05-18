import { Component, inject, signal, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { AccountService } from '../../../core/services/account.service';
import { Account } from '../../../shared/models/account.model';
import { NavbarComponent } from '../../../shared/components/navbar/navbar.component';

/**
 * Account list component.
 * Angular 17 standalone with signals for state.
 */
@Component({
  selector: 'app-account-list',
  standalone: true,
  imports: [CommonModule, RouterLink, NavbarComponent],
  template: `
    <div class="min-h-screen bg-gray-50">
      <app-navbar></app-navbar>

      <main class="max-w-7xl mx-auto px-4 py-8">
        <div class="flex items-center justify-between mb-6">
          <h1 class="text-2xl font-bold text-slate-800">My Accounts</h1>
          <a routerLink="/accounts/new"
            class="px-4 py-2 bg-blue-600 text-white text-sm font-medium rounded-lg hover:bg-blue-700 transition">
            + Open Account
          </a>
        </div>

        @if (isLoading()) {
          <div class="text-center py-12 text-slate-500">Loading accounts...</div>
        } @else {
          <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
            @for (account of accounts(); track account.id) {
              <div class="bg-white rounded-xl shadow-sm border border-slate-100 p-6 hover:shadow-md transition">
                <div class="flex items-center justify-between mb-4">
                  <div class="w-12 h-12 rounded-xl flex items-center justify-center"
                    [class]="getIconClass(account.accountType)">
                    <span class="text-white font-bold">{{ account.accountType[0] }}</span>
                  </div>
                  <span class="text-xs px-2 py-1 rounded-full font-medium"
                    [class]="account.status === 'ACTIVE' ? 'bg-green-100 text-green-700' : 'bg-red-100 text-red-700'">
                    {{ account.status }}
                  </span>
                </div>

                <h3 class="font-semibold text-slate-800">{{ account.accountTypeDescription }}</h3>
                <p class="text-slate-500 text-sm font-mono mt-1">{{ account.accountNumber }}</p>

                <div class="mt-4 pt-4 border-t border-slate-100">
                  <p class="text-sm text-slate-500">Available Balance</p>
                  <p class="text-2xl font-bold text-green-600 mt-1">
                    &#36;{{ account.balance | number:'1.2-2' }}
                  </p>
                </div>

                <div class="mt-3 flex items-center justify-between text-sm text-slate-500">
                  <span>Interest: {{ account.interestRate }}% p.a.</span>
                </div>

                <div class="mt-4 flex gap-2">
                  <a [routerLink]="['/transactions/history', account.id]"
                    class="flex-1 text-center py-2 text-blue-600 border border-blue-600 rounded-lg text-sm hover:bg-blue-50 transition">
                    History
                  </a>
                  <a routerLink="/transactions/transfer"
                    class="flex-1 text-center py-2 bg-blue-600 text-white rounded-lg text-sm hover:bg-blue-700 transition">
                    Transfer
                  </a>
                </div>
              </div>
            }
          </div>
        }
      </main>
    </div>
  `
})
export class AccountListComponent implements OnInit {
  private accountService = inject(AccountService);
  accounts = signal<Account[]>([]);
  isLoading = signal(true);

  ngOnInit(): void {
    this.accountService.getMyAccounts().subscribe({
      next: res => {
        this.accounts.set(res.data || []);
        this.isLoading.set(false);
      },
      error: () => this.isLoading.set(false)
    });
  }

  getIconClass(type: string): string {
    const m: Record<string, string> = {
      SAVINGS: 'bg-blue-500',
      CURRENT: 'bg-purple-500',
      FIXED_DEPOSIT: 'bg-green-600'
    };
    return m[type] || 'bg-slate-500';
  }
}
