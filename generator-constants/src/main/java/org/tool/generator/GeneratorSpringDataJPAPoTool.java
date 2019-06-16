package org.tool.generator;
import java.io.File;
import java.sql.ResultSet;

/**
 * mysql的语法
 * 生成hibernate的po类，没有get和set方法，使用lombok的注解
 * @author liuzhsh 2017年8月28日
 */
public class GeneratorSpringDataJPAPoTool {
	
	private static String lineSeparator = System.getProperty("line.separator");
	
	private static String packageName = "com.zanfq.model.po";
	
	private static String fieldTpl = "";
	
	static{
		//初始化两个模板
		StringBuffer fields = new StringBuffer();
		fields
			.append("!")
			.append("	private & %;" + lineSeparator);
		fieldTpl = fields.toString();
	}
	
	public static void main(String[] args) throws Exception  {
		//不支持联合主键
		System.out.println("开始生成。。。。。");
		String[] tables = {"platform_role_resource"};
		for (String table : tables) {
			generator(table);
		}
		System.out.println("生成结束。。。。。");
	}

	private static void generator(String table) throws Exception{
		System.out.println("▽▽▽正在生成，表名：" + table + "。▽▽▽");
		//读取数据库配置
		SqlUtil util = SqlUtil.getInstance();
		//读取表注释信息
		String tableSql = "select table_comment from information_schema.TABLES where TABLE_SCHEMA='" + util.getSchema() + "' and TABLE_NAME= '" + table + "'";
		ResultSet tableRs = util.select(tableSql);
		String tableComments = "";//表注释
		while(tableRs.next()){
			tableComments = tableRs.getString("table_comment");
		}
		tableComments = tableComments.replace("\r", ",").replace("\n", ",");
		System.out.println("正在生成，表注释：" + tableComments);
		//读取表主键信息
		String consSql = "SELECT c.COLUMN_NAME FROM INFORMATION_SCHEMA.TABLE_CONSTRAINTS AS t, INFORMATION_SCHEMA.KEY_COLUMN_USAGE AS c "
				+ "WHERE t.TABLE_NAME = c.TABLE_NAME AND t.TABLE_SCHEMA = '" + util.getSchema()
				+ "' and t.table_name = '" + table + "' AND t.CONSTRAINT_TYPE = 'PRIMARY KEY'";
		ResultSet consRs = util.select(consSql);
		String pkColumn = "";
		while(consRs.next()){
			pkColumn = consRs.getString("column_name");
			break;//TODO:联合主键目前取第一个，待修改
		}
		System.out.println("正在生成，表主键字段：" + (pkColumn.equals("") ? "没有主键" : pkColumn));
		//读取表字段信息
		String sql = "select column_name,data_type,column_type,column_default,is_nullable,column_comment "
				+ "from information_schema.COLUMNS where TABLE_SCHEMA='" + util.getSchema() + "' and TABLE_NAME= '" + table + "'";
		ResultSet rs = util.select(sql);
		StringBuffer allFields = new StringBuffer("");
		while(rs.next()){
			String columnName = rs.getString("column_name");
			String dataType = rs.getString("data_type");
			String columnType = rs.getString("column_type");
			String columnDefault = rs.getString("column_default");
			String nullable = rs.getString("is_nullable");
			String columnComment = rs.getString("column_comment");
			if(null == columnComment || "".equals(columnComment)){
				columnComment = columnName;
			} else {
				columnComment = rs.getString("column_comment").replace("\r", ",").replace("\n", ",");
			}
			
			String fieldName = StringUtil.humpColumn(columnName);
			
			//拼接fields
			Boolean isPk = pkColumn.equals(columnName);
			String fields = genField(columnName,fieldName,dataType,columnType,columnDefault,nullable,columnComment,isPk);
			allFields.append(fields);
		}
		System.out.println("正在生成：读取表字段信息完成。");
		//读取模板文件spring-data-jpa-mdl.tpl
		String mdlName = StringUtil.humpTable(table);
		StringBuffer fileContent = MyFileUtil.readFile("D:/work/workspace/generator-constants/src/main/resources/spring-data-jpa-mdl.tpl");
		String output = fileContent.toString()
				.replace("#packageName#", packageName)
				.replace("#tableComments#", tableComments)
				.replace("#tableName#", table)
				.replace("#allFields#", allFields.toString())
				.replace("#mdlName#", mdlName);
		//将包名替换成文件夹路径
		String path = "D:/work/workspace/generator-constants/src/main/java/" + packageName.replace(".", File.separator);
		MyFileUtil.createDir(path);
		//输出内容到文件
		String filePath = path + File.separator + mdlName + ".java";
		MyFileUtil.writeToFile(filePath, output);
		System.out.println("△△△表:" + table + ",生成完成。△△△");
	}

	/**
	 * 生成属性，例如：private String name;
	 */
	private static String genField(String columnName, String fieldName, String dataType,
			String columnType, String columnDefault, String nullable, String columnComment, Boolean isPk) {
		String javaType = getJavaType(dataType,columnType);
		boolean required = false;
		String requiredStr = "";
		if(nullable.equalsIgnoreCase("NO")){
			required = true;
			requiredStr = ", required = true";
		}
		StringBuffer methodStr = new StringBuffer();
		methodStr.append("    @ApiModelProperty(value = \"" + columnComment + "\"" + requiredStr + ")" + lineSeparator);
		if(isPk){
			methodStr
				.append("    @Id" + lineSeparator)
				.append("    @GeneratedValue(strategy = GenerationType.AUTO)" + lineSeparator);
		}
		if(dataType.equalsIgnoreCase("timestamp") || dataType.equalsIgnoreCase("datetime")){
			methodStr.append("    @JsonSerialize(using = CustomDateTimeJsonSerializer.class)" + lineSeparator);
			methodStr.append("    @Temporal(TemporalType.TIMESTAMP)" + lineSeparator);
		}
		if(required && !isPk){
			if(javaType.equals("String")){
				methodStr.append("    @NotBlank" + lineSeparator);
			} else {
				methodStr.append("    @NotNull" + lineSeparator);
			}
		}
		methodStr.append("    @Column(name = \"" + columnName +"\")" + lineSeparator);
		String replaceOne = fieldTpl.replace("#", columnComment).replace("!", methodStr).replace("&", javaType).replace("%", fieldName);
		return replaceOne;
	}
	/**
	 * 根据oracle的data_type判断javaType
	 */
	private static String getJavaType(String dataType, String columnType){
		String javaType = "";
		if(dataType.equalsIgnoreCase("int")){
			String length = columnType.substring(columnType.indexOf("(") + 1, columnType.indexOf(")"));
			if(Integer.valueOf(length) > 9){
				javaType = "Long";
			} else {
				javaType = "Integer";
			}
		} else if(dataType.equalsIgnoreCase("smallint")){
			javaType = "Integer";
		} else if(dataType.equalsIgnoreCase("tinyint")){
			javaType = "Integer";
		} else if(dataType.equalsIgnoreCase("mediumint")){
			javaType = "Integer";
		} else if(dataType.equalsIgnoreCase("bigint")){
			javaType = "Long";
		} else if(dataType.equalsIgnoreCase("decimal")){
			javaType = "Double";
		} else if(dataType.equalsIgnoreCase("double")){
			javaType = "Double";
		} else if(dataType.equalsIgnoreCase("float")){
			javaType = "Float";
		} else if(dataType.equalsIgnoreCase("date")){
			javaType = "Date";
		} else if(dataType.equalsIgnoreCase("datetime")){
			javaType = "Date";
		} else if(dataType.equalsIgnoreCase("TIMESTAMP")){
			javaType = "Date";
		} else if(dataType.equalsIgnoreCase("VARCHAR")){
			javaType = "String";
		} else if(dataType.equalsIgnoreCase("CHAR")){
			javaType = "String";
		} else if(dataType.equalsIgnoreCase("text")){
			javaType = "String";
		} else if(dataType.equalsIgnoreCase("longtext")){
			javaType = "String";
		} else if(dataType.equalsIgnoreCase("BLOB")){
			javaType = "String";
		} else if(dataType.equalsIgnoreCase("longblob")){
			javaType = "String";
		} else if(dataType.equalsIgnoreCase("enum")){
			javaType = "String";
		}
		return javaType;
	}

}
