package myGame;

import java.awt.Color;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.naming.InitialContext;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.spi.AudioFileReader;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.util.Scanner;



public class ClassPrincipal extends JPanel implements Runnable, KeyListener {
	
	static int width = 640;
    static int height = 480;
	public static void main(String[] args) {

		JFrame tela = new JFrame();
         
		tela.setSize(width, height);
		tela.setVisible(true);
		tela.setLocationRelativeTo(null);
		tela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// INSTANCIAçãoO DO CANVAS (Elemento que permite desenhos na tela)
		
		ClassPrincipal canvas = new ClassPrincipal();
		canvas.setBounds(0, 0, width, height);
		canvas.setVisible(true);
		// Adição do canvas na tela principal
		// Tornar valores customizaveis
		tela.setLayout(null);
		tela.add(canvas);
		tela.setResizable(true);

		tela.addKeyListener(canvas);

	}
	int lives = 5;
	int initialLives = 5;
	 BufferedImage background;
	 BufferedImage livesIco;
	 File hitPlayer = new File("hitPlayer.WAV");
	 int speedup =0;
	 
	

	public ClassPrincipal() {
		
		try {
			background = ImageIO.read(new File("background_18.png"));
			
		} catch(IOException e) {
			
			e.printStackTrace();
			
//			
		}
		
		try {
			livesIco = ImageIO.read(new File("heart-icon.png"));
		} catch (IOException e1) {
			System.out.println("Não achei a imagem do coração");
			e1.printStackTrace();
		}

		Thread thread = new Thread(this);
		thread.start();
		
		 

	}
	
	// MÉTODO PARA LEITURA DE AUDIO
	boolean play = true;

	public void run() {

		while (play) {
  long startTime = System.currentTimeMillis();
			atualizar();
			// Para renderizar as imagens continuamente colocando o metodo paintComponent em
			// loop
			repaint();
			FPS();
			
 long finalTime	= System.currentTimeMillis();	
 
 
	long FPS = 1000/(finalTime -startTime);
  System.out.println("FPS: "+FPS);
 
 
		}
	}

	boolean up = false;
	boolean down = false;

  

	public void atualizar() {
		// Aqui trataremos dos movimentos

		colisoesBola();
		colisoesBolaJogador();
		movimentation();
		touchWall();
		playerColision();	
		GameOver();
	
		

	}

	
      public void playerColision(){
    	  
    	  if(posYPlayer<= 0)
    		  posYPlayer = 0;
    	  else if(posYPlayer  >=380)
    		  posYPlayer = 380;
      }
	Random aleatorio = new Random();
	int taxBall = 2;
	int taxPlayer = 1;
	
   public void GameOver() {
		if(lives<1) {
			int resposta =JOptionPane.showConfirmDialog(this, "Você perdeu!!, Gostaria de jogar novamente");
            if(resposta == JOptionPane.OK_OPTION) {
            	lives = initialLives;
              	 posYBola = 190;
              	 posXBola = 450;
              	 posXPlayer = 50;
              	 posYPlayer = 140;
              	 score =0;
              	 BallspeedX = 5;
              	 BallspeedY = 5;
              	 Playerspeed = 3;
              	 up =false;
              	 down = false;
            			 }
                 
            else if(resposta == JOptionPane.NO_OPTION)
            	System.exit(lives);
            
		}
	   
   }
	public void touchWall() {

		if (posXBola <= 0) {
			
		
			lives -= 1;
			posYBola = aleatorio.nextInt(400) + 30;
			posXBola = aleatorio.nextInt(250) + 200;
			
			
			
		

		}
		
		
	}

	int BallspeedX = 5;
	int BallspeedY = 5;

	int Playerspeed = 3;

	public void movimentation() {

		if (up) {

			posYPlayer -= Playerspeed;

		}
		if (down) {

			posYPlayer += Playerspeed;

		}

	}
	int score = 0;
	
	public void colisoesBolaJogador() {

		if (posXBola <= posXPlayer + tamPlayerX) {

			// Segundo if , agora referente ao eixo Y, ele precisa estar dentro de x
			// pois as 2 condiï¿½ï¿½es devem ser atendidas
			
			

			if (posYBola >= posYPlayer && posYBola <= posYPlayer + tamPlayerY) {

				BallspeedX *= -1;
				BallspeedY *= -1;
				score++;
				Playerspeed+= taxPlayer;
				
				if(score%2==0) {
	             
				if(BallspeedY<=0)
				BallspeedY -= taxBall;
			   else
			   BallspeedY +=taxBall;
	

			   if(BallspeedX<=0)
			   BallspeedX -= taxBall;
			  else
			  BallspeedX +=taxBall;
				}
				}
			}

		}

	

	int BallSpeedAbsoluteY;
	int BallSpeedAbsoluteX;

	public void colisoesBola() {
		posXBola += BallspeedX;
		posYBola += BallspeedY;

		if (posXBola >= width - 25 || posXBola <= 0) {

			BallspeedX *= -1;
			

		}

		if (posYBola >= height - 50 || posYBola <= 0) {

			BallspeedY *= -1;

		}
		if (BallspeedX < 0)
			BallSpeedAbsoluteX = -BallspeedX;
		else
			BallSpeedAbsoluteX = BallspeedX;
		if (BallspeedY < 0)
			BallSpeedAbsoluteY = -BallspeedY;
		else
			BallSpeedAbsoluteY = BallspeedY;

	}
	
	

	public void FPS() {

		try {
			Thread.sleep(1000 / 60);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	int tamBola = 10;
	int tamPlayerX = 10;
	int tamPlayerY = 60;
	int posYBola = 190;
	int posXBola = 450;
	int posXPlayer = 50;
	int posYPlayer = 140;

	public void paintComponent(Graphics g) {
		// O primeiro passo para a criaï¿½ï¿½o de um componente em tela ï¿½ definir a cor do
		// componente.
		
	   super.paintComponent(g);
	 
		//BACKGROUND
        g.drawImage(background,0,0,width,height, null);
       
		//Player
		g.setColor(Color.BLUE);
		g.fillRoundRect(posXPlayer, posYPlayer, tamPlayerX, tamPlayerY, 15, 10);
        //Ball
		g.setColor(Color.RED);
		g.fillOval(posXBola, posYBola, tamBola, tamBola);
		
		
		//LIVES
		for (int i = 0; i<= lives-1;i++) {
		 
			g.drawImage(livesIco,60+25*i,23,25,25,null);
		}
		
		g.setColor(Color.BLUE);
		g.drawString("Vidas:  ", 20, 40);
		g.setColor(Color.RED);

		g.drawString("velocidade da bola em Y: " + BallSpeedAbsoluteY + " pixel(s)", width-240, 360);
		g.setColor(Color.RED);

		g.drawString("velocidade da bola em X: " + BallSpeedAbsoluteX + " pixel(s)", width-240, 380);
		g.setColor(Color.BLUE);

		g.drawString("velocidade do Jogador: " + Playerspeed + " pixel(s)", 20, 380);
		
		g.setColor(Color.BLUE);

		g.drawString("Placar: " + score+ " ponto(s)", width-130,40 );
		

		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyPressed(KeyEvent e) {

		if (e.getKeyCode() == KeyEvent.VK_UP)
			up = true;
		if (e.getKeyCode() == KeyEvent.VK_DOWN)
			down = true;

	}

	@Override
	public void keyReleased(KeyEvent e) {

		if (e.getKeyCode() == KeyEvent.VK_UP)
			up = false;

		if (e.getKeyCode() == KeyEvent.VK_DOWN)
			down = false;

	}

}