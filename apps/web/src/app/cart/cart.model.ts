export interface CartItem {
  product: {
    id: number;
    name: string;
    description: string;
    category: string;
    price: number;
    availableQuantity: number;
  };
  quantity: number;
}

export interface Cart {
  id: number;
  customerId: number;
  items: CartItem[];
}