package nzqr.java.test.functions.scalar;

import static java.lang.Double.NaN;

import nzqr.java.functions.Domain;
import nzqr.java.functions.Function;
import nzqr.java.functions.scalar.AffineFunctional;
import nzqr.java.functions.scalar.ScalarFunctional;

//----------------------------------------------------------------
/** Test function for 1d minimization.
 * <p>
 *
 * @author palisades dot lakes at gmail dot com
 * @version 2018-09-28
 */

public final class Square extends ScalarFunctional {

  //--------------------------------------------------------------
  // Function methods
  //--------------------------------------------------------------

  @Override
  public final double doubleValue (final double x) { return x*x; }

  @Override
  public final double slope (final double x) { return 2.0*x; }

  @Override
  public final Function tangentAt (final double x) {
    return AffineFunctional.make(doubleValue(x),slope(x)); }
  
  @Override
  public final double doubleArgmin (final Domain support) { 
    if (support.contains(0.0)) { return 0.0; }
    return NaN; }

  //--------------------------------------------------------------
  // construction
  //--------------------------------------------------------------
  // TODO: singleton?
  
  private Square () { super(); }

  //--------------------------------------------------------------
  /** Return a {@link Square} test function of the given
   * <code>dimension</code>.
   */

  public static final Square get () { return new Square(); }

  //--------------------------------------------------------------
} // end class
//--------------------------------------------------------------

