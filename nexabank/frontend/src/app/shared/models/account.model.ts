export interface Account {
  id: number;
  accountNumber: string;
  userId: number;
  balance: number;
  accountType: 'SAVINGS' | 'CURRENT' | 'FIXED_DEPOSIT';
  accountTypeDescription: string;
  interestRate: number;
  status: 'ACTIVE' | 'SUSPENDED' | 'CLOSED';
  createdAt: string;
}

export interface CreateAccountRequest {
  accountType: 'SAVINGS' | 'CURRENT' | 'FIXED_DEPOSIT';
  initialDeposit: number;
}

export interface DepositRequest {
  amount: number;
}
