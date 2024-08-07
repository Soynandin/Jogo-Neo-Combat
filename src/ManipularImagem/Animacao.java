package ManipularImagem;

import java.awt.image.BufferedImage;

public class Animacao {
	// Intervalo entre quadros e índice do quadro atual
    public int msQuadros, i; 

    // Tempo do último tick e temporizador
    private long msTempo, temporizador;
    
	// Array de imagens para a animação
    private BufferedImage[] frames; 
    
	// Flag para verificar se a animação foi reproduzida uma vez
    private boolean reproducao = false; 

    public Animacao(int msQuadros, BufferedImage[] frames) {
        // Inicializa as variáveis da classe
        this.msQuadros = msQuadros;
        this.frames = frames;
        
        i = 0; // Indice inicial
        
        msTempo = System.currentTimeMillis(); // Tempo desde o início do programa
    }

    public void atualizacaoEstados() {
        // Tempo passado desde o último método tick e o método tick atual
        temporizador += System.currentTimeMillis() - msTempo;
        // Reseta o temporizador
        msTempo = System.currentTimeMillis();
        
        // Se o tempo passado for maior que o intervalo entre quadros...
        if (temporizador > msQuadros) {
            // Vai para o próximo quadro
            i++;
            temporizador = 0;
            
            // Se o índice for igual ao número de quadros, volta para o primeiro quadro
            if (i == frames.length) {
                i = 0;
                // A animação foi reproduzida uma vez
                reproducao = true;
            }
        }
    }

    // Retorna o índice do quadro atual da animação
    public int getIndex() {
        return i;
    }
    
    // Retorna a imagem do quadro atual
    public BufferedImage getFrames() {
        return frames[i];
    }
    
    // Verifica se a animação foi reproduzida
    public boolean getReproducao() {
        return reproducao;
    }
    
    // Define que a animação não foi reproduzida ainda
    public boolean setReproducao() {
        return reproducao = false;
    }
}
