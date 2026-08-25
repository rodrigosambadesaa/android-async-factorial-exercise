# Revisión de la solución original (2018)

## Evaluación

**Valoración para el contexto de 2018: 7/10.** La solución alcanza la idea pedagógica principal: ejecuta un factorial potencialmente costoso fuera del hilo principal mediante `AsyncTask` y usa `BigInteger`, por lo que evita el desbordamiento inmediato de los tipos enteros primitivos.

**Valoración con criterios Android actuales: 3,5/10.** La base matemática sigue siendo válida, pero el patrón de concurrencia y el acoplamiento con la interfaz ya no son recomendables.

## Aciertos

- El cálculo se desplaza fuera del hilo de UI.
- `BigInteger` es una elección adecuada para factoriales.
- Se contemplan explícitamente `0! = 1` y `1! = 1`.
- Se intenta dar feedback al usuario para números negativos.
- El ejercicio está dividido en `MainActivity`, tarea asíncrona e interfaz de callback.

## Problemas detectados

1. `AsyncTask` quedó obsoleto y fue deprecado en Android API 30.
2. `Tarea` conserva una referencia fuerte a `MainActivity`, lo que puede retener una Activity destruida y provocar fugas o callbacks sobre una UI ya inválida.
3. La tarea modifica directamente `mainActivity.resultadoTxt`, por lo que la lógica asíncrona queda acoplada a una vista concreta.
4. Los negativos se representan devolviendo `null` y se detectan capturando `NullPointerException`; el flujo de error debería ser explícito.
5. Una entrada vacía o no numérica produce `NumberFormatException` antes de lanzar la tarea.
6. Hay imports sin usar y una interfaz `Interfaz` que no aporta desacoplamiento real.
7. No hay cancelación ni control del ciclo de vida.
8. No existe un límite defensivo: un factorial desmesurado puede consumir CPU y memoria de forma extrema.
9. Los textos del layout estaban hardcodeados en XML, en vez de `strings.xml`.
10. El proyecto de Drive incluye artefactos generados/locales como `.gradle`, `.idea`, `build/` y `local.properties`, que no deberían versionarse.

## Qué cambia esta versión

- `AsyncTask` se sustituye por `ExecutorService` + `Handler`.
- La operación matemática pasa a `FactorialCalculator`, una clase Java pura y testeable.
- Validación de entrada antes de lanzar trabajo.
- Errores explícitos en lugar de `null` + `NullPointerException`.
- Cancelación de la tarea y limpieza de callbacks en `onDestroy()`.
- Límite de 10.000 para proteger una demo móvil de cálculos desproporcionados.
- View Binding en lugar de `findViewById`.
- Recursos de texto externalizados.
- Pruebas unitarias y GitHub Actions.
- `.gitignore` adecuado para Android Studio/Gradle.
