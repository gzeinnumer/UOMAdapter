package com.gzeinnumer.uomadapter.ui.main;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.gzeinnumer.uomadapter.R;
import com.gzeinnumer.uomadapter.databinding.ActivityMainBinding;
import com.gzeinnumer.uomadapter.databinding.ActivityMultiUomBinding;
import com.gzeinnumer.uomadapter.ui.multi.MultiUOMActivity;
import com.gzeinnumer.uomadapter.ui.single.SingleUOMActivity;

public class MainActivity extends AppCompatActivity {


    private ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initOnClick();
    }

    private void initOnClick() {
        binding.btnSingle.setOnClickListener(view -> {
            startActivity(new Intent(getApplicationContext(), SingleUOMActivity.class));
        });
        binding.btnMulti.setOnClickListener(view -> {
            startActivity(new Intent(getApplicationContext(), MultiUOMActivity.class));
        });
    }
}