package net.hajar.mybus;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.BackendlessDataQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class BusListActivity extends AppCompatActivity {

    public static final String KEY_QUERY_WHERE_CLAUSE = "QUERY_WHERE_CLUSE";
    public static final String KEY_SHOW_CURRENT_AVAILABLE_BUSES = "SHOW_CURRENT_AVAILABLE_BUSES";

    private static final String TAG = "BusListActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_list);

        String whereClause = getIntent().getStringExtra(KEY_QUERY_WHERE_CLAUSE);

        boolean showCurrent = getIntent().getBooleanExtra(KEY_SHOW_CURRENT_AVAILABLE_BUSES,false);

        if(showCurrent){
            Log.d(TAG, "onCreate: Will show ");
            loadAllActiveBuses();
        }else{
            initList(whereClause);
        }


    }
    void initList (String whereClause){
        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.list);
        if(recyclerView == null)return;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new BusListAdapter(
                this,
                new BackendlessDataQuery(whereClause),
                Backendless.Persistence.of(Buses.class)));
    }


    private void doneLoadingAllActiveBuses(List<location> locationList){
        String whereClause = writeWhereClauseFromLocations(locationList);
        initList(whereClause);
    }

    String writeWhereClauseFromLocations(List<location> locationList){
        String whereClause = "busnumber in ";
        whereClause += "(";
        if(locationList.size() == 0){
            whereClause += "''";
        }else {
            for (int i = 0; i < locationList.size(); i++) {
                whereClause += "'" + locationList.get(i).getBus_number() + "'";
                if (i != (locationList.size() - 1)) {
                    whereClause += ",";
                }
            }
        }
        whereClause += ")";
        Log.d(TAG, "writeWhereClauseFromLocations: Where Clause: "+whereClause);
        return whereClause;
    }

    private void loadAllActiveBuses()
    {
        final int PAGE_SIZE = 100;
        final AsyncCallback<BackendlessCollection<location>> callback = new AsyncCallback<BackendlessCollection<location>>()
        {
            private int offset = 0;
            private boolean firstResponse = true;
            private List<location> locationList = new ArrayList<>();
            @Override
            public void handleResponse(BackendlessCollection<location> locationsCollection )
            {
                if( firstResponse )
                {
                    firstResponse = false;
                }
                int size  = locationsCollection.getCurrentPage().size();
                locationList.addAll(locationsCollection.getCurrentPage());
                if( size > 0 )
                {
                    offset+=locationsCollection.getCurrentPage().size();
                    locationsCollection.getPage( PAGE_SIZE, offset, this );
                }
                else
                {
                    doneLoadingAllActiveBuses(locationList);
                }
            }

            @Override
            public void handleFault(BackendlessFault backendlessFault)
            {

            }
        };
        BackendlessDataQuery dataQuery = new BackendlessDataQuery();
        dataQuery.setPageSize(PAGE_SIZE);
        Backendless.Data.of(location.class).find( dataQuery, callback );
    }
}
