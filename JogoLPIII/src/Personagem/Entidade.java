package Personagem;

import java.awt.Graphics;

public abstract class Entidade {
	// every creature has an x,y
	protected float x, y;

	// constructor
	public Entidade(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	// update
	public abstract void tick();
	
	// draw
	public abstract void render(Graphics g);	
}

