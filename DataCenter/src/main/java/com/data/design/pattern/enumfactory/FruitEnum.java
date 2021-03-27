package com.data.design.pattern.enumfactory;

public enum FruitEnum implements FruitColor {
    APPLE {
        @Override
        public String getColor() {
            return "red";
        }
    },
    PEAR {
        @Override
        public String getColor() {
            return "yellow";
        }
    },
    ORANGE {
        @Override
        public String getColor() {
            return "orangeX";
        }
    };

    private FruitEnum() {
    }

    public static void main(String[] args) {
        System.out.println(APPLE.getColor());
        System.out.println(PEAR.getColor());
    }
}
