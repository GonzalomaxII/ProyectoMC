package model;

public class SucursalLocal extends Sucursal {
    
    // Constructor con 2 parámetros (el original)
    public SucursalLocal(int id, String direccion) {
        super(id, direccion);
    }
    
    // Constructor con 3 parámetros (ESTE ES EL QUE FALTABA)
    public SucursalLocal(int id, String direccion, String nombreSucursal) {
        super(id, direccion, nombreSucursal);
    }

    @Override
    public void detalle() {
        javax.swing.JOptionPane.showMessageDialog(
            null,
            "Sucursal: " + getNombreSucursal() + 
            "\nID: " + getId() + 
            "\nDirección: " + getDireccion(),
            "Detalle Sucursal",
            javax.swing.JOptionPane.INFORMATION_MESSAGE
        );
    }

    @Override
    public String toString() {
        return getNombreSucursal() + " - " + getDireccion();
    }
}