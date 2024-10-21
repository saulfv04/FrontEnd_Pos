package pos.logic;

import javax.swing.*;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Proxy;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

public class SocketListener {
    ThreadListener listener;
    String sid;
    Socket as;
    ObjectOutputStream aos;
    ObjectInputStream ais;

    public SocketListener(ThreadListener listener, String sid) throws Exception {
        this.listener = listener;

        // Inicializar el socket primero
        as = new Socket(Protocol.SERVER, Protocol.PORT);

        // Invertir el orden de inicialización de los streams
        aos = new ObjectOutputStream(as.getOutputStream());
        aos.flush(); // Asegúrate de enviar cualquier encabezado necesario
        ais = new ObjectInputStream(as.getInputStream());


        this.sid = sid;
        aos.writeInt(Protocol.ASYNC);
        aos.writeObject(sid);
        aos.flush();
    }

    boolean condition = true;
    private Thread t;

    public void start() {
        t = new Thread(new Runnable() {
            public void run() {
                listen();
            }
        });
        condition = true;
        t.start();
    }

    public void stop() {
        condition = false;
        try {
            // Cerrar el socket para liberar el hilo en caso de que esté bloqueado
            as.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void listen() {
        int method;
        try {
            while (condition) {
                try {
                    method = ais.readInt(); // Este es un punto donde el hilo podría bloquearse esperando datos
                    switch (method) {
                        case Protocol.DELIVER_MESSAGE:
                            String message = (String) ais.readObject();
                            deliver(message);
                            break;
                        // Maneja otros casos según sea necesario
                    }
                } catch (ClassNotFoundException e) {
                    System.err.println("Clase no encontrada al leer el objeto: " + e.getMessage());
                    e.printStackTrace();
                } catch (SocketException e) {
                    System.out.println("Error de socket, posiblemente el servidor se desconectó.");
                    condition = false; // Detener el hilo si ocurre un error en el socket
                } catch (IOException e) {
                    System.err.println("Error de entrada/salida: " + e.getMessage());
                    e.printStackTrace();
                    condition = false; // Asegura que el hilo se detenga en caso de otros errores
                }
            }
        } catch (Exception ex) {
            System.err.println("Error en el hilo de escucha: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
    public void deliver(final String message) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                listener.deliver_message(message);
            }
        });
    }
    public List<String> getActiveUsers() {
        List<String> activeUsers = new ArrayList<>();
        activeUsers= Service.instance().usuariosActivos();
        return activeUsers;
    }
}