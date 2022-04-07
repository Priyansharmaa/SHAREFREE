package com.example.sharefree;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class UserProfile extends AppCompatActivity {
    TextInputLayout frstname,lstname; String s1,s2,s3,s4;
    TextInputEditText frstname1,lstname1;
    MaterialButton updatebtn,checkbtn,addbtn;
    String stringjsonobject;
    Uri selectedImageUri;
    //ImageView imageviewprofile;
    //Handler handler;
    ShapeableImageView shapeableImageView;
    String pat="jhj";
    File ff;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        frstname = (TextInputLayout) findViewById(R.id.textFieldfrstname);
        lstname = (TextInputLayout) findViewById(R.id.textFieldsurname);
        updatebtn = (MaterialButton) findViewById(R.id.updatebutton);
        checkbtn = (MaterialButton) findViewById(R.id.checkbutton);
        addbtn=(MaterialButton)findViewById(R.id.addbtn);
        shapeableImageView=(ShapeableImageView)findViewById(R.id.shapeimgview);
        frstname1 = (TextInputEditText) findViewById(R.id.textFieldfrstname1);
        lstname1 = (TextInputEditText) findViewById(R.id.textFieldsurname1);


        updatebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                s1 = frstname1.getText().toString();
                s2 = lstname1.getText().toString();

                Log.d("frstname", s1);
                Log.d("frstname2", s2);
                //BufferedImage image = ImageIO.read(fileOrInputStreamOrURL)

                JSONObject userdetails = new JSONObject();
                try {
                    userdetails.put("firstname", s1);
                    userdetails.put("lastname", s2);
                    userdetails.put("Profileimg",pat);
                    //getImageRealPath(getContentResolver(), uri, null);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                String jsonobject = userdetails.toString();

                Log.d("fname", jsonobject);


                File file = new File(getApplicationContext().getFilesDir(), "userdetails1.json");
                FileWriter fileWriter = null;
                try {
                    fileWriter = new FileWriter(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                try {
                    bufferedWriter.write(jsonobject);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    bufferedWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Intent it=new Intent(getApplicationContext(),mainPage.class);
                startActivity(it);

            }


        });


        checkbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                File file = new File(getApplicationContext().getFilesDir(), "userdetails.json");
                FileReader fileReader = null;
                try {
                    fileReader = new FileReader(file);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                BufferedReader bufferedReader = new BufferedReader(fileReader);
                StringBuilder stringBuilder = new StringBuilder();
                String line = null;
                try {
                    line = bufferedReader.readLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                while (line != null) {
                    stringBuilder.append(line).append("\n");
                    try {
                        line = bufferedReader.readLine();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                String responce = stringBuilder.toString();

                try {
                    JSONObject jsonObject  = new JSONObject(responce);
                    stringjsonobject = jsonObject.toString();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Log.d("jsonfiledata",stringjsonobject);



//Java Object


                //  Log.d("jsonfiledata", line);
                //  Toast.makeText(UserProfile.this,line,Toast.LENGTH_LONG).show();


            }


        });


        addbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                i.setType("image/*");
                i.setAction(Intent.ACTION_GET_CONTENT);

                // pass the constant to compare it
                // with the returned requestCode
                startActivityForResult(Intent.createChooser(i, "Select Picture"), 100);
                // imageviewprofile.setImageBitmap(selectedImageUri);
                //imageviewprofile.setImageURI(selectedImageUri);
                //imageviewprofile.setImageURI(selectedImageUri);
            }
        });




    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

            // compare the resultCode with the
            // SELECT_PICTURE constant
            if (requestCode == 100) {
                // Get the url of the image from data
                selectedImageUri = data.getData();
                ff=new File(selectedImageUri.getPath());
                File ff1=new File(getApplicationContext().getFilesDir(), "dp.jpg");
                //imageviewprofile.setImageURI(selectedImageUri);
                String [] proj={MediaStore.Images.Media.DATA};
                Cursor cursor = managedQuery(selectedImageUri, proj, null, null, null);
                cursor.moveToFirst();
                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                pat=pat+cursor.getString(idx);
                shapeableImageView.setImageURI(selectedImageUri);
                if (null != selectedImageUri) {
                    // update the preview image in the layout
                    //iv.setImageURI(selectedImageUri);
                }
            }
            //Bitmap photo = (Bitmap) data.getExtras().get("data");
            //shapeableImageView.setImageURI(data.getData());
            //knop.setVisibility(Button.VISIBLE);


            // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
            //Uri tempUri = data.getData();

            // CALL THIS METHOD TO GET THE ACTUAL PATH
            //File finalFile = new File(getRealPathFromURI(tempUri));
            //File f=finalFile;
            //System.out.println(mImageCaptureUri);
        }
    }


    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        pat=cursor.getString(idx);
        return cursor.getString(idx);
    }



}