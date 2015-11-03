package amidst.minecraft;

import java.util.Random;

/** Produces some of the same simplex noise as provided by the Simplex2d class in Minecraft */
public class Simplex2dNoise
{
    public  static final double cSqrt_of_3 = Math.sqrt(3.0);
    private static int[][]      cLatticeVertices = new int[][] { { 1, 1, 0 }, { -1, 1, 0 }, { 1, -1, 0 }, { -1, -1, 0 }, { 1, 0, 1 }, { -1, 0, 1 }, { 1, 0, -1 }, { -1, 0, -1 }, { 0, 1, 1 }, { 0, -1, 1 }, { 0, 1, -1 }, { 0, -1, -1 } };
    private static final double cSimplexSkewConstantFor2d = 0.5 * (cSqrt_of_3 - 1.0);;
    
    private int[] shuffledArrayOfConsecutiveBytes;
    public double b;
    public double c;
    public double d;
    
    public Simplex2dNoise(Random rand) {
        this.shuffledArrayOfConsecutiveBytes = new int[512];
        this.b = rand.nextDouble() * 256.0;
        this.c = rand.nextDouble() * 256.0;
        this.d = rand.nextDouble() * 256.0;
        for (int i = 0; i < 256; ++i) {
            this.shuffledArrayOfConsecutiveBytes[i] = i;
        }
        for (int i = 0; i < 256; ++i) {
            final int swapIndex = rand.nextInt(256 - i) + i;
            final int temp = this.shuffledArrayOfConsecutiveBytes[i];
            this.shuffledArrayOfConsecutiveBytes[i] = this.shuffledArrayOfConsecutiveBytes[swapIndex];
            this.shuffledArrayOfConsecutiveBytes[swapIndex] = temp;
            this.shuffledArrayOfConsecutiveBytes[i + 256] = this.shuffledArrayOfConsecutiveBytes[i];
        }
    }
    
    private static int RoundTowardZero(final double value) {
        return (value > 0.0) ? ((int)value) : ((int)value - 1);
    }
    
    private static double a(final int[] vertice, final double x, final double y) {
        return vertice[0] * x + vertice[1] * y;
    }
    
    // TODO: Rewrite this
    public double a(final double a1, final double a2) {
        final double v2 = (a1 + a2) * cSimplexSkewConstantFor2d;
        final int v3 = RoundTowardZero(/*EL:68*/a1 + v2);
        final int v4 = RoundTowardZero(/*EL:69*/a2 + v2);
        final double v5 = /*EL:70*/(3.0 - cSqrt_of_3) / 6.0;
        final double v6 = /*EL:71*/(v3 + v4) * v5;
        final double v7 = /*EL:72*/v3 - v6;
        final double v8 = /*EL:73*/v4 - v6;
        final double v9 = /*EL:74*/a1 - v7;
        final double v10 = /*EL:75*/a2 - v8;
        int v11;
        int v12;
        /*SL:79*/if (v9 > v10) {
            /*SL:80*/v11 = 1;
            /*SL:81*/v12 = 0;
        }
        else {
            /*SL:84*/v11 = 0;
            /*SL:85*/v12 = 1;
        }
        final double v13 = /*EL:90*/v9 - v11 + v5;
        final double v14 = /*EL:91*/v10 - v12 + v5;
        final double v15 = /*EL:92*/v9 - 1.0 + 2.0 * v5;
        final double v16 = /*EL:93*/v10 - 1.0 + 2.0 * v5;
        final int v17 = /*EL:95*/v3 & 0xFF;
        final int v18 = /*EL:96*/v4 & 0xFF;
        final int v19 = /*EL:97*/this.shuffledArrayOfConsecutiveBytes[v17       + this.shuffledArrayOfConsecutiveBytes[v18      ]] % 12;
        final int v20 = /*EL:98*/this.shuffledArrayOfConsecutiveBytes[v17 + v11 + this.shuffledArrayOfConsecutiveBytes[v18 + v12]] % 12;
        final int v21 = /*EL:99*/this.shuffledArrayOfConsecutiveBytes[v17 + 1   + this.shuffledArrayOfConsecutiveBytes[v18 + 1  ]] % 12;
        double v22 = /*EL:101*/0.5 - v9 * v9 - v10 * v10;
        double n;
        /*SL:102*/if (v22 < 0.0) {
            /*SL:103*/n = 0.0;
        }
        else {
            /*SL:105*/v22 *= v22;
            /*SL:106*/n = v22 * v22 * a(cLatticeVertices[v19], v9, v10);
        }
        double v23 = /*EL:108*/0.5 - v13 * v13 - v14 * v14;
        double n2;
        /*SL:109*/if (v23 < 0.0) {
            /*SL:110*/n2 = 0.0;
        }
        else {
            /*SL:112*/v23 *= v23;
            /*SL:113*/n2 = v23 * v23 * a(cLatticeVertices[v20], v13, v14);
        }
        double v24 = /*EL:115*/0.5 - v15 * v15 - v16 * v16;
        double n3;
        /*SL:116*/if (v24 < 0.0) {
            /*SL:117*/n3 = 0.0;
        }
        else {
            /*SL:119*/v24 *= v24;
            /*SL:120*/n3 = v24 * v24 * a(cLatticeVertices[v21], v15, v16);
        }
        return 70.0 * (n + n2 + n3);
    }
}
