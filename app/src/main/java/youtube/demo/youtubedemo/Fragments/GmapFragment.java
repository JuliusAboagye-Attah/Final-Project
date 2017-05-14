package youtube.demo.youtubedemo.Fragments;


import android.Manifest;
import android.annotation.TargetApi;
import android.app.Fragment;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.renderscript.Double2;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.google.android.gms.fitness.data.Value;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import youtube.demo.youtubedemo.R;


public class GmapFragment extends Fragment implements OnMapReadyCallback {
    TrackGPS trackGPS = new TrackGPS(GmapFragment.this.getContext());
    double longi =   TrackGPS.longitude;
    double lati =   TrackGPS.latitude;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_gmaps, container, false);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        MapFragment fragment = (MapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        fragment.getMapAsync(this);

        trackGPS.canGetLocation();
        trackGPS.getLat1();
        trackGPS.getLong1();
        trackGPS.onLocationChanged(trackGPS.loc);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        LatLng marker = new LatLng(lati,longi);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(marker, 15));
        googleMap.addMarker(new MarkerOptions().title("In Accra").position(marker));
        if (ActivityCompat.checkSelfPermission(GmapFragment.this.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        googleMap.setMyLocationEnabled(true);



    }


}
