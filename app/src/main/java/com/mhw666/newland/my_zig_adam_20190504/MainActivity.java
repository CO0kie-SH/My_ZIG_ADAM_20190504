package com.mhw666.newland.my_zig_adam_20190504;

import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.nle.mylibrary.forUse.mdbus4150.Modbus4150;
import com.nle.mylibrary.forUse.zigbee.Zigbee;
import com.nle.mylibrary.transfer.DataBusFactory;

public class MainActivity extends AppCompatActivity {

    Zigbee zig;
    Modbus4150 adam;
    ToggleButton tgmen,tgfeng,tgdeng;
    boolean men=false,feng=false,deng=false,ren=false,yan=false;
    double wendu=0,shidu=0;
    static String IP="172.10.48.16";
    TextView tvRen,tvYan,tvWen,tvShi;
    RadioButton zd;
    ImageView imgren,imgfeng,imgdeng;
    int Aren=6,Ayan=5,Adeng=1,Afeng=0,Agan1=3,Agan2=2;
    EditText edtL;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        adam=new Modbus4150(DataBusFactory.newSocketDataBus(IP,2001));
        zig=new Zigbee(DataBusFactory.newSocketDataBus(IP,2002));
        tvRen=findViewById(R.id.textView);
        tvYan=findViewById(R.id.textView2);
        tvWen=findViewById(R.id.textView3);
        tvShi=findViewById(R.id.textView4);

        tgmen=findViewById(R.id.tbdooroff);
        tgfeng=findViewById(R.id.tbfanoff);
        tgdeng=findViewById(R.id.tbalarmoff);
        zd=findViewById(R.id.rbauto);
        edtL=findViewById(R.id.etset);
        imgren=findViewById(R.id.ivdoor);
        imgfeng=findViewById(R.id.ivfan);
        imgdeng=findViewById(R.id.ivalarm);

        tgmen.setOnCheckedChangeListener((compoundButton, b) -> {
            men=b;
            if(b){
                adamsw(Agan1,false);
                adamsw(Agan2,true);
            }else{
                adamsw(Agan2,false);
                adamsw(Agan1,true);
            }
        });
        tgfeng.setOnCheckedChangeListener((compoundButton, b) -> {feng=b;adamsw(Afeng,b);});
        tgdeng.setOnCheckedChangeListener((compoundButton, b) -> {
            deng=b;
            adamsw(Adeng,b);
        });

        handler.postDelayed(ref,1000);
        adamsw(Agan2,false);
        adamsw(Agan1,true);
    }
    Handler handler=new Handler();
    Runnable ref=new Runnable() {
        @Override
        public void run() {
            getData();
            refUI();
            handler.postDelayed(ref,1000);
        }
    };

    private void getData() {
        try {
            adam.getVal(Ayan,val -> yan=val==1);
            adam.getVal(Aren,val -> ren=val==0);
            double[] wenshi = zig.getTmpHum();
            wendu=wenshi[0];
            shidu=wenshi[1];
            String ling=edtL.getText().toString().trim();
            double lin=Double.parseDouble(ling);
            feng=wendu>lin;
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if(zd.isChecked()){
                men=ren;
                deng=yan;
                tgmen.setChecked(men);
                tgfeng.setChecked(feng);
                tgdeng.setChecked(deng);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void adamsw(int i,boolean b) {
        try {
            if(b) {
                adam.openRelay(i, null);
            }else{
                adam.closeRelay(i,null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void refUI() {
        System.out.println("人"+ren+"烟"+yan+"温"+wendu+"湿"+shidu);
        try {
            tvRen.setText("人体:"+(ren?"有":"无"));
            tvYan.setText("烟雾:"+(yan?"有":"无"));
            tvWen.setText("温度:"+(wendu));
            tvShi.setText("湿度:"+(shidu));
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            AnimationDrawable dh=new AnimationDrawable();
            dh= (AnimationDrawable) imgfeng.getDrawable();
            if(feng){
                dh.start();
            }else{
                dh.stop();
            }
            dh= (AnimationDrawable) imgdeng.getDrawable();
            if(deng){
                dh.start();
            }else{
                dh.stop();
            }
            if(men){
                imgren.setImageResource(R.drawable.dooropen);
                dh= (AnimationDrawable) imgren.getDrawable();
                dh.start();
            }else{
                imgren.setImageResource(R.drawable.doorclose);
                dh= (AnimationDrawable) imgren.getDrawable();
                dh.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(ref);
        try {
            adam.closeRelay(Agan1,null);
            adam.closeRelay(Agan2,null);
            adam.closeRelay(Afeng,null);
            adam.closeRelay(Adeng,null);
            adam.stopConnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void OnCl(View v){
        try {
            adam.closeRelay(Agan1,null);
            adam.closeRelay(Agan2,null);
            adam.closeRelay(Afeng,null);
            adam.closeRelay(Adeng,null);
            adam.stopConnect();
            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
