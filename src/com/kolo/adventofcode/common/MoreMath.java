package com.kolo.adventofcode.common;

import java.math.BigInteger;
import java.util.Arrays;

public final class MoreMath {

    public static BigInteger gcd(BigInteger a, BigInteger b) {
        if (a.equals(BigInteger.ZERO))
            return b;
        return gcd(b.mod(a), a);
    }

    public static BigInteger lcm(BigInteger a, BigInteger b) {
        return (a.multiply(b)).divide(gcd(a, b));
    }

    public static BigInteger lcm(BigInteger... n) {
        if (n.length == 2) {
            return lcm(n[0], n[1]);
        }
        
        return lcm(n[0], lcm(Arrays.copyOfRange(n, 1, n.length)));
    }

    public static BigInteger gcd(int a, int b) {
        return gcd(BigInteger.valueOf(a), BigInteger.valueOf(b));
    }

    public static BigInteger lcm(int a, int b) {
        return lcm(BigInteger.valueOf(a), BigInteger.valueOf(b));
    }

    public static BigInteger lcm(int... n) {
        BigInteger[] nBigInt = new BigInteger[n.length];
        for (int i = 0; i < n.length; i++) {
            nBigInt[i] = BigInteger.valueOf(n[i]);
        }
        return lcm(nBigInt);
    }
}
