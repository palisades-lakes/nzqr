package nzqr.java.functions.scalar;

import static java.lang.Double.NEGATIVE_INFINITY;
import static java.lang.Double.NaN;
import static java.lang.Double.POSITIVE_INFINITY;
import static java.lang.Double.isFinite;
import static java.lang.Double.isNaN;
import static java.lang.Math.fma;
import static java.lang.Math.max;
import static java.lang.Math.min;

import java.util.Arrays;

import org.apache.commons.math3.fraction.BigFraction;

import nzqr.java.functions.Domain;
import nzqr.java.functions.Function;

/** A quadratic function from <b>R</b> to <b>R</b> in monomial 
 * form composed implicitly with standardizing affine functions
 * that map a domain interval to [0,1] and [0,1] codomain values
 * to a given codomain interval.
 * 
 * @author palisades dot lakes at gmail dot com
 * @version 2018-10-08
 */

public final class QuadraticMonomialStandardized 
extends Polynomial {

  //--------------------------------------------------------------
  // fields
  //--------------------------------------------------------------
  // returned value is ay*f(ax*x+bx)+by where
  // f(u) = a0 + a1*u + a2*u^2

  // TODO: compare to composition with standardizing transforms?


  private final double _a0;
  private final double _a1;
  private final double _a2;

  private final double _ax;
  private final double _bx;

  private final double _by;
  private final double _ay;

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
    final double u = fma(_ax,x,_bx);
    if (isFinite(u)) {
      return fma(_ay,fma(u,fma(u,_a2,_a1),_a0),_by);  }
    if (isNaN(u)) { return NaN; }
    if (POSITIVE_INFINITY == u) { return _positiveLimitValue; }
    return _negativeLimitValue; }

  @Override
  public final double slope (final double x) {
    final double u = fma(_ax,x,_bx);
    if (isFinite(u)) {
      return _ax*_ay*fma(2.0*_a2,u,_a1);  }
    if (isNaN(u)) { return NaN; }
    if (POSITIVE_INFINITY == u) { return _positiveLimitSlope; }
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
      "QMST[" + 
      "u= "+ _ax + "*x + " + _bx + "; " + 
      "v= " + _a0 + " + " +
      _a1 + "*u + " +
      _a2 + "*u^2; " +
      "y= " + _ay + "*v + " + _by + "; " +
      _xmin + "]"; }

  //--------------------------------------------------------------
  // construction
  //--------------------------------------------------------------

  private QuadraticMonomialStandardized (final double a0, 
                                         final double a1,
                                         final double a2, 
                                         final double ax,
                                         final double bx,
                                         final double ay,
                                         final double by) {
    _a0 = a0;
    _a1 = a1;
    _a2 = a2;
    _bx = bx;
    _ax = ax;
    _by = by; 
    _ay = ay;
    if (0.0 < a2*ay) {
      _xmin = fma(1.0/ax,-0.5*a1/a2,-bx/ax);
      _positiveLimitValue = POSITIVE_INFINITY; 
      _negativeLimitValue = POSITIVE_INFINITY; 
      _positiveLimitSlope = POSITIVE_INFINITY; 
      _negativeLimitSlope = NEGATIVE_INFINITY; }
    else if (0.0 > a2*ay) {
      _xmin = POSITIVE_INFINITY;
      _positiveLimitValue = NEGATIVE_INFINITY; 
      _negativeLimitValue = NEGATIVE_INFINITY; 
      _positiveLimitSlope = NEGATIVE_INFINITY; 
      _negativeLimitSlope = POSITIVE_INFINITY; }
    else {// affine, look at a1
      if (0.0 < a1*ax*ay) {
        _xmin = NEGATIVE_INFINITY;
        _positiveLimitValue = POSITIVE_INFINITY; 
        _negativeLimitValue = NEGATIVE_INFINITY; 
        _positiveLimitSlope = a1; 
        _negativeLimitSlope = a1; }
      else if (0.0 > a1*ax*ay) {
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

  public static ScalarFunctional 
  make (final double a0, 
        final double a1,
        final double a2, 
        final double ax,
        final double bx,
        final double ay,
        final double by) {
    if (0.0==a2) {
      if (0.0==a1) { 
        return ConstantFunctional.make(fma(ay,a0,by)); }
      final double c0 = fma(ay,fma(a1,bx,a0),by);
      final double c1 = a1*ax*ay;
      return AffineFunctional.make(c0,c1); }
    return 
      new QuadraticMonomialStandardized(a0,a1,a2,ax,bx,ay,by); }

  //--------------------------------------------------------------

  private static final double qfma (final BigFraction a,
                                    final double z,
                                    final BigFraction b) {
    return a.multiply(new BigFraction(z)).add(b).doubleValue(); }

  public final static ScalarFunctional
  interpolateXY (final double x0, 
                 final double y0,
                 final double x1, 
                 final double y1,
                 final double x2,
                 final double y2) {

    // TODO: better to use centered (-1,1) standardization?

    // transform x range to (0,1)
    // final double xmin = min(x0,min(x1,x2));
    // final double xmax = max(x0,max(x1,x2));
    // final double ax = xmax-xmin);
    // final double bx = -ax*xmin;

    final BigFraction xmin = new BigFraction(min(x0,min(x1,x2)));
    final BigFraction xmax = new BigFraction(max(x0,max(x1,x2)));
    final BigFraction ax = xmax.subtract(xmin).reciprocal();
    final BigFraction bx = ax.negate().multiply(xmin);

    // transform y range to (0,1)
    // final double ymin = min(y0,min(y1,y2));
    // final double ymax = max(y0,max(y1,y2));
    // final double ay = 1.0/(ymax-ymin);
    // final double by = -ay*ymin;

    final BigFraction ymin = new BigFraction(min(y0,min(y1,y2)));
    final BigFraction ymax = new BigFraction(max(y0,max(y1,y2)));
    final BigFraction ay;
    final BigFraction by;
    if (ymin.equals(ymax) ) {
      ay = BigFraction.ZERO;
      by = ymin; }
    else {
      ay = ymax.subtract(ymin).reciprocal();
      by = ay.negate().multiply(ymin); }

    // monomial coefficients for interpolating points 
    // (0,1) -> (0,1)
    final double[] a = 
      Polynomial.interpolatingMonomialCoefficients(
        qfma(ax,x0,bx),qfma(ay,y0,by),
        qfma(ax,x1,bx),qfma(ay,y1,by),
        qfma(ax,x2,bx),qfma(ay,y2,by));

    // need y transform from (0,1) to y range
    return make(
      a[0],a[1],a[2],
      ax.doubleValue(),bx.doubleValue(),
      ymax.subtract(ymin).doubleValue(),ymin.doubleValue()); }

  //--------------------------------------------------------------

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

