package knormal.justcart.JustCart_ver4;

public class PersonalData {

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


    public String getincomeDate() {
        return incomeDate;
    }

    public String getshelfLife() {
        return shelfLife;
    }
    
    public String getproductName() { return productName; }
    
    public String getproductPrice() {
        return productPrice;
    }

    public String getclassNum() {
        return classNum;
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


    public void setincomeDate(String incomeDate) {
        this.incomeDate = incomeDate;
    }

    public void setshelfLife(String shelfLife) {
        this.shelfLife = shelfLife;
    }

    public void setproductName(String productName) {
        this.productName = productName;
    }

    public void setproductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public void setclassNum(String classNum) {
        this.classNum = classNum;
    }
    


}
