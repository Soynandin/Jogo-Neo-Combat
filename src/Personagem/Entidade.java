package Personagem;

import java.awt.Graphics;

public abstract class Entidade {
    // Cada entidade tem uma posição x e y
    protected float x, y;

    // Construtor
    public Entidade(float x, float y) {
        this.x = x; 
        this.y = y;
    }
    
    // Atualiza o estado da entidade
    public abstract void tick();
    
    // Desenha a entidade na tela
    public abstract void renderizar(Graphics g);
}

