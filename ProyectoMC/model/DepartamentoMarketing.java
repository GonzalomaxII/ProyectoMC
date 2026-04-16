package model;
import java.util.ArrayList;

public abstract class DepartamentoMarketing {
    private ArrayList<String> reporteMarket;

    public DepartamentoMarketing(ArrayList<String> reporteMarket) {
        this.reporteMarket = reporteMarket;
    }

    public ArrayList<String> getReporteMarket() {
        return reporteMarket;
    }

    public void setReporteMarket(ArrayList<String> reporteMarket) {
        this.reporteMarket = reporteMarket;
    }

    public abstract void detalle();
}