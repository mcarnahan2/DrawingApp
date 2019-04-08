package edu.apsu.drawingapp;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SubMenu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class MainActivity extends AppCompatActivity {

    DrawingView drawingView;

    private AlertDialog dialog;
    private View dialogView;
    private SeekBar sbwidth;
    private int paintWidth;
    TextView textTargetUri;
    public static Bitmap bitmap;
    public static int backgroundColor = 0;
    public static int buttonPressed =0;
    public static String text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textTargetUri = findViewById(R.id.textView);
        drawingView = findViewById(R.id.drawingView);

        ImageButton shapeButton = findViewById(R.id.shape_imageButton);

        final PopupMenu popupMenuShape = new PopupMenu(getApplicationContext(), shapeButton);
        final Menu shapeMenu = popupMenuShape.getMenu();
        popupMenuShape.getMenuInflater().inflate(R.menu.shape_menu, shapeMenu );

        popupMenuShape.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.rect) {
                    textTargetUri.setText("Rectangle");
                    buttonPressed = 2;
                    Log.i("BUTTON", "Button is " + buttonPressed);
                } else if (menuItem.getItemId() == R.id.circle) {
                    textTargetUri.setText("Circle");
                    buttonPressed = 3;
                    Log.i("BUTTON", "Button is " + buttonPressed);
                }
                return false;
            }
        });

        shapeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupMenuShape.show();
            }
        });

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
        ImageButton pictureButton = findViewById(R.id.picture_button);
        pictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 0);
            }
        });

        ImageButton saveButton = findViewById(R.id.save_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bitmap bitmap = drawingView.getDrawingCache();

                /*
                    public static final String insertImage (ContentResolver cr, Bitmap source,
                    String title, String description)

                        Insert an image and create a thumbnail for it.

                    Parameters
                        cr : The content resolver to use
                        source : The stream to use for the image
                        title : The name of the image
                        description : The description of the image

                    Returns
                        The URL to the newly created image, or null if the image
                        failed to be stored for any reason.
                */

                // Save image to gallery
                String savedImageURL = MediaStore.Images.Media.insertImage(
                        getContentResolver(),
                        bitmap, "", ""
                );

                // Parse the gallery image url to uri
                Uri savedImageURI = Uri.parse(savedImageURL);
                getApplicationContext().grantUriPermission("edu.apsu.drawingapp",
                        savedImageURI, Intent.FLAG_GRANT_READ_URI_PERMISSION);

                // Display the saved image to ImageView
               // drawingView.setImageURI(savedImageURI);

                // Display saved image url to TextView
                textTargetUri.setText("Image saved to gallery." + savedImageURL);
               /* try {
                    drawingView.setDrawingCacheEnabled(true);
                    Bitmap bitmap = drawingView.getDrawingCache();
                    File f = null;
                    if(Environment.getExternalStorageDirectory().equals(Environment.MEDIA_MOUNTED)){
                        File file = new File(Environment.getExternalStorageDirectory(),"Gallery");
                        if(!file.exists()){
                            file.mkdirs();
                        }
                        f = new File(file.getAbsolutePath()+file.separator+ "filename"+".jpg");
                    }
                    FileOutputStream os = new FileOutputStream(f);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 10, os);
                    os.close();
                } catch(Exception e){
                    e.printStackTrace();
                } */
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
                    buttonPressed=1;
                } else if (menuItem.getItemId() == R.id.orange) {
                 //   drawingView.setColor(getResources().getColor(R.color.orange));
                    buttonPressed=1;
                } else if(menuItem.getItemId() == R.id.yellow){
                    drawingView.setColor(Color.YELLOW);
                    buttonPressed=1;
                } else if(menuItem.getItemId() == R.id.green){
                    drawingView.setColor(Color.GREEN);
                    buttonPressed=1;
                } else if(menuItem.getItemId() == R.id.blue){
                    drawingView.setColor(Color.BLUE);
                    buttonPressed=1;
                } else if(menuItem.getItemId() == R.id.purple){
                 //   drawingView.setColor(getResources().getColor(R.color.purple));
                    buttonPressed=1;
                } else if(menuItem.getItemId() == R.id.black){
                    drawingView.setColor(Color.BLACK);
                    buttonPressed=1;
                } else if(menuItem.getItemId() == R.id.white){
                    drawingView.setColor(Color.WHITE);
                    buttonPressed=1;
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

        ImageButton textButton = findViewById(R.id.text_imageButton4);
        textButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog();
            }
        });

        ImageButton fillButton = findViewById(R.id.fill_imageButton6);
        final PopupMenu popupMenuBG = new PopupMenu(getApplicationContext(), fillButton);
        final Menu menuBG = popupMenuBG.getMenu();
        popupMenuBG.getMenuInflater().inflate(R.menu.bg_color_menu, menuBG );

        popupMenuBG.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.red) {
                    drawingView.setBackgroundColor(Color.RED);
                } else if (menuItem.getItemId() == R.id.orange) {
                    drawingView.setBackgroundColor(getResources().getColor(R.color.orange));
                } else if(menuItem.getItemId() == R.id.yellow){
                    drawingView.setBackgroundColor(Color.YELLOW);
                } else if(menuItem.getItemId() == R.id.green){
                    drawingView.setBackgroundColor(Color.GREEN);
                } else if(menuItem.getItemId() == R.id.blue){
                    drawingView.setBackgroundColor(Color.BLUE);
                } else if(menuItem.getItemId() == R.id.purple){
                    drawingView.setBackgroundColor(getResources().getColor(R.color.purple));
                } else if(menuItem.getItemId() == R.id.black){
                    drawingView.setBackgroundColor(Color.BLACK);
                } else if(menuItem.getItemId() == R.id.white){
                    drawingView.setBackgroundColor(Color.WHITE);
                }
                return false;
            }
        });

        fillButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupMenuBG.show();
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

        Button clearButton = findViewById(R.id.clear_button);
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawingView.clearScreen();
                Log.i("CLEAR", "clear screen");
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
                Drawable drawable = new BitmapDrawable(getResources(), bitmap);
                drawingView.setBackground(drawable);

            } catch (FileNotFoundException e){
                e.printStackTrace();
            }
        }
    }

    private void alertDialog(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);

        dialog.setTitle("Enter Text");
        dialog.setMessage("Please enter some text:");

        final EditText et = new EditText(this);
        dialog.setView(et);

        dialog.setPositiveButton("Place text", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                text = et.getText().toString();
                Log.i("TEXT", "text is :" + text);
                buttonPressed=4;
                Log.i("BUTTON", "Button is " + buttonPressed);
            }
        });

        AlertDialog alertDialog = dialog.create();
        alertDialog.show();
    }

    private void alertDialogColor(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);

        dialog.setTitle("Enter Text");
        dialog.setMessage("Please enter some text:");

        final EditText et = new EditText(this);
        dialog.setView(et);

        dialog.setPositiveButton("Place text", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                text = et.getText().toString();
                Log.i("TEXT", "text is :" + text);
                buttonPressed=4;
                Log.i("BUTTON", "Button is " + buttonPressed);
            }
        });

        AlertDialog alertDialog = dialog.create();
        alertDialog.show();
    }

}
