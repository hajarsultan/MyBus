package net.hajar.mybus;

import android.Manifest;
import android.content.DialogInterface;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.location.Location;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.BackendlessDataQuery;
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
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class TrackingMapActivity extends FragmentActivity implements
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    public static final String KEY_TRACK_POSTION = "TRACK_POSTION";
    public static final String KEY_BUS_NUMBER = "BUS_NUMBER";

    private String userID = "test";

    private static final String TAG = "TrackingMapActivity";

    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private Location mLastLocation;

    private Location locationCheck;

    private String busNumber;

    private Marker markerBus;

    private location savedLocationToUpdate;

    private TextView distanceTV;
    private Button stopBTN;

    private final int CHECK_DISTANCE = 20;//in meters
    private int distanceInMeters = 0;
    private int distanceChecked = 0;

    private void initViews() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        distanceTV = (TextView)findViewById(R.id.textDistance);
        stopBTN = (Button)findViewById(R.id.stopBtn);

        stopBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkExixtinData();
            }
        });

        checkExixtinData();
        busNumber = getIntent().getStringExtra(KEY_BUS_NUMBER);

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

    void checkExixtinData(){
        BackendlessDataQuery query = new BackendlessDataQuery();
        query.setWhereClause("ownerId = 'test'");
        query.setPageSize(100);
        Backendless.Persistence.of(location.class).find(
                query, new AsyncCallback<BackendlessCollection<location>>() {
                    @Override
                    public void handleResponse(BackendlessCollection<location> locationBackendlessCollection) {
                        for (location loc:locationBackendlessCollection.getData()){
                            loc.removeAsync(new AsyncCallback<Long>() {
                                @Override
                                public void handleResponse(Long aLong) {
                                    Toast.makeText(TrackingMapActivity.this, "DEL", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void handleFault(BackendlessFault backendlessFault) {

                                }
                            });
                        }
                    }

                    @Override
                    public void handleFault(BackendlessFault backendlessFault) {

                    }
                }
        );
    }

    void uploadToServer(Location location){
        if(savedLocationToUpdate == null) {
            savedLocationToUpdate = new location();
            savedLocationToUpdate.setBus_number(busNumber);
            savedLocationToUpdate.setOwnerId(userID);
        }

        savedLocationToUpdate.setLat(location.getLatitude());
        savedLocationToUpdate.setLng(location.getLongitude());

        savedLocationToUpdate.saveAsync(new AsyncCallback<net.hajar.mybus.location>() {
            @Override
            public void handleResponse(location location) {
                Toast.makeText(TrackingMapActivity.this, "UPLD", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void handleFault(BackendlessFault backendlessFault) {

            }
        });
    }


    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(2500);
        mLocationRequest.setFastestInterval(500);
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
                                    TrackingMapActivity.this,
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
        mMap.setPadding(0,0,0,230);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        startLocationUpdates();
        //mMap.setMyLocationEnabled(true);

        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (mLastLocation != null) {
            LatLng currentPos = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            mMap.moveCamera(CameraUpdateFactory.newLatLng(currentPos));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(17));
        }
    }


    protected void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
        checkExixtinData();
        super.onStop();
    }

    @Override
    public void onBackPressed() {
       askB4Close();
    }

    void askB4Close(){
        new AlertDialog.Builder(this)
                .setMessage("هل أنت متأكد من إيقاف تتبع الأتوبيس ؟")
                .setPositiveButton("متأكد", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
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


    String metersToSting(double meters){
        double km = meters/1000;
        if(km > 1){
            return round(km,2)+" كم";
        }else{
            return round(meters,0)+" متر";
        }
    }

    double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    @Override
    public void onLocationChanged(Location location) {
        if(mLastLocation != null){
            distanceInMeters += mLastLocation.distanceTo(location);
            distanceTV.setText("المسافة المقطوعة : "+ metersToSting(distanceInMeters));
        }
        mLastLocation = location;
        if(distanceInMeters - distanceChecked > CHECK_DISTANCE){
            distanceChecked = distanceInMeters;
            uploadToServer(location);
        }



        LatLng currentPos = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
        if(markerBus == null){
            markerBus = mMap.addMarker(new MarkerOptions().position(currentPos).title("You are here !"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(markerBus.getPosition(),17));
        }else{
            animateMarker(markerBus,currentPos,false);
        }
    }

    public void animateMarker(final Marker marker, final LatLng toPosition,
                              final boolean hideMarker) {
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        Projection proj = mMap.getProjection();
        Point startPoint = proj.toScreenLocation(marker.getPosition());
        final LatLng startLatLng = proj.fromScreenLocation(startPoint);
        final long duration = 500;

        final Interpolator interpolator = new LinearInterpolator();

        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;
                float t = interpolator.getInterpolation((float) elapsed
                        / duration);
                double lng = t * toPosition.longitude + (1 - t)
                        * startLatLng.longitude;
                double lat = t * toPosition.latitude + (1 - t)
                        * startLatLng.latitude;
                marker.setPosition(new LatLng(lat, lng));

                if (t < 1.0) {
                    // Post again 16ms later.
                    handler.postDelayed(this, 16);
                } else {
                    if (hideMarker) {
                        marker.setVisible(false);
                    } else {
                        marker.setVisible(true);
                    }
                }
            }
        });
    }
}
