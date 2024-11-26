package com.example.btlg05.Map;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.btlg05.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class MapsFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private String curLocation;
    private String nameLocation;
    private double latitude;
    private double longitude;

    // Hàm tạo để nhận dữ liệu vị trí

    public static MapsFragment newInstance(String currentLat, String locationName) {
        MapsFragment fragment = new MapsFragment();
        Bundle args = new Bundle();
        args.putString("currentLat", currentLat);
        args.putString("locationName", locationName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment (đặt layout cho fragment này)
        View rootView = inflater.inflate(R.layout.fragment_maps, container, false);

        // Lấy SupportMapFragment và gọi onMapReady khi bản đồ sẵn sàng
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this); // Đăng ký callback
        }

        return rootView;  // Trả về view cho fragment
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        Geocoder geocoder = new Geocoder(getContext());
        curLocation = getArguments().getString("currentLat");
        nameLocation =getArguments().getString("locationName");
        try {
            List<Address> addresses = geocoder.getFromLocationName(curLocation, 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                latitude = address.getLatitude();
                longitude = address.getLongitude();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // Đánh dấu vị trí thu gom
        LatLng currentLocation = new LatLng(latitude, longitude);
        mMap.addMarker(new MarkerOptions().position(currentLocation).title(""+nameLocation));
//
//        // Đánh dấu vị trí hiện tại
//        LatLng destination = new LatLng(destinationLat, destinationLng);
//        mMap.addMarker(new MarkerOptions().position(destination).title("Vị trí đích"));

        // Di chuyển camera để hiển thị cả hai điểm
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15)); // Thiết lập độ zoom
    }
}
