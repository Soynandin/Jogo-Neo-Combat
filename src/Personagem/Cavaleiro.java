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

public class Cavaleiro extends Posicao {

	private int vida;
	private int velX, velY;
	private Jogo jogo;

	// Constantes para estados do Cavaleiro
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

	private final int morrer = 15;
	private final int verifica_fig = 19;
	private final int gravidade = 1;
	private final int velocidade = 2;
	private final int velocidadePulo = -10;

	private boolean[] animacoes = new boolean[20]; // Array de animações

	 private Thread musicaThread; // Thread para tocar a música
    private AdvancedPlayer player; // Reprodutor de música
    public boolean musica_esta_tocando = false; // Flag para verificar se a música está tocando

	// Animações do cavaleiro
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
	private Animacao cavaleiroMorrendo;

	// cooldowns
	private boolean atingido; // Estado de ser atingido
	private long invulneravel; // Tempo de invulnerabilidade

	Random rand;

	public Cavaleiro(Jogo jogo, float x, float y) {
		super(x, y);

		this.jogo = jogo;
		rand = new Random();

		vida = 100;

		// Inicialização das animações
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
		cavaleiroMorrendo 			= new Animacao(100, Imagens.p2_morrendo);
	}

	public int getVida() {
		return vida;
	}

	public int getCavaleiroX() {
		return (int) x;
	}

	/* 
	Retorna o retângulo delimitador da raposa:
	- Se o cavaleiro está abaixado, retorna um retângulo menor e mais baixo
	- Caso contrário, retorna um retângulo padrão
	*/
	public Rectangle getPersonagemRange() {

		if (animacoes[abaixar])
			return new Rectangle((int) x - 60, (int) y + 30, 60, 80);

		return new Rectangle((int) x - 60, (int) y, 60, 110);
	}

	// Retorna o retângulo delimitador do ataque do cavaleiro
	public Rectangle getAtaqueRange() {

		if (animacoes[atacar1] && cavaleiroAtacando.i >= 3 && cavaleiroAtacando.i <= 6)
			return new Rectangle((int) x - 5, (int) y + 10, 5, 20);

		if (animacoes[atacar2] && cavaleiroAtacando2.i >= 1 && cavaleiroAtacando2.i <= 3)
			return new Rectangle((int) x - 5, (int) y , 1 , 10);
		
		if (animacoes[atacar3] && cavaleiroAtacando3.i >= 1 && cavaleiroAtacando3.i <= 3)
			return new Rectangle((int) x - 5, (int) y , 1 , 10);

		return new Rectangle((int) x - 30, (int) y, 0, 0);
	}

	// Função para obter o frame atual da animação com base no estado do cavaleiro
	private BufferedImage getAnimFrameAtual() {

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

		else if (animacoes[morrer])
			return cavaleiroMorrendo.getFrames();
		
		else
			return cavaleiroParado.getFrames();

	}

	// Atualização dos estados do cavaleiro
	@Override
	public void tick() {
		cavaleiroParado.atualizacaoEstados();
		cavaleiroAbaixada.atualizacaoEstados();

		cavaleiroCorrendoDireita.atualizacaoEstados();
		cavaleiroCorrendoEsquerda.atualizacaoEstados();

		// Verifica se a animação de ataque está ativa
		if (animacoes[atacar1])
			cavaleiroAtacando.atualizacaoEstados();
		if (animacoes[atacar2])
			cavaleiroAtacando2.atualizacaoEstados();
		if (animacoes[atacar3])
			cavaleiroAtacando3.atualizacaoEstados();

		// Verificação das teclas pressionadas para definir a ação do cavaleiro
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

				// Verificação de ataques enquanto abaixado
				if (jogo.getTeclas().N1) {
					velX = 0;
					velY = 0;
					animacoes[abaixar] = false;
					if (verificarAnimacaoAtiva())
						controlarAnimacao(atacar1);
				}

				if (jogo.getTeclas().N2) {
					velX = 0;
					velY = 0;
					animacoes[abaixar] = false;
					if (verificarAnimacaoAtiva())
						controlarAnimacao(atacar2);
				}

				if (jogo.getTeclas().N3) {
					velX = 0;
					velY = 0;
					animacoes[abaixar] = false;
					if (verificarAnimacaoAtiva())
						controlarAnimacao(atacar3);
				}

				resetAnim(cavaleiroAtacando, atacar1);
				resetAnim(cavaleiroAtacando2, atacar2);
				resetAnim(cavaleiroAtacando3, atacar3);
			} 
			
			else if (jogo.getTeclas().N1 && !animacoes[abaixar]) {
				velX = 0;
				velY = 0;
				if (verificarAnimacaoAtiva())
					pegaAnimacao(atacar1);
				
			} 
			
			else if (jogo.getTeclas().N2 && !animacoes[abaixar]) {
				velX = 0;
				velY = 0;
				if (verificarAnimacaoAtiva())
					pegaAnimacao(atacar2);
			} 
			else if (jogo.getTeclas().N3 && !animacoes[abaixar]) {
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
					pegaAnimacao(parado);
				}
			}

		} else {
			animacoes[correrDireita] = false;
			animacoes[correrEsquerda] = false;

			cavaleiroPulando.atualizacaoEstados();
			cavaleiroPulandoDireita.atualizacaoEstados();
			cavaleiroPulandoEsquerda.atualizacaoEstados();

			// Atualização das animações de pulo
			if (velX < 0) {
				animacao_ataqueAereo(cavaleiroPulandoEsquerda, pularEsquerda);
			} else if (velX > 0) {
				animacao_ataqueAereo(cavaleiroPulandoDireita, pularDireita);
			} else {
				animacao_ataqueAereo(cavaleiroPulando, pular);
			}
		}

		if (animacoes[atacar1] || animacoes[atacar2] || animacoes[atacar3]) {
			velX = 0;
			velY = 0;
		}

		resetAnim(cavaleiroAtacando, atacar1);
		resetAnim(cavaleiroAtacando2, atacar2);
		resetAnim(cavaleiroAtacando3, atacar3);

		colisoes(); // Função de colisões

		if (atingido) {
			if (System.currentTimeMillis() - invulneravel > 400) {
				animacoes[levaDano] = false;
				atingido = false;
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

		if (atingido) {
			int k = rand.nextInt(3);
			g2d.translate(-k, k);
		}
		g.setColor(new Color(0, 0, 0, 125));
		g.fillOval((int) x + 65, 200 * Jogo.escala, 64, 16);

		if (animacoes[correrDireita])
			g.drawImage(getAnimFrameAtual(), (int) (x + 50), (int) (y - 3), null);

		else if (animacoes[correrEsquerda])
			g.drawImage(getAnimFrameAtual(), (int) (x - 4), (int) y, null);

		else if (animacoes[abaixar])
			g.drawImage(getAnimFrameAtual(), (int) x +30, (int) y + 80, null);

		else if (animacoes[pular])
			g.drawImage(getAnimFrameAtual(), (int) x - 3, (int) y - 20, null);

		else if (animacoes[pularDireita])
			g.drawImage(getAnimFrameAtual(), (int) x +30, (int) y - 15, null);

		else if (animacoes[pularEsquerda])
			g.drawImage(getAnimFrameAtual(), (int) x - 5, (int) y - 15, null);

		else if (animacoes[atacar1])
			g.drawImage(getAnimFrameAtual(), (int) x, (int) (y + 3), null);

		else if (animacoes[atacar2])
			g.drawImage(getAnimFrameAtual(), (int) x, (int) (y + 3), null);
		
		else if (animacoes[atacar3])
			g.drawImage(getAnimFrameAtual(), (int) x, (int) (y + 3), null);
		
		else if (animacoes[levaDano])
			g.drawImage(getAnimFrameAtual(), (int) (x+30), (int) (y + 1), null);

		else if (animacoes[morrer])
			g.drawImage(getAnimFrameAtual(), (int) (x+30), (int) (y + 1), null);
		
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
	public void movimentar() {
		velY += gravidade;

		if (velY > velocidade) {
			velY = velocidade+2;
		}

		if (velX < 0) {
			// Pular para trás
			if (cavaleiroPulandoEsquerda.getIndex() >= 2 && cavaleiroPulandoEsquerda.getIndex() <= 3) {
				velY = 0;
			}
		}

		if (velX > 0) {
			// Pular para frente
			if (cavaleiroPulandoDireita.getIndex() >= 2 && cavaleiroPulandoDireita.getIndex() <= 3) {
				velY = 0;
			}
		}

		if (velX == 0) {
			// Pular
			if (cavaleiroPulando.getIndex() >= 3 && cavaleiroPulando.getIndex() <= 4) {
				velY = 0;
			}
		}

		y += velY;
	}

	// Verifica se há colisão entre o range do personagem e o range do inimigo
	public void colisoes() {

		if (jogo.getGameEstado().getRaposaAtaqueHit().intersects(getPersonagemRange())) {

			if (!atingido) {
				atingido = true;
				invulneravel = System.currentTimeMillis();
				controlarAnimacao(levaDano);
				vida -= 5;
				tocarMusica("RecursoExterno\\Mp3\\golpeP1.mp3");
			}

			// Tempo de cooldown
			try {
				TimeUnit.MILLISECONDS.sleep(40);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}
	}

	// Função para controlar a animação de ataque aéreo
	public void animacao_ataqueAereo(Animacao anim, int index) {
		if (jogo.getTeclas().N1) {
			controlarAnimacao(atacar1);
		} else if (jogo.getTeclas().N2) {
			controlarAnimacao(atacar2);
		}else if (jogo.getTeclas().N3) {
			controlarAnimacao(atacar3);
		} else if (verificarAnimacaoAtiva()) {
			anim.atualizacaoEstados();
			controlarAnimacao(index);
		}

	}

	// Ativa a animação de morte
	public void iniciarAnimacaoMorte() {
		pegaAnimacao(morrer);
		cavaleiroMorrendo.atualizacaoEstados();
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

		if (x > Jogo.largura + 90) {
			x = Jogo.largura + 90;
		}
	}

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
