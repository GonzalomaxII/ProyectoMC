package dao;

import java.sql.*;

public class MarketingDAO {
    
    public boolean insertarRegistroMarketing(int idSucursal, String reporte) {
        String query = "INSERT INTO Departamento_Marketing (id_sucursal, reporte_market) VALUES (?, ?)";
        
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, idSucursal);
            pstmt.setString(2, reporte);
            
            int filasAfectadas = pstmt.executeUpdate();
            return filasAfectadas > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al insertar registro de marketing: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    public ResultSet obtenerMarketingPorSucursal(int idSucursal) {
        String query = "SELECT * FROM Departamento_Marketing WHERE id_sucursal = ?";
        
        try {
            Connection conn = ConexionDB.getConexion();
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, idSucursal);
            return pstmt.executeQuery();
            
        } catch (SQLException e) {
            System.err.println("Error al obtener marketing: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    public int contarRegistros(int idSucursal) {
        String query = "SELECT COUNT(*) as total FROM Departamento_Marketing WHERE id_sucursal = ?";
        
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, idSucursal);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (SQLException e) {
            System.err.println("Error al contar registros: " + e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }
}
