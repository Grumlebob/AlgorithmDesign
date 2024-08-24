package W0_hello_kattis;

import java.util.Scanner;

public class Backspace {


    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
            //Logik er simpel. Hvis vi møder et '<' så skal vi fjerne det sidste tegn i vores stringbuilder.
            //Ellers skal vi tilføje tegnet til vores stringbuilder.
            char c = input.charAt(i);
            if (c == '<') {
                sb.deleteCharAt(sb.length() - 1);
            } else {
                sb.append(c);
            }
        }
        System.out.println(sb.toString());

        //Total runtime er O(n) hvor n er længden af input.

    }
}
