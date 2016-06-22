package net.hajar.mybus;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by tarekkma on 21/06/16.
 */
public class BusListAdapter extends RecyclerView.Adapter<BusListAdapter.BusVH> {


    @Override
    public BusVH onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BusVH(LayoutInflater.from(context).inflate(R.layout.layout_busitem,null,false));
    }

    @Override
    public void onBindViewHolder(BusVH holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class BusVH extends RecyclerView.ViewHolder{

        public BusVH(View itemView) {
            super(itemView);
        }
    }
}
