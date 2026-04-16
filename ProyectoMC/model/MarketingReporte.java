package model;
import java.util.Map;
import java.util.HashMap;

public class MarketingReporte extends DepartamentoMarketing {
    private Map<Integer, Float> ingresosPorCliente = new HashMap<>();
    private Map<Integer, Float> gastosPorCliente = new HashMap<>();
    private Map<Integer, Integer> cantidadPorCliente = new HashMap<>();

    public MarketingReporte() {
        super(new java.util.ArrayList<String>());
    }

    public void addRegistro(int dni, String r, float ingreso, float gasto) {
        getReporteMarket().add(r);
        ingresosPorCliente.put(dni, ingresosPorCliente.getOrDefault(dni, 0f) + ingreso);
        gastosPorCliente.put(dni, gastosPorCliente.getOrDefault(dni, 0f) + gasto);
        cantidadPorCliente.put(dni, cantidadPorCliente.getOrDefault(dni, 0) + 1);
    }

    @Override
    public void detalle() {
        java.util.ArrayList<String> rep = getReporteMarket();
        StringBuilder sb = new StringBuilder();
        sb.append("Reporte Marketing (agrupado por DNI):\n\n");
        if (ingresosPorCliente.isEmpty()) {
            sb.append("No hay compras registradas.\n\n");
        } else {
            for (Integer dni : ingresosPorCliente.keySet()) {
                float ing = ingresosPorCliente.getOrDefault(dni, 0f);
                float gas = gastosPorCliente.getOrDefault(dni, 0f);
                int cnt = cantidadPorCliente.getOrDefault(dni, 0);
                sb.append("DNI ").append(dni).append(": Compras ").append(cnt)
                  .append(", Ingresos $").append(String.format("%.2f", ing))
                  .append(", Gastos $").append(String.format("%.2f", gas)).append("\n");
            }
            sb.append("\n");
        }
        if (!rep.isEmpty()) {
            sb.append("Detalles:\n");
            for (String s : rep) sb.append(s).append("\n");
        }
        javax.swing.JOptionPane.showMessageDialog(null, sb.toString(), "Reporte Marketing", javax.swing.JOptionPane.INFORMATION_MESSAGE);
    }
}
