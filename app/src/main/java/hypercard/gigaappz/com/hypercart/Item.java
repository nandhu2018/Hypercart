package hypercard.gigaappz.com.hypercart;

/**
 * Created by Nandhu on 18-03-2018.
 */

public class Item {
    String birdListName;
    String birdListImage;

    public Item(String birdName, String birdImage)
    {
        this.birdListImage=birdImage;
        this.birdListName=birdName;
    }
    public String getbirdName()
    {
        return birdListName;
    }
    public String getbirdImage()
    {
        return birdListImage;
    }
}
