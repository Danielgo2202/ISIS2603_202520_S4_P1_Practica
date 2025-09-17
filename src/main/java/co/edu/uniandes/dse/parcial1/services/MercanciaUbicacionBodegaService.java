package co.edu.uniandes.dse.parcial1.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.uniandes.dse.parcial1.entities.MercanciaEntity;
import co.edu.uniandes.dse.parcial1.entities.UbicacionBodegaEntity;
import co.edu.uniandes.dse.parcial1.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.parcial1.repositories.MercanciaRepository;
import co.edu.uniandes.dse.parcial1.repositories.UbicacionBodegaRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MercanciaUbicacionBodegaService {

    // TODO: Cree la lógica de asociación entre mercancía y ubicación de bodega
    @Autowired
    private MercanciaRepository mercanciaRepository;

    @Autowired
    private UbicacionBodegaRepository ubicacionBodegaRepository;

    @Transactional
    public MercanciaEntity addMercancia(Long ubicacionBodegaId, Long mercanciaId) throws IllegalOperationException, EntityNotFoundException {
        
        Optional<MercanciaEntity> MercanciaEntityOptional = mercanciaRepository.findById(mercanciaId);
        if (MercanciaEntityOptional.isEmpty()) {
            throw new EntityNotFoundException("La mercancia no existe");
        }
        MercanciaEntity mercanciaEntity = MercanciaEntityOptional.get();

        Optional<UbicacionBodegaEntity> ubicacionBodegaEntityOptional = ubicacionBodegaRepository.findById(ubicacionBodegaId);
        if (ubicacionBodegaEntityOptional.isEmpty()) {
            throw new EntityNotFoundException("La ubicacion no exsiste");
        }
        UbicacionBodegaEntity ubicacionBodegaEntity = ubicacionBodegaEntityOptional.get();

        mercanciaEntity.setUbicacion(ubicacionBodegaEntity);
        ubicacionBodegaEntity.getMercancias().add(mercanciaEntity);

        mercanciaRepository.save(mercanciaEntity);
        ubicacionBodegaRepository.save(ubicacionBodegaEntity);

        return mercanciaEntity;
    }


}
