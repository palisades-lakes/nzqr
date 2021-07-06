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

public final class SumTwo1d extends ScalarFunctional {

  private final Function _f0;
  private final Function _f1;

  //--------------------------------------------------------------
  // Function methods
  //--------------------------------------------------------------

  @Override
  public final double doubleValue (final double x) { 
    return _f0.doubleValue(x) + _f1.doubleValue(x); }

  //--------------------------------------------------------------

  @Override
  public final double slope (final double x) { 
    return _f0.slope(x) + _f1.slope(x); }

  //--------------------------------------------------------------

  @Override
  public final Function tangentAt (final double x) {
    return AffineFunctional.make(doubleValue(x),slope(x)); }

  //--------------------------------------------------------------
  // construction
  //--------------------------------------------------------------

  private SumTwo1d (final Function f0,
                    final Function f1) { 
    super(); 
    // assuming immutable...strictly just that don't need to copy.
    _f0 = f0;
    _f1 = f1; }

  //--------------------------------------------------------------
  /** Return a {@link SumTwo1d} test function of the given
   * <code>dimension</code>.
   */

  public static final SumTwo1d get (final Function f0,
                                    final Function f1)  { 
    return new SumTwo1d(f0,f1); }

  //--------------------------------------------------------------
} // end class
//--------------------------------------------------------------

