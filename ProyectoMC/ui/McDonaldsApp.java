package ui;

import dao.FinanzasDAO;
import dao.MarketingDAO;
import dao.SucursalDAO;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import model.Cliente;
import model.FinanzasReporte;
import model.MCDonaldsLocal;
import model.MarketingReporte;
import model.Sucursal;

public class McDonaldsApp {
    
    // Colores McDonald's
    private static final Color MC_ROJO = new Color(218, 41, 28);
    private static final Color MC_AMARILLO = new Color(255, 188, 13);
    private static final Color MC_BLANCO = Color.WHITE;
    private static final Color MC_GRIS = new Color(41, 41, 41);
    
    // DAOs
    private static SucursalDAO sucursalDAO = new SucursalDAO();
    private static FinanzasDAO finanzasDAO = new FinanzasDAO();
    private static MarketingDAO marketingDAO = new MarketingDAO();
    
    // Precios
    private static final float PRICE_HAMB = 3.5f;
    private static final float PRICE_PAPAS = 1.5f;
    private static final float PRICE_COMBO = 5.0f;
    private static final float FEE = 0.1f;
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> crearVentanaPrincipal());
    }
    
    private static void crearVentanaPrincipal() {
        // Modelo de negocio
        final MCDonaldsLocal mc = new MCDonaldsLocal("Sede Central - Av. Principal 1", "Director General");
        final MarketingReporte marketing = new MarketingReporte();
        final FinanzasReporte finanzas = new FinanzasReporte();
        mc.setMarketing(marketing);
        mc.setFinanzas(finanzas);
        
        // Cargar sucursales desde BD
        for (Sucursal s : sucursalDAO.obtenerTodasSucursales()) {
            mc.addSucursal(s);
        }
        
        // Frame principal
        JFrame frame = new JFrame("McDonald's - Sistema de Ventas");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1100, 800);
        
        // Panel principal con fondo
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(MC_BLANCO);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // ===== HEADER CON LOGO =====
        JPanel headerPanel = crearHeader();
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        // ===== PANEL CENTRAL =====
        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        centerPanel.setBackground(MC_BLANCO);
        
        // Panel de sucursal y cliente
        JPanel infoPanel = crearPanelInfo(frame, mc);
        centerPanel.add(infoPanel, BorderLayout.NORTH);
        
        // Panel de productos
        final Map<String, Integer> cart = new LinkedHashMap<>();
        cart.put("Hamburguesa", 0);
        cart.put("Papas", 0);
        cart.put("Combo", 0);
        
        final JLabel cartLabel = new JLabel("🛒 Carrito: vacío");
        cartLabel.setFont(new Font("Arial", Font.BOLD, 16));
        cartLabel.setForeground(MC_GRIS);
        cartLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        Runnable updateCart = () -> actualizarCarrito(cart, cartLabel);
        
        JPanel productsPanel = crearPanelProductos(cart, updateCart);
        centerPanel.add(productsPanel, BorderLayout.CENTER);
        centerPanel.add(cartLabel, BorderLayout.SOUTH);
        
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        
        // ===== PANEL INFERIOR - BOTONES =====
        JPanel bottomPanel = crearPanelBotones(frame, cart, infoPanel, mc, marketing, finanzas, updateCart);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
        
        frame.add(mainPanel);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
    
    private static JPanel crearHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(MC_ROJO);
        header.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Intentar cargar logo
        JLabel logoLabel;
        try {
            ImageIcon logoIcon = cargarImagen("resources/images/logo.png", 80, 80);
            if (logoIcon != null) {
                logoLabel = new JLabel(logoIcon);
            } else {
                logoLabel = new JLabel("🍔 McDonald's");
                logoLabel.setFont(new Font("Arial", Font.BOLD, 32));
            }
        } catch (Exception e) {
            logoLabel = new JLabel("🍔 McDonald's");
            logoLabel.setFont(new Font("Arial", Font.BOLD, 32));
        }
        
        logoLabel.setForeground(MC_AMARILLO);
        header.add(logoLabel, BorderLayout.WEST);
        
        JLabel subtitle = new JLabel("Sistema de Ventas");
        subtitle.setFont(new Font("Arial", Font.PLAIN, 18));
        subtitle.setForeground(MC_BLANCO);
        header.add(subtitle, BorderLayout.SOUTH);
        
        return header;
    }
    
    private static JPanel crearPanelInfo(JFrame frame, MCDonaldsLocal mc) {
        JPanel panel = new JPanel(new GridLayout(2, 1, 10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(MC_AMARILLO, 2),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        // Fila sucursal
        JPanel filaSucursal = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        filaSucursal.setBackground(Color.WHITE);
        
        JLabel lblSucursal = new JLabel("📍 Sucursal:");
        lblSucursal.setFont(new Font("Arial", Font.BOLD, 14));
        filaSucursal.add(lblSucursal);
        
        final JComboBox<String> comboSucursales = new JComboBox<>();
        comboSucursales.setFont(new Font("Arial", Font.PLAIN, 13));
        comboSucursales.setPreferredSize(new Dimension(250, 30));
        cargarSucursalesEnCombo(mc, comboSucursales);
        filaSucursal.add(comboSucursales);
        
        JButton btnAddSucursal = crearBoton("+ Nueva Sucursal", MC_AMARILLO, MC_GRIS);
        btnAddSucursal.addActionListener(e -> abrirDialogoNuevaSucursal(frame, mc, comboSucursales));
        filaSucursal.add(btnAddSucursal);
        
        // Fila cliente
        JPanel filaCliente = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        filaCliente.setBackground(Color.WHITE);
        
        JLabel lblNombre = new JLabel("👤 Nombre:");
        lblNombre.setFont(new Font("Arial", Font.BOLD, 14));
        filaCliente.add(lblNombre);
        
        final JTextField nombreField = new JTextField(15);
        nombreField.setFont(new Font("Arial", Font.PLAIN, 13));
        nombreField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        filaCliente.add(nombreField);
        
        JLabel lblDNI = new JLabel("🆔 DNI:");
        lblDNI.setFont(new Font("Arial", Font.BOLD, 14));
        filaCliente.add(lblDNI);
        
        final JTextField dniField = new JTextField(10);
        dniField.setFont(new Font("Arial", Font.PLAIN, 13));
        dniField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        filaCliente.add(dniField);
        
        // Guardar referencias para acceso posterior
        panel.putClientProperty("nombreField", nombreField);
        panel.putClientProperty("dniField", dniField);
        panel.putClientProperty("comboSucursales", comboSucursales);
        
        panel.add(filaSucursal);
        panel.add(filaCliente);
        
        return panel;
    }
    
    private static JPanel crearPanelProductos(Map<String, Integer> cart, Runnable updateCart) {
        JPanel panel = new JPanel(new GridLayout(1, 3, 15, 15));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        panel.setPreferredSize(new Dimension(850, 350)); // ← AGREGA ESTO
        
        // Hamburguesa
        panel.add(crearTarjetaProducto(
            "🍔 Hamburguesa",
            PRICE_HAMB,
            "resources/images/hamburguesa.png",
            e -> {
                cart.put("Hamburguesa", cart.get("Hamburguesa") + 1);
                updateCart.run();
            }
        ));
        
        // Papas
        panel.add(crearTarjetaProducto(
            "🍟 Papas Fritas",
            PRICE_PAPAS,
            "resources/images/papas.png",
            e -> {
                cart.put("Papas", cart.get("Papas") + 1);
                updateCart.run();
            }
        ));
        
        // Combo
        panel.add(crearTarjetaProducto(
            "🍔🍟 Combo",
            PRICE_COMBO,
            "resources/images/combo.png",
            e -> {
                cart.put("Combo", cart.get("Combo") + 1);
                updateCart.run();
            }
        ));
        
        return panel;
    }
    
    private static JPanel crearTarjetaProducto(String nombre, float precio, String imagePath, ActionListener action) {
        JPanel card = new JPanel(new BorderLayout(10, 10));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(MC_AMARILLO, 3, true),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
    
        // Panel superior: Imagen
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.LIGHT_GRAY); // ← Fondo gris para ver el espacio
        topPanel.setPreferredSize(new Dimension(200, 180));
    
        JLabel imgLabel;
        ImageIcon icon = cargarImagen(imagePath, 150, 150);
    
        if (icon != null) {
            System.out.println("✓ Icono cargado: " + icon.getIconWidth() + "x" + icon.getIconHeight());
            imgLabel = new JLabel(icon);
            imgLabel.setBackground(Color.YELLOW); // ← Fondo amarillo para debug
            imgLabel.setOpaque(true);
        } else {
            System.out.println("✗ Icono NULL, usando emoji");
            imgLabel = new JLabel(nombre.split(" ")[0], SwingConstants.CENTER);
            imgLabel.setFont(new Font("Arial", Font.BOLD, 50));
            imgLabel.setBackground(Color.CYAN); // ← Fondo cyan para debug
            imgLabel.setOpaque(true);
        }
    
        topPanel.add(imgLabel, BorderLayout.CENTER);
    
        // Panel inferior
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));
        bottomPanel.setBackground(Color.WHITE);
    
        JLabel lblNombre = new JLabel(nombre);
        lblNombre.setFont(new Font("Arial", Font.BOLD, 16));
        lblNombre.setForeground(MC_GRIS);
        lblNombre.setAlignmentX(Component.CENTER_ALIGNMENT);
    
        JLabel lblPrecio = new JLabel(String.format("$%.2f", precio));
        lblPrecio.setFont(new Font("Arial", Font.BOLD, 22));
        lblPrecio.setForeground(MC_ROJO);
        lblPrecio.setAlignmentX(Component.CENTER_ALIGNMENT);
    
        JButton btnAdd = crearBoton("Añadir", MC_AMARILLO, MC_GRIS);
        btnAdd.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnAdd.addActionListener(action);
    
        bottomPanel.add(Box.createVerticalStrut(5));
        bottomPanel.add(lblNombre);
        bottomPanel.add(Box.createVerticalStrut(5));
        bottomPanel.add(lblPrecio);
        bottomPanel.add(Box.createVerticalStrut(10));
        bottomPanel.add(btnAdd);
    
        card.add(topPanel, BorderLayout.CENTER);
        card.add(bottomPanel, BorderLayout.SOUTH);
    
        return card;
    }
    
    private static JPanel crearPanelBotones(JFrame frame, Map<String, Integer> cart, 
                                           JPanel infoPanel, MCDonaldsLocal mc,
                                           MarketingReporte marketing, FinanzasReporte finanzas,
                                           Runnable updateCart) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        panel.setBackground(MC_BLANCO);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JButton btnConfirmar = crearBoton("✓ Confirmar Compra", MC_ROJO, MC_BLANCO);
        btnConfirmar.setPreferredSize(new Dimension(180, 45));
        btnConfirmar.setFont(new Font("Arial", Font.BOLD, 14));
        btnConfirmar.addActionListener(e -> {
            JTextField nombreField = (JTextField) infoPanel.getClientProperty("nombreField");
            JTextField dniField = (JTextField) infoPanel.getClientProperty("dniField");
            @SuppressWarnings("unchecked")
            JComboBox<String> combo = (JComboBox<String>) infoPanel.getClientProperty("comboSucursales");
            procesarCompra(frame, cart, nombreField, dniField, combo, mc, marketing, finanzas, updateCart);
        });
        
        JButton btnMarketing = crearBoton("📊 Marketing", MC_AMARILLO, MC_GRIS);
        btnMarketing.addActionListener(e -> marketing.detalle());
        
        JButton btnFinanzas = crearBoton("💰 Finanzas", MC_AMARILLO, MC_GRIS);
        btnFinanzas.addActionListener(e -> finanzas.detalle());
        
        JButton btnEstado = crearBoton("ℹ️ Estado General", MC_AMARILLO, MC_GRIS);
        btnEstado.addActionListener(e -> {
            mc.mostrarEstado();
            mc.darReporte();
        });
        
        panel.add(btnConfirmar);
        panel.add(btnMarketing);
        panel.add(btnFinanzas);
        panel.add(btnEstado);
        
        return panel;
    }
    
    private static JButton crearBoton(String texto, Color bg, Color fg) {
        JButton btn = new JButton(texto);
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFont(new Font("Arial", Font.BOLD, 13));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(bg.darker(), 2),
            BorderFactory.createEmptyBorder(8, 15, 8, 15)
        ));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Efecto hover
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(bg.brighter());
            }
            public void mouseExited(MouseEvent e) {
                btn.setBackground(bg);
            }
        });
        
        return btn;
    }
    
    private static ImageIcon cargarImagen(String path, int width, int height) {
        System.out.println("=== INTENTANDO CARGAR: " + path + " ===");
        try {
            File imgFile = new File(path);
            String rutaAbsoluta = imgFile.getAbsolutePath();
            boolean existe = imgFile.exists();
        
            System.out.println("Ruta absoluta: " + rutaAbsoluta);
            System.out.println("¿Existe el archivo? " + existe);
        
            if (existe) {
                System.out.println("✓ Cargando imagen...");
                ImageIcon icon = new ImageIcon(rutaAbsoluta);
                Image img = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
                return new ImageIcon(img);
            } else {
                System.err.println("✗ Archivo NO encontrado");
            }
        } catch (Exception e) {
            System.err.println("✗ ERROR al cargar: " + e.getMessage());
            e.printStackTrace();
        }
        System.out.println("=== FIN intento de carga ===\n");
        return null;
    }
    
    private static void actualizarCarrito(Map<String, Integer> cart, JLabel cartLabel) {
        int qH = cart.get("Hamburguesa");
        int qP = cart.get("Papas");
        int qC = cart.get("Combo");
        
        if (qH + qP + qC == 0) {
            cartLabel.setText("🛒 Carrito: vacío");
            return;
        }
        
        float subtotal = qH * PRICE_HAMB + qP * PRICE_PAPAS + qC * PRICE_COMBO;
        float fees = (qH + qP + qC) * FEE;
        
        StringBuilder sb = new StringBuilder("🛒 ");
        if (qH > 0) sb.append("🍔x").append(qH).append(" ");
        if (qP > 0) sb.append("🍟x").append(qP).append(" ");
        if (qC > 0) sb.append("🍔🍟x").append(qC).append(" ");
        sb.append(String.format("| Subtotal: $%.2f | Total: $%.2f", subtotal, subtotal + fees));
        
        cartLabel.setText(sb.toString());
    }
    
    // Métodos auxiliares (mantener los anteriores)
    private static void cargarSucursalesEnCombo(MCDonaldsLocal mc, JComboBox<String> combo) {
        combo.removeAllItems();
        for (Sucursal s : mc.getSucursales()) {
            combo.addItem(s.toString());
        }
    }
    
    private static void abrirDialogoNuevaSucursal(JFrame parent, MCDonaldsLocal mc, JComboBox<String> combo) {
        JDialog dialog = new JDialog(parent, "Nueva Sucursal", true);
        dialog.setSize(400, 200);
        
        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        panel.add(new JLabel("Nombre:"));
        JTextField nombreField = new JTextField();
        panel.add(nombreField);
        
        panel.add(new JLabel("Dirección:"));
        JTextField dirField = new JTextField();
        panel.add(dirField);
        
        JButton btnGuardar = crearBoton("Guardar", MC_AMARILLO, MC_GRIS);
        JButton btnCancelar = crearBoton("Cancelar", Color.LIGHT_GRAY, MC_GRIS);
        
        btnGuardar.addActionListener(e -> {
            String nombre = nombreField.getText().trim();
            String dir = dirField.getText().trim();
            
            if (nombre.isEmpty() || dir.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Complete todos los campos");
                return;
            }
            
            if (sucursalDAO.insertarSucursal(dir, nombre)) {
                mc.getSucursales().clear();
                for (Sucursal s : sucursalDAO.obtenerTodasSucursales()) {
                    mc.addSucursal(s);
                }
                cargarSucursalesEnCombo(mc, combo);
                JOptionPane.showMessageDialog(dialog, "Sucursal agregada");
                dialog.dispose();
            }
        });
        
        btnCancelar.addActionListener(e -> dialog.dispose());
        
        panel.add(btnGuardar);
        panel.add(btnCancelar);
        
        dialog.add(panel);
        dialog.setLocationRelativeTo(parent);
        dialog.setVisible(true);
    }
    
    private static void procesarCompra(JFrame frame, Map<String, Integer> cart, 
                                      JTextField nombreField, JTextField dniField,
                                      JComboBox<String> comboSucursales,
                                      MCDonaldsLocal mc, MarketingReporte marketing, 
                                      FinanzasReporte finanzas, Runnable updateCart) {
        
        int qH = cart.get("Hamburguesa");
        int qP = cart.get("Papas");
        int qC = cart.get("Combo");
        int totalItems = qH + qP + qC;
        
        if (totalItems == 0) {
            JOptionPane.showMessageDialog(frame, "El carrito está vacío");
            return;
        }
        
        String nombre = nombreField.getText().trim();
        String dniStr = dniField.getText().trim();
        
        if (nombre.isEmpty() || dniStr.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Ingrese nombre y DNI");
            return;
        }
        
        int dni;
        try {
            dni = Integer.parseInt(dniStr);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(frame, "DNI inválido");
            return;
        }
        
        Object[] metodos = {"Efectivo", "Tarjeta", "Transferencia"};
        Object metodoObj = JOptionPane.showInputDialog(frame, 
            "Seleccione método de pago:", "Método de Pago",
            JOptionPane.QUESTION_MESSAGE, null, metodos, metodos[0]);
        
        if (metodoObj == null) return;
        String metodo = metodoObj.toString();
        
        if (metodo.equals("Tarjeta")) {
            String card = JOptionPane.showInputDialog(frame, 
                "Ingrese número de tarjeta (16 dígitos):");
            if (card == null) return;
            card = card.replaceAll("\\s+", "");
            if (!card.matches("\\d{16}")) {
                JOptionPane.showMessageDialog(frame, "Número de tarjeta inválido");
                return;
            }
        }
        
        Cliente cliente = new Cliente(nombre, dni);
        String selectedItem = (String) comboSucursales.getSelectedItem();
        Sucursal sucursal = obtenerSucursalDesdeCombo(mc, selectedItem);
        
        if (sucursal == null) {
            JOptionPane.showMessageDialog(frame, "Seleccione una sucursal");
            return;
        }
        
        float subtotal = qH * PRICE_HAMB + qP * PRICE_PAPAS + qC * PRICE_COMBO;
        float fees = totalItems * FEE;
        float total = subtotal + fees;
        
        String descripcion = construirDescripcionCompra(cliente, qH, qP, qC, sucursal, metodo, total);
        
        marketing.addRegistro(dni, descripcion, subtotal, fees);
        finanzas.registrarVenta(dni, subtotal, descripcion);
        finanzas.registrarGasto(dni, fees, "Gasto operativo: $" + String.format("%.2f", fees));
        
        registrarEnBaseDatos(sucursal.getId(), subtotal, fees, descripcion);
        
        JOptionPane.showMessageDialog(frame, 
            "✓ Compra confirmada\n\n" + descripcion, 
            "Éxito", JOptionPane.INFORMATION_MESSAGE);
        
        cart.put("Hamburguesa", 0);
        cart.put("Papas", 0);
        cart.put("Combo", 0);
        updateCart.run();
    }
    
    private static Sucursal obtenerSucursalDesdeCombo(MCDonaldsLocal mc, String selectedItem) {
        if (selectedItem == null || mc.getSucursales().isEmpty()) return null;
        for (Sucursal s : mc.getSucursales()) {
            if (selectedItem.equals(s.toString())) return s;
        }
        return mc.getSucursales().get(0);
    }
    
    private static String construirDescripcionCompra(Cliente cliente, int qH, int qP, int qC,
                                                    Sucursal sucursal, String metodo, float total) {
        StringBuilder desc = new StringBuilder();
        desc.append("Cliente: ").append(cliente.getNombre())
            .append(" (DNI ").append(cliente.getDni()).append(") compró ");
        
        boolean first = true;
        if (qH > 0) {
            desc.append(qH).append("x Hamburguesa");
            first = false;
        }
        if (qP > 0) {
            if (!first) desc.append(", ");
            desc.append(qP).append("x Papas");
            first = false;
        }
        if (qC > 0) {
            if (!first) desc.append(", ");
            desc.append(qC).append("x Combo");
        }
        
        desc.append(" en ").append(sucursal.getNombreSucursal())
            .append(" vía ").append(metodo)
            .append(". Total: $").append(String.format("%.2f", total));
        
        return desc.toString();
    }
    
    private static boolean registrarEnBaseDatos(int idSucursal, float subtotal, float fees, String descripcion) {
        try {
            boolean finOk = finanzasDAO.insertarRegistroFinanzas(idSucursal, fees, subtotal, descripcion);
            boolean markOk = marketingDAO.insertarRegistroMarketing(idSucursal, descripcion);
            return finOk && markOk;
        } catch (Exception e) {
            System.err.println("Error al registrar en BD: " + e.getMessage());
            return false;
        }
    }
}