package com.kadima.FourClass;

/**
 * 内部类拥有类的基本特征。(eg：可以继承父类，实现接口。)在实际问题中我们会遇到一些接口无法解决或难以解决的问题，
 * 此时我们可以使用内部类继承某个具体的或抽象的类，间接解决类无法多继承引起的一系列问题
 * 个类的定义放在里另一个类的内部，这就是内部类。广义上我们将内部类分为四种：成员内部类、静态内部类、局部（方法）内部类、匿名内部类。
 */
public class OutClass {

    private String name=" I am OutClass";

    /**
     * 作为成员存在，可以被任意权限修饰符修饰
     * 可以调用外部所有信息
     * 外部类使用，new对象打点
     * 可以和外部类属性、方法重名。调用外部时使用：外部类名.this.属性/方法名
     */
    class innerClass{
        public String name = "I am innerClass";

        public void show(){
            System.out.println("I am innerClass");
            //访问外部同名属性
            String name1 = OutClass.this.name;
        }

    }

    /**
     * 作为静态成员属性存在，可以被任意权限修饰符修饰
     */
    static class staticInnerClass{
        private String name="I am static innerClass";

        public void show(){
            System.out.println(name);
        }
    }

    /**
     * 位于方法之中，只能在方法中使用，具备类的基本特征，类前不能有访问权限
     * 无法创造静态信息，在方法结束后就释放内存
     * 可以访问方法的局部变量（默认final修饰，不能修改）
     */
    public void show(){
        class localClass{
            private String name = "I am localClass";

            public void show(){
                System.out.println(name);
            }
        }
    }

    /**
     * 用匿名内部类还有个前提条件：必须继承一个父类或实现一个接口，但最多只能继承一个父类，或实现一个接口
     * 匿名内部类常常被用来重写某个或某些方法
     */
    public animal getInstance() {
       return new animal() {
           @Override
           public void getname() {
               System.out.println("I am anonymousClass");
           }
       };
    }

    public test geTestInstance(){
        return new test(){
            @Override
            void getname() {
                System.out.println("父类中的方法被我重写了哦");
            }
        };
    }

    public static void main(String[] args) {
        OutClass outClass = new OutClass();
        animal instance = outClass.getInstance();
        instance.getname();
        test test = outClass.geTestInstance();
        test.getname();
    }
}

interface animal{
    void getname();
}

class test{
    void getname(){
        System.out.println("实体类的方法");
    };
}
