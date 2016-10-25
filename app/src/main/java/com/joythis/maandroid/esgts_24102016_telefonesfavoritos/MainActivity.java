package com.joythis.maandroid.esgts_24102016_telefonesfavoritos;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.content.Context;

import java.util.HashSet;
import java.util.Set;
//import java.util.jar.Manifest;
import android.Manifest;

public class MainActivity extends AppCompatActivity {
    Context mContext;
    LinearLayout mLlTelefonesFavoritos;
    /*
    cuidado com a grandeza deste número
    tem que ser de 16 bits (até 32768) em android <7
     */

    static final int TELEFONES_FAVORITOS_CODE = 123;
    static final int NUM_DIGITOS_DUM_TELEFONE_PORTUGUES = 9;


    //declarar membros de dados
    Button mBtnEntrarTelefoneFavorito;
    EditText mEtEntrarTelefoneFavorito;

    Set<Long> mSetTelefonesFavoritos;

    View.OnClickListener mTratadorDeClicksNoButtonParaEntrarTelefoneFavorito;
    //---------------------------

    public boolean utilPedirQualquerPermissaoEmRuntime(
            String strPermissao // a permissao que pretendemos pedir

    ) {

        //XML : android.permission.CALL_PHONE;
        //JAVA : Manifest.permission.CALL_PHONE;
        /*

        começar por verificar se em ocasiões anteriores o user já
        não autorizou a app para a permissão em causa

         */
        int iResultadoDaConsulta =
                ContextCompat.checkSelfPermission(
                        mContext,
                        strPermissao
                );
        boolean bAutorizada =
                iResultadoDaConsulta == PackageManager.PERMISSION_GRANTED;

        if (bAutorizada) {
            //app ja estava previamente autorizada
            return true;
        }//if
        else {
            //app não estava previamente autorizada
            try {
                ActivityCompat.requestPermissions(
                        MainActivity.this, //android.app.Activity
                        new String[]{strPermissao},// array de permissões
                        TELEFONES_FAVORITOS_CODE //codigo numérico da nossa app

                );

                //TODO: implementar um callback

                return true; //
            } catch (Exception e) {

                String strErroAoPedirPermisoes = e.getMessage().toString();
                mUtilFeedback(strErroAoPedirPermisoes);
                return false; //fomoes incapazes de fazer o pedido
            }//catch
        }//else

    }//utilPedirQualquerPermissaoEmRuntime

    //---------------------------
    public boolean utilMarcarNumeroDeTelefone(String strNumber) {
        return utilDialPhoneNumber(strNumber);
    }

    public boolean utilDialPhoneNumber(
            String strNumero //string correspondente ao nº ex 112
    ) {
        /*o código abaixo serviu apenas para explicitar paralelos entre
          abrir um URL externamente e marcar um nº Telefone
        */

        //android.content.Intent
        Intent abrirUrlComoOSistema = new Intent(Intent.ACTION_VIEW);
        //dados? que Url abrir?
        //java.net.Url
        Uri url = Uri.parse("http://www.dn.pt");
        abrirUrlComoOSistema.setData(url);
        abrirUrlComoOSistema.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        /*
        confere uma "Activity Stack" própra ao componente
         */
        startActivity(abrirUrlComoOSistema);
        Intent telefonar = new Intent(Intent.ACTION_CALL);
        Uri dadosParaTelefonar = Uri.parse("tel:" + strNumero);
        telefonar.setData(dadosParaTelefonar);
        /*desde Android -5.0 que as permissões podem ser alteradas/aceites/rejeitadas
        pelo utilizador em runtime; ou seja, não basta em design-time expressá-las em
        AndroidManifest.xml

        */
        //android.permission.CALL_PHONE
        try {
            startActivity(telefonar);
            return true;
        }//try
        catch (SecurityException e) {
            mUtilFeedback("Sem permissões para telefonar.");
            return false;
        }//catch
    }//utilDialPhoneNumber


    void personallizardEditTextParaEntradaDeInteiros(
            EditText et, //Edittext que será personalizdo
            int iMaxDigitos //nº max de digitos a aceitar no Edittext
    ) {
        //et.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS); //aceita tudo
        //et.setInputType(InputType.TYPE_CLASS_PHONE); //digitos e ainda ceita virgulas
        // et.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);//só aceita digitos 09

        //TODO: Tentar manter NO_SUGGESTIONS mas ainda assim só aceitar digitos

        /*
        import android.text.InputFilter;
        queremos um filtro, que filtro? Um que estabeleça o tamanho max do input
         */
        InputFilter[] filtros = new InputFilter[0];
        filtros[0] = new InputFilter.LengthFilter(iMaxDigitos);
        et.setFilters(filtros);
    }

    void mUtilFeedback(String mensagem) {
        //por defeito duração LONG

        Toast t = Toast.makeText(
                mContext,
                mensagem,
                Toast.LENGTH_LONG
        );
        t.show();

    }//mUtilFeedback

    void mUtilFeedback(String mensagem, int duracao) {
        duracao = duracao < 30 ? Toast.LENGTH_SHORT : Toast.LENGTH_LONG;
        switch (duracao) {
            case Toast.LENGTH_SHORT:
                Toast t = Toast.makeText(
                        mContext,
                        mensagem,
                        Toast.LENGTH_SHORT
                );
                t.show();
                break;
            default:
        }

    }//mUtilFeedback


    boolean adicionarTelefoneFavorito() {

        /*1) consultar a EditText que tem o telefone a adicionar
        garanting que é um inteiro, se tudo estiver ok, guardar no Set
        */
        String strTelCandidato = mEtEntrarTelefoneFavorito.getText().toString();
        try {
            Long novoTelefone = Long.parseLong(strTelCandidato);// consultar
            mSetTelefonesFavoritos.add(novoTelefone); //guardar no Set
            return true; //se acontecesse estaria errado : ainda há mais para fazer
        }//try
        catch (Exception e) {
            String strCorreuMalPorque = e.getMessage().toString();
            mUtilFeedback(strCorreuMalPorque);
             /* 2) como o Set ficará modificado, se houver elementos de interface que
              dependam dos seus valores, esses elementos deveriam ser atualizados
              em runtime
         */
        }//catch


        //TODO: ainda não temos interface para mudar
        /*
        ja temos interface (mLlTelefonesFavoritos)
         */
        atualizarButtonsParaTelefonar();

        return false;
    }//adicionarTelefoneFavorito

    void atualizarButtonsParaTelefonar() {
        Long[] numerosDeTelefoneEnquantoArrayDeLongs = mSetTelefonesFavoritos.toArray(
                new Long[mSetTelefonesFavoritos.toArray().length]
        );

        for (
                Long n : numerosDeTelefoneEnquantoArrayDeLongs
                ) {
            Button b = new Button(mContext);
            mLlTelefonesFavoritos.add(b);
            b.setOnClickListener(metodoQueRespondeAClicksEmNumerosDeTelefone);

        }
    }//atualizarButtonsParaTelefonar

    View.OnClickListener metodoQueRespondeAClicksEmNumerosDeTelefone = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            if (view instanceof Button) {
                Button b = (Button) view;
                String numero = b.getText().toString();
                utilDialPhoneNumber(numero);
            }//if

        }//onClick


    };

    void init() {
        mContext = MainActivity.this;
        utilPedirQualquerPermissaoEmRuntime(Manifest.permission.CALL_PHONE);

        //não podemos fazer new Set<Long>() porque Set é uma classe abstracta

        mSetTelefonesFavoritos = new HashSet<Long>();
        mBtnEntrarTelefoneFavorito = (Button) findViewById(R.id.id_btnEntrarTelefoneFavorito);
        mEtEntrarTelefoneFavorito = (EditText) findViewById(R.id.id_etEntrarTelefoneFavorito);
        mLlTelefonesFavoritos = (LinearLayout) findViewById(R.id.id_llTelefoneFavorito);
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

        //utilDialPhoneNumber("123");
    }//init

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }//onCreate
}//MainActivity
