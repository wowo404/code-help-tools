package org.justing.generator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by emmet on 17-3-29.
 * desc:
 */
public class GenerateServiceInterface {

    //生成Service类
    public static void generate(String entityName, String entityPackage, String mapperPackage, String serviceInterfacePackage,
                                String serviceInterfacePath, String entitySuffix) throws IOException {
        File file = new File(serviceInterfacePath + entityName + "Service.java");
        if (file.exists()) return;

        StringBuffer sb = new StringBuffer();
        sb.append("package ").append(serviceInterfacePackage).append(";\r\n").append("\r\n");

        sb.append("import ").append(entityPackage).append(".").append(entityName + entitySuffix).append(";\r\n");
        sb.append("import com.baomidou.mybatisplus.extension.service.IService;\r\n\r\n");


        sb.append("public interface ").append(entityName).append("Service extends IService<" + entityName + entitySuffix + "> {\r\n");
        sb.append("\r\n");
        sb.append("}\r\n");
        String content = sb.toString();
        System.out.println(content);
        FileWriter fw = new FileWriter(serviceInterfacePath + entityName + "Service.java");
        PrintWriter pw = new PrintWriter(fw);
        pw.println(content);
        pw.flush();
        pw.close();
    }

    //把输入字符串的首字母改成小写
    private static String initial(String str) {
        char[] ch = str.toCharArray();
        if (ch[0] >= 'A' && ch[0] <= 'Z') ch[0] = (char) (ch[0] + 32);
        return new String(ch);
    }

}
