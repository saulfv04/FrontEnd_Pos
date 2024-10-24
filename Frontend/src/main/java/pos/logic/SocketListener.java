package pos.logic;

import pos.Application;
import pos.presentation.Usuario.TableModel;

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

    public SocketListener(ThreadListener listener, String sid, String usuario) throws Exception {
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
        aos.writeObject((String) Application.usuario.getId());
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
                        case Protocol.NEW_CONNECTION:
                            List<String> activeUsers = (List<String>) ais.readObject();
                            Application.usuarioController.setList(activeUsers);
                            break;
                        case Protocol.FACTURA_RECEIVE:
                            Factura factura = (Factura) ais.readObject();
                            String id = (String) ais.readObject();
                            Application.usuarioController.facturRecibida(factura, id);
                            break;

                    }

                } catch (Exception ex) {
                    System.err.println("Error en el hilo de escucha: " + ex.getMessage());
                    ex.printStackTrace();
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void deliver(final String message) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                listener.deliver_message(message);
            }
        });
    }

}