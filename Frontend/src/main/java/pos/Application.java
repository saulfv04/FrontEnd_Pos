package pos;

import pos.logic.Service;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Application {

    public static JFrame window;

    public static pos.presentation.clientes.Controller clientesController;
    public static pos.presentation.producto.Controller productosController;
    public static pos.presentation.Cajeros.Controller cajerosController;
    public static pos.presentation.facturar.Controller faturasController;

    public final static int MODE_CREATE = 1;
    public final static int MODE_EDIT = 2;
    public static Border BORDER_ERROR = BorderFactory.createMatteBorder(0, 0, 2, 0, Color.RED);

    public static void main(String[] args) {

        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception ex) {
            ex.printStackTrace(); // Para depuración
        }

        window = new JFrame();
        JTabbedPane tabbedPane = new JTabbedPane();
        window.setContentPane(tabbedPane);

        window.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
//                Service.instance().stop();
            }
        });

        // Inicializar modelos y vistas
        initializeControllers(tabbedPane);


        // Configuración del JFrame
        window.setSize(900, 550);
        window.setResizable(false);
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.setIconImage((new ImageIcon(pos.Application.class.getResource("presentation/icons/icon.png"))).getImage());
        window.setTitle("POS: Point Of Sale");
        window.setLocationRelativeTo(null); // Centrar la ventana
        window.setVisible(true);

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

}