package com.example.cobaaan;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.gkemon.XMLtoPDF.PdfGenerator;
import com.gkemon.XMLtoPDF.PdfGeneratorListener;
import com.gkemon.XMLtoPDF.model.FailureResponse;
import com.gkemon.XMLtoPDF.model.SuccessResponse;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class ProduksiActivity extends AppCompatActivity {

    Button makePdf;
    EditText edtFilename;

    int pageHeight = 1120;
    int pagewidth = 792;


    Bitmap bmp, scaledbmp;


    private static final int PERMISSION_REQUEST_CODE = 200;

    Context context;
    ArrayList<DataUser> listData = new ArrayList<>();
    AdapterRows adapterRows;
    RecyclerView rv;

    DatabaseReference database = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.produksi);
        context = this;
        rv = findViewById(R.id.rv_item_table);


        database.child("user").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                setupRecycler(snapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        makePdf = findViewById(R.id.makePdf);
        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.pohon);
        scaledbmp = Bitmap.createScaledBitmap(bmp, 140, 140, false);



        if (checkPermission()) {
            Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
        } else {
            requestPermission();
        }

        makePdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generateTableToPdf();

                //  generatePDF();

//                File file = new File(getExternalCacheDir(), "DefaultName.pdf");
//
//                AlertDialog.Builder dialog = new AlertDialog.Builder(ProduksiActivity.this);
//                View view = View.inflate(ProduksiActivity.this, R.layout.sheet_layout, null);
//                dialog.setView(view);
//
//
//                edtFilename = view.findViewById(R.id.inputNew);
//                edtFilename.setText(String.valueOf(file));
//
//                dialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        Log.d("FILE", file.toString());
//
//                        String newFileName = edtFilename.getText().toString();
//
//
//                            generateTableToPdf(new File(newFileName));
//
//
//                    }
//                });
//
//                dialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        dialogInterface.dismiss();
//                    }
//                });
//
//                dialog.show();

            }
        });
    }


    private void generateTableToPdf() {
        PdfGenerator.getBuilder()
                .setContext(this)
                .fromViewIDSource()
                .fromViewID(this, R.id.tables)
                .setFileName("Default")
                .setFolderNameOrPath("Test-PDF-folder")
                .actionAfterPDFGeneration(PdfGenerator.ActionAfterPDFGeneration.OPEN)
                .build(new PdfGeneratorListener() {
                    @Override
                    public void onFailure(FailureResponse failureResponse) {
                        super.onFailure(failureResponse);
                    }

                    @Override
                    public void showLog(String log) {
                        super.showLog(log);
                    }

                    @Override
                    public void onStartPDFGeneration() {
                        /*When PDF generation begins to start*/
                        Toast.makeText(ProduksiActivity.this, "Start generating...", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFinishPDFGeneration() {
                        /*When PDF generation is finished*/
                        Toast.makeText(ProduksiActivity.this, "Create PDF is successfully", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onSuccess(SuccessResponse response) {
                        super.onSuccess(response);
                    }
                });
    }

    private void generatePDF() {
        PdfDocument pdfDocument = new PdfDocument();

        Paint paint = new Paint();
        Paint title = new Paint();

        PdfDocument.PageInfo mypageInfo = new PdfDocument.PageInfo.Builder(pagewidth, pageHeight, 1).create();
        PdfDocument.Page myPage = pdfDocument.startPage(mypageInfo);

        Canvas canvas = myPage.getCanvas();
        canvas.drawBitmap(scaledbmp, 56, 40, paint);

        title.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        title.setTextSize(15);
        title.setColor(ContextCompat.getColor(this, R.color.purple_200));

        canvas.drawText("Wit App", 209, 100, title);
        canvas.drawText("Data Hasil Produksi", 209, 80, title);

        title.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        title.setColor(ContextCompat.getColor(this, R.color.purple_200));
        title.setTextSize(15);

        title.setTextAlign(Paint.Align.CENTER);
        canvas.drawText("tesss....", 396, 560, title);


        pdfDocument.finishPage(myPage);

        File file = new File(getExternalCacheDir(), "Default11.pdf");

        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        View view = View.inflate(this, R.layout.sheet_layout, null);
        dialog.setView(view);


        edtFilename = view.findViewById(R.id.inputNew);
        edtFilename.setText(String.valueOf(file));

        dialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Log.d("FILE", file.toString());

                String newFileName = edtFilename.getText().toString();

                try {
                    pdfDocument.writeTo(new FileOutputStream(new File(newFileName)));
                    Toast.makeText(ProduksiActivity.this, "PDF file generated successfully.", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                pdfDocument.close();
            }
        });

        dialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        dialog.show();


    }

    private boolean checkPermission() {
        // checking of permissions.
        int permission1 = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int permission2 = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
        return permission1 == PackageManager.PERMISSION_GRANTED && permission2 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        // requesting permissions if not provided.
        ActivityCompat.requestPermissions(this, new String[]{WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0) {

                // after requesting permissions we are showing
                // users a toast message of permission granted.
                boolean writeStorage = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                boolean readStorage = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                if (writeStorage && readStorage) {
                    Toast.makeText(this, "Permission Granted..", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Permission Denied.", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }

    }


    private void setupRecycler(DataSnapshot snapshot) {
        listData.clear();
        for (DataSnapshot item : snapshot.getChildren()) {
            DataUser user = item.getValue(DataUser.class);
            listData.add(user);
        }
        Log.d("LIST_DATA", listData.toString());

        adapterRows = new AdapterRows(listData);
        rv.setAdapter(adapterRows);
        rv.setHasFixedSize(true);
    }


}