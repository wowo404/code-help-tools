package org.tool.generator;
import java.io.File;
import java.sql.ResultSet;

/**
 * oracle的语法
 * 生成hibernate的po类，有get和set方法
 * @author liuzhsh 2017年8月28日
 */
public class GeneratorHibernatePoTool {
	
	private static String lineSeparator = System.getProperty("line.separator");
	
	private static String packageName = "com.zanfq.model.po";
	
	private static String fieldTpl = "";
	
	private static String methodTpl = "";
	
	private static String constructorHasKey = "";
	
	static{
		//初始化两个模板
		StringBuffer fields = new StringBuffer();
		fields
			.append("	/**" + lineSeparator)
			.append("	 * #" + lineSeparator)
			.append("	 */" + lineSeparator)
			.append("	private & %;" + lineSeparator);
		fieldTpl = fields.toString();
		
		StringBuffer method = new StringBuffer();
		method
			.append("	public # get@() {" + lineSeparator)
			.append("		return this.%;" + lineSeparator)
			.append("	}" + lineSeparator)
			.append("	public void set@(# %) {" + lineSeparator)
			.append("		this.% = %;" + lineSeparator)
			.append("	}" + lineSeparator);
		methodTpl = method.toString();
		
		StringBuffer constructor = new StringBuffer();
		constructor
			.append("	/** id constructor */" + lineSeparator)
			.append("	public @(Long #) {" + lineSeparator)
			.append("		this.# = #;" + lineSeparator)
			.append("	}" + lineSeparator);
		constructorHasKey = constructor.toString();
	}
	
	public static void main(String[] args) throws Exception  {
		//暂时只支持有主键的表，不支持联合主键
		System.out.println("开始生成。。。。。");
		String[] tables = {"HELLO"};
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
		String tableSql = "select t.comments from user_tab_comments t where t.table_name = '" + table + "'";
		ResultSet tableRs = util.select(tableSql);
		String tableComments = "";//表注释
		while(tableRs.next()){
			tableComments = tableRs.getString("comments");
		}
		System.out.println("正在生成，表注释：" + tableComments);
		//读取表主键信息
		String consSql = "select col.column_name from user_constraints con,user_cons_columns col where "
				+ "con.constraint_name=col.constraint_name and con.constraint_type='P' and col.table_name='" + table + "'";
		ResultSet consRs = util.select(consSql);
		String pkColumn = "";
		while(consRs.next()){
			pkColumn = consRs.getString("column_name");
		}
		System.out.println("正在生成，表主键字段：" + (pkColumn.equals("") ? "没有主键" : pkColumn));
		//读取表字段信息
		String sql = "select t.column_name,t.data_type,t.data_length,t.data_precision,t.data_scale,"
				+ "t.nullable,c.comments from user_tab_columns t left join user_col_comments c on t.column_name = c.column_name "
				+ "where t.table_name = '" + table + "' and c.table_name = '" + table + "'";
		ResultSet rs = util.select(sql);
		StringBuffer allFields = new StringBuffer("");
		StringBuffer gettersSetters = new StringBuffer("");
		while(rs.next()){
			String columnName = rs.getString("column_name");
			String dataType = rs.getString("data_type");
			String dataLength = rs.getString("data_length");
			String dataPrecision = rs.getString("data_precision");
			String dataScale = rs.getString("data_scale");
			String nullable = rs.getString("nullable");
			String comments = rs.getString("comments");
			
			String fieldName = StringUtil.humpColumn(columnName);
			
			//拼接fields
			String fields = genField(fieldName,dataType,dataPrecision,dataScale,comments);
			allFields.append(fields);
			//拼接getter和setter
			Boolean isPk = pkColumn.equals(columnName);
			StringBuffer getterSetter = genGetterSetter(fieldName,columnName,dataType,dataLength,dataPrecision,dataScale,nullable,isPk);
			gettersSetters.append(getterSetter);
		}
		System.out.println("正在生成：读取表字段信息完成。");
		//读取模板文件mdl.tpl
		String mdlName = StringUtil.humpTable(table);
		StringBuffer fileContent = MyFileUtil.readFile("D:/work/workspace/generator-constants/src/main/resources/mdl.tpl");
		String output = fileContent.toString()
				.replace("#packageName#", packageName)
				.replace("#tableComments#", tableComments)
				.replace("#tableName#", table)
				.replace("#allFields#", allFields.toString())
				.replace("#mdlName#", mdlName)
				.replace("#constructorHasKey#", genConstructorHasKey(StringUtil.humpColumn(pkColumn),mdlName))
				.replace("#gettersAndSetters#", gettersSetters.toString());
		//将包名替换成文件夹路径
		String path = "D:/work/workspace/generator-constants/src/main/java/" + packageName.replace(".", File.separator);
		MyFileUtil.createDir(path);
		//输出内容到文件
		String filePath = path + File.separator + mdlName + ".java";
		MyFileUtil.writeToFile(filePath, output);
		System.out.println("△△△表:" + table + ",生成完成。△△△");
	}
	
	/**
	 * 根据是否有主键生成含主键的构造方法
	 */
	private static String genConstructorHasKey(String pkColumn,String mdlName) {
		if(pkColumn.equals("")){
			return "";
		}
		return constructorHasKey.replace("@", mdlName).replace("#", pkColumn);
	}

	private static StringBuffer genGetterSetter(String fieldName, String columnName,
			String dataType, String dataLength, String dataPrecision,
			String dataScale, String nullable, Boolean isPk) {
		//数据长度
		String precisionOrLength = "";
		if(dataType.equals("NUMBER")){
			precisionOrLength = ", precision = " + dataPrecision + ", scale = " + dataScale;
		} else if(dataType.equals("VARCHAR2") || dataType.equals("VARCHAR") || dataType.equals("CHAR") || dataType.equals("NVARCHAR2")){
			precisionOrLength = ", length = " + dataLength;
		}
		//是否可以为空
		String nullableStr = "";
		if(nullable.equals("N")){
			nullableStr = ", nullable = false";
		}
		//序列取个名字
		String seqName = StringUtil.humpColumn("SEQ_" +columnName);
		StringBuffer methodStr = new StringBuffer();
		//是否唯一
		String uniqueStr = "";
		if(isPk){
			methodStr
				.append("	@Id" + lineSeparator)
				.append("	@SequenceGenerator(name = \"" + seqName + "\", sequenceName = \"SEQ_"+columnName+"\", allocationSize = 1)" + lineSeparator)
				.append("	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = \"" + seqName + "\")" + lineSeparator);
			uniqueStr = ", unique = true";
		}
		if(dataType.equals("DATE")){
			methodStr
				.append("	@Temporal(TemporalType.TIMESTAMP)" + lineSeparator);
		}
		methodStr
			.append("	@Column(name = \"" + columnName + "\"" + uniqueStr + nullableStr + precisionOrLength + ")" + lineSeparator);
		//替换模板
		String otherField = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1, fieldName.length());
		String javaType = getJavaType(dataType,dataPrecision,dataScale);
		String newTpl = methodTpl.replace("#", javaType).replace("@", otherField).replace("%", fieldName);
		
		methodStr.append(newTpl);
		return methodStr;
	}

	/**
	 * 生成属性，例如：private String name;
	 */
	private static String genField(String fieldName, String dataType,
			String dataPrecision, String dataScale, String comments) {
		String javaType = getJavaType(dataType,dataPrecision,dataScale);
		String replaceOne = fieldTpl.replace("&", javaType).replace("%", fieldName).replace("#", comments);
		return replaceOne;
	}
	/**
	 * 根据oracle的data_type判断javaType
	 */
	private static String getJavaType(String dataType, String dataPrecision, String dataScale){
		String javaType = "";
		if(dataType.equals("NUMBER")){
			if(null == dataScale){
				javaType = "Long";
			} else if(null != dataScale && Integer.valueOf(dataScale) != 0){
				javaType = "Double";
			} else if(Integer.valueOf(dataScale) == 0 && Integer.valueOf(dataPrecision) < 10){
				javaType = "Integer";
			} else if(Integer.valueOf(dataScale) == 0 && Integer.valueOf(dataPrecision) >= 10){
				javaType = "Long";
			}
		} else if(dataType.equals("DATE")){
			javaType = "Date";
		} else if(dataType.equals("TIMESTAMP")){
			javaType = "Date";
		} else if(dataType.equals("VARCHAR2")){
			javaType = "String";
		} else if(dataType.equals("VARCHAR")){
			javaType = "String";
		} else if(dataType.equals("CHAR")){
			javaType = "String";
		} else if(dataType.equals("NVARCHAR2")){
			javaType = "String";
		} else if(dataType.equals("NCHAR")){
			javaType = "String";
		} else if(dataType.equals("BLOB")){
			javaType = "String";
		} else if(dataType.equals("CLOB")){
			javaType = "String";
		} else if(dataType.equals("NCLOB")){
			javaType = "String";
		} else if(dataType.equals("BFILE")){
			javaType = "String";
		}
		return javaType;
	}

}
