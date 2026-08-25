package com.example.rodri.ejercicio2asynctask;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.rodri.ejercicio2asynctask.databinding.ActivityMainBinding;

import java.math.BigInteger;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MainActivity extends AppCompatActivity {

    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    private ActivityMainBinding binding;
    private Future<?> currentTask;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.calculateButton.setOnClickListener(view -> calculateFactorial());
    }

    private void calculateFactorial() {
        if (binding == null) {
            return;
        }

        binding.numberInput.setError(null);
        String rawInput = binding.numberInput.getText().toString().trim();

        if (rawInput.isEmpty()) {
            binding.numberInput.setError(getString(R.string.error_empty));
            binding.numberInput.requestFocus();
            return;
        }

        final int number;
        try {
            number = Integer.parseInt(rawInput);
        } catch (NumberFormatException exception) {
            binding.numberInput.setError(getString(R.string.error_invalid));
            binding.numberInput.requestFocus();
            return;
        }

        if (number < 0) {
            binding.numberInput.setError(getString(R.string.error_negative));
            binding.numberInput.requestFocus();
            return;
        }

        if (number > FactorialCalculator.MAX_INPUT) {
            binding.numberInput.setError(getString(R.string.error_too_large));
            binding.numberInput.requestFocus();
            return;
        }

        setBusy(true);

        currentTask = executor.submit(() -> {
            try {
                BigInteger factorial = FactorialCalculator.factorial(number);
                String formattedResult = factorial.toString();
                mainHandler.post(() -> showResult(formattedResult));
            } catch (CancellationException ignored) {
                // Expected when the Activity is destroyed while work is still running.
            } catch (RuntimeException exception) {
                mainHandler.post(this::showCalculationError);
            }
        });
    }

    private void showResult(String result) {
        if (!canUpdateUi()) {
            return;
        }
        binding.resultText.setText(result);
        setBusy(false);
    }

    private void showCalculationError() {
        if (!canUpdateUi()) {
            return;
        }
        binding.resultText.setText(R.string.error_calculation);
        setBusy(false);
    }

    private void setBusy(boolean busy) {
        if (binding == null) {
            return;
        }
        binding.calculateButton.setEnabled(!busy);
        binding.numberInput.setEnabled(!busy);
        binding.calculateButton.setText(busy ? R.string.calculating : R.string.calculate);
        binding.progress.setVisibility(busy ? View.VISIBLE : View.GONE);
    }

    private boolean canUpdateUi() {
        return binding != null && !isFinishing() && !isDestroyed();
    }

    @Override
    protected void onDestroy() {
        if (currentTask != null) {
            currentTask.cancel(true);
            currentTask = null;
        }
        executor.shutdownNow();
        mainHandler.removeCallbacksAndMessages(null);
        binding = null;
        super.onDestroy();
    }
}
