package com.example.admin123.citytour.Fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.SyncStateContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.admin123.citytour.DbBitmapUtility;
import com.example.admin123.citytour.R;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class PostcardFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private ImageView postcardThumbnail;
    private Bitmap nonCompressedUpload;

    public PostcardFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_postcard, container, false);


        final EditText postcard_message = (EditText) v.findViewById(R.id.postcard_message);

        postcardThumbnail = (ImageView) v.findViewById(R.id.uploaded_image);

        DbBitmapUtility dbBitmapUtility = new DbBitmapUtility();
        if (getArguments() != null) {
            byte[] photoArg = getArguments().getByteArray("photo");
            Bitmap photo = dbBitmapUtility.getImage(photoArg);
            postcardThumbnail.setImageBitmap(photo);
            nonCompressedUpload = photo;
        }

        Button uploadFromGallery = (Button) v.findViewById(R.id.upload_photo);
        uploadFromGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new   Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("video/, images/");
                startActivityForResult(intent, 2);
            }
        });

        Button preview_postcard = (Button) v.findViewById(R.id.preview_postcard);
        preview_postcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (postcard_message.getText() != null && nonCompressedUpload!=null) {
                    previewPostcard(nonCompressedUpload, postcard_message.getText().toString());
                }
            }
        });

        Button share_postcard = (Button) v.findViewById(R.id.share_postcard);
        share_postcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getShareOptions(nonCompressedUpload);
            }
        });


        return v;
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


            float percentage = (float) 0.3;
            int width_res = Math.round(bitmap.getWidth()*percentage);
            int height_res = Math.round(bitmap.getHeight()*percentage);

            nonCompressedUpload = bitmap;
            Bitmap thumbnail = ThumbnailUtils.extractThumbnail(bitmap, width_res, height_res );
            //Attach the canvas to the ImageView
            postcardThumbnail.setImageDrawable(new BitmapDrawable(getResources(), thumbnail));

        }
    }

    public void getShareOptions(Bitmap inImage) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                }, 10);
            } else {

                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                String path = MediaStore.Images.Media.insertImage(getContext().getContentResolver(), inImage, "Title", null);


                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("image/*");
                share.putExtra(Intent.EXTRA_STREAM, Uri.parse(path));
                startActivity(Intent.createChooser(share,"Share via"));
            }
        }
    }


    public void previewPostcard(Bitmap bitmap, String text){

        Integer width = bitmap.getWidth();
        Integer height = bitmap.getHeight();


        //Create a new image bitmap and attach a brand new canvas to it
        Bitmap tempBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        Canvas tempCanvas = new Canvas(tempBitmap);

        //Draw the image bitmap into the canvas
        tempCanvas.drawBitmap(bitmap, 0, 0, null);

        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setTextSize(width/8);

        tempCanvas.drawText(text, 0, height, paint);

        nonCompressedUpload = tempBitmap;
        Bitmap thumbnail = ThumbnailUtils.extractThumbnail(tempBitmap, width, height);

        postcardThumbnail.setImageDrawable(new BitmapDrawable(getResources(), thumbnail));
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
