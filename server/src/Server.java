
import java.net.Socket;
import java.net.ServerSocket;
import java.io.*;

class S {
    public static boolean check(float x, float y, float R){
        if (x >= 0 && y <= 0) return (x*x + y*y <= R*R/4);
        else if (x<=0 && y <= 0) return (x + y >= - R / 2 );
        else if (x<=0 && y >= 0) return (x >= -R/2 && y <= R);
        else return false;
    }
}

public class Server {
    
    private int port;
    private boolean running = false;
    
    public Server(int port){
        this.port = port;
    }
    
    public void start(){
        while(true) tryToAccept();
    }
    
    private void tryToAccept(){
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("tryingToAccept!");
            Socket socket = serverSocket.accept();
            ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
            System.out.println("i sense a connection between us");
            Thread listenThread = new Thread(() -> listen(input, output));
            listenThread.start();
            running = true;
        } catch (IOException e) {
            return;
        }
    }
    
    
    private void process (float x, float y, float r, ObjectOutputStream output){
        boolean answer = S.check(x,y,r);
        try {
            output.writeObject(answer);
            System.out.println(answer);
        } catch (IOException e) {
        }
    }
    
    private void listen(ObjectInputStream input, ObjectOutputStream output){
        while (running){
            try {
                System.out.println("trying to read");
                float x = (float)input.readObject();
                float y = (float)input.readObject();
                float r = (float)input.readObject();
                System.out.println(x);
                System.out.println(y);
                System.out.println(r);
                new Thread(() -> process(x,y,r, output)).start();
            } catch (Exception e) {
                Thread.currentThread().stop();
            }
        }
    }
}
