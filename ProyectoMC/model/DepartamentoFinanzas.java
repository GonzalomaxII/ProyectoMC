package model;
import java.util.ArrayList;

public abstract class DepartamentoFinanzas {
    private ArrayList<String> reporteFinanza;
    private float gastos;
    private float ingresos;

    public DepartamentoFinanzas(float gastos, float ingresos, ArrayList<String> reporteFinanza) {
        this.gastos = gastos;
        this.ingresos = ingresos;
        this.reporteFinanza = reporteFinanza;
    }

    public float getGastos() {
        return gastos;
    }

    public void setGastos(float gastos) {
        this.gastos = gastos;
    }

    public float getIngresos() {
        return ingresos;
    }

    public void setIngresos(float ingresos) {
        this.ingresos = ingresos;
    }

    public ArrayList<String> getReporteFinanza() {
        return reporteFinanza;
    }

    public void setReporteFinanza(ArrayList<String> reporteFinanza) {
        this.reporteFinanza = reporteFinanza;
    }

    public abstract void detalle();
}