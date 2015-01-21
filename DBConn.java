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

	private static String driver = "com.mysql.jdbc.Driver"; // ���ݿ�����

	private static String url = "jdbc:mysql://localhost:3306/apriori"; // ����url

	private static String dbUser = "root"; // ���ݿ��û���

	private static String dbPwd = ""; // ���ݿ�����

	public static Connection conn = null;



	// ���췽�����������ݿ�����
	public DBConn() {
		try {
			if (conn == null) {
				Class.forName(driver); // �������ݿ�����
				conn = DriverManager.getConnection(url, dbUser, dbPwd); // �������ݿ�����
			} else
				return;
		} catch (Exception ee) {
			ee.printStackTrace();
		}
	}

	// ִ�����ݿ��ѯ����
	public static ResultSet executeQuery(String sql) {
		try {
			if (conn == null)
				new DBConn();
			return conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
					ResultSet.CONCUR_UPDATABLE).executeQuery(sql); // ִ�����ݿ��ѯ
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}


	// ִ�����ݿ���²���
	public static int executeUpdate(String sql) {

		try {
			if (conn == null)
				new DBConn();
			return conn.createStatement().executeUpdate(sql); // ִ�����ݿ����
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
