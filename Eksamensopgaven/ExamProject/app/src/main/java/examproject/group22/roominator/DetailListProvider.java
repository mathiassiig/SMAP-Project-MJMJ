package examproject.group22.roominator;

import java.security.Timestamp;
import java.text.DateFormat;
import java.util.Date;

/**
 * Created by Maria Dam on 10-10-2016.
 */

public class DetailListProvider {
    //private Timestamp buy_date;
    private String product_name;
    private String product_price;

    public DetailListProvider(String product_name, String product_price){
        this.setProductName(product_name);
        this.setProductPrice(product_price);
    }


    public String getProductName() {
        return product_name;
    }

    public void setProductName(String product_name) {
        this.product_name = product_name;
    }

    public String getProductPrice() {
        return product_price;
    }

    public void setProductPrice(String product_price) {
        this.product_price = product_price;
    }
}
