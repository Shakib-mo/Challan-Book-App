package shakibk.app.challanbook.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

import shakibk.app.challanbook.Invoice;
import shakibk.app.challanbook.R;
public class InvoiceAdapter extends RecyclerView.Adapter<InvoiceAdapter.ViewHolder> {

    Context context;
    List<Invoice> arrayList;
    Invoice invoice = new Invoice();
    public InvoiceAdapter(Context co, List<Invoice> arr){
        this.arrayList = arr;
        this.context = co;
    }
    @NonNull
    @Override
    public InvoiceAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.text_view_recy_convert_pdf,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        invoice = arrayList.get(position);
        holder.te_name.setText(invoice.dtoCustomer.getName());
        holder. te_date.setText(arrayList.get(position).getBillData());
        holder.teSrNo.setText(String.valueOf(arrayList.get(position).getBillNo()));
        holder.te_product.setText(String.valueOf(invoice.totalQtyPrice.getTotalItem()));
        holder.te_quantity.setText(String.valueOf(invoice.totalQtyPrice.getTotalQty()));
        holder.te_total_price.setText(String.valueOf(invoice.totalQtyPrice.getTotalPrice()));
    }
    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        ConstraintLayout constraintLayout;
        TextView te_name, te_date, te_product, teSrNo, te_quantity, te_total_price;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            te_name = itemView.findViewById(R.id.te_name);
            te_date = itemView.findViewById(R.id.te_date);
            te_product = itemView.findViewById(R.id.te_product);
            teSrNo = itemView.findViewById(R.id.te_searial_number);
            te_quantity = itemView.findViewById(R.id.te_quantity);
            te_total_price = itemView.findViewById(R.id.te_total_price);
            constraintLayout = itemView.findViewById(R.id.con__);
        }
    }
}
