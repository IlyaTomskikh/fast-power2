import java.util.Arrays;

public class BigInt {
    private byte[] digits;

    public BigInt() {
        this.digits = new byte[0];
    }
    public BigInt(byte[] digits) {
        this.digits = digits;
        this.reverseArray();
    }
    public BigInt(int len) {
        this.digits = new byte[len];
    }
    public BigInt(String bigInt) {
        this.digits = BigInt.valueOf(bigInt).digits;
        this.reverseArray();
    }
    public BigInt(BigInt that) {
        this.digits = that.digits;
    }

    @Override
    public String toString() {
        return "Digits = " + Arrays.toString(digits);
    }

    public static BigInt valueOf(String s) {
        var bigInt = new BigInt(s.length());
        var i = 0;
        for (var digit: s.toCharArray()) bigInt.digits[i++] = digit <= '9' && digit >= '0' ? (byte) Character.getNumericValue(digit) : 9;
        return bigInt;
    }
    private void addZerosLeft(int n) {
        if (n <= 0) return;
        this.reverseArray();
        byte[] tmp = new byte[digits.length + n];
        System.arraycopy(digits, 0, tmp, 0, digits.length);
        digits = tmp;
        this.reverseArray();
    }
    public BigInt mul(BigInt bigInt) {
        if (bigInt == null) return new BigInt();
        var multi = new byte[bigInt.digits.length + digits.length];
        final byte ten = 10;
        for (var i = 0; i < bigInt.digits.length; ++i) {
            byte carry = 0;
            for (var j = 0; j < digits.length; ++j) {
                var ix = i + j;
                multi[ix] += (byte) (carry + bigInt.digits[i] * digits[j]);
                carry = (byte) (multi[ix] / ten);
                multi[ix] %= ten;
            }
            if (carry != 0) multi[i + digits.length] += carry;
        }
        var res = new BigInt(multi);
        res.clearZeros();
        return res;
    }
    public BigInt slowSq() {
        return mul(this);
    }
    public BigInt fastSq() {
        var len = digits.length;
        var square = new byte[len * 2];
        var sLen = square.length;
        final byte ten = 10;
        int c, uv, cuv;
        byte tmp;
        for (var i = 0; i < len; ++i) {
            c = 0;
            uv = square[2 * i] + digits[i] * digits[i];
            square[2 * i] = (byte) (uv % ten);
            //step 2.2:
            for (var j = i + 1; j < len; ++j) {
                cuv = square[i + j] + 2 * digits[i] * digits[j] + c * ten + uv / ten;
                c = cuv / ten / ten;
                uv = cuv - (c * ten * ten);
                square[i + j] = (byte) (uv % ten); //y_i+j = v
            }
            //step 2.3:
            square[i + len] += (byte) (uv / ten);   //y_i+len += u
            tmp = (byte) (square[i + len] / ten);
            if (tmp > 0) {
                if (i + len + 1 < sLen) square[i + len + 1] += tmp;
                square[i + len] %= ten;
            }
            if (i + len + 1 < sLen) {
                square[i + len + 1] += c;    //y_i+len+1 += c
                tmp = (byte) (square[i + len + 1] / ten);
                if (tmp > 0) {
                    square[i + len + 2] += tmp;
                    square[i + len + 1] %= ten;
                }
            }
        }
        var res = new BigInt(square);
        res.clearZeros();
        return res;
    }
    private void clearZeros() {
        while (digits[0] == 0) digits = Arrays.copyOfRange(digits, 1, digits.length);
    }
    private void reverseArray() {
        if (digits != null) for (var i = 0; i < digits.length / 2; ++i) {
            digits[i] ^= digits[digits.length - i - 1];
            digits[digits.length - i - 1] ^= digits[i];
            digits[i] ^= digits[digits.length - i - 1];
        }
    }
}