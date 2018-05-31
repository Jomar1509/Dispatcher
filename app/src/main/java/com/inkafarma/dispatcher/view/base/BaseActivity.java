package com.inkafarma.dispatcher.view.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.inkafarma.dispatcher.R;

import butterknife.ButterKnife;

/**
 * <p>
 * BaseActivity contiene algunas modificaciones de la class nativa  AppCompatActivity.
 * Principalmente, utiliza ButterKnife para ver vinculante y verifica automaticamente
 * si la barra de herramientas existe.
 * toolbar exists.
 * </p>
 */

public abstract class BaseActivity extends AppCompatActivity {
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        bindViews();
        initView(savedInstanceState);
    }

    /**
     * Este metodo usado para inicializar los componentes de vista, este metodo se llama despues {@link
     * BaseActivity#bindViews()}.
     */

    public void initView(Bundle savedInstanceState) {
    }

    /**
     * Su uso comun es una barra de herramientas dentro de la actividad, si existe en el
     * diseño  esto sera configurado.
     */
    public void setupToolbar(String labelToolbar) {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
        }
    }


    private void bindViews() {
        ButterKnife.bind(this);
    }

    @Nullable
    public Toolbar getToolbar() {
        return mToolbar;
    }

    /**
     * @return El Id de diseño que va a ser la vista de actividad.
     */
    protected abstract int getLayoutId();

}
