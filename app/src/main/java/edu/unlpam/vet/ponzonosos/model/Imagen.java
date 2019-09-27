package edu.unlpam.vet.ponzonosos.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import edu.unlpam.vet.ponzonosos.MainActivity;
import edu.unlpam.vet.ponzonosos.util.LoadImage;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Imagen {

    @org.greenrobot.greendao.annotation.Id
    private Long Id;
    private String img;
    private int principal;
    private boolean descargada;

    private Long id_animal;
    
    @Generated(hash = 664125258)
    public Imagen() {
    }
    @Generated(hash = 788050374)
    public Imagen(Long Id, String img, int principal, boolean descargada, Long id_animal) {
        this.Id = Id;
        this.img = img;
        this.principal = principal;
        this.descargada = descargada;
        this.id_animal = id_animal;
    }
    public Long getId() {
        return this.Id;
    }
    public void setId(Long Id) {
        this.Id = Id;
    }
    public String getImg() {
        return this.img;
    }
    public void setImg(String img) {
        this.img = img;
    }
    public int getPrincipal() {
        return this.principal;
    }
    public void setPrincipal(int principal) {
        this.principal = principal;
    }
    public Long getId_animal() {
        return this.id_animal;
    }
    public void setId_animal(Long id_animal) {
        this.id_animal = id_animal;
    }

    public void fillData(JSONObject jsonObject){
        Log.d("rmdebug", "Imagen - fillData");
        try {
            setId(jsonObject.getLong("id"));
            setId_animal(jsonObject.getLong("id_animal"));
            setImg(jsonObject.getString("img"));
            setPrincipal(jsonObject.getInt("es_principal"));
        }catch (Exception e){
            e.printStackTrace();
            Log.d("rmdebug","Imagen - Error al llenarDatos > " + e.toString());
        }
    }

    public void guardar() {
        MainActivity.getInstance().getDaoSession().getImagenDao().insertOrReplace(this);
    }

    public void abmImagenes(JSONArray responseJSONArray, Context mContext) {
        Log.d("rmdebug", "Imagen - abmImagenes(JSONArray)");
        List<Imagen> importedImages = new ArrayList<>();
        for (int i = 0; i < responseJSONArray.length(); i++) {
            Imagen image = new Imagen();
            try {
                image.fillData(responseJSONArray.getJSONObject(i));
                importedImages.add(image);
            } catch (JSONException e) {
                Log.e("rmdebug", "Imagen - abmImagenes(JSONArray) > error " + e.toString());
                e.printStackTrace();
            }
        }
        abmImagenes(importedImages, mContext);
    }

    private void abmImagenes(List<Imagen> importedImages, Context mContext){
        Log.d("rmdebug", "Imagen - abmImagenes(List<Imagen>)");
        List<Imagen> images = MainActivity.getInstance().getDaoSession().getImagenDao()
                .queryBuilder().list();
        List<Imagen> imagesToDelete = new ArrayList<>();
        boolean exists;
        for (Imagen image : images){
            exists = false;
            for (Imagen importedImage : importedImages){
                if (image.getId().equals(importedImage.getId())){
                    exists = true;
                }
            }
            if (!exists){
                imagesToDelete.add(image);
            }
        }
        if (!imagesToDelete.isEmpty()){
            deleteImages(imagesToDelete, mContext);
        }
        for (Imagen importedImage : importedImages){
            exists = false;
            for (Imagen image : images){
                if (importedImage.getId().equals(image.getId())){
                    exists = true;
                    importedImage.setId(image.getId());
                    if (!image.getImg().equals(importedImage.getImg())){
                        image.deleteImage(mContext);
                        importedImage.setDescargada(false);
                        importedImage.update(mContext, true);
                    }else {

                        importedImage.setDescargada(image.getDescargada());
                        importedImage.update(mContext, false);
                    }
                }
            }
            if (!exists){
                importedImage.setDescargada(false);
                importedImage.insert(mContext);
            }
        }
    }

    private void deleteImages(List<Imagen> imagesToDelete, Context mContext) {
        for (Imagen image : imagesToDelete){
            image.deleteImage(mContext);
            image.delete();
        }
    }

    private void deleteImage(Context mContext) {
        Log.d("rmdebug", "Imagen - deleteImage");
        try {
            File file = new File(mContext.getFilesDir()+"/"+ getImg());
            if (!file.delete()){
                Log.e("rmdebug", "Imagen - deleteImage > error delete");
            }
        }catch (Exception e){
            Log.e("rmdebug", "Imagen - deleteImage > error " + e.toString());
            e.printStackTrace();
        }
    }

    private void update(final Context mContext, boolean dowloadImage) {
        Log.d("rmdebug", "Imagen - update");
        ImagenDao imagenDao = MainActivity.getInstance().getDaoSession().getImagenDao();
        if (dowloadImage){
            LoadImage loadImage = new LoadImage(new LoadImage.Listener() {
                @Override
                public void onImageLoaded(Bitmap bitmap) {
                    saveImageToInternalStorage(mContext,
                            getImg(),
                            bitmap);
                }

                @Override
                public void onError() {
                    Log.e("rmdebug", "Imagen - update > onError");
                }
            });
            loadImage.execute(MainActivity.DIR_IMAGES + getImg());
        }
        imagenDao.update(this);
    }

    public void saveImageToInternalStorage (Context context, String nombre, Bitmap imagen){
        Log.d("rmdebug", "Imagen - saveImageToInternalStorage " + nombre);
        try {
            ByteArrayOutputStream stream;
            byte[] byteArray = null;
            int quality = 100;
            boolean optimize = true;
            while (optimize) {
                stream = new ByteArrayOutputStream();
                imagen.compress(Bitmap.CompressFormat.JPEG, quality, stream);
                byteArray = stream.toByteArray();
                Log.d("rmdebug", "lenght " + byteArray.length);
                if (byteArray.length / 1000 <= 300){
                    optimize = false;
                }else {
                    quality = quality - 20;
                }
            }
            FileOutputStream outputStream = context.openFileOutput(nombre, Context.MODE_PRIVATE);
            outputStream.write(byteArray);
            outputStream.close();
            setDescargada(true);
            if (getId() != null) {
                update(context, false);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.e("rmdebug", "Imagen - saveImageToInternalStorage > error " + e.toString());
        } catch (IOException e2) {
            e2.printStackTrace();
            Log.e("rmdebug", "Imagen - saveImageToInternalStorage > error " + e2.toString());
        } catch (Exception e3){
            e3.printStackTrace();
            Log.e("rmdebug", "Imagen - saveImageToInternalStorage > error " + e3.toString());
        }
    }

    private void insert(final Context mContext) {
        Log.d("rmdebug", "Imagen - insert");
        ImagenDao imagenDao = MainActivity.getInstance().getDaoSession().getImagenDao();
        LoadImage loadImage = new LoadImage(new LoadImage.Listener() {
            @Override
            public void onImageLoaded(Bitmap bitmap) {
                saveImageToInternalStorage(mContext,
                        getImg(),
                        bitmap);
            }

            @Override
            public void onError() {
                Log.e("rmdebug", "Imagen - insert > onError");
            }
        });
        Long id = imagenDao.insertOrReplace(this);
        setId(id);
        loadImage.execute(MainActivity.DIR_IMAGES + getImg());
    }

    private void delete() {
        ImagenDao imagenDao = MainActivity.getInstance().getDaoSession().getImagenDao();
        imagenDao.delete(this);
    }
    public boolean getDescargada() {
        return this.descargada;
    }
    public void setDescargada(boolean descargada) {
        this.descargada = descargada;
    }
}
