package org.mybatis.generator;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import org.mybatis.generator.api.CommentGenerator;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.CompilationUnit;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.InnerClass;
import org.mybatis.generator.api.dom.java.InnerEnum;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.config.PropertyRegistry;
import org.mybatis.generator.internal.util.StringUtility;

public class MyCommentGenerator implements CommentGenerator {

	private Properties properties;
	private Properties systemPro;
	private boolean suppressDate;
	private boolean suppressAllComments;
	private String currentDateStr;

	public MyCommentGenerator() {
        super();
        properties = new Properties();
        systemPro = System.getProperties();
        suppressDate = false;
        suppressAllComments = false;
        currentDateStr = (new SimpleDateFormat("yyyy-MM-dd")).format(new Date());
    }
	
	@Override
	public void addConfigurationProperties(Properties properties) {
		this.properties.putAll(properties);

		suppressDate = StringUtility.isTrue(properties
				.getProperty(PropertyRegistry.COMMENT_GENERATOR_SUPPRESS_DATE));

		suppressAllComments = StringUtility
				.isTrue(properties
						.getProperty(PropertyRegistry.COMMENT_GENERATOR_SUPPRESS_ALL_COMMENTS));
	}

	@Override
	public void addFieldComment(Field field,
			IntrospectedTable introspectedTable,
			IntrospectedColumn introspectedColumn) {
		if (suppressAllComments) {
            return;
        }
        field.addJavaDocLine("/**");
        field.addJavaDocLine(" * " + introspectedColumn.getRemarks());
        field.addJavaDocLine(" */");
	}

	@Override
	public void addFieldComment(Field field, IntrospectedTable introspectedTable) {
		if (suppressAllComments) {
            return;
        }
        field.addJavaDocLine("/**");
        field.addJavaDocLine(" * " + introspectedTable.getRemarks());
        field.addJavaDocLine(" */");
	}

	@Override
	public void addModelClassComment(TopLevelClass topLevelClass,
			IntrospectedTable introspectedTable) {
		if (suppressAllComments) {
            return;
        }
        topLevelClass.addJavaDocLine("/**");
        topLevelClass.addJavaDocLine(" * " + introspectedTable.getRemarks());
        topLevelClass.addJavaDocLine(" * @author " + systemPro.getProperty("user.name"));
        topLevelClass.addJavaDocLine(" * " + currentDateStr + " Created");
        topLevelClass.addJavaDocLine(" */");
	}

	/**
	 * Java类的类注释
	 */
	@Override
	public void addClassComment(InnerClass innerClass,
			IntrospectedTable introspectedTable) {
		if (suppressAllComments) {
			return;
		}
		innerClass.addJavaDocLine("/**");
		innerClass.addJavaDocLine(" * " + introspectedTable.getRemarks());
		innerClass.addJavaDocLine(" * @author " + systemPro.getProperty("user.name"));
		innerClass.addJavaDocLine(" * " + currentDateStr + " Created");
		innerClass.addJavaDocLine(" */");
	}

	@Override
	public void addClassComment(InnerClass innerClass,
			IntrospectedTable introspectedTable, boolean markAsDoNotDelete) {
		if (suppressAllComments) {
            return;
        }
		innerClass.addJavaDocLine("/**");
		innerClass.addJavaDocLine(" * " + introspectedTable.getRemarks());
		innerClass.addJavaDocLine(" * @author " + systemPro.getProperty("user.name"));
		innerClass.addJavaDocLine(" * " + currentDateStr + " Created");
		innerClass.addJavaDocLine(" */");
	}
	
	protected String getDateString() {
		String result = null;
		if (!suppressDate) {
			result = currentDateStr;
		}
		return result;
	}

	@Override
	public void addEnumComment(InnerEnum innerEnum,
			IntrospectedTable introspectedTable) {
		if (suppressAllComments) {
            return;
        }
        innerEnum.addJavaDocLine("/**");
        innerEnum.addJavaDocLine(" * " + introspectedTable.getFullyQualifiedTable());
        innerEnum.addJavaDocLine(" */");
	}

	@Override
	public void addGetterComment(Method method,
			IntrospectedTable introspectedTable,
			IntrospectedColumn introspectedColumn) {}

	@Override
	public void addSetterComment(Method method,
			IntrospectedTable introspectedTable,
			IntrospectedColumn introspectedColumn) {}

	@Override
	public void addGeneralMethodComment(Method method,
			IntrospectedTable introspectedTable) {}

	/**
	 * 在java文件头部添加注释，在package上面，主要是版权信息
	 */
	@Override
	public void addJavaFileComment(CompilationUnit compilationUnit) {
		compilationUnit.addFileCommentLine("/**");
		compilationUnit.addFileCommentLine(" * " + compilationUnit.getType().getShortName() + ".java");
		compilationUnit.addFileCommentLine(" * Copyright(C) liuzhsh");
		compilationUnit.addFileCommentLine(" * All rights reserved.");
		compilationUnit.addFileCommentLine(" */");
	}

	/**
	 * 给生成的XML文件加注释
	 */
	@Override
	public void addComment(XmlElement xmlElement) {}

	/**
	 * 同样是给生成的XML文件加注释，猜测这里应该是在xml文件的顶部节点添加注释，DOCTYPE下面
	 */
	@Override
	public void addRootComment(XmlElement rootElement) {}

}
