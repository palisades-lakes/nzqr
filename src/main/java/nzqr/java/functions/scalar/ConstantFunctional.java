package nzqr.java.functions.scalar;

import static java.lang.Double.NaN;
import static java.lang.Double.isNaN;

import nzqr.java.functions.Domain;
import nzqr.java.functions.Function;

/** An quadratic function from <b>R</b> to <b>R</b> in Lagrange 
 * form.
 *
 * @author palisades dot lakes at gmail dot com
 * @version 2018-10-08
 */

public final class ConstantFunctional extends Polynomial {

  //--------------------------------------------------------------
  // fields
  //--------------------------------------------------------------

  private final double _y;

  //--------------------------------------------------------------
  // Polynomial methods
  //--------------------------------------------------------------

  @Override
  public final int degree () { return 0; }
  
  //--------------------------------------------------------------
  // Function methods
  //--------------------------------------------------------------

  @Override
  public final double doubleValue (final double x) {
    if (isNaN(x)) { return NaN; }
    return _y; }
  @Override
  public final double slope (final double x) {
    if (isNaN(x)) { return NaN; }
    return 0.0; }

  @Override
  public final double doubleArgmin (final Domain support) { 
    return NaN; }

  //--------------------------------------------------------------
  // Object methods
  //--------------------------------------------------------------

  @Override
  public final String toString () {
    return getClass().getSimpleName() + "[" + _y + "]"; }

  //--------------------------------------------------------------
  // construction
  //--------------------------------------------------------------

  private ConstantFunctional (final double y) {
    _y = y; }

  public static final ConstantFunctional 
  make (final double y) {
    return new ConstantFunctional(y); }

  public static final boolean
  validKnots (final double[][] knots) {
    return validKnots(knots,0); }
  
  public static final ConstantFunctional 
  interpolate (final Function f, 
                 final double[][] x) {
    assert validKnots(x);
    return make(f.doubleValue(x[0][0]));}

  public static final ConstantFunctional 
  interpolate (final Object f, 
                 final Object x) {
    return interpolate((Function) f, (double[][]) x); } 

  //--------------------------------------------------------------
}
//--------------------------------------------------------------

