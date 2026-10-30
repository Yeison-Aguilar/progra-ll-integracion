package cr.ac.una.reservas.strategy;

import cr.ac.una.reservas.exception.ReservaInvalidaException;
import cr.ac.una.reservas.model.Reserva;

public class ValidacionHorario
        implements ValidadorReserva {

    private static final int HORA_MINIMA = 7;
    private static final int HORA_MAXIMA = 21;

    @Override
    public void validar(Reserva reserva)
            throws ReservaInvalidaException {

        if (reserva.getHoraInicio() < HORA_MINIMA
                || reserva.getHoraFinal() > HORA_MAXIMA) {

            throw new ReservaInvalidaException(
                    "El horario permitido es entre las "
                    + HORA_MINIMA + ":00 y las "
                    + HORA_MAXIMA + ":00"
            );
        }
    }
}