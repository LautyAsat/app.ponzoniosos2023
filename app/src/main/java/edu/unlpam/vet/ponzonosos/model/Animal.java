package edu.unlpam.vet.ponzonosos.model;


import android.util.Log;

import edu.unlpam.vet.ponzonosos.MainActivity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.ToMany;

import java.util.ArrayList;
import java.util.List;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

@Entity
public class Animal{
    @org.greenrobot.greendao.annotation.Id
    private Long Id;
    private String nombre;
    private String nombreSecundario;
    private String nombreC;
    private String tamano;
    private int agresividad;
    private String tipo; // 1-Araña 2-Serpiente 3-Escorpion
    private String efectoPicadura;
    private String antidoto;
    private String lugar_encuentro;
    private String accion_picadura;
    private String confusion;
    private String descripcion;

    @ToMany(referencedJoinProperty = "id_animal")
    private List<Imagen> imagen;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 1746493452)
    private transient AnimalDao myDao;


    @Generated(hash = 308569294)
    public Animal() {
    }

    @Generated(hash = 836175454)
    public Animal(Long Id, String nombre, String nombreSecundario, String nombreC, String tamano,
            int agresividad, String tipo, String efectoPicadura, String antidoto,
            String lugar_encuentro, String accion_picadura, String confusion, String descripcion) {
        this.Id = Id;
        this.nombre = nombre;
        this.nombreSecundario = nombreSecundario;
        this.nombreC = nombreC;
        this.tamano = tamano;
        this.agresividad = agresividad;
        this.tipo = tipo;
        this.efectoPicadura = efectoPicadura;
        this.antidoto = antidoto;
        this.lugar_encuentro = lugar_encuentro;
        this.accion_picadura = accion_picadura;
        this.confusion = confusion;
        this.descripcion = descripcion;
    }

    public Long getId() {
        return this.Id;
    }

    public void setId(Long Id) {
        this.Id = Id;
    }

    public String getNombre() {
        return this.nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNombreSecundario() {
        return this.nombreSecundario;
    }

    public void setNombreSecundario(String nombreSecundario) {
        this.nombreSecundario = nombreSecundario;
    }

    public String getNombreC() {
        return this.nombreC;
    }

    public void setNombreC(String nombreC) {
        this.nombreC = nombreC;
    }

    public String getTamaño() {
        return this.tamano;
    }

    public void setTamaño(String tamaño) {
        this.tamano = tamaño;
    }

    public int getAgresividad() {
        return this.agresividad;
    }

    public void setAgresividad(int agresividad) {
        this.agresividad = agresividad;
    }

    public String getTipo() {
        return this.tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getEfectoPicadura() {
        return this.efectoPicadura;
    }

    public void setEfectoPicadura(String efectoPicadura) {
        this.efectoPicadura = efectoPicadura;
    }

    public String getAntidoto() {
        return this.antidoto;
    }

    public void setAntidoto(String antidoto) {
        this.antidoto = antidoto;
    }

    public String getLugar_encuentro() {
        return this.lugar_encuentro;
    }

    public void setLugar_encuentro(String lugar_encuentro) {
        this.lugar_encuentro = lugar_encuentro;
    }

    public String getAccion_picadura() {
        return this.accion_picadura;
    }

    public void setAccion_picadura(String accion_picadura) {
        this.accion_picadura = accion_picadura;
    }

    public String getConfusion() {
        return this.confusion;
    }

    public void setConfusion(String confusion) {
        this.confusion = confusion;
    }

    public String getDescripcion() {
        return this.descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 2143814383)
    public List<Imagen> getImagen() {
        if (imagen == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            ImagenDao targetDao = daoSession.getImagenDao();
            List<Imagen> imagenNew = targetDao._queryAnimal_Imagen(Id);
            synchronized (this) {
                if (imagen == null) {
                    imagen = imagenNew;
                }
            }
        }
        return imagen;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 560328671)
    public synchronized void resetImagen() {
        imagen = null;
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1486695615)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getAnimalDao() : null;
    }

    public void fillData(JSONObject jsonObject) {
        try {
            setNombre(jsonObject.getString("nombre_principal"));
            setNombreSecundario(jsonObject.getString("nombre_secundario"));
            setNombreC(jsonObject.getString("nombre_c"));
            setId(jsonObject.getLong("id"));
            setAgresividad(jsonObject.getInt("agresividad"));
            setTamaño(jsonObject.getString("tamaño"));
            setTipo(jsonObject.getString("tipo"));
            setEfectoPicadura(jsonObject.getString("efecto_picadura"));
            setAntidoto(jsonObject.getString("antidoto"));
            setLugar_encuentro(jsonObject.getString("lugar_encuentro"));
            setAccion_picadura(jsonObject.getString("accion_picadura"));
            setConfusion(jsonObject.getString("confusion"));
            setDescripcion(jsonObject.getString("descripcion"));
            guardar();
        }catch (Exception e){
            e.printStackTrace();
            Log.e("rmdebug","Animal - fillData > " + e.toString());
        }
    }

    private void guardar() {
        MainActivity.getInstance().getDaoSession().getAnimalDao().insertOrReplace(this);
    }

    public Imagen getPrincipalImage(){
        Imagen img = null;
        for (Imagen imagen : this.getImages()){
            if (imagen.getPrincipal() == 1){
                img = imagen;
            }
        }
        return img;
    }


    public List<Imagen> getImages() {
        return MainActivity.getInstance().getDaoSession().getImagenDao()
                .queryBuilder().where(ImagenDao.Properties.Id_animal.eq(Id)).list();
    }

    public static Animal getAnimal(Long idAnimal) {
        AnimalDao animalDao = MainActivity.getInstance().getDaoSession().getAnimalDao();
        List<Animal> animal = animalDao.queryBuilder()
                .where(AnimalDao.Properties.Id.eq(idAnimal)).list();
        if (!animal.isEmpty()){
            return animal.get(0);
        }
        return null;
    }

    public void abmAnimales(JSONArray responseJSONArray) {
        Log.d("rmdebug", "Animal - abmArboles(JSONArray)");
        List<Animal> importedAnimals = new ArrayList<>();
        for (int i = 0; i < responseJSONArray.length(); i++) {
            Animal animal = new Animal();
            try {
                animal.fillData(responseJSONArray.getJSONObject(i));
                importedAnimals.add(animal);
            } catch (JSONException e) {
                Log.e("rmdebug", "Animal - abmArboles(JSONArray) > error " + e.toString());
                e.printStackTrace();
            }
        }
        abmAnimales(importedAnimals);
    }

    private void abmAnimales(List<Animal> importedAnimals){
        Log.d("rmdebug", "Animal - abmAnimales(List<Arbol>)");
        List<Animal> animals = MainActivity.getInstance().getDaoSession().getAnimalDao()
                .queryBuilder().list();
        List<Animal> animalsToDelete = new ArrayList<>();
        boolean exists;
        for (Animal animal : animals){
            exists = false;
            for (Animal importedAnimal : importedAnimals){
                if (animal.getId().equals(importedAnimal.getId())){
                    exists = true;
                }
            }
            if (!exists){
                animalsToDelete.add(animal);
            }
        }
        if (!animalsToDelete.isEmpty()){
            deleteAnimals(animalsToDelete);
        }
        for (Animal importedAnimal : importedAnimals){
            exists = false;
            for (Animal animal : animals){
                if (importedAnimal.getId().equals(animal.getId())){
                    exists = true;
                    importedAnimal.setId(animal.getId());
                    importedAnimal.updateEntity();
                }
            }
            if (!exists){
                importedAnimal.insertEntity();
            }
        }
    }

    private void deleteAnimals(List<Animal> animalsToDelete) {
        for (Animal animal : animalsToDelete){
            animal.deleteEntity();
        }
    }

    private void updateEntity() {
        AnimalDao animalDao = MainActivity.getInstance().getDaoSession().getAnimalDao();
        animalDao.update(this);
    }

    private void insertEntity() {
        AnimalDao animalDao = MainActivity.getInstance().getDaoSession().getAnimalDao();
        animalDao.insertOrReplace(this);
    }

    private void deleteEntity(){
        AnimalDao animalDao = MainActivity.getInstance().getDaoSession().getAnimalDao();
        animalDao.delete(this);
    }

    public String getTamano() {
        return this.tamano;
    }

    public void setTamano(String tamano) {
        this.tamano = tamano;
    }
}
