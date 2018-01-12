package com.cyber_i.ionicembedded;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

  IonicFragment ionicFragment = IonicFragment.newInstance();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.startIonicActivity:
        Intent intent = new Intent(this, IonicActivity.class);
        startActivity(intent);
        break;
      case R.id.startIonicFragment:
        Button btns[] = {
          findViewById(R.id.startIonicActivity),
          findViewById(R.id.startIonicFragment)
        };

        for (Button btn : btns) {
          btn.setVisibility(View.GONE);
        }

        FragmentManager fm = getFragmentManager();
        FragmentTransaction tx = fm.beginTransaction();
        tx.add(R.id.cordovaWebViewFrag, ionicFragment);
        tx.commitAllowingStateLoss();
        break;
    }
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    ionicFragment.onActivityResult(requestCode, resultCode, data);
  }
}
