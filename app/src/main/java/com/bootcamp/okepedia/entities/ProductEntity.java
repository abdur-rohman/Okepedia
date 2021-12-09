package com.bootcamp.okepedia.entities;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.bootcamp.okepedia.R;
import com.bumptech.glide.Glide;

import java.io.File;

@Entity(tableName = "products")
public class ProductEntity implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private long id;

    private long quantity, price;
    private String name, photo;

    public ProductEntity(long quantity, long price, String name, String photo) {
        this.quantity = quantity;
        this.price = price;
        this.name = name;
        this.photo = photo;
    }

    protected ProductEntity(Parcel in) {
        id = in.readLong();
        quantity = in.readLong();
        price = in.readLong();
        name = in.readString();
        photo = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeLong(quantity);
        dest.writeLong(price);
        dest.writeString(name);
        dest.writeString(photo);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ProductEntity> CREATOR = new Creator<ProductEntity>() {
        @Override
        public ProductEntity createFromParcel(Parcel in) {
            return new ProductEntity(in);
        }

        @Override
        public ProductEntity[] newArray(int size) {
            return new ProductEntity[size];
        }
    };

    public long getId() {
        return id;
    }

    public long getQuantity() {
        return quantity;
    }

    public long getPrice() {
        return price;
    }

    public String getName() {
        return name;
    }

    public String getPhoto() {
        return photo;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    @BindingAdapter("file")
    public static void setImageFile(ImageView view, String path) {
        if (!TextUtils.isEmpty(path)) {
            File file = new File(path);

            if (file.exists()) Glide.with(view).load(file).into(view);
            else Glide.with(view).load(R.drawable.download).into(view);
        } else {
            Glide.with(view).load(R.drawable.download).into(view);
        }
    }
}
