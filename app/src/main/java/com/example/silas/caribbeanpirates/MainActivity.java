package com.example.silas.caribbeanpirates;

import android.os.Bundle;

import com.example.silas.caribbeanpirates.AndGraph.AGActivityGame;
import com.example.silas.caribbeanpirates.AndGraph.AGGameManager;

public class MainActivity extends AGActivityGame {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init(this, true);

        // cria o gerenciador principal com acelera
        // vrManager = new AGGameManager(this, false);

        // MenuScene menuScene = new MenuScene(this.vrManager);
        // CreditsScene creditsScene = new CreditsScene(this.vrManager);
        PlayScene playScene = new PlayScene(this.vrManager);

        // vrManager.addScene(menuScene);
        // vrManager.addScene(creditsScene);
        vrManager.addScene(playScene);
    }
}
