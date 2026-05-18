import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink, RouterLinkActive } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';

/**
 * Sidebar navigation component.
 * Angular 17 standalone component.
 */
@Component({
  selector: 'app-sidebar',
  standalone: true,
  imports: [CommonModule, RouterLink, RouterLinkActive],
  template: `
    <aside class="w-64 min-h-screen bg-slate-800 flex flex-col">
      <!-- Brand -->
      <div class="p-6 border-b border-slate-700">
        <div class="flex items-center gap-3">
          <div class="w-10 h-10 bg-blue-600 rounded-xl flex items-center justify-center">
            <span class="text-white font-bold">NB</span>
          </div>
          <div>
            <p class="text-white font-bold">NexaBank</p>
            <p class="text-slate-400 text-xs">Digital Banking</p>
          </div>
        </div>
      </div>

      <!-- Navigation -->
      <nav class="flex-1 p-4 space-y-1">
        <a routerLink="/dashboard" routerLinkActive="bg-blue-600 text-white"
          class="flex items-center gap-3 px-4 py-3 rounded-lg text-slate-300 hover:bg-slate-700 hover:text-white transition text-sm font-medium">
          <span>Dashboard</span>
        </a>
        <a routerLink="/accounts" routerLinkActive="bg-blue-600 text-white"
          class="flex items-center gap-3 px-4 py-3 rounded-lg text-slate-300 hover:bg-slate-700 hover:text-white transition text-sm font-medium">
          <span>Accounts</span>
        </a>
        <a routerLink="/transactions/transfer" routerLinkActive="bg-blue-600 text-white"
          class="flex items-center gap-3 px-4 py-3 rounded-lg text-slate-300 hover:bg-slate-700 hover:text-white transition text-sm font-medium">
          <span>Transfer</span>
        </a>
        <a routerLink="/notifications" routerLinkActive="bg-blue-600 text-white"
          class="flex items-center gap-3 px-4 py-3 rounded-lg text-slate-300 hover:bg-slate-700 hover:text-white transition text-sm font-medium">
          <span>Notifications</span>
        </a>
      </nav>

      <!-- User info at bottom -->
      <div class="p-4 border-t border-slate-700">
        <div class="flex items-center gap-3 mb-3">
          <div class="w-9 h-9 bg-blue-600 rounded-full flex items-center justify-center">
            <span class="text-white text-sm font-bold">
              {{ (authService.getEmail() || 'U')[0].toUpperCase() }}
            </span>
          </div>
          <div class="flex-1 min-w-0">
            <p class="text-slate-300 text-xs font-medium truncate">{{ authService.getEmail() }}</p>
            <p class="text-slate-500 text-xs">{{ authService.getRole() }}</p>
          </div>
        </div>
        <button (click)="authService.logout()"
          class="w-full py-2 text-slate-400 hover:text-white text-sm transition text-left px-1">
          Sign out
        </button>
      </div>
    </aside>
  `
})
export class SidebarComponent {
  authService = inject(AuthService);
}
