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
 * Negative of commons math3 quintic test function.
 * Zeros at 0, +-0.5 and +-1.
 * Local minimum at 0.27195613.
 *
 * @author palisades dot lakes at gmail dot com
 * @version 2018-09-26
 */

public final class Quintic extends ScalarFunctional {

  //--------------------------------------------------------------
  // Function methods
  //--------------------------------------------------------------

  @Override
  public final double doubleValue (final double x) {
    return -(x-1.0)*(x-0.5)*x*(x+0.5)*(x+1.0); }

  // TODO: accurate polynomial evaluation?

  @Override
  public final double slope (final double x) {
    return 
      -(((x-0.5)*x*(x+0.5)*(x+1.0)) +
      ((x-1.0)*x*(x+0.5)*(x+1.0)) +
      ((x-1.0)*(x-0.5)*(x+0.5)*(x+1.0)) +
      ((x-1.0)*(x-0.5)*x*(x+1.0)) +
      ((x-1.0)*(x-0.5)*x*(x+0.5))); }

  @Override
  public final Function tangentAt (final double x) {
    return AffineFunctional.make(doubleValue(x),slope(x)); }

  @Override
  public final double doubleArgmin (final Domain support) { 
    if (support.contains(0.27195613)) { return 0.27195613; }
    return NaN; }
  
  //--------------------------------------------------------------
  // construction
  //--------------------------------------------------------------
  // TODO: singleton?

  private Quintic () { super(); }

  //--------------------------------------------------------------
  /** Return a {@link Quintic} test function of the given
   * <code>dimension</code>.
   */

  public static final Quintic get () { return new Quintic(); }

  //--------------------------------------------------------------
} // end class
//--------------------------------------------------------------

