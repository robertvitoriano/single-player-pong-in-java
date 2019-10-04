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
import javax.swing.ImageIcon;
import javax.swing.JFrame;
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
	 private BufferedImage background;
	 BufferedImage livesIco;

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

	boolean play = true;

	public void run() {

		while (play) {

			atualizar();
			// Para renderizar as imagens continuamente colocando o metodo paintComponent em
			// loop
			repaint();
			FPS();
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

	}

	/*
	 * String answer; public void playAgain() {
	 * 
	 * Scanner reader = new Scanner(System.in); answer = reader.next();
	 * 
	 * if(lives == 0) { play = false;
	 * System.out.println("Do you want to play again ? (answer with yes or no)");
	 * 
	 * if(answer.equalsIgnoreCase("yes")) {
	 * 
	 * lives =3; play=true;
	 * 
	 * } else if (answer.equalsIgnoreCase("no")) {
	 * System.out.println("O jogo acabou"); play = false; }
	 * 
	 * 
	 * 
	 * 
	 * }
	 * 
	 * }
	 */
      public void playerColision(){
    	  
    	  if(posYPlayer<= 0)
    		  posYPlayer = 0;
    	  else if(posYPlayer  >=380)
    		  posYPlayer = 380;
      }
	Random aleatorio = new Random();
	int taxBall = 2;
	int taxPlayer = 1;
	

	public void touchWall() {

		if (posXBola <= 0) {

			lives -= 1;
			posYBola = aleatorio.nextInt(400);
			posXBola = aleatorio.nextInt(250) + 200;
			Playerspeed+= taxPlayer;
              
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

	int BallspeedX = 5;
	int BallspeedY = 5;

	int Playerspeed = 4;

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
		g.drawImage(livesIco,100,23,25,25,null);
		g.setColor(Color.BLUE);
		g.drawString("você tem:  " + lives, 20, 40);
		g.setColor(Color.RED);

		g.drawString("velocidade da bola em Y: " + BallSpeedAbsoluteY + " pixels", 430, 360);
		g.setColor(Color.RED);

		g.drawString("velocidade da bola em X: " + BallSpeedAbsoluteX + " pixels", 430, 380);
		g.setColor(Color.BLUE);

		g.drawString("velocidade do Jogador: " + Playerspeed + " pixels", 20, 380);
		
		g.setColor(Color.BLUE);

		g.drawString("Placar: " + score+ " ponto(s)", width-114,40 );
		

	
		
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