package ManipularImagem;

import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

public class Carregar_imagem {
    // Método para carregar uma imagem a partir de um caminho especificado
    public static BufferedImage carregaImagem(String strPacote) {
        try {
            return ImageIO.read(Carregar_imagem.class.getResource(strPacote));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null; // Retorna null se a imagem não puder ser carregada
    }
}

