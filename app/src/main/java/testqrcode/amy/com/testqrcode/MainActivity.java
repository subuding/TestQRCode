package testqrcode.amy.com.testqrcode;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.xys.libzxing.zxing.activity.CaptureActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.im_qrcode)
    ImageView imQrcode;
    @BindView(R.id.tv_scannerqrcode)
    TextView tvScannerqrcode;
    @BindView(R.id.et_createqrcode)
    EditText etCreateqrcode;
    QRCodeUtils qrCodeUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        qrCodeUtils=new QRCodeUtils();
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @OnClick({R.id.bt_createqrcode, R.id.bt_scannerqrcode,R.id.bt_scannerqrcode2})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_createqrcode:
               if(!etCreateqrcode.getText().toString().trim().equals("")){
                   Bitmap logoBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
                   Bitmap qrBitmap = qrCodeUtils.addLogo(qrCodeUtils.generateBitmap(etCreateqrcode.getText().toString(), 400, 400), logoBitmap);
                   imQrcode.setImageBitmap(qrBitmap);
               }else{
                   Toast.makeText(this,"请输入url",Toast.LENGTH_SHORT).show();
               }
                break;
            case R.id.bt_scannerqrcode:
                //动态申请相机权限
                if (!(checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)) {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, 1);
                } else {
                    startActivityForResult(new Intent(this, CaptureActivity.class), 0);
                }
                break;
            case R.id.bt_scannerqrcode2:
               if(imQrcode.getDrawable()!=null){
                   Bitmap image = ((BitmapDrawable)imQrcode.getDrawable()).getBitmap();
                   com.google.zxing.Result result=qrCodeUtils.parseQRcodeBitmap(image);
                   tvScannerqrcode.setText(result.getText());
                    }else{
                   Toast.makeText(this,"请先生成二维码",Toast.LENGTH_SHORT).show();
               }
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            startActivityForResult(new Intent(this, CaptureActivity.class), 0);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 0 && resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            //显示扫描到的内容
            tvScannerqrcode.setText(bundle.getString("result"));
        }
    }

}
