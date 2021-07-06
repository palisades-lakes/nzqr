package nzqr.java.test.functions;

import static java.lang.Math.fma;
import java.util.Arrays;

import nzqr.java.functions.AffineFunctional;
import nzqr.java.functions.Dn;
import nzqr.java.functions.Domain;
import nzqr.java.functions.Function;
import nzqr.java.functions.Functional;
import nzqr.java.functions.Vektor;

//==========================================================
/** A common cost function used to test optimization code.<br>
 * <b>Note:</b> The dimension of the domain must be even.
 * <p>
 * See Dennis-Schnabel, Appendix B.
 * <p>
 * Could re-write this as square nonlinear Map x -> _f
 * followed by l2 norm for testing product Cost functions.
 * <p>
 * Note: immutable.
 * <p>
 * TODO: add translation to test other optima
 *
 * @author palisades dot lakes at gmail dot com
 * @version 2018-09-07
 */

//strictfp 
public final class RosenbrockFn extends Functional {

  private final Domain _domain;

  //--------------------------------------------------------------
  // methods
  //--------------------------------------------------------------
  /** Return the minimizer for the {@link RosenbrockFn} test
   * function.
   */

  public final double[] trueMinimizer () {

    final double[] xmin = new double[domain().dimension()];
    Arrays.fill(xmin,1.0);
    return xmin; }

  //--------------------------------------------------------------
  /** Return a standard starting point for the
   * {@link RosenbrockFn} test function.
   */

  public final double[] start0 () {

    final double[] p = new double[domain().dimension()];
    final int n = domain().dimension()/2;
    for (int i=0;i<n;i++) {
      final int j= 2*i;
      p[j] = -1.2;
      p[j+1] = 1.0; }

    return p; }

  //--------------------------------------------------------------
  /** Return a standard starting point for the
   * {@link RosenbrockFn} test function.
   */

  public final double[] start1 () {

    final double[] p = new double[domain().dimension()];
    final int n = domain().dimension()/2;
    for (int i=0;i<n;i++) {
      final int j= 2*i;
      p[j] = 6.390;
      p[j+1] =-0.221; }

    return p; }

  //--------------------------------------------------------------
  // Function methods
  //--------------------------------------------------------------

  @Override
  public final Domain domain () { return _domain; }

  //--------------------------------------------------------------

  @Override
  public final double doubleValue (final Vektor x) {
    final int n = domain().dimension();
    assert n == x.dimension();
    final double[] xx = x.unsafeCoordinates();
    double y = 0.0;
    for (int j=0;j<n;) {
      final double x2im1 = xx[j++];
      final double x2i   = xx[j++];
      final double f2im1 = 10.0*(x2i - (x2im1*x2im1));
      final double f2i   = 1.0 - x2im1;
      y = fma(f2im1,f2im1,y);
      y = fma(f2i,f2i,y); } 
    return y; }

  //--------------------------------------------------------------

  @Override
  public final Vektor gradient (final Vektor x) {
    final int n = domain().dimension();
    assert n == x.dimension();
    final double[] xx = x.unsafeCoordinates();
    final double[] g = new double[n];
    for (int j=0;j<n;j+=2) {
      final double x2im1 = xx[j];
      final int j1 = j+1;
      final double x2i   = xx[j1];

      final double f2im1 = 10.0*(x2i - (x2im1*x2im1));
      final double f2i   = 1.0 - x2im1;

      final double df2im1_dx2im1 = -20.0*x2im1;
      g[j] = 2.0*fma(f2im1,df2im1_dx2im1,-f2i);
      g[j1] = 20.0*f2im1; }
    return Vektor.unsafeMake(g); }

  //--------------------------------------------------------------

  @Override
  public final Function tangentAt (final Vektor x) {
    final int n = domain().dimension();
    assert n == x.dimension();
    final double[] xx = x.unsafeCoordinates();
    final double[] g = new double[n];
    double y = 0.0;
    for (int j=0;j<n;j+=2) {
      final double x2im1 = xx[j];
      final int j1 = j+1;
      final double x2i   = xx[j1];

      final double f2im1 = 10.0*(x2i-(x2im1*x2im1));
      final double f2i   = 1.0 - x2im1;

      final double df2im1_dx2im1 = -20.0*x2im1;
      g[j] = 2.0*fma(f2im1,df2im1_dx2im1,-f2i);
      g[j1] = 20.0*f2im1;
      y = fma(f2im1,f2im1,y);
      y = fma(f2i,f2i,y); }

    return AffineFunctional.make(Vektor.unsafeMake(g),y); }

  //--------------------------------------------------------------
  // construction
  //--------------------------------------------------------------

  private RosenbrockFn (final int dimension) {
    super();
    assert (dimension % 2) == 0;
    _domain = Dn.get(dimension); }

  //--------------------------------------------------------------
  /** Return a {@link RosenbrockFn} test function of the given
   ** <code>dimension</code> (which is forced to be even).
   **/

  public static final RosenbrockFn get (final int halfDimension) {
    return new RosenbrockFn(2*halfDimension); }

  //--------------------------------------------------------------
} // end class
//--------------------------------------------------------------

