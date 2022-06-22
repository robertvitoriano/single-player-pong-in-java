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
public class ClassPrincipal extends JPanel implements Runnable, KeyListener {
    static int width = 640;
    static int height = 480;
    int lives = 5;
    int initialLives = 5;
    BufferedImage background;
    BufferedImage livesIco;
    int bestScore;
    int speedup = 0;
    boolean pause = false;
    boolean up = false;
    boolean down = false;
    Random aleatorio = new Random();
    int taxBall = 2;
    int taxPlayer = 1;
    int BallspeedX = 5;
    int BallspeedY = 5;
    int Playerspeed = 4;
    int score = 0;
    int BallSpeedAbsoluteY;
    int BallSpeedAbsoluteX;
    int tamBola = 10;
    int tamPlayerX = 10;
    int tamPlayerY = 60;
    int posYBola = 190;
    int posXBola = 450;
    int posXPlayer = 10;
    int posYPlayer = 140;

    public static void main(String[] args) throws Exception {
        JFrame tela = new JFrame("                                                      Single Player Pong - by Robert Vitoriano");
        tela.setSize(width, height);
        tela.setVisible(true);
        tela.setLocationRelativeTo(null);
        tela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // INSTANCIAÇÃO DO CANVAS (Elemento que permite desenhos na tela)
        ClassPrincipal canvas = new ClassPrincipal();
        canvas.setBounds(0, 0, width, height);
        canvas.setVisible(true);
        // Adiçãpoo do canvas na tela principal
        // Tornar valores customizaveis
        tela.setLayout(null);
        tela.add(canvas);
        tela.setResizable(true);
        tela.addKeyListener(canvas);
    }
    public ClassPrincipal() throws Exception {
        BufferedReader reader;
        reader = new BufferedReader(new FileReader("bestScore.txt"));
        String FileContent = reader.readLine();
        bestScore = Integer.parseInt(FileContent);
        reader.close();
        try {
            background = ImageIO.read(new File("background_18.png"));
        } catch (IOException e) {
            e.printStackTrace();
            //			
        }
        try {
            livesIco = ImageIO.read(new File("heart-icon.png"));
        } catch (IOException e1) {
            System.out.println("N�o achei a imagem do cora��o");
            e1.printStackTrace();
        }
        Thread thread = new Thread(this);
        thread.start();
        //Pegando URL
    }
    public void run() {
        while (true) {
            if (pause == false) {
                long startTime = System.currentTimeMillis();
                atualizar();
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
    public void atualizar() {
        // Aqui trataremos dos movimentos
        colisoesBola();
        colisoesBolaJogador();
        movimentation();
        touchWall();
        playerColision();
        GameOver();
    }
    public void playerColision() {
        if (posYPlayer <= 0)
            posYPlayer = 0;
        else if (posYPlayer >= 380)
            posYPlayer = 380;
    }

    public void GameOver() {
        if (lives < 1) {
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
                posYBola = 190;
                posXBola = 450;
                posXPlayer = 50;
                posYPlayer = 140;
                score = 0;
                BallspeedX = 5;
                BallspeedY = 5;
                Playerspeed = 3;
                up = false;
                down = false;
            } else if (resposta == JOptionPane.NO_OPTION)
                System.exit(lives);
        }
    }
    public void touchWall() {
        if (posXBola <= 0) {
            lives -= 1;
            posYBola = aleatorio.nextInt(400) + 30;
            posXBola = aleatorio.nextInt(250) + 200;
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
        if (up) {
            posYPlayer -= Playerspeed;
        }
        if (down) {
            posYPlayer += Playerspeed;
        }
    }
    public void colisoesBolaJogador() {
        if (posXBola <= posXPlayer + tamPlayerX) {
            // Segundo if , agora referente ao eixo Y, ele precisa estar dentro de x
            // pois as 2 condições devem ser atendidas
            if (posYBola >= posYPlayer && posYBola <= posYPlayer + tamPlayerY) {
                if (bestScore < score)
                    bestScore = score;
                BallspeedX *= -1;
                BallspeedY *= -1;
                score++;
                Playerspeed += taxPlayer;
                if (score % 4 == 0) {
                    if (BallspeedY <= 0)
                        BallspeedY -= taxBall;
                    else
                        BallspeedY += taxBall;
                    if (BallspeedX <= 0)
                        BallspeedX -= taxBall;
                    else
                        BallspeedX += taxBall;
                }
            }
        }
    }

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

    public void paintComponent(Graphics g) {
        // O primeiro passo para a criação de um componente em tela é definir a cor do
        // componente.
        super.paintComponent(g);
        //BACKGROUND
        g.drawImage(background, 0, 0, width, height, null);
        //Player
        g.setColor(Color.BLUE);
        g.fillRoundRect(posXPlayer, posYPlayer, tamPlayerX, tamPlayerY, 15, 10);
        //Ball
        g.setColor(Color.RED);
        g.fillOval(posXBola, posYBola, tamBola, tamBola);
        //LIVES
        for (int i = 0; i <= lives - 1; i++) {
            g.drawImage(livesIco, 60 + 25 * i, 23, 25, 25, null);
        }
        g.setColor(Color.BLUE);
        g.drawString("Vidas:  ", 20, 40);
        g.setColor(Color.RED);

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
            up = true;
        if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            down = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            pause = !pause;
        }
    }
    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_UP)
            up = false;
        if (e.getKeyCode() == KeyEvent.VK_DOWN)
            down = false;
    }
}