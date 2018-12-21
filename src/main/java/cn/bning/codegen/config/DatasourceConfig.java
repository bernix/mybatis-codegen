package cn.bning.codegen.config;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author Bernix Ning
 * @date 2018-12-14
 */
@Getter
@Setter
@Builder
public class DatasourceConfig {

    private static final String DRIVER_NAME = "com.mysql.cj.jdbc.Driver";

    private String url;
    private String username;
    private String pwd;
    private String db;

    public Connection getConnection() {
        Connection conn = null;
        try {
            Class.forName(DRIVER_NAME);
            conn = DriverManager.getConnection(url, username, pwd);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }
}
