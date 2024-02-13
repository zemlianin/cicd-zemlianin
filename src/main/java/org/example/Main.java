package org.example;

public class Main {
    public static void main(String[] args) {
        try {
            var firstNumber = Integer.parseInt(args[1]);
            var secondNumber = Integer.parseInt(args[1]);
            var result = firstNumber + secondNumber;
            System.out.println(result);
        } catch (Exception e) {
            System.out.println("Иди от сюда - Idi ot suda");
        }
    }
}