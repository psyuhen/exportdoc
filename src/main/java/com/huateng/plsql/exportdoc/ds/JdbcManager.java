package com.huateng.plsql.exportdoc.ds;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.Logger;

import com.huateng.plsql.exportdoc.util.PropertiesReader;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

/**
 * @author Administrator
 *
 */
public class JdbcManager {
	private static Logger logger = Logger.getLogger(JdbcManager.class);
	private static HikariDataSource hikariDataSource;
	private static Connection con;
	
	static{
		PropertiesConfiguration reader = PropertiesReader.getReader();
		
		String driver = reader.getString("dataSourceClassName");
		String user = reader.getString("dataSource.user");
		String password = reader.getString("dataSource.password");
		String url = reader.getString("dataSource.url");
		
		HikariConfig hikariConfig = new HikariConfig();
		hikariConfig.setUsername(user);
		hikariConfig.setPassword(password);
		hikariConfig.setDataSourceClassName(driver);
		hikariConfig.addDataSourceProperty("url", url);
		hikariDataSource = new HikariDataSource(hikariConfig);
	}
	/**
	 * 获取连接
	 * @return
	 * @throws Exception
	 */
	public static Connection getConnection(){
		try{
			if (con==null || con.isClosed()) {
				con = hikariDataSource.getConnection();
			}
		}catch (Exception e) {
			logger.error("数据库连接异常", e);
		}
		return con;
	}
	/**
	 * 获取SQl的单条数据
	 * @param <E>
	 * @param sql
	 * @param dao
	 * @return
	 * @throws Exception
	 */
	public static <E> E  executeSql(String sql) throws Exception {
		Statement stmt = null;
		ResultSet rs = null;
		try {
			stmt = getConnection().createStatement();
			rs = stmt.executeQuery(sql);
			ResultSetMetaData metaData = rs.getMetaData();
			int columnCount = metaData.getColumnCount();
			for (int i = 1; i <= columnCount; i++) {
				String columnLabel = metaData.getColumnLabel(i);
				String columnName = metaData.getColumnName(i);
				
				System.err.println(columnLabel + "," + columnName);
			}
			
			return null;
		} catch (SQLException e) {
			logger.error("查询异常，查询语句[" + sql + "]", e);
			return null;
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stmt != null)
					stmt.close();
			} catch (Exception e) {
				logger.error("statement关闭异常", e);
			}
		}
	}
	
	public static <E> List<E>  queryForList(String sql, Object[] params, AbstractDao<E> abstractDao){
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = getConnection().prepareStatement(sql);
			if(params != null && params.length > 0){
				for (int i = 1; i <= params.length; i++) {
					stmt.setObject(i, params[i-1]);
				}
			}
			rs = stmt.executeQuery();
			List<E> list = new ArrayList<>();
			while(rs.next()){
				list.add(abstractDao.mapping(rs));
			}
			
			return list;
		} catch (SQLException e) {
			logger.error("查询异常，查询语句[" + sql + "]", e);
			return null;
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stmt != null)
					stmt.close();
			} catch (Exception e) {
				logger.error("statement关闭异常", e);
			}
		}
	}
	
}
