package nzqr.java.test.functions.scalar;

import static java.lang.Double.NaN;
import static java.lang.Math.*;

import nzqr.java.functions.Domain;
import nzqr.java.functions.Function;
import nzqr.java.functions.scalar.AffineFunctional;
import nzqr.java.functions.scalar.ScalarFunctional;

//----------------------------------------------------------------
/** Test function for 1d minimization.
 * <p>
 * See http://heath.cs.illinois.edu/scicomp/notes/chap07.pdf
 *
 * @author palisades dot lakes at gmail dot com
 * @version 2018-09-28
 */

public final class Runge extends ScalarFunctional {

  //--------------------------------------------------------------
  // Function methods
  //--------------------------------------------------------------

  @Override
  public final double doubleValue (final double x) { 
    final double denom = fma(25.0*x,x,1.0); 
    return 2.0-(1.0/denom); }

  @Override
  public final double slope (final double x) { 
    final double denom = fma(25.0*x,x,1.0); 
    return (50.0*x)/(denom*denom); }

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
  
  private Runge () { super(); }

  //--------------------------------------------------------------
  /** Return a {@link Runge} test function of the given
   * <code>dimension</code>.
   */

  public static final Runge get () { return new Runge(); }

  //--------------------------------------------------------------
} // end class
//--------------------------------------------------------------

