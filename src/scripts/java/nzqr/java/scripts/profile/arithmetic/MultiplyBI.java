package nzqr.java.scripts.profile.arithmetic;

import java.math.BigInteger;

import nzqr.java.numbers.Naturals;
import nzqr.java.prng.Generator;
import nzqr.java.prng.Generators;
import nzqr.java.prng.PRNG;

//----------------------------------------------------------------
/** Test long -> double -> long arithmetic.
 * <p>
 * <pre>
 * j --enable-preview --source 19 src/scripts/java/nzqr/java/scripts/profile/arithmetic/MultiplyBI.java
 * jy --enable-preview --source 19 src/scripts/java/nzqr/java/scripts/profile/arithmetic/MultiplyBI.java
 * </pre>
 *
 * @author palisades dot lakes at gmail dot com
 * @version 2022-11-07
 */

public final class MultiplyBI {

  private static final Naturals NATURALS = Naturals.get();
  private static final int NBYTES = 256;
  private static final int NINTS = 1024 * 1024;
  
  private static final Generator generator =  
    Generators.nonNegativeBigIntegerGenerator(
    NBYTES, PRNG.well44497b("seeds/Well44497b-2019-01-07.txt"), NINTS); 
  
  private static final BigInteger[] x0 = (BigInteger[]) generator.next();
  private static final BigInteger[] x1 = (BigInteger[]) generator.next(); 

  private static final Object[] 
    fromBigInteger (final BigInteger[] x) {
    
    return x; }

  private static final Object[] y0 = fromBigInteger(x0);
  private static final Object[] y1 = fromBigInteger(x1);

  private static final Object[] p = new Object[NINTS];

  private static final void multiply (final String stage,
                                      final int iterations) {
    final int n = y0.length;
    for (int j=0;j<iterations;j++) {
      final long t0 = System.nanoTime();
      for (int i=0;i<n;i++) {
        p[i] =  NATURALS.multiply(y0[i],y1[i]); }
      System.out.printf(stage + " Total seconds: %4.3f\n",
        Double.valueOf((System.nanoTime()-t0)*1.0e-9)); } }

  // distinguish warmup run from profile run in call tree
  private static final void warmup () { multiply("warmup",2); }
  private static final void profile () { multiply("profile",16); }

  //--------------------------------------------------------------
  
  public static final void main (final String[] args) {
    warmup();
    profile();
  }

  //--------------------------------------------------------------
}
//--------------------------------------------------------------
