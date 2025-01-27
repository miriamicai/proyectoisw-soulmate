package isw.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import isw.domain.AutentifCustomer;
import isw.domain.Customer;

public class CustomerDAO {

    public static void getClientes(ArrayList<Customer> lista) { //devuelve lista de Clientes
        Connection conexion =ConnectionDAO.getInstance().getConnection(); //instance de la DAO -> como objeto Connection
        //RENOMBRADO NECESARIO PARA MI TABLA
        try (PreparedStatement pst = conexion.prepareStatement("SELECT * FROM users");
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                //creo una lista con todos los clientes que aparecen en la base de datos
                /*lista.add(new Customer(rs.getInt(1), rs.getString(2), rs.getString(3),
                    rs.getString(4), rs.getString(5), rs.getString(6),
                        rs.getString(7)));*/

                //PARA ACOMODAR TABLA MARCO
                lista.add(new Customer(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getInt(5)));
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static Customer getCliente(int id) { //se usa en CustomerControler
        Connection conexion = ConnectionDAO.getInstance().getConnection();
        Customer cu = null; //es nulo
        try (PreparedStatement pst = conexion.prepareStatement("SELECT * FROM users WHERE id="+id);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                cu = new Customer(rs.getString(1),rs.getString(2), rs.getString(3), rs.getString(4),
                        rs.getInt(5));
            }

        } catch (SQLException e) {

            System.out.println(e.getMessage());
        }
        return cu; //devuelve la información del customer si coincide el id, si no será nulo
    }


    public static int getClienteLogin(String user, String password) { //se usa en CustomerControler
        Connection conexion = ConnectionDAO.getInstance().getConnection();
        int idLogged = 0;

        String query = "SELECT * FROM users WHERE usuario = ?";
        try (PreparedStatement pst = conexion.prepareStatement(query)) {
            // Sustituimos el parámetro en la consulta
            pst.setString(1, user);

            try (ResultSet rs = pst.executeQuery()) {
                // Verificamos si hay resultados
                if (rs.next()) {
                    String pss = rs.getString("contraseña");

                    AutentifCustomer autentif = new AutentifCustomer();
                    boolean verificar = autentif.VerificarLogin(user, password, pss);
                    if (verificar) {
                        idLogged = rs.getInt("id");
                    }
                } else {
                    System.out.println("Usuario no encontrado.");
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return idLogged; // Devuelve la información del customer o 0 si no se encontró
    }


    // Método para añadir usuarios a la tabla (revisado para evitar duplicacion)
    public static void addUser(String usuario, String nombre, String email, String contraseña) throws SQLException {
        Connection conexion = ConnectionDAO.getInstance().getConnection();
        String query = "INSERT INTO users (usuario, nombre, email, contraseña) VALUES (?, ?, ?, ?)";

        try (PreparedStatement pst = conexion.prepareStatement(query)) {
            pst.setString(1, usuario);
            pst.setString(2, nombre);
            pst.setString(3, email);
            pst.setString(4, contraseña);

            int rowsAffected = pst.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("User added successfully, amazing");
            } else {
                System.out.println("Failed to add user oh no");
            }
        } catch (SQLException e) {
            System.out.println("Error while adding user: " + e.getMessage());
            throw e; // rethrow exception to allow SocketServer to handle it
        }
    }

    public static void logRelease(int uid, String mid, String title, String artist, Date release){
        Connection connection = ConnectionDAO.getInstance().getConnection();

        // SQL query to insert into the user_logs table
        String sql = "INSERT INTO user_logs (user_id, external_id, release_title, release_artist, release_date) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            // Set the parameters for the query
            pstmt.setInt(1, uid); // user_id
            pstmt.setString(2, mid.equals("NO_ID") ? null : mid);
            pstmt.setString(3, title); // release_title
            pstmt.setString(4, artist); // release_artist
            if (release != null) {
                pstmt.setDate(5, (java.sql.Date) release); // release_date
            } else {
                pstmt.setNull(5, java.sql.Types.DATE); // Set NULL if no release_date
            }

            // Execute the query
            int rowsInserted = pstmt.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Release logged successfully!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error logging the release: " + e.getMessage());
        }
    }


    /*public static void main(String[] args) {

        ArrayList<Customer> lista = new ArrayList<>();
        CustomerDAO.getClientes(lista);


        for (Customer customer : lista) {
            System.out.println("He leído el id: "+customer.getId()+" con nombre: "+customer.getId());
        }


    }*/

}
