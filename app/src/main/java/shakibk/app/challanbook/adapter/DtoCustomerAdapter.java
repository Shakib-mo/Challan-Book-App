package shakibk.app.challanbook.adapter;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import shakibk.app.challanbook.R;
import shakibk.app.challanbook.Sell_Act;
import shakibk.app.challanbook.object.DtoCustomer;

public class DtoCustomerAdapter extends RecyclerView.Adapter<DtoCustomerAdapter.ViewHolder> {
    List<DtoCustomer> arrayList;
    Context context;
    public DtoCustomerAdapter(Context context, List<DtoCustomer>array) {
        this.context = context;
        this.arrayList = array;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.text_view_contect_xml,parent,false);
        return new ViewHolder(view);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.name.setText(arrayList.get(position).getName());
        holder.mobile.setText(arrayList.get(position).getMobile());
        holder.address.setText(arrayList.get(position).getAddress());
        holder.itemView.setOnClickListener(v -> {
            notifyItemChanged(position);
            notifyDataSetChanged();
            ((Sell_Act)context).te_name.setText(arrayList.get(position).getName());
            ((Sell_Act)context).te_mobile.setText(arrayList.get(position).getMobile());
            ((Sell_Act)context).CoustomerEmail = arrayList.get(position).getEmail();
            ((Sell_Act)context).CoustomerAddress = arrayList.get(position).getAddress();
            ((Sell_Act)context).bottomSheetDialog.cancel();
        });

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name,mobile,address;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.te_name);
            mobile = itemView.findViewById(R.id.te_mobile);
            address = itemView.findViewById(R.id.te_address);
        }
    }
}
