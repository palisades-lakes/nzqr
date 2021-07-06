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

public final class QuadraticNewton extends Polynomial {

  //--------------------------------------------------------------
  // fields
  //--------------------------------------------------------------

  private final double _x0;
  private final double _x1;

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
      return 
        fma(dx0,
          fma(dx1,
            _b2,
            _b1),
          _b0); }

    if (isNaN(x)) { return NaN; }
    if (POSITIVE_INFINITY == x) { return _positiveLimitValue; }
    return _negativeLimitValue; }

  @Override
  public final double slope (final double x) {
    if (isFinite(x)) {
      //      return fma(_b2,fma(2.0,x,-(_x0+_x1)),_b1); }
      final double dx0 = x-_x0;
      final double dx1 = x-_x1;
      return 
        _b1 +
        _b2*(dx0+dx1); }
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

  @Override
  public final String toString () {
    return
      getClass().getSimpleName() + "[" + 
      _x0 + "," + _x1 + ";" +
      _b0 + "," + _b1 + "," + _b2 + ";" +
      _xmin + "]"; }

  //--------------------------------------------------------------
  // construction
  //--------------------------------------------------------------

  private QuadraticNewton (final double x0, final double y0,
                           final double x1, final double y1,
                           final double x2, final double y2) {
    assert x0 != x1;
    assert x1 != x2;
    assert x2 != x0;
    _x0 = x0;
    _x1 = x1;
    _b0 = y0;
    _b1 = (y1-y0)/(x1-x0);
    final double x20 = x2-x0;
    final double x21 = x2-x1;
    final double y20 = y2-y0;
    _b2 = (y20 - (_b1*x20))/(x20*x21);

    final double[] a = 
      Polynomial.interpolatingMonomialCoefficients(
        x0,y0,x1,y1,x2,y2);
    if (0.0 < a[2]) {
      _xmin = 0.5*(x0+x1-(_b1/_b2)); 
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

  //--------------------------------------------------------------

  // TODO: experiment with reordering x0,x1,x2
  // currrent form drops x2; better to retain xmin,xmax and drop 
  // inner sample point?
  public static final ScalarFunctional 
  interpolateXY (final double x0, final double y0,
                 final double x1, final double y1,
                 final double x2, final double y2) {

    if ((y0==y1) && (y1==y2)) {
      return ConstantFunctional.make(y0); }

    assert (x0 != x1) && (x1 != x2) && (x2 != x0);

    return new QuadraticNewton(x0,y0,x1,y1,x2,y2); }

  public static final ScalarFunctional 
  interpolateXY (final Function f, 
                 final double x0, 
                 final double x1, 
                 final double x2) {
    return interpolateXY(
      x0,f.doubleValue(x0),
      x1,f.doubleValue(x1),
      x2,f.doubleValue(x2));}

  public static final ScalarFunctional 
  interpolateXY (final Function f, 
                 final double[] x) {
    return interpolateXY(
      x[0],f.doubleValue(x[0]),
      x[1],f.doubleValue(x[1]),
      x[2],f.doubleValue(x[2]));}

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
    assert validKnots(x,2) : 
      Arrays.toString(x[0]) + ", " + Arrays.toString(x[1]);
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

