package com.gzeinnumer.uomadapter.ui.single;

import static com.gzeinnumer.uomadapter.helper.GblFunction.idr;
import static com.gzeinnumer.uomadapter.helper.GblFunction.s;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.gzeinnumer.uomadapter.model.UOM;
import com.gzeinnumer.uomadapter.databinding.ActivitySingleUomBinding;
import com.gzeinnumer.uomadapter.databinding.ItemUomBinding;

import java.util.ArrayList;
import java.util.List;

public class SingleUOMActivity extends AppCompatActivity {

    private ActivitySingleUomBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySingleUomBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initAdapter();
    }

    private List<UOM> list = new ArrayList<>();
    private UOMAdapter adapter;

    private void initAdapter() {

        int priceCTN = 16000;
        int priceHGR = 8000;
        int pricePCS = 1000;

        list = new ArrayList<>();

//        list.add(new UOM(0, "CTN", priceCTN));
//        list.add(new UOM(1, "HGR", priceHGR));
//        list.add(new UOM(2, "PCS", pricePCS));
        list.add(new UOM(0, "CTN", priceCTN, 10));
        list.add(new UOM(1, "HGR", priceHGR, 10));
        list.add(new UOM(2, "PCS", pricePCS, 10));

        adapter = new UOMAdapter(getApplicationContext(), list);

        binding.rv.setAdapter(adapter);
        binding.rv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        binding.rv.hasFixedSize();

        adapter.setOnItemClickListener(isFocus -> {
            if (isFocus) countNow();
        });
        binding.progressCircular.setVisibility(View.VISIBLE);
        adapter.setOnFinishRender(() -> {
            binding.progressCircular.setVisibility(View.GONE);
            if (list.size() > 0) {
                initLastData(list);
            }
        });
    }

    private void countNow() {
        String[] countAll = new String[adapter.getHolders().size()];
        for (int i = 0; i < adapter.getHolders().size(); i++) {
            ItemUomBinding bind = adapter.getHolders().get(i);
            String s = bind.tvQtyXPrice.getText().toString();
            if (s.length() > 0) {
                countAll[i] = s;
            } else {
                countAll[i] = "0";
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

        binding.tvPricePerProduct.setText(idr(s(harga)));
    }

    private void initLastData(List<UOM> list) {
        for (int i = 0; i < adapter.getHolders().size(); i++) {
            ItemUomBinding itemUomBinding = adapter.getHolders().get(i);
            UOM current = list.get(i);
            itemUomBinding.ed.setText(current.getLastData() + "");
            int harga = current.getLastData() * current.getPrice();
            itemUomBinding.tvQtyXPrice.setText(harga + "");
        }
        binding.progressCircular.setVisibility(View.GONE);
    }
}