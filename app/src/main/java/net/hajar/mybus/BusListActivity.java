package net.hajar.mybus;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.backendless.Backendless;
import com.backendless.persistence.BackendlessDataQuery;

public class BusListActivity extends AppCompatActivity {

    public static final String KEY_QUERY_WHERE_CLAUSE = "QUERY_WHERE_CLUSE";

    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_list);

        String whereClause = getIntent().getStringExtra(KEY_QUERY_WHERE_CLAUSE);

        recyclerView = (RecyclerView)findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new BusListAdapter(
                        this,
                        new BackendlessDataQuery(whereClause),
                        Backendless.Persistence.of(Buses.class)));
    }
}
