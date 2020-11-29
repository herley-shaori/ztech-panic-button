package com.example.panickingbutton;

import android.content.Context;
import android.telephony.SmsManager;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class OperasiKontak {
    private final Gson gson;
    private final Context context;

    public OperasiKontak(Context context) {
        this.gson = new Gson();
        this.context = context;
    }

    public List<Kontak> baca() {
        File file = this.context.getFileStreamPath("daftar_kontak.json");
        int READ_BLOCK_SIZE = 100;
        if (file.exists()) {
            String jsonString = "";
            try {
                FileInputStream fileIn=this.context.openFileInput("daftar_kontak.json");
                InputStreamReader InputRead= new InputStreamReader(fileIn);

                char[] inputBuffer= new char[READ_BLOCK_SIZE];
                String s="";
                int charRead;

                while ((charRead=InputRead.read(inputBuffer))>0) {
                    // char to string conversion
                    String readstring=String.copyValueOf(inputBuffer,0,charRead);
                    s +=readstring;
                }
                InputRead.close();
                jsonString = s;
            } catch (Exception e) {
                e.printStackTrace();
            }

            Type listType = new TypeToken<List<Kontak>>() {}.getType();
            List<Kontak> posts = this.gson.fromJson(jsonString, listType);
            return posts;
        }else{
            System.err.println("Pengembalian operasi kontak mendapat NULL");
            return  null;
        }
    }

    public void tulis(Kontak kontak) {
        File f = this.context.getFileStreamPath("daftar_kontak.json");
        if (f.exists()) {

            String jsonString = "";
            int READ_BLOCK_SIZE = 100;
            try {
                FileInputStream fileIn=this.context.openFileInput("daftar_kontak.json");
                InputStreamReader InputRead= new InputStreamReader(fileIn);

                char[] inputBuffer= new char[READ_BLOCK_SIZE];
                String s="";
                int charRead;

                while ((charRead=InputRead.read(inputBuffer))>0) {
                    // char to string conversion
                    String readstring=String.copyValueOf(inputBuffer,0,charRead);
                    s +=readstring;
                }
                InputRead.close();
                jsonString = s;
            } catch (Exception e) {
                e.printStackTrace();
            }

            Type listType = new TypeToken<List<Kontak>>() {}.getType();
            List<Kontak> posts = this.gson.fromJson(jsonString, listType);
            Set<String> set = new HashSet();
            for (Kontak kontakan : posts) {
                set.add(kontakan.getNomorKontak());
            }
            if (!set.contains(kontak.getNomorKontak())) {
                posts.add(kontak);
                String atsugai = this.gson.toJson(posts);
                try {
                    FileOutputStream fOut = this.context.openFileOutput("daftar_kontak.json", Context.MODE_PRIVATE);
                    OutputStreamWriter outputWriter=new OutputStreamWriter(fOut);
                    outputWriter.write(atsugai);
                    outputWriter.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else{
                System.out.println("Kontak yang sama tidak disimpan.");
            }
        } else {
            File file = new File("daftar_kontak.json");
            final ArrayList<Kontak> daftarKontak = new ArrayList<Kontak>();
            daftarKontak.add(kontak);
            String atsugai = this.gson.toJson(daftarKontak);
            try {
                FileOutputStream fOut = this.context.openFileOutput("daftar_kontak.json", Context.MODE_PRIVATE);
                OutputStreamWriter outputWriter=new OutputStreamWriter(fOut);
                outputWriter.write(atsugai);
                outputWriter.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void hapusKontak(String nomorKontak){
        File file  = this.context.getFileStreamPath("daftar_kontak.json");
        if(file.exists()){
            String jsonString = "";
            int READ_BLOCK_SIZE = 100;
            try {
                FileInputStream fileIn=this.context.openFileInput("daftar_kontak.json");
                InputStreamReader InputRead= new InputStreamReader(fileIn);
                char[] inputBuffer= new char[READ_BLOCK_SIZE];
                String s="";
                int charRead;

                while ((charRead=InputRead.read(inputBuffer))>0) {
                    String readstring=String.copyValueOf(inputBuffer,0,charRead);
                    s +=readstring;
                }
                InputRead.close();
                jsonString = s;
            } catch (Exception e) {
                e.printStackTrace();
            }

            Type listType = new TypeToken<List<Kontak>>() {}.getType();
            List<Kontak> posts = this.gson.fromJson(jsonString, listType);
            int indeksHapus = -1;
            for(int i=0; i<posts.size(); i++){
                if(posts.get(i).getNomorKontak().equals(nomorKontak)){
                    indeksHapus = i;
                    break;
                }
            }

            if(indeksHapus != -1){
                posts.remove(indeksHapus);
                String atsugai = this.gson.toJson(posts);
                try {
                    FileOutputStream fOut = this.context.openFileOutput("daftar_kontak.json", Context.MODE_PRIVATE);
                    OutputStreamWriter outputWriter=new OutputStreamWriter(fOut);
                    outputWriter.write(atsugai);
                    outputWriter.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else{
                Toast.makeText(this.context, "Indeks kontak tidak ditemukan.\nKontak tidak dapat dihapus.",
                        Toast.LENGTH_SHORT).show();
            }
        }else{
            System.err.println("Tidak bisa menghapus berkas karena berkas tidak ada.");
        }
    }

    public void hubungiKontak(String nomorKontak, double latitude, double longitude){
        String pesan = "Help me. My location:\n" +
                "http://maps.google.com/maps?z=12&t=m&q=loc:"+latitude+"+"+longitude;
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(nomorKontak, null, pesan, null, null);
        System.out.println("SMS sudah dikirim");
    }
}
