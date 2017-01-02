package com.specht.jonas.ros17app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.SharedPreferencesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 1;

    private SharedPreferences sharedPref;

    private File photoFile;

    private Spinner spinner;
    private ImageButton cameraButton, coordButton;
    private ImageView photoView;
    private Button sendButton;
    private TextView textLat, textLon, textRFK;
    private EditText textDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPref = getSharedPreferences(getString(R.string.user_info_file_key), MODE_PRIVATE);
        String firstName = sharedPref.getString(getString(R.string.saved_first_name), null);
        if (firstName == null) {
            startActivity(new Intent(this, UserInfoActivity.class));
        }

        initSpinner();
        initButtons();
        initTextviews();
        initPhotoView();
        initEdittext();
        photoFile = getPhotoFile();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_IMAGE_CAPTURE) {
            updatePhotoView();
        }
    }

    private void updatePhotoView() {
        if (photoFile == null || !photoFile.exists()) {
            photoView.setImageDrawable(null);
        } else {
            //photoView.setImageURI(Uri.fromFile(photoFile));
            Bitmap image = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
            try {
                image = PictureUtils.correctBitmapOrientation(photoFile.getAbsolutePath(), image);
            } catch (Exception e) {
                Log.i("error", "Could not rotate bitmap");
            }

            photoView.setImageBitmap(image);
        }
    }

    private void initButtons() {
        cameraButton = (ImageButton) findViewById(R.id.button_camera);
        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //start camera app
                captureImage();
            }
        });

        coordButton = (ImageButton) findViewById(R.id.button_coordinates);
        coordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get gps coordinates
            }
        });

        sendButton = (Button) findViewById(R.id.button_send);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //make emailtext and start email app
            }
        });
    }

    private void captureImage() {
        final Intent captureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (captureImage.resolveActivity(getPackageManager()) != null) {
            deletePhotoFile();
            Uri photoURI = Uri.fromFile(photoFile);
            captureImage.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            photoView.setImageDrawable(null);
            startActivityForResult(captureImage, REQUEST_IMAGE_CAPTURE);
        }
    }

    private void initSpinner() {
        spinner = (Spinner) findViewById(R.id.spinner_category);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.category_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    private void initTextviews() {
        textLat = (TextView) findViewById(R.id.text_view_lat);
        textLon = (TextView) findViewById(R.id.text_view_lon);
        textRFK = (TextView) findViewById(R.id.text_view_rfk);
    }

    private void initPhotoView() {
        photoView = (ImageView) findViewById(R.id.image_view_main);
    }

    private void initEdittext() {
        textDescription = (EditText) findViewById(R.id.edit_text_description);
    }

    private File getPhotoFile() {
        File externalFilesDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        if (externalFilesDir == null) {
            return null;
        }

        return new File(externalFilesDir, "IMG_test.jpg");
    }

    private boolean deletePhotoFile() {
        boolean deleted = photoFile.delete();
        photoFile = getPhotoFile();
        return deleted;
    }
}
