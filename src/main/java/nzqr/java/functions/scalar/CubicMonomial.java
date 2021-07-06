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

/** A cubic function from <b>R</b> to <b>R</b> in monomial form.
 * 
 * @author palisades dot lakes at gmail dot com
 * @version 2018-10-08
 */

public final class CubicMonomial extends Polynomial {

  //--------------------------------------------------------------
  // fields
  //--------------------------------------------------------------

  private final double _a0;
  private final double _a1;
  private final double _a2;
  private final double _a3;

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
  public final int degree () { return 3; }
  
  //--------------------------------------------------------------
  // Function methods
  //--------------------------------------------------------------
  // Horner's algorithm

  @Override
  public final double doubleValue (final double x) {
    if (isFinite(x)) {
      return fma(x,fma(x,fma(x,_a3,_a2),_a1),_a0);  }
    if (isNaN(x)) { return NaN; }
    if (POSITIVE_INFINITY == x) { return _positiveLimitValue; }
    return _negativeLimitValue; }

  @Override
  public final double slope (final double x) {
    if (isFinite(x)) {
      return fma(x,fma(x,3.0*_a3,2.0*_a2),_a1);  }
    if (isNaN(x)) { return NaN; }
    if (POSITIVE_INFINITY == x) { return _positiveLimitSlope; }
    return _negativeLimitSlope; }

  @Override
  public final double doubleArgmin (final Domain support) { 
    if (support.contains(_xmin)) { return _xmin; }
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
      _a2 + "*x^2 + " +
      _a3 + "*x^3; " +
      _xmin + "]"; }

  //--------------------------------------------------------------
  // construction
  //--------------------------------------------------------------

  private static final double argmin (final double a1,
                                      final double a2,
                                      final double a3) {
    final double[] roots = Polynomial.roots(a1,2.0*a2,3.0*a3);
    //System.out.println(Arrays.toString(roots));
    assert 2 >= roots.length;
    if (0 == roots.length) { // no critical points
      if (0.0 < a3) { return NEGATIVE_INFINITY; }
      return POSITIVE_INFINITY; } 
    else if (2 == roots.length) {
      if (6.0*a3*roots[0] + 2.0*a2 > 0.0) { return roots[0]; }
      return roots[1]; }
    else { // 1 == roots.length;
      if (0.0 < a3) { return NEGATIVE_INFINITY; }
      return POSITIVE_INFINITY; } }

  //--------------------------------------------------------------

  private CubicMonomial (final double a0, 
                         final double a1,
                         final double a2,
                         final double a3) {
    _a0 = a0;
    _a1 = a1;
    _a2 = a2;
    _a3 = a3;
    _xmin = argmin(a1,a2,a3); 

    if (0.0 < a3) {
      _positiveLimitValue = POSITIVE_INFINITY; 
      _negativeLimitValue = NEGATIVE_INFINITY; 
      _positiveLimitSlope = POSITIVE_INFINITY; 
      _negativeLimitSlope = NEGATIVE_INFINITY; }
    else if (0.0 > a3) {
      _positiveLimitValue = NEGATIVE_INFINITY; 
      _negativeLimitValue = POSITIVE_INFINITY; 
      _positiveLimitSlope = NEGATIVE_INFINITY; 
      _negativeLimitSlope = POSITIVE_INFINITY;  }
    else { // quadratic, so look at a2:
      if (0.0 < a2) {
        _positiveLimitValue = POSITIVE_INFINITY; 
        _negativeLimitValue = POSITIVE_INFINITY; 
        _positiveLimitSlope = POSITIVE_INFINITY; 
        _negativeLimitSlope = NEGATIVE_INFINITY; }
      else if (0.0 > a2) {
        _positiveLimitValue = NEGATIVE_INFINITY; 
        _negativeLimitValue = NEGATIVE_INFINITY; 
        _positiveLimitSlope = NEGATIVE_INFINITY; 
        _negativeLimitSlope = POSITIVE_INFINITY; }
      else { // affine, look at a1
        _positiveLimitSlope = a1; 
        _negativeLimitSlope = a1; 
        if (0.0 < a1) {
          _positiveLimitValue = POSITIVE_INFINITY; 
          _negativeLimitValue = NEGATIVE_INFINITY; }
        else if (0.0 > a1) {
          _positiveLimitValue = NEGATIVE_INFINITY; 
          _negativeLimitValue = POSITIVE_INFINITY; }
        else { // constant
          _positiveLimitValue = a0; 
          _negativeLimitValue = a0; } } } } 

  public static final ScalarFunctional 
  make (final double a0, 
        final double a1,
        final double a2,
        final double a3) {
    if (0.0==a3) {
      if (0.0==a2) {
        if (0.0==a1) { return ConstantFunctional.make(a0); }
        return AffineFunctional.make(a0,a1); }
      return QuadraticMonomial.make(a0,a1,a2); }
    return new CubicMonomial(a0,a1,a2,a3); }

  public static final ScalarFunctional 
  interpolateXY (final double x0, final double y0, 
                 final double x1, final double y1,
                 final double x2, final double y2,
                 final double x3, final double y3) {
    final double[] a = 
      Polynomial.interpolatingMonomialCoefficients(
        x0,y0,x1,y1,x2,y2,x3,y3);
    return make(a[0],a[1],a[2],a[3]); }

  // only interpolates 4 values for now.
  public static final boolean 
  validKnots (final double[][] knots) {
    return
      validKnots(knots,3)
      && 
      (4==knots[0].length) 
      && 
      (0==knots[1].length); }

  public static final ScalarFunctional 
  interpolate (final Function f, 
               final double[][] x) {
    assert validKnots(x) : 
      Arrays.toString(x[0]) + ", " + Arrays.toString(x[1]);
    return interpolateXY(
      x[0][0],f.doubleValue(x[0][0]),
      x[0][1],f.doubleValue(x[0][1]),
      x[0][2],f.doubleValue(x[0][2]),
      x[0][3],f.doubleValue(x[0][3]));}

  public static final ScalarFunctional 
  interpolate (final Object f, 
               final Object x) {
    return interpolate((Function) f, (double[][]) x);}

  //--------------------------------------------------------------
}
//--------------------------------------------------------------

