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

public class Cavaleiro extends Posicao {

	private int vida;
	private int velX, velY;
	private Jogo jogo;

	private final int parado = 0;
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

	private Animacao cavaleiroParado;
	private Animacao cavaleiroCorrendoDireita;
	private Animacao cavaleiroCorrendoEsquerda;
	private Animacao cavaleiroPulando;
	private Animacao cavaleiroPulandoDireita;
	private Animacao cavaleiroPulandoEsquerda;
	private Animacao cavaleiroAbaixada;
	private Animacao cavaleiroAtacando;
	private Animacao cavaleiroAtacando2;
	private Animacao cavaleiroAtacando3;
	private Animacao cavaleiroLevaDano;
	//private Animacao cavaleiroMorrendo;

	// cooldowns
	private boolean atingido;
	private long invulneravel;

	// random generator
	Random rand;

	public Cavaleiro(Jogo jogo, float x, float y) {
		super(x, y);

		this.jogo = jogo;
		rand = new Random();

		vida = 100;
		// atingida = false;

		cavaleiroParado 			= new Animacao(100, Imagens.p2_parado);
		cavaleiroCorrendoDireita 	= new Animacao(100, Imagens.p2_correrDireita);
		cavaleiroCorrendoEsquerda 	= new Animacao(100, Imagens.p2_correrEsquerda);
		cavaleiroPulando 			= new Animacao(100, Imagens.p2_pularEsquerda);
		cavaleiroPulandoDireita 	= new Animacao(100, Imagens.p2_pularDireita);
		cavaleiroPulandoEsquerda 	= new Animacao(100, Imagens.p2_pularEsquerda);
		cavaleiroAbaixada 			= new Animacao(100, Imagens.p2_abaixar);
		cavaleiroAtacando 			= new Animacao(100, Imagens.p2_ataque_1);
		cavaleiroAtacando2 			= new Animacao(100, Imagens.p2_ataque_2);
		cavaleiroAtacando3 			= new Animacao(100, Imagens.p2_ataque_3);
		cavaleiroLevaDano 			= new Animacao(100, Imagens.p2_levaDano);
		//cavaleiroMorrendo 			= new Animacao(100, Imagens.p2_morrendo);
	}

	@Override
	public void tick() {
		cavaleiroParado.atualizacaoEstados();
		cavaleiroAbaixada.atualizacaoEstados();

		cavaleiroCorrendoDireita.atualizacaoEstados();
		cavaleiroCorrendoEsquerda.atualizacaoEstados();

		if (animacoes[atacar1])
			cavaleiroAtacando.atualizacaoEstados();
		if (animacoes[atacar2])
			cavaleiroAtacando2.atualizacaoEstados();
		if (animacoes[atacar3])
			cavaleiroAtacando3.atualizacaoEstados();

		if (y == 280) {

			if (jogo.getTeclas().esquerda1 && !jogo.getTeclas().cima1) {
				velX = -2;
				pegaAnimacao(correrEsquerda);
			} 
			
			else if (jogo.getTeclas().direita1 && !jogo.getTeclas().cima1) {
				velX = 2;
				pegaAnimacao(correrDireita);
			} 
			
			else if (jogo.getTeclas().cima1 && !jogo.getTeclas().direita1 && !jogo.getTeclas().esquerda1
					&& !animacoes[pular]) {
				// jump
				velY = velocidadePulo - 2;
				y -= 1;
				cavaleiroPulando.i = 0;
			} 
			
			else if (jogo.getTeclas().cima1 && jogo.getTeclas().direita1 && !jogo.getTeclas().esquerda1
					&& !animacoes[pularDireita]) {
				velY = velocidadePulo;
				velX = 2;
				y -= 1;
				cavaleiroPulandoDireita.i = 0;
			} 
			
			else if (jogo.getTeclas().cima1 && !jogo.getTeclas().direita1 && jogo.getTeclas().esquerda1
					&& !animacoes[pularEsquerda]) {
				velY = velocidadePulo;
				velX = -2;
				y -= 1;
				cavaleiroPulandoEsquerda.i = 0;
			} 
			
			else if (!jogo.getTeclas().direita1 && !jogo.getTeclas().esquerda1 && jogo.getTeclas().baixo1) {
				velX = 0;
				velY = 0;
				
				pegaAnimacao(abaixar);

				// if pressing g while crouching
				if (jogo.getTeclas().N1) {
					velX = 0;
					velY = 0;

					// for punching frames, deactivate crouch
					animacoes[abaixar] = false;

					// reset and init true to state, punch
					if (testeCorrendo())
						handleAnims(atacar1);

				}
				// if pressing g while crouching
				if (jogo.getTeclas().N2) {
					velX = 0;
					velY = 0;

					// for punching frames, deactivate crouch
					animacoes[abaixar] = false;

					// reset and init true to state, punch
					if (testeCorrendo())
						handleAnims(atacar2);

				}
				// if pressing g while crouching
				if (jogo.getTeclas().N3) {
					velX = 0;
					velY = 0;

					// for punching frames, deactivate crouch
					animacoes[abaixar] = false;

					// reset and init true to state, punch
					if (testeCorrendo())
						handleAnims(atacar3);

				}

				// play full animation once then reset
				resetAnim(cavaleiroAtacando, atacar1);
				resetAnim(cavaleiroAtacando2, atacar2);
				resetAnim(cavaleiroAtacando3, atacar3);
			} 
			
			else if (jogo.getTeclas().N1 && !animacoes[abaixar]) {
				velX = 0;
				velY = 0;
				if (testeCorrendo())
					pegaAnimacao(atacar1);
			} 
			
			else if (jogo.getTeclas().N2 && !animacoes[abaixar]) {
				velX = 0;
				velY = 0;
				if (testeCorrendo())
					pegaAnimacao(atacar2);
			} 
			else if (jogo.getTeclas().N3 && !animacoes[abaixar]) {
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
					pegaAnimacao(parado);
				}
			}

		} else {
			animacoes[correrDireita] = false;
			animacoes[correrEsquerda] = false;

			cavaleiroPulando.atualizacaoEstados();
			cavaleiroPulandoDireita.atualizacaoEstados();
			cavaleiroPulandoEsquerda.atualizacaoEstados();

			// if moving left
			if (velX < 0) {
				// back flip
				handleAirAttacks(cavaleiroPulandoEsquerda, pularEsquerda);
				// if moving right
			} else if (velX > 0) {
				// front flip
				handleAirAttacks(cavaleiroPulandoDireita, pularDireita);
				// otherwise
			} else {
				// jump vertically
				handleAirAttacks(cavaleiroPulando, pular);
			}
		}

		if (animacoes[atacar1] || animacoes[atacar2] || animacoes[atacar3]) {
			velX = 0;
			velY = 0;
		}

		// reset ground attacks
		resetAnim(cavaleiroAtacando, atacar1);
		resetAnim(cavaleiroAtacando2, atacar2);
		resetAnim(cavaleiroAtacando3, atacar3);

		collisions();

		if (atingido) {
			if (System.currentTimeMillis() - invulneravel > 400) {
				animacoes[levaDano] = false;
				atingido = false;
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

	public void verificaParede() {
		// if x < 0...
		if (x < -30) {
			// do not go past left edge
			x = -30;
		}

		// if x > 0
		if (x > Jogo.largura + 90) {
			// do not go past right edge
			x = Jogo.largura + 90;
		}
	}

	public void resetAnim(Animacao anim, int frame) {
		// if called anim is played once...
		if (anim.getReproducao()) {
			// set anim to false
			anim.setReproducao();
			animacoes[frame] = false;
		}

	}

	public void cair() {

		// update y speed
		velY += gravidade;

		// if greater than max speed, just equal max speed
		if (velY > velocidade) {
			velY = velocidade+2;
		}

		// if moving left...
		if (velX < 0) {
			// backflip
			if (cavaleiroPulandoEsquerda.getIndex() >= 2 && cavaleiroPulandoEsquerda.getIndex() <= 3) {
				velY = 0;
			}
		}

		// if moving right...
		if (velX > 0) {
			// front flip
			if (cavaleiroPulandoDireita.getIndex() >= 2 && cavaleiroPulandoDireita.getIndex() <= 3) {
				velY = 0;
			}
		}

		// if horizontally still...
		if (velX == 0) {
			// jump anims
			if (cavaleiroPulando.getIndex() >= 3 && cavaleiroPulando.getIndex() <= 4) {
				velY = 0;
			}
		}

		// update y by y-speed
		y += velY;

	}

	public void collisions() {

		// if rectangle of player collides with enemy rectangle
		if (jogo.getGameEstado().getRaposaAtaqueHit().intersects(getHitBounds())) {

			// if not already hurting...
			if (!atingido) {
				atingido = true;
				invulneravel = System.currentTimeMillis();
				handleAnims(levaDano);
				vida -= 3;
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
	public void render(Graphics g) {
		// 2d graphics for transformations
		Graphics2D g2d = (Graphics2D) g;

		// when hit, vibrate randomly
		if (atingido) {
			int k = rand.nextInt(3);
			g2d.translate(-k, k);
		}
		// draw shadow
		g.setColor(new Color(0, 0, 0, 125));
		g.fillOval((int) x + 30, 200 * Jogo.escala, 64, 16);

		if (animacoes[correrDireita])
			g.drawImage(getCurrentAnimFrame(), (int) (x + 50), (int) (y - 3), null);

		else if (animacoes[correrEsquerda])
			g.drawImage(getCurrentAnimFrame(), (int) (x - 4), (int) y, null);

		else if (animacoes[abaixar])
			g.drawImage(getCurrentAnimFrame(), (int) x +30, (int) y + 80, null);

		else if (animacoes[pular])
			g.drawImage(getCurrentAnimFrame(), (int) x - 3, (int) y - 20, null);

		else if (animacoes[pularDireita])
			g.drawImage(getCurrentAnimFrame(), (int) x +30, (int) y - 15, null);

		else if (animacoes[pularEsquerda])
			g.drawImage(getCurrentAnimFrame(), (int) x - 5, (int) y - 15, null);

		else if (animacoes[atacar1])
			g.drawImage(getCurrentAnimFrame(), (int) x, (int) (y + 3), null);

		else if (animacoes[atacar2])
			g.drawImage(getCurrentAnimFrame(), (int) x, (int) (y + 3), null);
		
		else if (animacoes[atacar3])
			g.drawImage(getCurrentAnimFrame(), (int) x, (int) (y + 3), null);
		
		else if (animacoes[levaDano])
			g.drawImage(getCurrentAnimFrame(), (int) (x+30), (int) (y + 1), null);
		
		else
			g.drawImage(getCurrentAnimFrame(), (int) x, (int) y, null);
	}

	
	private BufferedImage getCurrentAnimFrame() {

		if (animacoes[correrDireita])
			return cavaleiroCorrendoDireita.getFrames();

		else if (animacoes[correrEsquerda])
			return cavaleiroCorrendoEsquerda.getFrames();

		else if (animacoes[abaixar])
			return cavaleiroAbaixada.getFrames();

		else if (animacoes[pular])
			return cavaleiroPulando.getFrames();

		else if (animacoes[pularDireita])
			return cavaleiroPulandoDireita.getFrames();

		else if (animacoes[pularEsquerda])
			return cavaleiroPulandoEsquerda.getFrames();

		else if (animacoes[atacar1])
			return cavaleiroAtacando.getFrames();

		else if (animacoes[atacar2])
			return cavaleiroAtacando2.getFrames();
		
		else if (animacoes[atacar3])
			return cavaleiroAtacando3.getFrames();

		else if (animacoes[levaDano])
			return cavaleiroLevaDano.getFrames();
		
		else
			return cavaleiroParado.getFrames();

	}

	public void pegaAnimacao(int unchanged) {

		// make all false...
		for (int i = 0; i < 19; i++) {
			animacoes[i] = false;
		}

		// except active anim
		animacoes[unchanged] = true;
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

	public Rectangle getHitBounds() {

		if (animacoes[abaixar])
			return new Rectangle((int) x - 60, (int) y + 30, 60, 80);

		return new Rectangle((int) x - 60, (int) y, 60, 110);
	}

	public Rectangle getAttackBounds() {

		if (animacoes[atacar1] && cavaleiroAtacando.i >= 3 && cavaleiroAtacando.i <= 6)
			return new Rectangle((int) x - 5, (int) y + 10, 5, 20);

		if (animacoes[atacar2] && cavaleiroAtacando2.i >= 1 && cavaleiroAtacando2.i <= 3)
			return new Rectangle((int) x - 5, (int) y , 1 , 10);
		
		if (animacoes[atacar3] && cavaleiroAtacando3.i >= 1 && cavaleiroAtacando3.i <= 3)
			return new Rectangle((int) x - 5, (int) y , 1 , 10);

		return new Rectangle((int) x - 30, (int) y, 0, 0);
	}

	public void handleAnims(int unchanged) {

		// make all false...
		for (int i = 0; i < 19; i++) {
			animacoes[i] = false;
		}

		// except active anim
		animacoes[unchanged] = true;
	}

	public void handleAirAttacks(Animacao anim, int index) {

		// if g, h, b while in air... set all anims to false except called anim
		if (jogo.getTeclas().N1) {
			handleAnims(atacar1);
		} else if (jogo.getTeclas().N2) {
			handleAnims(atacar2);
		}else if (jogo.getTeclas().N3) {
			handleAnims(atacar3);
		} else if (testeCorrendo()) {
			// update anim and set all to false
			anim.atualizacaoEstados();
			handleAnims(index);
		}

	}

	public int getHealth() {
		return vida;
	}

	// get x
	public int getRaposaX() {
		return (int) x;
	}

}
