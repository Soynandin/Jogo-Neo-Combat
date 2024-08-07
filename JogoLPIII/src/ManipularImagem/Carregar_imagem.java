package ManipularImagem;

import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

public class Carregar_imagem {
	public static BufferedImage carregaImage(String path) {	
		try {
			return ImageIO.read(Carregar_imagem.class.getResource(path));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
