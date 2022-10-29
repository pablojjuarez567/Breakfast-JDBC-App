package org.example.sqlConnection;

import org.example.models.Request;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;


public class RequestMySQL {
    //private static final String USER = "root";
    private static final String USER = "newuser";
    //private static final String PASSWORD = "";
    private static final String PASSWORD = "newuser";
    private static final String URL = "jdbc:mysql://localhost:3306/breakfast";

    private static Connection connection;

    private static final String GET_ALL = "SELECT * FROM request";
    private static final String GET_QUERY = "SELECT * FROM request WHERE id = ?";
    private static final String GET_A_CLIENT = "SELECT * FROM request WHERE client = ?";
    private static final String GET_BY_CLIENT = "SELECT * FROM request ORDER BY client";


    private static final String INSERT_QUERY = """
            INSERT INTO request 
            (id, date, client, delivered, product) 
            VALUES  (NULL, ?, ?, ?, ?);""";

    private static final String UPDATE_QUERY = """
            UPDATE request SET
            date = ?,
            client = ?,
            delivered = ?,
            product = ?
            WHERE id = ?;""";

    private static final String DELETE_QUERY = "DELETE FROM request WHERE id = ?";

    private static final String LAST_MONTH_QUERY = "SELECT sum(p.price) FROM product p JOIN request r on p.id = r.product WHERE month(r.date) = month(now())";


    public RequestMySQL() {

    }

    static {
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            Logger.getLogger(ProductMySQL.class.getName()).info("Conexión establecida con exito");
        } catch (SQLException ex) {
            Logger.getLogger(ProductMySQL.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    public static Request get(Integer id) {

        try (var pst = connection.prepareStatement(GET_QUERY)) {

            pst.setInt(1, id);

            ResultSet result = pst.executeQuery();

            if (result.next()) return buildRequest(result);
            else return null;

        } catch (SQLException ex) {
            Logger.getLogger(RequestMySQL.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static ArrayList<Request> getAll() {

        var all = new ArrayList<Request>();

        try (var pst = connection.prepareStatement(GET_ALL)) {

            ResultSet result = pst.executeQuery();
            while (result.next()) all.add(buildRequest(result));

        } catch (SQLException ex) {
            Logger.getLogger(RequestMySQL.class.getName()).log(Level.SEVERE, null, ex);
        }
        return all;
    }

    public static ArrayList<Request> getAllClient() {

        var all = new ArrayList<Request>();

        try (var pst = connection.prepareStatement(GET_BY_CLIENT)) {

            ResultSet result = pst.executeQuery();
            while (result.next()) all.add(buildRequest(result));

        } catch (SQLException ex) {
            Logger.getLogger(RequestMySQL.class.getName()).log(Level.SEVERE, null, ex);
        }
        return all;
    }

    public static void add(Request r) {
        try (var pst = connection.prepareStatement(INSERT_QUERY, RETURN_GENERATED_KEYS)) {

            java.util.Date now = new java.util.Date();
            java.sql.Date sqlDate = new java.sql.Date(now.getTime());

            pst.setString(1, sqlDate.toString());
            pst.setString(2, r.getClient());

            if (r.getDelivered()) {
                pst.setString(3, "1");
            } else {
                pst.setString(3, "0");
            }

            pst.setString(4, r.getProduct().getId().toString());

            if (pst.executeUpdate() > 0) {

                var keys = pst.getGeneratedKeys();
                keys.next();

                r.setId(keys.getInt(1));
            }

        } catch (SQLException ex) {
            Logger.getLogger(RequestMySQL.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void update(Request r) {
        try (var pst = connection.prepareStatement(UPDATE_QUERY)) {

            pst.setString(1, r.getDate());
            pst.setString(2, r.getClient());

            if (r.getDelivered()) {
                pst.setString(3, "1");
            } else {
                pst.setString(3, "0");
            }

            pst.setString(4, r.getProduct().getId().toString());
            pst.setInt(5, r.getId());

            if (pst.executeUpdate() == 0) {
                Logger.getLogger(RequestMySQL.class.getName()).severe("This request doesn't exist.");
            }

        } catch (SQLException ex) {
            Logger.getLogger(RequestMySQL.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void delete(Integer id) {

        try (var pst = connection.prepareStatement(DELETE_QUERY)) {

            pst.setInt(1, id);

            if (pst.executeUpdate() == 0) {
                Logger.getLogger(RequestMySQL.class.getName()).warning("This request doesn't exist.");
            }

        } catch (SQLException ex) {
            Logger.getLogger(RequestMySQL.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    public static ArrayList<Request> getRequestClient(String client) {

        try (var pst = connection.prepareStatement(GET_A_CLIENT)) {

            var all = new ArrayList<Request>();

            pst.setString(1, client);

            ResultSet result = pst.executeQuery();
            while (result.next()) all.add(buildRequest(result));

            return all;

        } catch (SQLException ex) {
            Logger.getLogger(RequestMySQL.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static HashMap<String, Integer> numberRequestByClient() {

        var all = getAll();
        var counter = new HashMap<String, Integer>();
        String client;
        Integer n;

        for (Integer i = 0; i < all.size(); i++) {
            client = all.get(i).getClient();
            n = counter.get(client);

            if (n != null) {
                counter.remove(client);
                counter.put(client, n + 1);
            } else {
                counter.put(client, 1);
            }
        }

//TODO Miguel : Borrar LOG
//        for (String name: counter.keySet()) {
//            String key = name.toString();
//            Integer value = counter.get(name);
//            System.out.println(key + " " + value);
//        }

        return counter;
    }

    public static ArrayList<String> getAllClients(ArrayList<Request> allRequest) {

        var clients = new ArrayList<String>();

        for (Integer i = 0; i < allRequest.size(); i++) {
            if (!clients.contains(allRequest.get(i).getClient())) {
                clients.add(allRequest.get(i).getClient());
            }
        }
        return clients;
    }

    public static Float lastMonthBenefit() {

        try (var pst = connection.prepareStatement(LAST_MONTH_QUERY)) {

            ResultSet result = pst.executeQuery();
            System.out.println(result);
            if (result.next()) return result.getFloat("sum(p.price)");
            else return null;

        } catch (SQLException ex) {
            Logger.getLogger(RequestMySQL.class.getName()).log(Level.SEVERE, null, ex);
        }


        Float benefit = 0f;


        return benefit;
    }


    public static ArrayList<Request> todayPendingRequest(ArrayList<Request> all) {

        var pending = new ArrayList<Request>();

        java.util.Date now = new java.util.Date();
        java.sql.Date sqlDate = new java.sql.Date(now.getTime());
        String date = sqlDate.toString();

        for (Integer i = 0; i < all.size(); i++) {
            if (all.get(i).getDate().equals(date) && all.get(i).getDelivered()) {
                pending.add(all.get(i));
            }
        }
        return pending;
    }

    public static ArrayList<Request> lastWeek(ArrayList<Request> all) {

        var pending = new ArrayList<Request>();

        java.util.Date now = new java.util.Date();
        java.sql.Date sqlDate = new java.sql.Date(now.getTime());
        String date = sqlDate.toString();

        for (Integer i = 0; i < all.size(); i++) {
            if (all.get(i).getDate().equals(date) && all.get(i).getDelivered()) {
                pending.add(all.get(i));
            }
        }

        return pending;
    }

    public static Request buildRequest(ResultSet result) {
        var request = new Request();
        try {
            request.setId(result.getInt("id"));
            request.setDate(result.getString("date"));
            request.setClient(result.getString("client"));
            request.setDelivered(result.getBoolean("delivered"));
            request.setProduct(ProductMySQL.get(result.getInt("product")));
        } catch (SQLException ex) {
            Logger.getLogger(RequestMySQL.class.getName()).log(Level.SEVERE, null, ex);
        }
        return request;
    }


}
