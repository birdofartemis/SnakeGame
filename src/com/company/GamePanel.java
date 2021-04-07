package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import  java.util.Random;

public class GamePanel extends JPanel implements ActionListener {
    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 600;
    static final int UNITS_SIZE = 25;
    static final int GAME_UNITS = (SCREEN_WIDTH*SCREEN_HEIGHT) /UNITS_SIZE;
    static final int DELAY = 75;

    final int[] xBodyParts = new int[GAME_UNITS];
    final int[] yBodyParts = new int[GAME_UNITS];
    int bodyParts = 6;
    char direction = 'D';
    char lastDirection;

    int applesEaten;
    int appleX;
    int appleY;

    boolean running = false;
    boolean stopped = false;
    Timer timer;
    Random random;

    JButton restart = new JButton();

    GamePanel(){
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(new Color(220, 192, 139));
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();

    }

    public void startGame(){
        newApple();
        running = true;
        timer = new Timer(DELAY, this);
        timer.start();
    }
    public void paintComponent(Graphics graphics){
        super.paintComponent(graphics);
        draw(graphics);
    }

    public void draw(Graphics graphics){
        if(running) {
            graphics.setColor(Color.red);
            graphics.fillOval(appleX, appleY, UNITS_SIZE, UNITS_SIZE);

            for (int i = 0; i < bodyParts; i++) {
                if (i == 0) {
                    graphics.setColor(Color.green);
                }
                else {
                    graphics.setColor(new Color(0, 100, 0));
                }
                graphics.fillRect(xBodyParts[i], yBodyParts[i], UNITS_SIZE, UNITS_SIZE);
            }
            graphics.setColor(Color.red);
            graphics.setFont(new Font("Ink Free", Font.BOLD, 75));
            FontMetrics metrics = getFontMetrics(graphics.getFont());
            graphics.drawString("Score: " + applesEaten,
                    (SCREEN_WIDTH-metrics.stringWidth("Score: " + applesEaten))/2, graphics.getFont().getSize());

        }
        if(stopped){
            stopGame(graphics);
        }
        if(!running){
            gameOver(graphics);
        }
    }
    public void newApple(){
        appleX = random.nextInt(SCREEN_WIDTH/UNITS_SIZE)*UNITS_SIZE;
        appleY = random.nextInt(SCREEN_HEIGHT/UNITS_SIZE)*UNITS_SIZE;
    }
    public void move(){
        if(direction != 'S') {
            for (int i = bodyParts; i > 0; i--) {
                xBodyParts[i] = xBodyParts[i - 1];
                yBodyParts[i] = yBodyParts[i - 1];
            }
            switch (direction) {
                case 'R' -> xBodyParts[0] = xBodyParts[0] + UNITS_SIZE;
                case 'L' -> xBodyParts[0] = xBodyParts[0] - UNITS_SIZE;
                case 'U' -> yBodyParts[0] = yBodyParts[0] - UNITS_SIZE;
                case 'D' -> yBodyParts[0] = yBodyParts[0] + UNITS_SIZE;
            }
        }
    }
    public void checkApple(){
        if((xBodyParts[0] == appleX && yBodyParts[0] == appleY)){
            bodyParts++;
            applesEaten++;
            newApple();
        }

    }
    public void checkCollisions() {
        //Check body Collisions
        for (int i = bodyParts; i > 0; i--) {
            if ((xBodyParts[0] == xBodyParts[i]) && yBodyParts[0] == yBodyParts[i]) {
                running = false;
                break;
            }
        }
        //Check collisions with the borders;
        if (xBodyParts[0] < 0) {
            running = false;
        }
        if (xBodyParts[0] > SCREEN_WIDTH) {
            running = false;
        }
        if (yBodyParts[0] < 0) {
            running = false;
        }
        if (yBodyParts[0] > SCREEN_HEIGHT) {
            running = false;
        }
        if(!running){
            timer.stop();
        }
    }
    public void gameOver(Graphics graphics){
        graphics.setColor(Color.red);
        graphics.setFont(new Font("Ink Free", Font.BOLD, 75));
        FontMetrics metrics1 = getFontMetrics(graphics.getFont());
        graphics.drawString("Game Over", (SCREEN_WIDTH-metrics1.stringWidth("Game Over"))/2,
                SCREEN_HEIGHT/2);

        graphics.setColor(Color.red);
        graphics.setFont(new Font("Ink Free", Font.BOLD, 75));
        FontMetrics metrics2 = getFontMetrics(graphics.getFont());
        graphics.drawString("Score: " + applesEaten,
                (SCREEN_WIDTH-metrics2.stringWidth("Score: " + applesEaten))/2, graphics.getFont().getSize());
    }

    public void stopGame(Graphics graphics){
        graphics.setColor(Color.red);
        graphics.setFont(new Font("Ink Free", Font.BOLD, 75));
        FontMetrics metrics1 = getFontMetrics(graphics.getFont());
        graphics.drawString("Game Stopped", (SCREEN_WIDTH-metrics1.stringWidth("Game Stopped"))/2,
                SCREEN_HEIGHT/2);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(running){
            move();
            checkApple();
            checkCollisions();
        }
        repaint();
    }

    public class MyKeyAdapter extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent event){
            switch (event.getKeyCode()){
                case KeyEvent.VK_LEFT:{
                        if(direction != 'R'){
                            direction = 'L';
                            break;
                        }
                }
                case KeyEvent.VK_RIGHT:{
                        if(direction != 'L'){
                            direction = 'R';
                            break;
                        }
                }
                case KeyEvent.VK_UP:{
                        if(direction != 'D'){
                            direction = 'U';
                            break;
                        }
                }
                case KeyEvent.VK_DOWN:{
                        if(direction != 'U'){
                            direction = 'D';
                            break;
                        }
                }
                case KeyEvent.VK_SPACE:{
                    if(direction != 'S'){
                        lastDirection = direction;
                        direction = 'S';
                        stopped = true;
                    }
                    else{
                        direction = lastDirection;
                        stopped = false;
                    }
                }
                case KeyEvent.VK_ENTER:{
                    if (!running){

                    }
                }
            }
        }
    }
}
