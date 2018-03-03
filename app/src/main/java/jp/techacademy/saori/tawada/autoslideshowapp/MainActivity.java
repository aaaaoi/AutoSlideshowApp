package jp.techacademy.saori.tawada.autoslideshowapp;

import android.graphics.Bitmap;
import android.media.Image;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 100;

    Timer mTimer;
    ImageView imageView;

    Handler mHandler = new Handler();

    Button mGoButton;
    Button mBackButton;
    Button mStartStopButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mGoButton = (Button) findViewById(R.id.go_button);
        mBackButton = (Button) findViewById(R.id.back_button);
        mStartStopButton = (Button) findViewById(R.id.start_stop_button);

        // Android 6.0以降の場合
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // パーミッションの許可状態を確認する
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                // 許可されている
                getContentsInfo();
            } else {
                // 許可されていないので許可ダイアログを表示する
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
            }
            // Android 5系以下の場合
        } else {
            getContentsInfo();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getContentsInfo();
                }
                break;
            default:
                break;
        }
    }

    private void getContentsInfo() {

        mGoButton.setOnClickListener(new View.OnClickListener() {

            Cursor cursor = null;

            @Override
            public void onClick(View v) {
                if (cursor == null) {
                    ContentResolver resolver = getContentResolver();
                    cursor = resolver.query(
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                            null,
                            null,
                            null,
                            null
                    );
                }

                if (cursor.moveToNext()) {
                    int fieldIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID);
                    Long id = cursor.getLong(fieldIndex);
                    Uri imageUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);

                    ImageView imageView = (ImageView) findViewById(R.id.imageView);
                    imageView.setImageURI(imageUri);
                } else {
                    cursor.moveToFirst();
                    int fieldIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID);
                    Long id = cursor.getLong(fieldIndex);
                    Uri imageUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);

                    ImageView imageView = (ImageView) findViewById(R.id.imageView);
                    imageView.setImageURI(imageUri);
                }
            }
        });

        mBackButton.setOnClickListener(new View.OnClickListener() {

            Cursor cursor = null;

            @Override
            public void onClick(View view) {
                if (cursor == null) {
                    ContentResolver resolver = getContentResolver();
                    cursor = resolver.query(
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                            null,
                            null,
                            null,
                            null
                    );
                }

                if (cursor.moveToPrevious()) {
                    int fieldIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID);
                    Long id = cursor.getLong(fieldIndex);
                    Uri imageUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);

                    ImageView imageView = (ImageView) findViewById(R.id.imageView);
                    imageView.setImageURI(imageUri);
                } else {
                    cursor.moveToLast();
                    int fieldIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID);
                    Long id = cursor.getLong(fieldIndex);
                    Uri imageUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);

                    ImageView imageView = (ImageView) findViewById(R.id.imageView);
                    imageView.setImageURI(imageUri);
                }
            }
        });

        mStartStopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTimer == null) {
                    mTimer = new Timer();
                    mTimer.schedule(new TimerTask() {
                        @Override
                        public void run() {

                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    mGoButton.performClick();
                                }
                            });
                        }
                    }, 2000, 2000);

                    mGoButton.setEnabled(false);
                    mBackButton.setEnabled(false);

                } else {
                    mTimer.cancel();
                    mTimer = null;

                    mGoButton.setEnabled(true);
                    mBackButton.setEnabled(true);
                }
            }
        });
    }

}