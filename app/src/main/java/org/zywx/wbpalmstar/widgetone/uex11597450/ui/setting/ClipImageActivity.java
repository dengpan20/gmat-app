package org.zywx.wbpalmstar.widgetone.uex11597450.ui.setting;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.zywx.wbpalmstar.widgetone.uex11597450.base.BaseActivity;
import org.zywx.wbpalmstar.widgetone.uex11597450.R;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;


public class ClipImageActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "ClipImageActivity";
    private ClipViewLayout clipViewLayout1;
    private ImageView back;
    private TextView btnCancel;
    private TextView btnOk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clip_image);
        initView();
    }

    /**
     * 初始化组件
     */
    public void initView() {
        clipViewLayout1 = (ClipViewLayout) findViewById(R.id.clipViewLayout1);
        back = (ImageView) findViewById(R.id.iv_back);
        btnCancel = (TextView) findViewById(R.id.btn_cancel);
        btnOk = (TextView) findViewById(R.id.bt_ok);
        //设置点击事件监听器
        back.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        btnOk.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "image uri: " + getIntent().getData());
        clipViewLayout1.setVisibility(View.VISIBLE);
        //设置图片资源
        clipViewLayout1.setImageSrc(getIntent().getData());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_cancel:
                finish();
                break;
            case R.id.bt_ok:
                generateUriAndReturn();
                break;
        }
    }


    /**
     * 生成Uri并且通过setResult返回给打开的activity
     */
    private void generateUriAndReturn() {
        //调用返回剪切图
        Bitmap zoomedCropBitmap;
        zoomedCropBitmap = clipViewLayout1.clip();
        if (zoomedCropBitmap == null) {
            Log.e("android", "zoomedCropBitmap == null");
            return;
        }
        Uri mSaveUri = Uri.fromFile(new File(getCacheDir(), "cropped_" + System.currentTimeMillis() + ".jpg"));
        if (mSaveUri != null) {
            OutputStream outputStream = null;
            try {
                outputStream = getContentResolver().openOutputStream(mSaveUri);
                if (outputStream != null) {
                    zoomedCropBitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream);
                }
            } catch (IOException ex) {
                Log.e("android", "Cannot open file: " + mSaveUri, ex);
            } finally {
                if (outputStream != null) {
                    try {
                        outputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            Intent intent = new Intent();
            intent.setData(mSaveUri);
            setResult(RESULT_OK, intent);
            finish();
        }
    }
}
