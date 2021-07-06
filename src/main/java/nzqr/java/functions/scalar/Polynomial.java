package nzqr.java.functions.scalar;

import static java.lang.Math.fma;
import static java.lang.Math.sqrt;
import static org.apache.commons.math3.fraction.BigFraction.ZERO;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.math3.fraction.BigFraction;

/** Base for polynomials on <b>R</b>.
 *
 * @author palisades dot lakes at gmail dot com
 * @version 2018-10-10
 */

@SuppressWarnings("unchecked")
public abstract class Polynomial extends ScalarFunctional {

  //--------------------------------------------------------------
  // methods
  //--------------------------------------------------------------

  public abstract int degree ();

  //--------------------------------------------------------------
  /** Check if an arbitrary precision quadratic polynomial 
   * over the rationals ({@link BigFraction}) can match the given 
   * value and slope at <code>x0</code> and <code>x1</code>.
   */
  public static final boolean 
  isQQuadraticXYD2 (final BigFraction x0,
                    final BigFraction y0,
                    final BigFraction d0,
                    final BigFraction x1,
                    final BigFraction y1,
                    final BigFraction d1) {
    return 
      y1.subtract(y0)
      .multiply(2)
      .equals(
        d1.subtract(d0)
        .multiply(
          x1.subtract(x0))); }

  /** Check if an arbitrary precision quadratic polynomial 
   * over the rationals ({@link BigFraction}) can match the given 
   * value and slope at <code>x0</code> and <code>x1</code>.
   */

  public static final boolean 
  isQQuadraticXYD2 (final double x0,
                    final double y0,
                    final double d0,
                    final double x1,
                    final double y1,
                    final double d1) {
    return 
      isQQuadraticXYD2(
        new BigFraction(x0),
        new BigFraction(y0),
        new BigFraction(d0),
        new BigFraction(x1),
        new BigFraction(y1),
        new BigFraction(d1)); }

  //--------------------------------------------------------------

  public static final double[] 
    interpolatingMonomialCoefficients (final double x0, 
                                       final double y0,
                                       final double x1, 
                                       final double y1,
                                       final double x2, 
                                       final double y2,
                                       final double x3, 
                                       final double y3) {
    final BigFraction qx0 = new BigFraction(x0);
    final BigFraction qx1 = new BigFraction(x1);
    final BigFraction qx2 = new BigFraction(x2);
    final BigFraction qx3 = new BigFraction(x3);
    final BigFraction x01 = qx0.subtract(qx1);
    final BigFraction x02 = qx0.subtract(qx2);
    final BigFraction x03 = qx0.subtract(qx3);
    final BigFraction l0 = new BigFraction(y0)
      .divide(x01.multiply(x02).multiply(x03));
    final BigFraction x12 = qx1.subtract(qx2);
    final BigFraction x13 = qx1.subtract(qx3);
    final BigFraction l1 = new BigFraction(y1).negate()
      .divide(x01.multiply(x12).multiply(x13));
    final BigFraction x23 = qx2.subtract(qx3);
    final BigFraction l2 = new BigFraction(y2)
      .divide(x23.multiply(x12).multiply(x02));
    final BigFraction l3 = new BigFraction(y3).negate()
      .divide(x23.multiply(x03).multiply(x13));

    final double[] a = new double[4];
    a[3] = l0.add(l1).add(l2).add(l3).doubleValue();
    a[2] = 
      (l0.multiply(qx1.add(qx2).add(qx3)))
      .add
      (l1.multiply(qx2.add(qx3).add(qx0)))
      .add
      (l2.multiply(qx3.add(qx0).add(qx1)))
      .add
      (l3.multiply(qx0.add(qx1).add(qx2)))
      .negate().doubleValue();
    a[1] =
      (l0.multiply(qx1.multiply(qx2).add(qx2.multiply(qx3)).add(qx3.multiply(qx1)))) 
      .add
      (l1.multiply(qx2.multiply(qx3).add(qx3.multiply(qx0)).add(qx0.multiply(qx2)))) 
      .add
      (l2.multiply(qx3.multiply(qx0).add(qx0.multiply(qx1)).add(qx1.multiply(qx3)))) 
      .add
      (l3.multiply(qx0.multiply(qx1).add(qx1.multiply(qx2)).add(qx2.multiply(qx0)))) 
      .doubleValue();
    a[0] =
      (l0.multiply(qx1).multiply(qx2).multiply(qx3)) 
      .add
      (l1.multiply(qx2).multiply(qx3).multiply(qx0)) 
      .add
      (l2.multiply(qx3).multiply(qx0).multiply(qx1))
      .add
      (l3.multiply(qx0).multiply(qx1).multiply(qx2))
      .negate().doubleValue();
    return a; }

  /** Return the monomial coefficients for a parabola that 
   * interpolates <code>(x0,y0), (x1,y1), x2,y2)</code>.
   * Use {@link BigFraction} internally for accuracy.
   */

  public static final double[] 
    interpolatingMonomialCoefficients (final double x0, 
                                       final double y0,
                                       final double x1, 
                                       final double y1,
                                       final double x2, 
                                       final double y2) {
    final BigFraction qx0 = new BigFraction(x0);
    final BigFraction qy0 = new BigFraction(y0);
    final BigFraction qx1 = new BigFraction(x1);
    final BigFraction qy1 = new BigFraction(y1);
    final BigFraction qx2 = new BigFraction(x2);
    final BigFraction qy2 = new BigFraction(y2);
    final BigFraction dx01 = qx0.subtract(qx1);
    final BigFraction dx12 = qx1.subtract(qx2);
    final BigFraction dx20 = qx2.subtract(qx0);
    final BigFraction dx0120 = dx01.multiply(dx20);
    final BigFraction dx1201 = dx12.multiply(dx01);
    final BigFraction dx2012 = dx20.multiply(dx12);
    final BigFraction a00 = qy0.multiply(qx1).multiply(qx2)
      .divide(dx0120);
    final BigFraction a01 = qy1.multiply(qx2).multiply(qx0)
      .divide(dx1201);
    final BigFraction a02 = qy2.multiply(qx0).multiply(qx1)
      .divide(dx2012);
    final BigFraction a10 = qy0.multiply(qx1.add(qx2))
      .divide(dx0120);
    final BigFraction a11 = qy1.multiply(qx2.add(qx0))
      .divide(dx1201);
    final BigFraction a12 = qy2.multiply(qx0.add(qx1))
      .divide(dx2012);
    final BigFraction a20 = qy0.divide(dx0120);
    final BigFraction a21 = qy1.divide(dx1201);
    final BigFraction a22 = qy2.divide(dx2012);

    return new double[] 
      { a00.add(a01).add(a02).negate().doubleValue(), 
        a10.add(a11).add(a12).doubleValue(),
        a20.add(a21).add(a22).negate().doubleValue(), }; }

  //--------------------------------------------------------------
  /** return a <code>double[]</code> containing the real zeros
   * of <code>a0+a1*x+a2*x^2</code>.
   * If there is onyl 1, return an array of length 1.
   * If there are no real roots, return an empty array.
   */

  public static final double[] roots (final double a0,
                                      final double a1,
                                      final double a2) {
    final double b2m4ac = (a1*a1) - (4.0*a0*a2);
    if (b2m4ac < 0.0) { return new double[0]; }
    if (b2m4ac == 0.0) { return new double[] { -0.5*a1/a2, }; }
    final double sqrtb2m4ac = sqrt(b2m4ac);  
    // one step improvement
    final double r0 = 0.5*(-a1 - sqrtb2m4ac)/a2;
    final double r1 = 0.5*(-a1 + sqrtb2m4ac)/a2;
    return improveRoots(a0,a1,a2,new double[] { r0, r1 }); }

  //--------------------------------------------------------------
  /** do a couple newton steps for more accuracy in quadratic 
   * roots. modifies <code>roots</code>!!!
   */

  public static final double[] improveRoots (final double a0,
                                             final double a1,
                                             final double a2,
                                             final double[] r) {
    assert 2 >= r.length;
    if (2 == r.length) { 
      r[0] = -fma(a2,r[0]*r[0],a0)/a1;
      //      r[0] = -fma(a2,r[0]*r[0],a0)/a1;
      //      r[0] = -fma(a2,r[0]*r[0],a0)/a1;
      r[1] = -fma(a2,r[1]*r[1],a0)/a1;
      //      r[1] = -fma(a2,r[1]*r[1],a0)/a1;
      //      r[1] = -fma(a2,r[1]*r[1],a0)/a1; 
    }
    return r; }

  //--------------------------------------------------------------
  /** Return a <code>BigFraction[]</code> containing the real 
   * zeros of <code>a0+a1*x+a2*x^2</code>.
   * If there is onyl 1, return an array of length 1.
   * If there are no real roots, return an empty array.
   * <p>
   * Calculations are exact except for square root, which rounds 
   * to <code>double</code> twice. Could implement an arbitrary
   * precision rational sqrt to get enough accuracy so that the 
   * final result is the exact answer rounded to 
   * <code>double</code>.
   */

  public static final BigFraction[] roots (final BigFraction a0,
                                           final BigFraction a1,
                                           final BigFraction a2) {
    final BigFraction b2m4ac = 
      a1.multiply(a1).subtract(a0.multiply(a2).multiply(4));
    final int sgn = b2m4ac.compareTo(ZERO);
    if (sgn < 0) { return new BigFraction[0]; }
    final BigFraction m2a2 = a2.multiply(-2);
    if (sgn == 0) { 
      return new BigFraction[] { a1.divide(m2a2), }; }
    final BigFraction sqrtb2m4ac = 
      new BigFraction(sqrt(b2m4ac.doubleValue())); 

    return new BigFraction [] 
      { a1.add(sqrtb2m4ac).divide(m2a2),
        a1.subtract(sqrtb2m4ac).divide(m2a2), }; }

  // TODO: move elsewhere
  private static final int countDistinct (final double[] x) {
    final Set unique = new HashSet();
    for (final double xi : x) {
      unique.add(Double.valueOf(xi)); } 
    return unique.size(); }

  public static final boolean validKnots (final double[][] knots,
                                          final int degree) {
    final int nv = countDistinct(knots[0]);
    final int ns = countDistinct(knots[1]);
    //System.out.println(nv + "," + ns);
    return 
      (nv>=1) 
      &&
      ((nv+ns) == degree+1); }

  //  public final boolean validKnots (final double[] x,
  //                                   final KnotType[] flags) {
  //    final Set vKnots = KnotType.distinctValueKnots(x,flags);
  //    final int nv = vKnots.size();
  //    final Set sKnots = KnotType.distinctSlopeKnots(x,flags);
  //    final int ns = sKnots.size();
  //    return 
  //      (nv>=1) 
  //      &&
  //      ((nv+ns) == degree()+1); }


  //--------------------------------------------------------------
  // construction
  //--------------------------------------------------------------

  public Polynomial () { }

  //--------------------------------------------------------------
}
//--------------------------------------------------------------

