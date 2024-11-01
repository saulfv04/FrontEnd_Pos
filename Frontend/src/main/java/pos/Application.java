package pos;

import org.apache.commons.imaging.formats.jpeg.iptc.IptcRecord;
import pos.logic.Service;
import pos.logic.Usuarios;
import pos.presentation.Usuario.Controller;
import pos.presentation.Usuario.Model;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

public class Application {


    public static JFrame window;
    public static pos.presentation.clientes.Controller clientesController;
    public static pos.presentation.producto.Controller productosController;
    public static pos.presentation.Cajeros.Controller cajerosController;
    public static pos.presentation.facturar.Controller faturasController;
    public static pos.presentation.Usuario.Controller usuarioController;  // Vista de usuario
    public static Usuarios usuario;  // Vista de usuario
    public static List<Usuarios> listaUsuariosActivos;
    public static Usuarios getUsuario() {
        return usuario;
    }

    public static void setUsuario(Usuarios usuario) {
        Application.usuario = usuario;
    }

    public final static int MODE_CREATE = 1;
    public final static int MODE_EDIT = 2;
    public static Border BORDER_ERROR = BorderFactory.createMatteBorder(0, 0, 2, 0, Color.RED);

    public static void main(String[] args) {

        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        // Autenticación
        boolean authenticated = authenticateUser(); // Método para autenticar
        if (!authenticated) {
            JOptionPane.showMessageDialog(null, "Autenticación fallida. El programa se cerrará.");
            Service.instance().exit();
            System.exit(0);  // Cerrar la aplicación si no se autenticó correctamente
        }

        // Crear la ventana principal
        window = new JFrame();
        JTabbedPane tabbedPane = new JTabbedPane();


        window.setContentPane(tabbedPane);
        window.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                Service.instance().exit();
                usuarioController.stop();

            }
        });

        initializeControllers(tabbedPane);
        initializeUsuarios(tabbedPane);
        window.setSize(900, 550);
        window.setResizable(false);
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.setIconImage((new ImageIcon(pos.Application.class.getResource("presentation/icons/icon.png"))).getImage());
        window.setLocationRelativeTo(null); // Centrar la ventana
        window.setVisible(true);
    }

    // Método para autenticar al usuario
    public  static boolean authenticateUser() {
        JPanel panel = new JPanel(new GridLayout(2, 2));
        JLabel labelId = new JLabel("ID:");
        JLabel labelClave = new JLabel("Clave:");
        JTextField fieldId = new JTextField();
        JPasswordField fieldClave = new JPasswordField();

        panel.add(labelId);
        panel.add(fieldId);
        panel.add(labelClave);
        panel.add(fieldClave);

        int option = JOptionPane.showConfirmDialog(null, panel, "Ingrese sus credenciales", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (option == JOptionPane.OK_OPTION) {
            String id = fieldId.getText();
            String clave = new String(fieldClave.getPassword());


            // Aquí llamas a tu lógica de validación de usuario
            return validateCredentials(id, clave);  // Este método debe validar contra la base de datos
        } else {
            return false;  // Usuario canceló el diálogo
        }
    }

    // Método que valida las credenciales contra la base de datos (dummy aquí)
    public static boolean validateCredentials(String id, String clave) {
        try {
            // Obtener la lista total de usuarios
            List<Usuarios> usuariosTotales = Service.instance().search(new Usuarios());
            listaUsuariosActivos = Service.instance().verificarUsuariosActivos();

            // Verificar que `usuariosTotales` no esté vacía
            if (usuariosTotales.isEmpty()) {
                return false;  // No hay usuarios en la base de datos
            } else {
                for (Usuarios usuario : usuariosTotales) {
                    // Comparar el id y la clave
                    if (usuario.getId().equals(id) && usuario.getClave().equals(clave)) {
                        // Verificar si usuariosActivos es null o está vacío
                        if (listaUsuariosActivos == null || listaUsuariosActivos.isEmpty()) {
                            setUsuario(usuario);
                            return true;  // Credenciales correctas y no hay usuarios activos
                        } else {
                            // Recorrer lista de usuarios activos
                            for (Usuarios usuarioActivo :listaUsuariosActivos) {
                                if (usuarioActivo.getId().equals(id)) {
                                    return false;
                                }
                            }
                            // Si no se encontró el usuario en usuariosActivos, se permite el acceso
                            setUsuario(usuario);
                            return true;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();  // Para depuración
        }
        return false;  // El usuario no existe, la clave no coincide o el usuario ya está activo
    }

    private static void initializeControllers(JTabbedPane tabbedPane) {
        // Inicialización de cada componente
        initializeFacturar(tabbedPane);
        initializeClientes(tabbedPane);
        initializeCajeros(tabbedPane);
        initializeProductos(tabbedPane);
        initializeEstadisticas(tabbedPane);
        initializeHistorico(tabbedPane);

    }

    private static void initializeClientes(JTabbedPane tabbedPane) {
        pos.presentation.clientes.Model clientesModel = new pos.presentation.clientes.Model();
        pos.presentation.clientes.View clientesView = new pos.presentation.clientes.View();
        clientesController = new pos.presentation.clientes.Controller(clientesView, clientesModel);
        Icon clientesIcon = new ImageIcon(pos.Application.class.getResource("/pos/presentation/icons/client.png"));
        tabbedPane.addTab("Clientes", clientesIcon, clientesView.getPanel());
    }

    private static void initializeFacturar(JTabbedPane tabbedPane) {
        pos.presentation.facturar.Model facturarModel = new pos.presentation.facturar.Model();
        pos.presentation.facturar.View facturaView = new pos.presentation.facturar.View();
        faturasController = new pos.presentation.facturar.Controller(facturaView, facturarModel);
        Icon facturarIcon = new ImageIcon(pos.Application.class.getResource("/pos/presentation/icons/shopping.png"));
        tabbedPane.addTab("Facturar", facturarIcon, facturaView.getPanel());


    }

    private static void initializeProductos(JTabbedPane tabbedPane) {
        pos.presentation.producto.Model productosModel = new pos.presentation.producto.Model();
        pos.presentation.producto.View produtoView = new pos.presentation.producto.View();
        productosController = new pos.presentation.producto.Controller(produtoView, productosModel);
        Icon productoIcon = new ImageIcon(pos.Application.class.getResource("/pos/presentation/icons/barcode.png"));
        tabbedPane.addTab("Productos", productoIcon, produtoView.getPanel());
    }

    private static void initializeEstadisticas(JTabbedPane tabbedPane) {
        pos.presentation.Estadisticas.View estadisticasView = new pos.presentation.Estadisticas.View();
        pos.presentation.Estadisticas.Model estadisticasModel = new pos.presentation.Estadisticas.Model();
        pos.presentation.Estadisticas.Controller estadisticasController = new pos.presentation.Estadisticas.Controller(estadisticasView, estadisticasModel);
        Icon estadisticasIcon = new ImageIcon(pos.Application.class.getResource("/pos/presentation/icons/bar-chart.png"));
        tabbedPane.addTab("Estadisticas", estadisticasIcon, estadisticasView.getPanel());
    }

    private static void initializeCajeros(JTabbedPane tabbedPane) {
        pos.presentation.Cajeros.Model cajerosModel = new pos.presentation.Cajeros.Model();
        pos.presentation.Cajeros.View cajerosView = new pos.presentation.Cajeros.View();
        cajerosController = new pos.presentation.Cajeros.Controller(cajerosView, cajerosModel);
        Icon cajerosIcon = new ImageIcon(pos.Application.class.getResource("/pos/presentation/icons/cashier.png"));
        tabbedPane.addTab("Cajeros", cajerosIcon, cajerosView.getPanel());
    }

    private static void initializeHistorico(JTabbedPane tabbedPane) {
        pos.presentation.Historico.View historicoView = new pos.presentation.Historico.View();
        pos.presentation.Historico.Model historicoModel = new pos.presentation.Historico.Model();
        pos.presentation.Historico.Controller historicoController = new pos.presentation.Historico.Controller(historicoView, historicoModel);
        Icon historicoIcon = new ImageIcon(pos.Application.class.getResource("/pos/presentation/icons/book.png"));
        tabbedPane.addTab("Historico", historicoIcon, historicoView.getPanel());

    }

    private  static  void initializeUsuarios(JTabbedPane tabbedPane){
        // Instanciar vista y modelo de usuario
        pos.presentation.Usuario.View usuariosView = new pos.presentation.Usuario.View();
        pos.presentation.Usuario.Model usuariosModel = new pos.presentation.Usuario.Model();
        usuarioController = new pos.presentation.Usuario.Controller(usuariosView, usuariosModel);
        // Configurar el JSplitPane con el panel de pestañas a la izquierda y el panel de usuarios a la derecha
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, tabbedPane, usuariosView.getPanel());
        splitPane.setDividerLocation(700);  // Ajustar la ubicación del divisor
        splitPane.setEnabled(false);  // Deshabilitar el movimiento del divisor

        // Establecer información del usuario (esto puede depender de tu lógica de datos)
        usuariosView.setUsuarios(usuario);

        // Actualizar el título de la ventana con el ID del usuario actual
        window.setTitle("POS: Point Of Sale - " + usuariosView.getModel().getCurrent().getId());

        // Establecer el contenido de la ventana
        window.setContentPane(splitPane);
    }

}