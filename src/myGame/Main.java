package myGame;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Main extends JPanel implements Runnable, KeyListener {
    static int width = 640;
    static int height = 480;
    int lives = 5;
    int initialLives = 5;
    BufferedImage livesIco;
    int bestScore;
    int speedup = 0;
    boolean pause = false;
    Random randomNumber = new Random();
    int ballSpeedRate = 2;
    int playerSpeedRate = 1;
    int BallspeedX = 5;
    int BallspeedY = 5;
    int score = 0;
    int BallSpeedAbsoluteY;
    int BallSpeedAbsoluteX;
    int ballSize = 10;
    int ballYPosition = 190;
    int ballXPosition = 450;
    GameObject player;
    GameObject life;
    GameObject background;

    public static void main(String[] args) throws Exception {
        JFrame screen = new JFrame("Single Player Pong - by Robert Vitoriano");
        screen.setSize(width, height);
        screen.setVisible(true);
        screen.setLocationRelativeTo(null);
        screen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Main canvas = new Main();
        canvas.setBounds(0, 0, width, height);
        canvas.setVisible(true);
        screen.setLayout(null);
        screen.add(canvas);
        screen.setResizable(true);
        screen.addKeyListener(canvas);
    }

    public Main() throws Exception {
        BufferedReader reader;
        reader = new BufferedReader(new FileReader("bestScore.txt"));
        String FileContent = reader.readLine();
        bestScore = Integer.parseInt(FileContent);
        reader.close();

        background = new Background("background.png", 0, 0, width, height);
        player = new GameObject("small-mario.png", 10, 140, 60, 60);
        life = new GameObject("heart-icon.png", 0, 23, 25, 25);

        Thread thread = new Thread(this);
        thread.start();
    }

    public void run() {
        while (true) {
            if (pause == false) {
                long startTime = System.currentTimeMillis();
                updateGame();
                repaint();
                FPS();
                long finalTime = System.currentTimeMillis();
                long FPS = 1000 / (finalTime - startTime);
                System.out.println("FPS: " + FPS);
                System.out.println("Rodando");
            } else
                System.out.println("Pausado");
        }
    }

    public void updateGame() {
        ballCollisions();
        playerBallCollisions();
        movimentation();
        touchWall();
        playerColision();
        GameOver();
    }

    public void playerColision() {
        if (player.getYPosition() <= 0)
            player.setYPosition(0);
        else if (player.getYPosition() >= 380)
            player.setYPosition(380);
    }

    public void GameOver() {
        if (lives < 1) {
            try {
                playSound("loseSound.wav");
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } catch (Exception e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            if (score > bestScore) {
                FileWriter writer;
                try {
                    writer = new FileWriter("bestScore.txt");
                    writer.write("" + score);
                    writer.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            int resposta;
            int lastScore = bestScore;
            if (score < lastScore) {
                resposta = JOptionPane.showConfirmDialog(this, "Você perdeu!!" +
                        "Você fez " + score + " pontos " +
                        "e a " +
                        "melhor pontução até agora foi " + lastScore + " pontos. " +
                        "  Gostaria de jogar novamente");
            } else {
                resposta = JOptionPane.showConfirmDialog(this, "Você perdeu, mas está de parabéns." +
                        "Você fez " + score + " pontos " +
                        "e a " +
                        "melhor pontuação até agora tinha si " + lastScore + " pontos." +
                        "  Gostaria de jogar novamente");
            }
            if (resposta == JOptionPane.OK_OPTION) {
                lives = initialLives;
                ballYPosition = 190;
                ballXPosition = 450;
                player.setXPosition(50);
                player.setYPosition(140);
                score = 0;
                BallspeedX = 5;
                BallspeedY = 5;
                player.setSpeed(3);
                player.setMovingUp(false);
                player.setMovingDown(false);
            } else if (resposta == JOptionPane.NO_OPTION)
                System.exit(lives);
        }
    }

    public void touchWall() {
        if (ballXPosition <= 0) {
            lives -= 1;
            ballYPosition = randomNumber.nextInt(400) + 30;
            ballXPosition = randomNumber.nextInt(250) + 200;
            try {
                playSound("hitPlayer.wav");
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    void playSound(String soundFile) throws Exception, IOException {
        File f = new File("./" + soundFile);
        AudioInputStream audioIn = AudioSystem.getAudioInputStream(f.toURI().toURL());
        Clip clip = AudioSystem.getClip();
        clip.open(audioIn);
        clip.start();
    }

    public void movimentation() {
        if (player.isMovingUp()) {
            player.setYPosition(player.getYPosition() - player.getSpeed());
        }
        if (player.isMovingDown()) {
            player.setYPosition(player.getYPosition() + player.getSpeed());
        }
    }

    public void playerBallCollisions() {
        if (ballXPosition <= player.getXPosition() + player.getWidth()) {
            // Segundo if , agora referente ao eixo Y, ele precisa estar dentro de x
            // pois as 2 condições devem ser atendidas
            if (ballYPosition >= player.getYPosition()
                    && ballYPosition <= player.getYPosition() + player.getHeight()) {
                if (bestScore < score)
                    bestScore = score;
                BallspeedX *= -1;
                BallspeedY *= -1;
                try {
                    playSound("hitBallSound.wav");
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                score++;
                player.setSpeed(player.getSpeed() + player.getSpeedRate());
                if (score % 4 == 0) {
                    if (BallspeedY <= 0)
                        BallspeedY -= ballSpeedRate;
                    else
                        BallspeedY += ballSpeedRate;
                    if (BallspeedX <= 0)
                        BallspeedX -= ballSpeedRate;
                    else
                        BallspeedX += ballSpeedRate;
                }
            }
        }
    }

    public void ballCollisions() {
        ballXPosition += BallspeedX;
        ballYPosition += BallspeedY;
        if (ballXPosition >= width - 25 || ballXPosition <= 0) {
            BallspeedX *= -1;
        }
        if (ballYPosition >= height - 50 || ballYPosition <= 0) {
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

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        try {
            background.drawImage(g);
            player.drawImage(g);
            drawLives(g);

        } catch (Exception e) {
            e.printStackTrace();
        }

        // Ball
        g.setColor(Color.RED);
        g.fillOval(ballXPosition, ballYPosition, ballSize, ballSize);

        drawScore(g);

    }

    public void drawLives(Graphics g) throws Exception {
        for (int i = 0; i <= lives - 1; i++) {
            life.setXPosition(60 + 25 * i);
            life.drawImage(g);
        }
        g.setColor(Color.BLUE);
        g.drawString("Vidas:  ", 20, 40);
        g.setColor(Color.RED);
    }

    public void drawScore(Graphics g) {
        g.setColor(Color.BLUE);
        g.drawString("Placar: " + score + " ponto(s)", width - 185, 40);
        if (bestScore > score) {
            g.drawString("Melhor Pontuação: " + bestScore + " pontos", width - 185, 80);
        } else
            g.drawString("Melhor Pontuação: " + score + " pontos", width - 185, 80);
        if (pause == true) {
            g.setColor(Color.BLACK);
            g.drawString("Aperte espaço para voltar", 200, 200);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // TODO Auto-generated method stub
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_UP)
            player.setMovingUp(true);
        if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            player.setMovingDown(true);
        }
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            pause = !pause;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_UP)
            player.setMovingUp(false);
        if (e.getKeyCode() == KeyEvent.VK_DOWN)
            player.setMovingDown(false);
    }
}