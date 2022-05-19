package java.lang;
import com.kadima.StringTest.MyClassLoad;

public class test {

    public static void main(String[] args) throws Exception {

        MyClassLoad myClassLoad = new MyClassLoad("D:\\study\\study\\javeSE\\target\\classes\\");
        Class<?> aClass = myClassLoad.loadClass("java.lang.String");
        Object instance = aClass.newInstance();
        if (instance instanceof String){
            System.out.println("原javaString");
        }else {
            System.out.println("新的string类");
        }
    }


}
