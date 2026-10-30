package cr.ac.una.reservas;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import cr.ac.una.reservas.exception.ReservaInvalidaException;
import cr.ac.una.reservas.model.Reserva;
import cr.ac.una.reservas.service.ReservaService;
import cr.ac.una.reservas.strategy.ValidacionBasica;
import cr.ac.una.reservas.strategy.ValidacionCapacidad;
import cr.ac.una.reservas.strategy.ValidacionHorario;

class ReservaServiceTest {

    private ReservaService service;

    @BeforeEach
    void setUp() {

        service = new ReservaService();

        service.agregarValidador(
                new ValidacionBasica()
        );

        service.agregarValidador(
                new ValidacionCapacidad(30)
       
        );
        
        service.agregarValidador( new ValidacionHorario());
    }

    @Test
    void debeAceptarReservaValida()
            throws Exception {

        Reserva reserva =
                new Reserva(
                        "R-001",
                        "Ana",
                        "Lab 1",
                        LocalDate.now(),
                        8,
                        10,
                        20
                );

        service.registrarReserva(reserva);

        assertEquals(
                1,
                service.listarReservas().size()
        );
    }

    @Test
    void debeRechazarCantidadPersonasCero() {

        Reserva reserva =
                new Reserva(
                        "R-002",
                        "Luis",
                        "Lab 1",
                        LocalDate.now(),
                        10,
                        12,
                        0
                );

        assertThrows(
                ReservaInvalidaException.class,
                () -> service.registrarReserva(
                        reserva
                )
        );
    }
    
    @Test
    void debeRechazarReservasSuperpuestas() 
            
            throws Exception {
    
        Reserva primera = new Reserva(
        
        "R-003",
        "Ana",
        "Lab-1",
        LocalDate.now(),
        8,
        10,
        20        
        
        );
    
        Reserva segunda = new Reserva(
        
        
        "R-004",
        "Luis",
        "Lab-1",
        LocalDate.now(),
        9,        
        11,
        15
        
        
        );
        
        
        
        service.registrarReserva(primera);
        
        assertThrows(
        
                ReservaInvalidaException.class, () -> service.registrarReserva(segunda)
        
        );
        
        assertEquals(1, service.listarReservas().size());
    
    }
    
    @Test
    void debeAceptarReservasConsecutivas() 
    
    throws Exception{
    
    Reserva primera = new Reserva(
            "R-005",
            "Ana",
            "Lab 1",
            LocalDate.now(),
            8,
            10,
            20
    );

    Reserva segunda = new Reserva(
            "R-006",
            "Luis",
            "Lab 1",
            LocalDate.now(),
            10,
            12,
            15
    );

    service.registrarReserva(primera);
    service.registrarReserva(segunda);

    assertEquals(2, service.listarReservas().size());
    
    }        

    @Test
    void debeAceptarMimoHorarioEnOtroLaboratorio()
            
     throws Exception{
        
        Reserva primera = new Reserva(
        
        "R-007",
        "Ana",
        "Lab-1",
        LocalDate.now(),
        8,
        10,
        20
        );
        
    
        Reserva segunda = new Reserva(
        
            "R-006",
            "Luis",
            "Lab 1",
            LocalDate.now(),
            10,
            12,
            15
        );
                
            service.registrarReserva(primera);
            service.registrarReserva(segunda);

            assertEquals(2, service.listarReservas().size());
    
    }  
    
    @Test
    void debeRechazarReservaQueComienzaAntesDeOtra()
        throws Exception {

        Reserva primera = new Reserva(
            "R-020",
            "Ana",
            "Lab 1",
            LocalDate.now(),
            9,
            12,
            20
        );

        Reserva segunda = new Reserva(
            "R-021",
            "Luis",
            "Lab 1",
            LocalDate.now(),
            8,
            10,
            15
        );

    service.registrarReserva(primera);

        assertThrows(
            ReservaInvalidaException.class,
            () -> service.registrarReserva(segunda)
        );

        assertEquals(1, service.listarReservas().size());
        
    }
    
    
}
