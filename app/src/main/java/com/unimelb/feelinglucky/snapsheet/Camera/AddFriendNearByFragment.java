package com.unimelb.feelinglucky.snapsheet.Camera;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.unimelb.feelinglucky.snapsheet.R;
import com.unimelb.feelinglucky.snapsheet.SnapSheetActivity;
import com.unimelb.feelinglucky.snapsheet.Util.SharedPreferencesUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static android.os.Looper.getMainLooper;

/**
 * Created by mac on 16/10/6.
 */

public class AddFriendNearByFragment extends Fragment {
    private Button mScan;
    private View view;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_search_nearby, container, false);
        initWIFISetting();
        scanUser();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        android.app.Fragment f =  getActivity().getFragmentManager().findFragmentById(R.id.near_by_list);
        if (f != null) {
            getActivity().getFragmentManager().beginTransaction().remove(f).commit();
        }
    }

    private WifiP2pManager mManager;
    private WifiP2pManager.Channel mChannel;
    private BroadcastReceiver mReceiver;
    private static ProgressDialog progressDialog;

    private IntentFilter mIntentFilter;

    private void initWIFISetting() {
        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);

        mManager = (WifiP2pManager) getActivity().getSystemService(Context.WIFI_P2P_SERVICE);
        mChannel = mManager.initialize(getActivity(), getMainLooper(), null);
        mReceiver = new WiFiDirectBroadcastReceiver(mManager, mChannel, (SnapSheetActivity) getActivity());
    }

    public void onInitiateDiscovery() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }

        try {
            Method m = mManager.getClass().getMethod("setDeviceName", new Class[]{
                    mChannel.getClass(), String.class,
                    WifiP2pManager.ActionListener.class
            });
            String deviceName = "[Snap Sheet] " + SharedPreferencesUtils.getSharedPreferences(getActivity()).getString("username", null);
            m.invoke(mManager, mChannel, deviceName, new WifiP2pManager.ActionListener() {
                @Override
                public void onSuccess() {
                }

                @Override
                public void onFailure(int reason) {
                }
            });
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        progressDialog = ProgressDialog.show(getActivity(), "Press back to cancel", "finding peers", true,
                true, new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        getFragmentManager().popBackStack();
                    }
                });
    }

    public static void stopLoading() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    public void scanUser() {
        onInitiateDiscovery();
        mManager.discoverPeers(mChannel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                Log.i("success", "discoverPeers");
            }

            @Override
            public void onFailure(int reasonCode) {
                Log.i("failure", "discoverPeers");
            }
        });
    }

    @Override
    public void onResume() {
        getActivity().registerReceiver(mReceiver, mIntentFilter);
        super.onResume();
    }

    @Override
    public void onPause() {
        getActivity().unregisterReceiver(mReceiver);
        super.onPause();
    }
}
