package model;
import java.util.ArrayList;
import java.util.List;

public abstract class MCDonalds implements Comunicacion {
    private String direccion;
    private String nombreDirector;

    private List<Sucursal> sucursales;
    private DepartamentoMarketing marketing;
    private DepartamentoFinanzas finanzas;

    public MCDonalds(String direccion, String nombreDirector) {
        this.direccion = direccion;
        this.nombreDirector = nombreDirector;
        this.sucursales = new ArrayList<>();
    }

    public abstract void detalle();
    
    @Override
    public abstract void mostrarEstado();

    @Override
    public abstract void darReporte(); 

    public String getNombreDirector() {
        return nombreDirector;
    }

    public void setNombreDirector(String nombreDirector) {
        this.nombreDirector = nombreDirector;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public List<Sucursal> getSucursales() {
        return sucursales;
    }

    public void addSucursal(Sucursal sucursal) {
        this.sucursales.add(sucursal);
    }

    public DepartamentoMarketing getMarketing() {
        return marketing;
    }

    public void setMarketing(DepartamentoMarketing marketing) {
        this.marketing = marketing;
    }

    public DepartamentoFinanzas getFinanzas() {
        return finanzas;
    }

    public void setFinanzas(DepartamentoFinanzas finanzas) {
        this.finanzas = finanzas;
    }
}

