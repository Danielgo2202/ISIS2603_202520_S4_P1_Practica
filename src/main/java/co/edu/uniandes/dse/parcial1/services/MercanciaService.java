package co.edu.uniandes.dse.parcial1.services;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.uniandes.dse.parcial1.entities.MercanciaEntity;
import co.edu.uniandes.dse.parcial1.entities.UbicacionBodegaEntity;
import co.edu.uniandes.dse.parcial1.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.parcial1.repositories.MercanciaRepository;
import co.edu.uniandes.dse.parcial1.repositories.UbicacionBodegaRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MercanciaService {
    
    // TODO: Cree la lógica de creación de una mercancía

    @Autowired
    private MercanciaRepository mercanciaRepository;

    @Autowired
    private UbicacionBodegaRepository ubicacionBodegaRepository;

    @Transactional
    public MercanciaEntity crearMercancia(MercanciaEntity mercanciaEntity) throws IllegalOperationException{
        
        if(mercanciaEntity.getCodigoBarras() == null){
            throw new IllegalOperationException("Mercancia no valida");
        }
        if(mercanciaEntity.getFechaRecepcion().isAfter(LocalDateTime.now())){
            throw new IllegalOperationException("Mercancia no valida");
        }
        if(mercanciaEntity.getNombre().isEmpty()){
            throw new IllegalOperationException("Mercancia no valida");
        }

        Optional<UbicacionBodegaEntity> ubicacionBodegaEntity = ubicacionBodegaRepository.findById(mercanciaEntity.getUbicacion().getId());

        if(ubicacionBodegaEntity.isEmpty()){
            throw new IllegalOperationException("Mercancia no valida pq no se obtuvo la ubicacion");
        }
        
        mercanciaEntity.setUbicacion(ubicacionBodegaEntity.get());

        return mercanciaRepository.save(mercanciaEntity);
    }
}
