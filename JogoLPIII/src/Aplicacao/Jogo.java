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

public class Jogo extends Canvas implements Runnable {

	private static final long serialVersionUID = 1L;//-------------------------------------------corrigir crop raposa

	public static final String titulo = "Neo-Combat";
	public static final int largura = 256;
	public static final int altura = 224;
	public static final int escala = 2;

	public boolean executar = false;
	public int atualizaFrame = 0;

	// graphics
	//private Graphics g;

	// states
	//private Estado menuEstado;
	private Estado gameEstado;

	// input
	private Teclas controleTeclas;

	// init jFrame
	private JFrame frame;

	// int map
	//private int mapa = 0;

	public Jogo() {
		frame = new JFrame(titulo);
		frame.setSize(largura * escala, altura * escala);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());

		frame.add(this, BorderLayout.CENTER);

		frame.setUndecorated(false);
		frame.setAlwaysOnTop(true);
		frame.setResizable(false);
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);
		setFocusable(false);

		// user input
		controleTeclas = new Teclas();
		frame.addKeyListener(controleTeclas);

		// pack everything
		frame.pack();
	}

	public synchronized void start() throws IOException {
		executar = true;
		
		Imagens.instanciaAnimacao();
		
		gameEstado = new EstadoJogo(this);
		Estado.setState(gameEstado);
		
		//mapa=0;
		new Thread(this).start();
	}

	public synchronized void stop() {
		// if program is stopped, running is false
		executar = false;
	}

	@Override
	public void run() {
		// init vars
		long lastTime = System.nanoTime();
		double nsPerTick = 1000000000.0 / 60.0;

		@SuppressWarnings("unused")
		int ticks = 0;
		@SuppressWarnings("unused")
		int frames = 0;

		long lastTimer = System.currentTimeMillis();
		double delta = 0;

		// while the program is running....
		while (executar) {

			// get the current system time
			long now = System.nanoTime();
			// find delta by taking difference between now and last
			delta += (now - lastTime) / nsPerTick;
			lastTime = now;

			// can render each frame...
			boolean canRender = true;

			// if ratio is greater than one, meaning...
			while (delta >= 1) {
				/*
				 * if the current time - last / n, where n can be any real number is greater
				 * than 1 update the game...
				 */
				ticks++;
				tick();
				delta--;
				canRender = true;
			}

			// sleep program so that not to many frames are produced (reduce lag)
			try {
				Thread.sleep(2);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			// if can render...
			if (canRender) {
				// increment frames and render
				frames++;
				render();

			}

			// if one second has passed...
			if (System.currentTimeMillis() - lastTimer > 1000) {
				// increment last timer, output frames to user
				lastTimer += 1000;
				//System.out.println(ticks + " ticks, " + frames + " frames");
				frames = 0;
				ticks = 0;
			}
		}
	}
	
	public void tick() {
		// update keyboard input
		controleTeclas.atualizaTeclas();
		
		// if current state exist, then update the game
		if (Estado.getState() != null) {
			
			// increment tick count and get state of program
			atualizaFrame++;
			Estado.getState().tick();

		}
	}
	
	public void render() {		
		BufferStrategy bs = getBufferStrategy();
		
		// create a double buffering strategy
		if (bs == null) {
			createBufferStrategy(2);
			return;
		}
		
		Graphics g = bs.getDrawGraphics();
		// create temp white rect that fills screen
		g.setColor(new Color(0, 0, 0));
		g.fillRect(0, 0, getWidth(), getHeight());
		
		/* ALL DRAWING HERE */
		// init maps
		ImageIcon forestStage = new ImageIcon("Imagem/jogo/forest_stage.gif");
		
		if (forestStage.getImage() != null) {
	        g.drawImage(forestStage.getImage(), -900, -220, forestStage.getIconWidth() * 2, forestStage.getIconHeight() * 2, null);
	    } else {
	        System.out.println("Imagem não encontrada!");
	    }
		
		// if current state exist, then render		
		if (Estado.getState() != null) {		
			atualizaFrame++;
			Estado.getState().render(g);	
		}
					
		/* ALL DRAWING HERE */
		
		g.dispose();
		bs.show();
	}
	
	public static void main(String[] args) throws IOException {
		Jogo game = new Jogo();
		game.start();
	}
	
	// GETTERS AND SETTERS
	
	/**
	 * @description 
	 * 	   gets key presses of user
	*/
	public Teclas getTeclas() {
		return controleTeclas;
	}
	
	/**
	 * @description 
	 * 	   gets current game state
	*/
	public Estado getGameEstado() {
		return gameEstado;
	}

}
