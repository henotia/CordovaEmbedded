package com.cyber_i.ionicembedded;

import android.app.Fragment;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import org.apache.cordova.*;
import org.json.JSONException;

import java.util.ArrayList;

public class IonicFragment extends Fragment {

  private CordovaWebView appView;
  protected CordovaPreferences preferences;
  protected String launchUrl;
  protected ArrayList<PluginEntry> pluginEntries;
  protected CordovaInterfaceImpl cordovaInterface;

  public static IonicFragment newInstance() {
    return new IonicFragment();
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.fragment_layout, container, false);

    cordovaInterface = new CordovaInterfaceImpl(getActivity());

    if (savedInstanceState != null) {
      cordovaInterface.restoreInstanceState(savedInstanceState);
    }

    loadConfig();

    // system webview 구현
    appView = makeWebView();

    createViews(rootView);

    if (!appView.isInitialized()) {
      appView.init(cordovaInterface, pluginEntries, preferences);
    }
    cordovaInterface.onCordovaInit(appView.getPluginManager());

    appView.loadUrl(launchUrl);
    return rootView;
  }

  private void createViews(View rootView) {
    // Cordova SystemWebView에 대한 Layout Parameter 설정
    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
      RelativeLayout.LayoutParams.MATCH_PARENT,
      RelativeLayout.LayoutParams.MATCH_PARENT);

    appView.getView().setLayoutParams(params);

    // rootView에 SystemWebView 추가
    ((RelativeLayout) rootView).addView(appView.getView());
  }

  private CordovaWebView makeWebView() {
    return new CordovaWebViewImpl(makeWebViewEngine());
  }

  private CordovaWebViewEngine makeWebViewEngine() {
    return CordovaWebViewImpl.createEngine(getActivity(), preferences);
  }

  private void loadConfig() {
    ConfigXmlParser parser = new ConfigXmlParser();
    parser.parse(getActivity());
    preferences = parser.getPreferences();
    preferences.setPreferencesBundle(getActivity().getIntent().getExtras());
    pluginEntries = parser.getPluginEntries();
    launchUrl = parser.getLaunchUrl();
  }

  // Plugin 통신을 위한 추가 부분
  @Override
  public void startActivityForResult(Intent intent, int requestCode) {
    cordovaInterface.setActivityResultRequestCode(requestCode);
    super.startActivityForResult(intent, requestCode);
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    cordovaInterface.onActivityResult(requestCode, resultCode, data);
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    try {
      cordovaInterface.onRequestPermissionResult(requestCode, permissions, grantResults);
    } catch (JSONException e) {
      e.printStackTrace();
    }
  }

  public void onSaveInstanceState(Bundle outState) {
    cordovaInterface.onSaveInstanceState(outState);
    super.onSaveInstanceState(outState);
  }

  @Override
  public void onConfigurationChanged(Configuration newConfig) {
    super.onConfigurationChanged(newConfig);
    if (appView == null) return;
    PluginManager pm = appView.getPluginManager();
    if (pm != null) {
      pm.onConfigurationChanged(newConfig);
    }
  }
}
