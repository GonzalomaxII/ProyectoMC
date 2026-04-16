package model;
import java.util.Map;
import java.util.HashMap;

public class FinanzasReporte extends DepartamentoFinanzas {
    // agrego acumulaciones por cliente
    private Map<Integer, Float> ingresosPorCliente = new HashMap<>();
    private Map<Integer, Float> gastosPorCliente = new HashMap<>();

    public FinanzasReporte() {
        super(0f, 0f, new java.util.ArrayList<String>());
    }

    // registrar ingreso por cliente (acumula)
    public void registrarVenta(int dni, float ingreso, String desc) {
        setIngresos(getIngresos() + ingreso);
        getReporteFinanza().add(desc + " - Ingreso: " + ingreso);
        ingresosPorCliente.put(dni, ingresosPorCliente.getOrDefault(dni, 0f) + ingreso);
    }

    // registrar gasto por cliente (acumula)
    public void registrarGasto(int dni, float gasto, String desc) {
        setGastos(getGastos() + gasto);
        getReporteFinanza().add(desc + " - Gasto: " + gasto);
        gastosPorCliente.put(dni, gastosPorCliente.getOrDefault(dni, 0f) + gasto);
    }

    @Override
    public void detalle() {
        StringBuilder sb = new StringBuilder();
        sb.append("Ingresos totales: ").append(getIngresos()).append("\n");
        sb.append("Gastos totales: ").append(getGastos()).append("\n\n");
        if (ingresosPorCliente.isEmpty() && gastosPorCliente.isEmpty()) {
            sb.append("No hay movimientos financieros.\n");
        } else {
            sb.append("Detalle por DNI:\n");
            // mostrar todas las claves encontradas en ambas maps
            java.util.Set<Integer> claves = new java.util.HashSet<>();
            claves.addAll(ingresosPorCliente.keySet());
            claves.addAll(gastosPorCliente.keySet());
            for (Integer dni : claves) {
                float ing = ingresosPorCliente.getOrDefault(dni, 0f);
                float gas = gastosPorCliente.getOrDefault(dni, 0f);
                sb.append("DNI ").append(dni).append(": Ingresos $").append(String.format("%.2f", ing))
                  .append(", Gastos $").append(String.format("%.2f", gas)).append("\n");
            }
            sb.append("\nMovimientos:\n");
            for (String s : getReporteFinanza()) sb.append(s).append("\n");
        }
        javax.swing.JOptionPane.showMessageDialog(null, sb.toString(), "Reporte Finanzas", javax.swing.JOptionPane.INFORMATION_MESSAGE);
    }
}
