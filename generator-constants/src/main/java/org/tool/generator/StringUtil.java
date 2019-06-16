package org.tool.generator;
/**
 * 字符串工具类
 * @author liuzhsh 2017年5月12日
 */
public class StringUtil {

	/**
	 * 将列转换成po的驼峰风格的属性名，例如：my_id，转换后为myId
	 */
	public static String humpColumn(String columnName){
		columnName = columnName.toLowerCase();
		if(columnName.indexOf("_") != -1){
			String[] array = columnName.split("_");
			String humpName = array[0];
			for (int i = 1; i < array.length; i++) {
				humpName += array[i].substring(0, 1).toUpperCase() + array[i].substring(1, array[i].length());
			}
			return humpName;
		}
		return columnName;
	}
	
	/**
	 * 将表名转换为符合驼峰风格的className，例如：tb_class_name，转换后为TbClassName
	 */
	public static String humpTable(String tableName){
		tableName = tableName.toLowerCase();
		if(tableName.indexOf("_") != -1){
			String[] array = tableName.split("_");
//			int idx = 1;
//			if(tableName.startsWith("tb_")){
//				idx = 1;
//			} else {
//				idx = 0;
//			}
			String humpName = "";
			if(array.length > 1){
				for (int i = 0; i < array.length; i++) {
					humpName += array[i].substring(0, 1).toUpperCase() + array[i].substring(1, array[i].length());
				}
			}
			return humpName;
		} else {
			return tableName.substring(0, 1).toUpperCase() + tableName.substring(1);
		}
	}

}
