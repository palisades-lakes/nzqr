package nzqr.java.scripts.profile.arithmetic;

import nzqr.java.numbers.BoundedNatural;
import nzqr.java.numbers.Naturals;
import nzqr.java.prng.Generator;
import nzqr.java.prng.Generators;
import nzqr.java.prng.PRNG;

import nzqr.openjdk.math.BigIntegerJDK;

import java.math.BigInteger;

//----------------------------------------------------------------

/** Profile natural number division.
 * <p>
 * <pre>
 * jy --enable-preview --source 21 src/scripts/java/nzqr/java/scripts/profile/arithmetic/AddBIJ.java
 * </pre>
 *
 * @author palisades dot lakes at gmail dot com
 * @version 2024-01-16
 * */

public final class AddBIJ {
  private static final Naturals NATURALS = Naturals.get();
  private static final int FACTOR = 1;
  private static final int NBYTES = FACTOR * 256;
  private static final int NINTS = 1024 * 1024 / FACTOR;

  private static final Generator generator =  
    Generators.nonNegativeBigIntegerGenerator(
      NBYTES,
      PRNG.well44497b("seeds/Well44497b-2019-01-07.txt"), 
      NINTS); 

  private static final BigInteger[] x0 = (BigInteger[]) generator.next();
  private static final BigInteger[] x1 = (BigInteger[]) generator.next();

  private static final BigIntegerJDK[] fromBigInteger (final BigInteger[] x) {
    final int n = x.length;
    final BigIntegerJDK[] y = new BigIntegerJDK[n];
    for (int i=0;i<n;i++) { y[i] = new BigIntegerJDK(x[i].toByteArray()); }
    return y; }

  private static final BigIntegerJDK[] y0 = fromBigInteger(x0);
  private static final BigIntegerJDK[] y1 = fromBigInteger(x1);

  private static final BigIntegerJDK[] p = new BigIntegerJDK[NINTS];

  private static final void add (final String stage,
                                 final int iterations) {
    final int n = y0.length;
    for (int j=0;j<iterations;j++) {
      final long t0 = System.nanoTime();
      for (int i=0;i<n;i++) {
        p[i] = y0[i].add(y1[i]);
        assert y1[i].compareTo(p[i]) <= 0; }
      System.out.printf(stage + " " + j + " Total seconds: %4.3f\n",
        (System.nanoTime()-t0)*1.0e-9); } }

  // distinguish warmup run from profile run in call tree
  private static final void warmup () { add("warmup",16); }
  private static final void profile () { add("profile",128); }

  //--------------------------------------------------------------

  public static final void main (final String[] args) {
    warmup(); profile();   }

  //--------------------------------------------------------------
}
//--------------------------------------------------------------
