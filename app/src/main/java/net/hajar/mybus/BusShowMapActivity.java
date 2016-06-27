package net.hajar.mybus;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.BackendlessDataQuery;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class BusShowMapActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final String TAG = "BusShowMapActivity";

    public static final String KEY_STATIONS = "STATIONS";
    public static final String KEY_BUSNUMBER = "BUSNUMBER";


    char[] mistakeArray ={'ي','ى','ا','أ','إ'};
    String [] stationsNameArray = null;
    int hits = 0 ;
    Map<Integer , Station> stations = new HashMap<>();

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_show_map);

        Bundle b = getIntent().getExtras();
        String stationsFromB = b.getString(KEY_STATIONS);
        String busNumber = b.getString(KEY_BUSNUMBER);
        stationsNameArray =  (stationsFromB == null) ? null : stationsFromB.split("-");
        fixStationsName();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        for (int i = 0;i<stationsNameArray.length;i++) {
            loadStation(stationsNameArray[i],i);
        }
    }

    void fixStationsName(){
        for (int i = 0;i<stationsNameArray.length;i++) {
            String unfixedName = "$"+stationsNameArray[i]+"$";
            String fixedName = unfixedName.replace("$ ","").replace(" $","").replace("$","");
            for (char mistakeChar : mistakeArray){
                fixedName = fixedName.replace(mistakeChar,'_');
            }
            if(!unfixedName.equalsIgnoreCase(fixedName)){
                Log.d(TAG, "fixStationsName: Fixed index <"+i+"> '"+stationsNameArray[i]+"' to '"+fixedName+"'");
                stationsNameArray[i] = fixedName;
            }
        }
    }

    void addStationsOnMap(){
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (int i = 0;i<stationsNameArray.length;i++) {
            Station station = stations.get(i);
            if (station != null) {
                LatLng stationLocation = new LatLng(station.getLat(),station.getLng());
                mMap.addMarker(new MarkerOptions().position(stationLocation)
                        .title(station.getStation_name()));
                builder.include(stationLocation);
            }
        }
        LatLngBounds bounds = builder.build();
        int padding = 200; // offset from edges of the map in pixels
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
        mMap.animateCamera(cu);
    }

    void checkToAddStations(){
        Log.d(TAG, "checkToAddStations: Loaded Stations ["+hits+"]");
        if(hits == stationsNameArray.length){
            Log.d(TAG, "checkToAddStations: Finished Loading "+hits+" Stations");
            Log.d(TAG, "checkToAddStations: SUCCESS STATIONS : " + stations.size());
            Log.d(TAG, "checkToAddStations: FAILED STATIONS : " + (stationsNameArray.length-stations.size()));
            addStationsOnMap();
        }

    }

    void loadStation(String stationName, final int index){
        Log.d(TAG, "loadStation: Loading The Station with the name '"+stationName+"'");
        Backendless.Persistence.of(Station.class).find(
                new BackendlessDataQuery("station_name LIKE '" + stationName + "'"),
                new AsyncCallback<BackendlessCollection<Station>>() {
                    @Override
                    public void handleResponse(BackendlessCollection<Station> stationBackendlessCollection) {
                        hits++;
                        if(!stationBackendlessCollection.getData().isEmpty()){
                            Log.d(TAG, "handleResponse: index <"+index+"> SUCCESS CALL");
                            stations.put(index,stationBackendlessCollection.getData().get(0));
                        }else{
                            Log.e(TAG, "handleResponse: index <"+index+"> EMPTY RESPONSE");
                        }
                        checkToAddStations();
                    }

                    @Override
                    public void handleFault(BackendlessFault backendlessFault) {
                        hits++;
                        Log.e(TAG, "handleFault: index <"+index+"> FAILED CALL with the err:"+backendlessFault.getMessage());
                        checkToAddStations();
                    }
                });
    }

    public static LatLng reverseGeocoding(Context context, String locationName){
        if(!Geocoder.isPresent()){
            Log.w("zebia", "Geocoder implementation not present !");
        }
        Geocoder geoCoder = new Geocoder(context, Locale.getDefault());

        try {
            List<Address> addresses = geoCoder.getFromLocationName(locationName, 1);
            int tentatives = 0;
            while (addresses.size()==0 && (tentatives < 10)) {
                addresses = geoCoder.getFromLocationName(locationName, 1);
                tentatives ++;
            }


            if(addresses.size() > 0){
                Log.d("zebia", "reverse Geocoding : locationName " + locationName + "Latitude " + addresses.get(0).getLatitude() );
                return new LatLng(addresses.get(0).getLatitude(), addresses.get(0).getLongitude());
            }else{
                //use http api
            }

        } catch (IOException e) {
            Log.d(BusShowMapActivity.class.getName(), "not possible finding LatLng for Address : " + locationName);
        }
        return null;
    }
}
