package com.example.admin123.citytour.Fragments.Favourites;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin123.citytour.DbBitmapUtility;
import com.example.admin123.citytour.Fragments.Currency.ChooseCurrencyDialog;
import com.example.admin123.citytour.Fragments.SeeSights.SearchArea.SearchAreaItem;
import com.example.admin123.citytour.R;
import com.google.android.gms.maps.model.LatLng;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import static com.google.android.gms.wearable.DataMap.TAG;

/**
 * Created by theom on 13/03/2017.
 */
public class FragmentFavouriteEdits extends Fragment {

    private FavouritesDBHelper favouritesDB;
    private String locationTitle;
    private EditText userNotesEditText;
    private Button addImageButton1;
    private Button addImageButton2;
    private Button currentImageButton;
    private String currentPhotoCol;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_favourite_edits,container,false);

        favouritesDB = new FavouritesDBHelper(getActivity());
        locationTitle = getArguments().getString("locationTitle");
        userNotesEditText = (EditText) v.findViewById(R.id.user_notes);

        //getNotes(locationTitle);

        Button saveNotesButton = (Button) v.findViewById(R.id.save_notes_button);

        saveNotesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                Integer isUpdate = updateDBNotes(userNotesEditText.getText().toString());
                if (isUpdate == 1){
                    Toast.makeText(getContext(), "Saved notes", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getContext(), "You must favourite the item before you can save notes", Toast.LENGTH_SHORT).show();
                }
                View view = getActivity().getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(getContext().INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }

            }
        });

        addImageButton1 = (Button) v.findViewById(R.id.image1);
        addImageButton2 = (Button) v.findViewById(R.id.image2);


        addImageButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (favouritesDB.isLocationVisited(locationTitle) == true) {
                    currentImageButton = addImageButton1;
                    currentPhotoCol = "USERPHOTO1";
                    addImage(addImageButton1);
                }else{
                    Toast.makeText(getContext(), "You must visit the location before you can put a photo", Toast.LENGTH_SHORT).show();
                }
            }
        });

        addImageButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (favouritesDB.isLocationVisited(locationTitle) == true) {
                    currentImageButton = addImageButton2;
                    currentPhotoCol = "USERPHOTO2";
                    addImage(addImageButton2);
                }else{
                    Toast.makeText(getContext(), "You must favourite before you can put a photo", Toast.LENGTH_SHORT).show();
                }
            }
        });


        getDB();
        favouritesDB.close();

        return v;
    }

    private void getDB(){
        Cursor res = favouritesDB.getLocationData(locationTitle);
        StringBuffer dbContents = new StringBuffer();
        byte [] image1 = new byte[0000];
        byte [] image2 = new byte[0000];

        if (res.getCount() == 0){
            return;
        }
        while (res.moveToNext()){
            dbContents.append(res.getString(5));
            image1 = res.getBlob(9);
            image2 = res.getBlob(10);
        }

        DbBitmapUtility dbBitmapUtility = new DbBitmapUtility();

        if (image1!=null && image1!= new byte[0000]) {
            Bitmap image = dbBitmapUtility.getImage(image1);
            addImageButton1.setBackground(new BitmapDrawable(getResources(), image));
        }

        if (image2!=null && image2!= new byte[0000]) {
            Bitmap image = dbBitmapUtility.getImage(image2);
            addImageButton2.setBackground(new BitmapDrawable(getResources(), image));
        }

        String userNotes = dbContents.toString();
        if (!userNotes.equals("DEFAULT TEXT")){
            userNotesEditText.setText(userNotes);
        }
    }

    private void addImage(Button button){
        Intent intent = new   Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("video/, images/");
        startActivityForResult(intent, 2);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Detects request codes
        if(requestCode==2 && resultCode == Activity.RESULT_OK) {
            Uri selectedImage = data.getData();
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImage);
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            Bitmap thumbnail = ThumbnailUtils.extractThumbnail(bitmap, 300, 300);
            currentImageButton.setBackground(new BitmapDrawable(getResources(), thumbnail));

            updateUserPhoto(thumbnail);

            // uploadedImage.setImageBitmap(canvas);
            Log.i(TAG, bitmap.toString());
        }
    }

    private Integer updateDBNotes(String userNotes){
        Integer updateNotesResults = favouritesDB.updateUserNotes(locationTitle, userNotes);
        Log.i("DB_Helper", "The database was updated with the text: "+userNotes+ " Result: "+updateNotesResults);
        return updateNotesResults;
    }

    private void updateUserPhoto(Bitmap thumbnail){
        DbBitmapUtility dbBitmapUtility = new DbBitmapUtility();
        byte[] photo = dbBitmapUtility.getBytes(thumbnail);
        Integer updateUserPhoto = favouritesDB.addUserPhoto(locationTitle, photo, currentPhotoCol);

        Log.i("DB_Helper", "The database was updated with the photo: "+photo.toString()+ " Result: "+updateUserPhoto);
    }

}
