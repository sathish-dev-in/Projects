import { Component, OnInit, signal, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ServiceOrderService } from '../../core/services/service-order.service';
import { VehicleService } from '../../core/services/vehicle.service';
import { ServiceOrder, ServiceOrderRequest, ServiceStatus, ServiceType, Vehicle } from '../../core/models';

@Component({
  selector: 'app-service-orders',
  standalone: true,
  imports: [CommonModule, FormsModule, ReactiveFormsModule],
  templateUrl: './service-orders.component.html'
})
export class ServiceOrdersComponent implements OnInit {
  private readonly serviceOrderService = inject(ServiceOrderService);
  private readonly vehicleService = inject(VehicleService);
  private readonly fb = inject(FormBuilder);

  orders = signal<ServiceOrder[]>([]);
  vehicles = signal<Vehicle[]>([]);
  loading = signal(true);
  showForm = signal(false);
  editingId = signal<number | null>(null);
  error = signal<string | null>(null);
  successMsg = signal<string | null>(null);
  filterStatus = signal<ServiceStatus | 'ALL'>('ALL');

  serviceTypes: ServiceType[] = [
    'OIL_CHANGE', 'TIRE_ROTATION', 'BRAKE_SERVICE', 'ENGINE_REPAIR',
    'TRANSMISSION_SERVICE', 'AC_SERVICE', 'ELECTRICAL_REPAIR', 'BODY_WORK',
    'WHEEL_ALIGNMENT', 'BATTERY_REPLACEMENT', 'GENERAL_INSPECTION', 'CUSTOM'
  ];

  statuses: ServiceStatus[] = ['PENDING', 'IN_PROGRESS', 'COMPLETED', 'CANCELLED'];

  form: FormGroup = this.fb.group({
    vehicleId: [null, Validators.required],
    mechanicId: [null],
    serviceType: ['OIL_CHANGE', Validators.required],
    description: ['', [Validators.required, Validators.minLength(10)]],
    estimatedCost: [null, [Validators.required, Validators.min(0)]],
    notes: ['']
  });

  ngOnInit(): void {
    this.loadOrders();
    this.loadVehicles();
  }

  loadOrders(): void {
    this.loading.set(true);
    this.serviceOrderService.getAll().subscribe({
      next: (res) => {
        this.orders.set(res.data ?? []);
        this.loading.set(false);
      },
      error: () => {
        this.error.set('Failed to load service orders');
        this.loading.set(false);
      }
    });
  }

  loadVehicles(): void {
    this.vehicleService.getAll().subscribe({
      next: (res) => this.vehicles.set(res.data ?? []),
      error: () => {}
    });
  }

  get filteredOrders(): ServiceOrder[] {
    const status = this.filterStatus();
    if (status === 'ALL') return this.orders();
    return this.orders().filter(o => o.status === status);
  }

  openCreateForm(): void {
    this.form.reset({ serviceType: 'OIL_CHANGE' });
    this.editingId.set(null);
    this.showForm.set(true);
  }

  closeForm(): void {
    this.showForm.set(false);
    this.form.reset();
    this.editingId.set(null);
  }

  submitForm(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    const request: ServiceOrderRequest = this.form.value;
    this.serviceOrderService.create(request).subscribe({
      next: () => {
        this.showSuccess('Service order created');
        this.closeForm();
        this.loadOrders();
      },
      error: (err) => this.error.set(err.error?.message ?? 'Failed to create order')
    });
  }

  updateStatus(order: ServiceOrder, newStatus: ServiceStatus): void {
    if (!order.status) return;
    this.serviceOrderService.updateStatus(order.id, newStatus).subscribe({
      next: () => {
        this.showSuccess(`Status updated to ${newStatus}`);
        this.loadOrders();
      },
      error: (err) => this.error.set(err.error?.message ?? 'Status update failed')
    });
  }

  getStatusClass(status: ServiceStatus): string {
    const map: Record<ServiceStatus, string> = {
      PENDING: 'badge-pending',
      IN_PROGRESS: 'badge-in-progress',
      COMPLETED: 'badge-completed',
      CANCELLED: 'badge-cancelled'
    };
    return map[status];
  }

  getNextStatuses(status: ServiceStatus): ServiceStatus[] {
    const transitions: Record<ServiceStatus, ServiceStatus[]> = {
      PENDING: ['IN_PROGRESS', 'CANCELLED'],
      IN_PROGRESS: ['COMPLETED', 'CANCELLED'],
      COMPLETED: [],
      CANCELLED: []
    };
    return transitions[status];
  }

  formatServiceType(type: string): string {
    return type.replace(/_/g, ' ');
  }

  private showSuccess(msg: string): void {
    this.successMsg.set(msg);
    this.error.set(null);
    setTimeout(() => this.successMsg.set(null), 3000);
  }

  isInvalid(field: string): boolean {
    const ctrl = this.form.get(field);
    return !!(ctrl?.invalid && ctrl?.touched);
  }
}
