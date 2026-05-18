import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink, RouterLinkActive } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';

/**
 * Navbar component with authentication state.
 * Angular 17 standalone with inject() pattern.
 */
@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [CommonModule, RouterLink, RouterLinkActive],
  template: `
    <nav class="bg-slate-800 shadow-lg">
      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div class="flex items-center justify-between h-16">
          <!-- Brand -->
          <div class="flex items-center gap-3">
            <div class="w-8 h-8 bg-blue-600 rounded-lg flex items-center justify-center">
              <span class="text-white text-sm font-bold">NB</span>
            </div>
            <span class="text-white font-bold text-lg">NexaBank</span>
          </div>

          <!-- Nav links -->
          <div class="hidden md:flex items-center gap-1">
            <a routerLink="/dashboard" routerLinkActive="bg-slate-700"
              class="px-3 py-2 rounded-lg text-slate-300 hover:text-white hover:bg-slate-700 text-sm font-medium transition">
              Dashboard
            </a>
            <a routerLink="/accounts" routerLinkActive="bg-slate-700"
              class="px-3 py-2 rounded-lg text-slate-300 hover:text-white hover:bg-slate-700 text-sm font-medium transition">
              Accounts
            </a>
            <a routerLink="/transactions/transfer" routerLinkActive="bg-slate-700"
              class="px-3 py-2 rounded-lg text-slate-300 hover:text-white hover:bg-slate-700 text-sm font-medium transition">
              Transfer
            </a>
            <a routerLink="/notifications" routerLinkActive="bg-slate-700"
              class="px-3 py-2 rounded-lg text-slate-300 hover:text-white hover:bg-slate-700 text-sm font-medium transition">
              Notifications
            </a>
          </div>

          <!-- User menu -->
          <div class="flex items-center gap-3">
            <span class="text-slate-400 text-sm hidden md:block">{{ authService.getEmail() }}</span>
            <button (click)="logout()"
              class="px-3 py-2 text-slate-300 hover:text-white hover:bg-slate-700 rounded-lg text-sm transition">
              Logout
            </button>
          </div>
        </div>
      </div>
    </nav>
  `
})
export class NavbarComponent {
  authService = inject(AuthService);

  logout(): void {
    this.authService.logout();
  }
}
