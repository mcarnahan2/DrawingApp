package edu.apsu.drawingapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;

public class MainActivity extends AppCompatActivity {

    DrawingView drawingView;

    private AlertDialog dialog;
    private View dialogView;
    private SeekBar sbwidth;
    private int paintWidth;
    TextView textTargetUri;
    public static Bitmap bitmap;
    public static int backgroundColor = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textTargetUri = findViewById(R.id.textView);
        drawingView = findViewById(R.id.drawingView);
        sbwidth = findViewById(R.id.seekBar1);
        sbwidth.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                drawingView.setPaintWidth(i + 3);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        Button pictureButton = findViewById(R.id.picture_button);
        pictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 0);
            }
        });

        Button saveButton = findViewById(R.id.save_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SaveImage.saveScreen(drawingView)) {
                    Toast.makeText(getApplicationContext(), "Save Succesful", Toast.LENGTH_SHORT);
                } else {
                    Toast.makeText(getApplicationContext(), "Save Failed", Toast.LENGTH_SHORT);

                }
            }
        });

        ImageButton colorButton = findViewById(R.id.color_imageButton3);

        final PopupMenu popupMenu = new PopupMenu(getApplicationContext(), colorButton);
        final Menu menu = popupMenu.getMenu();
        popupMenu.getMenuInflater().inflate(R.menu.color_menu, menu );

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.red) {
                    drawingView.setColor(Color.RED);
                } else if (menuItem.getItemId() == R.id.blue) {
                    drawingView.setColor(Color.BLUE);
                }
                return false;
            }
        });
        colorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupMenu.show();
            }
        });

        ImageButton drawButton = findViewById(R.id.draw_imageButton2);
        drawButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawingView.setStyle(DrawingView.PEN);
            }
        });

        ImageButton fillButton = findViewById(R.id.fill_imageButton6);
        fillButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawingView.setStyle(DrawingView.PAIL);
            }
        });

        ImageButton eraserButton = findViewById(R.id.eraser_imageButton5);
        eraserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawingView.setStyle(DrawingView.PEN);
                drawingView.setColor(Color.TRANSPARENT);
                drawingView.setPaintWidth(paintWidth);
            }
        });

        /*Button clearButton = findViewById(R.id.clear_button);
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawingView.clearScreen();
            }
        });*/

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
