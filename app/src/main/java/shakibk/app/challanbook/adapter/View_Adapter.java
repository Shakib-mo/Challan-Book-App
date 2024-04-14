package shakibk.app.challanbook.adapter;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import com.itextpdf.text.DocumentException;
import java.io.IOException;
import java.util.List;
import shakibk.app.challanbook.Invoice;
import shakibk.app.challanbook.MyApplication;
import shakibk.app.challanbook.R;
import shakibk.app.challanbook.Sell_Act;
public class View_Adapter extends RecyclerView.Adapter<View_Adapter.ViewHolder> {

    Activity context;
    List<Invoice> arrayList;
    Invoice invoice = new Invoice();
    private static Clicklistner clicklistner;
    public View_Adapter(Activity co, List<Invoice> arr, Clicklistner cl){
        this.arrayList = arr;
        this.context = co;
        this.clicklistner = cl;
    }
    @NonNull
    @Override
    public View_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.text_view_recy_convert_pdf,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        invoice = arrayList.get(position);
        holder.te_name.setText(invoice.dtoCustomer.getName());
        holder. te_date.setText(arrayList.get(position).getBillData());
        holder.teSrNo.setText(String.valueOf(arrayList.get(position).getBillNo()));
        holder.te_product.setText(String.valueOf(invoice.totalQtyPrice.getTotalItem()));
        holder.te_quantity.setText(String.valueOf(invoice.totalQtyPrice.getTotalQty()));
        holder.te_total_price.setText(String.valueOf(invoice.totalQtyPrice.getTotalPrice()));

        holder.itemView.setOnLongClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(context,v);
            popupMenu.getMenuInflater().inflate(R.menu.menu_delete,popupMenu.getMenu());
            popupMenu.show();
            popupMenu.setOnMenuItemClickListener(item -> {
                int id = item.getItemId();
                if (id==R.id.delete_item_menu){
                    deleteInvoice(position,invoice);
                    notifyDataSetChanged();
                }else if (id ==R.id.edt_item_menu){
                    Intent intent = new Intent(context,Sell_Act.class);
                    context.startActivity(intent);
                }
                return true;
            });
            return true;
        });
    }
    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ConstraintLayout constraintLayout;
        TextView te_name,te_date,te_product,teSrNo,te_quantity,te_total_price;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            te_name = itemView.findViewById(R.id.te_name);
            te_date = itemView.findViewById(R.id.te_date);
            te_product = itemView.findViewById(R.id.te_product);
            teSrNo = itemView.findViewById(R.id.te_searial_number);
            te_quantity = itemView.findViewById(R.id.te_quantity);
            te_total_price = itemView.findViewById(R.id.te_total_price);
            constraintLayout = itemView.findViewById(R.id.con__);
        }

        @Override
        public void onClick(View v) {
            int position = getAbsoluteAdapterPosition();
            try {
                clicklistner.onItemClick(position,v);
            } catch (IOException | DocumentException w) {
            }
        }
    }
    public void deleteInvoice(int position,Invoice in){
        MyApplication.getIntence().deleteInvoiceData(in.getDocID());
        arrayList.remove(position);
        notifyItemChanged(position);
    }

    public interface Clicklistner{
        void onItemClick(int position,View v) throws IOException, DocumentException;
    }
}
