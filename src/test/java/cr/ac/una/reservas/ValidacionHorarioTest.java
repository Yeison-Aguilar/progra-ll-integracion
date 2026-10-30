package cr.ac.una.reservas;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import cr.ac.una.reservas.exception.ReservaInvalidaException;
import cr.ac.una.reservas.model.Reserva;
import cr.ac.una.reservas.strategy.ValidacionHorario;

class ValidacionHorarioTest {

    private ValidacionHorario validador;

    @BeforeEach
    void setUp() {
        validador = new ValidacionHorario();
    }

    @Test
    void debeAceptarHorarioDentroDelRango()
            throws Exception {

        Reserva reserva = new Reserva(
                "R-010", "Ana", "Lab 1",
                LocalDate.now(), 8, 10, 20
        );

        assertDoesNotThrow(
                () -> validador.validar(reserva)
        );
    }

    @Test
    void debeAceptarHorarioEnLosLimites() {

        Reserva reserva = new Reserva(
                "R-011", "Ana", "Lab 1",
                LocalDate.now(), 7, 21, 20
        );

        assertDoesNotThrow(
                () -> validador.validar(reserva)
        );
    }

    @Test
    void debeRechazarInicioAntesDeLas7() {

        Reserva reserva = new Reserva(
                "R-012", "Luis", "Lab 1",
                LocalDate.now(), 6, 8, 20
        );

        assertThrows(
                ReservaInvalidaException.class,
                () -> validador.validar(reserva)
        );
    }

    @Test
    void debeRechazarFinalDespuesDeLas21() {

        Reserva reserva = new Reserva(
                "R-013", "Luis", "Lab 1",
                LocalDate.now(), 20, 22, 20
        );

        assertThrows(
                ReservaInvalidaException.class,
                () -> validador.validar(reserva)
        );
    }
}