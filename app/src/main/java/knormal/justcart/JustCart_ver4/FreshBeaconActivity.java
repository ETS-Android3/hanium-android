package knormal.justcart.JustCart_ver4;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.JustCart_ver4.R;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static java.lang.Thread.sleep;

public class FreshBeaconActivity extends AppCompatActivity {
    private List<Beacon> beaconList = new ArrayList<>();
    private BeaconManager beaconManager = BeaconManager.getInstanceForApplication(this);
    private static String TAG = "phpquerytest";
    String data = "상추";

    @Override
    protected void onCreate(Bundle savedInstanceState) {    //액티비티 생성할 때
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_freshbeacon);

        beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24")); //iBeacon의 layout
        Log.i("foxy", "ScanBT - i am in onCreateeeeeeeee");
    }

    //비콘인식
    @Override
    protected void onResume() {
        super.onResume();
        beaconManager.addRangeNotifier (new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
                if (beacons.size() > 0) {
                    for (Beacon beacon : beacons) {
                        int minor = beacon.getId3().toInt();// beacon minor
                        if (((Beacon) beacons.iterator().next()).getDistance() < 0.3 && minor == 55024) {// minor가 55177인 비콘 인식
                            Log.i(TAG, "The first beacon I see is about " + minor + " !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                            Log.i(TAG, "The first beacon I see is about " + ((Beacon) beacons.iterator().next()).getDistance() + " meters away.");
                            Intent intent = new Intent(FreshBeaconActivity.this, FreshActivity2.class);
                            intent.putExtra("Fresh", data);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//액티비티 스택제거
                            intent.addFlags (Intent.FLAG_ACTIVITY_NO_ANIMATION);//액티비티 애니메이션제거
                            startActivity(intent);
                            finish();
                        } else {
                            Log.i(TAG, "The no beacon I see is about --------------------------- " + ((Beacon) beacons.iterator().next()).getDistance() + " meters away." + minor);
                        }
                    }
                }
            }

        });
        //Region region = new Region("myRangingUniqueId", uUID, major, minor);
        beaconManager.startRangingBeacons(new Region("FreshBeacon", Identifier.parse("E2C56DB5-DFFB-48D2-B060-D0F5A71096E0"), Identifier.parse("40010"), Identifier.parse("55024")));

    }

    @Override
    //액티비티 옮겨갈 때 비콘인식 중지
    protected void onPause() {
        super.onPause();
        beaconManager.stopRangingBeacons(BeaconReferenceApplication.wildcardRegion);
        beaconManager.removeAllRangeNotifiers();
    }

    //뒤로가기 버튼 눌렀을 때
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(FreshBeaconActivity.this, CheckActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}