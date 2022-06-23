package myGame;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.awt.Color;
import java.awt.Graphics;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class Player extends JPanel {
  BufferedImage playerImage;
  String playerImagePath;
  int playerXPosition;
  int playerYPosition;
  int playerWidth;
  int playerHeight;
  int Playerspeed = 4;
  int playerSpeedRate = 1;
  boolean playerMovingDown;
  boolean playerMovingUp;

  public Player(String playerImagePath,
      int playerXPosition,
      int playerYPosition,
      int playerWidth,
      int playerHeight) {
    this.playerImagePath = playerImagePath;
    this.playerXPosition = playerXPosition;
    this.playerYPosition = playerYPosition;
    this.playerWidth = playerWidth;
    this.playerHeight = playerHeight;
    readPlayerImage();
  }

  public int getPlayerSpeedRate() {
    return playerSpeedRate;
  }

  private void readPlayerImage() {
    try {
      playerImage = ImageIO.read(new File(this.playerImagePath));
    } catch (IOException e) {
      e.printStackTrace();
    }
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

  public BufferedImage getPlayerImage() {
    return playerImage;
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

  public void drawPlayer(Graphics g) {
    g.drawImage(playerImage, playerXPosition, playerYPosition, playerWidth,
        playerHeight, null);
  }
}
