package knormal.justcart.JustCart_ver4;

public class RecommData {
    private String Name;
    private String Price;
    private String Desc;
    private String Location;
    private String Image;

    //신선도용
    private String incomeDate;
    private String shelfLife;

    //USERBASCKET용
    private String productName;
    private String productPrice;
    private String classNum;

    //Error detection용
    private String errordata;


    public String getName() {
        return Name;
    }

    public String getPrice() {
        return Price;
    }

    public String getDesc() {
        return Desc;
    }

    public String getLocation() {
        return Location;
    }

    public String getImage() {
        return Image;
    }


    public String getproductPrice() {
        return productPrice;
    }



    public void setName(String Name) {
        this.Name = Name;
    }

    public void setPrice(String Price) {
        this.Price = Price;
    }

    public void setDesc(String Desc) {
        this.Desc = Desc;
    }

    public void setLocation(String Location) {
        this.Location = Location;
    }

    public void setImage(String Image) {
        this.Image = Image;
    }


}
