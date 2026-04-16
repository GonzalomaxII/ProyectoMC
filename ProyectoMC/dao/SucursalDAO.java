package dao;

import model.Sucursal;
import model.SucursalLocal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SucursalDAO {
    
    public List<Sucursal> obtenerTodasSucursales() {
        List<Sucursal> sucursales = new ArrayList<>();
        String query = "SELECT id_sucursal, direccion, nombre_sucursal FROM Sucursal";
        
        try (Connection conn = ConexionDB.getConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                int id = rs.getInt("id_sucursal");
                String direccion = rs.getString("direccion");
                String nombre = rs.getString("nombre_sucursal");
                sucursales.add(new SucursalLocal(id, direccion, nombre));
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener sucursales: " + e.getMessage());
            e.printStackTrace();
        }
        return sucursales;
    }
    
    public boolean insertarSucursal(String direccion, String nombre) {
        String query = "INSERT INTO Sucursal (direccion, nombre_sucursal) VALUES (?, ?)";
        
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, direccion);
            pstmt.setString(2, nombre);
            int filasAfectadas = pstmt.executeUpdate();
            return filasAfectadas > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al insertar sucursal: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    public Sucursal obtenerSucursalPorId(int id) {
        String query = "SELECT id_sucursal, direccion, nombre_sucursal FROM Sucursal WHERE id_sucursal = ?";
        
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                String direccion = rs.getString("direccion");
                String nombre = rs.getString("nombre_sucursal");
                return new SucursalLocal(id, direccion, nombre);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener sucursal: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
    
    public boolean eliminarSucursal(int id) {
        String query = "DELETE FROM Sucursal WHERE id_sucursal = ?";
        
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, id);
            int filasAfectadas = pstmt.executeUpdate();
            return filasAfectadas > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al eliminar sucursal: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
