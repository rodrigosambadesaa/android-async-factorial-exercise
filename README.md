# Android Async Factorial Exercise

Modernización de un ejercicio Android resuelto originalmente en 2018: cálculo de factoriales con trabajo en segundo plano.

## Objetivo

Mostrar de forma pequeña y verificable cómo ejecutar una operación de CPU fuera del hilo principal sin conservar referencias inseguras a una `Activity`.

La solución original empleaba `AsyncTask`, que era coherente con el temario y las APIs de la época. Esta revisión mantiene **Java** y el mismo objetivo pedagógico, pero sustituye el patrón obsoleto por concurrencia estándar de Java.

## Mejoras principales

- `ExecutorService` para el cálculo en background.
- `Handler(Looper.getMainLooper())` para publicar el resultado en UI.
- Cancelación al destruir la `Activity`.
- `FactorialCalculator` independiente de Android y cubierto con pruebas.
- `BigInteger` para resultados arbitrariamente grandes dentro del límite de la demo.
- Validación de vacío, formato, negativos y valores excesivos.
- View Binding.
- Estado de carga y bloqueo temporal de controles para impedir tareas duplicadas.
- Textos en recursos Android.
- AndroidX actual y configuración de compilación moderna.
- GitHub Actions para tests, lint y build.

## Límites deliberados

La aplicación admite valores entre **0 y 10.000**. Matemáticamente el factorial existe para enteros no negativos mayores, pero una demo móvil que aceptase entradas arbitrarias permitiría solicitar cálculos y representaciones decimales desproporcionados en CPU y memoria.

## Stack

- Java 17
- Android Gradle Plugin 9.3.0
- Gradle 9.5.0
- compileSdk / targetSdk 37
- minSdk 23
- AndroidX AppCompat 1.8.0
- JUnit 4.13.2

## Comandos

```bash
gradle testDebugUnitTest
gradle lintDebug
gradle assembleDebug
```

El workflow de CI instala explícitamente Gradle 9.5.0, de modo que el repositorio no depende de un wrapper binario heredado. Si se desea añadir el wrapper estándar al clonar el proyecto, puede generarse con `gradle wrapper --gradle-version 9.5.0`.

## Origen y revisión

El proyecto parte del ejercicio `Ejercicio2AsyncTask` conservado en la carpeta académica de Google Drive. La evaluación técnica detallada de aquella solución y la motivación de cada cambio están en [`docs/ORIGINAL_REVIEW.md`](docs/ORIGINAL_REVIEW.md).

## Conectividad

Este ejercicio **no usa red**, por lo que no incorpora ninguna utilidad de conectividad. Los ejercicios Android que sí necesiten conectividad deben integrar el Gist indicado para esa finalidad, en lugar de añadir comprobaciones artificiales aquí.
