import { Component, Input } from '@angular/core';
import { RouterLink, RouterLinkActive } from '@angular/router';
import { NgClass } from '@angular/common';

interface NavItem {
  label: string;
  route: string;
  icon: string;
}

@Component({
  selector: 'app-sidebar',
  standalone: true,
  imports: [RouterLink, RouterLinkActive, NgClass],
  templateUrl: './sidebar.component.html'
})
export class SidebarComponent {
  @Input() isOpen = true;

  navItems: NavItem[] = [
    { label: 'Dashboard', route: '/dashboard', icon: '🏠' },
    { label: 'Customers', route: '/customers', icon: '👥' },
    { label: 'Vehicles', route: '/vehicles', icon: '🚗' },
    { label: 'Service Orders', route: '/service-orders', icon: '🔧' },
    { label: 'Parts & Inventory', route: '/parts', icon: '🔩' }
  ];
}
