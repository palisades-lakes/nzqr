package nzqr.java.scripts.profile.arithmetic;

import java.math.BigInteger;

import nzqr.java.numbers.BoundedNatural;
import nzqr.java.numbers.Naturals;
import nzqr.java.prng.Generator;
import nzqr.java.prng.Generators;
import nzqr.java.prng.PRNG;

//----------------------------------------------------------------
/** Profile natural number division.
 * <p>
 * <pre>
 * j --enable-preview --source 20 src/scripts/java/nzqr/java/scripts/profile/arithmetic/DivideAndRemainderBN.java
 * jy --enable-preview --source 20 src/scripts/java/nzqr/java/scripts/profile/arithmetic/DivideAndRemainderBN.java
 * </pre>
 *
 * @author palisades dot lakes at gmail dot com
 * @version 2023-08-15
 */

public final class DivideAndRemainderBN {

  private static final Naturals NATURALS = Naturals.get();
  private static final int NBYTES = 256;
  private static final int NINTS = 1024 * 1024;

  private static final Generator generator =  
    Generators.nonNegativeBigIntegerGenerator(
      NBYTES,
      PRNG.well44497b("seeds/Well44497b-2019-01-07.txt"), 
      NINTS); 

  private static final BigInteger[] x0 = (BigInteger[]) generator.next();
  private static final BigInteger[] x1 = (BigInteger[]) generator.next(); 

  private static final Object[] fromBigInteger (final BigInteger[] x) {
    final int n = x.length;
    final Object[] y = new Object[n];
    for (int i=0;i<n;i++) { y[i] = BoundedNatural.valueOf(x[i]); }
    return y; }

  private static final Object[] y0 = fromBigInteger(x0);
  private static final Object[] y1 = fromBigInteger(x1);

  private static final Object[] p = new Object[NINTS];

  private static final void divideAndRemainder (final String stage,
                                                final int iterations) {
    final int n = y0.length;
    for (int j=0;j<iterations;j++) {
      final long t0 = System.nanoTime();
      for (int i=0;i<n;i++) {
        p[i] = NATURALS.divideAndRemainder(y0[i],y1[i]);
        final BoundedNatural rem = (BoundedNatural) ((Object[]) p[i])[1];
        assert ((BoundedNatural)y1[i]).compareTo(rem) > 0; }
      System.out.printf(stage + " " + j + " Total seconds: %4.3f\n",
        (System.nanoTime()-t0)*1.0e-9); } }

  // distinguish warmup run from profile run in call tree
  private static final void warmup () { divideAndRemainder("warmup",4); }
  private static final void profile () { divideAndRemainder("profile",32); }

  //--------------------------------------------------------------

  public static final void main (final String[] args) {
    warmup();
    profile();
  }

  //--------------------------------------------------------------
}
//--------------------------------------------------------------
