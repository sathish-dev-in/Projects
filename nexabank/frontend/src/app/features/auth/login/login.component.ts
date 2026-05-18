import { Component, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';

/**
 * Login component with reactive forms and signals.
 * Angular 17 standalone component with inject() and signal() usage.
 */
@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  template: `
    <div class="min-h-screen bg-gradient-to-br from-slate-900 via-blue-900 to-slate-900 flex items-center justify-center p-4">
      <div class="w-full max-w-md">
        <!-- Logo -->
        <div class="text-center mb-8">
          <div class="inline-flex items-center justify-center w-16 h-16 bg-blue-600 rounded-2xl mb-4">
            <span class="text-white text-2xl font-bold">NB</span>
          </div>
          <h1 class="text-3xl font-bold text-white">NexaBank</h1>
          <p class="text-slate-400 mt-1">Digital Banking Platform</p>
        </div>

        <!-- Card -->
        <div class="bg-white rounded-2xl shadow-2xl p-8">
          <h2 class="text-2xl font-semibold text-slate-800 mb-2">Welcome back</h2>
          <p class="text-slate-500 text-sm mb-6">Sign in to your account</p>

          <!-- Error -->
          @if (errorMessage()) {
            <div class="mb-4 p-3 bg-red-50 border border-red-200 rounded-lg">
              <p class="text-red-600 text-sm">{{ errorMessage() }}</p>
            </div>
          }

          <form [formGroup]="loginForm" (ngSubmit)="onSubmit()">
            <div class="mb-4">
              <label class="block text-sm font-medium text-slate-700 mb-1">Email</label>
              <input
                type="email"
                formControlName="email"
                placeholder="you@nexabank.com"
                class="w-full px-4 py-3 border border-slate-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500 outline-none transition"
              />
              @if (loginForm.get('email')?.invalid && loginForm.get('email')?.touched) {
                <p class="text-red-500 text-xs mt-1">Valid email is required</p>
              }
            </div>

            <div class="mb-6">
              <label class="block text-sm font-medium text-slate-700 mb-1">Password</label>
              <input
                type="password"
                formControlName="password"
                placeholder="••••••••"
                class="w-full px-4 py-3 border border-slate-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500 outline-none transition"
              />
              @if (loginForm.get('password')?.invalid && loginForm.get('password')?.touched) {
                <p class="text-red-500 text-xs mt-1">Password is required</p>
              }
            </div>

            <button
              type="submit"
              [disabled]="isLoading() || loginForm.invalid"
              class="w-full py-3 px-4 bg-blue-600 hover:bg-blue-700 disabled:bg-blue-300 text-white font-semibold rounded-lg transition duration-200"
            >
              {{ isLoading() ? 'Signing in...' : 'Sign In' }}
            </button>
          </form>

          <div class="mt-6 text-center">
            <p class="text-slate-500 text-sm">
              Don't have an account?
              <a routerLink="/auth/register" class="text-blue-600 hover:underline font-medium ml-1">Create one</a>
            </p>
          </div>

          <!-- Demo credentials -->
          <div class="mt-4 p-3 bg-slate-50 rounded-lg">
            <p class="text-xs text-slate-500 font-medium mb-1">Demo credentials:</p>
            <p class="text-xs text-slate-600">Admin: admin&#64;nexabank.com / admin123</p>
            <p class="text-xs text-slate-600">User: john.doe&#64;nexabank.com / user123</p>
          </div>
        </div>
      </div>
    </div>
  `
})
export class LoginComponent {
  private authService = inject(AuthService);
  private router = inject(Router);
  private fb = inject(FormBuilder);

  loginForm = this.fb.group({
    email: ['', [Validators.required, Validators.email]],
    password: ['', Validators.required]
  });

  isLoading = signal(false);
  errorMessage = signal('');

  onSubmit(): void {
    if (this.loginForm.invalid) return;

    this.isLoading.set(true);
    this.errorMessage.set('');

    const { email, password } = this.loginForm.value;
    this.authService.login(email!, password!).subscribe({
      next: () => {
        this.isLoading.set(false);
        this.router.navigate(['/dashboard']);
      },
      error: (err) => {
        this.isLoading.set(false);
        this.errorMessage.set(err.error?.message || 'Login failed. Please try again.');
      }
    });
  }
}
