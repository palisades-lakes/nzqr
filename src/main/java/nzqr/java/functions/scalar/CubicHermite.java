package nzqr.java.functions.scalar;

import static java.lang.Double.NEGATIVE_INFINITY;
import static java.lang.Double.NaN;
import static java.lang.Double.POSITIVE_INFINITY;
import static java.lang.Double.isFinite;
import static java.lang.Double.isNaN;
import static java.lang.Math.abs;
import static java.lang.Math.fma;
import static java.lang.Math.sqrt;

import java.util.Arrays;

import org.apache.commons.math3.fraction.BigFraction;

import nzqr.java.functions.Domain;
import nzqr.java.functions.Function;

/** A cubic function from <b>R</b> to <b>R</b> 
 * in the Hermite basis. Convenient for matching value and slope
 * at 2 points.
 *
 * @author palisades dot lakes at gmail dot com
 * @version 2018-10-08
 */

public final class CubicHermite extends Polynomial {

  //--------------------------------------------------------------
  // fields
  //--------------------------------------------------------------

  private final double _x0;
  private final double _dx10;
  private final double _y0;
  private final double _y1;
  private final double _d0;
  private final double _d1;

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
  // hermite basis
  //--------------------------------------------------------------
  // TODO: what's the most accurate/efficient representation?
  // see wikipedia Cubic Hermite Spline

  // factored form
  //  private static final double h00 (final double t) {
  //    return (1.0+2.0*t)*(1.0-t)*(1.0-t); }
  // 
  //  private static final double h01 (final double t) {
  //    return t*t*(3.0-2.0*t); }
  //  
  //  private static final double h10 (final double t) {
  //    return t*(1.0-t)*(1.0-t); }
  //  
  //  private static final double h11 (final double t) {
  //    return t*t*(t-1.0); }

  // monomial form using fma/Horner for evaluation
  private static final double h00 (final double t) {
    return fma(fma(2.0,t,-3),t*t,1.0); }

  private static final double h01 (final double t) {
    return t*t*fma(-2.0,t,3.0); }

  private static final double h10 (final double t) {
    return t*fma(t,t-2.0,1); }

  private static final double h11 (final double t) {
    return t*fma(t,t,-t); }

  private static final double dh00 (final double t) {
    return 6.0*fma(t,t,-t); }

  private static final double dh01 (final double t) {
    return -6.0*fma(t,t,-t); }

  private static final double dh10 (final double t) {
    return fma(t,fma(3.0,t,-4.0),1); }

  private static final double dh11 (final double t) {
    return t*fma(3.0,t,-2.0); }

  //--------------------------------------------------------------
  // Function methods
  //--------------------------------------------------------------

  @Override
  public final double doubleValue (final double x) {

    final double t = (x-_x0)/_dx10;
    if (isFinite(x)) {
      //      return 
      //        _y0*h00(t) +
      //        _y1*h01(t) +
      //        _d0*_dx10*h10(t) +
      //        _d1*_dx10*h11(t); }
      return 
        fma(_y0,h00(t),
          fma(_y1,h01(t),
            _dx10*fma(_d0,h10(t),
              _d1*h11(t)))); }
    if (isNaN(x)) { return NaN; }
    if (POSITIVE_INFINITY == x) { return _positiveLimitValue; }
    return _negativeLimitValue; }

  @Override
  public final double slope (final double x) {
    if (isFinite(x)) {
      final double t = (x-_x0)/_dx10;
      return 
        _y0*dh00(t)/_dx10 +
        _y1*dh01(t)/_dx10 +
        _d0*dh10(t) +
        _d1*dh11(t); }
    if (isNaN(x)) { return NaN; }
    if (POSITIVE_INFINITY == x) { return _positiveLimitSlope; }
    return _negativeLimitSlope; }

  //  @Override
  //  public final double doubleArgmin (final Domain support) { 
  //    final Interval bounds = (Interval) support;
  //    if (bounds.contains(_xmin)) { return _xmin; }
  //    final double x0 = bounds.lower();
  //    final double x1 = nextUp(bounds.upper());
  //    // note: if xmin == xupper and it's a strict local minimum, 
  //    // that will be returned.
  //    final double y0 = doubleValue(x0);
  //    final double y1 = doubleValue(x1);
  //    if (y0 <= y1) { return x0; }
  //    return x1; }

  @Override
  public final double doubleArgmin (final Domain support) { 
    if (support.contains(_xmin)) { return _xmin; }
    return NaN; }

  //--------------------------------------------------------------
  // Object methods
  //--------------------------------------------------------------

  @Override
  public final String toString () {
    return
      getClass().getSimpleName() + "[" + 
      _x0 + "," + _y0 + "," + _d0 + ";" +
      (_dx10+_x0) + "," + _y1 + "," + _d1 + ";" +
      _xmin + "]"; }

  //--------------------------------------------------------------
  // construction
  //--------------------------------------------------------------
  // monomial coefficients for y = a0 + a1*u + a2+u^2 + a3*u^3,
  // where u = (x-x0)/(x1-x0)
  // Use BigFraction to accurately detect quadratic, affine, 
  // constant cases...

  @SuppressWarnings("unused")
  private static final double a0 (final double x0,
                                  final double y0,
                                  final double d0,
                                  final double x1,
                                  final double y1,
                                  final double d1) {
    return y0; }

  @SuppressWarnings("unused")
  private static final double a1 (final double x0,
                                  final double y0,
                                  final double d0,
                                  final double x1,
                                  final double y1,
                                  final double d1) {
    final BigFraction dx = 
      new BigFraction(x1).subtract(new BigFraction(x0));
    return new BigFraction(d0).multiply(dx).doubleValue(); }

  private static final double a2 (final double x0,
                                  final double y0,
                                  final double d0,
                                  final double x1,
                                  final double y1,
                                  final double d1) {
    final BigFraction dx = 
      new BigFraction(x1).subtract(new BigFraction(x0));
    final BigFraction dy = 
      new BigFraction(y1).subtract(new BigFraction(y0));
    final BigFraction dd = 
      new BigFraction(d0).multiply(2).add(new BigFraction(d1));
    return dy.multiply(3).subtract(dd.multiply(dx)).doubleValue(); }

  private static final double a3 (final double x0,
                                  final double y0,
                                  final double d0,
                                  final double x1,
                                  final double y1,
                                  final double d1) {
    final BigFraction dx = 
      new BigFraction(x1).subtract(new BigFraction(x0)).reduce();
    //System.out.println("dx=" + dx);
    final BigFraction dy = 
      new BigFraction(y1).subtract(new BigFraction(y0)).reduce();
    //System.out.println("dy=" + dy);
    //System.out.println("dy/dx=" + dy.divide(dx).reduce());
    final BigFraction dd = 
      new BigFraction(d0).add(new BigFraction(d1)).reduce();
    //System.out.println("dd/2="+dd.divide(2).reduce());
    //System.out.println("dd="+dd);
    //System.out.println("2dy/dx="+dy.multiply(2).divide(dx).reduce());
    //System.out.println("dd*dx="+dd.multiply(dx).reduce());
    //System.out.println("2dy="+dy.multiply(2).reduce());
    return dy.multiply(-2).add(dd.multiply(dx)).doubleValue(); }

  private static final double argmin (final double x0,
                                      final double y0,
                                      final double d0,
                                      final double x1,
                                      final double y1,
                                      final double d1) {
    final double dx = x1-x0;
    final double v = (d0+d1) - ((3.0*(y1-y0)) / dx);
    final double t = (v*v) - (d0*d1);
    // complex roots, no critical points
    // could return global min at +/- infinity.
    if (t < 0.0) { 
      final double a3 = a3(x0,y0,d0,x1,y1,d1);
      if (0.0 > a3) { return POSITIVE_INFINITY; }
      return NEGATIVE_INFINITY; }

    final double w0 = sqrt(t);
    final double w1 = (d0+v)-w0;
    final double w2 = (d1+v)+w0;
    if ((w1 == 0.0) && (w2 == 0.0)) { return NaN; }
    if (abs(w1) >= abs(w2)) { return  x0 + ((dx*d0) / w1); }
    return x1 - ((dx*d1)/w2); } 

  private CubicHermite (final double x0, 
                        final double y0,
                        final double d0,
                        final double x1, 
                        final double y1,
                        final double d1) {
    assert x0 != x1  : "Fail: " + x0 + " == " + x1 ;
    //    System.out.println("CH[" + 
    //      a0(x0,y0,d0,x1,y1,d1) + " + " + 
    //      a1(x0,y0,d0,x1,y1,d1) + "*(x-" + x0 + ") + " +
    //      a2(x0,y0,d0,x1,y1,d1) + "*(x-" + x0 + ")^2 + "+ 
    //      a3(x0,y0,d0,x1,y1,d1) + "*(x-" + x0 + ")^3]");

    _x0 = x0;
    _dx10 = x1-x0;
    _y0 = y0;
    _y1 = y1;
    _d0 = d0;
    _d1 = d1;
    // limiting values:
    // sign of limit depends on monomial 3
    if (0.0 < a3(x0,y0,d0,x1,y1,d1)) {
      _xmin = argmin(x0,y0,d0,x1,y1,d1); 
      _positiveLimitValue = POSITIVE_INFINITY; 
      _negativeLimitValue = NEGATIVE_INFINITY; 
      _positiveLimitSlope = POSITIVE_INFINITY; 
      _negativeLimitSlope = NEGATIVE_INFINITY; }
    else if (0.0 > a3(x0,y0,d0,x1,y1,d1)) {  
      _xmin = argmin(x0,y0,d0,x1,y1,d1); 
      _positiveLimitValue = NEGATIVE_INFINITY; 
      _negativeLimitValue = POSITIVE_INFINITY; 
      _positiveLimitSlope = NEGATIVE_INFINITY; 
      _negativeLimitSlope = POSITIVE_INFINITY; }
    else // quadratic, so look at a2:
      if (0.0 < a2(x0,y0,d0,x1,y1,d1)) {
        _xmin = argmin(x0,y0,d0,x1,y1,d1); 
        _positiveLimitValue = POSITIVE_INFINITY; 
        _negativeLimitValue = POSITIVE_INFINITY; 
        _positiveLimitSlope = POSITIVE_INFINITY; 
        _negativeLimitSlope = NEGATIVE_INFINITY; }
      else if (0.0 > a2(x0,y0,d0,x1,y1,d1)) {
        _xmin = POSITIVE_INFINITY;
        _positiveLimitValue = NEGATIVE_INFINITY; 
        _negativeLimitValue = NEGATIVE_INFINITY; 
        _positiveLimitSlope = NEGATIVE_INFINITY; 
        _negativeLimitSlope = POSITIVE_INFINITY; }
      else { // affine, look at a1
        if (0.0 < d0) {
          _xmin = NEGATIVE_INFINITY;
          _positiveLimitValue = POSITIVE_INFINITY; 
          _negativeLimitValue = NEGATIVE_INFINITY; 
          _positiveLimitSlope = d0; 
          _negativeLimitSlope = d0; }
        else if (0.0 > d0) {
          _xmin = POSITIVE_INFINITY;
          _positiveLimitValue = NEGATIVE_INFINITY; 
          _negativeLimitValue = POSITIVE_INFINITY; 
          _positiveLimitSlope = d0; 
          _negativeLimitSlope = d0; }
        else {
          _xmin = NaN;
          _positiveLimitValue = y0; 
          _negativeLimitValue = y0; 
          _positiveLimitSlope = 0.0; 
          _negativeLimitSlope = 0.0; } } } 

  public static final ScalarFunctional 
  interpolateXYD (final double x0, final double y0, final double d0,
                  final double x1, final double y1, final double d1) {
    // TODO: sorting not necessary?
    //if (x0 < x1) {
    if ((y0==y1) && (0.0==d0) && (0.0==d1)) {
      return ConstantFunctional.make(y0); }
    return new CubicHermite(x0,y0,d0,x1,y1,d1); }
  //return new CubicHermite(x1,y1,d1,x0,y0,d0); }

  public static final ScalarFunctional 
  interpolateXYD (final Function f, 
                  final double[] x) {
    return interpolateXYD(
      x[0],f.doubleValue(x[0]),f.slope(x[0]),
      x[1],f.doubleValue(x[1]),f.slope(x[1])); }

  // only interpolates 3 values for now.
  public static final boolean 
  validKnots (final double[][] knots) {
    return 
      validKnots(knots,3)
      && 
      (2==knots[0].length) 
      && 
      (2==knots[1].length)
      &&
      (knots[0][0]==knots[1][0])
      &&
      (knots[0][1]==knots[1][1]); }

  public static final ScalarFunctional 
  interpolate (final Function f, 
               final double[][] x) {
    assert validKnots(x) : 
      Arrays.toString(x[0]) + ", " + Arrays.toString(x[1]);
    return interpolateXYD(
      x[0][0],f.doubleValue(x[0][0]),f.slope(x[0][0]),
      x[0][1],f.doubleValue(x[0][1]),f.slope(x[0][1]));}

  public static final ScalarFunctional 
  interpolate (final Object f, 
               final Object x) {
    return interpolate((Function) f, (double[][]) x);}

  //--------------------------------------------------------------
}
//--------------------------------------------------------------

