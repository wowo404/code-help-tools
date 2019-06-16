package org.mybatis.generator.codegen.mybatis3.xmlmapper.elements;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.ListUtilities;
import org.mybatis.generator.codegen.mybatis3.MyBatis3FormattingUtilities;

/**
 * 自定义的一个sql column
 * @author liuzhsh 2017年6月30日
 */
public class WhereColumnListElementGenerator extends
		AbstractXmlElementGenerator {

	public WhereColumnListElementGenerator(){
		super();
	}
	
	@Override
	public void addElements(XmlElement parentElement) {
		XmlElement answer = new XmlElement("sql"); //$NON-NLS-1$
		answer.addAttribute(new Attribute("id", "where_column")); //$NON-NLS-1$ //$NON-NLS-2$
		
		XmlElement whereElement = new XmlElement("where"); //$NON-NLS-1$
		whereElement.addElement(new TextElement(" 0 = 0 "));

        StringBuilder sb = new StringBuilder();
        
        for (IntrospectedColumn introspectedColumn : ListUtilities.removeGeneratedAlwaysColumns(introspectedTable
                .getAllColumns())) {
            XmlElement isNotNullElement = new XmlElement("if"); //$NON-NLS-1$
            sb.setLength(0);
            sb.append(introspectedColumn.getJavaProperty()); //$NON-NLS-1$
            sb.append(" != null and "); //$NON-NLS-1$
            sb.append(introspectedColumn.getJavaProperty()); //$NON-NLS-1$
            sb.append(" != ''"); //$NON-NLS-1$
            isNotNullElement.addAttribute(new Attribute("test", sb.toString())); //$NON-NLS-1$

            sb.setLength(0);
            sb.append("and ");
            sb.append(MyBatis3FormattingUtilities
                    .getAliasedEscapedColumnName(introspectedColumn));
            sb.append(" = "); //$NON-NLS-1$
            sb.append(MyBatis3FormattingUtilities.getParameterClause(introspectedColumn)); //$NON-NLS-1$

            isNotNullElement.addElement(new TextElement(sb.toString()));
            
            whereElement.addElement(isNotNullElement);//将if节点加入where节点下
        }
		
		answer.addElement(whereElement);//将where节点加入sql节点下
		
		parentElement.addElement(answer);//将sql节点加入mapper节点下
	}

}
