package cse2.bhooshan.uberforrepair;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ServiceRequestAdapter extends RecyclerView.Adapter<ServiceRequestAdapter.ViewHolder> {

    private List<PuncherActivity1.ServiceRequest> serviceRequests;
    private LayoutInflater mInflater;

    // data is passed into the constructor
    ServiceRequestAdapter(Context context, List<PuncherActivity1.ServiceRequest> data) {
        this.mInflater = LayoutInflater.from(context);
        this.serviceRequests = data;
    }

    // inflates the row layout from xml when needed
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recyclerview_item, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PuncherActivity1.ServiceRequest request = serviceRequests.get(position);
        holder.myTextView.setText(request.description); // Adjust ID as necessary
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return serviceRequests.size();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView myTextView;

        ViewHolder(View itemView) {
            super(itemView);
            myTextView = itemView.findViewById(R.id.tvServiceRequestDescription); // Adjust ID as necessary
        }
    }
}
