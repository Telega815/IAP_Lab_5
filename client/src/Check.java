import static java.lang.Math.abs;


public class Check {
    public static boolean check (double R, Dot sended){
        Dot d = new Dot((sended.x-300)/20, -(sended.y-300)/20);
        if (d.x>=0 && d.y <= 0) return (d.x < R && abs(d.y) < abs(R/2));
        else if (d.x>=0 && d.y >= 0) return (d.x + d.y <= R );
        else if (d.x<=0 && d.y >= 0) return (d.x*d.x + d.y*d.y <= R*R/4);
        else return false;
    }
}
