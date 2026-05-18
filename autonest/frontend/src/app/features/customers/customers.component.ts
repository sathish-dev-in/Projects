import { Component, OnInit, signal, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { CustomerService } from '../../core/services/customer.service';
import { Customer, CustomerRequest } from '../../core/models';

@Component({
  selector: 'app-customers',
  standalone: true,
  imports: [CommonModule, FormsModule, ReactiveFormsModule],
  templateUrl: './customers.component.html'
})
export class CustomersComponent implements OnInit {
  private readonly customerService = inject(CustomerService);
  private readonly fb = inject(FormBuilder);

  customers = signal<Customer[]>([]);
  loading = signal(true);
  showForm = signal(false);
  editingId = signal<number | null>(null);
  searchQuery = signal('');
  error = signal<string | null>(null);
  successMsg = signal<string | null>(null);

  form: FormGroup = this.fb.group({
    firstName: ['', [Validators.required, Validators.minLength(2)]],
    lastName: ['', [Validators.required, Validators.minLength(2)]],
    email: ['', [Validators.required, Validators.email]],
    phone: ['', [Validators.required, Validators.pattern(/^[+]?[0-9]{10,15}$/)]],
    address: [''],
    city: ['']
  });

  ngOnInit(): void {
    this.loadCustomers();
  }

  loadCustomers(): void {
    this.loading.set(true);
    this.customerService.getAll().subscribe({
      next: (res) => {
        this.customers.set(res.data ?? []);
        this.loading.set(false);
      },
      error: () => {
        this.error.set('Failed to load customers');
        this.loading.set(false);
      }
    });
  }

  openCreateForm(): void {
    this.form.reset();
    this.editingId.set(null);
    this.showForm.set(true);
  }

  openEditForm(customer: Customer): void {
    this.editingId.set(customer.id);
    this.form.patchValue({
      firstName: customer.firstName,
      lastName: customer.lastName,
      email: customer.email,
      phone: customer.phone,
      address: customer.address,
      city: customer.city
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

    const request: CustomerRequest = this.form.value;
    const id = this.editingId();

    if (id) {
      this.customerService.update(id, request).subscribe({
        next: () => {
          this.showSuccess('Customer updated successfully');
          this.closeForm();
          this.loadCustomers();
        },
        error: (err) => this.error.set(err.error?.message ?? 'Update failed')
      });
    } else {
      this.customerService.create(request).subscribe({
        next: () => {
          this.showSuccess('Customer created successfully');
          this.closeForm();
          this.loadCustomers();
        },
        error: (err) => this.error.set(err.error?.message ?? 'Create failed')
      });
    }
  }

  deleteCustomer(id: number, name: string): void {
    if (!confirm(`Delete customer "${name}"?`)) return;
    this.customerService.delete(id).subscribe({
      next: () => {
        this.showSuccess('Customer deleted');
        this.loadCustomers();
      },
      error: () => this.error.set('Delete failed')
    });
  }

  search(): void {
    const q = this.searchQuery();
    if (!q.trim()) {
      this.loadCustomers();
      return;
    }
    this.customerService.search(q).subscribe({
      next: (res) => this.customers.set(res.data ?? []),
      error: () => this.error.set('Search failed')
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
}
