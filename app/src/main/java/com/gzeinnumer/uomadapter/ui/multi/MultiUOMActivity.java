package com.gzeinnumer.uomadapter.ui.multi;

import static com.gzeinnumer.uomadapter.helper.GblFunction.clearAllSymbol;
import static com.gzeinnumer.uomadapter.helper.GblFunction.idr;
import static com.gzeinnumer.uomadapter.helper.GblFunction.s;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.util.Log;

import com.gzeinnumer.uomadapter.callback.OnFocusListener;
import com.gzeinnumer.uomadapter.databinding.ActivityMultiUomBinding;
import com.gzeinnumer.uomadapter.databinding.ItemProductBinding;
import com.gzeinnumer.uomadapter.helper.GblFunction;

import java.util.ArrayList;
import java.util.List;

public class MultiUOMActivity extends AppCompatActivity {

    private ActivityMultiUomBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMultiUomBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initAdapter();
    }

    private ProduckAdapter adapter;

    private void initAdapter() {
        List<String> list = new ArrayList<>();
        list.add("");
        list.add("");

        adapter = new ProduckAdapter(list);

        binding.rv.setAdapter(adapter);
        binding.rv.hasFixedSize();
        binding.rv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        adapter.setOnFocusListener(isFocus -> {
            if (isFocus) countNow();
        });
    }

    private void countNow() {
        String[] countAll = new String[adapter.getHolders().size()];
        for (int i = 0; i < adapter.getHolders().size(); i++) {
            ItemProductBinding bind = adapter.getHolders().get(i);
            if (bind!=null){
                String s = bind.tvPricePerProduct.getText().toString();
                if (s.length() > 0) {
                    countAll[i] = clearAllSymbol(s);
                } else {
                    countAll[i] = "0";
                }
            }
        }

        double harga = 0;
        for (String value : countAll) {
            try {
                double current = Double.parseDouble(value);
                harga = Math.round(current + harga);
            } catch (Exception e) {
                Log.e("T_A_G", "countNow: "+e.getMessage());
            }
        }

        binding.tvPriceAllProduct.setText(idr(s(harga)));
    }
}