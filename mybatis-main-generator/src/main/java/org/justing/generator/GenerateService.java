package org.justing.generator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by emmet on 17-3-29.
 * desc:
 */
public class GenerateService {

    //生成Service类
    public static void generate(String entityName, String entityPackage, String mapperPackage, String servicePackage,
                                String servicePath, String serviceInterfacePackage, String entitySuffix) throws IOException {
        File file = new File(servicePath + entityName + "ServiceImpl.java");
        if (file.exists()) return;

        StringBuffer sb = new StringBuffer();
        sb.append("package ").append(servicePackage).append(";\r\n").append("\r\n");

        sb.append("import ").append(entityPackage).append(".").append(entityName + entitySuffix).append(";\r\n\r\n");
        sb.append("import ").append(mapperPackage).append(".").append(entityName).append("Dao;\r\n");
        sb.append("import ").append(serviceInterfacePackage).append(".").append(entityName).append("Service;\r\n");
        sb.append("import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;\r\n");
        sb.append("import lombok.extern.slf4j.Slf4j;\r\n");
        sb.append("import org.springframework.stereotype.Service;\r\n");
        sb.append("import org.springframework.transaction.annotation.Transactional;\r\n\r\n");

        sb.append("@Slf4j\r\n");
        sb.append("@Transactional\r\n");
        sb.append("@Service\r\n");
        sb.append("public class ").append(entityName).append("ServiceImpl extends ServiceImpl<" + entityName + "Dao, " + entityName + entitySuffix + "> implements " + entityName + "Service {\r\n");
        sb.append("\r\n");
        sb.append("}\r\n");
        String content = sb.toString();
        System.out.println(content);
        FileWriter fw = new FileWriter(servicePath + entityName + "ServiceImpl.java");
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
