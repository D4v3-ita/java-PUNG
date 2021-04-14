import java.awt.*;
import java.util.Random;

public class Modifier extends Rectangle {
    // attributi
    Random random;

    // costruttore
    Modifier(int x, int y, int width, int height){
        super(x,y,width,height);
        random = new Random();
    }

    // powerup freeze
    public void disegna1(Graphics g) {
        g.setColor(Color.cyan);
        g.fillOval(x, y, height, width);
    }

    // powerup straight
    public void disegna2(Graphics g) {
        g.setColor(Color.black);
        g.fillRect(x, y, height, width);
    }

    // powerup stratch
    public void disegna3(Graphics g) {
        g.setColor(Color.red);
        g.fillRect(x, y, height, width);
    }

    //AGGIUNGERE TIPI DI MODIFIER (ingrandisci, congela, tiradritto)
}