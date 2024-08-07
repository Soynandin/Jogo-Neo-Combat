package Estados;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.ImageIcon;

import Aplicacao.Jogo;
import Personagem.Cavaleiro;
import Personagem.Raposa;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;

public class EstadoJogo extends Estado {

	private Raposa raposa;
	private Cavaleiro cavaleiro;

	private Thread musicThread;
	private AdvancedPlayer player;
	private boolean playing = false;

	public EstadoJogo(Jogo jogo) {
		super(jogo);
		raposa = new Raposa(jogo, 50, 280);
		cavaleiro = new Cavaleiro(jogo, 300, 280);
		tocarMusica("RecursoExterno/Mp3/MusicaFundo.mp3");
	}

	@Override
	public void tick() {
		// update hitboxes, attack boxes
		raposa.getAtaqueRange();
		raposa.getPersonagemRange();

		cavaleiro.getAttackBounds();
		cavaleiro.getHitBounds();

		raposa.tick();
		cavaleiro.tick();

	}

	public void tocarMusica(String caminhoArquivo) {
		musicThread = new Thread(() -> {
			while (playing) {
				try (FileInputStream fis = new FileInputStream(caminhoArquivo)) {
					player = new AdvancedPlayer(fis);
					player.play();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
					break; // Exit the loop if the file is not found
				} catch (JavaLayerException | IOException e) {
					e.printStackTrace();
				}
			}
		});

		playing = true;
		musicThread.start();
	}
	
	 public void pararMusica() {
	        playing = false;
	        if (player != null) {
	            player.close();
	        }
	        if (musicThread != null) {
	            try {
	                musicThread.join();
	            } catch (InterruptedException e) {
	                e.printStackTrace();
	            }
	        }
	    }

	    public boolean isPlaying() {
	        return playing;
	    }

	@Override
	public void render(Graphics g) {
		ImageIcon healthBar = new ImageIcon("Imagem/jogo/healthBar.png");

		// print ui for raposa
		g.setColor(Color.PINK);
		double porcentagemRaposa = raposa.getVida() / 100.0;
		g.fillRect(61, 19, (int) (173 * porcentagemRaposa), 11);

		// print ui for cavaleiro
		g.setColor(Color.PINK);
		double porcentagemCavaleiro = cavaleiro.getHealth() / 100.0;
		int cavaleiroBarWidth = (int) (173 * porcentagemCavaleiro);
		g.fillRect(99 + 29 + 144 + (173 - cavaleiroBarWidth), 19, cavaleiroBarWidth, 11);

		// drawn last so that rect and ui could be under
		g.drawImage(healthBar.getImage(), 60, 16, (int) (healthBar.getIconWidth() * 1.2),
				(int) (healthBar.getIconHeight() * 1.2), null);

		raposa.render(g);
		cavaleiro.render(g);

		g.setColor(Color.BLACK);

		if (cavaleiro.getHealth() <= 0) {
			int pos = 150;
			String raposaG = "RAPOSA WINS THE GAME!";
			g.fillRect(147, 138, Jogo.WIDTH + 190, Jogo.HEIGHT + 20);
			g.setColor(Color.WHITE);
			//int width = g.getFontMetrics().stringWidth(raposaG);
			g.drawString(raposaG, Jogo.WIDTH + pos, Jogo.HEIGHT + pos);

		} else if (raposa.getVida() <= 0) {
			int pos = 150;
			String cavaleiroChernoWin = "CAVALEIRO WINS THE GAME!";
			g.fillRect(147, 138, Jogo.WIDTH + 190, Jogo.HEIGHT + 20);
			g.setColor(Color.WHITE);
			//int width = g.getFontMetrics().stringWidth(cavaleiroChernoWin);
			g.drawString(cavaleiroChernoWin, Jogo.WIDTH + pos, Jogo.HEIGHT + pos);
		}
	}

	// GETTERS AND SETTERS:

	public Rectangle getRaposaRangeHit() {
		return raposa.getPersonagemRange();
	}

	public Rectangle getRaposaAtaqueHit() {
		return raposa.getAtaqueRange();
	}

	public int getRaposaX() {
		return raposa.getRaposaX();
	}

	// GETTERS AND SETTERS:

	public Rectangle getCavaleiroRangeHit() {
		return cavaleiro.getHitBounds();
	}

	public Rectangle getCavaleiroAtaqueHit() {
		return cavaleiro.getAttackBounds();
	}

	public int getCavaleiroX() {
		return cavaleiro.getRaposaX();
	}

	@Override
	public void music() {
	}

}
