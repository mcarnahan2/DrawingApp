package edu.apsu.drawingapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import java.io.FileNotFoundException;

public class MainActivity extends AppCompatActivity {

    DrawingView drawingView;
    TextView textTargetUri;
    public static Bitmap bitmap;
    public static int backgroundColor = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textTargetUri = findViewById(R.id.textView);
        drawingView = findViewById(R.id.drawingView);
        Button pictureButton = findViewById(R.id.picture_button);
        pictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 0);
            }
        });

        Button paintButton = findViewById(R.id.paint_button);
        paintButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backgroundColor = Color.BLUE;
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK){
            Uri targetUri = data.getData();
            textTargetUri.setText(targetUri.toString());

            try{
                bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(targetUri));

            } catch (FileNotFoundException e){
                e.printStackTrace();
            }



        }
    }
}
