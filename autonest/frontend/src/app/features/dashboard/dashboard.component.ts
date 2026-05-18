import { Component, OnInit, signal, inject } from '@angular/core';
import { CommonModule, CurrencyPipe } from '@angular/common';
import { RouterLink } from '@angular/router';
import { DashboardService } from '../../core/services/dashboard.service';
import { ServiceOrderService } from '../../core/services/service-order.service';
import { DashboardStats, ServiceOrder } from '../../core/models';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, RouterLink, CurrencyPipe],
  templateUrl: './dashboard.component.html'
})
export class DashboardComponent implements OnInit {
  private readonly dashboardService = inject(DashboardService);
  private readonly serviceOrderService = inject(ServiceOrderService);

  stats = signal<DashboardStats | null>(null);
  recentOrders = signal<ServiceOrder[]>([]);
  loading = signal(true);
  error = signal<string | null>(null);

  ngOnInit(): void {
    this.loadDashboard();
  }

  private loadDashboard(): void {
    this.loading.set(true);
    this.dashboardService.getStats().subscribe({
      next: (res) => {
        this.stats.set(res.data);
        this.loading.set(false);
      },
      error: (err) => {
        this.error.set('Failed to load dashboard stats');
        this.loading.set(false);
        console.error(err);
      }
    });

    this.serviceOrderService.getActive().subscribe({
      next: (res) => this.recentOrders.set(res.data?.slice(0, 5) ?? []),
      error: (err) => console.error(err)
    });
  }

  getStatusClass(status: string): string {
    switch (status) {
      case 'PENDING': return 'badge-pending';
      case 'IN_PROGRESS': return 'badge-in-progress';
      case 'COMPLETED': return 'badge-completed';
      case 'CANCELLED': return 'badge-cancelled';
      default: return 'badge-pending';
    }
  }

  getStatusLabel(status: string): string {
    return status.replace('_', ' ');
  }

  refresh(): void {
    this.loadDashboard();
  }
}
