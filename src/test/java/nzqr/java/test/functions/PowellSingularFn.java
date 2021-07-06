package nzqr.java.test.functions;

import static java.lang.StrictMath.fma;

import java.util.Arrays;

import nzqr.java.functions.AffineFunctional;
import nzqr.java.functions.Dn;
import nzqr.java.functions.Domain;
import nzqr.java.functions.Function;
import nzqr.java.functions.Functional;
import nzqr.java.functions.Vektor;

//==========================================================
/** A common cost function used to test optimization code.<br>
 * <b>Note:</b> The dimension of the domain must be a multiple
 * of 4.
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
public final class PowellSingularFn extends Functional {

  private final Domain _domain;

  //--------------------------------------------------------------
  // methods
  //--------------------------------------------------------------
  /** Return the minimizer for the {@link PowellSingularFn} test
   * function.
   */

  public final double[] trueMinimizer () {

    final double[] xmin = new double[domain().dimension()];
    Arrays.fill(xmin,0.0);
    return xmin; }

  //--------------------------------------------------------------
  /** Return a standard starting point for the
   * {@link PowellSingularFn} test function.
   */

  public final double[] start () {

    final double[] x = new double[domain().dimension()];
    final int n4 = domain().dimension()/4;
    for (int i=0;i<n4;i++) {
      final int j= 4*i;
      x[j] = 3.0;
      x[j+1] = -1.0;
      x[j+2] = 0.0;
      x[j+3] = 1.0; }
    return x; }

  //--------------------------------------------------------------
  // Function methods
  //--------------------------------------------------------------

  @Override
  public final Domain domain () { return _domain; }

  //--------------------------------------------------------------

  private static final double SQRT10 = StrictMath.sqrt(10.0);
  private static final double SQRT05 = StrictMath.sqrt(5.0);

  //--------------------------------------------------------------

  @Override
  public final double doubleValue (final Vektor x) {
    final int n = domain().dimension();
    assert n == x.dimension();
    final double[] xx = x.unsafeCoordinates();
    double y = 0.0;
    // note changes from dennis-schnabel for zero-based indexing
    for (int i=0;i<(n/4);i++) {
      final int j0 = 4*i;
      final int j1 = j0 + 1;
      final int j2 = j1 + 1;
      final int j3 = j2 + 1;
      final double x0 = xx[j0];
      final double x1 = xx[j1];
      final double x2 = xx[j2];
      final double x3 = xx[j3];
      final double x03 = x0 - x3;
      final double x12 = fma(-2.0,x2,x1);
      final double y01 = fma(10.0,x1,x0);
      final double y03 = SQRT10*x03*x03;
      final double y12 = x12*x12;
      final double x23 = x2 - x3;
      final double y23 = SQRT05*x23;
      y = fma(y01,y01,y);
      y = fma(y03,y03,y);
      y = fma(y12,y12,y);
      y = fma(y23,y23,y);
    }
    return y; }


  //--------------------------------------------------------------

  @Override
  public final Vektor gradient (final Vektor x) {
    final int n = domain().dimension();
    assert n == x.dimension();
    final double[] xx = x.unsafeCoordinates();
    final double[] g = new double[n];
    // note changes from dennis-schnabel for zero-based indexing
    for (int i=0;i<(n/4);i++) {
      final int j0 = 4*i;
      final int j1 = j0 + 1;
      final int j2 = j1 + 1;
      final int j3 = j2 + 1;
      final double x0 = xx[j0];
      final double x1 = xx[j1];
      final double x2 = xx[j2];
      final double x3 = xx[j3];
      final double x03 = x0 - x3;
      final double x12 = fma(-2.0,x2,x1);
      final double y01 = fma(10.0,x1,x0);
      final double x23 = x2 - x3;
      final double x333 = 40.0*x03*x03*x03;
      g[j0] = fma(2.0,y01,x333);
      final double x123 = x12*x12*x12;
      g[j1] = fma(20.0,y01,4.0*x123);
      g[j2] = fma(-8.0,x123,10.0*x23);
      g[j3] = fma(-10.0,x23,-x333); }
    return Vektor.unsafeMake(g); }

  //--------------------------------------------------------------

  @Override
  public final Function tangentAt (final Vektor x) {
    final int n = domain().dimension();
    final double[] xx = x.unsafeCoordinates();
    final double[] g = new double[n];
    double y = 0.0;
    // note changes from dennis-schnabel for zero-based indexing
    for (int i=0;i<(n/4);i++) {
      final int j0 = 4*i;
      final int j1 = j0 + 1;
      final int j2 = j1 + 1;
      final int j3 = j2 + 1;
      final double x0 = xx[j0];
      final double x1 = xx[j1];
      final double x2 = xx[j2];
      final double x3 = xx[j3];
      final double x03 = x0 - x3;
      final double x12 = fma(-2.0,x2,x1);
      final double y01 = fma(10.0,x1,x0);
      final double y03 = SQRT10*x03*x03;
      final double y12 = x12*x12;
      final double x23 = x2 - x3;
      final double y23 = SQRT05*x23;
      y = fma(y01,y01,y);
      y = fma(y03,y03,y);
      y = fma(y12,y12,y);
      y = fma(y23,y23,y);
      final double x333 = 40.0*x03*x03*x03;
      g[j0] = fma(2.0,y01,x333);
      final double x123 = x12*x12*x12;
      g[j1] = fma(20.0,y01,4.0*x123);
      g[j2] = fma(-8.0,x123,10.0*x23);
      g[j3] = fma(-10.0,x23,-x333); }
    return AffineFunctional.make(Vektor.unsafeMake(g),y); }

  //--------------------------------------------------------------
  // construction
  //--------------------------------------------------------------

  private PowellSingularFn (final int dimension) {
    super();
    assert 0 == (dimension % 4) :
      "dimension (" + dimension +") must be a multiple of 4.";
    _domain = Dn.get(dimension); }

  //--------------------------------------------------------------
  /** Return a {@link PowellSingularFn} test function of the given
   * <code>dimension</code> (which is forced to be a multiple of
   * 4).
   */

  public static final PowellSingularFn get (final int n4) {
    return new PowellSingularFn(4*n4); }

  //--------------------------------------------------------------
} // end class
//--------------------------------------------------------------

