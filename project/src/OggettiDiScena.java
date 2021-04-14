import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

public class OggettiDiScena extends JPanel implements Runnable{



    //attributi
    static final int GAME_WIDTH = 1200;
    static final int GAME_HEIGHT = (int)(GAME_WIDTH * (0.5555));
    static final Dimension SCREEN_SIZE = new Dimension(GAME_WIDTH,GAME_HEIGHT);
    static final int PALLINA_DIAMETER = 20;
    static final int GIOCATORE_WIDTH = 25;
    static final int GIOCATORE_HEIGHT = 100;
    Thread gameThread;
    Image image;
    Graphics graphics;
    Random random;
    Giocatore giocatore1;
    Giocatore giocatore2;
    Pallina Pallina;
    Modifier freeze;
    Modifier stratch;
    Modifier straight;
    Tabellone tabellone;

    //costruttore
    OggettiDiScena(){
        newGiocatori();
        newPallina();
        newModifier();
        tabellone = new Tabellone(GAME_WIDTH,GAME_HEIGHT);
        this.setFocusable(true);
        this.addKeyListener(new AL());
        this.setPreferredSize(SCREEN_SIZE);
        gameThread = new Thread(this);
        gameThread.start();
    }

    //creo la pallina
    public void newPallina() {
        random = new Random();
        Pallina = new Pallina((GAME_WIDTH/2)-(PALLINA_DIAMETER/2),random.nextInt(GAME_HEIGHT-PALLINA_DIAMETER),PALLINA_DIAMETER,PALLINA_DIAMETER);
    }
    // creo i powerup
    public void newModifier() {
        random = new Random();
        freeze = new Modifier((GAME_WIDTH/2)-(PALLINA_DIAMETER/2),random.nextInt(GAME_HEIGHT-PALLINA_DIAMETER),PALLINA_DIAMETER,PALLINA_DIAMETER);
        straight = new Modifier((GAME_WIDTH/2)-(PALLINA_DIAMETER/2),random.nextInt(GAME_HEIGHT-PALLINA_DIAMETER),PALLINA_DIAMETER,PALLINA_DIAMETER);
        stratch = new Modifier((GAME_WIDTH/2)-(PALLINA_DIAMETER/2),random.nextInt(GAME_HEIGHT-PALLINA_DIAMETER),PALLINA_DIAMETER,PALLINA_DIAMETER);
    }
    //creo i giocatori
    public void newGiocatori() {
        giocatore1 = new Giocatore(0,(GAME_HEIGHT/2)-(GIOCATORE_HEIGHT/2),GIOCATORE_WIDTH,GIOCATORE_HEIGHT,1);
        giocatore2 = new Giocatore(GAME_WIDTH-GIOCATORE_WIDTH,(GAME_HEIGHT/2)-(GIOCATORE_HEIGHT/2),GIOCATORE_WIDTH,GIOCATORE_HEIGHT,2);
    }

    // metodi che disegnano i miei oggetti di scena
    public void paint(Graphics g) {
        image = createImage(getWidth(),getHeight());
        graphics = image.getGraphics();
        disegna(graphics);
        g.drawImage(image,0,0,this);
    }
    public void disegna(Graphics g) {
        giocatore1.disegna(g);
        giocatore2.disegna(g);
        Pallina.disegna(g);
        freeze.disegna1(g);
        stratch.disegna2(g);
        straight.disegna3(g);
        tabellone.disegna(g);
//        Toolkit.getDefaultToolkit().sync();

    }

    //metodo muovi che "implementa" il metodo muovi alle singole istanze
    public void muovi() {
        giocatore1.muovi();
        giocatore2.muovi();
        Pallina.muovi();
    }


    //collisioni
    public void Collisioni() {

        //la pallina colpisce le sponde
        if(Pallina.y <=0) {
            Pallina.setYDirection(-Pallina.velocitaY);
        }
        if(Pallina.y >= GAME_HEIGHT-PALLINA_DIAMETER) {
            Pallina.setYDirection(-Pallina.velocitaY);
        }
        //bounce Pallina off giocatori
        if(Pallina.intersects(giocatore1)) {
            Pallina.velocitaX = Math.abs(Pallina.velocitaX);
//            Pallina.velocitaX++;
//            if(Pallina.velocitaY>0)
//                Pallina.velocitaY++;
//            else
//                Pallina.velocitaY--;
            Pallina.setXDirection(Pallina.velocitaX);
            Pallina.setYDirection(Pallina.velocitaY);
        }
        if(Pallina.intersects(giocatore2)) {
            Pallina.velocitaX = Math.abs(Pallina.velocitaX);
//            Pallina.velocitaX++;
//            if(Pallina.velocitaY>0)
//                Pallina.velocitaY++;
//            else
//                Pallina.velocitaY--;
            Pallina.setXDirection(-Pallina.velocitaX);
            Pallina.setYDirection(Pallina.velocitaY);
        }
        //ferma i giocatori al bordo della finestra
        if(giocatore1.y<=0)
            giocatore1.y=0;
        if(giocatore1.y >= (GAME_HEIGHT-GIOCATORE_HEIGHT))
            giocatore1.y = GAME_HEIGHT-GIOCATORE_HEIGHT;
        if(giocatore2.y<=0)
            giocatore2.y=0;
        if(giocatore2.y >= (GAME_HEIGHT-GIOCATORE_HEIGHT))
            giocatore2.y = GAME_HEIGHT-GIOCATORE_HEIGHT;
        //dai al giocatore il punto e inizia un altro set
        if(Pallina.x <=0) {
            tabellone.giocatore2++;
            newGiocatori();
            newPallina();
            newModifier();
            System.out.println("Giocatore 2: "+tabellone.giocatore2);
        }
        if(Pallina.x >= GAME_WIDTH-PALLINA_DIAMETER) {
            tabellone.giocatore1++;
            newGiocatori();
            newPallina();
            newModifier();
            System.out.println("Giocatore 1: "+tabellone.giocatore1);
        }
    }



    public void run() {
        //
        long lastTime = System.nanoTime();
        double amountOfTicks =60.0; // tick al secondo
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;
        while(true) {
            long now = System.nanoTime();
            delta += (now -lastTime)/ns;
            lastTime = now;
            if(delta >=1) {
                muovi();
                Collisioni();
                repaint();
                delta--;
            }
        }
    }
    
    //classe interna per rispondere a pressione o rilascio tasti
    public class AL extends KeyAdapter{
        public void keyPressed(KeyEvent e) {
            giocatore1.keyPressed(e);
            giocatore2.keyPressed(e);
        }
        public void keyReleased(KeyEvent e) {
            giocatore1.keyReleased(e);
            giocatore2.keyReleased(e);
        }
    }
}