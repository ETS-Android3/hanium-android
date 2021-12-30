package knormal.justcart.JustCart_ver4;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.JustCart_ver4.R;

public class BluetoothActivity extends AppCompatActivity {
    private ActivityResultLauncher<Intent> resultLauncher;

    public static Activity bluetoothActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);

        bluetoothActivity = BluetoothActivity.this;

        //블루투스 지원 유무 확인
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        resultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if(result.getResultCode() == RESULT_OK){ //사용자가 블루투스 요청 허용하면 메인화면으로 돌아가기
                            Intent intent2 = new Intent(BluetoothActivity.this, MainActivity.class);
                            startActivity(intent2);
                        } else {
                            finishAffinity(); //사용자가 블루투스 요청 거절 시 앱 종료
                        }
                    }
                });

        if (bluetoothAdapter == null) {
            // Device doesn't support Bluetooth
            Toast.makeText(this, "블루투스를 지원하지 않는 단말기 입니다.", Toast.LENGTH_SHORT).show();
            finish();
            return;

        } else {
            if (!bluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                enableBtIntent.putExtra("REQUEST_ENABLE_BT", 1);
                resultLauncher.launch(enableBtIntent);
            } else {
                Intent intent2 = new Intent(BluetoothActivity.this, MainActivity.class);
                startActivity(intent2);
            }
            finish();
        }
    }
}
