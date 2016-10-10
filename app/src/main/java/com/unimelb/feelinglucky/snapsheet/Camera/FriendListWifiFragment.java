package com.unimelb.feelinglucky.snapsheet.Camera;

import android.app.Fragment;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.unimelb.feelinglucky.snapsheet.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mac on 16/10/6.
 */

public class FriendListWifiFragment extends Fragment implements WifiP2pManager.PeerListListener {

    private List<WifiP2pDevice> peers;
    private FriendListViewApter apter;

    private ListView listView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scan_friend_list, null);
        peers = new ArrayList();
        apter = new FriendListViewApter(getActivity(), peers);
        listView = (ListView) view.findViewById(R.id.lvBluetooth);
        listView.setAdapter(apter);

        return view;
    }

    @Override
    public void onPeersAvailable(WifiP2pDeviceList peerList) {
        AddFriendNearByFragment.stopLoading();
        peers.clear();
        peers.addAll(peerList.getDeviceList());
        apter.notifyDataSetChanged();
        if (peers.size() == 0) {
            Log.d("None", "No devices found");
            return;
        }
    }
}
