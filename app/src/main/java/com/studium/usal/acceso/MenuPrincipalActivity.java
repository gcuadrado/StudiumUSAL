package com.studium.usal.acceso;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.CookieManager;
import android.widget.EditText;

public class MenuPrincipalActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);
    }

    public void acceso(View view) {
        String url;
        int tocado=view.getId();
        if (tocado==R.id.studium){
            url="https://moodle2.usal.es/login/index.php";
        } else{
            url="https://portal.usal.es/portal/page/portal/servicios/ac/notas";
        }
        Intent i = new Intent(MenuPrincipalActivity.this, WebpageActivity.class);
        i.putExtra("URL", url);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
        finishAffinity();
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
                GestionarPreferences.guardarPrefSaltarInicio(false,MenuPrincipalActivity.this);
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP){
                    CookieManager.getInstance().removeAllCookies(null);
                }
                startActivity(new Intent(this, MainActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
