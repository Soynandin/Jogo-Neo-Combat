package ManipularImagem;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class SpriteSheet {

    private BufferedImage sheet; // Imagem que contém os sprites

    public SpriteSheet(String caminho) {
        try {
            // Carrega a imagem da page de sprites a partir do caminho fornecido
            sheet = ImageIO.read(getClass().getClassLoader().getResourceAsStream(caminho));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Obtém um sprite específico da page de sprites com base nas coordenadas e tamanho fornecidos
    public BufferedImage getSprite(int x, int y, int largura, int altura) {
        // Extrai e retorna a parte da imagem especificada
        return sheet.getSubimage(x, y, largura, altura); 
    }

    // Obtém uma série de sprites da page, organizados em colunas
    public BufferedImage[] getSprites(int colunas, int largura, int altura) {
        // Array para armazenar os sprites
        BufferedImage[] sprites = new BufferedImage[colunas]; 
        // Extrai cada sprite da coluna atual
        for (int i = 0; i < colunas; i++) {
            sprites[i] = getSprite(i * largura, 0, largura, altura);
        }
        return sprites; // Retorna o array de sprites
    }

    // Retorna a page de sprites completa
    public BufferedImage getSheet() {
        return sheet;
    }
}

