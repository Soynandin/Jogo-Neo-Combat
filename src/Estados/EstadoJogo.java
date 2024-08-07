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

	// Instância da raposa e do cavaleiro
    private Raposa raposa; 
    private Cavaleiro cavaleiro;

    private Thread musicaThread; // Thread para tocar a música
    private AdvancedPlayer player; // Reprodutor de música
    private boolean musica_esta_tocando = false; // Flag para verificar se a música está tocando

    public EstadoJogo(Jogo jogo) {
        super(jogo);
        raposa = new Raposa(jogo, 50, 280); 
        cavaleiro = new Cavaleiro(jogo, 300, 280); 
        tocarMusica("RecursoExterno\\Mp3\\MusicaFundo.mp3");
    }

    @Override
    public void tick() {
        // Atualiza as áreas de ataque e colisão da raposa
        raposa.getAtaqueRange();
        raposa.getPersonagemRange();

        // Atualiza as áreas de ataque e colisão do cavaleiro
        cavaleiro.getAtaqueRange();
        cavaleiro.getPersonagemRange();

        // Atualiza o estado da raposa e do cavaleiro
        raposa.tick();
        cavaleiro.tick();
    }

    public void tocarMusica(String caminhoArquivo) {
        musicaThread = new Thread(() -> {
            while (musica_esta_tocando) {
                try (FileInputStream fis = new FileInputStream(caminhoArquivo)) {
                    player = new AdvancedPlayer(fis); // Inicializa o reprodutor de música
                    player.play(); // Inicia a reprodução da música
                } catch (FileNotFoundException e) {
                    e.printStackTrace(); 
                    break; 
                } catch (JavaLayerException | IOException e) {
                    e.printStackTrace();
                }
            }
        });

        musica_esta_tocando = true; // Define que a música está tocando
        musicaThread.start(); // Inicia a thread de música
    }

    public void pararMusica() {
        musica_esta_tocando = false; // Define que a música deve parar
        if (player != null) {
            player.close(); // Fecha o reprodutor de música
        }
        if (musicaThread != null) {
            try {
                musicaThread.join(); // Aguarda o término da thread de música
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean seMusica_esta_tocando() {
        return musica_esta_tocando; // Retorna se a música está tocando
    }

    @Override
    public void renderizar(Graphics g) {
		// Carrega a imagem da barra de saúde
        ImageIcon healthBar = new ImageIcon("Imagem\\jogo\\healthBar.png"); 

        // Desenha a barra de vida da raposa
        g.setColor(Color.PINK);
        double porcentagemRaposa = raposa.getVida() / 100.0; // Calcula a porcentagem de vida da raposa
        g.fillRect(61, 19, (int) (173 * porcentagemRaposa), 11); // Desenha a barra de vida da raposa

        // Desenha a barra de vida do cavaleiro
        g.setColor(Color.PINK);
        double porcentagemCavaleiro = cavaleiro.getVida() / 100.0; // Calcula a porcentagem de vida do cavaleiro
        int cavaleiroBarWidth = (int) (173 * porcentagemCavaleiro); // Calcula a largura da barra de vida do cavaleiro
        g.fillRect(99 + 29 + 144 + (173 - cavaleiroBarWidth), 19, cavaleiroBarWidth, 11); // Desenha a barra de vida do cavaleiro

        // Desenha a imagem da barra de saúde, que deve estar acima das barras de vida
        g.drawImage(healthBar.getImage(), 60, 16, (int) (healthBar.getIconWidth() * 1.2),
                (int) (healthBar.getIconHeight() * 1.2), null);

        // Renderiza a raposa e o cavaleiro
        raposa.renderizar(g);
        cavaleiro.renderizar(g);

        g.setColor(Color.BLACK);

        // Verifica se o cavaleiro perdeu e a raposa venceu
        if (cavaleiro.getVida() <= 0) {
            int pos = 150;
            String raposaG = "RAPOSA WINS THE GAME!"; // Mensagem de vitória da raposa
            g.fillRect(147, 138, Jogo.WIDTH + 190, Jogo.HEIGHT + 20); // Desenha o fundo para a mensagem
            g.setColor(Color.WHITE);
            g.drawString(raposaG, Jogo.WIDTH + pos, Jogo.HEIGHT + pos); // Desenha a mensagem de vitória da raposa
            cavaleiro.iniciarAnimacaoMorte();
        // Verifica se a raposa perdeu e o cavaleiro venceu
        } else if (raposa.getVida() <= 0) {
            int pos = 150;
            String cavaleiroChernoWin = "CAVALEIRO WINS THE GAME!"; // Mensagem de vitória do cavaleiro
            g.fillRect(147, 138, Jogo.WIDTH + 190, Jogo.HEIGHT + 20); // Desenha o fundo para a mensagem
            g.setColor(Color.WHITE);
            g.drawString(cavaleiroChernoWin, Jogo.WIDTH + pos, Jogo.HEIGHT + pos); // Desenha a mensagem de vitória do cavaleiro
            raposa.iniciarAnimacaoMorte();
        }
    }

    // GET E SET Raposa:

    public Rectangle getRaposaRangeHit() {
        return raposa.getPersonagemRange(); // Retorna a área de colisão da raposa
    }

    public Rectangle getRaposaAtaqueHit() {
        return raposa.getAtaqueRange(); // Retorna a área de ataque da raposa
    }

    public int getRaposaX() {
        return raposa.getRaposaX(); // Retorna a posição X da raposa
    }

    // GET E SET Cavaleiro:

    public Rectangle getCavaleiroRangeHit() {
        return cavaleiro.getPersonagemRange(); // Retorna a área de colisão do cavaleiro
    }

    public Rectangle getCavaleiroAtaqueHit() {
        return cavaleiro.getAtaqueRange(); // Retorna a área de ataque do cavaleiro
    }

    public int getCavaleiroX() {
        return cavaleiro.getCavaleiroX(); // Retorna a posição X do cavaleiro
    }

    @Override
    public void musica() {
        // Método vazio, possivelmente reservado para futuras implementações
    }
}
