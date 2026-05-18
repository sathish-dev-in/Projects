import { Component, OnInit, signal, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { VehicleService } from '../../core/services/vehicle.service';
import { CustomerService } from '../../core/services/customer.service';
import { Vehicle, VehicleRequest, Customer, VehicleType } from '../../core/models';

@Component({
  selector: 'app-vehicles',
  standalone: true,
  imports: [CommonModule, FormsModule, ReactiveFormsModule],
  templateUrl: './vehicles.component.html'
})
export class VehiclesComponent implements OnInit {
  private readonly vehicleService = inject(VehicleService);
  private readonly customerService = inject(CustomerService);
  private readonly fb = inject(FormBuilder);

  vehicles = signal<Vehicle[]>([]);
  customers = signal<Customer[]>([]);
  loading = signal(true);
  showForm = signal(false);
  editingId = signal<number | null>(null);
  error = signal<string | null>(null);
  successMsg = signal<string | null>(null);

  vehicleTypes: VehicleType[] = ['SEDAN', 'SUV', 'HATCHBACK', 'TRUCK', 'VAN', 'MOTORCYCLE', 'ELECTRIC', 'HYBRID'];

  form: FormGroup = this.fb.group({
    customerId: [null, Validators.required],
    make: ['', Validators.required],
    model: ['', Validators.required],
    year: [null, [Validators.required, Validators.min(1900), Validators.max(2030)]],
    licensePlate: ['', Validators.required],
    vin: [''],
    vehicleType: ['SEDAN', Validators.required],
    color: ['']
  });

  ngOnInit(): void {
    this.loadVehicles();
    this.loadCustomers();
  }

  loadVehicles(): void {
    this.loading.set(true);
    this.vehicleService.getAll().subscribe({
      next: (res) => {
        this.vehicles.set(res.data ?? []);
        this.loading.set(false);
      },
      error: () => {
        this.error.set('Failed to load vehicles');
        this.loading.set(false);
      }
    });
  }

  loadCustomers(): void {
    this.customerService.getAll().subscribe({
      next: (res) => this.customers.set(res.data ?? []),
      error: () => {}
    });
  }

  openCreateForm(): void {
    this.form.reset({ vehicleType: 'SEDAN' });
    this.editingId.set(null);
    this.showForm.set(true);
  }

  openEditForm(vehicle: Vehicle): void {
    this.editingId.set(vehicle.id);
    this.form.patchValue({
      customerId: vehicle.customer?.id,
      make: vehicle.make,
      model: vehicle.model,
      year: vehicle.year,
      licensePlate: vehicle.licensePlate,
      vin: vehicle.vin,
      vehicleType: vehicle.vehicleType,
      color: vehicle.color
    });
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

    const request: VehicleRequest = this.form.value;
    const id = this.editingId();

    if (id) {
      this.vehicleService.update(id, request).subscribe({
        next: () => {
          this.showSuccess('Vehicle updated successfully');
          this.closeForm();
          this.loadVehicles();
        },
        error: (err) => this.error.set(err.error?.message ?? 'Update failed')
      });
    } else {
      this.vehicleService.create(request).subscribe({
        next: () => {
          this.showSuccess('Vehicle registered successfully');
          this.closeForm();
          this.loadVehicles();
        },
        error: (err) => this.error.set(err.error?.message ?? 'Create failed')
      });
    }
  }

  deleteVehicle(id: number, name: string): void {
    if (!confirm(`Delete vehicle "${name}"?`)) return;
    this.vehicleService.delete(id).subscribe({
      next: () => {
        this.showSuccess('Vehicle deleted');
        this.loadVehicles();
      },
      error: () => this.error.set('Delete failed')
    });
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

  getVehicleTypeIcon(type: VehicleType): string {
    const icons: Record<VehicleType, string> = {
      SEDAN: '🚗', SUV: '🚙', HATCHBACK: '🚘', TRUCK: '🚛',
      VAN: '🚐', MOTORCYCLE: '🏍', ELECTRIC: '⚡', HYBRID: '🔋'
    };
    return icons[type] ?? '🚗';
  }
}
