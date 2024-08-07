package ManipularImagem;

import java.awt.image.BufferedImage;

public class Animacao {

	public int msQuadros, i;
	
	private long msTempo, timer;
	
	private BufferedImage[] frames;
	
	private boolean reproducao = false;
	
	public Animacao(int msQuadros, BufferedImage[] frames) {

		// inits class vars
		this.msQuadros = msQuadros;
		this.frames = frames;
		
		i = 0;
		// ms passed since start of program
		msTempo = System.currentTimeMillis();
		
	}
	
	public void atualizacaoEstados() {
		
		// time passed since current tick method and previously called tick method
		timer += System.currentTimeMillis() - msTempo;
		// reset
		msTempo = System.currentTimeMillis();
			
		// if timer > rate per frame...
		if (timer > msQuadros) {
			// increment index (i.e. go to next frame)
			i++;
			timer = 0;
				
			// if last frame, go to first frame
			if (i == frames.length) {
				i = 0;
				// then the animation has played once...
				reproducao = true;
			}
		}
	}
	
	// gets index of frame of animation
		public int getIndex() {
			return i;
		}
		
		// gets image of current frame
		public BufferedImage getFrames() {
			return frames[i];
		}
		
		// ask if has been played...
		public boolean getReproducao() {
			return reproducao;
		}
		
		// set played once to false...
		public boolean setReproducao() {
			return reproducao = false;
		}
}
