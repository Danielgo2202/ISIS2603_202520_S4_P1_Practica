package co.edu.uniandes.dse.parcial1.entities;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Data
@Entity
public class UbicacionBodegaEntity extends BaseEntity{
   
    private int numeroEstante;
    private String canastaMercancia;
    private float pesoMaximo;

    @OneToMany(mappedBy = "ubicacion")
    private List<MercanciaEntity> mercancias = new ArrayList<>();
}
