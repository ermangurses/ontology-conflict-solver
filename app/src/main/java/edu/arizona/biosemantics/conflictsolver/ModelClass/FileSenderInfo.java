
package edu.arizona.biosemantics.conflictsolver.ModelClass;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class FileSenderInfo implements Parcelable {

    @SerializedName("file_type")
    private String file_type;


    public FileSenderInfo() {}

    public FileSenderInfo(String file_type) {

        this.file_type = file_type;
    }

    public final static Parcelable.Creator<FileSenderInfo> CREATOR = new Creator<FileSenderInfo>() {

        @SuppressWarnings({
            "unchecked"
        })
        public FileSenderInfo createFromParcel(Parcel in) {
            FileSenderInfo instance = new FileSenderInfo();
            instance.file_type = ((String) in.readValue((String.class.getClassLoader())));
            return instance;
        }

        public FileSenderInfo[] newArray(int size) {

            return (new FileSenderInfo[size]);
        }

    };


    public void writeToParcel(Parcel dest, int flags) {

        dest.writeValue(file_type);
    }

    public int describeContents() {

        return  0;
    }

}
