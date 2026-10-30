package cr.ac.una.reservas.service;

import java.util.ArrayList;
import java.util.List;

import cr.ac.una.reservas.exception.ReservaInvalidaException;
import cr.ac.una.reservas.model.Reserva;
import cr.ac.una.reservas.observer.ReservaObserver;
import cr.ac.una.reservas.strategy.ValidadorReserva;
import cr.ac.una.reservas.model.EstadoReserva;

public class ReservaService {

    private final List<Reserva> reservas;
    private final List<ValidadorReserva> validadores;
    private final List<ReservaObserver> observers;

    public ReservaService() {
        reservas = new ArrayList<>();
        validadores = new ArrayList<>();
        observers = new ArrayList<>();
    }

    public void agregarValidador(
            ValidadorReserva validador
    ) {
        validadores.add(validador);
    }

    public void agregarObserver(
            ReservaObserver observer
    ) {
        observers.add(observer);
    }

    public void registrarReserva(
            Reserva reserva
    ) throws ReservaInvalidaException {

        for (ValidadorReserva validador
                : validadores) {

            validador.validar(reserva);
        }

        validarCodigoDuplicado(reserva);
        validarSuperposicion(reserva);

        reservas.add(reserva);
        notificarRegistro(reserva);
    }

    private void validarCodigoDuplicado(
            Reserva nueva
    ) throws ReservaInvalidaException {

        for (Reserva existente : reservas) {

            if (existente.getCodigo()
                    .equalsIgnoreCase(
                            nueva.getCodigo()
                    )) {

                throw new ReservaInvalidaException(
                        "Ya existe una reserva "
                        + "con ese código"
                );
            }
        }
    }

    private void validarSuperposicion(
            Reserva nueva
    ) throws ReservaInvalidaException {

        /*
         * TODO:
         * Implementar la regla que impida
         * reservas superpuestas para el mismo
         * laboratorio y fecha.
         */
        
        for(Reserva existente : reservas){
            
            if(!existente.getLaboratorio().equalsIgnoreCase(nueva.getLaboratorio())){
            
                continue;
            }
            if(!existente.getFecha().equals(nueva.getFecha())){
            
                continue;
            }
            
            if(existente.getEstado() == EstadoReserva.CANCELADA){
            
                continue;
            }
            
            boolean haySuperposicion = nueva.getHoraInicio() < existente.getHoraFinal() && nueva.getHoraFinal() > existente.getHoraInicio();
            
            if(haySuperposicion){
            
                throw new ReservaInvalidaException("El laboratorio ya tiene " + "una reserva en ese horario");
            }
        }
        
        
    }

    private void notificarRegistro(
            Reserva reserva
    ) {

        for (ReservaObserver observer
                : observers) {

            observer.reservaRegistrada(reserva);
        }
    }

    public List<Reserva> listarReservas() {
        return List.copyOf(reservas);
    }
}
