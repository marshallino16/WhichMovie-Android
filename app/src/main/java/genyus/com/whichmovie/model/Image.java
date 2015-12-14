package genyus.com.whichmovie.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.felipecsl.asymmetricgridview.library.model.AsymmetricItem;

import java.io.Serializable;

/**
 * Created by genyus on 13/12/15.
 */
public class Image implements Serializable, AsymmetricItem {

    private int columnSpan;
    private int rowSpan;
    private int position;

    public String path;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Image(){
        this.columnSpan = Math.random() < 0.2f ? 2 : 1;;
        this.rowSpan = Math.random() < 0.2f ? 2 : 1;
        this.position = 0;
    }

    public Image(int columnSpan, int rowSpan, int position) {
        this.columnSpan = columnSpan;
        this.rowSpan = rowSpan;
        this.position = position;
    }

    public Image(Parcel in) {
        readFromParcel(in);
    }

    @Override public int getColumnSpan() {
        return columnSpan;
    }

    @Override public int getRowSpan() {
        return rowSpan;
    }

    public int getPosition() {
        return position;
    }

    @Override public String toString() {
        return String.format("%s: %sx%s", position, rowSpan, columnSpan);
    }

    @Override public int describeContents() {
        return 0;
    }

    private void readFromParcel(Parcel in) {
        columnSpan = in.readInt();
        rowSpan = in.readInt();
        position = in.readInt();
    }

    @Override public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(columnSpan);
        dest.writeInt(rowSpan);
        dest.writeInt(position);
    }

    /* Parcelable interface implementation */
    public static final Parcelable.Creator<Image> CREATOR = new Parcelable.Creator<Image>() {

        @Override public Image createFromParcel(@NonNull Parcel in) {
            return new Image(in);
        }

        @Override @NonNull public Image[] newArray(int size) {
            return new Image[size];
        }
    };
}
