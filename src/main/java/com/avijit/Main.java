package com.avijit;

import java.math.BigInteger;
import java.util.Scanner;

class Main {

    public static void main(String[] args) {
        // your code goes here
        Scanner scan = new Scanner(System.in);
        int t = scan.nextInt();
        for (int cs = 1; cs <= t; ++cs) {
            String a = scan.next();
            String b = scan.next();
            System.out.println(a + " " + b);
            BigInteger bg = new BigInteger(a);
            BigInteger gg = new BigInteger(b);
            BigInteger ans = pow(bg, gg);
            String an = ans.toString();
            long gt = gv(an);

            while (gt >= 10) {
                gt = sod(gt);
            }
            System.out.println("Case " + cs + ": " + gt);
        }

    }

    public static long sod(long x) {
        long anss = 0;
        while (x != 0) {
            anss += x % 10;
            x /= 10;
        }
        return anss;
    }

    public static long gv(String x) {
        long xx = 0;
        for (int i = 0; i < x.length(); ++i) {
            xx += x.charAt(i) - '0';
        }
        return xx;
    }

    public static BigInteger pow(BigInteger base, BigInteger exponent) {
        BigInteger result = BigInteger.ONE;
        while (exponent.signum() > 0) {
            if (exponent.testBit(0)) {
                result = result.multiply(base);
            }
            base = base.multiply(base);
            exponent = exponent.shiftRight(1);
        }
        return result;
    }
}