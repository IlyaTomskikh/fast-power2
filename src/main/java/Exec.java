import java.util.Scanner;

public class Exec {
    public static void main(String[] args) {
        final var scanner = new Scanner(System.in);
        System.out.println("Enter number x");
        final var xs = new StringBuilder(scanner.nextLine());
        final var indexOf = xs.indexOf("-");
        if (indexOf != -1) xs.deleteCharAt(indexOf);
        System.out.println("Enter base b");
        final var b = Integer.parseInt(scanner.nextLine());
        if (b > 1 && b < 11 || b == 16) {
            var start = System.nanoTime();
            var fastX2 = new BigInt(xs.toString()).fastSq();
            var finish = System.nanoTime();
            var duration = finish - start;
            System.out.println("Fast square of X = " + xs + " is " + fastX2 + ", took time in nanos: " + duration);
            start = System.nanoTime();
            var slowX2 = new BigInt(xs.toString()).slowSq();
            finish = System.nanoTime();
            duration = finish - start;
            System.out.println("Slow square of X = " + xs + " is " + slowX2 + ", took time in nanos: " + duration);
        } else System.out.println("Can't perform such operation");
    }
}
