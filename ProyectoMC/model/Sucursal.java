package model;

public class Sucursal {
    private String direccion;
    private int id;
    private String nombreSucursal;

    public Sucursal(int id, String direccion) {
        this.id = id;
        this.direccion = direccion;
        this.nombreSucursal = "Sucursal " + id;
    }
    
    public Sucursal(int id, String direccion, String nombreSucursal) {
        this.id = id;
        this.direccion = direccion;
        this.nombreSucursal = nombreSucursal;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
    
    public String getNombreSucursal() {
        return nombreSucursal;
    }
    
    public void setNombreSucursal(String nombreSucursal) {
        this.nombreSucursal = nombreSucursal;
    }
    
    public void detalle() {
        javax.swing.JOptionPane.showMessageDialog(
            null,
            "Sucursal: " + nombreSucursal + "\nID: " + id + "\nDirección: " + direccion,
            "Detalle Sucursal",
            javax.swing.JOptionPane.INFORMATION_MESSAGE
        );
    }
    
    @Override
    public String toString() {
        return nombreSucursal + " - " + direccion;
    }
}