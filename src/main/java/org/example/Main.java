package org.example;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try {
            System.out.println("Enter two arguments");
            var scanner = new Scanner(System.in);
            var firstNumber = scanner.nextInt();
            var secondNumber = scanner.nextInt();
            var result = firstNumber + secondNumber;
            System.out.println(result);
        } catch (Exception e) {
            System.out.println("Иди от сюда or Idi ot suda");
        }
    }
}