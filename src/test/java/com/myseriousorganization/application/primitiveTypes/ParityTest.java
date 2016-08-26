package myseriousorganization.application.primitiveTypes;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ParityTest {

    public int findParityN1(long n) {
        int parity = 0;
        while (n!=0) {
            parity = (int) (parity ^ (n & 1));
            n = n >> 1;
        }

        return parity;
    }

    public int findParityN2(long n) {
        int parity = 0;
        while (n!=0) {
            if ((n & 1) == 1) {
                parity +=1;
            }
            n = n >> 1;
        }

        return parity%2;
    }

    public int findParityN3(long n) {
        int parity = 0;
        while (n!=0) {
            parity ^= 1;
            n = n & (n-1);
        }

        return parity;
    }

//  11111111
//  11111111
    public int findParityMO(long n) {
    
        n = n ^ (n >> 32);
        n = n ^ (n >> 16);
        n = n ^ (n >> 8);
        n = n ^ (n >> 4);
        n = n & 15;

        return _lookupParity(n);
    }

    private int _lookupParity(long n) {
        int table = 27030;

        return (table >> n) & 1;
    }

    @Test
    public void test4() {
        Assert.assertEquals(0, findParityMO(0));
        Assert.assertEquals(1, findParityMO(1));
        Assert.assertEquals(1, findParityMO(2));
        Assert.assertEquals(0, findParityMO(3));
        Assert.assertEquals(0, findParityMO(5));
    }

    @Test
    public void test3() {
        Assert.assertEquals(0, findParityN3(0));
        Assert.assertEquals(1, findParityN3(1));
        Assert.assertEquals(1, findParityN3(2));
        Assert.assertEquals(0, findParityN3(3));
        Assert.assertEquals(0, findParityN3(5));
    }

    @Test
    public void test1() {
        Assert.assertEquals(0, findParityN1(0));
        Assert.assertEquals(1, findParityN1(1));
        Assert.assertEquals(1, findParityN1(2));
        Assert.assertEquals(0, findParityN1(3));
        Assert.assertEquals(0, findParityN1(5));
    }
    
    @Test
    public void test2() {
        Assert.assertEquals(0, findParityN2(0));
        Assert.assertEquals(1, findParityN2(1));
        Assert.assertEquals(1, findParityN2(2));
        Assert.assertEquals(0, findParityN2(3));
        Assert.assertEquals(0, findParityN2(5));
    }
}
