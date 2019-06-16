package org.tool.generator;
import java.sql.ResultSet;

public class GeneratorConstantsTool {
	
	private static String lineSeparator = System.getProperty("line.separator");
	
	private static String prefix = "static final int & = %;";
	
	private static String fileName = "Constants";
	
	public static void main(String[] args) throws Exception {
		//读取数据库配置
		SqlUtil util = SqlUtil.getInstance();
		//读取数据字典表数据
		String sql = "select t.dict_type,t.dict_key,t.dict_value,t.dict_desc from tb_system_dict t order by t.dict_type,t.dict_key";
		ResultSet rs = util.select(sql);
		StringBuffer buf = new StringBuffer("public class " + fileName + " {");
		while(rs.next()){
			String dictType = rs.getString("dict_type");
			int dictKey = rs.getInt("dict_key");
			String dictValue = rs.getString("dict_value");
			String dictDesc = rs.getString("dict_desc");
			
			String prefixBak = prefix;
			prefixBak = prefixBak.replace("&", dictType.toUpperCase() + "_" + dictKey);
			prefixBak = prefixBak.replace("%", String.valueOf(dictKey));
			//拼接内容
			buf.append(lineSeparator);
			buf.append("/**");
			buf.append(lineSeparator);
			buf.append(" * " + dictDesc + "（" + dictKey + "-" + dictValue + ")");
			buf.append(lineSeparator);
			buf.append(" */");
			buf.append(lineSeparator);
			buf.append(prefixBak);
		}
		buf.append(lineSeparator);
		buf.append("}");
		//根据数据生成常量类文件
		//输出内容到文件
		String path = "src/main/java/" + fileName + ".java";
		MyFileUtil.writeToFile(path, buf.toString());
	}

}
