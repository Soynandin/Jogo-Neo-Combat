package Estados;

import java.awt.Graphics;
import java.awt.Rectangle;

import Aplicacao.Jogo;


public abstract class Estado {

	// init State var
	private static Estado currState = null;
	
	// setter for State
	public static void setState(Estado state) {
		currState = state;
	}
	
	// getter for State
	public static Estado getState() {
		return currState;
	}
	
	// init Game var
	protected Jogo jogo;
	
	// constructor
	public Estado(Jogo jogo) {
		this.jogo = jogo;
	}
	
	// nethods that all extended classes will share...
	public abstract void tick();
	
	public abstract void render(Graphics g);
	
	public abstract void music();
		
	// hitbox methods
	public abstract Rectangle getRaposaRangeHit();	
	public abstract Rectangle getCavaleiroRangeHit();	
	public abstract Rectangle getRaposaAtaqueHit();
	public abstract Rectangle getCavaleiroAtaqueHit();
	public abstract int getRaposaX();	
	public abstract int getCavaleiroX();
}
