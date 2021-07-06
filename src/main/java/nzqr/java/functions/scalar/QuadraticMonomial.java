package nzqr.java.functions.scalar;

import static java.lang.Double.NEGATIVE_INFINITY;
import static java.lang.Double.NaN;
import static java.lang.Double.POSITIVE_INFINITY;
import static java.lang.Double.isFinite;
import static java.lang.Double.isNaN;
import static java.lang.Math.fma;

import java.util.Arrays;

import nzqr.java.functions.Domain;
import nzqr.java.functions.Function;

/** A quadratic function from <b>R</b> to <b>R</b> in monomial 
 * form.
 * 
 * @author palisades dot lakes at gmail dot com
 * @version 2018-10-08
 */

public final class QuadraticMonomial extends Polynomial {

  //--------------------------------------------------------------
  // fields
  //--------------------------------------------------------------

  private final double _a0;
  private final double _a1;
  private final double _a2;

  // TODO: space vs re-computing cost?
  private final double _xmin;

  private final double _positiveLimitValue;
  private final double _negativeLimitValue;
  private final double _positiveLimitSlope;
  private final double _negativeLimitSlope;

  //--------------------------------------------------------------
  // Polynomial methods
  //--------------------------------------------------------------

  @Override
  public final int degree () { return 2; }
  
  //--------------------------------------------------------------
  // Function methods
  //--------------------------------------------------------------
  // Horner's algorithm

  @Override
  public final double doubleValue (final double x) {
    if (isFinite(x)) {
      return fma(x,fma(x,_a2,_a1),_a0);  }
    if (isNaN(x)) { return NaN; }
    if (POSITIVE_INFINITY == x) { return _positiveLimitValue; }
    return _negativeLimitValue; }

  @Override
  public final double slope (final double x) {
    if (isFinite(x)) {
      return fma(x,2.0*_a2,_a1);  }
    if (isNaN(x)) { return NaN; }
    if (POSITIVE_INFINITY == x) { return _positiveLimitSlope; }
    return _negativeLimitSlope; }

  @Override
  public final double doubleArgmin (final Domain support) { 
    final Interval bounds = (Interval) support;
    if (bounds.contains(_xmin)) { return _xmin; }
    return NaN; }

  //--------------------------------------------------------------
  // Object methods
  //--------------------------------------------------------------
  // TODO: too long for toString..

  @Override
  public final String toString () {
    return
      getClass().getSimpleName() + "[" + 
      _a0 + " + " +
      _a1 + "*x + " +
      _a2 + "*x^2; " +
      _xmin + "]"; }

  //--------------------------------------------------------------
  // construction
  //--------------------------------------------------------------

  private QuadraticMonomial (final double a0, 
                             final double a1,
                             final double a2) {
    _a0 = a0;
    _a1 = a1;
    _a2 = a2;
    if (0.0 < a2) {
      _xmin = -0.5*a1/a2;
      _positiveLimitValue = POSITIVE_INFINITY; 
      _negativeLimitValue = POSITIVE_INFINITY; 
      _positiveLimitSlope = POSITIVE_INFINITY; 
      _negativeLimitSlope = NEGATIVE_INFINITY; }
    else if (0.0 > a2) {
      _xmin = POSITIVE_INFINITY;
      _positiveLimitValue = NEGATIVE_INFINITY; 
      _negativeLimitValue = NEGATIVE_INFINITY; 
      _positiveLimitSlope = NEGATIVE_INFINITY; 
      _negativeLimitSlope = POSITIVE_INFINITY; }
    else {// affine, look at a1
      if (0.0 < a1) {
        _xmin = NEGATIVE_INFINITY;
        _positiveLimitValue = POSITIVE_INFINITY; 
        _negativeLimitValue = NEGATIVE_INFINITY; 
        _positiveLimitSlope = a1; 
        _negativeLimitSlope = a1; }
      else if (0.0 > a1) {
        _xmin = POSITIVE_INFINITY;
        _positiveLimitValue = NEGATIVE_INFINITY; 
        _negativeLimitValue = POSITIVE_INFINITY; 
        _positiveLimitSlope = a1; 
        _negativeLimitSlope = a1; }
      else { // constant
        _xmin = NaN;
        _positiveLimitValue = a0; 
        _negativeLimitValue = a0; 
        _positiveLimitSlope = 0.0; 
        _negativeLimitSlope = 0.0; } } } 

  public static final ScalarFunctional 
  make (final double a0, 
        final double a1,
        final double a2) {
    if (0.0==a2) {
      if (0.0==a1) { return ConstantFunctional.make(a0); }
      return AffineFunctional.make(a0,a1); }
    return new QuadraticMonomial(a0,a1,a2); }
 
  //--------------------------------------------------------------

  public static final ScalarFunctional 
  interpolateXY (final double x0, final double y0, 
                 final double x1, final double y1,
                 final double x2, final double y2) {
    final double[] a = 
      Polynomial.interpolatingMonomialCoefficients(
        x0,y0,x1,y1,x2,y2);
    return make(a[0],a[1],a[2]); }

  public static final ScalarFunctional 
  interpolateXY (final Function f,
                 final double[] x) {
    return interpolateXY(
      x[0],f.doubleValue(x[0]),
      x[1],f.doubleValue(x[1]),
      x[2],f.doubleValue(x[2])); }

  // only interpolates 3 values for now.
  public static final boolean 
  validKnots (final double[][] knots) {
    return 
      validKnots(knots,2)
      && 
      (3==knots[0].length) 
      && 
      (0== knots[1].length); }

  public static final ScalarFunctional 
  interpolate (final Function f, 
               final double[][] x) {
    assert validKnots(x) : 
      Arrays.toString(x[0]) + ", " + Arrays.toString(x[1]);
    return interpolateXY(
      x[0][0],f.doubleValue(x[0][0]),
      x[0][1],f.doubleValue(x[0][1]),
      x[0][2],f.doubleValue(x[0][2]));}

  public static final ScalarFunctional 
  interpolate (final Object f, 
               final Object x) {
    return interpolate((Function) f, (double[][]) x);}

  //--------------------------------------------------------------
}
//--------------------------------------------------------------

