// ── Generic Response Wrapper ──────────────────────────────────────────────────
export interface GenericResponse<T> {
  success: boolean;
  message: string;
  data: T;
  timestamp: string;
}

// ── Customer ──────────────────────────────────────────────────────────────────
export interface Customer {
  id: number;
  firstName: string;
  lastName: string;
  email: string;
  phone: string;
  address: string;
  city: string;
  active: boolean;
  createdAt: string;
  updatedAt: string;
}

export interface CustomerRequest {
  firstName: string;
  lastName: string;
  email: string;
  phone: string;
  address?: string;
  city?: string;
}

// ── Vehicle ───────────────────────────────────────────────────────────────────
export type VehicleType = 'SEDAN' | 'SUV' | 'HATCHBACK' | 'TRUCK' | 'VAN' | 'MOTORCYCLE' | 'ELECTRIC' | 'HYBRID';

export interface Vehicle {
  id: number;
  customer: Customer;
  make: string;
  model: string;
  year: number;
  licensePlate: string;
  vin: string;
  vehicleType: VehicleType;
  color: string;
  active: boolean;
  createdAt: string;
  updatedAt: string;
}

export interface VehicleRequest {
  customerId: number;
  make: string;
  model: string;
  year: number;
  licensePlate: string;
  vin?: string;
  vehicleType: VehicleType;
  color?: string;
}

// ── Service Order ─────────────────────────────────────────────────────────────
export type ServiceStatus = 'PENDING' | 'IN_PROGRESS' | 'COMPLETED' | 'CANCELLED';
export type ServiceType =
  | 'OIL_CHANGE'
  | 'TIRE_ROTATION'
  | 'BRAKE_SERVICE'
  | 'ENGINE_REPAIR'
  | 'TRANSMISSION_SERVICE'
  | 'AC_SERVICE'
  | 'ELECTRICAL_REPAIR'
  | 'BODY_WORK'
  | 'WHEEL_ALIGNMENT'
  | 'BATTERY_REPLACEMENT'
  | 'GENERAL_INSPECTION'
  | 'CUSTOM';

export interface Mechanic {
  id: number;
  firstName: string;
  lastName: string;
  email: string;
  phone: string;
  specialization: string;
  employeeId: string;
}

export interface ServiceOrder {
  id: number;
  vehicle: Vehicle;
  mechanic: Mechanic | null;
  serviceType: ServiceType;
  description: string;
  status: ServiceStatus;
  estimatedCost: number;
  actualCost: number | null;
  notes: string;
  startedAt: string | null;
  completedAt: string | null;
  createdAt: string;
  updatedAt: string;
}

export interface ServiceOrderRequest {
  vehicleId: number;
  mechanicId?: number;
  serviceType: ServiceType;
  description: string;
  estimatedCost: number;
  notes?: string;
}

// ── Part ──────────────────────────────────────────────────────────────────────
export interface Part {
  id: number;
  name: string;
  partNumber: string;
  description: string;
  unitPrice: number;
  stockQuantity: number;
  minimumStockLevel: number;
  supplier: string;
  category: string;
  active: boolean;
  lowStock: boolean;
  outOfStock: boolean;
  createdAt: string;
}

export interface PartRequest {
  name: string;
  partNumber: string;
  description?: string;
  unitPrice: number;
  stockQuantity: number;
  minimumStockLevel: number;
  supplier?: string;
  category?: string;
}

// ── Dashboard ─────────────────────────────────────────────────────────────────
export interface DashboardStats {
  totalCustomers: number;
  totalVehicles: number;
  activeServices: number;
  pendingServices: number;
  completedServices: number;
  cancelledServices: number;
  totalParts: number;
  lowStockParts: number;
  totalRevenue: number;
  totalMechanics: number;
}
