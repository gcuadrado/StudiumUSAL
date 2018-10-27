package com.studium.usal.acceso;

import android.Manifest;
import android.app.DownloadManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.DownloadListener;
import android.webkit.URLUtil;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class WebpageActivity extends AppCompatActivity {
    int contador=0;
    final public int PERMISO_CONCEDIDO=1;
    private WebView webview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webpage);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    PERMISO_CONCEDIDO);

        }
        String url = getIntent().getExtras().getString("URL","defaultKey");
        webview = new WebView(this);


        final String usuario=GestionarPreferences.getUsuario(this);
        final String password=GestionarPreferences.getContraseña(this);

        webview.getSettings().setDomStorageEnabled(true);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.setWebChromeClient(new WebChromeClient());
        setContentView(webview);

        webview.loadUrl(url);

        webview.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

           public void onPageFinished(WebView view, String url) {

               if (contador==0) {

                  final Handler handler = new Handler();
                   handler.postDelayed(new Runnable() {
                      @Override
                      public void run() {
                           // Do something after 5s = 5000ms
                           webview.loadUrl("javascript: var usuario=document.getElementsByName(\"adAS_username\")[0].value ='" + usuario + "';");
                           webview.loadUrl("javascript: var uselessvar=document.getElementsByName(\"adAS_password\")[0].value ='"+password+"';");
                           webview.loadUrl("javascript: var uselessvar=document.getElementsByName(\"formulario1\")[0].submit();");

                       }
                   }, 2000);


                   contador++;
               }

            }

        });

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            webview.setDownloadListener(new DownloadListener() {


                @Override
                public void onDownloadStart(String url, String userAgent,
                                            String contentDisposition, String mimetype,
                                            long contentLength) {
                    DownloadManager.Request request = new DownloadManager.Request(
                            Uri.parse(url));

                    request.allowScanningByMediaScanner();
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED); //Notify client once download is completed!
                    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, URLUtil.guessFileName(
                            url, contentDisposition, mimetype));;
                    DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                    dm.enqueue(request);
                    Toast.makeText(getApplicationContext(), "Descargando archivo", //To notify the Client that the file is being downloaded
                            Toast.LENGTH_LONG).show();

                }


            });

        }

    }



    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.menu, menu);

        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id_item = item.getItemId();
        switch (id_item) {
            case R.id.cerrar_sesion:
                GestionarPreferences.guardarPrefSaltarInicio(false,WebpageActivity.this);
                getWebView().clearCache(true);
                getWebView().destroy();
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP){
                    CookieManager.getInstance().removeAllCookies(null);
                }

                startActivity(new Intent(this, MainActivity.class));
                break;
            case android.R.id.home:

                Intent i = new Intent(WebpageActivity.this, MenuPrincipalActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                finishAffinity();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public WebView getWebView() {
        return webview;
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage("¿Estás seguro de que quieres salir?")
                .setCancelable(false)
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        finishAffinity();


                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

}
