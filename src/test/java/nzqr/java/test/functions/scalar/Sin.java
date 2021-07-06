package nzqr.java.test.functions.scalar;

import nzqr.java.functions.Function;
import nzqr.java.functions.scalar.AffineFunctional;
import nzqr.java.functions.scalar.ScalarFunctional;

//----------------------------------------------------------------
/** Test function for 1d minimization.
 * <p>
 *
 * @author palisades dot lakes at gmail dot com
 * @version 2018-09-07
 */

public final class Sin extends ScalarFunctional {

  //--------------------------------------------------------------
  // Function methods
  //--------------------------------------------------------------

  @Override
  public final double doubleValue (final double x) {
    return Math.sin(x); }

  //--------------------------------------------------------------

  @Override
  public final double slope (final double x) {
    return Math.cos(x); }
  
  //--------------------------------------------------------------

  @Override
  public final Function tangentAt (final double x) {
    return AffineFunctional.make(doubleValue(x),slope(x)); }
  
  //--------------------------------------------------------------
  // construction
  //--------------------------------------------------------------
  // TODO: singleton?
  
  private Sin () { super(); }

  //--------------------------------------------------------------
  /** Return a {@link Sin} test function of the given
   * <code>dimension</code>.
   */

  public static final Sin get () { return new Sin(); }

  //--------------------------------------------------------------
} // end class
//--------------------------------------------------------------

