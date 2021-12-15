package com.gzeinnumer.uomadapter.ui.multi;

import static com.gzeinnumer.uomadapter.helper.GblFunction.idr;
import static com.gzeinnumer.uomadapter.helper.GblFunction.s;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gzeinnumer.stw.SimpleTextWatcher;
import com.gzeinnumer.uomadapter.callback.OnFocusListener;
import com.gzeinnumer.uomadapter.databinding.ItemProductBinding;
import com.gzeinnumer.uomadapter.databinding.ItemUomBinding;
import com.gzeinnumer.uomadapter.model.UOM;
import com.gzeinnumer.uomadapter.ui.single.UOMAdapter;

import java.util.ArrayList;
import java.util.List;

public class ProduckAdapter extends RecyclerView.Adapter<ProduckAdapter.MyHolder> {
    private final List<ItemProductBinding> holders;
    private List<String> list;
    private OnFocusListener onFocusListener;

    public ProduckAdapter(List<String> list) {
        this.list = new ArrayList<>(list);
        this.holders = new ArrayList<>(list.size());
        initHolders();
    }

    public void setOnFocusListener(OnFocusListener onFocusListener) {
        this.onFocusListener = onFocusListener;
    }

    public void setList(List<String> list) {
        this.list = new ArrayList<>(list);
        initHolders();
        notifyDataSetChanged();
    }

    private void initHolders() {
        for (int i = 0; i < list.size(); i++) {
            holders.add(null);
        }
    }

    public List<ItemProductBinding> getHolders() {
        return holders;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyHolder(ItemProductBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        holders.set(position, ItemProductBinding.bind(holder.itemBinding.getRoot()));
        holder.bind(list.get(position), onFocusListener);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        public ItemProductBinding itemBinding;
        private Context context;

        public MyHolder(@NonNull ItemProductBinding itemView) {
            super(itemView.getRoot());
            this.itemBinding = itemView;
            this.context = itemBinding.getRoot().getContext();
        }

        public void bind(String data, OnFocusListener onFocusListener) {
            itemBinding.tvName.setText("Product Name - " + (getAdapterPosition() + 1));
            initAdapter();
            itemBinding.tvPricePerProduct.addTextChangedListener(new SimpleTextWatcher(s -> {
                onFocusListener.isFocus(true);
            }));
        }

        private List<UOM> list;
        private UOMAdapter adapterUom;

        private void initAdapter() {

            int priceCTN = 10000;

            int priceHGR = 5000;

            int pricePCS = 1000;

            list = new ArrayList<>();

//            list.add(new UOM(0, "CTN", priceCTN));
//            list.add(new UOM(1, "HGR", priceHGR));
//            list.add(new UOM(2, "PCS", pricePCS));
            list.add(new UOM(0, "CTN", priceCTN, 10));
            list.add(new UOM(1, "HGR", priceHGR, 10));
            list.add(new UOM(2, "PCS", pricePCS, 10));

            adapterUom = new UOMAdapter(context, list);

            itemBinding.rv.setAdapter(adapterUom);
            itemBinding.rv.setLayoutManager(new LinearLayoutManager(context));
            itemBinding.rv.hasFixedSize();

            adapterUom.setOnItemClickListener(isFocus -> {
                if (isFocus) countNow();
            });

            itemBinding.progressCircular.setVisibility(View.VISIBLE);
            adapterUom.setOnFinishRender(() -> {
                itemBinding.progressCircular.setVisibility(View.GONE);
                if (list.size() > 0) {
                    initLastData(list);
                }
            });
        }

        private void countNow() {
            String[] countAll = new String[adapterUom.getHolders().size()];
            for (int i = 0; i < adapterUom.getHolders().size(); i++) {
                ItemUomBinding bind = adapterUom.getHolders().get(i);
                if (bind!=null){
                    String s = bind.tvQtyXPrice.getText().toString();
                    if (s.length() > 0) {
                        countAll[i] = s;
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
                } catch (Exception e){
                    Log.e("T_A_G", "countNow: "+e.getMessage());
                }
            }

            itemBinding.tvPricePerProduct.setText(idr(s(harga)));
        }

        private void initLastData(List<UOM> list) {
            for (int i = 0; i < adapterUom.getHolders().size(); i++) {
                ItemUomBinding bind = adapterUom.getHolders().get(i);
                if (bind!=null) {
                    UOM current = list.get(i);
                    bind.ed.setText(current.getLastData() + "");
                    int harga = current.getLastData() * current.getPrice();
                    bind.tvQtyXPrice.setText(harga + "");
                }
            }
            itemBinding.progressCircular.setVisibility(View.GONE);
        }
    }
}