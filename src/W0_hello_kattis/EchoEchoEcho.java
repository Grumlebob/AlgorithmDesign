package W0_hello_kattis;

import java.util.Scanner;

public class EchoEchoEcho {

    public static void main(String[] args) {


        Scanner scanner = new Scanner(System.in);
        String word = scanner.next();

        StringBuilder sb = new StringBuilder(word);
        sb.append(" ");
        sb.append(word);
        sb.append(" ");
        sb.append(word);
        System.out.println(sb.toString());
    }
}
