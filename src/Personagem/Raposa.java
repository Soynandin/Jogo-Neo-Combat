package Personagem;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import Aplicacao.Jogo;
import ManipularImagem.Animacao;
import ManipularImagem.Imagens;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;

public class Raposa extends Posicao {

	private int vida;
	private int velX, velY;
	private Jogo jogo;

	// Constantes para estados da raposa
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
	
	private final int morrer = 15;
	private final int verifica_fig = 19;
	private final int gravidade = 1;
	private final int velocidade = 2;
	private final int velocidadePulo = -10;

	private boolean[] animacoes = new boolean[20]; // Array de animações

	private Thread musicaThread; // Thread para tocar a música
    private AdvancedPlayer player; // Reprodutor de música
    public boolean musica_esta_tocando = false; // Flag para verificar se a música está tocando

	// Animações da raposa
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
	private Animacao raposaMorrendo;

	private boolean atingida; // Estado de ser atingida
	private long invulneravel; // Tempo de invulnerabilidade

	Random rand;

	public Raposa(Jogo jogo, float x, float y) {
		super(x, y);

		this.jogo = jogo;
		rand = new Random();

		vida = 100;
		
		// Inicialização das animações
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
		raposaMorrendo 			= new Animacao(100, Imagens.p1_morrendo);
	}

	public int getVida() {
		return vida;
	}

	public int getRaposaX() {
		return (int) x;
	}

	/* 
	Retorna o retângulo delimitador da raposa:
	- Se a raposa está abaixada, retorna um retângulo menor e mais baixo
	- Caso contrário, retorna um retângulo padrão
	*/
	public Rectangle getPersonagemRange() {

		if (animacoes[abaixar])
			return new Rectangle((int) x, (int) y + 30, 60, 80);

		return new Rectangle((int) x, (int) y, 60, 110);
	}

	// Retorna o retângulo delimitador do ataque da raposa
	public Rectangle getAtaqueRange() {

		if (animacoes[atacar1] && raposaAtacando.i == 2)
			return new Rectangle((int) x -10, (int) y + 10, 30, 30);
		
		if (animacoes[atacar2] && raposaAtacando2.i == 2)
			return new Rectangle((int) x -30, (int) y + 10, 10, 30);
		
		if (animacoes[atacar3] && raposaAtacando3.i == 2)
			return new Rectangle((int) x -10, (int) y + 10, 10, 30);

		return new Rectangle((int) x, (int) y, 0, 0);
	}

	// Função para obter o frame atual da animação com base no estado da raposa
	private BufferedImage getAnimFrameAtual() {
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

		else if (animacoes[morrer])
			return raposaMorrendo.getFrames();
		
		else
			return raposaParada.getFrames();
	}

	// Atualização dos estados da raposa
	@Override
	public void tick() {
		raposaParada.atualizacaoEstados();
		raposaAbaixada.atualizacaoEstados();

		raposaCorrendoDireita.atualizacaoEstados();
		raposaCorrendoEsquerda.atualizacaoEstados();

		// Verifica se a animação de ataque está ativa
		if (animacoes[atacar1])
			raposaAtacando.atualizacaoEstados();
		if (animacoes[atacar2])
			raposaAtacando2.atualizacaoEstados();
		if (animacoes[atacar3])
			raposaAtacando3.atualizacaoEstados();
		
		// Verificação das teclas pressionadas para definir a ação da raposa
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

				// Verificação de ataques enquanto abaixado
				if (jogo.getTeclas().G) {
					velX = 0;
					velY = 0;
					animacoes[abaixar] = false;
					if (verificarAnimacaoAtiva())
						controlarAnimacao(atacar1);
				}
				
				if (jogo.getTeclas().H) {
					velX = 0;
					velY = 0;
					animacoes[abaixar] = false;
					if (verificarAnimacaoAtiva())
						controlarAnimacao(atacar2);
				}
				
				if (jogo.getTeclas().J) {
					velX = 0;
					velY = 0;
					animacoes[abaixar] = false;
					if (verificarAnimacaoAtiva())
						controlarAnimacao(atacar3);
				}
				
				resetAnim(raposaAtacando, atacar1);
				resetAnim(raposaAtacando2, atacar2);
				resetAnim(raposaAtacando3, atacar3);
			} 
			
			else if (jogo.getTeclas().G && !animacoes[abaixar]) {
				velX = 0;
				velY = 0;
				if (verificarAnimacaoAtiva())
					pegaAnimacao(atacar1);
			}
			
			else if (jogo.getTeclas().H && !animacoes[abaixar]) {
				velX = 0;
				velY = 0;
				if (verificarAnimacaoAtiva())
					pegaAnimacao(atacar2);
			}
			
			else if (jogo.getTeclas().J && !animacoes[abaixar]) {
				velX = 0;
				velY = 0;
				if (verificarAnimacaoAtiva())
					pegaAnimacao(atacar3);
			}
			
			else {
				velX = 0;
				velY = 0;
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

			// Atualização das animações de pulo
			if (velX < 0) {
				animacao_ataqueAereo(raposaPulandoEsquerda, pularEsquerda);
			} else if (velX > 0) {
				animacao_ataqueAereo(raposaPulandoDireita, pularDireita);
			} else {
				animacao_ataqueAereo(raposaPulando, pular);
			}
		}

		if (animacoes[atacar1] || animacoes[atacar2] || animacoes[atacar3]) {
			velX = 0;
			velY = 0;
		}

		resetAnim(raposaAtacando, atacar1);
		resetAnim(raposaAtacando2, atacar2);
		resetAnim(raposaAtacando3, atacar3);
		
		colisoes(); // Função de colisões
		
		if (atingida) {	
			if (System.currentTimeMillis() - invulneravel > 400) {
				animacoes[levaDano] = false;
				atingida = false;
				invulneravel += 400;
			}
		}

		x += velX;

		// Se pular, aplica a gravidade
		if (y < 280)
			movimentar();

		if (y > 280)
			y = 280;
		
		// Verifica os limites do Frame
		verificaParede();
	}

	// Desenho das animações da raposa de acordo com o estado atual
	@Override
	public void renderizar(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		if (atingida) {
			int k = rand.nextInt(3);
			g2d.translate(-k, k);
		}
		g.setColor(new Color(0, 0, 0, 125));
		g.fillOval((int) x + 30, 200 * Jogo.escala, 64, 16);

		if (animacoes[correrDireita])
			g.drawImage(getAnimFrameAtual(), (int) (x - 9), (int) (y - 3), null);

		else if (animacoes[correrEsquerda])
			g.drawImage(getAnimFrameAtual(), (int) (x - 4), (int) y, null);

		else if (animacoes[abaixar])
			g.drawImage(getAnimFrameAtual(), (int) x, (int) y + 50, null);

		else if (animacoes[pular])
			g.drawImage(getAnimFrameAtual(), (int) x - 3, (int) y - 20, null);

		else if (animacoes[pularDireita])
			g.drawImage(getAnimFrameAtual(), (int) x - 15, (int) y - 15, null);

		else if (animacoes[pularEsquerda])
			g.drawImage(getAnimFrameAtual(), (int) x - 15, (int) y - 15, null);

		else if (animacoes[atacar1])
			g.drawImage(getAnimFrameAtual(), (int) x+3, (int) (y + 3), null);
		
		else if (animacoes[atacar2])
			g.drawImage(getAnimFrameAtual(), (int) x, (int) (y + 3), null);
		
		else if (animacoes[atacar3])
			g.drawImage(getAnimFrameAtual(), (int) x+3, (int) (y + 3), null);
		
		else if (animacoes[levaDano])
			g.drawImage(getAnimFrameAtual(), (int) (x - 15), (int) (y + 1), null);

		else if (animacoes[morrer])
			g.drawImage(getAnimFrameAtual(), (int) (x - 15), (int) (y + 1), null);
		
		else
			g.drawImage(getAnimFrameAtual(), (int) x, (int) y, null);
	}

	// Função para controlar a animação ativa
	public void controlarAnimacao(int aux) {
		for (int i = 0; i < 19; i++) {
			animacoes[i] = false;
		}
		animacoes[aux] = true;
	}

	// Função para movimentar a raposa e aplicar gravidade
	public void movimentar(){
		velY += gravidade;

		if (velY > velocidade){
			velY = velocidade;
		} 		

		if (velX < 0) {
			// Pular para trás
			if (raposaPulandoEsquerda.getIndex() >= 2 && raposaPulandoEsquerda.getIndex() <= 3) {
				velY = 0;
			}
		}

		if (velX > 0) {
			// Pular para frente
			if (raposaPulandoDireita.getIndex() >= 2 && raposaPulandoDireita.getIndex() <= 3) {
				velY = 0;
			}
		}

		if (velX == 0) {
			// Pular
			if (raposaPulando.getIndex() >= 3 && raposaPulando.getIndex() <= 4) {
				velY = 0;
			}
		}

		y += velY;
	}
	
	// Verifica se há colisão entre o range do personagem e o range do inimigo
	public void colisoes() {
		if (jogo.getGameEstado().getCavaleiroAtaqueHit().intersects(getPersonagemRange())) {

			if (!atingida) {
				controlarAnimacao(levaDano);
				invulneravel = System.currentTimeMillis();
				atingida = true;
				vida-=2;
				tocarMusica("RecursoExterno\\Mp3\\golpeP2.mp3");
			}

			// Tempo de cooldown
			try {
				TimeUnit.MILLISECONDS.sleep(20);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} 
	}

	// Função para controlar a animação de ataque aéreo
	public void animacao_ataqueAereo(Animacao anim, int index) {
		if (jogo.getTeclas().G) {
			controlarAnimacao(atacar1);
		}else if (jogo.getTeclas().H) {
			controlarAnimacao(atacar2);
		}else if (jogo.getTeclas().J) {
			controlarAnimacao(atacar3);
		} else if (verificarAnimacaoAtiva()) {
			anim.atualizacaoEstados();
			controlarAnimacao(index);
		}

	}

	public void iniciarAnimacaoMorte() {
		// Ativa a animação de morte
		pegaAnimacao(morrer);
		raposaMorrendo.atualizacaoEstados();
		new Thread(new Runnable() {
			public void run() {
				try {
					TimeUnit.MILLISECONDS.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();
		pegaAnimacao(morrer);
	}

	// Resetar animações
	public void resetAnim(Animacao anim, int frame) {
		if (anim.getReproducao()) {
			anim.setReproducao();
			animacoes[frame] = false;
		}
	}
	
	public void pegaAnimacao(int aux) {
		for (int i = 0; i < 19; i++) {
			animacoes[i] = false;
		}
		animacoes[aux] = true;
	}

	

	public void tocarMusica(String caminhoArquivo) {
        musicaThread = new Thread(() -> {
            try (FileInputStream fis = new FileInputStream(caminhoArquivo)) {
                player = new AdvancedPlayer(fis); // Inicializa o reprodutor de música
                musica_esta_tocando = true; // Define que a música está tocando
                player.play(); // Inicia a reprodução da música
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (JavaLayerException | IOException e) {
                e.printStackTrace();
            } finally {
                musica_esta_tocando = false; // Define que a música parou de tocar
            }
        });
        musicaThread.start(); // Inicia a thread de música
    }

	public void verificaParede() {
		if (x < -30) {
			x = -30;
		}
		if (x > Jogo.largura + 150) {
			x = Jogo.largura + 150;
		}
	}

	// Função para verificar se alguma animação está ativa
	public boolean verificarAnimacaoAtiva() {
		int i = 0;
		for (boolean b : animacoes) {
			if (b == false)
				i++;
		}
		if (i == 19) {
			return false;
		}
		return true;
	}
}