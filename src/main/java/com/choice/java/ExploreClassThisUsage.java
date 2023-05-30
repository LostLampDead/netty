package com.choice.java;

import java.util.ArrayList;
import java.util.List;

/**
 * 在Java中，类.this表示对外部类的当前实例的引用。使用类.this的主要好处是在内部类中访问外部类的成员变量或方法时，可以避免与内部类成员的名称冲突。
 * 例如，假设我们有一个外部类Outer和一个内部类Inner。如果Outer和Inner都有一个名为value的成员变量，
 * 那么在Inner中使用this.value会引用到Inner的成员变量，而不是Outer的成员变量。为了访问Outer的value成员变量，可以使用Outer.this.value。
 * 另一个好处是，如果你需要在内部类中创建一个对外部类的实例的引用，你可以使用类名.this，而不是使用外部类的名称。这样做可以使代码更加清晰和易于理解。
 * 总之，使用类.this可以帮助我们在内部类中避免命名冲突，并使代码更加易于理解和维护。
 */
public class ExploreClassThisUsage {
    public static void main(String[] args) {
        Outer outer = new Outer();
        outer.start();
        List<String> list = new ArrayList<>();
        list.add("A");
        list.add("B");
        list.add("C");
        for (String next : list) {
            System.out.println(next);

        }
    }

    private static class Outer {

        public void start(){
            new Inner().explore();
        }

        private class Inner {
            public void explore(){
                // 当需要访问当前类中的 sayHello 可以使用 this.sayHello
                // 当需要访问外部类中的 sayHello 可以使用外部类.this.sayHello
                // ps: 便于区分
                this.sayHello();
                Outer.this.sayHello();
            }

            public void sayHello(){
                System.out.println("Hello, this is inside!");
            }
        }

        public void sayHello(){
            System.out.println("Hello, this is outside!");
        }
    }
}
