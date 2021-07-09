package nzqr.java.scripts;

import static java.lang.Double.toHexString;

import java.util.Arrays;

import org.apache.commons.rng.UniformRandomProvider;
import org.apache.commons.rng.sampling.ListSampler;

import nzqr.java.Classes;
import nzqr.java.accumulators.Accumulator;
import nzqr.java.accumulators.RationalFloatAccumulator;
import nzqr.java.numbers.Doubles;
import nzqr.java.prng.Generator;
import nzqr.java.prng.PRNG;

/** Benchmark rational dot products.
 *
 * <pre>
 * jy --source 12 src/scripts/java/xfp/java/scripts/RationalDot.java
 * </pre>
 * @author palisades dot lakes at gmail dot com
 * @version 2021-07-08
 */
@SuppressWarnings("unchecked")
public final class RationalDot {

  //--------------------------------------------------------------

  private static final int DIM = 1024 * 1024;
  private static final int N = 128;

  //--------------------------------------------------------------

  public static final void main (final String[] args)
    throws InterruptedException {

    final double[][] x0 = Doubles.sampleDoubles(DIM,N);
    final double[][] x1 = Doubles.sampleDoubles(DIM,N);

    // should be zero with current construction
    final double[] truth = new double[N];
    final double[] pred = new double[N];
    for (int i=0;i<N;i++) {
      truth[i] =
        RationalFloatAccumulator.make().addProducts(x0[i],x1[i]).doubleValue(); }

    for (int i=0;i<N;i++) {
      System.out.println(
        i + " : "
          + Double.toHexString(truth[i])
          + ", "
          + Double.toHexString(Doubles.maxAbs(x0[i]))
          + ", "
          + Double.toHexString(Doubles.maxAbs(x1[i]))); }
    System.out.println();
    final Accumulator[] accumulators =
    {
     RationalFloatAccumulator.make(),
    };

    Thread.sleep(16*1024);
    for (final Accumulator a : accumulators) {
      long t;
      t = System.nanoTime();
      for (int i=0;i<N;i++) {
        pred[i] =
          a.clear().addProducts(x0[i],x1[i]).doubleValue(); }
      t = (System.nanoTime()-t);

      System.out.println(toHexString(Doubles.l1Dist(truth,pred)) +
        " in " + (t*1.0e-9)
        + " secs " + Classes.className(a)); }

    Thread.sleep(16*1024); }

  //--------------------------------------------------------------
}
//--------------------------------------------------------------
