package com.example.silas.caribbeanpirates;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.example.silas.caribbeanpirates.AndGraph.AGActivityGame;

public class MainActivity extends AGActivityGame {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_main);
        //Inicia o motor... o segundo parametro está relacionado ao acelerometro.. (false = desligado, true = ligado)
        init(this, false);
        MenuScene menuScene = new MenuScene(this.vrManager);
        CreditsScene creditsScene = new CreditsScene(this.vrManager);
        vrManager.addScene(menuScene);
        vrManager.addScene(creditsScene);
    }
}
