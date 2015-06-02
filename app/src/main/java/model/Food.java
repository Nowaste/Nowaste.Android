package model;

import android.content.ContentValues;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by florian on 01/06/15.
 */
public class Food implements Parcelable {

    private int id;
    private String title;
    private String date;
    private int quantity = 0;

    public Food() {}

    public Food(int id, String isbn, String title, String date, int quantity) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.quantity = quantity;
    }

    public Food(Parcel in) {
        id = in.readInt();
        title = in.readString();
        date = in.readString();
        quantity = in.readInt();
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int wishlist) { this.quantity = quantity; }

    public boolean isEmpty () {
        if (this.title.isEmpty()) {
            return true;
        }
        else {
            return false;
        }
    }

    public ContentValues getContentValues () {
        ContentValues cV = new ContentValues();
        cV.put("title", title);
        cV.put("date", date);
        cV.put("quantity", quantity);
        return cV;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(id);
        out.writeString(title);
        out.writeString(date);
        out.writeInt(quantity);
    }

    public static final Parcelable.Creator<Food> CREATOR = new Parcelable.Creator<Food>() {
        @Override
        public Food createFromParcel(Parcel in) { return new Food(in); }
        @Override
        public Food[] newArray(int size) { return new Food[size]; }
    };
}
