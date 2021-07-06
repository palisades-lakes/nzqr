package nzqr.java.search;

import nzqr.java.functions.Dn;
import nzqr.java.functions.Function;
import nzqr.java.functions.scalar.DoubleBracket;

/** Find 3 <code>double</code>
 * domain values, <code>x0 &lt; x1 &lt; x2</code>, such that
 * <code>f(x0) &gt; f(x1) &lt; f(x2)</code>.
 * <p>
 * To simplify history bookkeeping It is left to the 
 * <code>objective</code> function to determine whether to re-compute,
 * or cache <code>f.doubleValue(x)</code>.
 * 
 * @author palisades dot lakes at gmail dot com
 * @version 2021-06-07
 */

public final class Bracket {

  //--------------------------------------------------------------

  private static final double GOLDEN_RATIO = 0.5*(1.0+Math.sqrt(5.0));
  public static final double MAXIMUM_MAGNIFICATION = 100.0;

  // roughly 1.0e-20
  private static final double EPSILON = Math.ulp(1.0e-4);
  

  /** See Press, Teukolsky, Vetterling, Flannery; Numerical Recipes
   * 3rd edition, section 10.1.
   */

  public static final DoubleBracket nr (final Function f,
                                        final double xmin,
                                        final double xmax) {
    assert Dn.D1 == f.domain();
    assert Dn.D1 == f.codomain();
    assert xmin < xmax;
    
    double x0 = xmin;
    double x1 = xmax;
    double y0 = f.doubleValue(x0);
    double y1 = f.doubleValue(x1);
    // downhill is the direction from x0 to x1.
    // not assuming here x0 < x1;
    if (y1 > y0) {
      x1 = xmin;
      x0 = xmax;
      y0 = f.doubleValue(x0);
      y1 = f.doubleValue(x1); }
    double x2 = x1 + GOLDEN_RATIO*(x1-x0);
    double y2 = f.doubleValue(x2);
    while (y1 > y2) {
      final double dx10 = x1-x0;
      final double dx12 = x1-x2;
      final double dy10 = y1-y0;
      final double dy12 = y1-y2;
//      final double ulim = x1 - MAXIMUM_MAGNIFICATION*dx12;
      
      final double r = dx10*dy12;
      final double q = dx12*dy10;
      final double numer = (dx12*q) - (dx10*r);
      final double dqr = q-r;
      final double denom;
      if ((dqr > EPSILON) || (dqr < EPSILON)) { denom = dqr; }
      else if (dqr >= 0.0) { denom = EPSILON; }
      else  { denom = -EPSILON; }
      final double u = x1 - numer/(2.0*denom);
  //    final double fu = f.doubleValue(u);
      
      if (0.0 < ((x1-u)*(u-x2))) {
        }
      }
    return DoubleBracket.make(x0,x1,x2); }

  //--------------------------------------------------------------
} // end interface
//--------------------------------------------------------------
