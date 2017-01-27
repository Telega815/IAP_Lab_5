import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;

import static java.lang.Thread.sleep;


public class Graph extends JPanel implements Runnable{
    private Socket socket;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private boolean connected;
    // Fields ---------------------------------------------------------------------------
    static double R = 200;
    ArrayList<Dot> dots = new ArrayList<>();
    static double x = 300, y = 300;


    //-------------------------
    static int xLine = 600, yLine = 600;
    //-------------------------

    static int r = (int)R;
    static int min = r/22, max = r/12, k = min, incr = 0;
    static int xCenter = xLine/2, yCenter = yLine/2;
    Color backColor = new Color(237,221,175);
    Color oldColor = new Color(0,0,0);
    Color fillColor = new Color(110, 84, 97);
    Color fitDotColor = new Color(91,200, 57);
    Color unfitDotColor = new Color(145, 35, 29);
    Color laterColor = new Color(169, 169, 169);


    // methods --------------------------------------------------------------------------
    public void setR (double R){this.R = R*20; r = (int)this.R; min = r/22; max = r/12; k = min;}
    public void setX (double x){this.x = xCenter + x*20;}
    public void setY (double y){this.y = yCenter - y*20;}
    public Graph(){
        try {
            socket = new Socket(InetAddress.getByName("127.0.0.1"), 7787);
            input = new ObjectInputStream(socket.getInputStream());
            output = new ObjectOutputStream(socket.getOutputStream());
            connected = true;
            System.out.println("connected");
        } catch (IOException e) {
            connected = false;
            //e.printStackTrace();
        }
    }

    public void addDot (){
        Dot d = new Dot (x,y);
        if(connected){
            try {
                if (dots.isEmpty()) {
                    d.checkLater = false;
                    d.fits = check(d);
                    dots.add(d);}
                else{
                    for (int i = 0; i < dots.size(); i++) {
                        if (dots.get(i).x != x || dots.get(i).y != y) {
                            d.checkLater = false;
                            d.fits = check(d);
                            dots.add(d);
                        }
                    }
                }
            }catch (Exception e){
                connected = false;
                d.checkLater = true;
                e.printStackTrace();
            }
        }
        else {
            if (dots.isEmpty()) {
               d.checkLater = true;
               dots.add(d);
            }
            else{
                for (int i = 0; i < dots.size(); i++)
                    if (dots.get(i).x != x || dots.get(i).y != y) {
                        d.checkLater = true;
                        dots.add(d);
                    }
            }
        }
    }
   
    private boolean check (Dot d){
        try{
            output.writeObject((float) (d.x - 300) / 20);
            output.writeObject((float) -(d.y - 300) / 20);
            output.writeObject((float) R / 20);
            return (boolean) input.readObject();
        }catch (Exception e){
            System.out.println("disconnected");
            connected = false;
            //e.printStackTrace();
            d.checkLater = true;
            return false;
        }
    }
    
    private void reCheck(){
        for (int i = 0; i < dots.size(); i++) {
            if (dots.get(i).checkLater == true) {
                dots.get(i).checkLater = false;
                dots.get(i).fits = check(dots.get(i));
            }
        }
    }
    
    
    public void tryToConnect(){
        try {
            socket = new Socket(InetAddress.getByName("127.0.0.1"), 7787);
            input = new ObjectInputStream(socket.getInputStream());
            output = new ObjectOutputStream(socket.getOutputStream());
            connected = true;
            reCheck();
        } catch (IOException e) {
            connected = false;
        }
    }
    

    public void removeDots () {dots.clear(); repaint();}

    @Override
    public void run() {
        while (true){
            repaint();
            try {
                sleep(100);
            }
            catch(Exception e){

            }
        }
    }


    public void paint (Graphics g) {


        int[] xArr = {xLine / 2, xLine / 2, xLine / 2 - r / 2};
        int[] yArr = {yLine / 2, yLine / 2 + r / 2, yLine / 2};


        ((Graphics2D) g).setBackground(backColor);
        g.clearRect(0, 0, 600, 600);

        // draw fill objects -------------------------------------
        g.setColor(fillColor);
        /*
        g.fillRect(xLine / 2, yLine / 2, r, r / 2);
        g.fillPolygon(xArr, yArr, 3);
        g.fillArc(xLine / 2 - r / 2, yLine / 2 - r / 2, r, r, -270, 90);
        */
    
        g.fillRect(xLine / 2 - r / 2, yLine / 2 - r, r /2, r);
        g.fillPolygon(xArr, yArr, 3);
        g.fillArc(xLine / 2 - r/2, yLine / 2 -r/2, r, r, 0, -90);
        g.setColor(oldColor);

        // draw x and y ------------------------------------------
        g.drawLine(10, yLine / 2, xLine - 10, yLine / 2);
        g.drawLine(xLine / 2, 10, xLine / 2, yLine - 10);

        // draw -> -----------------------------------------------
        g.drawLine(xLine - 10, yLine / 2, xLine - 10 - 5, yLine / 2 + 4);
        g.drawLine(xLine - 10, yLine / 2, xLine - 10 - 5, yLine / 2 - 4);
        g.drawLine(xLine / 2, 10, xLine / 2 - 4, 10 + 5);
        g.drawLine(xLine / 2, 10, xLine / 2 + 4, 10 + 5);

        // draw labels -------------------------------------------
        g.drawString("Y", xLine / 2 + 8, 20);
        g.drawString("X", xLine - 8 - 10, yLine / 2 + 15);
        String s = "-R";
        for (double i = -R; i <= R; i = i + R / 2) {
            int I = (int) i;
            g.drawLine(xLine / 2 + I, yLine / 2 - 3, xLine / 2 + I, yLine / 2 + 3);
            g.drawString(s, xLine / 2 + I - 5, yLine / 2 + 15);
            g.drawLine(xLine / 2 - 3, yLine / 2 + I, xLine / 2 + 3, yLine / 2 + I);
            g.drawString(s, xLine / 2 + 8, yLine / 2 - I + 5);
            if (i == -R) {
                s = "-R/2";
            } else if (i == -R / 2) {
                s = "";
            } else if (i == 0) {
                s = "R/2";
            } else if (i == R / 2) {
                s = "R";
            }
        }

        // draw dots --------------------------------------------
        if (!dots.isEmpty()) {
            for (int i = 0; i < dots.size(); i++) {
                if (dots.get(i).checkLater == true) g.setColor(laterColor);
                else if (dots.get(i).fits) g.setColor(fitDotColor);
                else g.setColor(unfitDotColor);
                g.fillOval((int) dots.get(i).x - k / 2, (int) dots.get(i).y - k / 2, k, k);
                g.setColor(oldColor);
                
            }
            if (k >= max) incr = 0;
            else if (k <= min) incr = 1;
            if (incr == 1) k++;
            else k--;
        }
    }

}
