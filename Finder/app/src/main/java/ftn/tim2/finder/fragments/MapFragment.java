package ftn.tim2.finder.fragments;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
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
    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private DatabaseReference databaseUsers;
    private List<Marker> mMarkers;
    private ArrayList<User> users;
    private User selectedUser;
    private String currentUserId;
    private ImageView filter_btn;
    private Dialog filterPopup;
    private Button filterSubmitBtn;
    private RadioGroup radioGroup;
    private ArrayList<Marker> mFollowing;
    private ArrayList<Marker> mFollowers;
    private TextView filterCloseBtn;
    private Location currentLocation;
    private Location currentLocation1;
    private ChildEventListener childEventListener;

    public MapFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databaseUsers = FirebaseDatabase.getInstance().getReference("users");
        mMarkers = new ArrayList<>();
        users = new ArrayList<User>();
        filterPopup = new Dialog(getContext());
        mFollowers = new ArrayList<>();
        mFollowing = new ArrayList<>();
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

                }
            }
        });
        final SearchView searchInput = v.findViewById(R.id.search_field);
        searchInput.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                findUser(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        ///////////////////////////////////////////////////////////////////////////
        filter_btn = v.findViewById(R.id.filter_btn);
        filter_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFilterPopUp();
            }
        });
    }

    private void showFilterPopUp() {
        filterPopup.setContentView(R.layout.filter_popup);
        filterPopup.show();

        filterSubmitBtn = filterPopup.findViewById(R.id.filter_submit);
        filterSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                radioGroup = filterPopup.findViewById(R.id.filter_radio_group);
                int selectedRadioId = radioGroup.getCheckedRadioButtonId();
                RadioButton selectedRadioBtn = filterPopup.findViewById(selectedRadioId);
                String filterCriteria = String.valueOf(selectedRadioBtn.getText());
                if(filterCriteria.equals("FOLLOWING")) {
                    showOnlyFollowing();
                } else if(filterCriteria.equals("FOLLOWERS")) {
                    showOnlyFollowers();
                } else {
                    showAll();
                }
                filterPopup.dismiss();
            }
        });
        filterCloseBtn = filterPopup.findViewById(R.id.close_filter);
        filterCloseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterPopup.dismiss();
            }
        });
    }

    private void resetPreviousFilter() {
        for(Marker marker: mMarkers) {
            marker.setVisible(true);
        }
        for(Marker follower: mFollowers) {
            for(Marker following: mFollowing) {
                if(following.getTitle().equals(follower.getTitle())) {
                    follower.setVisible(false);
                }
            }
        }
    }

    private void showOnlyFollowing() {
        resetPreviousFilter();

        for(Marker marker: mMarkers) {
            if(!mFollowing.contains(marker)) {
                marker.setVisible(false);
            }
        }
    }

    private void showOnlyFollowers() {
        resetPreviousFilter();

        for(Marker marker: mMarkers) {
            if(!mFollowers.contains(marker)) {
                marker.setVisible(false);
            } else {
                marker.setVisible(true);
            }
        }
    }

    private void showAll() {
        resetPreviousFilter();
    }

    private void findUser(String query) {
        for(Marker m: mMarkers) {
            if(m.getTitle().contains(query)) {
                moveCamera(new LatLng(m.getPosition().latitude, m.getPosition().longitude), DEFAULT_ZOOM);
                m.showInfoWindow();
                setSelectedUser(m.getTitle());
            }
        }
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
                            currentLocation1 = (Location) task.getResult();
                            if(currentUserId != null && currentLocation1 != null) {
                                databaseUsers.child(currentUserId).child("location").child("latitude").setValue(currentLocation1.getLatitude());
                                databaseUsers.child(currentUserId).child("location").child("longitude").setValue(currentLocation1.getLongitude());
                                moveCamera(new LatLng(currentLocation1.getLatitude(), currentLocation1.getLongitude()), DEFAULT_ZOOM);
                            } else {
                                LatLng defaultLocation = new LatLng(45.2691, 19.8374983);
                                moveCamera(defaultLocation, DEFAULT_ZOOM);
                                currentLocation1 = new Location("");
                                currentLocation1.setLatitude(defaultLocation.latitude);
                                currentLocation1.setLongitude(defaultLocation.longitude);
                            }
                            continueMapInit();
                        }else {
                            Toast.makeText(getContext(), "unable to get current location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        } catch (SecurityException e){

        }

    }

    private void continueMapInit() {
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

    private void moveCamera(LatLng latLng, float zoom) {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
    }

    private void addMarker(User user, boolean visible) {
        Log.d(TAG, "addMarkers");
        if(user.getLocation() != null) {
            Marker marker = mMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker_user))
                    .anchor(0.0f, 1.0f) // Anchors the marker on the bottom left
                    .title(user.getUsername())
                    .snippet(user.getEmail())
                    .position(new LatLng(user.getLocation().getLatitude(), user.getLocation().getLongitude())));
            marker.setVisible(visible);
            mMarkers.add(marker);
        }
    }

    private void addFollowingMarker(User user, boolean visible) {
        Log.d(TAG, "add following Markers");
        if(user.getLocation() != null) {
            Marker marker = mMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_following))
                    .anchor(0.0f, 1.0f) // Anchors the marker on the bottom left
                    .title(user.getUsername())
                    .snippet(user.getEmail())
                    .position(new LatLng(user.getLocation().getLatitude(), user.getLocation().getLongitude())));
            marker.setVisible(visible);
            mMarkers.add(marker);
            mFollowing.add(marker);
        }
    }

    private void addFollowerMarker(User user, boolean visible) {
        Log.d(TAG, "add follower Markers");
        if(user.getLocation() != null) {
            Marker marker = mMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_follower))
                    .anchor(0.0f, 1.0f) // Anchors the marker on the bottom left
                    .title(user.getUsername())
                    .snippet(user.getEmail())
                    .position(new LatLng(user.getLocation().getLatitude(), user.getLocation().getLongitude())));
            marker.setVisible(visible);
            mMarkers.add(marker);
            mFollowers.add(marker);
        }
    }

    private void moveMarker(User user, String username, LatLng newPosition) {
        Log.d(TAG, "moveMarker");
        boolean visible = inRadius(user);
        Marker changedMarker = null;
        for(Marker marker: mMarkers) {
            if(marker.getTitle().equals(username)) {
                marker.setPosition(newPosition);
                marker.setVisible(visible);
            }
        }
        for(Marker marker: mFollowing) {
            if(marker.getTitle().equals(username)) {
                marker.setPosition(newPosition);
                changedMarker = marker;
                marker.setVisible(visible);
            }
        }

        for(Marker marker: mFollowers) {
            if(marker.getTitle().equals(username)) {
                marker.setPosition(newPosition);
                if(changedMarker == null) {
                    marker.setVisible(visible);
                }
            }
        }
    }

    private void removeMarker(String username) {
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
            if(mFollowing.contains(markerToRemove)) {
                mFollowing.remove(markerToRemove);
            }
            if(mFollowers.contains(markerToRemove)) {
                mFollowers.remove(markerToRemove);
            }
        }
    }

    private boolean inRadius(User user) {
        if (user.getLocation() != null) {
            if(currentLocation1 != null) {
                float[] results = new float[2];
                Location.distanceBetween(currentLocation1.getLatitude(), currentLocation1.getLongitude(), user.getLocation().getLatitude(), user.getLocation().getLongitude(), results);
                if(results.length > 0) {
                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this.getContext());
                    if(preferences.getAll().get("pref_radius") != null) {
                        String radius = preferences.getAll().get("pref_radius").toString();
                        if (radius != null && results[0] < Integer.valueOf(radius) * 1000) {
                            return true;
                        } else {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    private void getUsers() {
        childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.d(TAG, dataSnapshot.getValue().toString());
                User user = dataSnapshot.getValue(User.class);
                if(inRadius(user)) {
                    addUser(user, true);
                } else {
                    addUser(user, false);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                //ako je promenjen ulogovan korisnik pomeri kameru
                //ako je promenjen neko drugi pomeri marker na novu lokaciju
                User user = dataSnapshot.getValue(User.class);
                if(user.getId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid()) && user.getLocation() != null) {
                    moveCamera(new LatLng(user.getLocation().getLatitude(), user.getLocation().getLongitude()), DEFAULT_ZOOM);
                } else {
                    if(user.getLocation() != null) {
                        Log.d(TAG, "pomeren");
                        moveMarker(user, user.getUsername(), new LatLng(user.getLocation().getLatitude(), user.getLocation().getLongitude()));
                    }
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
        };
        databaseUsers.addChildEventListener(childEventListener);
    }

    private void addUser(User user, boolean inRadius) {
        for(User u: users) {
            if(u.getEmail().equals(user.getEmail()) || FirebaseAuth.getInstance().getCurrentUser().getUid().equals(user.getId())) {
                return;
            }
        }
        users.add(user);
        if (user.getUserProfile().getFollowers() != null && user.getUserProfile().getFollowers().contains(currentUserId)) {
            addFollowingMarker(user, inRadius);
            if(user.getUserProfile().getFollowing().contains(currentUserId)) {
                addFollowerMarker(user, false);
            }
            return;
        }else if(user.getUserProfile().getFollowing() != null && user.getUserProfile().getFollowing().contains(currentUserId)) {
            addFollowerMarker(user, inRadius);
        }else {
            addMarker(user, inRadius);
        }
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

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(TAG, "detach");
        if(childEventListener != null) {
            databaseUsers.removeEventListener(childEventListener);
        }
    }
}
