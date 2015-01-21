/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Administrator
 */
import java.sql.*;
import java.util.*;

public class DBConn {

	private static String driver = "com.mysql.jdbc.Driver"; // 数据库驱动

	private static String url = "jdbc:mysql://localhost:3306/apriori"; // 连接url

	private static String dbUser = "root"; // 数据库用户名

	private static String dbPwd = ""; // 数据库密码

	public static Connection conn = null;



	// 构造方法，创建数据库连接
	public DBConn() {
		try {
			if (conn == null) {
				Class.forName(driver); // 加载数据库驱动
				conn = DriverManager.getConnection(url, dbUser, dbPwd); // 建立数据库连接
			} else
				return;
		} catch (Exception ee) {
			ee.printStackTrace();
		}
	}

	// 执行数据库查询操作
	public static ResultSet executeQuery(String sql) {
		try {
			if (conn == null)
				new DBConn();
			return conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
					ResultSet.CONCUR_UPDATABLE).executeQuery(sql); // 执行数据库查询
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}


	// 执行数据库更新操作
	public static int executeUpdate(String sql) {

		try {
			if (conn == null)
				new DBConn();
			return conn.createStatement().executeUpdate(sql); // 执行数据库更新
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			return -1;
		} finally {

		}
	}



	public static void close() {
		try {
			conn.close();
		}
                catch (SQLException e) {
			e.printStackTrace();
		}
                finally {
			conn = null;
		}
	}


}
