package Estados;

import java.awt.Graphics;
import java.awt.Rectangle;

import Aplicacao.Jogo;


// Estado do jogo
public abstract class Estado {

    // Guarda o estado atual do jogo
    private static Estado estadoAtual = null;

	// Representa o jogo associado a este estado
    protected Jogo jogo;

	// Construtor recebe uma instância do jogo
    public Estado(Jogo jogo) {
        this.jogo = jogo;
    }
    // GET estado atual do jogo
    public static void setEstado(Estado state) {
        estadoAtual = state;
    }

    // SET estado atual do jogo
    public static Estado getEstado() {
        return estadoAtual;
    }

    // Métodos que todas as classes que estendem esta classe devem implementar
    public abstract void tick(); // Atualiza a lógica do estado
    public abstract void renderizar(Graphics g); // Renderiza o estado na tela
    public abstract void musica(); // Controla a música associada ao estado

    // Métodos relacionados aos hitboxes dos personagens
    public abstract Rectangle getRaposaRangeHit(); // Obtém a área de impacto da Raposa
    public abstract Rectangle getCavaleiroRangeHit(); // Obtém a área de impacto do Cavaleiro

    public abstract Rectangle getRaposaAtaqueHit(); // Obtém a área de ataque da Raposa
    public abstract Rectangle getCavaleiroAtaqueHit(); // Obtém a área de ataque do Cavaleiro
	
    public abstract int getRaposaX(); // Obtém a posição X da Raposa
    public abstract int getCavaleiroX(); // Obtém a posição X do Cavaleiro
}
