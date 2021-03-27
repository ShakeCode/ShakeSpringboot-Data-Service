//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.data.junit.test;

public class Test {
    public Test() {
    }

    public static void main(String[] args) {
        System.out.println((new Test.B()).getValue());
    }

    static class B extends Test.A {
        public B() {
            super(5);
            this.setValue(this.getValue() - 3);
        }

        public void setValue(int value) {
            super.setValue(2 * value);
        }
    }

    static class A {
        protected int value;

        public A(int v) {
            this.setValue(v);
        }

        public void setValue(int value) {
            this.value = value;
        }

        public int getValue() {
            int var1;
            try {
                ++this.value;
                var1 = this.value;
            } finally {
                this.setValue(this.value);
                System.out.println(this.value);
            }

            return var1;
        }
    }
}
