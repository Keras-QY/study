package com.kadima.ClassLoader;


import java.io.*;

/**
 * 自定义加载器
 */
public class MyClassLoad extends ClassLoader {

    /**
     * 磁盘上类的路径
     */
    private String codePath;

    public MyClassLoad(ClassLoader parent, String codePath){
        super(parent);
        this.codePath = codePath;
    }

    public MyClassLoad(String codePath){
        super(MyClassLoad.class.getClassLoader().getParent());
        this.codePath = codePath;
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        BufferedInputStream InputStream = null;
        ByteArrayOutputStream outputStream = null;

        //完整的类名
        String file = codePath + name.replaceAll("\\.","/") + ".class";
        try {
            //获取输入流
            InputStream = new BufferedInputStream(new FileInputStream(file));
            //获取输出流
            outputStream = new ByteArrayOutputStream();

            byte[] bytes = new byte[1024];
            int len;
            while ((len = InputStream.read(bytes)) != -1){
                outputStream.write(bytes,0,len);
            };

            byte[] byteArray = outputStream.toByteArray();
            Class<?> aClass = defineClass(null, byteArray, 0, byteArray.length);
            return aClass;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (InputStream!=null){
                try {
                    InputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outputStream!=null){
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
