package Personagem;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import Aplicacao.Jogo;
import ManipularImagem.Animacao;
import ManipularImagem.Imagens;

public class Raposa extends Posicao {

	private int vida;
	private int velX, velY;
	private Jogo jogo;

	private final int parada = 0;
	private final int correrDireita = 1;
	private final int correrEsquerda = 2;
	private final int abaixar = 3;
	private final int levaDano = 6;
	private final int pular = 9;
	private final int pularDireita = 10;
	private final int pularEsquerda = 11;

	private final int atacar1 = 4;
	private final int atacar2 = 5;
	private final int atacar3 = 7;
	
	//private final int ferido = 15;
	private final int verifica_fig = 19;
	private final int gravidade = 1;
	private final int velocidade = 2;
	private final int velocidadePulo = -10;

	private boolean[] animacoes = new boolean[20];

	private Animacao raposaParada;
	private Animacao raposaCorrendoDireita;
	private Animacao raposaCorrendoEsquerda;
	private Animacao raposaPulando;
	private Animacao raposaPulandoDireita;
	private Animacao raposaPulandoEsquerda;
	private Animacao raposaAbaixada;
	private Animacao raposaAtacando;
	private Animacao raposaAtacando2;
	private Animacao raposaAtacando3;
	private Animacao raposaLevaDano;
	//private Animacao raposaMorrendo;

	private boolean atingida;
	private long invulneravel;

	Random rand;

	public Raposa(Jogo jogo, float x, float y) {
		super(x, y);

		this.jogo = jogo;
		rand = new Random();

		vida = 100;
		
		raposaParada 			= new Animacao(100, Imagens.p1_parado);
		raposaCorrendoDireita	= new Animacao(100, Imagens.p1_correrDireita);
		raposaCorrendoEsquerda 	= new Animacao(100, Imagens.p1_correrEsquerda);
		raposaPulando			= new Animacao(100, Imagens.p1_pularDireita);
		raposaPulandoDireita 	= new Animacao(100, Imagens.p1_pularDireita);
		raposaPulandoEsquerda 	= new Animacao(100, Imagens.p1_pularEsquerda);
		raposaAbaixada 			= new Animacao(100, Imagens.p1_abaixar);
		raposaAtacando 			= new Animacao(100, Imagens.p1_ataque_g);
		raposaAtacando2 		= new Animacao(100, Imagens.p1_ataque_h);
		raposaAtacando3			= new Animacao(100, Imagens.p1_ataque_j);
		raposaLevaDano 			= new Animacao(100, Imagens.p1_levaDano);
		//raposaMorrendo 			= new Animacao(100, Imagens.p1_morrendo);
	}

	@Override
	public void tick() {
		// TODO Auto-generated method stub
		raposaParada.atualizacaoEstados();
		raposaAbaixada.atualizacaoEstados();

		raposaCorrendoDireita.atualizacaoEstados();
		raposaCorrendoEsquerda.atualizacaoEstados();

		if (animacoes[atacar1])
			raposaAtacando.atualizacaoEstados();
		if (animacoes[atacar2])
			raposaAtacando2.atualizacaoEstados();
		if (animacoes[atacar3])
			raposaAtacando3.atualizacaoEstados();
		
		if (y == 280) {

			if (jogo.getTeclas().esquerda && !jogo.getTeclas().cima) {
				velX = -2;
				pegaAnimacao(correrEsquerda);
			} 
			
			else if (jogo.getTeclas().direita && !jogo.getTeclas().cima) {
				velX = 2;
				pegaAnimacao(correrDireita);
			} 
			
			else if (jogo.getTeclas().cima && !jogo.getTeclas().direita && !jogo.getTeclas().esquerda
					&& !animacoes[pular]) {
				// jump
				velY = velocidadePulo - 2;
				y -= 1;
				raposaPulando.i = 0;
			} 
			
			else if (jogo.getTeclas().cima && jogo.getTeclas().direita && !jogo.getTeclas().esquerda
					&& !animacoes[pularDireita]) {
				velY = velocidadePulo;
				velX = 2;
				y -= 1;
				raposaPulandoDireita.i = 0;
			} 
			
			else if (jogo.getTeclas().cima && !jogo.getTeclas().direita && jogo.getTeclas().esquerda
					&& !animacoes[pularEsquerda]) {
				velY = velocidadePulo;
				velX = -2;
				y -= 1;
				raposaPulandoEsquerda.i = 0;
			} 
			
			else if (!jogo.getTeclas().direita && !jogo.getTeclas().esquerda && jogo.getTeclas().baixo) {
				velX = 0;
				velY = 0;
				
				pegaAnimacao(abaixar);

				// if pressing g while crouching
				if (jogo.getTeclas().G) {
					velX = 0;
					velY = 0;

					// for punching frames, deactivate crouch
					animacoes[abaixar] = false;

					// reset and init true to state, punch
					if (testeCorrendo())
						controlarAnimacao(atacar1);

				}
				
				if (jogo.getTeclas().H) {
					velX = 0;
					velY = 0;

					// for punching frames, deactivate crouch
					animacoes[abaixar] = false;

					// reset and init true to state, punch
					if (testeCorrendo())
						controlarAnimacao(atacar2);

				}
				
				if (jogo.getTeclas().J) {
					velX = 0;
					velY = 0;

					// for punching frames, deactivate crouch
					animacoes[abaixar] = false;

					// reset and init true to state, punch
					if (testeCorrendo())
						controlarAnimacao(atacar3);

				}
				
				resetAnim(raposaAtacando, atacar1);
				resetAnim(raposaAtacando2, atacar2);
				resetAnim(raposaAtacando3, atacar3);
			} 
			
			else if (jogo.getTeclas().G && !animacoes[abaixar]) {
				velX = 0;
				velY = 0;
				if (testeCorrendo())
					pegaAnimacao(atacar1);
			}
			
			else if (jogo.getTeclas().H && !animacoes[abaixar]) {
				velX = 0;
				velY = 0;
				if (testeCorrendo())
					pegaAnimacao(atacar2);
			}
			
			else if (jogo.getTeclas().J && !animacoes[abaixar]) {
				velX = 0;
				velY = 0;
				if (testeCorrendo())
					pegaAnimacao(atacar3);
			}
			
			else {
				velX = 0;
				velY = 0;

				// reset anims that are instantaneous (only activate when pressed)
				animacoes[abaixar] = false;
				animacoes[correrDireita] = false;
				animacoes[correrEsquerda] = false;
				animacoes[pular] = false;
				animacoes[pularDireita] = false;
				animacoes[pularEsquerda] = false;

				if (animacoes[verifica_fig]) {
					pegaAnimacao(parada);
				}
			}

		} else {
			animacoes[correrDireita] = false;
			animacoes[correrEsquerda] = false;

			raposaPulando.atualizacaoEstados();
			raposaPulandoDireita.atualizacaoEstados();
			raposaPulandoEsquerda.atualizacaoEstados();

			// if moving left
			if (velX < 0) {
				// back flip
				animacao_ataqueAereo(raposaPulandoEsquerda, pularEsquerda);
				// if moving right
			} else if (velX > 0) {
				// front flip
				animacao_ataqueAereo(raposaPulandoDireita, pularDireita);
				// otherwise
			} else {
				// jump vertically
				animacao_ataqueAereo(raposaPulando, pular);
			}
		}

		if (animacoes[atacar1] || animacoes[atacar2] || animacoes[atacar3]) {
			velX = 0;
			velY = 0;
		}

		// reset ground attacks
		resetAnim(raposaAtacando, atacar1);
		resetAnim(raposaAtacando2, atacar2);
		resetAnim(raposaAtacando3, atacar3);
		
		collisions();
		
		if (atingida) {	
			if (System.currentTimeMillis() - invulneravel > 400) {
				animacoes[levaDano] = false;
				atingida = false;
				invulneravel += 400;
			}
		}
		// update horizontal pos.
		x += velX;

		// if in air, fall
		if (y < 280)
			cair();

		if (y > 280)
			y = 280;
		
		verificaParede();

	}

	public void resetAnim(Animacao anim, int frame) {
		// if called anim is played once...
		if (anim.getReproducao()) {
			// set anim to false
			anim.setReproducao();
			animacoes[frame] = false;
		}

	}
	
	public void cair(){

		// update y speed
		velY += gravidade;

		// if greater than max speed, just equal max speed
		if (velY > velocidade){
			velY = velocidade;
		} 

		// if moving left...		
		if (velX < 0) {
			// backflip
			if (raposaPulandoEsquerda.getIndex() >= 2 && raposaPulandoEsquerda.getIndex() <= 3) {
				velY = 0;
			}
		}

		// if moving right...
		if (velX > 0) {
			// front flip
			if (raposaPulandoDireita.getIndex() >= 2 && raposaPulandoDireita.getIndex() <= 3) {
				velY = 0;
			}
		}

		// if horizontally still...
		if (velX == 0) {
			// jump anims
			if (raposaPulando.getIndex() >= 3 && raposaPulando.getIndex() <= 4) {
				velY = 0;
			}
		}

		// update y by y-speed
		y += velY;


	}
	
	public void collisions() {

		// if rectangle of player collides with enemy rectangle
		if (jogo.getGameEstado().getCavaleiroAtaqueHit().intersects(getPersonagemRange())) {

			// if not already hurting...
			if (!atingida) {
				controlarAnimacao(levaDano);
				invulneravel = System.currentTimeMillis();
				atingida = true;
				vida-=3;
			}

			// freeze..

			try {
				TimeUnit.MILLISECONDS.sleep(30);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		} 

	}

	@Override
	public void render(Graphics g) {// ------------------------------------parei aqui
		// TODO Auto-generated method stub

		// 2d graphics for transformations
		Graphics2D g2d = (Graphics2D) g;

		// when hit, vibrate randomly
		if (atingida) {
			int k = rand.nextInt(3);
			g2d.translate(-k, k);
		}
		// draw shadow
		g.setColor(new Color(0, 0, 0, 125));
		g.fillOval((int) x + 30, 200 * Jogo.escala, 64, 16);

		if (animacoes[correrDireita])
			g.drawImage(getCurrentAnimFrame(), (int) (x - 9), (int) (y - 3), null);

		else if (animacoes[correrEsquerda])
			g.drawImage(getCurrentAnimFrame(), (int) (x - 4), (int) y, null);

		else if (animacoes[abaixar])
			g.drawImage(getCurrentAnimFrame(), (int) x, (int) y + 50, null);

		else if (animacoes[pular])
			g.drawImage(getCurrentAnimFrame(), (int) x - 3, (int) y - 20, null);

		else if (animacoes[pularDireita])
			g.drawImage(getCurrentAnimFrame(), (int) x - 15, (int) y - 15, null);

		else if (animacoes[pularEsquerda])
			g.drawImage(getCurrentAnimFrame(), (int) x - 15, (int) y - 15, null);

		else if (animacoes[atacar1])
			g.drawImage(getCurrentAnimFrame(), (int) x+3, (int) (y + 3), null);
		
		else if (animacoes[atacar2])
			g.drawImage(getCurrentAnimFrame(), (int) x, (int) (y + 3), null);
		
		else if (animacoes[atacar3])
			g.drawImage(getCurrentAnimFrame(), (int) x+3, (int) (y + 3), null);
		
		else if (animacoes[levaDano])
			g.drawImage(getCurrentAnimFrame(), (int) (x - 15), (int) (y + 1), null);
		
		else
			g.drawImage(getCurrentAnimFrame(), (int) x, (int) y, null);
	}

	private BufferedImage getCurrentAnimFrame() {

		if (animacoes[correrDireita])
			return raposaCorrendoDireita.getFrames();

		else if (animacoes[correrEsquerda])
			return raposaCorrendoEsquerda.getFrames();

		else if (animacoes[abaixar])
			return raposaAbaixada.getFrames();

		else if (animacoes[pular])
			return raposaPulando.getFrames();

		else if (animacoes[pularDireita])
			return raposaPulandoDireita.getFrames();

		else if (animacoes[pularEsquerda])
			return raposaPulandoEsquerda.getFrames();

		else if (animacoes[atacar1])
			return raposaAtacando.getFrames();
		
		else if (animacoes[atacar2])
			return raposaAtacando2.getFrames();
		
		else if (animacoes[atacar3])
			return raposaAtacando3.getFrames();
		
		else if (animacoes[levaDano])
			return raposaLevaDano.getFrames();
		
		else
			return raposaParada.getFrames();

	}

	public void pegaAnimacao(int unchanged) {

		// make all false...
		for (int i = 0; i < 19; i++) {
			animacoes[i] = false;
		}

		// except active anim
		animacoes[unchanged] = true;
	}

	public void verificaParede() {
		// if x < 0...
		if (x < -30) {
			// do not go past left edge
			x = -30;
		}

		// if x > 0
		if (x > Jogo.largura + 150) {
			// do not go past right edge
			x = Jogo.largura + 150;
		}
	}

	public boolean testeCorrendo() {

		// counter
		int i = 0;

		// traverse through anims
		for (boolean b : animacoes) {
			// if false, increment counter
			if (b == false)
				i++;
		}

		// if all anims are false, return false
		if (i == 19) {
			return false;
		}

		// otherwise, true
		return true;
	}

	public Rectangle getPersonagemRange() {

		if (animacoes[abaixar])
			return new Rectangle((int) x, (int) y + 30, 60, 80);

		return new Rectangle((int) x, (int) y, 60, 110);
	}

	public Rectangle getAtaqueRange() {

		if (animacoes[atacar1] && raposaAtacando.i == 2)
			return new Rectangle((int) x + 20, (int) y + 10, 10, 30);
		
		if (animacoes[atacar2] && raposaAtacando2.i == 2)
			return new Rectangle((int) x + 20, (int) y + 10, 20, 30);
		
		if (animacoes[atacar3] && raposaAtacando3.i == 2)
			return new Rectangle((int) x + 20, (int) y + 10, 10, 30);

		return new Rectangle((int) x, (int) y, 0, 0);
	}

	public void controlarAnimacao(int unchanged) {

		// make all false...
		for (int i = 0; i < 19; i++) {
			animacoes[i] = false;
		}

		// except active anim
		animacoes[unchanged] = true;
	}

	public void animacao_ataqueAereo(Animacao anim, int index) {
		if (jogo.getTeclas().G) {
			controlarAnimacao(atacar1);
		}else if (jogo.getTeclas().H) {
			controlarAnimacao(atacar2);
		}else if (jogo.getTeclas().J) {
			controlarAnimacao(atacar3);
		} else if (testeCorrendo()) {
			anim.atualizacaoEstados();
			controlarAnimacao(index);
		}

	}
	
	public int getVida() {
		return vida;
	}

	public int getRaposaX() {
		return (int) x;
	}

}
