package com.joythis.maandroid.esgts_24102016_telefonesfavoritos;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    static final int NUM_DIGITOS_DUM_TELEFONE_PORTUGUES = 9;
    //declarar membros de dados
    Button mBtnEntrarTelefoneFavorito;
    EditText mEtEntrarTelefoneFavorito;

    Set<Long> mSetTelefonesFavoritos;

    View.OnClickListener mTratadorDeClicksNoButtonParaEntrarTelefoneFavorito;

    mTratadorDeClicksNoButtonParaEntrarTelefoneFavorito=new View.OnClickListener()

    {

    }


    void personallizardEditTextParaEntradaDeInteiros(
            EditText et, //Edittext que será personalizdo
            int iMaxDigitos //nº max de digitos a aceitar no Edittext
    ) {
        //et.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS); //aceita tudo
        //et.setInputType(InputType.TYPE_CLASS_PHONE); //digitos e ainda ceita virgulas
        et.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);//só aceita digitos 09

        //TODO: Tentar manter NO_SUGGESTIONS mas ainda assim só aceitar digitos

        /*
        import android.text.InputFilter;
        queremos um filtro, que filtro? Um que estabeleça o tamanho max do input
         */
        InputFilter[] filtros = new InputFilter[];
        filtros[0] = new InputFilter.LengthFilter(iMaxDigitos);
        et.setFilters(filtros);
    }

    boolean adicionarTelefoneFavorito() {

        /*1) consultar a EditText que tem o telefone a adicionar
        garanting que é um inteiro, se tudo estiver ok, guardar no Set
        */
        String strTelCandidato = mEtEntrarTelefoneFavorito.getText().toString();
        try {
            Long novoTelefone = Long.parseLong(strTelCandidato);// consultar
            mSetTelefonesFavoritos.add(novoTelefone); //guardar no Set
            //return true; //se acontecesse estaria errado : ainda há mais para fazer
        }//try
        catch (Exception e) {
            String strCorreuMalPorque = e.getMessage().toString();
            mUtilFeedback(strCorreuMalPorque);
            return false;
        }//ctach

        /*
        2) como o Set ficará modificado, se houver elementos de interface que
        dependam dos seus valores, esses elementos deveriam ser atualizados
        em runtime
         */

        //TODO: ainda não temos interface para mudar

    }//adicionarTelefoneFavorito

    void init() {
        //não podemos fazer new Set<Long>() porque Set é uma classe abstracta

        mSetTelefonesFavoritos = new HashSet<Long>();
        mBtnEntrarTelefoneFavorito = (Button) findViewById(R.id.id_btnEntrarTelefoneFavorito);
        mEtEntrarTelefoneFavorito = (EditText) findViewById(R.id.id_etEntrarTelefoneFavorito);
        personallizardEditTextParaEntradaDeInteiros(
                mEtEntrarTelefoneFavorito,
                NUM_DIGITOS_DUM_TELEFONE_PORTUGUES
        );
        mTratadorDeClicksNoButtonParaEntrarTelefoneFavorito = new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                adicionarTelefoneFavorito();

            }//onClick

        };//mTratadorDeClicksNoButtonParaEntrarTelefoneFavorito
        mBtnEntrarTelefoneFavorito.setOnClickListener(mTratadorDeClicksNoButtonParaEntrarTelefoneFavorito);
    }//init

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }//onCreate
}//MainActivity
