import java.util.Scanner;

public class Exec {
    public static void main(String[] args) {
        final Scanner scanner = new Scanner(System.in);
        final StringBuilder xs = new StringBuilder(scanner.nextLine());
        int indexOf = xs.indexOf("-");
        if (indexOf != -1) xs.deleteCharAt(indexOf);
        final int x = Integer.parseInt(String.valueOf(xs)),
        b = Integer.parseInt(scanner.nextLine());

        long start = System.nanoTime();
        long x2 = fastSq(x, xs.length());
        long finish = System.nanoTime(), timeTook = finish - start;
        StringBuilder result = new StringBuilder("With fastSq: x^2 = ");

        if (b == 10) result.append(x2);
        else if (b > 0 && b < 10) result.append(conversion(String.valueOf(x2), 10, b));
        else if (b == 16) result.append(Long.toHexString(x2));
        else {
            System.out.println("Can't convert to " + b);
            result.append(x2);
        }

        result.append(", number system: ").append(b).append(" and it took ").append(timeTook).append(" nanoseconds");
        System.out.println(result);

        start = System.nanoTime();
        final double sq = Math.pow(x, 2);
        finish = System.nanoTime();
        timeTook = finish - start;
        result = new StringBuilder("With Math.pow(x, 2): x^2 = ");
        result.append(sq).append(" and it took ").append(timeTook).append(" nanoseconds");
        System.out.println(result);

        start = System.nanoTime();
        x2 = (long) x * x;
        finish = System.nanoTime();
        timeTook = finish - start;
        result = new StringBuilder("With x * x: x^2 = ");
        result.append(x2).append(" and it took ").append(timeTook).append(" nanoseconds");
        System.out.println(result);
    }
    static long fastSq(long input, int len) {
        //step 1:
        long x2 = 0;
        int[] x = new int[len];
        for (int i = 0; i < len; ++i) {
            x[i] = (int) (input % 10);
            input /= 10;
        }
        int[] y = new int[2 * len];
        //step 2.1:
        for (int i = 0; i < len; ++i) {
            int     c = 0,
                    uv = y[2 * i] + x[i] * x[i],
                    cuv;
            y[2 * i] = uv % 10;
            //step 2.2:
            for (int j = i + 1; j < len; ++j) {
                cuv = y[i + j] + 2 * x[i] * x[j] + c * 10 + uv / 10;
                c = cuv / 10 / 10;
                uv = cuv - (c * 10 * 10);
                y[i + j] = uv % 10; //y_i+j = v
            }
            //step 2.3:
            y[i + len] += uv / 10;   //y_i+len += u
            int tmp = y[i + len] / 10;
            if (tmp > 0) {
                if (i + len + 1 < y.length) y[i + len + 1] += tmp;
                y[i + len] %= 10;
            }
            if (i + len + 1 < y.length) {
                y[i + len + 1] += c;    //y_i+len+1 += c
                tmp = y[i + len + 1] / 10;
                if (tmp > 0) {
                    y[i + len + 2] += tmp;
                    y[i + len + 1] %= 10;
                }
            }
        }
        for (int i = y.length - 1; i >= 0; --i) {
            x2 *= 10;
            x2 += y[i];
        }
        return x2;
    }

    public static String conversion(String s, int from, int to) {
        if (from == to) return s;
        long r = 0, n = s.length();
        for (int i = 0; i < n; ++i)
            r = r * from + Integer.parseInt("" + s.charAt(i));
        StringBuilder q = new StringBuilder();
        while(r > 0) {
            q.insert(0, (r % to));
            r = r / to;
        }
        return q.toString();
    }
}
