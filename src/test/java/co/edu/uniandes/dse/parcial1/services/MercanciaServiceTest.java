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
@Import(MercanciaService.class)
public class MercanciaServiceTest {
    
    // TODO: Escriba las pruebas para la clase MercanciaService
    @Autowired
    private MercanciaService mercanciaService;
    @Autowired
	private TestEntityManager entityManager;

	private PodamFactory factory = new PodamFactoryImpl();

    private List<MercanciaEntity> mercanciasList = new ArrayList<>();
    private UbicacionBodegaEntity ubicacionBodegaEntity;


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
		
		ubicacionBodegaEntity = factory.manufacturePojo(UbicacionBodegaEntity.class);
		entityManager.persist(ubicacionBodegaEntity);

		for (int i = 0; i < 3; i++) {
			MercanciaEntity mercanciaEntity = factory.manufacturePojo(MercanciaEntity.class);
			mercanciaEntity.setUbicacion(ubicacionBodegaEntity);
			entityManager.persist(mercanciaEntity);
			mercanciasList.add(mercanciaEntity);
		}
	}

    @Test
    void testCreateMercancia() throws EntityNotFoundException, IllegalOperationException {
        MercanciaEntity newMercancia = new MercanciaEntity();

        newMercancia.setUbicacion(ubicacionBodegaEntity);

        newMercancia.setNombre("Zapatillas Nike");
        newMercancia.setCodigoBarras("101010");
        newMercancia.setFechaRecepcion(LocalDateTime.now().minusMonths(5));
        newMercancia.setCantidadDisponible(800);

        MercanciaEntity result = mercanciaService.crearMercancia(newMercancia);
        assertNotNull(result);
		MercanciaEntity entity = entityManager.find(MercanciaEntity.class, result.getId());

        assertEquals(newMercancia.getNombre(), entity.getNombre());
        assertEquals(newMercancia.getCodigoBarras(), entity.getCodigoBarras());
        assertEquals(newMercancia.getFechaRecepcion(), entity.getFechaRecepcion());
        assertEquals(newMercancia.getCantidadDisponible(), entity.getCantidadDisponible());
    }
    
    @Test
    void testCreateMercanciaInvalid() throws EntityNotFoundException, IllegalOperationException{
        assertThrows(IllegalOperationException.class, () -> {
			MercanciaEntity newEntity = factory.manufacturePojo(MercanciaEntity.class);
			newEntity.setUbicacion(ubicacionBodegaEntity);
			newEntity.setFechaRecepcion(LocalDateTime.now().plusMonths(5));
			mercanciaService.crearMercancia(newEntity);
		});
    }
}
