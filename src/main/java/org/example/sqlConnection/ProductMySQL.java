package org.example.sqlConnection;

import org.example.models.Product;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;



public class ProductMySQL {
    //private static final String USER = "root";
    private static final String USER = "newuser";
    //private static final String PASSWORD = "";
    private static final String PASSWORD = "newuser";
    private static final String URL = "jdbc:mysql://localhost:3306/breakfast";
    private static Connection connection;

    private static final String GET_ALL = "SELECT * FROM product";
    private static final String GET_QUERY = "SELECT * FROM product WHERE id = ?";
    private static final String GET_QUERY_BY_NAME = "SELECT * FROM product WHERE name = ?";



    public ProductMySQL(){

    }

    static{
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            Logger.getLogger(ProductMySQL.class.getName()).info("Conexión establecida con exito");
        } catch (SQLException ex) {
            Logger.getLogger(ProductMySQL.class.getName()).log(Level.SEVERE, null, ex);
        }
    }



    public static Product get(Integer id){

        try(var pst = connection.prepareStatement(GET_QUERY)){

            pst.setInt(1, id);

            ResultSet result = pst.executeQuery();

            if(result.next())               return buildProduct(result);
            else                            return null;

        } catch (SQLException ex) {
            Logger.getLogger(ProductMySQL.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static Product getByName(String name){

        try(var pst = connection.prepareStatement(GET_QUERY_BY_NAME)){

            pst.setString(1, name);

            ResultSet result = pst.executeQuery();

            if(result.next())               return buildProduct(result);
            else                            return null;

        } catch (SQLException ex) {
            Logger.getLogger(ProductMySQL.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static ArrayList<Product> getAll() {

        var all = new ArrayList<Product>();

        try( var pst = connection.prepareStatement(GET_ALL)){

            ResultSet result = pst.executeQuery();

            while(result.next())            all.add(buildProduct(result));

        } catch (SQLException ex) {
            Logger.getLogger(ProductMySQL.class.getName()).log(Level.SEVERE, null, ex);
        }
        return all;
    }



    public static Product buildProduct(ResultSet result){
        var product = new Product();
        try {
            product.setId(result.getInt("id") );
            product.setName(result.getString("name") );
            product.setType(result.getString("type") );
            product.setPrice(result.getFloat("price") );
            product.setAvailibity(result.getBoolean("availibity") );
        } catch (SQLException ex) {
            Logger.getLogger(ProductMySQL.class.getName()).log(Level.SEVERE, null, ex);
        }
        return product;
    }
}
