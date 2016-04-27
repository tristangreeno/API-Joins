
/**
 * Created by tristangreeno on 4/4/16.
 */
public class Item {
    String text;
    Boolean isDone; // use isBoolean for boolean names
    Integer userId;
    Integer itemId;

    public Item(String text, Boolean isDone, Integer userId, Integer itemId) {
        this.text = text;
        this.isDone = isDone;
        this.userId = userId;
        this.itemId = itemId;
    }
}
