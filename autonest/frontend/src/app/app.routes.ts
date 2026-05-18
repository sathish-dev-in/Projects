import { Routes } from '@angular/router';

export const routes: Routes = [
  {
    path: '',
    redirectTo: 'dashboard',
    pathMatch: 'full'
  },
  {
    path: 'dashboard',
    loadComponent: () =>
      import('./features/dashboard/dashboard.component').then(m => m.DashboardComponent)
  },
  {
    path: 'customers',
    loadComponent: () =>
      import('./features/customers/customers.component').then(m => m.CustomersComponent)
  },
  {
    path: 'vehicles',
    loadComponent: () =>
      import('./features/vehicles/vehicles.component').then(m => m.VehiclesComponent)
  },
  {
    path: 'service-orders',
    loadComponent: () =>
      import('./features/service-orders/service-orders.component').then(m => m.ServiceOrdersComponent)
  },
  {
    path: 'parts',
    loadComponent: () =>
      import('./features/parts/parts.component').then(m => m.PartsComponent)
  },
  {
    path: '**',
    redirectTo: 'dashboard'
  }
];
