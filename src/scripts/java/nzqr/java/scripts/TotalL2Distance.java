package nzqr.java.scripts;

import nzqr.java.accumulators.Accumulator;
import nzqr.java.prng.Generator;
import nzqr.java.prng.Generators;

/** Profile L2 distance.
 *
 * <pre>
 * jy --source 12 src/scripts/java/xfp/java/scripts/TotalL2Distance.java
 * </pre>
 * @author palisades dot lakes at gmail dot com
 * @version 2019-10-10
 */
@SuppressWarnings("unchecked")
public final class TotalL2Distance {

  public static final void main (final String[] args) {
    final int dim = (1*1024*1024) - 1;
    final int trys = 8 * 1024;
    final Generator g = Generators.make("gaussian",dim);
    //final Generator g = Generators.make("finite",dim);
    //final Generator g = Generators.make("uniform",dim);
    final Accumulator a =
      nzqr.java.accumulators.RationalFloatAccumulator.make();
    //    nzqr.java.accumulators.BigFloatAccumulator.make();
    assert a.isExact();
    for (int i=0;i<trys;i++) {
      final double[] x0 = (double[]) g.next();
      final double[] x1 = (double[]) g.next();
      final double z = a.clear().addL2Distance(x0,x1).doubleValue();
      assert Double.isFinite(z); } }

  //--------------------------------------------------------------
}
//--------------------------------------------------------------
