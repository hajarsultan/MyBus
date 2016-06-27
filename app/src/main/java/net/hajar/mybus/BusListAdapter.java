package net.hajar.mybus;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.backendless.BackendlessCollection;
import com.backendless.IDataStore;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.BackendlessDataQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tarekkma on 21/06/16.
 */
public class BusListAdapter extends RecyclerView.Adapter {
    private static final String TAG = "BusListAdapter";
    private static final int BUS_VIEWHOLEDER = 686;
    private static final int LOADMORE_VIEWHOLDER = 801;

    Context context;
    IDataStore<Buses> call = null;
    BackendlessDataQuery query = null;
    List<Buses> busList = new ArrayList<>();

    boolean loading = false;

    private boolean isThereMoreToLoad = true;

    private BackendlessCollection<Buses> currentPage;
    int totalItemsCount = 0;
    int itemsLoadedCount = 0;
    boolean isFirstCalled = true;

    AsyncCallback<BackendlessCollection<Buses>> callback = new AsyncCallback<BackendlessCollection<Buses>>() {
        private static final String pTAG = "AsyncCallback";
        @Override
        public void handleResponse(BackendlessCollection<Buses> busCollection) {
            currentPage = busCollection;
            handlePage();
        }

        @Override
        public void handleFault(BackendlessFault backendlessFault) {

        }
    };

    void handlePage(){
        if(isFirstCalled) {
            totalItemsCount = currentPage.getTotalObjects();
            isFirstCalled = false;
            Log.d(TAG, "handlePage:  Total Items Count : "+ totalItemsCount);
        }

        int size  = currentPage.getCurrentPage().size();
        itemsLoadedCount += size;
        Log.d(TAG,"handlePage: Current Page Size : "+ size);
        Log.d(TAG,"handlePage: Items Loaded Count : "+ itemsLoadedCount);
        int sizeBeforeAddedPage = busList.size();
        busList.addAll(currentPage.getCurrentPage());

        isThereMoreToLoad = (totalItemsCount != itemsLoadedCount);

        loading = false;
        notifyItemRangeInserted(sizeBeforeAddedPage-1,size);//update the added items
        notifyDataSetChanged();

    }

    void callTheNextPage(){
        if(loading)return;
        loading = true;
        notifyDataSetChanged();
        query.setPageSize(10);
        query.setOffset(itemsLoadedCount);
        call.find(query,callback);
    }

    public BusListAdapter(Context context, BackendlessDataQuery query, IDataStore<Buses> call) {
        this.context = context;
        this.query = query;
        this.call = call;
        callTheNextPage();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == LOADMORE_VIEWHOLDER) {
            return new LoadMoreVH(LayoutInflater.from(context).inflate(R.layout.layout_loadmore, parent, false));
        }else{
            return new BusVH(LayoutInflater.from(context).inflate(R.layout.layout_busitem, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder rawHolder, int rawPosition) {
        if(getItemViewType(rawPosition) == LOADMORE_VIEWHOLDER){
            LoadMoreVH holder = (LoadMoreVH) rawHolder;
            holder.bind();
        }else if (getItemViewType(rawPosition) == BUS_VIEWHOLEDER){
            BusVH holder = (BusVH) rawHolder;
            int position = rawPosition;
            holder.bind(busList.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return busList.size() + ((isThereMoreToLoad) ? 1 : 0);
    }

    @Override
    public int getItemViewType(int position) {
        if(position == (busList.size()) && isThereMoreToLoad){
            return LOADMORE_VIEWHOLDER;
        }else{
            return BUS_VIEWHOLEDER;
        }
    }

    class BusVH extends RecyclerView.ViewHolder{
        public TextView busnumberTV,startTV,endTV,lineTV;
        public Button chooseBusBTN,showOnMapBTN;
        public BusVH(View v) {
            super(v);
            busnumberTV = (TextView) v.findViewById(R.id.buslayout_number);
            startTV = (TextView) v.findViewById(R.id.buslayout_start);
            endTV = (TextView) v.findViewById(R.id.buslayout_end);
            lineTV = (TextView) v.findViewById(R.id.buslayout_line);

            chooseBusBTN = (Button) v.findViewById(R.id.buslayout_choose);
            showOnMapBTN = (Button) v.findViewById(R.id.buslayout_show);
        }
        public void bind(final Buses bus){
            busnumberTV.setText(bus.getBusnumber());
            startTV.setText(bus.getStart());
            endTV.setText(bus.getEnd());
            lineTV.setText(bus.getBusline());

            showOnMapBTN.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(context,BusShowMapActivity.class)
                    .putExtra(BusShowMapActivity.KEY_STATIONS,bus.getBusline())
                    .putExtra(BusShowMapActivity.KEY_BUSNUMBER,bus.getBusnumber())
                            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                }
            });

            chooseBusBTN.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(context)
                            .setMessage("هل أنت متأكد من إختيارك لأتوبيس رقم '"+bus.getBusnumber()+"' لركوبة"
                            +"\n الأتوبيس المتوجة من '"+bus.getStart()+"' إلي '"+bus.getEnd()+"' .\n")
                            .setPositiveButton("متأكد", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                    context.startActivity(new Intent(context,TrackingMapActivity.class)
                                            .putExtra(TrackingMapActivity.KEY_BUS_NUMBER,bus.getBusnumber())
                                            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));

                                }
                            })
                            .setNegativeButton("إلغاء", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // do nothing
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
            });
        }
    }
    class LoadMoreVH extends RecyclerView.ViewHolder{

        public ContentLoadingProgressBar progressBar;
        public Button loadmoreBTN;

        public LoadMoreVH(View v) {
            super(v);
            progressBar = (ContentLoadingProgressBar) v.findViewById(R.id.loadmoreProgressbar);
            loadmoreBTN = (Button) v.findViewById(R.id.loadmoreBtn);
        }

        public void bind(){
            loadmoreBTN.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callTheNextPage();
                }
            });
            if (loading){
                loadmoreBTN.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
            }else{
                progressBar.setVisibility(View.GONE);
                loadmoreBTN.setVisibility(View.VISIBLE);
            }
        }
    }
}
