package ManipularImagem;

import java.awt.image.BufferedImage;
import java.io.IOException;

public class Imagens {

    // ---------------------------------------------Sprites: Personagem 1 - Raposa

    // movimentos básicos
    public static BufferedImage[] p1_parado = new BufferedImage[8], p1_correrDireita = new BufferedImage[8],
            p1_correrEsquerda = new BufferedImage[8], p1_abaixar = new BufferedImage[1],
            p1_pularDireita = new BufferedImage[10], p1_pularEsquerda = new BufferedImage[10];

    // golpes
    public static BufferedImage[] p1_ataque_g = new BufferedImage[10], p1_ataque_h = new BufferedImage[10], p1_ataque_j = new BufferedImage[7];
    
    // movimentos ocasionais
    public static BufferedImage[] p1_levaDano = new BufferedImage[2], p1_morrendo = new BufferedImage[10];
    
 // ---------------------------------------------Sprites: Personagem 2

 // movimentos básicos
    public static BufferedImage[] p2_parado = new BufferedImage[4], p2_correrDireita = new BufferedImage[7],
            p2_correrEsquerda = new BufferedImage[7], p2_abaixar = new BufferedImage[1],
            p2_pularDireita = new BufferedImage[6], p2_pularEsquerda = new BufferedImage[6];

    // golpes
    public static BufferedImage[] p2_ataque_1 = new BufferedImage[5], p2_ataque_2 = new BufferedImage[4], p2_ataque_3 = new BufferedImage[4];
    
    // movimentos ocasionais
    public static BufferedImage[] p2_levaDano = new BufferedImage[2], p2_morrendo = new BufferedImage[6];

    public static void instanciaAnimacao() throws IOException {
    	// Sprites Personagem Raposa
    	SpriteSheet p1_abaixarSheet 		= new SpriteSheet("raposa/Abaixar.png");
        SpriteSheet p1_ataqueGSheet 		= new SpriteSheet("raposa/Ataque_G.png");
        SpriteSheet p1_ataqueHSheet 		= new SpriteSheet("raposa/Ataque_H.png");
        SpriteSheet p1_ataqueJSheet 		= new SpriteSheet("raposa/Ataque_J.png");
        SpriteSheet p1_correrDireitaSheet 	= new SpriteSheet("raposa/Correr_direita.png");
        SpriteSheet p1_correrEsquerdaSheet 	= new SpriteSheet("raposa/Correr_esquerda.png");
        SpriteSheet p1_morrerSheet 			= new SpriteSheet("raposa/Morrer.png");
        SpriteSheet p1_paradoSheet 			= new SpriteSheet("raposa/Parada1.png");
        SpriteSheet p1_pularDireitaSheet 	= new SpriteSheet("raposa/Pular_direita.png");
        SpriteSheet p1_pularEsquerdaSheet 	= new SpriteSheet("raposa/Pular_esquerda.png");
        SpriteSheet p1_sofreDanoSheet 		= new SpriteSheet("raposa/Sofre_dano.png");
        
     // Sprites Personagem Cavaleiro
        SpriteSheet p2_abaixarSheet 		= new SpriteSheet("cavaleiro/Abaixar.png");
        SpriteSheet p2_ataque1Sheet 		= new SpriteSheet("cavaleiro/Ataque_1.png");
        SpriteSheet p2_ataque2Sheet 		= new SpriteSheet("cavaleiro/Ataque_2.png");
        SpriteSheet p2_ataque3Sheet 		= new SpriteSheet("cavaleiro/Ataque_3.png");
        SpriteSheet p2_correrDireitaSheet 	= new SpriteSheet("cavaleiro/Correr_direita.png");
        SpriteSheet p2_correrEsquerdaSheet 	= new SpriteSheet("cavaleiro/Correr_esquerda.png");
        SpriteSheet p2_morrerSheet 			= new SpriteSheet("cavaleiro/Morrer.png");
        SpriteSheet p2_paradoSheet 			= new SpriteSheet("cavaleiro/Parado.png");
        SpriteSheet p2_pularDireitaSheet 	= new SpriteSheet("cavaleiro/Pular_direita.png");
        SpriteSheet p2_pularEsquerdaSheet 	= new SpriteSheet("cavaleiro/Pular_esquerda.png");
        SpriteSheet p2_sofreDanoSheet 		= new SpriteSheet("cavaleiro/Sofre_dano.png");

        
        // Instâncias Sprites Raposa
        int larguraSpriteParado = p1_paradoSheet.getSheet().getWidth() / 8;
        int alturaSpriteParado = p1_paradoSheet.getSheet().getHeight();
        p1_parado = p1_paradoSheet.getSprites(8, larguraSpriteParado, alturaSpriteParado);

        int larguraSpriteCorrer = p1_correrDireitaSheet.getSheet().getWidth() / 8;
        int alturaSpriteCorrer = p1_correrDireitaSheet.getSheet().getHeight();
        p1_correrDireita = p1_correrDireitaSheet.getSprites(8, larguraSpriteCorrer, alturaSpriteCorrer);
        
        int larguraSpriteCorrerEsquerda = p1_correrEsquerdaSheet.getSheet().getWidth() / 8;
        int alturaSpriteCorrerEsquerda = p1_correrEsquerdaSheet.getSheet().getHeight();
        for (int i = 0; i < 8; i++) {
            int x = (7-i) * larguraSpriteCorrerEsquerda;
            p1_correrEsquerda[i] = p1_correrEsquerdaSheet.getSprite(x, 0, larguraSpriteCorrerEsquerda, alturaSpriteCorrerEsquerda);
        }

        p1_abaixar[0] = p1_abaixarSheet.getSprite(0, 0, p1_abaixarSheet.getSheet().getWidth(), p1_abaixarSheet.getSheet().getHeight());

        int larguraSpritePular = p1_pularDireitaSheet.getSheet().getWidth() / 10;
        int alturaSpritePular = p1_pularDireitaSheet.getSheet().getHeight();
        p1_pularDireita = p1_pularDireitaSheet.getSprites(10, larguraSpritePular, alturaSpritePular);
        
        int larguraSpritePularEsquerda = p1_pularEsquerdaSheet.getSheet().getWidth() / 10;
        int alturaSpritePularEsquerda = p1_pularEsquerdaSheet.getSheet().getHeight();
        for (int i = 0; i < 10; i++) {
            int x = (9-i) * larguraSpritePularEsquerda;
            p1_pularEsquerda[i] = p1_pularEsquerdaSheet.getSprite(x, 0, larguraSpritePularEsquerda, alturaSpritePularEsquerda);
        }

        int larguraSpriteAtaqueG = p1_ataqueGSheet.getSheet().getWidth() / 10;
        int alturaSpriteAtaqueG = p1_ataqueGSheet.getSheet().getHeight();
        p1_ataque_g = p1_ataqueGSheet.getSprites(10, larguraSpriteAtaqueG, alturaSpriteAtaqueG);

        int larguraSpriteAtaqueH = p1_ataqueHSheet.getSheet().getWidth() / 10;
        int alturaSpriteAtaqueH = p1_ataqueHSheet.getSheet().getHeight();
        p1_ataque_h = p1_ataqueHSheet.getSprites(10, larguraSpriteAtaqueH, alturaSpriteAtaqueH);

        int larguraSpriteAtaqueJ = p1_ataqueJSheet.getSheet().getWidth() / 7;
        int alturaSpriteAtaqueJ = p1_ataqueJSheet.getSheet().getHeight();
        p1_ataque_j = p1_ataqueJSheet.getSprites(7, larguraSpriteAtaqueJ, alturaSpriteAtaqueJ);

        int larguraSpriteLevaDano = p1_sofreDanoSheet.getSheet().getWidth() / 2;
        int alturaSpriteLevaDano = p1_sofreDanoSheet.getSheet().getHeight();
        p1_levaDano = p1_sofreDanoSheet.getSprites(2, larguraSpriteLevaDano, alturaSpriteLevaDano);

        int larguraSpriteMorrendo = p1_morrerSheet.getSheet().getWidth() / 10;
        int alturaSpriteMorrendo = p1_morrerSheet.getSheet().getHeight();
        p1_morrendo = p1_morrerSheet.getSprites(10, larguraSpriteMorrendo, alturaSpriteMorrendo);

    	// Instâncias Sprites Cavaleiro
        
        int p2_larguraSpriteParado = p2_paradoSheet.getSheet().getWidth() / 4;
        int p2_alturaSpriteParado = p2_paradoSheet.getSheet().getHeight();
        for (int i = 0; i < 4; i++) {
            int x = (3-i) * p2_larguraSpriteParado;
            p2_parado[i] = p2_paradoSheet.getSprite(x, 0, p2_larguraSpriteParado, p2_alturaSpriteParado);
        }

        int p2_larguraSpriteCorrer = p2_correrDireitaSheet.getSheet().getWidth() / 7;
        int p2_alturaSpriteCorrer = p2_correrDireitaSheet.getSheet().getHeight();
        p2_correrDireita = p2_correrDireitaSheet.getSprites(7, p2_larguraSpriteCorrer, p2_alturaSpriteCorrer);
        
        
        int p2_larguraSpriteCorrerEsquerda = p2_correrEsquerdaSheet.getSheet().getWidth() / 7;
        int p2_alturaSpriteCorrerEsquerda = p2_correrEsquerdaSheet.getSheet().getHeight();
        for (int i = 0; i < 7; i++) {
            int x = (6-i) * p2_larguraSpriteCorrerEsquerda;
            p2_correrEsquerda[i] = p2_correrEsquerdaSheet.getSprite(x, 0, p2_larguraSpriteCorrerEsquerda, p2_alturaSpriteCorrerEsquerda);
        }

        p2_abaixar[0] = p2_abaixarSheet.getSprite(0, 0, p2_abaixarSheet.getSheet().getWidth(), p2_abaixarSheet.getSheet().getHeight());

        int p2_larguraSpritePular = p2_pularDireitaSheet.getSheet().getWidth() / 6;
        int p2_alturaSpritePular = p2_pularDireitaSheet.getSheet().getHeight();
        p2_pularDireita = p2_pularDireitaSheet.getSprites(6, p2_larguraSpritePular, p2_alturaSpritePular);
        
        int p2_larguraSpritePularEsquerda = p2_pularEsquerdaSheet.getSheet().getWidth() / 6;
        int p2_alturaSpritePularEsquerda = p2_pularEsquerdaSheet.getSheet().getHeight();
        for (int i = 0; i < 6; i++) {
            int x = (5-i) * p2_larguraSpritePularEsquerda;
            p2_pularEsquerda[i] = p2_pularEsquerdaSheet.getSprite(x, 0, p2_larguraSpritePularEsquerda, p2_alturaSpritePularEsquerda);
        }

        int p2_larguraSpriteAtaque1 = p2_ataque1Sheet.getSheet().getWidth() / 5;
        int p2_alturaSpriteAtaque1 = p2_ataque1Sheet.getSheet().getHeight();
        for (int i = 0; i < 5; i++) {
            int x = (4-i) * p2_larguraSpriteAtaque1;
            p2_ataque_1[i] = p2_ataque1Sheet.getSprite(x, 0, p2_larguraSpriteAtaque1, p2_alturaSpriteAtaque1);
        }

        int p2_larguraSpriteAtaque2 = p2_ataque2Sheet.getSheet().getWidth() / 4;
        int p2_alturaSpriteAtaque2 = p2_ataque2Sheet.getSheet().getHeight();
        for (int i = 0; i < 4; i++) {
            int x = (3-i) * p2_larguraSpriteAtaque2;
            p2_ataque_2[i] = p2_ataque2Sheet.getSprite(x, 0, p2_larguraSpriteAtaque2, p2_alturaSpriteAtaque2);
        }

        int p2_larguraSpriteAtaque3 = p2_ataque3Sheet.getSheet().getWidth() / 4;
        int p2_alturaSpriteAtaque3 = p2_ataque3Sheet.getSheet().getHeight();
        for (int i = 0; i < 4; i++) {
            int x = (3-i) * p2_larguraSpriteAtaque3;
            p2_ataque_3[i] = p2_ataque3Sheet.getSprite(x, 0, p2_larguraSpriteAtaque3, p2_alturaSpriteAtaque3);
        }

        int p2_larguraSpriteLevaDano = p2_sofreDanoSheet.getSheet().getWidth() / 2;
        int p2_alturaSpriteLevaDano = p2_sofreDanoSheet.getSheet().getHeight();
        for (int i = 0; i < 2; i++) {
            int x = (1-i) * p2_larguraSpriteLevaDano;
            p2_levaDano[i] = p2_sofreDanoSheet.getSprite(x, 0, p2_larguraSpriteLevaDano, p2_alturaSpriteLevaDano);
        }

        int p2_larguraSpriteMorrendo = p2_morrerSheet.getSheet().getWidth() / 6;
        int p2_alturaSpriteMorrendo = p2_morrerSheet.getSheet().getHeight();
        for (int i = 0; i < 6; i++) {
            int x = (5-i) * p2_larguraSpriteMorrendo;
            p2_morrendo[i] = p2_morrerSheet.getSprite(x, 0, p2_larguraSpriteMorrendo, p2_alturaSpriteMorrendo);
        }
    }
}
















