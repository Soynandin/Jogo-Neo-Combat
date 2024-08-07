package ManipularImagem;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class SpriteSheet {
    private BufferedImage sheet;

    public SpriteSheet(String caminho) {
        try {
        	sheet = ImageIO.read(getClass().getClassLoader().getResourceAsStream(caminho));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public BufferedImage getSprite(int x, int y, int largura, int altura) {
        return sheet.getSubimage(x, y, largura, altura);
    }

    public BufferedImage[] getSprites(int colunas, int largura, int altura) {
        BufferedImage[] sprites = new BufferedImage[colunas];
        for (int i = 0; i < colunas; i++) {
            sprites[i] = getSprite(i * largura, 0, largura, altura);
        }
        return sprites;
    }
    
    public BufferedImage getSheet() {
        return sheet;
    }
}
