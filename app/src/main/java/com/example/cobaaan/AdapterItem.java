package com.example.cobaaan;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

@RequiresApi(api = Build.VERSION_CODES.N)
public class AdapterItem extends RecyclerView.Adapter<AdapterItem.ItemViewHolder> {
    Context context;
    ArrayList<DataUser> dataUserArrayList;

    Locale id = new Locale("in", "ID");
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMMM-yyyy", id);

    Calendar calendar = Calendar.getInstance();

    EditText edtPlatKendaraan, edtTanggalUpdate, edtPenerimaanUpdate, edtProduksiUpdate;
    Button btnDateUpdate, btnSimpanUpdate;
    MaterialButton btnDeleteItem;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference reference = database.getReference("user");


    public AdapterItem(Context context, ArrayList<DataUser> dataUserArrayList) {
        this.context = context;
        this.dataUserArrayList = dataUserArrayList;
    }

    @NonNull
    @Override
    public AdapterItem.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);
        return new ItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterItem.ItemViewHolder holder, int position) {
        holder.viewBind(dataUserArrayList.get(position));


        holder.itemView.setOnClickListener(v -> {
            AlertDialog.Builder dialog = new AlertDialog.Builder(context);
            View view = View.inflate(context, R.layout.update_layout, null);
            dialog.setView(view);

            AlertDialog newDialog;
            newDialog = dialog.create();

            edtPlatKendaraan = view.findViewById(R.id.et_nama_update);
            edtTanggalUpdate = view.findViewById(R.id.et_tgl_update);
            edtPenerimaanUpdate = view.findViewById(R.id.et_penerimaan_update);
            edtProduksiUpdate = view.findViewById(R.id.et_produksi_update);
            btnDateUpdate = view.findViewById(R.id.btnDateUpdate);
            btnSimpanUpdate = view.findViewById(R.id.simpanDataUpdate);
            btnDeleteItem = view.findViewById(R.id.btnDeleteItem);


            DataUser dataUser = dataUserArrayList.get(position);


            edtPlatKendaraan.setText(dataUser.getNama());
            edtTanggalUpdate.setText(dataUser.getTgl_pendaftaran());
            edtPenerimaanUpdate.setText(dataUser.getPenerimaan());
            edtProduksiUpdate.setText(dataUser.getProduksi());

            newDialog.show();

            btnDateUpdate.setOnClickListener(clickDate -> {
                DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view1, int year, int month, int dayOfMonth) {
                        calendar.set(year, month, dayOfMonth);
                        edtTanggalUpdate.setText(simpleDateFormat.format(calendar.getTime()));
                        Log.d("dataaa", calendar.getTime().toString());
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            });


            btnSimpanUpdate.setOnClickListener(clickUpdate -> {

                HashMap<String, String> result = new HashMap<>();
                result.put("nama", edtPlatKendaraan.getText().toString());
                result.put("penerimaan", edtPenerimaanUpdate.getText().toString());
                result.put("produksi", edtProduksiUpdate.getText().toString());
                result.put("tgl_pendaftaran", edtTanggalUpdate.getText().toString());

                reference.child(dataUser.getNama())
                        .setValue(result).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(context, "Succes update data", Toast.LENGTH_SHORT).show();
                                newDialog.dismiss();
                            }
                        }); // result success

            }); // btn simpan


            btnDeleteItem.setOnClickListener(clickToDelete -> {
                Log.d("deletee", String.valueOf(dataUser.getNama()));

                String name = String.valueOf(dataUser.getNama());

                reference.orderByChild("nama").equalTo(name)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                for (DataSnapshot delete : snapshot.getChildren()) {
                                    delete.getRef().removeValue();

                                    Toast.makeText(context, "Success delete data", Toast.LENGTH_SHORT).show();
                                    newDialog.dismiss();

                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });


            });


        }); // item click
    }


    @Override
    public int getItemCount() {
        return dataUserArrayList.size();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView tv_nama,
                tv_penerimaaan,
                tv_produksii,
                tv_tanggal_pendaftaran,
                tv_sort,
                tv_keterangan;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_nama = itemView.findViewById(R.id.tv_nama);
            tv_penerimaaan = itemView.findViewById(R.id.tv_penerimaaan);
            tv_produksii = itemView.findViewById(R.id.tv_produksii);
            tv_tanggal_pendaftaran = itemView.findViewById(R.id.tv_tanggal_pendaftaran);
            tv_sort = itemView.findViewById(R.id.tv_sort);
            tv_keterangan = itemView.findViewById(R.id.tv_keterangan);

        }

        public void viewBind(DataUser dataUser) {
            Log.d("hhhhhhhhhhhhhh", String.valueOf(dataUser.getTgl_pendaftaran()));
            tv_nama.setText(dataUser.getNama());
            tv_penerimaaan.setText(dataUser.getPenerimaan());
            tv_produksii.setText(dataUser.getProduksi());
            tv_tanggal_pendaftaran.setText(dataUser.getTgl_pendaftaran());

            int penerimaan = Integer.parseInt(dataUser.getPenerimaan());
            int produksi = Integer.parseInt(dataUser.getProduksi());
            int result = penerimaan - produksi;
            tv_sort.setText(String.valueOf(result));
            if (penerimaan >= 10_000){
                tv_keterangan.setText("Terpenuhi");
            } else {
                tv_keterangan.setText("Tidak Terpenuhi");
            }
        }


    }


}
