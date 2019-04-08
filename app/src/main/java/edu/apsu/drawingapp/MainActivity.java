package edu.apsu.drawingapp;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
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
    public static int x=0;
    public static int y=0;
    public static int rectWidth=0;
    public static int rectHeight=0;
    public static int radius=0;
    public static int numbersSet=0;

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
                    Log.i("NUMBERS", "Rectangle Details: W= " + rectWidth + " H= " + rectHeight + " X=" + x + " Y=" + y);

                    alertDialogRect();

                } else if (menuItem.getItemId() == R.id.circle) {
                    textTargetUri.setText("Circle");
                    alertDialogCircle();
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
                Drawable drawable = new BitmapDrawable(getResources(), bitmap);
                drawingView.setBackground(drawable);

            } catch (FileNotFoundException e){
                e.printStackTrace();
            }
        }
    }

    private void alertDialogRect() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.rectangle_activity, null);
        dialog.setView(dialogView);

        final EditText width_et = (EditText) dialogView.findViewById(R.id.width_editText);
        final EditText height_et = (EditText) dialogView.findViewById(R.id.height_editText);
        final EditText x_et = (EditText) dialogView.findViewById(R.id.r_x_editText);
        final EditText y_et = (EditText) dialogView.findViewById(R.id.r_y_editText);
        final TextView tv = (TextView) dialogView.findViewById(R.id.error_textView);

        dialog.setPositiveButton("Create", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                if(!width_et.getText().equals("") && !height_et.getText().equals("") && !x_et.getText().equals("") && !y_et.getText().equals("")) {
                    Log.i("LOCATION", "In if statement");
                    rectWidth = Integer.parseInt(width_et.getText().toString());
                    rectHeight = Integer.parseInt(height_et.getText().toString());
                    x = Integer.parseInt(x_et.getText().toString());
                    y = Integer.parseInt(y_et.getText().toString());

                    numbersSet = 1;
                } else {
                    String msg = "Please make sure all fields are filled in.";
                    tv.setText(msg);
                }

                Log.i("NUMBERS", "Rectangle Details: W= " + rectWidth + " H= " + rectHeight + " X=" + x + " Y=" + y);
                Toast.makeText(getApplicationContext(), "Rectangle Created", Toast.LENGTH_SHORT).show();
            }
        });

        AlertDialog alertDialog = dialog.create();
        alertDialog.show();
    }

    private void alertDialogCircle() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        dialog.setView(inflater.inflate(R.layout.circle_activity, null));

        dialog.setPositiveButton("Create", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                Toast.makeText(getApplicationContext(), "Rectangle Created", Toast.LENGTH_SHORT).show();
            }
        });

        AlertDialog alertDialog = dialog.create();
        alertDialog.show();
    }
}
