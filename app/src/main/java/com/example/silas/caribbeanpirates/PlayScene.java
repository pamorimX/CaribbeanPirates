package com.example.silas.caribbeanpirates;

import com.example.silas.caribbeanpirates.AndGraph.AGGameManager;
import com.example.silas.caribbeanpirates.AndGraph.AGInputManager;
import com.example.silas.caribbeanpirates.AndGraph.AGScene;
import com.example.silas.caribbeanpirates.AndGraph.AGScreenManager;
import com.example.silas.caribbeanpirates.AndGraph.AGSoundManager;
import com.example.silas.caribbeanpirates.AndGraph.AGSprite;
import com.example.silas.caribbeanpirates.AndGraph.AGTimer;

import java.util.ArrayList;


public class PlayScene extends AGScene {
    // Cria o vetor de tiros
    ArrayList<AGSprite> vetorTiros = null;
    AGSprite[] navios = new AGSprite[2];

    // Cria o tempo para controle movimento canhao
    AGTimer tempoCanhao = null;
    AGTimer tempoBala = null;

    // cria a variavel para armazenar o cod efeito som
    int efeitoCatraca = 0;
    int efeitoExplosao = 0;

    AGSprite planoFundo = null;
    AGSprite canhao = null;

    public PlayScene(AGGameManager vrGerente) {
        super(vrGerente);
    }

    @Override
    public void init() {
        // Carrega a imagem na memoria
        createSprite(R.drawable.bala, 1, 1).bVisible = false;

        vetorTiros = new ArrayList<AGSprite>();

        // Seta a cor do fundo
        setSceneBackgroundColor(1, 1, 1);

        // Carrega a imagem de fundo 100x100 centro da tela
        planoFundo = createSprite(R.drawable.textmar, 1, 1);
        planoFundo.setScreenPercent(100, 100);
        planoFundo.vrPosition.setX(AGScreenManager.iScreenWidth / 2);
        planoFundo.vrPosition.setY(AGScreenManager.iScreenHeight / 2);

        // Carrega a imagem do canhao na base da tela
        canhao = createSprite(R.drawable.canhao, 1, 1);
        canhao.setScreenPercent(12, 20);
        canhao.vrPosition.setX(AGScreenManager.iScreenWidth / 2);
        canhao.vrPosition.setY(canhao.getSpriteHeight() / 2);

        tempoCanhao = new AGTimer(100);
        tempoBala = new AGTimer(500);
        efeitoCatraca = AGSoundManager.vrSoundEffects.loadSoundEffect("toc.wav");
        efeitoExplosao = AGSoundManager.vrSoundEffects.loadSoundEffect("explodenavio.wav");

        // Carrega os sprites dos navios
        navios[0] = createSprite(R.drawable.navio, 1, 1);
        navios[0].setScreenPercent(20, 12);
        navios[0].iMirror = AGSprite.HORIZONTAL;
        navios[0].vrDirection.fX = 1;
        navios[0].vrPosition.fX = -navios[0].getSpriteWidth() / 2;
        navios[0].vrPosition.fY = AGScreenManager.iScreenHeight - navios[0].getSpriteHeight() / 2;

        navios[1] = createSprite(R.drawable.navio, 1, 1);
        navios[1].setScreenPercent(20, 12);
        navios[1].vrDirection.fX = -1;
        navios[1].vrPosition.fX = AGScreenManager.iScreenWidth + navios[1].getSpriteWidth() / 2;
        navios[1].vrPosition.fY = navios[0].vrPosition.fY - navios[1].getSpriteHeight();
    }

    @Override
    public void restart() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void loop() {
        if (AGInputManager.vrTouchEvents.backButtonClicked()) {
            vrGameManager.setCurrentScene(0);
            return;
        }

        atualizaMovimentoCanhao();
        atualizaBalas();
        criaTiro();
        atualizaNavios();
        verificaColisaoBalasNavios();
    }

    // Metodo que verifica a colisao entre b alas e navios
    private void verificaColisaoBalasNavios() {
        for (AGSprite bala : vetorTiros) {
            if (bala.bRecycled) {
                continue;
            }
            for (AGSprite navio : navios) {
                if (bala.collide(navio)) {
                    bala.bRecycled = true;
                    bala.bVisible = false;
                    AGSoundManager.vrSoundEffects.play(efeitoExplosao);

                    if (navio.vrDirection.fX == 1) {
                        navio.vrDirection.fX = -1;
                        navio.iMirror = AGSprite.NONE;
                        navio.vrPosition.fX = AGScreenManager.iScreenWidth + navio.getSpriteWidth() / 2;
                    } else {
                        navio.vrDirection.fX = 1;
                        navio.iMirror = AGSprite.HORIZONTAL;
                        navio.vrPosition.fX = -navio.getSpriteWidth();
                    }
                    break;
                }
            }

        }
    }

    // Metodo que atualiza a posicao dos navios
    private void atualizaNavios() {
        for (AGSprite navio : navios) {
            navio.vrPosition.fX += 5 * navio.vrDirection.fX;
            if (navio.vrDirection.fX == 1) {
                if (navio.vrPosition.fX > AGScreenManager.iScreenWidth + navio.getSpriteWidth() / 2) {
                    navio.iMirror = AGSprite.NONE;
                    navio.vrDirection.fX = -1;
                }
            } else {
                if (navio.vrPosition.fX <= -navio.getSpriteWidth() / 2) {
                    navio.iMirror = AGSprite.HORIZONTAL;
                    navio.vrDirection.fX = 1;
                }
            }
        }
    }

    // Coloca uma bala no vetor de tiros
    private void criaTiro() {
        tempoBala.update();

        // Tenta reciclar uma bala criada anteriormente
        if (AGInputManager.vrTouchEvents.screenClicked()) {
            if (!tempoBala.isTimeEnded()) {
                return;
            }

            tempoBala.restart();

            for (AGSprite bala : vetorTiros) {
                if (bala.bRecycled) {
                    bala.bRecycled = false;
                    bala.bVisible = true;
                    bala.vrPosition.fX = canhao.vrPosition.fX;
                    bala.vrPosition.fY = canhao.getSpriteHeight() + bala.getSpriteHeight() / 2;
                    return;
                }
            }

            AGSprite novaBala = createSprite(R.drawable.bala, 1, 1);
            novaBala.setScreenPercent(8, 5);
            novaBala.vrPosition.fX = canhao.vrPosition.fX;
            novaBala.vrPosition.fY = canhao.getSpriteHeight() + novaBala.getSpriteHeight() / 2;
            vetorTiros.add(novaBala);
        }
    }

    // metodo para atualizar o movimento das balas
    private void atualizaBalas() {
        for (AGSprite bala : vetorTiros) {
            bala.vrPosition.fY += 10;

            if (bala.vrPosition.fY > AGScreenManager.iScreenHeight + bala.getSpriteHeight() / 2) {
                bala.bRecycled = true;
                bala.bVisible = false;
            }
        }
    }

    // Metodo criado para movimentar
    private void atualizaMovimentoCanhao() {
        tempoCanhao.update();
        if (tempoCanhao.isTimeEnded()) {
            tempoCanhao.restart();
            if (AGInputManager.vrAccelerometer.getAccelX() > 2.0f) {
                if (canhao.vrPosition.getX() <= AGScreenManager.iScreenWidth - canhao.getSpriteWidth() / 2) {
                    AGSoundManager.vrSoundEffects.play(efeitoCatraca);
                    canhao.vrPosition.setX(canhao.vrPosition.getX() + 10);
                }
            } else if (AGInputManager.vrAccelerometer.getAccelX() < -2.0f) {
                AGSoundManager.vrSoundEffects.play(efeitoCatraca);
                canhao.vrPosition.setX(canhao.vrPosition.getX() - 10);
            }
        }
    }
}
