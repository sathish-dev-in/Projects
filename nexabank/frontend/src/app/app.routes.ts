import { Routes } from '@angular/router';
import { authGuard } from './core/guards/auth.guard';

/**
 * Application routes with lazy loading for all feature modules.
 * Demonstrates Angular 17 standalone routing with lazy-loaded components.
 */
export const routes: Routes = [
  {
    path: '',
    redirectTo: '/dashboard',
    pathMatch: 'full'
  },
  {
    path: 'auth',
    children: [
      {
        path: 'login',
        loadComponent: () =>
          import('./features/auth/login/login.component').then(m => m.LoginComponent)
      },
      {
        path: 'register',
        loadComponent: () =>
          import('./features/auth/register/register.component').then(m => m.RegisterComponent)
      }
    ]
  },
  {
    path: 'dashboard',
    canActivate: [authGuard],
    loadComponent: () =>
      import('./features/dashboard/dashboard.component').then(m => m.DashboardComponent)
  },
  {
    path: 'accounts',
    canActivate: [authGuard],
    children: [
      {
        path: '',
        loadComponent: () =>
          import('./features/accounts/account-list/account-list.component').then(m => m.AccountListComponent)
      },
      {
        path: 'new',
        loadComponent: () =>
          import('./features/accounts/account-create/account-create.component').then(m => m.AccountCreateComponent)
      }
    ]
  },
  {
    path: 'transactions',
    canActivate: [authGuard],
    children: [
      {
        path: 'transfer',
        loadComponent: () =>
          import('./features/transactions/transfer/transfer.component').then(m => m.TransferComponent)
      },
      {
        path: 'history/:accountId',
        loadComponent: () =>
          import('./features/transactions/history/transaction-history.component').then(m => m.TransactionHistoryComponent)
      }
    ]
  },
  {
    path: 'notifications',
    canActivate: [authGuard],
    loadComponent: () =>
      import('./features/notifications/notification-list.component').then(m => m.NotificationListComponent)
  },
  {
    path: '**',
    redirectTo: '/dashboard'
  }
];
