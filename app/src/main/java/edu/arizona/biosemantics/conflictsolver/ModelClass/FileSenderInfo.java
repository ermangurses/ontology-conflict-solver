
package edu.arizona.biosemantics.conflictsolver.ModelClass;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class FileSenderInfo implements Parcelable {

    @SerializedName("sender_name")
    private String sender;
    @SerializedName("sender_age")
    private int age;

    public FileSenderInfo() {}

    public FileSenderInfo(String sender, int age) {

        this.sender = sender;
        this.age = age;
    }

    public final static Parcelable.Creator<FileSenderInfo> CREATOR = new Creator<FileSenderInfo>() {

        @SuppressWarnings({
            "unchecked"
        })
        public FileSenderInfo createFromParcel(Parcel in) {
            FileSenderInfo instance = new FileSenderInfo();
            instance.sender = ((String) in.readValue((String.class.getClassLoader())));
            instance.age = ((int) in.readValue((int.class.getClassLoader())));
            return instance;
        }

        public FileSenderInfo[] newArray(int size) {

            return (new FileSenderInfo[size]);
        }

    };


    public void writeToParcel(Parcel dest, int flags) {

        dest.writeValue(sender);
        dest.writeValue(age);
    }

    public int describeContents() {


        return  0;
    }

}
