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

    public SocketListener(ThreadListener listener, String sid, Usuarios usuario) throws Exception {
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
        aos.writeObject(usuario);
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
                if (as.isClosed()) {
                    break;
                }
                method = ais.readInt(); // This is a point where the thread could block waiting for data
                switch (method) {
                    case Protocol.DELIVER_MESSAGE:
                        try {
                            String message = (String) ais.readObject();
                            SwingUtilities.invokeLater(new Runnable() {
                                public void run() {
                                    deliver(message);
                                }
                            });
                        } catch (ClassNotFoundException ex) {
                            ex.printStackTrace();
                        }
                        break;
                    case Protocol.NEW_CONNECTION:
                        try {
                            List<Usuarios> activeUsers = (List<Usuarios>) ais.readObject();
                            SwingUtilities.invokeLater(new Runnable() {
                                public void run() {
                                    Application.usuarioController.setList(activeUsers);
                                }
                            });
                        } catch (ClassNotFoundException ex) {
                            ex.printStackTrace();
                        }
                        break;
                    case Protocol.FACTURA_RECEIVE:
                        try {
                            Factura factura = (Factura) ais.readObject();
                            Usuarios usuario = (Usuarios) ais.readObject();
                            SwingUtilities.invokeLater(new Runnable() {
                                public void run() {
                                    Application.usuarioController.facturRecibida(factura, usuario);
                                }
                            });
                        } catch (ClassNotFoundException ex) {
                            ex.printStackTrace();
                        }
                        break;
                }
            } catch (SocketException ex) {
                if ("Socket closed".equals(ex.getMessage())) {
                    break;
                } else {
                    System.err.println("SocketException in listener: " + ex.getMessage());
                    ex.printStackTrace();
                }
            } catch (Exception ex) {
                System.err.println("Error in listener thread: " + ex.getMessage());
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