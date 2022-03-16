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
        if (b > 1 && b <= 10 || b == 16) {
            final var x = (b == 16) ? Long.parseLong(String.valueOf(xs), 16) : fromNto10(b, Integer.parseInt(String.valueOf(xs)));

            var start = System.nanoTime();
            var x2 = fastSq(x, String.valueOf(x).length());
            long finish = System.nanoTime(), timeTook = finish - start;
            var result = new StringBuilder("With fastSq: x^2 = ");

            switch (b) {
                case 16:
                    result.append(Long.toHexString(x2));
                    break;
                case 10:
                    result.append(x2);
                    break;
                default:
                    result.append(from10ToM(b, x2));
                    break;
            }

            result.append(", number system: ").append(b).append(" and it took ").append(timeTook).append(" nanoseconds");
            System.out.println(result);

            start = System.nanoTime();
            final var sq = Math.pow(x, 2);
            finish = System.nanoTime();
            timeTook = finish - start;
            result = new StringBuilder("With Math.pow(x, 2): x^2 = ").append(sq).append(" and it took ").append(timeTook).append(" nanoseconds");
            System.out.println(result);

            start = System.nanoTime();
            x2 = x * x;
            finish = System.nanoTime();
            timeTook = finish - start;
            result = new StringBuilder("With x * x: x^2 = ").append(x2).append(" and it took ").append(timeTook).append(" nanoseconds");
            System.out.println(result);
        } else System.out.println("Can't perform such operation");
    }
    static long fastSq(long input, int len) {
        //step 1:
        long x2 = 0;
        var x = new int[len];
        for (var i = 0; i < len; ++i) {
            x[i] = (int) (input % 10);
            input /= 10;
        }
        var y = new int[2 * len];
        //step 2.1:
        for (var i = 0; i < len; ++i) {
            int     c = 0,
                    uv = y[2 * i] + x[i] * x[i],
                    cuv;
            y[2 * i] = uv % 10;
            //step 2.2:
            for (var j = i + 1; j < len; ++j) {
                cuv = y[i + j] + 2 * x[i] * x[j] + c * 10 + uv / 10;
                c = cuv / 10 / 10;
                uv = cuv - (c * 10 * 10);
                y[i + j] = uv % 10; //y_i+j = v
            }
            //step 2.3:
            y[i + len] += uv / 10;   //y_i+len += u
            var tmp = y[i + len] / 10;
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
        for (var i = y.length - 1; i >= 0; --i) {
            x2 *= 10;
            x2 += y[i];
        }
        return x2;
    }

    private static long reverseInt(long num) {
        long result = 0;
        while (num > 0) {
            result = result * 10 + num % 10;
            num /= 10;
        }
        return result;
    }

    private static long fromNto10(int n, long inp) {
        long in10 = 0;
        var power = 0;
        while (inp != 0) {
            int nTimesPower = 1, i = power;
            while (i > 0) {
                nTimesPower *= n;
                --i;
            }
            in10 +=  inp % 10 * nTimesPower;
            inp /= 10;
            ++power;
        }
        return in10;
    }

    private static long from10ToM(int m, long in10) {
        long outM = 0;
        while (in10 != 0) {
            outM += in10 % m;
            outM *= 10;
            in10 /= m;
        }
        return reverseInt(outM);
    }
}
