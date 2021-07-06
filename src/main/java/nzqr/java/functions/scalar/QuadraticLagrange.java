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

/** An quadratic function from <b>R</b> to <b>R</b> in Lagrange 
 * form.
 *
 * @author palisades dot lakes at gmail dot com
 * @version 2018-10-08
 */

public final class QuadraticLagrange extends Polynomial {

  //--------------------------------------------------------------
  // fields
  //--------------------------------------------------------------

  private final double _x0;
  private final double _x1;
  private final double _x2; 

  private final double _b0;
  private final double _b1;
  private final double _b2;

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

  @Override
  public final double doubleValue (final double x) {
    if (isFinite(x)) {
      final double dx0 = x-_x0;
      final double dx1 = x-_x1;
      final double dx2 = x-_x2;
      return fma(_b0,dx1*dx2,fma(_b1,dx2*dx0,_b2*dx0*dx1)); }
    if (isNaN(x)) { return NaN; }
    if (POSITIVE_INFINITY == x) { return _positiveLimitValue; }
    return _negativeLimitValue; }

  @Override
  public final double slope (final double x) {
    if (isFinite(x)) {
      final double dx0 = x-_x0;
      final double dx1 = x-_x1;
      final double dx2 = x-_x2;
      return fma(_b0,dx1+dx2,fma(_b1,dx2+dx0,_b2*(dx0+dx1))); }
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

  @Override
  public final String toString () {
    return
      getClass().getSimpleName() + "[" + 
      _x0 + "," + _x1 + "," + _x2 + ";" +
      _b0 + "," + _b1 + "," + _b2 + ";" +
      _xmin + "]"; }

  //--------------------------------------------------------------
  // construction
  //--------------------------------------------------------------

  private static final double argmin (final double x0, 
                                      final double y0,
                                      final double x1, 
                                      final double y1,
                                      final double x2, 
                                      final double y2) {
    final double c0 = (x1-x2)*(y0-y1);
    final double c1 = (x0-x1)*(y1-y2);
    final double numer = ((x1+x0)*c1)-((x1+x2)*c0); 
    return numer/(2.0*(c1-c0)); }

  //--------------------------------------------------------------

  private QuadraticLagrange (final double x0, final double y0,
                             final double x1, final double y1,
                             final double x2, final double y2) {
    assert x0 != x1;
    assert x1 != x2;
    assert x2 != x0;
    _x0 = x0;
    _x1 = x1;
    _x2 = x2;
    _b0 = y0/((x0-x1)*(x0-x2));
    _b1 = y1/((x1-x2)*(x1-x0));
    _b2 = y2/((x2-x0)*(x2-x1));

    // TODO: accurate 1st and 2nd derivative sign calculation?
    final double[] a = Polynomial
      .interpolatingMonomialCoefficients(x0,y0,x1,y1,x2,y2);
    //    System.out.println(
    //      "QL[" + a[0] + " + " + a[1] + "*x + " + a[2] + "*x^2]");
    if (0.0 < a[2]) {
      _xmin = argmin(x0,y0,x1,y1,x2,y2);
      _positiveLimitValue = POSITIVE_INFINITY; 
      _negativeLimitValue = POSITIVE_INFINITY; 
      _positiveLimitSlope = POSITIVE_INFINITY; 
      _negativeLimitSlope = NEGATIVE_INFINITY; }
    else if (0.0 > a[2]) {
      _xmin = POSITIVE_INFINITY;
      _positiveLimitValue = NEGATIVE_INFINITY; 
      _negativeLimitValue = NEGATIVE_INFINITY; 
      _positiveLimitSlope = NEGATIVE_INFINITY; 
      _negativeLimitSlope = POSITIVE_INFINITY; }
    else {// affine, look at a1
      if (0.0 < a[1]) {
        _xmin = NEGATIVE_INFINITY;
        _positiveLimitValue = POSITIVE_INFINITY; 
        _negativeLimitValue = NEGATIVE_INFINITY; 
        _positiveLimitSlope = a[1]; 
        _negativeLimitSlope = a[1]; }
      else if (0.0 > a[1]) {
        _xmin = POSITIVE_INFINITY;
        _positiveLimitValue = NEGATIVE_INFINITY; 
        _negativeLimitValue = POSITIVE_INFINITY; 
        _positiveLimitSlope = a[1]; 
        _negativeLimitSlope = a[1]; }
      else { // constant
        _xmin = NaN;
        _positiveLimitValue = a[0]; 
        _negativeLimitValue = a[0]; 
        _positiveLimitSlope = 0.0; 
        _negativeLimitSlope = 0.0; } } } 

  public static final ScalarFunctional 
  interpolateXY (final double x0, final double y0,
                 final double x1, final double y1,
                 final double x2, final double y2) {
    assert (x0!=x1) && (x1!=x2) && (x2!=x0);
    if ((y0==y1) && (y1==y2)) {
      return ConstantFunctional.make(y0); }

    // TODO: not necessary to sort?
    //if (x0 < x1) {
    // if (x1 < x2) {
    return new QuadraticLagrange(x0,y0,x1,y1,x2,y2); }
  //      if (x0 < x2) {
  //        return new QuadraticLagrange(x0,y0,x2,y2,x1,y1); } 
  //      if (x2 < x0) {
  //        return new QuadraticLagrange(x2,y2,x0,y0,x1,y1); } }
  //
  //    if (x1 < x0) {
  //      if (x0 < x2) {
  //        return new QuadraticLagrange(x1,y1,x0,y0,x2,y2); }
  //      if (x1 < x2) {
  //        return new QuadraticLagrange(x1,y1,x2,y2,x0,y0); } 
  //      if (x2 < x1) {
  //        return new QuadraticLagrange(x2,y2,x1,y1,x0,y0); } }
  //
  //    throw new IllegalArgumentException(
  //      "Not distinct: " + x0 + ", " + x1 + ", " + x2); }

  public static final ScalarFunctional 
  interpolateXY (final Function f, 
                 final double x0, 
                 final double x1, 
                 final double x2) {
    return interpolateXY(
      x0,f.doubleValue(x0),
      x1,f.doubleValue(x1),
      x2,f.doubleValue(x2));}

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

