package com.example.panickingbutton.dynaview;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.panickingbutton.Kontak;
import com.example.panickingbutton.OperasiKontak;
import com.example.panickingbutton.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AktifitasKriket extends AppCompatActivity implements View.OnClickListener {

    LinearLayout layoutList;
    Button buttonAdd;
    Button buttonSubmitList;
    private final int RESULT_PICK_CONTACT = 1;
    private final OperasiKontak operasiKontak = new OperasiKontak(this);
    private final List<Kontak> daftarKontak = new ArrayList();
    private final Set<String> himpunanKontak = new HashSet();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aktifitas_kriket_main);

        layoutList = findViewById(R.id.layout_list);
        buttonAdd = findViewById(R.id.button_add);
        buttonSubmitList = findViewById(R.id.button_submit_list);

        buttonAdd.setOnClickListener(this);
        buttonSubmitList.setOnClickListener(this);

        // Contact Auto Update.
        perbaruiBaca();
    }

    private void perbaruiBaca(){
        List<Kontak> bacaKontak = this.operasiKontak.baca();
        if(bacaKontak != null && bacaKontak.size() > 0){
            for(Kontak kontak:bacaKontak){
                this.daftarKontak.add(kontak);
                this.himpunanKontak.add(kontak.getNomorKontak());
                addView(kontak);
            }
        }
    }

    private void aktifitasPilihKontak(){
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
        startActivityForResult(intent, RESULT_PICK_CONTACT);
    }

    private void tulisKontak(){
        if(this.daftarKontak.size() > 0){
            List<Integer> indexHapus = new ArrayList();
            for(int i=0; i<this.daftarKontak.size(); i++){
                if(!this.himpunanKontak.contains(this.daftarKontak.get(i).getNomorKontak())){
                    indexHapus.add(i);
                }
            }

            for(int daff:indexHapus){
                this.daftarKontak.remove(daff);
            }

            for(Kontak kontak:this.daftarKontak){
                this.operasiKontak.tulis(kontak);
            }
            Toast.makeText(getBaseContext(), "Contacts Saved.",
                    Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(getBaseContext(), "No contact could be added.",
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.button_add:
                aktifitasPilihKontak();
                break;

            case R.id.button_submit_list:
                tulisKontak();
                break;
        }
    }

    private void addView(Kontak kontak) {
        final View cricketerView = getLayoutInflater().inflate(R.layout.tambah_baris,
                null,false);

        EditText editText = (EditText)cricketerView.findViewById(R.id.edit_cricketer_name);
        EditText editTextNomorTelepon = (EditText)cricketerView.findViewById(R.id.nomor_telepon);
        ImageView imageClose = (ImageView)cricketerView.findViewById(R.id.image_remove);

        String nomorTelepon = kontak.getNomorKontak();
        nomorTelepon = nomorTelepon.replace("+62","0")
                .replace("-","").replace(" ","");

        editText.setText(kontak.getNamaKontak());
        editTextNomorTelepon.setText(nomorTelepon);
        this.himpunanKontak.add(nomorTelepon);
        daftarKontak.add(new Kontak(kontak.getNamaKontak(), nomorTelepon));

        imageClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeView(cricketerView);
            }
        });

        layoutList.addView(cricketerView);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode){
                case RESULT_PICK_CONTACT:
                    contactPicked(data);
                    break;
            }
        }else{
            Toast.makeText(getBaseContext(), "Failed on reading a contact: "+resultCode,
                    Toast.LENGTH_LONG).show();
        }
    }

    private void contactPicked(Intent data){
        Cursor cursor = null;
        String nomorTelepon = null, namaKontak = null;
        try {
            Uri uri = data.getData();
            cursor = getContentResolver().query(uri,null,null,null,null);
            int indexName = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
            int indexNumber = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            cursor.moveToFirst();
            nomorTelepon = cursor.getString(indexNumber);
            namaKontak = cursor.getString(indexName);
        }catch (Exception e){
            e.printStackTrace();
        }

        if(nomorTelepon != null && namaKontak != null){
            Kontak kontakBaru = new Kontak(namaKontak, nomorTelepon);
            addView(kontakBaru);
        }
    }

    private void removeView(View view){
        layoutList.removeView(view);
        EditText nomorTelepon = (EditText) view.findViewById(R.id.nomor_telepon);
        this.himpunanKontak.remove(nomorTelepon.getText().toString());
        this.operasiKontak.hapusKontak(nomorTelepon.getText().toString());
    }
}
