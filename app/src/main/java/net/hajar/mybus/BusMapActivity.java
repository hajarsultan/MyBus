package net.hajar.mybus;

import android.Manifest;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class BusMapActivity extends FragmentActivity implements
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {


    private static final String KEY_TRACK_POSTION = "TRACK_POSTION";
    private static final String KEY_BUS_NUMBER = "BUS_NUMBER";

    private String unserID = "test";

    private static final String TAG = "BusMapActivity";

    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private Location mLastLocation;
    private Location savedLocation;
    private MarkerOptions usersCurrentLocation;
    private boolean mRequestingLocationUpdates = false;

    private TextView currentLocationTV, savedLocationTV, savedDistanceTV;
    private Button saveBTN,showBusesBTN,selectBusesBTN;
    private EditText busSearchET;

    private void initViews() {
        showBusesBTN = (Button) findViewById(R.id.showBuses);

        selectBusesBTN = (Button) findViewById(R.id.selectBuses);

        busSearchET = (EditText) findViewById(R.id.bus_search);

        busSearchET.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String whereClause = "busline LIKE '%"+busSearchET.getText().toString()+"%'";
                    startActivity(new Intent(BusMapActivity.this,BusListActivity.class)
                            .putExtra(BusListActivity.KEY_QUERY_WHERE_CLAUSE,whereClause));
                    handled = true;
                }
                return handled;
            }
        });

        selectBusesBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogHelper.createInputDialog(BusMapActivity.this, "أدخل رقم الأوتوبيس للبحث عنة", new DialogHelper.OnInputDialogOK() {
                    @Override
                    public void onOK(String s) {
                        if(s.isEmpty()){
                            //Err its empty string
                        }else{
                            //do your magic

                            String whereClause = "busnumber LIKE '%"+s+"%'";
                            startActivity(new Intent(BusMapActivity.this,BusListActivity.class)
                                    .putExtra(BusListActivity.KEY_QUERY_WHERE_CLAUSE,whereClause));

                        }
                    }
                });
            }
        });

        showBusesBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BusMapActivity.this,BusListActivity.class)
                        .putExtra(BusListActivity.KEY_SHOW_CURRENT_AVAILABLE_BUSES,true));
            }
        });
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        initViews();

        // Create an instance of GoogleAPIClient.
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        createLocationRequest();

    }


    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);

        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient,
                        builder.build());

        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {
                final Status status = locationSettingsResult.getStatus();
                //final LocationSettingsStates = locationSettingsResult.getLocationSettingsStates();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        // All location settings are satisfied. The client can
                        // initialize location requests here.
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied, but this can be fixed
                        // by showing the user a dialog.
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult(
                                    BusMapActivity.this,
                                    565);
                        } catch (IntentSender.SendIntentException e) {
                            // Ignore the error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way
                        // to fix the settings so we won't show the dialog.
                        break;
                }
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setPadding(0,170,0,230);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        //startLocationUpdates();
        mMap.setMyLocationEnabled(true);

        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (mLastLocation != null) {
            LatLng currentPos = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            mMap.moveCamera(CameraUpdateFactory.newLatLng(currentPos));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(17));
        }
    }


    protected void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
    }


    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }


    @Override
    public void onLocationChanged(Location location) {
        boolean isMarkerAdded = true;
        mLastLocation = location;
        LatLng currentPos = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
        if(usersCurrentLocation == null)isMarkerAdded = false;
        //usersCurrentLocation = new MarkerOptions().position(currentPos).title("You are here !").icon(BitmapDescriptorFactory.fromResource(R.drawable.street_view));
        if(!isMarkerAdded){
            //mMap.addMarker(usersCurrentLocation);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(currentPos));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(17));
        }
    }
}
