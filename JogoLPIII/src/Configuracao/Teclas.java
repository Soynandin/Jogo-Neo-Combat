package Configuracao;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Teclas implements KeyListener {
	private boolean[] teclas;
	public boolean cima, baixo, esquerda, direita;
	public boolean cima1, baixo1, esquerda1, direita1;
	
	public boolean G, H, J;
	public boolean N1, N2, N3;

	
	public Teclas() {
		teclas = new boolean[256];
	}
	
	public void atualizaTeclas() {		
		// movement P1
		cima    = teclas[KeyEvent.VK_W];
		baixo  = teclas[KeyEvent.VK_S];
		esquerda  = teclas[KeyEvent.VK_A];
		direita = teclas[KeyEvent.VK_D];
		
		// attack P1
		G = teclas[KeyEvent.VK_G];
		H = teclas[KeyEvent.VK_H];
		J = teclas[KeyEvent.VK_J];
		
		// movement P2
		cima1    = teclas[KeyEvent.VK_UP];
		baixo1  = teclas[KeyEvent.VK_DOWN];
		esquerda1  = teclas[KeyEvent.VK_LEFT];
		direita1 = teclas[KeyEvent.VK_RIGHT];
		
		// attack P2
		N1 = teclas[KeyEvent.VK_NUMPAD1];
		N2 = teclas[KeyEvent.VK_NUMPAD2];
		N3 = teclas[KeyEvent.VK_NUMPAD3];
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		teclas[e.getKeyCode()] = true;
	}
	
	@Override
	public void keyReleased(KeyEvent e) {
		teclas[e.getKeyCode()] = false;
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
		
	}
}
