import { Component, inject, signal, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { RouterLink } from '@angular/router';
import { NavbarComponent } from '../../shared/components/navbar/navbar.component';
import { Notification } from '../../shared/models/notification.model';
import { environment } from '../../../environments/environment';

/**
 * Notification list component.
 * Angular 17 standalone with signals.
 */
@Component({
  selector: 'app-notification-list',
  standalone: true,
  imports: [CommonModule, RouterLink, NavbarComponent],
  template: `
    <div class="min-h-screen bg-gray-50">
      <app-navbar></app-navbar>

      <main class="max-w-3xl mx-auto px-4 py-8">
        <div class="flex items-center justify-between mb-6">
          <h1 class="text-2xl font-bold text-slate-800">Notifications</h1>
          <span class="px-3 py-1 bg-blue-100 text-blue-700 rounded-full text-sm font-medium">
            {{ unreadCount() }} unread
          </span>
        </div>

        <div class="bg-white rounded-xl shadow-sm border border-slate-100">
          @if (isLoading()) {
            <div class="p-8 text-center text-slate-500">Loading notifications...</div>
          } @else if (notifications().length === 0) {
            <div class="p-8 text-center text-slate-500">No notifications yet.</div>
          } @else {
            <div class="divide-y divide-slate-100">
              @for (notification of notifications(); track notification.id) {
                <div class="p-5 flex items-start gap-4 hover:bg-slate-50 transition"
                  [class.bg-blue-50]="!notification.read">
                  <div class="w-10 h-10 rounded-full bg-blue-100 flex items-center justify-center flex-shrink-0">
                    <span class="text-blue-600 text-sm font-bold">{{ notification.type[0] }}</span>
                  </div>
                  <div class="flex-1">
                    <div class="flex items-center justify-between">
                      <p class="font-medium text-slate-800">{{ notification.title }}</p>
                      @if (!notification.read) {
                        <span class="w-2 h-2 bg-blue-500 rounded-full"></span>
                      }
                    </div>
                    <p class="text-sm text-slate-600 mt-1">{{ notification.message }}</p>
                    <p class="text-xs text-slate-400 mt-1">
                      {{ notification.createdAt | date:'medium' }}
                    </p>
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
export class NotificationListComponent implements OnInit {
  private http = inject(HttpClient);

  notifications = signal<Notification[]>([]);
  isLoading = signal(true);
  unreadCount = signal(0);

  ngOnInit(): void {
    this.http.get<Notification[]>(`${environment.apiUrl}/api/notifications`).subscribe({
      next: data => {
        this.notifications.set(data || []);
        this.unreadCount.set(data?.filter(n => !n.read).length || 0);
        this.isLoading.set(false);
      },
      error: () => this.isLoading.set(false)
    });
  }
}
