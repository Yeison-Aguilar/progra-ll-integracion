package cr.ac.una.reservas.observer;

import cr.ac.una.reservas.model.Reserva;

public class ResumenObserver
        implements ReservaObserver {

    private int totalRegistradas;

    @Override
    public void reservaRegistrada(
            Reserva reserva
    ) {

        totalRegistradas++;

        System.out.println(
                "[RESUMEN] " + reserva.getLaboratorio()
                + " | " + reserva.getFecha()
                + " | " + reserva.getHoraInicio() + ":00-"
                + reserva.getHoraFinal() + ":00"
                + " | Reservas registradas: "
                + totalRegistradas
        );
    }
}