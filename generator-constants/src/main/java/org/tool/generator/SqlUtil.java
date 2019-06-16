package org.tool.generator;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

public class SqlUtil {

	private static SqlUtil sqlUtil;
	private Connection conn;
	private PreparedStatement pstmt;
	private String schema;

	private SqlUtil() throws IOException, SQLException, ClassNotFoundException {
		Properties ppt = readProperties();

		String driver = ppt.getProperty("driverClassName");
		String url = ppt.getProperty("url");
		String username = ppt.getProperty("username");
		String password = ppt.getProperty("password");
		Class.forName(driver);
		conn = DriverManager.getConnection(url, username, password);
		schema = conn.getSchema();
		if(null == schema || "".equals(schema)){
			schema = conn.getCatalog();
		}
		if(null == schema || "".equals(schema)){
			schema = conn.getMetaData().getUserName();
		}
	}
	
	/**
	 * 获取schema
	 */
	public String getSchema(){
		return schema;
	}
	
	/**
	 * 单例模式获取链接
	 * @throws SQLException 
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 */
	public static SqlUtil getInstance() throws ClassNotFoundException, IOException, SQLException{
		if(null == sqlUtil){
			sqlUtil = new SqlUtil();
		}
		return sqlUtil;
	}

	/**
	 * 查询
	 * @throws SQLException 
	 */
	public ResultSet select(String sql, Object... args) throws SQLException {
		setArgs(sql, args);
		System.out.println("正在执行sql：" + sql);
		return pstmt.executeQuery();
	}

	/**
	 * 新增
	 * @throws SQLException 
	 */
	public Integer add(String sql, Object... args) throws SQLException {
		return update(sql, args);
	}

	/**
	 * 删除
	 * @throws SQLException 
	 */
	public Integer delete(String sql, Object... args) throws SQLException {
		return update(sql, args);
	}

	/**
	 * 更新
	 * @throws SQLException 
	 */
	public Integer update(String sql, Object... args) throws SQLException {
		setArgs(sql, args);
		return pstmt.executeUpdate();
	}
	
	/**
	 * 手动关闭
	 * @throws SQLException 
	 */
	public void closeByHand() throws SQLException{
		if(null != pstmt){
			pstmt.close();
		}
		if(null != conn){
			conn.close();
		}
	}

	private void setArgs(String sql, Object... args) throws SQLException {
		pstmt = conn.prepareStatement(sql);
		if(null == args){
			return;
		}
		for (int i = 0; i < args.length; i++) {
			Object obj = args[i];
			if (obj instanceof String) {
				pstmt.setString(i + 1, (String) obj);
			} else if (obj instanceof Integer) {
				pstmt.setInt(i + 1, (Integer) obj);
			} else if (obj instanceof Long) {
				pstmt.setLong(i + 1, (Long) obj);
			} else if (obj instanceof Date) {
				pstmt.setDate(i + 1, (Date) obj);
			} else if (obj instanceof Double) {
				pstmt.setDouble(i + 1, (Double) obj);
			} else if (obj instanceof Float) {
				pstmt.setFloat(i + 1, (Float) obj);
			} else if (obj instanceof Boolean) {
				pstmt.setBoolean(i + 1, (Boolean) obj);
			} else {
				pstmt.setObject(i + 1, obj);
			}
		}
	}

	private Properties readProperties() throws IOException {
		Properties pt = new Properties();
		pt.load(SqlUtil.class.getResourceAsStream("/conn.properties"));
		return pt;
	}

}
