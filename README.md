# Reserva de Laboratorios

## Integrantes

- Kendald Gonzalez
- Yeison Aguilar

## Objetivo

Sistema en memoria para administrar reservas de laboratorios. Se parte de un programa funcional con un error intencional y se mejora aplicando programación orientada a objetos, colecciones, excepciones personalizadas, validaciones, los patrones Strategy y Observer, pruebas con JUnit, depuración, Git y Maven.

## Problemas detectados

| Situación detectada | Clase o método | Mejora realizada |
|---|---|---|
| La condición del horario estaba invertida y rechazaba reservas válidas | `ValidacionBasica.validar()` | [PENDIENTE: A] |
| La regla de superposición estaba vacía y permitía reservas en conflicto | `ReservaService.validarSuperposicion()` | [PENDIENTE: A] |
| Imprimir directo por consola habría acoplado el servicio a la salida | `ReservaService.notificarRegistro()` | Se usó Observer: el servicio solo avisa a los observers inscritos (`ConsolaObserver` y `ResumenObserver`) |
| Cada regla nueva obligaba a modificar el método de registro | `ReservaService.registrarReserva()` | Se usó Strategy: cada regla es una clase que implementa `ValidadorReserva` y se agrega con `agregarValidador(...)` |
| No existía validación del horario permitido de los laboratorios | `strategy` (nueva clase `ValidacionHorario`) | Se creó `ValidacionHorario` (7:00 a 21:00) y se probó con `ValidacionHorarioTest` |

## Patrón Strategy

Se usa en el paquete `strategy`. La interfaz `ValidadorReserva` define `validar(Reserva)` y cada regla la implementa en su propia clase: `ValidacionBasica`, `ValidacionCapacidad` y `ValidacionHorario`. `ReservaService` solo recorre la lista de validadores, así que agregar otra regla no obliga a modificarlo. La tercera Strategy, `ValidacionHorario`, valida que la reserva empiece a las 7:00 o después y termine a las 21:00 como máximo.

## Patrón Observer

Se usa en el paquete `observer`. La interfaz `ReservaObserver` define `reservaRegistrada(Reserva)`. `ConsolaObserver` imprime el aviso de reserva creada y `ResumenObserver` muestra laboratorio, fecha, horario y cuántas reservas lleva registradas. `ReservaService` notifica a todos los observers inscritos sin saber qué hace cada uno, por eso no está acoplado a la consola y agregar otra notificación no requiere modificarlo.

## Excepción personalizada

`ReservaInvalidaException` se lanza desde los validadores (`ValidacionBasica`, `ValidacionCapacidad` y `ValidacionHorario`) y desde `ReservaService` (código duplicado y superposición). `App` la captura y muestra el mensaje al usuario.

## Regla de superposición

[PENDIENTE: A] Explicar cómo se resolvió y qué se decidió sobre las reservas `CANCELADA`, con su justificación.

## Pruebas

- `ReservaServiceTest`: `debeAceptarReservaValida`, `debeRechazarCantidadPersonasCero` y las pruebas de superposición [PENDIENTE: A, nombres exactos].
- `ValidacionHorarioTest`: `debeAceptarHorarioDentroDelRango`, `debeAceptarHorarioEnLosLimites`, `debeRechazarInicioAntesDeLas7` y `debeRechazarFinalDespuesDeLas21`.

## Error detectado con depuración

[PENDIENTE: A] Explicar dónde estaba (`ValidacionBasica.validar()`), qué ocurría y cómo se corrigió.

## Ejecución

Requisitos: JDK 25 y Maven. Si usa JDK 21, cambie `maven.compiler.release` a `21` en `pom.xml`.
