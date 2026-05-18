import { Component, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';

/**
 * Registration component with reactive forms and Angular 17 signals.
 */
@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  template: `
    <div class="min-h-screen bg-gradient-to-br from-slate-900 via-blue-900 to-slate-900 flex items-center justify-center p-4">
      <div class="w-full max-w-md">
        <div class="text-center mb-8">
          <div class="inline-flex items-center justify-center w-16 h-16 bg-blue-600 rounded-2xl mb-4">
            <span class="text-white text-2xl font-bold">NB</span>
          </div>
          <h1 class="text-3xl font-bold text-white">NexaBank</h1>
          <p class="text-slate-400 mt-1">Create your account</p>
        </div>

        <div class="bg-white rounded-2xl shadow-2xl p-8">
          <h2 class="text-2xl font-semibold text-slate-800 mb-6">Open an Account</h2>

          @if (errorMessage()) {
            <div class="mb-4 p-3 bg-red-50 border border-red-200 rounded-lg">
              <p class="text-red-600 text-sm">{{ errorMessage() }}</p>
            </div>
          }

          <form [formGroup]="registerForm" (ngSubmit)="onSubmit()">
            <div class="mb-4">
              <label class="block text-sm font-medium text-slate-700 mb-1">Full Name</label>
              <input type="text" formControlName="fullName" placeholder="John Doe"
                class="w-full px-4 py-3 border border-slate-300 rounded-lg focus:ring-2 focus:ring-blue-500 outline-none transition" />
            </div>

            <div class="mb-4">
              <label class="block text-sm font-medium text-slate-700 mb-1">Email</label>
              <input type="email" formControlName="email" placeholder="you@example.com"
                class="w-full px-4 py-3 border border-slate-300 rounded-lg focus:ring-2 focus:ring-blue-500 outline-none transition" />
            </div>

            <div class="mb-4">
              <label class="block text-sm font-medium text-slate-700 mb-1">Phone (optional)</label>
              <input type="tel" formControlName="phone" placeholder="+1-555-0100"
                class="w-full px-4 py-3 border border-slate-300 rounded-lg focus:ring-2 focus:ring-blue-500 outline-none transition" />
            </div>

            <div class="mb-6">
              <label class="block text-sm font-medium text-slate-700 mb-1">Password</label>
              <input type="password" formControlName="password" placeholder="Min 6 characters"
                class="w-full px-4 py-3 border border-slate-300 rounded-lg focus:ring-2 focus:ring-blue-500 outline-none transition" />
            </div>

            <button type="submit" [disabled]="isLoading() || registerForm.invalid"
              class="w-full py-3 px-4 bg-blue-600 hover:bg-blue-700 disabled:bg-blue-300 text-white font-semibold rounded-lg transition">
              {{ isLoading() ? 'Creating account...' : 'Create Account' }}
            </button>
          </form>

          <div class="mt-6 text-center">
            <p class="text-slate-500 text-sm">
              Already have an account?
              <a routerLink="/auth/login" class="text-blue-600 hover:underline font-medium ml-1">Sign in</a>
            </p>
          </div>
        </div>
      </div>
    </div>
  `
})
export class RegisterComponent {
  private authService = inject(AuthService);
  private router = inject(Router);
  private fb = inject(FormBuilder);

  registerForm = this.fb.group({
    fullName: ['', Validators.required],
    email: ['', [Validators.required, Validators.email]],
    phone: [''],
    password: ['', [Validators.required, Validators.minLength(6)]]
  });

  isLoading = signal(false);
  errorMessage = signal('');

  onSubmit(): void {
    if (this.registerForm.invalid) return;
    this.isLoading.set(true);
    this.errorMessage.set('');

    this.authService.register(this.registerForm.value as any).subscribe({
      next: () => {
        this.isLoading.set(false);
        this.router.navigate(['/dashboard']);
      },
      error: (err) => {
        this.isLoading.set(false);
        this.errorMessage.set(err.error?.message || 'Registration failed.');
      }
    });
  }
}
