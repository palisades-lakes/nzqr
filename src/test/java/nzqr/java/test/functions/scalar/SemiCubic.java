package nzqr.java.test.functions.scalar;

import nzqr.java.functions.Function;
import nzqr.java.functions.scalar.AffineFunctional;
import nzqr.java.functions.scalar.ScalarFunctional;

//----------------------------------------------------------------
/** Test function for 1d minimization (see 
 * org.apache.commons.math3.optim.univariate.BracketFinderTest).
 * <p>
 *
 * @author palisades dot lakes at gmail dot com
 * @version 2018-09-07
 */

public final class SemiCubic extends ScalarFunctional {

  //--------------------------------------------------------------
  // Function methods
  //--------------------------------------------------------------

  @Override
  public final double doubleValue (final double x) {
    if (x < -2.0) { return doubleValue(-2.0); }
    return (x - 1.0) * (x + 2.0) * (x + 3.0); }

  //--------------------------------------------------------------
  // TODO: accurate polynomial evaluation?

  @Override
  public final double slope (final double x) {
    if (x < -2.0) { return 0.0; }
    return 
      ((x + 2.0) * (x + 3.0)) + 
      ((x - 1.0) * (x + 3.0)) + 
      ((x - 1.0) * (x + 2.0)); }

  //--------------------------------------------------------------

  @Override
  public final Function tangentAt (final double x) {
    return AffineFunctional.make(doubleValue(x),slope(x)); }

  //--------------------------------------------------------------
  // construction
  //--------------------------------------------------------------
  // TODO: singleton?

  private SemiCubic () { super(); }

  //--------------------------------------------------------------
  /** Return a {@link SemiCubic} test function of the given
   * <code>dimension</code>.
   */

  public static final SemiCubic get () { return new SemiCubic(); }

  //--------------------------------------------------------------
} // end class
//--------------------------------------------------------------

