package nzqr.java.test.functions.scalar;

import static java.lang.Double.NaN;

import nzqr.java.functions.Domain;
import nzqr.java.functions.Function;
import nzqr.java.functions.scalar.AffineFunctional;
import nzqr.java.functions.scalar.ScalarFunctional;

//----------------------------------------------------------------
/** Test function for 1d minimization (see 
 * org.apache.commons.math3.optim.univariate.BrentOptimizerTest).
 * <p>
 *
 * @author palisades dot lakes at gmail dot com
 * @version 2018-09-27
 */

public final class Math832 extends ScalarFunctional {

  //--------------------------------------------------------------
  // Function methods
  //--------------------------------------------------------------

  @Override
  public final double doubleValue (final double x) {
    final double sqrtX = Math.sqrt(x);
    final double a = 1.0e2 * sqrtX;
    final double b = 1.0e6 / x;
    final double c = 1.0e4 / sqrtX;
    return a + b + c; }

  @Override
  public final double slope (final double x) {
    final double sqrtX = Math.sqrt(x);
    final double da = 50.0 / sqrtX;
    final double db = -1.0e6 / x*x;
    final double dc = -5.0e3 / x*sqrtX;
    return da + db + dc; }

  @Override
  public final Function tangentAt (final double x) {
    return AffineFunctional.make(doubleValue(x),slope(x)); }

  // TODO: more decimal places?
  @Override
  public final double doubleArgmin (final Domain support) { 
    if (support.contains(804.9355825)) { return 804.9355825;  } 
    return NaN; }
  
  //--------------------------------------------------------------
  // construction
  //--------------------------------------------------------------
  // TODO: singleton?

  private Math832 () { super(); }

  //--------------------------------------------------------------
  /** Return a {@link Math832} test function of the given
   * <code>dimension</code>.
   */

  public static final Math832 get () { return new Math832(); }

  //--------------------------------------------------------------
} // end class
//--------------------------------------------------------------

