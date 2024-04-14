package shakibk.app.challanbook.adapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import shakibk.app.challanbook.R;
import shakibk.app.challanbook.object.DtoProduct;

public class DtoProductAdapter extends RecyclerView.Adapter<DtoProductAdapter.VewHolder> {
    Context context;
    List<DtoProduct> arrayList;
    public DtoProductAdapter(Context con, List<DtoProduct> arr){
        this.context = con;
        this.arrayList = arr;
    }
    @NonNull
    @Override
    public DtoProductAdapter.VewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.text_view_add_product_display,parent,false);
        return new VewHolder(view);
    }
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull VewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.te_size.setText(String.valueOf(position+1));
        holder.te_product.setText(arrayList.get(position).getProductNama());
        holder.te_price.setText(String.valueOf(arrayList.get(position).getPrice()));
        holder.te_quantity.setText(String.valueOf(arrayList.get(position).getQuantity()));

        String qt = holder.te_quantity.getText().toString();
        String pr = holder.te_price.getText().toString();
        int qty = Integer.parseInt(qt);
        float preice = Float.parseFloat(pr);
        holder.te_total_price.setText(qty*preice+"");
        DtoProduct dtoProduct = arrayList.get(position);

        dtoProduct.setTotalItemPrice(Float.parseFloat(holder.te_total_price.getText().toString()));
        dtoProduct.setItemNo(Integer.parseInt(holder.te_size.getText().toString()));

        holder.itemView.setOnLongClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(context,v);
            popupMenu.getMenuInflater().inflate(R.menu.menu_delete,popupMenu.getMenu());
            popupMenu.show();
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @SuppressLint({"NonConstantResourceId", "NotifyDataSetChanged"})
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    int id = item.getItemId();
                    if (id==R.id.delete_item_menu){
                        arrayList.remove(position);
                        notifyItemChanged(position);
                        notifyDataSetChanged();
                    }else if (id ==R.id.edt_item_menu){
                        updateProduct(position);
                    }else {
                        Toast.makeText(context, "Try again", Toast.LENGTH_SHORT).show();
                    }
                    return true;
                }
            });
            return true;
        });

    }
    @Override
    public int getItemCount() {
        return arrayList.size();
    }
    public static class VewHolder extends RecyclerView.ViewHolder {
        TextView te_size,te_product,te_price,te_quantity,te_total_price;
        public VewHolder(@NonNull View itemView) {
            super(itemView);
            te_size = itemView.findViewById(R.id.te_size);
            te_product = itemView.findViewById(R.id.te_product);
            te_price = itemView.findViewById(R.id.te_price);
            te_quantity = itemView.findViewById(R.id.te_quantity);
            te_total_price = itemView.findViewById(R.id.te_total_price);
        }
    }
    @SuppressLint("SetTextI18n")
    public void updateProduct(int position){
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.edit_text_add_product);
        TextView addProduct=dialog.findViewById(R.id.addProduct);
        addProduct.setText("Update Product");
        EditText ed_product = dialog.findViewById(R.id.ed_product);
        EditText ed_price = dialog.findViewById(R.id.ed_price);
        EditText ed_quantity = dialog.findViewById(R.id.ed_quantity);
        Button bt_save = dialog.findViewById(R.id.bt_save);
        bt_save.setText("Update");
        Button bt_cancle = dialog.findViewById(R.id.bt_cancle);
        ed_product.setText(arrayList.get(position).getProductNama());
        ed_price.setText(String.valueOf(arrayList.get(position).getPrice()));
        ed_quantity.setText(String.valueOf(arrayList.get(position).getQuantity()));

        bt_cancle.setOnClickListener(v -> dialog.cancel());
        bt_save.setOnClickListener(v -> {
            String product = ed_product.getText().toString().trim();
            String price = ed_price.getText().toString().trim();
            String quanitity = ed_quantity.getText().toString().trim();
            if (product.isEmpty()){
                ed_product.setError("Enter Product");
                ed_product.requestFocus();

            }else if (price.isEmpty()){
                ed_price.setError("Enter Price");
                ed_price.requestFocus();

            }else if (quanitity.isEmpty()){
                ed_quantity.setError("Enter Quantity");
                ed_quantity.requestFocus();
            }else {
                float prices = Float.parseFloat(price);
                int qty = Integer.parseInt(quanitity);
                arrayList.set(position,new DtoProduct(product,prices,qty));
                notifyItemChanged(position);
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}
