package ex2;

import ex1.*;

public class Main {

    public static void throwSomething() {
        System.out.println("Oi");
        throw new RuntimeException();
    }

    public static void main(String[] args) {
        Teste t = new Teste();
        t.main(args);
        Math.sqrt(102);
        int a = Integer.parseInt("123");
        try {
            throwSomething();
        } catch (RuntimeException re) {
            
        }
        System.out.println("oi");
    }
}
