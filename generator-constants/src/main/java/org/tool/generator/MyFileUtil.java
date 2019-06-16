package org.tool.generator;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

/**
 * 文件工具类
 * <br>----------------------------------------------------变更记录--------------------------------------------------
 * <br> 序号      |           时间                        	|   作者      |                          描述                                                         
 * <br> 0     | 2014年3月6日 下午8:49:21  	|  刘章盛     | 创建  
 */
public class MyFileUtil {
    
    /**
     * 
     * 如果该文件路径不存在则创建
     * @param path
     * <br>----------------------------------------------------变更记录--------------------------------------------------
     * <br> 序号      |           时间                        	|   作者      |                          描述                                                         
     * <br> 0     | 2014年3月6日 下午8:55:54  	|  刘章盛     | 创建
     */
    public static void createDir(String path){
        File file = new File(path);
        if(!file.exists()){
            file.mkdirs();
        }
    }
    /**
     * 
     * 删除一个文件
     * @param filePath 文件路径
     * @return
     * <br>----------------------------------------------------变更记录--------------------------------------------------
     * <br> 序号      |           时间                        	|   作者      |                          描述                                                         
     * <br> 0     | 2014年3月8日 上午11:42:41  	|  刘章盛     | 创建
     */
    public static Boolean deleteFile(String filePath){
        boolean flag = false;
        File file = new File(filePath);
        if(file.isFile() && file.exists()){
            file.delete();
            flag = true;
        }
        return flag;
    }
    
    /**
     * 读取文件
     */
    public static StringBuffer readFile(String filePath) {
		//读取原有文件内容，保存到buffer
		StringBuffer sb = new StringBuffer();
		try {
			File file = new File(filePath);
			FileInputStream fis = new FileInputStream(file);
			InputStreamReader isr = new InputStreamReader(fis);
			BufferedReader br = new BufferedReader(isr);
			String line = "";
			while((line = br.readLine()) != null){
				sb.append(line);
				sb.append(System.getProperty("line.separator"));
			}
			//关闭流
			br.close();
			isr.close();
			fis.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sb;
    }
    
    /**
     * 输出内容到文件
     */
    public static void writeToFile(String filePath,String content){
		try {
			File file = new File(filePath);
			if(!file.exists()){
	            file.createNewFile();
	        }
			FileOutputStream fos = new FileOutputStream(file);
			PrintWriter pw = new PrintWriter(fos);
			pw.write(content.toCharArray());
			//关闭流
			pw.close();
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    /**
     * 
     * 删除目录及目录下的所有文件
     * @param directoryPath
     * @return 0:成功,-1:失败
     * <br>----------------------------------------------------变更记录--------------------------------------------------
     * <br> 序号      |           时间                        	|   作者      |                          描述                                                         
     * <br> 0     | 2014年3月8日 下午12:59:11  	|  刘章盛     | 创建
     */
    public static Integer deleteDirectory(String directoryPath){
        if(!directoryPath.endsWith(File.separator)){
            directoryPath = directoryPath + File.separator;
        }
        File file = new File(directoryPath);
        if(!file.exists()){
            return -1;
        }
        if(file.isDirectory()){
            File[] files = file.listFiles();
            for (File file2 : files) {
                deleteDirectory(file2.getAbsolutePath());
            }
        }
        
        Boolean flag = file.delete();
        return flag == true ? 0 : -1;
    }
    
    /**
     * 
     * 文件拷贝
     * @param srcFile 源文件
     * @param destFile 目标文件
     * @return
     * <br>----------------------------------------------------变更记录--------------------------------------------------
     * <br> 序号      |           时间                        	|   作者      |                          描述                                                         
     * <br> 0     | 2014年5月2日 下午7:24:32  	|  刘章盛     | 创建
     */
    public static Integer copyFile(String srcFile,String destFile){
        String destFilePath = destFile.substring(0, destFile.lastIndexOf("/") + 1);
        createDir(destFilePath);// 目标文件不存在的话创建它
        BufferedInputStream inBuff = null;// 文件缓冲输出流
        BufferedOutputStream outBuff = null;// 文件缓冲输入流
        try {
            inBuff = new BufferedInputStream(new FileInputStream(new File(srcFile)));
            outBuff = new BufferedOutputStream(new FileOutputStream(new File(destFile)));
            // 缓冲数组
            byte[] b = new byte[1024 * 5];
            int len;
            while((len = inBuff.read(b)) != -1){
                outBuff.write(b, 0, len);
            }
            outBuff.flush();
        } catch (FileNotFoundException e) {
            return -1;
        } catch (IOException e) {
            return -1;
        } finally {
            if(null != inBuff){
                try {
                    inBuff.close();
                } catch (IOException e) {
                    return -1;
                }
            }
            if(null != outBuff){
                try {
                    outBuff.close();
                } catch (IOException e) {
                    return -1;
                }
            }
        }
        return 0;
    }

}
