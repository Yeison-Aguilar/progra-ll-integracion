
# Reserva de Laboratorios

## Integrantes

- Kendald Gonzalez
- Yeison Aguilar

## Objetivo

Sistema en memoria para administrar reservas de laboratorios. Se parte de un programa funcional con un error intencional y se mejora aplicando programación orientada a objetos, colecciones, excepciones personalizadas, validaciones, los patrones Strategy y Observer, pruebas con JUnit, depuración, Git y Maven.

El sistema permite registrar reservas, validar los datos ingresados, controlar la capacidad de los laboratorios, evitar reservas superpuestas y notificar cuando se registra una nueva reserva.

## Problemas detectados

| Situación detectada | Clase o método | Mejora realizada |
|---|---|---|
| La condición del horario estaba invertida y rechazaba reservas válidas | `ValidacionBasica.validar()` | Se corrigió la condición utilizando `>=` para rechazar únicamente cuando la hora inicial es igual o posterior a la hora final. |
| La regla de superposición estaba vacía y permitía reservas en conflicto | `ReservaService.validarSuperposicion()` | Se implementó una condición para detectar horarios superpuestos en el mismo laboratorio y fecha. |
| Imprimir directamente por consola habría acoplado el servicio a la salida | `ReservaService.notificarRegistro()` | Se utilizó Observer: el servicio solo avisa a los observers inscritos (`ConsolaObserver` y `ResumenObserver`). |
| Cada regla nueva obligaba a modificar el método de registro | `ReservaService.registrarReserva()` | Se utilizó Strategy: cada regla es una clase que implementa `ValidadorReserva` y se agrega con `agregarValidador(...)`. |
| No existía validación del horario permitido de los laboratorios | Paquete `strategy` (nueva clase `ValidacionHorario`) | Se creó `ValidacionHorario` (7:00 a 21:00) y se probó con `ValidacionHorarioTest`. |
| No existía una segunda forma de notificar información sobre las reservas | Paquete `observer` (nueva clase `ResumenObserver`) | Se creó `ResumenObserver`, que muestra el laboratorio, fecha, horario y cantidad de reservas registradas. |

## Patrón Strategy

Se utiliza en el paquete `strategy`. La interfaz `ValidadorReserva` define `validar(Reserva)` y cada regla la implementa en su propia clase: `ValidacionBasica`, `ValidacionCapacidad` y `ValidacionHorario`.

`ReservaService` solo recorre la lista de validadores, así que agregar otra regla no obliga a modificarlo.

La tercera Strategy, `ValidacionHorario`, valida que la reserva empiece a las 7:00 o después y termine a las 21:00 como máximo.

Se utilizó este patrón para separar las reglas de validación, facilitar el mantenimiento del código y permitir agregar nuevas validaciones de manera independiente.

## Patrón Observer

Se utiliza en el paquete `observer`. La interfaz `ReservaObserver` define `reservaRegistrada(Reserva)`.

El sistema cuenta con dos observadores:

- `ConsolaObserver`: imprime el aviso de reserva creada y muestra su código.
- `ResumenObserver`: muestra el laboratorio, fecha, horario y cuántas reservas lleva registradas.

`ReservaService` notifica a todos los observers inscritos sin saber qué hace cada uno. Por eso no está acoplado a la consola y agregar otra notificación no requiere modificarlo.

## Excepción personalizada

`ReservaInvalidaException` se lanza desde los validadores (`ValidacionBasica`, `ValidacionCapacidad` y `ValidacionHorario`) y desde `ReservaService` cuando se detectan códigos duplicados o reservas superpuestas.

También permite comunicar errores relacionados con los datos obligatorios, la cantidad de personas y los horarios permitidos.

`App` captura esta excepción mediante un bloque `try-catch` y muestra el mensaje correspondiente al usuario.

## Regla de superposición

La regla de superposición se implementó en el método `validarSuperposicion()` de la clase `ReservaService`.

Primero se comprueba que las reservas correspondan al mismo laboratorio y a la misma fecha. Si pertenecen a laboratorios diferentes o a fechas distintas, no existe conflicto.

Después se utiliza la siguiente condición para detectar si los horarios se superponen:

```java
boolean haySuperposicion =
        nueva.getHoraInicio() < existente.getHoraFinal()
        && nueva.getHoraFinal() > existente.getHoraInicio();
```

Si ambas condiciones se cumplen, significa que existe un conflicto y se lanza una `ReservaInvalidaException` con el mensaje de que el laboratorio ya tiene una reserva en ese horario.

Se decidió que las reservas con estado `CANCELADA` no bloqueen el horario, porque ya no ocupan el espacio del laboratorio y permiten que otra persona pueda reservarlo.

También se permite registrar reservas consecutivas. Por ejemplo, una reserva de 8:00 a 10:00 y otra de 10:00 a 12:00, ya que una termina exactamente cuando comienza la siguiente.

## Pruebas

Se utilizaron pruebas automatizadas con JUnit 5 para comprobar el funcionamiento del sistema.

### ReservaServiceTest

Se implementaron seis pruebas:

- `debeAceptarReservaValida`: comprueba que se pueda registrar una reserva con datos válidos.
- `debeRechazarCantidadPersonasCero`: comprueba que no se permita registrar una reserva con cero personas.
- `debeRechazarReservasSuperpuestas`: comprueba que se rechacen reservas con horarios superpuestos en el mismo laboratorio y fecha.
- `debeAceptarReservasConsecutivas`: comprueba que se permitan reservas donde una termina exactamente cuando comienza la siguiente.
- `debeAceptarMismoHorarioEnOtroLaboratorio`: comprueba que dos laboratorios diferentes puedan tener reservas en el mismo horario.
- Se incluyen pruebas adicionales de validación de superposición.

### ValidacionHorarioTest

Se implementaron cuatro pruebas:

- `debeAceptarHorarioDentroDelRango`: comprueba que se acepte un horario de 8:00 a 10:00.
- `debeAceptarHorarioEnLosLimites`: comprueba que se acepte un horario de 7:00 a 21:00.
- `debeRechazarInicioAntesDeLas7`: comprueba que se rechace una reserva que comienza antes de las 7:00.
- `debeRechazarFinalDespuesDeLas21`: comprueba que se rechace una reserva que termina después de las 21:00.

En total, se ejecutaron 10 pruebas JUnit, todas con resultados satisfactorios:

```text
Tests run: 10
Failures: 0
Errors: 0
Skipped: 0

BUILD SUCCESS
```

## Error detectado con depuración

El error intencional se encontraba en el método `validar()` de la clase `ValidacionBasica`.

La condición original utilizaba el operador menor que (`<`) para comparar la hora inicial con la hora final.

Esto provocaba que una reserva válida, por ejemplo de 8:00 a 10:00, fuera rechazada porque la condición se cumplía.

Se identificó el problema mediante el depurador, observando los valores de `horaInicio` y `horaFinal` y evaluando la condición durante la ejecución.

La corrección consistió en cambiar el operador menor que (`<`) por mayor o igual que (`>=`), de manera que solamente se rechacen las reservas cuya hora inicial sea igual o posterior a la hora final.

La condición corregida es:

```java
if (reserva.getHoraInicio()
        >= reserva.getHoraFinal()) {

    throw new ReservaInvalidaException(
            "La hora final debe ser posterior "
            + "a la hora inicial"
    );
}
```

Con esta corrección, las reservas con horarios válidos pueden registrarse correctamente.

## Ejecución

### Requisitos

- JDK 25 y Maven.
- Si se utiliza JDK 21, cambiar `maven.compiler.release` a `21` en `pom.xml`.

### Comandos Maven

Limpiar y compilar el proyecto:

```bash
mvn clean compile
```

Ejecutar las pruebas automatizadas:

```bash
mvn test
```

Ejecutar la aplicación:

```bash
mvn exec:java
```

El proyecto utiliza colecciones Java para almacenar las reservas en memoria, por lo que no requiere conexión a una base de datos.
