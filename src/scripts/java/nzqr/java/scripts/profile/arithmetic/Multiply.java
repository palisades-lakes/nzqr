package nzqr.java.scripts.profile.arithmetic;

import java.math.BigInteger;

import nzqr.java.numbers.BoundedNatural;
import nzqr.java.numbers.Naturals;
import nzqr.java.prng.Generator;
import nzqr.java.prng.Generators;
import nzqr.java.prng.PRNG;

//----------------------------------------------------------------
/** Test long -> double -> long arithmetic.
 * <p>
 * <pre>
 * j --enable-preview --source 19 src/scripts/java/nzqr/java/scripts/profile/arithmetic/Multiply.java
 * jy --enable-preview --source 19 src/scripts/java/nzqr/java/scripts/profile/arithmetic/Multiply.java
 * </pre>
 *
 * @author palisades dot lakes at gmail dot com
 * @version 2022-10-31
 */

public final class Multiply {

  private static final Naturals NATURALS = Naturals.get();
  
  private static final int N = 1024 * 1024;
  
  private static final Generator generator =  
    Generators.nonNegativeBigIntegerGenerator(
    N, PRNG.well44497b("seeds/Well44497b-2019-01-07.txt")); 
  
  private static final BigInteger[] x0 = (BigInteger[]) generator.next();
  private static final BigInteger[] x1 = (BigInteger[]) generator.next(); 

  private static final BoundedNatural[] 
    fromBigInteger (final BigInteger[] x) {
    
    final int n = x.length;
    final BoundedNatural[] y = new BoundedNatural[n];
    for (int i=0;i<n;i++) { y[i] = BoundedNatural.valueOf(x[i]); }
    return y; }

  private static final BoundedNatural[] y0 = fromBigInteger(x0);
  private static final BoundedNatural[] y1 = fromBigInteger(x1);

  private static final BoundedNatural[] p = new BoundedNatural[N];

  private static final void multiply (final String stage) {
    final long t0 = System.nanoTime();
    final int n = y0.length;
    for (int i=0;i<n;i++) { 
      p[i] = (BoundedNatural) NATURALS.multiply(y0[i],y1[i]); }
    System.out.printf(stage + " Total seconds: %4.3f\n",
      Double.valueOf((System.nanoTime()-t0)*1.0e-9));
  }

  // distinguish warmup run from profile run in call tree
  private static final void warmup () { multiply("warmup"); }
  private static final void profile () { multiply("profile"); }

  //--------------------------------------------------------------
  
  public static final void main (final String[] args) {
    warmup();
    profile();
  }

  //--------------------------------------------------------------
}
//--------------------------------------------------------------
