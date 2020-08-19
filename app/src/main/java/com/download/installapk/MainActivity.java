package com.download.installapk;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Toast;

import com.maning.updatelibrary.InstallUtils;

public class MainActivity extends AppCompatActivity {
    String apkUrl = "https://androidwave.com/source/apk/app-pagination-recyclerview.apk";
    private String APP_INSTALL_PATH = "\"application/vnd.android.package-archive\"";
    private String FILE_NAME = "SampleDownloadApp.apk";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    void onclick(){
        String destination = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).toString() + "/";
        destination += FILE_NAME;
        //下载APK
        InstallUtils.with(this)
                //必须-下载地址
                .setApkUrl(apkUrl)
                //非必须-下载保存的文件的完整路径+/name.apk，使用自定义路径需要获取读写权限
                .setApkPath(destination)
                //非必须-下载回调
                .setCallBack(new InstallUtils.DownloadCallBack() {
                    @Override
                    public void onStart() {
                        //下载开始
                    }

                    @Override
                    public void onComplete(String path) {

                        //下载完成
                        //先判断有没有安装权限---适配8.0
                        //如果不想用封装好的，可以自己去实现8.0适配
                        InstallUtils.checkInstallPermission(MainActivity.this, new InstallUtils.InstallPermissionCallBack() {
                            @Override
                            public void onGranted() {
                                //去安装APK
                               // installApk(...);
                            }

                            @Override
                            public void onDenied() {
                                //弹出弹框提醒用户
                                AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this)
                                        .setTitle("温馨提示")
                                        .setMessage("必须授权才能安装APK，请设置允许安装")
                                        .setNegativeButton("取消", null)
                                        .setPositiveButton("设置", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                //打开设置页面
                                                InstallUtils.openInstallPermissionSetting(MainActivity.this, new InstallUtils.InstallPermissionCallBack() {
                                                    @Override
                                                    public void onGranted() {
                                                        //去安装APK
                                                      //  installApk(...);
                                                    }

                                                    @Override
                                                    public void onDenied() {
                                                        //还是不允许咋搞？
                                                        Toast.makeText(MainActivity.this, "不允许安装咋搞？强制更新就退出应用程序吧！", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            }
                                        })
                                        .create();
                                alertDialog.show();
                            }
                        });

                    }

                    @Override
                    public void onLoading(long total, long current) {
                        //下载中
                    }

                    @Override
                    public void onFail(Exception e) {
                        //下载失败
                    }

                    @Override
                    public void cancle() {
                        //下载取消
                    }
                })
                //开始下载
                .startDownload();


        //安装APK
        InstallUtils.installAPK(MainActivity.this, APP_INSTALL_PATH, new InstallUtils.InstallCallBack() {
            @Override
            public void onSuccess() {
                //onSuccess：表示系统的安装界面被打开
                //防止用户取消安装，在这里可以关闭当前应用，以免出现安装被取消
                Toast.makeText(MainActivity.this, "正在安装程序", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFail(Exception e) {
                //安装出现异常，这里可以提示用用去用浏览器下载安装
            }
        });

        //取消下载
        InstallUtils.cancleDownload();

        //是否正在下载
        InstallUtils.isDownloading();

        //单独设置下载监听
        InstallUtils.setDownloadCallBack(new InstallUtils.DownloadCallBack() {
            @Override
            public void onStart() {

            }

            @Override
            public void onComplete(String path) {

            }

            @Override
            public void onLoading(long total, long current) {

            }

            @Override
            public void onFail(Exception e) {

            }

            @Override
            public void cancle() {

            }
        });


        //安装APK
        /**
         * 安装APK工具类
         * @param activity       上下文
         * @param filePath      文件路径
         * @param callBack      安装界面成功调起的回调
         */
        InstallUtils.installAPK(MainActivity.this, APP_INSTALL_PATH, new InstallUtils.InstallCallBack() {
            @Override
            public void onSuccess() {
                //onSuccess：表示系统的安装界面被打开
                //防止用户取消安装，在这里可以关闭当前应用，以免出现安装被取消
                Toast.makeText(MainActivity.this, "正在安装程序", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFail(Exception e) {
                Toast.makeText(MainActivity.this, "安装失败:" + e.toString(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}