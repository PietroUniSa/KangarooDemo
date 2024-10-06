package ita.kangaroo.model;

public class CartProduct {

        private ProdottoBean product;
        private int quantity;

        public CartProduct(ProdottoBean product, int quantity) {
            this.product = product;
            this.quantity = quantity;
        }

        public CartProduct (ProdottoBean product) {
            this.product = product;
            this.quantity = 1;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        public int getQuantity () {
            return quantity;
        }

        public ProdottoBean getProduct() {
            return product;
        }

}
