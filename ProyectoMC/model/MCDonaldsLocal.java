package model;
public class MCDonaldsLocal extends MCDonalds {
    public MCDonaldsLocal(String direccion, String nombreDirector) {
        super(direccion, nombreDirector);
    }

    @Override
    public void detalle() {
        StringBuilder sb = new StringBuilder();
        sb.append("Director: ").append(getNombreDirector()).append("\n");
        sb.append("Dirección central: ").append(getDireccion()).append("\n");
        sb.append("Sucursales: ").append(getSucursales().size());
        javax.swing.JOptionPane.showMessageDialog(null, sb.toString(), "Detalle McDonalds", javax.swing.JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void mostrarEstado() {
        detalle();
    }

    @Override
    public void darReporte() {
        if (getMarketing() != null) getMarketing().detalle();
        if (getFinanzas() != null) getFinanzas().detalle();
    }
}
