package org.justing.generator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by emmet on 17-3-29.
 * desc:
 */
public class GenerateController {

    //生成Service类
    public static void generate(String entityName, Generate.TableInfo tableInfo, String entityPackage, String mapperPackage, String serviceInterfacePackage, String servicePath,
                                String controllerPackage, String controllerPath, String entitySuffix, String requestMappingPrefix) throws IOException {
        File file = new File(controllerPath + entityName + "Controller.java");
        if (file.exists()) return;

        StringBuffer sb = new StringBuffer();
        sb.append("package ").append(controllerPackage).append(";\r\n").append("\r\n");

        sb.append("import ").append(entityPackage).append(".").append(entityName + entitySuffix).append(";\r\n\r\n");
        sb.append("import ").append(serviceInterfacePackage).append(".").append(entityName).append("Service;\r\n");
        sb.append("import lombok.RequiredArgsConstructor;\r\n");
        sb.append("import me.zhengjie.base.Response;\r\n");
//        sb.append("import com.minxun.modules.sys.controller.AbstractController;\r\n");
        sb.append("import org.springframework.web.bind.annotation.*;\r\n");
//        sb.append("import org.springframework.beans.factory.annotation.Autowired;\r\n");
        sb.append("\r\n");

        sb.append("/**\r\n");
        sb.append(" * " + tableInfo.getTableComment() + "\r\n");
        sb.append(" */\r\n");
        sb.append("@RequiredArgsConstructor\r\n");
        sb.append("@RestController\r\n");
        sb.append("@RequestMapping(\"" + requestMappingPrefix + "/" + initial(entityName) + "\")\r\n");
        sb.append("public class ").append(entityName).append("Controller {\r\n");
        sb.append("\r\n");
        sb.append("\tprivate final " + entityName + "Service " + initial(entityName) + "Service;\r\n");
        sb.append("\r\n");
        //--------分页列表
        sb.append("\t/**\r\n");
        sb.append("\t * 分页列表\r\n");
        sb.append("\t */\r\n");
        sb.append("\t@GetMapping(\"pageList\")\r\n");
        sb.append("\tpublic Response<Long> pageList() {\r\n");
        sb.append("\t\treturn Response.success();\r\n");
        sb.append("\t}\r\n");
        sb.append("\r\n");
        //--------
        //--------列表
        sb.append("\t/**\r\n");
        sb.append("\t * 列表\r\n");
        sb.append("\t */\r\n");
        sb.append("\t@GetMapping(\"list\")\r\n");
        sb.append("\tpublic Response<Long> list() {\r\n");
        sb.append("\t\treturn Response.success();\r\n");
        sb.append("\t}\r\n");
        sb.append("\r\n");
        //--------
        //--------详情
        sb.append("\t/**\r\n");
        sb.append("\t * 详情\r\n");
        sb.append("\t */\r\n");
        sb.append("\t@GetMapping(\"{id}\")\r\n");
        sb.append("\tpublic Response<Long> info(@PathVariable Long id) {\r\n");
        sb.append("\t\treturn Response.success();\r\n");
        sb.append("\t}\r\n");
        sb.append("\r\n");
        //--------
        //--------新增
        sb.append("\t/**\r\n");
        sb.append("\t * 新增\r\n");
        sb.append("\t */\r\n");
        sb.append("\t@PostMapping(\"add\")\r\n");
        sb.append("\tpublic Response<Long> add() {\r\n");
        sb.append("\t\treturn Response.success();\r\n");
        sb.append("\t}\r\n");
        sb.append("\r\n");
        //--------
        //--------编辑
        sb.append("\t/**\r\n");
        sb.append("\t * 编辑\r\n");
        sb.append("\t */\r\n");
        sb.append("\t@PutMapping(\"edit\")\r\n");
        sb.append("\tpublic Response<Void> edit() {\r\n");
        sb.append("\t\treturn Response.success();\r\n");
        sb.append("\t}\r\n");
        sb.append("\r\n");
        //--------
        //--------删除
        sb.append("\t/**\r\n");
        sb.append("\t * 删除\r\n");
        sb.append("\t */\r\n");
        sb.append("\t@DeleteMapping(\"/{ids}\")\r\n");
        sb.append("\tpublic Response<Void> delete(@PathVariable Long[] ids) {\r\n");
        sb.append("\t\treturn Response.success();\r\n");
        sb.append("\t}\r\n");
        //----------
        sb.append("}\r\n");
        String content = sb.toString();
        System.out.println(content);
        FileWriter fw = new FileWriter(controllerPath + entityName + "Controller.java");
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
