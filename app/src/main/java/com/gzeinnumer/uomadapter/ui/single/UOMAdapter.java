package com.gzeinnumer.uomadapter.ui.single;

import static com.gzeinnumer.uomadapter.helper.GblFunction.clearAllSymbol;
import static com.gzeinnumer.uomadapter.helper.GblFunction.s;
import static com.gzeinnumer.uomadapter.helper.GblFunction.saparator;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gzeinnumer.stw.SimpleTextWatcher;
import com.gzeinnumer.uomadapter.callback.OnFinishRender;
import com.gzeinnumer.uomadapter.callback.OnFocusListener;
import com.gzeinnumer.uomadapter.model.UOM;
import com.gzeinnumer.uomadapter.databinding.ItemUomBinding;

import java.util.ArrayList;
import java.util.List;

public class UOMAdapter extends RecyclerView.Adapter<UOMAdapter.MyHolder> {

    private final List<ItemUomBinding> holders;
    private final Context context;
    private List<UOM> list;
    private OnFocusListener onItemClickListener;
    private OnFinishRender onFinishRender;

    public UOMAdapter(Context context, List<UOM> list) {
        this.context = context;
        this.list = new ArrayList<>(list);
        this.holders = new ArrayList<>(list.size());
        initHolders();
    }

    public void setOnItemClickListener(OnFocusListener onFocusListener) {
        this.onItemClickListener = onFocusListener;
    }

    public void setOnFinishRender(OnFinishRender onFinishRender) {
        this.onFinishRender = onFinishRender;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setList(List<UOM> list) {
        this.list = new ArrayList<>(list);
        initHolders();
        notifyDataSetChanged();
    }

    private void initHolders() {
        for (int i = 0; i < list.size(); i++) {
            holders.add(null);
        }
    }

    public List<ItemUomBinding> getHolders() {
        return holders;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyHolder(ItemUomBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        holders.set(position, ItemUomBinding.bind(holder.itemBinding.getRoot()));
        holder.bind(list.get(position), context, onItemClickListener, onFinishRender, list.size());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        public ItemUomBinding itemBinding;

        private int minteger = 0;

        public MyHolder(@NonNull ItemUomBinding itemView) {
            super(itemView.getRoot());
            itemBinding = itemView;
        }

        public void bind(UOM data, Context context, OnFocusListener onFocusListener, OnFinishRender onFinishRender, int size) {
            itemBinding.tvHarga.setText(String.valueOf(data.getPrice()));
            itemBinding.tvUom.setText(data.getName());

            itemBinding.increase.setOnClickListener(view -> {
                increaseInteger();
            });

            itemBinding.decrease.setOnClickListener(view -> {
                decreaseInteger();
            });

            itemBinding.ed.setOnFocusChangeListener((view, hasFocus) -> {
                if (itemBinding.ed.getText().toString().length() == 0) {
                    itemBinding.ed.setText("0");
                }
            });

            itemBinding.ed.addTextChangedListener(new SimpleTextWatcher(s -> {
                double hargaXCount = 0;
                if (s.length() > 1 && s.toString().charAt(0) == '0') {
                    String newText = s.toString().substring(1);
                    itemBinding.ed.setText(newText);
                    itemBinding.ed.setSelection(newText.length());
                } else if (s.toString().length() > 0) {
                    double hargaItem = Double.parseDouble(itemBinding.tvHarga.getText().toString());
                    double qty = Double.parseDouble(s.toString());
                    double subtotal = (hargaItem * qty);
                    hargaXCount = Math.round(subtotal + hargaXCount);
                    itemBinding.tvQtyXPrice.setText(clearAllSymbol(saparator(s(hargaXCount))));
                } else {
                    itemBinding.tvQtyXPrice.setText("" + hargaXCount * 0);
                }
                onFocusListener.isFocus(true);
            }));

            itemBinding.ed.setText(data.getLastData()+"");

            if (onFinishRender != null) {
                if (getAdapterPosition() == size - 1) {
                    onFinishRender.onFinish();
                }
            }
        }

        public void increaseInteger() {
            if (itemBinding.ed.getText().toString().length() == 0) {
                minteger = 0;
            } else {
                minteger = Integer.parseInt(itemBinding.ed.getText().toString());
            }
            minteger = minteger + 1;
            display();
        }

        public void decreaseInteger() {
            if (itemBinding.ed.getText().toString().length() == 0) {
                minteger = 0;
            } else {
                minteger = Integer.parseInt(itemBinding.ed.getText().toString());
            }
            if (minteger > 0) {
                minteger = minteger - 1;
                display();
            }
        }

        private void display() {
            itemBinding.ed.setText("" + minteger);
        }
    }
}