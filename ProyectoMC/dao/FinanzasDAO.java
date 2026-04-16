package dao;

import java.sql.*;

public class FinanzasDAO {
    
    public boolean insertarRegistroFinanzas(int idSucursal, float gastos, float ingresos, String reporte) {
        String query = "INSERT INTO Departamento_Finanzas (id_sucursal, gastos, ingresos, reporte_finanza) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, idSucursal);
            pstmt.setFloat(2, gastos);
            pstmt.setFloat(3, ingresos);
            pstmt.setString(4, reporte);
            
            int filasAfectadas = pstmt.executeUpdate();
            return filasAfectadas > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al insertar registro de finanzas: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    public ResultSet obtenerFinanzasPorSucursal(int idSucursal) {
        String query = "SELECT * FROM Departamento_Finanzas WHERE id_sucursal = ?";
        
        try {
            Connection conn = ConexionDB.getConexion();
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, idSucursal);
            return pstmt.executeQuery();
            
        } catch (SQLException e) {
            System.err.println("Error al obtener finanzas: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    public float obtenerTotalIngresos(int idSucursal) {
        String query = "SELECT SUM(ingresos) as total FROM Departamento_Finanzas WHERE id_sucursal = ?";
        
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, idSucursal);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getFloat("total");
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener total ingresos: " + e.getMessage());
            e.printStackTrace();
        }
        return 0f;
    }
    
    public float obtenerTotalGastos(int idSucursal) {
        String query = "SELECT SUM(gastos) as total FROM Departamento_Finanzas WHERE id_sucursal = ?";
        
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, idSucursal);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getFloat("total");
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener total gastos: " + e.getMessage());
            e.printStackTrace();
        }
        return 0f;
    }
}