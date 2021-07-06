package nzqr.java.test.functions;

import static java.lang.Math.PI;
import static java.lang.Math.atan2;
import static java.lang.Math.sqrt;

import nzqr.java.functions.AffineFunctional;
import nzqr.java.functions.Dn;
import nzqr.java.functions.Domain;
import nzqr.java.functions.Function;
import nzqr.java.functions.Functional;
import nzqr.java.functions.Vektor;

//==========================================================
/** A common cost function used to test optimization code.<br>
 * <p>
 * See Dennis-Schnabel, Appendix B.
 * <p>
 * Note: immutable.
 * <p>
 * TODO: automatic gradient check.
 * TODO: add translation to test other optima
 *
 * @author palisades dot lakes at gmail dot com
 * @version 2018-09-07
 */

//strictfp 
public final class HelicalValleyFn  extends Functional {

  public static final int DIMENSION = 3;
  private static final Domain DOMAIN = Dn.get(DIMENSION);

  //--------------------------------------------------------------
  // methods
  //--------------------------------------------------------------
  /** Return the minimizer for the {@link HelicalValleyFn} test
   * function.
   */

  public static final double[] trueMinimizer () {
    return new double[] { 1, 0, 0, }; }

  //--------------------------------------------------------------
  /** Return a standard starting point for the
   * {@link HelicalValleyFn} test function.
   */

  public static final double[] start () {
    return new double[] { -1, 0, 0, }; }

  //--------------------------------------------------------------

  private static final double TWOPI = 2.0*PI;

  private static final double theta (final double x0,
                                     final double x1) {
    return atan2(x1,x0)/TWOPI; }

  private static final double dtheta0 (final double x0,
                                       final double x1) {
    final double x02 = x0*x0;
    final double x12 = x1*x1;
    return -x1/ (TWOPI*(x02 + x12)); }

  private static final double dtheta1 (final double x0,
                                       final double x1) {
    final double x02 = x0*x0;
    final double x12 = x1*x1;
    return x0 / (TWOPI*(x02 + x12)); }

  //--------------------------------------------------------------

  private static final double f0 (final double[] x) {
    assert DIMENSION == x.length;
    return 10.0*(x[2] - (10.0*theta(x[0],x[1]))); }

  private static final double df00 (final double[] x) {
    assert DIMENSION == x.length;
    return -100.0*dtheta0(x[0],x[1]); }

  private static final double df01 (final double[] x) {
    assert DIMENSION == x.length;
    return -100.0*dtheta1(x[0],x[1]); }

  //  private static final double df02 (final double[] x) {
  //    assert DIMENSION == x.length;
  //    return 10.0; }

  //--------------------------------------------------------------

  private static final double f1 (final double[] x) {
    assert DIMENSION == x.length;
    final double x0 = x[0];
    final double x1 = x[1];
    return 10.0*(sqrt((x0*x0) + (x1*x1)) - 1.0); }

  private static final double df10 (final double[] x) {
    assert DIMENSION == x.length;
    final double x0 = x[0];
    final double x1 = x[1];
    return (10.0*x0)/sqrt((x0*x0) + (x1*x1)); }

  private static final double df11 (final double[] x) {
    assert DIMENSION == x.length;
    final double x0 = x[0];
    final double x1 = x[1];
    return (10.0*x1)/sqrt((x0*x0) + (x1*x1)); }


  //--------------------------------------------------------------

  private static final double f2 (final double[] x) {
    assert DIMENSION == x.length;
    return x[2]; }

  //--------------------------------------------------------------
  // Function methods
  //--------------------------------------------------------------

  @Override
  public final Domain domain () { return DOMAIN; }

  //--------------------------------------------------------------

  @Override
  public final double doubleValue (final Vektor x) {
    assert DIMENSION == x.dimension();
    final double[] xx = x.unsafeCoordinates();
    final double y0 = f0(xx);
    final double y1 = f1(xx);
    final double y2 = f2(xx);
    return (y0*y0) + (y1*y1) + (y2*y2); }

  //--------------------------------------------------------------

  @Override
  public final Vektor gradient (final Vektor x) {
    assert DIMENSION == x.dimension();
    final double[] xx = x.unsafeCoordinates();
    final double[] g = new double[DIMENSION];
    final double y0 = f0(xx);
    final double y1 = f1(xx);
    final double y2 = f2(xx);
    g[0] = 2.0*((y0*df00(xx)) + (y1*df10(xx)));
    g[1] = 2.0*((y0*df01(xx)) + (y1*df11(xx)));
    g[2] = 2.0*((y0*10.0) + y2); 
    return Vektor.unsafeMake(g); }

  //--------------------------------------------------------------

  @Override
  public final Function tangentAt (final Vektor x) {
    return AffineFunctional.make(gradient(x),doubleValue(x)); }

  //--------------------------------------------------------------
  // construction
  //--------------------------------------------------------------

  private HelicalValleyFn () { super(); }

  //--------------------------------------------------------------
  /** Return a {@link HelicalValleyFn} test function.
   * Could be a singleton.
   */

  public static final HelicalValleyFn get () {
    return new HelicalValleyFn(); }

  //--------------------------------------------------------------
} // end class
//--------------------------------------------------------------

