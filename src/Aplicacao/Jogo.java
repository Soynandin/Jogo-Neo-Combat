package Aplicacao;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import Configuracao.Teclas;
import Estados.Estado;
import Estados.EstadoJogo;
import ManipularImagem.Imagens;

/*
	Classe que representa o jogo e implementa a interface Runnable,
	permitindo que ele seja executado em uma thread separada
*/
public class Jogo extends Canvas implements Runnable {
	private static final long serialVersionUID = 1L;

	public static final String titulo = "Neo-Combat";

	public static final int largura = 256;
	public static final int altura = 224;
	public static final int escala = 2;

	// Contador para rastrear atualizações de frame
	public int atualizaFrame = 0;

	// Variável para controlar se o jogo está em execução ou não
	public boolean executar = false;
	
	// Instâncias para representar o estado atual do jogo, o controle de teclas, e o JFrame da aplicação
	private Estado gameEstado;
	private Teclas controleTeclas;
	private JFrame app;

	// Construtor da classe Jogo, inicializa o JFrame e configura suas propriedades
	public Jogo() {
		app = new JFrame(titulo);
		app.setSize(largura * escala, altura * escala);
		app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		app.setLayout(new BorderLayout());
		app.add(this, BorderLayout.CENTER);
		app.setUndecorated(false);
		app.setAlwaysOnTop(true);
		app.setResizable(false);
		app.setVisible(true);
		app.setLocationRelativeTo(null);

		setFocusable(false);

		controleTeclas = new Teclas();
		app.addKeyListener(controleTeclas);

		app.pack();
	}

	// Método sincronizado que inicia o jogo, configurando o estado inicial e iniciando a thread
	public synchronized void iniciar() throws IOException {
		executar = true;
		Imagens.instanciaAnimacao();
		gameEstado = new EstadoJogo(this);
		Estado.setEstado(gameEstado);
		new Thread(this).start();
	}

	// Método sincronizado para parar a execução do jogo
	public synchronized void parar() {
		executar = false;
	}

	// Método run que será executado pela thread. Controla o loop principal do jogo
	@Override
	public void run() {
        // Marca o tempo inicial
        long marcoInicial_1 = System.nanoTime(); 
		// Determina o intervalo desejado entre atualizações
        double interTick = 1000000000.0 / 60.0; 

        @SuppressWarnings("unused")
        int ticks = 0; // Contador de atualizações de lógica (ticks)
        @SuppressWarnings("unused")
        int frames = 0; // Contador de renderizações de frames

		// Marca o tempo inicial
        long marcoInicial_2 = System.currentTimeMillis(); 
		// Diferença acumulada entre o tempo atual e o último tick
        double delta = 0; 

        // Loop principal do jogo, continua enquanto o jogo estiver em execução
        while (executar) {
            // Captura o tempo atual
            long tempoAtual = System.nanoTime();

            // Calcula a diferença de tempo desde a última atualização e acumula em delta
            delta += (tempoAtual - marcoInicial_1) / interTick;
            marcoInicial_1 = tempoAtual; // Atualiza o tempo de referência para o próximo loop

            // Variável para determinar se é possível renderizar um frame
            boolean podeRenderizar = false;

            // Se delta for maior ou igual a 1, é hora de atualizar a lógica do jogo
            while (delta >= 1) {
                ticks++; 
                tick(); // Atualiza a lógica do jogo.
                delta--;
                podeRenderizar = true;
            }

            // Dorme a thread por um curto período para evitar consumo excessivo de CPU
            try {
                Thread.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // Se puder renderizar, incrementa o contador de frames e chama o método render
            if (podeRenderizar) {
                frames++;
                render(); // Renderiza o próximo frame do jogo
            }

            /* 
			Se um segundo tiver passado desde o último tempo registrado, reinicia os contadores
			
			Utilize 'System.out.println(ticks + " ticks, " + frames + " frames");' dentro da condicional abaixo caso queira testar as atualizações de frame
			*/
            if (System.currentTimeMillis() - marcoInicial_2 > 1000) {
                marcoInicial_2 += 1000;
                frames = 0;
                ticks = 0;
            }
        }
    }
	
	// Método que atualiza a lógica do jogo a cada tick
	public void tick() {
		// Atualiza o estado das teclas pressionadas
		controleTeclas.atualizaTeclas();
		
		// Se o estado atual do jogo não for nulo, atualiza a lógica do jogo
		if (Estado.getEstado() != null) {
			atualizaFrame++;
			Estado.getEstado().tick();
		}
	}
	
	// Método responsável por renderizar o jogo na tela
	public void render() {		
		// Obtém o buffer do Canvas
        BufferStrategy bs = getBufferStrategy(); 

        // Se a estratégia de buffer ainda não existir, cria uma nova com duplo buffer
        if (bs == null) {
            createBufferStrategy(2);
            return;
        }
        
		// Obtém o contexto gráfico para desenhar
        Graphics g = bs.getDrawGraphics(); 

        // Preenche a tela com um retângulo preto para limpar a tela antes de desenhar
        g.setColor(new Color(0, 0, 0));
        g.fillRect(0, 0, getWidth(), getHeight());
        
        // Desenha o cenário do jogo (background)
        ImageIcon cenario = new ImageIcon("Imagem\\jogo\\forest_stage.gif");
        
        // Verifica se a imagem foi carregada corretamente antes de desenhar
        if (cenario.getImage() != null) {
            // Desenha a imagem do cenário com tamanho dobrado para se ajustar à escala
            g.drawImage(cenario.getImage(), -900, -220, cenario.getIconWidth() * 2, cenario.getIconHeight() * 2, null);
        } else {
            System.out.println("Imagem não encontrada!");
        }
        
        // Se o estado atual do jogo não for nulo, renderiza o estado atual
        if (Estado.getEstado() != null) {
            atualizaFrame++;
            Estado.getEstado().renderizar(g);
        }
        // Libera os recursos gráficos e mostra o próximo frame preparado na tela
        g.dispose(); 
        bs.show();
    }
	
	// Método principal (main)
	public static void main(String[] args) throws IOException {
		Jogo game = new Jogo();
		game.iniciar();
	}
	
	// GET controle de teclas (KeyListener)
	public Teclas getTeclas() {
		return controleTeclas;
	}
	
	// GET estado atual do jogo
	public Estado getGameEstado() {
		return gameEstado;
	}
}
