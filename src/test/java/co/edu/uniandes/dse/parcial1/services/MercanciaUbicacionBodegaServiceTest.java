package co.edu.uniandes.dse.parcial1.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import co.edu.uniandes.dse.parcial1.entities.MercanciaEntity;
import co.edu.uniandes.dse.parcial1.entities.UbicacionBodegaEntity;
import co.edu.uniandes.dse.parcial1.exceptions.IllegalOperationException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@DataJpaTest
@Transactional
@Import({MercanciaService.class, UbicacionBodegaService.class, MercanciaUbicacionBodegaService.class})
public class MercanciaUbicacionBodegaServiceTest {
    
    // TODO: Escriba las pruebas para la clase MercanciaService
    @Autowired
    private MercanciaUbicacionBodegaService mercanciaUbicacionBodegaService;
    @Autowired
	private TestEntityManager entityManager;

	private PodamFactory factory = new PodamFactoryImpl();

    private List<MercanciaEntity> mercanciasList = new ArrayList<>();
    private List<UbicacionBodegaEntity> ubicacionesList = new ArrayList<>();


    @BeforeEach
	void setUp() {
		clearData();
		insertData();
	}

	private void clearData() {
		entityManager.getEntityManager().createQuery("delete from MercanciaEntity").executeUpdate();
		entityManager.getEntityManager().createQuery("delete from UbicacionBodegaEntity").executeUpdate();
	}

	private void insertData() {
		
		for (int i = 0; i < 3; i++) {
			MercanciaEntity mercanciaEntity = factory.manufacturePojo(MercanciaEntity.class);
			entityManager.persist(mercanciaEntity);
			mercanciasList.add(mercanciaEntity);
		}

        for (int i = 0; i < 3; i++) {
			UbicacionBodegaEntity ubicacionBodegaEntity = factory.manufacturePojo(UbicacionBodegaEntity.class);
			entityManager.persist(ubicacionBodegaEntity);
			ubicacionesList.add(ubicacionBodegaEntity);

            mercanciasList.get(i).setUbicacion(ubicacionBodegaEntity);
            ubicacionBodegaEntity.getMercancias().add(mercanciasList.get(i));
		}
	}

    @Test
    void testAddMercancia() throws IllegalOperationException, EntityNotFoundException{
        UbicacionBodegaEntity ubicacionBodegaEntity = ubicacionesList.get(0);
        MercanciaEntity mercanciaEntity = mercanciasList.get(1);

        MercanciaEntity response = mercanciaUbicacionBodegaService.addMercancia(ubicacionBodegaEntity.getId(), mercanciaEntity.getId());

        assertNotNull(response);
        assertEquals(mercanciaEntity.getId(), response.getId());
    }

	
}
