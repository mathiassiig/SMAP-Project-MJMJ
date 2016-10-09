package examproject.group22.roominator;

/**
 * Created by Jonas on 10/9/2016.
 */

public class ShoppingListProvider {
    private String product_name;
    private String product_number;

    public ShoppingListProvider(String product_name, String product_number){
        this.setProduct_name(product_name);
        this.setProduct_number(product_number);
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getProduct_number() {
        return product_number;
    }

    public void setProduct_number(String product_number) {
        this.product_number = product_number;
    }
}
