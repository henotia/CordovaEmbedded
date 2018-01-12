package com.cyber_i.ionicembedded;

import android.os.Bundle;
import org.apache.cordova.CordovaActivity;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.CordovaWebViewImpl;
import org.apache.cordova.engine.SystemWebView;
import org.apache.cordova.engine.SystemWebViewEngine;

public class IonicActivity extends CordovaActivity {

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.cordova_layout);
    super.init();

    loadUrl(launchUrl);
  }

  @Override
  protected CordovaWebView makeWebView() {
    SystemWebView appView = (SystemWebView) findViewById(R.id.cordovaWebView);
    return new CordovaWebViewImpl(new SystemWebViewEngine(appView));
  }

  @Override
  protected void createViews() {
    // do nothings
  }
}
