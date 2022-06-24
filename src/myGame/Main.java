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
    int bestScore;
    boolean pause = false;
    Random randomNumber = new Random();
    int score = 0;
    Clip mainMusic;
    GameObject player;
    GameObject life;
    GameObject background;
    GameObject ball;
    MusicPlayer musicPlayer;

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

        musicPlayer = new MusicPlayer();
        musicPlayer.addMusic("gameMusic.wav", "mainMusic");
        musicPlayer.loop("mainMusic");
        musicPlayer.addMusic("loseSound.wav", "loseSound");
        musicPlayer.addMusic("ballHitWallSound.wav", "hitWallSound");
        musicPlayer.addMusic("hitBallSound.wav", "hitBallSound");

        background = new Background("background.png", 0, 0, width, height);
        player = new GameObject("small-mario.png", 10, 140, 60, 60);
        life = new GameObject("heart-icon.png", 0, 23, 25, 25);
        ball = new GameObject(Color.BLUE, 450, 190, 10, 10);
        ball.setSpeedX(5);
        ball.setSpeedY(5);
        ball.setSpeedRate(2);
        ball.setSize(10);
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
                musicPlayer.stop("mainMusic");
                musicPlayer.play("loseSound");
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            if (score > bestScore) {
                FileWriter writer;
                try {
                    writer = new FileWriter("bestScore.txt");
                    writer.write("" + score);
                    writer.close();
                } catch (IOException e) {
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
                restartGame();
            } else if (resposta == JOptionPane.NO_OPTION)
                System.exit(lives);
        }
    }

    public void restartGame() {
        musicPlayer.loop("mainMusic");
        lives = initialLives;
        score = 0;
        ball.setXPosition(450);
        ball.setYPosition(190);
        ball.setSpeedX(5);
        ball.setSpeedY(5);
        ball.setSpeedRate(2);
        player.setXPosition(50);
        player.setYPosition(140);
        player.setSpeed(3);
        player.setMovingUp(false);
        player.setMovingDown(false);
    }

    public void touchWall() {
        if (ball.getXPosition() <= 0) {
            lives -= 1;
            ball.setYPosition(randomNumber.nextInt(400) + 30);
            ball.setXPosition(randomNumber.nextInt(250) + 200);
            musicPlayer.play("hitWallSound");

        }
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
        if (ball.getXPosition() <= player.getXPosition() + player.getWidth()) {
            if (ball.getYPosition() >= player.getYPosition()
                    && ball.getYPosition() <= player.getYPosition() + player.getHeight()) {
                if (bestScore < score)
                    bestScore = score;
                ball.setSpeedX(ball.getSpeedX() * -1);
                ball.setSpeedY(ball.getSpeedY() * -1);
                musicPlayer.play("hitBallSound");

                score++;
                player.setSpeed(player.getSpeed() + player.getSpeedRate());
                if (score % 4 == 0) {
                    if (ball.getSpeedY() <= 0)
                        ball.setSpeedY(ball.getSpeedY() - ball.getSpeedRate());
                    else
                        ball.setSpeedY(ball.getSpeedY() + ball.getSpeedRate());
                    if (ball.getSpeedX() <= 0)
                        ball.setSpeedX(ball.getSpeedX() - ball.getSpeedRate());
                    else
                        ball.setSpeedX(ball.getSpeedX() + ball.getSpeedRate());
                }
            }
        }
    }

    public void ballCollisions() {
        ball.setXPosition(ball.getXPosition() + ball.getSpeedX());
        ball.setYPosition(ball.getYPosition() + ball.getSpeedY());
        if (ball.getXPosition() >= width - 25 || ball.getXPosition() <= 0) {
            ball.setSpeedX(ball.getSpeedX() * -1);
        }
        if (ball.getYPosition() >= height - 50 || ball.getYPosition() <= 0) {
            ball.setSpeedY(ball.getSpeedY() * -1);
        }
        if (ball.getSpeedX() < 0)
            ball.setSpeedAbsoluteX(-ball.getSpeedX());
        else
            ball.setSpeedAbsoluteX(ball.getSpeedX());
        if (ball.getSpeedY() < 0)
            ball.setSpeedAbsoluteY(-ball.getSpeedY());
        else
            ball.setSpeedAbsoluteY(ball.getSpeedY());
    }

    public void FPS() {
        try {
            Thread.sleep(1000 / 60);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        try {
            background.drawImage(g);
            player.drawImage(g);
            drawLives(g);
            drawScore(g);
            ball.drawOval(g);
        } catch (Exception e) {
            e.printStackTrace();
        }
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