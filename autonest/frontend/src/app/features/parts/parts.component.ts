import { Component, OnInit, signal, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { PartService } from '../../core/services/part.service';
import { Part, PartRequest } from '../../core/models';

@Component({
  selector: 'app-parts',
  standalone: true,
  imports: [CommonModule, FormsModule, ReactiveFormsModule],
  templateUrl: './parts.component.html'
})
export class PartsComponent implements OnInit {
  private readonly partService = inject(PartService);
  private readonly fb = inject(FormBuilder);

  parts = signal<Part[]>([]);
  loading = signal(true);
  showForm = signal(false);
  showStockModal = signal(false);
  editingId = signal<number | null>(null);
  stockPartId = signal<number | null>(null);
  stockPartName = signal('');
  error = signal<string | null>(null);
  successMsg = signal<string | null>(null);
  showLowStockOnly = signal(false);

  stockAdjustQty = 0;
  stockOperation = 'ADD';

  form: FormGroup = this.fb.group({
    name: ['', Validators.required],
    partNumber: ['', Validators.required],
    description: [''],
    unitPrice: [null, [Validators.required, Validators.min(0.01)]],
    stockQuantity: [0, [Validators.required, Validators.min(0)]],
    minimumStockLevel: [5, [Validators.required, Validators.min(0)]],
    supplier: [''],
    category: ['']
  });

  ngOnInit(): void {
    this.loadParts();
  }

  loadParts(): void {
    this.loading.set(true);
    const obs = this.showLowStockOnly()
      ? this.partService.getLowStock()
      : this.partService.getAll();

    obs.subscribe({
      next: (res) => {
        this.parts.set(res.data ?? []);
        this.loading.set(false);
      },
      error: () => {
        this.error.set('Failed to load parts');
        this.loading.set(false);
      }
    });
  }

  toggleLowStock(): void {
    this.showLowStockOnly.update(v => !v);
    this.loadParts();
  }

  openCreateForm(): void {
    this.form.reset({ stockQuantity: 0, minimumStockLevel: 5 });
    this.editingId.set(null);
    this.showForm.set(true);
  }

  openEditForm(part: Part): void {
    this.editingId.set(part.id);
    this.form.patchValue({
      name: part.name,
      partNumber: part.partNumber,
      description: part.description,
      unitPrice: part.unitPrice,
      stockQuantity: part.stockQuantity,
      minimumStockLevel: part.minimumStockLevel,
      supplier: part.supplier,
      category: part.category
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

    const request: PartRequest = this.form.value;
    const id = this.editingId();

    if (id) {
      this.partService.update(id, request).subscribe({
        next: () => {
          this.showSuccess('Part updated');
          this.closeForm();
          this.loadParts();
        },
        error: (err) => this.error.set(err.error?.message ?? 'Update failed')
      });
    } else {
      this.partService.create(request).subscribe({
        next: () => {
          this.showSuccess('Part added to inventory');
          this.closeForm();
          this.loadParts();
        },
        error: (err) => this.error.set(err.error?.message ?? 'Create failed')
      });
    }
  }

  openStockModal(part: Part): void {
    this.stockPartId.set(part.id);
    this.stockPartName.set(part.name);
    this.stockAdjustQty = 0;
    this.stockOperation = 'ADD';
    this.showStockModal.set(true);
  }

  closeStockModal(): void {
    this.showStockModal.set(false);
    this.stockPartId.set(null);
  }

  adjustStock(): void {
    const id = this.stockPartId();
    if (!id || this.stockAdjustQty <= 0) return;

    this.partService.adjustStock(id, this.stockAdjustQty, this.stockOperation).subscribe({
      next: () => {
        this.showSuccess('Stock adjusted successfully');
        this.closeStockModal();
        this.loadParts();
      },
      error: (err) => this.error.set(err.error?.message ?? 'Stock adjustment failed')
    });
  }

  deletePart(id: number, name: string): void {
    if (!confirm(`Delete part "${name}"?`)) return;
    this.partService.delete(id).subscribe({
      next: () => {
        this.showSuccess('Part deleted');
        this.loadParts();
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
}
