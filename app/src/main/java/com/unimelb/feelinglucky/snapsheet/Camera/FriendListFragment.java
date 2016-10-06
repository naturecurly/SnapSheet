package com.unimelb.feelinglucky.snapsheet.Camera;

import android.app.Fragment;
import android.net.wifi.WpsInfo;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.unimelb.feelinglucky.snapsheet.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mac on 16/10/6.
 */

public class FriendListFragment extends Fragment implements WifiP2pManager.PeerListListener {

    private List<WifiP2pDevice> peers;
    private FriendLIstScanAdpter adpter;
    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scan_friend_list, null);
        peers = new ArrayList();
        adpter = new FriendLIstScanAdpter(getActivity(), peers);
        recyclerView = (RecyclerView) view.findViewById(R.id.friend_list_reccycler);
        recyclerView.setAdapter(adpter);
        return view;
    }

    @Override
    public void onPeersAvailable(WifiP2pDeviceList peerList) {
        peers.clear();
        peers.addAll(peerList.getDeviceList());
        adpter = new FriendLIstScanAdpter(getActivity(), peers);
        recyclerView.setAdapter(adpter);

//        adpter.notifyDataSetChanged();

        for (WifiP2pDevice device : peers){
            device.deviceName = "username";
            Log.i("address",device.deviceAddress);
            Log.i("info",device.toString());
            Log.i("name",device.deviceName);

            if ("66:bc:0c:83:b8:b7".equals(device.deviceAddress)){

                WifiP2pConfig config = new WifiP2pConfig();
                config.deviceAddress = device.deviceAddress;
                config.wps.setup = WpsInfo.PBC;
                Log.i("good","good");
            }
        }

        if (peers.size() == 0) {
            Log.d("None", "No devices found");
            return;
        }
    }
}
