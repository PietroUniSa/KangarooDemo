package ita.kangaroo.model;

import java.util.ArrayList;

public class Prefered {

    private ArrayList<PreferedProduct> products;

    public Prefered() {
        products = new ArrayList<PreferedProduct>();
    }

    public void addPreferito(ProdottoBean product) {

        int i, pos = -1;
        for (i = 0; i<products.size(); i++) {
            if (products.get(i).getProduct().getId() == product.getId()) {
                pos = i;
                break;
            }
        }

        //Elemento non presente nel carrello
        if (pos == -1) {
            PreferedProduct p = new PreferedProduct(product);
            this.products.add(p);
        }

        //Elemento presente
        else {
            PreferedProduct p = this.products.get(pos);
        }

    }


    public void removePreferito(ProdottoBean product) {
        for(PreferedProduct c : products) {
            if(c.getProduct().getId() == product.getId()) {
                products.remove(c);
                break;
            }
        }
    }

    public ArrayList<PreferedProduct> getProducts() {
        return products;
    }
}
