public class CartItem {
    private final Product product;
    private int qty; // Hapus modifier 'final' agar qty bisa diperbarui

    public CartItem(Product product, int qty) {
        if (qty <= 0) {
            throw new IllegalArgumentException("Qty harus lebih dari 0");
        }
        this.product = product;
        this.qty = qty;
    }

    public Product getProduct() {
        return product;
    }

    public int getQty() {
        return qty;
    }

    // Challenge 1: menggabungkan Qty produk yang sama
    public void addQty(int additionalQty) {
        if (additionalQty <= 0) {
            throw new IllegalArgumentException("Tambahan Qty harus lebih dari 0");
        }
        this.qty += additionalQty;
    }

    public int getSubtotal() {
        return product.getPrice() * qty;
    }
}