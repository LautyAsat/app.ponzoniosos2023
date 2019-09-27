package edu.unlpam.vet.ponzonosos.model;

import java.util.ArrayList;
import java.util.List;

public class Agresividad {

    private Long id;
    private String name;

    public static List<Agresividad> getHardcodedElements(){
        List<Agresividad> entities = new ArrayList<>();
        Agresividad high = new Agresividad();
        high.id = 1L;
        high.name = "Alta";
        Agresividad medium = new Agresividad();
        medium.id = 2L;
        medium.name = "Media";
        Agresividad low = new Agresividad();
        low.id = 3L;
        low.name = "Baja";
        entities.add(high);
        entities.add(medium);
        entities.add(low);
        return entities;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
