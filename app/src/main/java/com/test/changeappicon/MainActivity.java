package com.test.changeappicon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.test.changeappicon.alias.Test2Activity2;
import com.test.changeappicon.alias.Test3Activity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class MainActivity extends AppCompatActivity {

    private Button btChangeOne;
    private Button btChangeThird;
    private Button btReset;
    private PackageManager packageManager;
    private ComponentName componentNameDef;
    private ComponentName componentName2;
    private ComponentName componentName3;
    private String packageName;
    private TextView tvText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!isTaskRoot()) {
            Intent mainIntent = getIntent();
            String action = mainIntent.getAction();
            if (mainIntent.hasCategory(Intent.CATEGORY_LAUNCHER) && action.equals(Intent.ACTION_MAIN)) {
                finish();
                return;
            }
        }
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        packageName = getPackageName();
        Log.i("packageName","packageName==="+packageName);
        componentNameDef = new ComponentName(packageName, MainActivity.class.getName());
        componentName2 = new ComponentName(packageName,  Test2Activity2.class.getName());
        componentName3 = new ComponentName(packageName,   Test3Activity.class.getName());

        packageManager = getApplicationContext().getPackageManager();
        btReset = findViewById(R.id.btReset);
        btReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                enable(componentNameDef);
                disable(componentName2);
                disable(componentName3);

//                enable(componentName4);
//                testService(0);
            }
        });

        btChangeOne = findViewById(R.id.btChangeOne);
        btChangeOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                disable(componentNameDef);
//                disable(componentName4);
                disable(componentName3);
                enable(componentName2);
//                testService(2);



            }
        });


        btChangeThird = findViewById(R.id.btChangeThird);
        btChangeThird.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                disable(componentNameDef);
//                disable(componentName4);
                disable(componentName2);
                enable(componentName3);
//                testService(3);


            }
        });


       /* View btExit = findViewById(R.id.btExit);
        btExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.exit(0);
            }
        });*/

        final TextView tvText = findViewById(R.id.tvText);
        String text = getIntent().getStringExtra("text");
        if(!TextUtils.isEmpty(text)){
            tvText.setText(text);
        }
        tvText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvText.setText("jump");
            }
        });
        View btJump1 = findViewById(R.id.btJump1);
        btJump1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                intent.putExtra("text","跳转成功");
                startActivity(intent);
            }
        });

        View btJump2 = findViewById(R.id.btJump2);
        btJump2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("text","跳转成功2");
                intent.setComponent(new ComponentName(MainActivity.this, Test2Activity2.class.getName()));
                startActivity(intent);
            }
        });
        View btJump3 = findViewById(R.id.btJump3);
        btJump3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("text","跳转成功3");
                intent.setComponent(new ComponentName(MainActivity.this, Test3Activity.class.getName()));
                startActivity(intent);
            }
        });
        View tvJump = findViewById(R.id.tvJump);
        tvJump.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                if(isEnabled(componentName2)){
                    intent.setComponent(componentName2);
                }else if(isEnabled(componentName3)){
                    intent.setComponent(componentName3);
                }else if(isEnabled(componentNameDef)){
                    intent.setComponent(componentNameDef);
                }else{
                    intent=new Intent(MainActivity.this,MainActivity.class);
                }
                intent.putExtra("text","跳转成功(兼容方法)");

               startActivity(intent);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    public static boolean isMainProcess(Context context) {
        String processName = getProcessName();
        //判断进程名，保证只有主进程运行
        if (!TextUtils.isEmpty(processName) && processName.equals(context.getPackageName())) {
            //在这里进行主进程初始化逻辑操作
            Log.i(">>>>>>", "oncreate");
            return true;
        }
        return false;
    }

    public static String getProcessName() {
        try {
            File file = new File("/proc/" + android.os.Process.myPid() + "/" + "cmdline");
            BufferedReader mBufferedReader = new BufferedReader(new FileReader(file));
            String processName = mBufferedReader.readLine().trim();
            mBufferedReader.close();
            return processName;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void enable(ComponentName componentName) {
        packageManager.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.SYNCHRONOUS);
    }

    public void disable(ComponentName componentName) {
        packageManager.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.SYNCHRONOUS);
    }
    public boolean isEnabled(ComponentName componentName) {
        int enabled = PackageManager.COMPONENT_ENABLED_STATE_ENABLED;
        int disabled = PackageManager.COMPONENT_ENABLED_STATE_DISABLED;
        int componentEnabledSetting = packageManager.getComponentEnabledSetting(componentName);
        return componentEnabledSetting==enabled;
    }
    public boolean isDef(ComponentName componentName) {
        int enabled = PackageManager.COMPONENT_ENABLED_STATE_ENABLED;
        int disabled = PackageManager.COMPONENT_ENABLED_STATE_DISABLED;
        int componentEnabledSetting = packageManager.getComponentEnabledSetting(componentName);
        return componentEnabledSetting==PackageManager.COMPONENT_ENABLED_STATE_DEFAULT;
    }
}
