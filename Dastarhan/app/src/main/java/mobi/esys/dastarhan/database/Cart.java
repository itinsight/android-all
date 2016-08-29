package mobi.esys.dastarhan.database;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;


public class Cart extends RealmObject {

    @PrimaryKey
    private int inner_id = 42;
    private boolean opened;
    private long current_order_id;
    private String notice;

    /**
     * For Realm usage only
     */
    public Cart() {
        //For Realm usage only
    }

    public Cart(boolean opened, long current_order_id, String notice) {
        this.opened = opened;
        this.current_order_id = current_order_id;
        this.notice = notice;
    }

    public boolean isOpened() {
        return opened;
    }

    public long getCurrentOrderID() {
        return current_order_id;
    }

    public String getNotice() {
        return notice;
    }

    public void setOpened(boolean opened) {
        this.opened = opened;
    }

    public void setCurrentOrderID(long currentOrderID) {
        this.current_order_id = currentOrderID;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Cart cart = (Cart) o;

        if (opened != cart.opened) return false;
        if (current_order_id != cart.current_order_id) return false;
        return !(notice != null ? !notice.equals(cart.notice) : cart.notice != null);

    }

    @Override
    public int hashCode() {
        int result = (opened ? 1 : 0);
        result = 31 * result + (int) (current_order_id ^ (current_order_id >>> 32));
        result = 31 * result + (notice != null ? notice.hashCode() : 0);
        return result;
    }
}