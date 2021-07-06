package nzqr.java.functions.scalar;

import static java.lang.Double.NEGATIVE_INFINITY;
import static java.lang.Double.NaN;
import static java.lang.Double.POSITIVE_INFINITY;
import static java.lang.Double.isFinite;
import static java.lang.Double.isNaN;

import java.util.Arrays;

import nzqr.java.functions.Domain;
import nzqr.java.functions.Function;

/** An quadratic function from <b>R</b> to <b>R</b> in Lagrange 
 * form.
 *
 * @author palisades dot lakes at gmail dot com
 * @version 2018-10-08
 */

public final class CubicLagrange extends Polynomial {

  //--------------------------------------------------------------
  // fields
  //--------------------------------------------------------------

  private final double _x0;
  private final double _x1;
  private final double _x2; 
  private final double _x3; 

  private final double _b0;
  private final double _b1;
  private final double _b2;
  private final double _b3;

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

  @Override
  public final double doubleValue (final double x) {
    if (isFinite(x)) {
      final double dx0 = x-_x0;
      final double dx1 = x-_x1;
      final double dx2 = x-_x2;
      final double dx3 = x-_x3;
      return 
        (_b0*dx1*dx2*dx3) +
        (_b1*dx2*dx3*dx0) +
        (_b2*dx3*dx0*dx1) +
        (_b3*dx0*dx1*dx2); }
    //    return 
    //      fma(_b0,dx1*dx2*dx3,
    //        fma(_b1,dx2*dx0*dx3,
    //          fma(_b2,dx0*dx1*dx3,
    //            _b3*dx0*dx1*dx2))); }
    if (isNaN(x)) { return NaN; }
    if (POSITIVE_INFINITY == x) { return _positiveLimitValue; }
    return _negativeLimitValue; }

  @Override
  public final double slope (final double x) {
    if (isFinite(x)) {
      final double dx0 = x-_x0;
      final double dx1 = x-_x1;
      final double dx2 = x-_x2;
      final double dx3 = x-_x3;
      return 
        _b0*((dx1*dx2)+(dx2*dx3)+(dx3*dx1)) +
        _b1*((dx2*dx3)+(dx3*dx0)+(dx0*dx2)) +
        _b2*((dx3*dx0)+(dx0*dx1)+(dx1*dx3)) +
        _b3*((dx0*dx1)+(dx1*dx2)+(dx2*dx0)); }
    //    return 
    //      fma(_b0,(dx1*dx2)+(dx2*dx3)+(dx3*dx1),
    //        fma(_b1,(dx2*dx3)+(dx3*dx0)+(dx0*dx2),
    //          fma(_b2,(dx3*dx0)+(dx0*dx1)+(dx1*dx3),
    //            _b3*(dx0*dx1)+(dx1*dx2)+(dx2*dx0)))); }
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
      _x0 + "," + _x1 + "," + _x2 + "," + _x3 + ";" +
      _b0 + "," + _b1 + "," + _b2 + "," + _b3 + ";" +
      _xmin + "]"; }

  //--------------------------------------------------------------
  // construction
  //--------------------------------------------------------------

  private CubicLagrange (final double x0, final double y0,
                         final double x1, final double y1,
                         final double x2, final double y2,
                         final double x3, final double y3) {
    assert x0 != x1;
    assert x0 != x2;
    assert x0 != x3;
    assert x1 != x2;
    assert x1 != x3;
    assert x2 != x3;
    _x0 = x0;
    _x1 = x1;
    _x2 = x2;
    _x3 = x3;
    // TODO: move to factory or constructor
    _b0 = y0/((x0-x1)*(x0-x2)*(x0-x3));
    _b1 = y1/((x1-x2)*(x1-x3)*(x1-x0));
    _b2 = y2/((x2-x0)*(x2-x1)*(x2-x3));
    _b3 = y3/((x3-x0)*(x3-x1)*(x3-x2));

    // derivative as a2*x^2 + a1*x + a0
    final double a2 = 3.0*(_b0+_b1+_b2+_b3);
    final double a1 = 
      -2.0*(
        (_b0*(x1+x2+x3)) +
        (_b1*(x2+x3+x0)) +
        (_b2*(x3+x0+x1)) +
        (_b3*(x0+x1+x2)));
    final double a0 = 
      (_b0*((x1*x2)+(x2*x3)+(x3*x1))) +
      (_b1*((x2*x3)+(x3*x0)+(x0*x2))) +
      (_b2*((x3*x0)+(x0*x1)+(x1*x3))) +
      (_b3*((x0*x1)+(x1*x2)+(x2*x0)));
    //    final double c = 
    //      -((_b0*x1*x2*x3) +
    //        (_b1*x2*x3*x0) +
    //        (_b2*x3*x0*x1) +
    //        (_b3*x0*x1*x2));
    //      System.out.println(
    //        "CL[" + c + " + " + a0 + "*x + " + a1/2 + "*x^2 + " + a2/3 + "*x^3]");
    if (0.0 == a2) { // quadratic
      if (0.0 < a1) { 
        _xmin = -a0/a1; 
        _positiveLimitValue = POSITIVE_INFINITY; 
        _negativeLimitValue = POSITIVE_INFINITY; 
        _positiveLimitSlope = POSITIVE_INFINITY; 
        _negativeLimitSlope = NEGATIVE_INFINITY; }
      else if (0.0 > a1) { // +/- infinity both minima
        _xmin = POSITIVE_INFINITY;
        _positiveLimitValue = NEGATIVE_INFINITY; 
        _negativeLimitValue = NEGATIVE_INFINITY; 
        _positiveLimitSlope = NEGATIVE_INFINITY; 
        _negativeLimitSlope = POSITIVE_INFINITY; }
      else { // affine
        if (0.0 < a1) {
          _xmin = NEGATIVE_INFINITY;
          _positiveLimitValue = POSITIVE_INFINITY; 
          _negativeLimitValue = NEGATIVE_INFINITY; 
          _positiveLimitSlope = a0; 
          _negativeLimitSlope = a0; }
        else if (0.0 > a1) {
          _xmin = POSITIVE_INFINITY;
          _positiveLimitValue = NEGATIVE_INFINITY; 
          _negativeLimitValue = POSITIVE_INFINITY; 
          _positiveLimitSlope = a0; 
          _negativeLimitSlope = a0; }
        else { // constant
          _xmin = NaN; 
          _positiveLimitValue = y0; 
          _negativeLimitValue = y0; 
          _positiveLimitSlope = 0.0; 
          _negativeLimitSlope = 0.0; } } }
    else { // 0.0 != a, nontrivial cubic
      final double[] roots = Polynomial.roots(a0,a1,a2);
      //System.out.println(Arrays.toString(roots));
      assert 2 >= roots.length;
      if (0 == roots.length) { // no critical points
        if (0.0 < a2) { 
          _xmin = NEGATIVE_INFINITY; }
        else { // (0.0 > a2)
          _xmin = POSITIVE_INFINITY; } } 
      else if (2 == roots.length) {
        if (2.0*a2*roots[0] + a1 > 0.0) { 
          _xmin = roots[0]; }
        else { // if (2.0*a2*roots[1] + a1 > 0.0) { 
          _xmin = roots[1]; } }
      else { // 1 == roots.length;
        if (0.0 < a2) {
          _xmin = NEGATIVE_INFINITY; }
        else { // (0.0 > a2)
          _xmin = POSITIVE_INFINITY; } }
      if (0.0 < a2) {
        _positiveLimitValue = POSITIVE_INFINITY; 
        _negativeLimitValue = NEGATIVE_INFINITY; 
        _positiveLimitSlope = POSITIVE_INFINITY; 
        _negativeLimitSlope = POSITIVE_INFINITY; }
      else { // 0.0 > a2
        _positiveLimitValue = NEGATIVE_INFINITY; 
        _negativeLimitValue = POSITIVE_INFINITY; 
        _positiveLimitSlope = NEGATIVE_INFINITY; 
        _negativeLimitSlope = NEGATIVE_INFINITY; } } } 

  public static final ScalarFunctional 
  interpolateXY (final double x0, final double y0,
                 final double x1, final double y1,
                 final double x2, final double y2,
                 final double x3, final double y3) {
    if ((y0==y1) && (y1==y0) && (y2==y3)) {
      return ConstantFunctional.make(y0); }
    return new CubicLagrange(x0,y0,x1,y1,x2,y2,x3,y3); }

  public static final ScalarFunctional 
  interpolateXY (final Function f, 
                 final double x0, 
                 final double x1, 
                 final double x2, 
                 final double x3) {
    return interpolateXY(
      x0,f.doubleValue(x0),
      x1,f.doubleValue(x1),
      x2,f.doubleValue(x2),
      x3,f.doubleValue(x3));}

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

