package myGame;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class Player extends JPanel {
  BufferedImage player;
  int playerXPosition = 10;
  int playerYPosition = 140;
  int playerWidth = 10;
  int playerHeight = 60;
  int Playerspeed = 4;
  boolean playerMovingDown;
  boolean playerMovingUp;

  public int getPlayerSpeedRate() {
    return playerSpeedRate;
  }

  public boolean isPlayerMovingDown() {
    return playerMovingDown;
  }

  public void setPlayerMovingDown(boolean playerMovingDown) {
    this.playerMovingDown = playerMovingDown;
  }

  public boolean isPlayerMovingUp() {
    return playerMovingUp;
  }

  public void setPlayerMovingUp(boolean playerMovingUp) {
    this.playerMovingUp = playerMovingUp;
  }

  public void setPlayerSpeedRate(int playerSpeedRate) {
    this.playerSpeedRate = playerSpeedRate;
  }

  int playerSpeedRate = 1;

  public Player() {
    try {
      player = ImageIO.read(new File("small-mario.png"));
    } catch (IOException e) {
      e.printStackTrace();
    }

  }

  public BufferedImage getPlayer() {
    return player;
  }

  public int getPlayerXPosition() {
    return playerXPosition;
  }

  public int getPlayerWidth() {
    return playerWidth;
  }

  public int getPlayerHeight() {
    return playerHeight;
  }

  public void setPlayerXPosition(int playerXPosition) {
    this.playerXPosition = playerXPosition;
  }

  public void setPlayerYPosition(int playerYPosition) {
    this.playerYPosition = playerYPosition;
  }

  public void setPlayerspeed(int playerspeed) {
    Playerspeed = playerspeed;
  }

  public int getPlayerYPosition() {
    return playerYPosition;
  }

  public int getPlayerspeed() {
    return Playerspeed;
  }

  // public void paintComponent(Graphics g) {
  //   super.paintComponent(g);
  //   g.drawImage(player, playerXPosition, playerYPosition, playerWidth, playerHeight, null);
  // }

}
  