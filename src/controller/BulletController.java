package controller;

import model.Bullet;
import model.Field;
import model.Game;
import model.Tank;

import java.util.ArrayList;

public class BulletController implements Runnable, TankListener {

    private final Game game;
    private final Field field;
    private ArrayList<Bullet> bullets = new ArrayList<>();
    private ArrayList<Tank> tanks;
    private final TankController tankController;

    public BulletController(Game game, TankController tankController) {
        this.game = game;
        field = game.getField();
        tanks = tankController.getTanks();
        this.tankController = tankController;
        startListenTanks();
        new Thread(this).start();
    }



    public void addBullet(Bullet bullet){
        bullets.add(bullet);
    }

    public void removeInactiveBullets(){
        ArrayList<Bullet> temp = new ArrayList<>(bullets.size());
        for (Bullet bullet: bullets) {
            if(bullet.isBulletStatus()){
                temp.add(bullet);
            }
        }
        bullets = temp;
    }

    public ArrayList<Bullet> getBullets() {
        return bullets;
    }

    @Override
    public void run() {
        int count = 0;
        while (true){
            count++;
            synchronized (bullets){
                for (Bullet b : bullets) {
                    b.move(field, tanks);
                }
            }
            if(count%30 == 0) {
                addBulets();
                System.out.println(count);
                removeInactiveBullets();
            }
            try{
                Thread.sleep(30);
            } catch (InterruptedException e){

            }
        }

    }

    private void startListenTanks(){
        for (Tank tank: tanks) {
            tank.addListeners(this);
        }
    }

    private void addBulets(){
        for (Tank t: tanks) {
            t.addBullet();
            System.out.println(t);
        }
    }

    @Override
    public void tankInactive() {
        tanks = tankController.getTanks();
    }
}
