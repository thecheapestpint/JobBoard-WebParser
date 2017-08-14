package com.web.database.MySQL;

import java.sql.*;
import java.util.ArrayList;

/**
 * Created by pamitchell on 11/07/2017.
 */
public class MySQL {

    private Connection con;

    public MySQL(String db) {
        con = MySQLConfig.getConnectionURL(db);
    }

    public static MySQL instance(String db) {
        return new MySQL(db);
    }


    public ResultSet select(String query, ArrayList args){
        ResultSet resultSet = null;
        try {
            if (args != null) {
                PreparedStatement preparedStatement = prepare(query, args, false);
                resultSet = preparedStatement.executeQuery();
            } else {
                Statement stm = this.con.createStatement();
                resultSet = stm.executeQuery(query);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return resultSet;
    }


    public int rowCount(String query, ArrayList args) {
        int count = 0;
        ResultSet resultSet = select(query, args);
        try {
            count = resultSet.next() ? resultSet.getInt(1) : 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return count;
    }


    public int update(String query, ArrayList args, boolean returnLastID) {
        int last_id = 0;
        int success = 0;
        try {
            PreparedStatement preparedStatement = prepare(query, args, true);
            success = preparedStatement.executeUpdate();
            ResultSet rs = preparedStatement.getGeneratedKeys();
            if(rs.next()) {
                 last_id = rs.getInt(1);
            }
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return (returnLastID) ? last_id : success;
    }

    private PreparedStatement prepare(String query, ArrayList args, boolean update) throws SQLException {

        PreparedStatement preparedStatement = !update ? this.con.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_READ_ONLY) : this.con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

        for (int x = 0; x < args.size(); x++) {
            String type = args.get(x).getClass().getSimpleName();
            switch (type) {
                case "Integer":
                    preparedStatement.setInt(x + 1, Integer.valueOf(args.get(x).toString()));
                    break;
                case "Float":
                    preparedStatement.setFloat(x + 1, Float.valueOf(args.get(x).toString()));
                    break;
                default:
                    preparedStatement.setString(x + 1, String.valueOf(args.get(x)));
                    break;
            }
        }
        return preparedStatement;
    }


}