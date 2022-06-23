package myGame;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.awt.Color;
import java.awt.Graphics;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class GameObject extends JPanel {
  BufferedImage image;
  String imagePath;
  int xPosition;
  int yPosition;
  int width;
  int height;
  int speed = 4;

  public boolean isMovingLeft() {
    return movingLeft;
  }

  public void setMovingLeft(boolean movingLeft) {
    this.movingLeft = movingLeft;
  }

  public boolean isMovingRight() {
    return movingRight;
  }

  public void setMovingRight(boolean movingRight) {
    this.movingRight = movingRight;
  }

  int speedRate = 1;
  boolean movingDown;
  boolean movingUp;
  boolean movingLeft;
  boolean movingRight;

  public GameObject(String imagePath,
      int xPosition,
      int yPosition,
      int width,
      int height) {
    this.imagePath = imagePath;
    this.xPosition = xPosition;
    this.yPosition = yPosition;
    this.width = width;
    this.height = height;
    readImage();
  }

  public int getSpeedRate() {
    return speedRate;
  }

  private void readImage() {
    try {
      image = ImageIO.read(new File(this.imagePath));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public boolean isMovingDown() {
    return movingDown;
  }

  public void setMovingDown(boolean movingDown) {
    this.movingDown = movingDown;
  }

  public boolean isMovingUp() {
    return movingUp;
  }

  public void setMovingUp(boolean MovingUp) {
    this.movingUp = MovingUp;
  }

  public void setSpeedRate(int SpeedRate) {
    this.speedRate = SpeedRate;
  }

  public BufferedImage getImage() {
    return image;
  }

  public int getXPosition() {
    return xPosition;
  }

  public int getWidth() {
    return width;
  }

  public int getHeight() {
    return height;
  }

  public void setXPosition(int XPosition) {
    this.xPosition = XPosition;
  }

  public void setYPosition(int YPosition) {
    this.yPosition = YPosition;
  }

  public void setSpeed(int speed) {
    this.speed = speed;
  }

  public int getYPosition() {
    return yPosition;
  }

  public int getSpeed() {
    return speed;
  }

  public void draw(Graphics g) {
    g.drawImage(image, xPosition, yPosition, width,
        height, null);
  }
}
