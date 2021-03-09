package ex1;

import java.util.Scanner;

public class Teste {

    public double soma(double valor1, int valor2) {
        return valor1 + valor2;
    }

    public void main(String[] args) {
        
        Scanner sc = new Scanner(System.in);
        System.out.println("Digite um valor: ");
        double valor = sc.nextFloat();
        int valor2 = 10;
        System.out.println(Math.sqrt(soma(valor, valor2)));
    }

}