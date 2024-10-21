package pos.logic;

import javax.swing.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Proxy;
import java.net.Socket;

public class SocketListener {
    ThreadListener listener;
    String sid;
    Socket as;
    ObjectOutputStream aos;
    ObjectInputStream ais;
    public SocketListener(ThreadListener listener, String sid)throws Exception{
        this.listener= listener;
        as= new Socket(Protocol.SERVER,Protocol.PORT);
        this.sid=sid;
        aos= new ObjectOutputStream(as.getOutputStream());
        ais= new ObjectInputStream(as.getInputStream());
        aos.writeInt(Protocol.ASYNC);
        aos.writeObject(sid);
        aos.flush();
    }
    boolean condition=true;
    private Thread t;
    public void start(){
        t= new Thread(new Runnable() {
            @Override
            public void run() {listen();}});
        condition=true;
        t.start();
    }
    public void stop(){
        condition=false;
    }

    public void listen(){
        int method;
        while(condition){
            try{
                method = ais.readInt(); // Este es un punto donde el hilo podría bloquearse esperando datos
                switch (method){
                    case Protocol.DELIVER_MESSAGE :
                        try {
                            String message = (String) ais.readObject();
                            deliver(message);
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace(); // Maneja adecuadamente la excepción
                        }
                        break;
                }
            } catch (IOException e) {
                // Aquí puedes loguear el error o imprimirlo para depuración
                e.printStackTrace();
                condition = false; // Asegura que el hilo se detiene
            }
        }
        // Cerrar el socket al salir del bucle
        try {
            as.close(); // Cierra correctamente el socket
        } catch (IOException e) {
            e.printStackTrace(); // Manejar cualquier posible error de cierre
        }
    }
    public void deliver(final String message){
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {listener.deliver_message(message);}
        });
    }
}
