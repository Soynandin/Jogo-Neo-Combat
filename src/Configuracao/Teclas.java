package Configuracao;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

// Classe que implementa a interface KeyListener para capturar eventos de teclado
public class Teclas implements KeyListener {
    // Array boolean que armazena o estado das teclas
    private boolean[] teclas;

    // Variáveis booleanas para movimento e ataque do jogador 1
    public boolean cima, baixo, esquerda, direita;
    public boolean G, H, J;

    // Variáveis booleanas para movimento e ataque do jogador 2
    public boolean cima1, baixo1, esquerda1, direita1;
    public boolean N1, N2, N3;

    // Construtor inicializa o array de teclas com 256 posições
    public Teclas() {
        teclas = new boolean[256];
    }

    // Método que atualiza o estado das teclas baseado no array de teclas
    public void atualizaTeclas() {
        // Movimento do Jogador 1
        cima = teclas[KeyEvent.VK_W];        // W para cima
        baixo = teclas[KeyEvent.VK_S];       // S para baixo
        esquerda = teclas[KeyEvent.VK_A];    // A para esquerda
        direita = teclas[KeyEvent.VK_D];     // D para direita

        // Ataque do Jogador 1
        G = teclas[KeyEvent.VK_G];           // G para ataque 1
        H = teclas[KeyEvent.VK_H];           // H para ataque 2
        J = teclas[KeyEvent.VK_J];           // J para ataque 3

        // Movimento do Jogador 2
        cima1 = teclas[KeyEvent.VK_UP];      // Seta para cima
        baixo1 = teclas[KeyEvent.VK_DOWN];   // Seta para baixo
        esquerda1 = teclas[KeyEvent.VK_LEFT]; // Seta para esquerda
        direita1 = teclas[KeyEvent.VK_RIGHT]; // Seta para direita

        // Ataque do Jogador 2
        N1 = teclas[KeyEvent.VK_NUMPAD1];    // Numpad 1 para ataque 1
        N2 = teclas[KeyEvent.VK_NUMPAD2];    // Numpad 2 para ataque 2
        N3 = teclas[KeyEvent.VK_NUMPAD3];    // Numpad 3 para ataque 3
    }

    // Tecla é pressionada
    @Override
    public void keyPressed(KeyEvent e) {
        teclas[e.getKeyCode()] = true;
    }

    // Tecla é liberada
    @Override
    public void keyReleased(KeyEvent e) {
        teclas[e.getKeyCode()] = false;
    }

    // Tecla é pressionada e liberada rapidamente (não utilizado aqui)
    @Override
    public void keyTyped(KeyEvent e) {
        // Não é necessário implementar neste caso
    }
}