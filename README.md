# Android Async Factorial Exercise

Modernización de un ejercicio Android de 2018: cálculo exacto de factoriales
sin bloquear el hilo principal.

## Implementación actual

- Prime Swing para calcular la parte impar de `n!` mediante factorización
  prima.
- Restauración de todas las potencias de dos con un único `shiftLeft`.
- Árbol equilibrado de productos `BigInteger`, evitando la multiplicación
  secuencial cada vez más desequilibrada.
- Comprobaciones de cancelación durante la criba, los swings recursivos y los
  productos.
- `ExecutorService` para trabajar fuera del hilo principal.
- `Handler(Looper.getMainLooper())` para publicar el resultado en la interfaz.
- Cancelación al destruir la `Activity`.
- View Binding, validación y estado de carga.

Prime Swing es una familia de algoritmos específicamente diseñada para
factoriales grandes. En esta aplicación se usa una variante monohilo porque el
cálculo completo ya se ejecuta en el executor de background y el límite móvil
de 10.000 no justifica crear más trabajadores.

## Límite defensivo

La interfaz acepta enteros entre **0 y 10.000**. `10000!` tiene 35.660 dígitos;
permitir entradas sin límite en una aplicación móvil facilitaría solicitudes
desproporcionadas en CPU, memoria y renderizado. El límite no procede del
algoritmo ni de `BigInteger`, sino de la responsabilidad de la interfaz.

## Pruebas

Las pruebas unitarias:

- comparan Prime Swing con un producto secuencial independiente de 0 a 1.000;
- verifican el número de dígitos y el prefijo conocido de `10000!`;
- comprueban negativos, exceso del límite y cancelación por interrupción.

```bash
gradle testDebugUnitTest
gradle lintDebug
gradle assembleDebug
```

GitHub Actions ejecuta las tres tareas en cada `push` y `pull_request`.

## Stack

- Java 17
- Android Gradle Plugin 9.3.0
- Gradle 9.5.0
- compileSdk / targetSdk 37
- minSdk 23
- AndroidX AppCompat 1.8.0
- JUnit 4.13.2

El enunciado original permanece en [`ENUNCIADO.md`](ENUNCIADO.md) y la revisión
histórica de la solución con `AsyncTask` en
[`docs/ORIGINAL_REVIEW.md`](docs/ORIGINAL_REVIEW.md).
