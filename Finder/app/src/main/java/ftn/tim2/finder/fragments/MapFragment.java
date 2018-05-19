package ftn.tim2.finder.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ftn.tim2.finder.R;
import ftn.tim2.finder.model.User;

public class MapFragment extends Fragment {

    View v;
    private static final String TAG = "ViewAllUsersActivity";
    private Boolean mLocationPermissionsGranted = false;
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final float DEFAULT_ZOOM = 15f;
    private final int NUMBER_OF_INITIAL_USERS = 5;
    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private DatabaseReference databaseUsers;
    private List<Marker> mMarkers;
    private ArrayList<User> users;
    private User selectedUser;
    private String currentUserId;

    public MapFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databaseUsers = FirebaseDatabase.getInstance().getReference("users");
        mMarkers = new ArrayList<>();
        users = new ArrayList<User>();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_map, container, false);

        getLocationPermission();

        return v;
    }

    private void getLocationPermission() {
        String[] permissions = {FINE_LOCATION, COARSE_LOCATION};
        if(ContextCompat.checkSelfPermission(getContext(), FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if(ContextCompat.checkSelfPermission(getContext(), COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionsGranted = true;
                currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                initMap();
            } else {
                ActivityCompat.requestPermissions(getActivity(), permissions, LOCATION_PERMISSION_REQUEST_CODE);
            }
        } else {
            ActivityCompat.requestPermissions(getActivity(), permissions, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    private void initMap() {
        final SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;
                if(mLocationPermissionsGranted) {
                    getDeviceLocation();
                    if (ContextCompat.checkSelfPermission(getContext(), FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        mMap.setMyLocationEnabled(true);
                    }
                    mMap.getUiSettings().setMyLocationButtonEnabled(false);
                    getUsers();
                    mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                        @Override
                        public boolean onMarkerClick(Marker marker) {
                            marker.showInfoWindow();
                            setSelectedUser(marker.getTitle());
                            return false;
                        }
                    });
                    mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                        @Override
                        public void onInfoWindowClick(Marker marker) {
                            seeProfile();
                        }
                    });
                }
            }
        });
    }

    private void setSelectedUser(String username) {
        for(User u: users) {
            if(u.getUsername().equals(username)) {
                selectedUser = u;
                break;
            }
        }
    }

    private void getDeviceLocation() {
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());
        try{
            if(mLocationPermissionsGranted) {
                Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if(task.isSuccessful()) {
                            Location currentLocation = (Location) task.getResult();
                            if(currentUserId != null) {
                                databaseUsers.child(currentUserId).child("location").child("latitude").setValue(currentLocation.getLatitude());
                                databaseUsers.child(currentUserId).child("location").child("longitude").setValue(currentLocation.getLongitude());
                                Log.d(TAG, currentLocation.getLatitude() + " " + currentLocation.getLongitude());
                                moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), DEFAULT_ZOOM);
                            } else {
                                LatLng defaultLocation = new LatLng(45.2691, 19.8374983);
                                moveCamera(defaultLocation, DEFAULT_ZOOM);
                            }
                        }else {
                            Toast.makeText(getContext(), "unable to get current location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        } catch (SecurityException e){

        }
    }

    private void moveCamera(LatLng latLng, float zoom) {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
    }

    private void addMarker(User user) {
        Log.d(TAG, "addMarkers");
        if(user.getLocation() != null) {
            Marker marker = mMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker_user))
                    .anchor(0.0f, 1.0f) // Anchors the marker on the bottom left
                    .title(user.getUsername())
                    .snippet(user.getEmail())
                    // formula da ih lepo rasporedi po mapi u blizini korisnika koji ima koordinate 19.8335 45.2671
                    //TODO: zameniti koordinatama korisnika
                    .position(new LatLng(user.getLocation().getLatitude(), user.getLocation().getLongitude())));
            mMarkers.add(marker);
        }
    }

    private void moveMarker(String username, LatLng newPosition) {
        for(Marker marker: mMarkers) {
            if(marker.getTitle().equals(username)) {
                marker.setPosition(newPosition);
            }
        }
    }

    private void removeMarker(String username) {
        Log.d(TAG, username);
        Marker markerToRemove = null;
        for(Marker marker: mMarkers) {
            if(marker.getTitle().equals(username)) {
                markerToRemove = marker;
                break;
            }
        }
        markerToRemove.setVisible(false);
        if(markerToRemove != null) {
            mMarkers.remove(markerToRemove);
        }
    }

    private void getUsers() {
        databaseUsers.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.d(TAG, dataSnapshot.getValue().toString());
                User user = dataSnapshot.getValue(User.class);
                addUser(user);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                //ako je promenjen ulogovan korisnik pomeri kameru
                //ako je promenjen neko drugi pomeri marker na novu lokaciju
                User user = dataSnapshot.getValue(User.class);
                if(user.getId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                    moveCamera(new LatLng(user.getLocation().getLatitude(), user.getLocation().getLongitude()), DEFAULT_ZOOM);
                } else {
                    moveMarker(user.getUsername(), new LatLng(user.getLocation().getLatitude(), user.getLocation().getLongitude()));
                }

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                removeMarker(user.getUsername());
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void addUser(User user) {
        for(User u: users) {
            if(u.getEmail().equals(user.getEmail()) || FirebaseAuth.getInstance().getCurrentUser().getUid().equals(user.getId())) {
                return;
            }
        }
        users.add(user);
        addMarker(user);
    }


    private void seeProfile(){
        ProfileDetailsFragment profileDetailsFragment = new ProfileDetailsFragment();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        Bundle bundle = new Bundle();
        bundle.putString("user_ID", selectedUser.getId());
        profileDetailsFragment.setArguments(bundle);
        transaction.replace(R.id.content_frame, profileDetailsFragment);
        transaction.commit();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mLocationPermissionsGranted = false;
        switch(requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE: {
                if(grantResults.length > 0) {
                    for(int i = 0; i < grantResults.length; i++) {
                        if(grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            mLocationPermissionsGranted = false;
                            return;
                        }
                    }
                    mLocationPermissionsGranted = true;
                    //initialize map
                    initMap();
                }
            }
        }
    }
}
