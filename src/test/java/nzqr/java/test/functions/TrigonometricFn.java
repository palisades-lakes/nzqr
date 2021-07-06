package nzqr.java.test.functions;

import static java.lang.Math.*;

import java.util.Arrays;

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
public final class TrigonometricFn extends Functional {

  private final Domain _domain;

  //--------------------------------------------------------------
  // methods
  //--------------------------------------------------------------
  /** Return the minimizer for the {@link TrigonometricFn} test
   * function. Only handles dimensions 2, 16, and 128.
   */

  public final double[] trueMinimizer () {
    switch(domain().dimension()) {
    case 2 :
      return new double[]
        { -0.3000256910524835, 1.197980361439049 };
    case 16 :
      return new double[]
        { -0.007661928570809261, -0.007633017483549931,
          -0.007604536289688477, -0.00757647317893481,
          -0.0075488168186337995, -0.007521556328062661,
          -0.007494681254524562, -0.007468181551780464,
          0.22875648990599431, 0.20675357496084004,
          0.18871061426563784, 0.17364821237429548,
          0.1608847704139503, 0.1499314792608856,
          0.14042875387701326, 0.1321062303232295, };
    default:
      //throw new IllegalArgumentException(
      System.out.println(
        "don't have 'true' minimizer for dimension "
          + domain().dimension()); }
    return new double[0]; }

  //--------------------------------------------------------------
  /** Return a standard starting point for the
   * {@link TrigonometricFn} test function.
   */

  public final double[] start () {

    final int n = domain().dimension();
    final double[] x = new double[n];
    Arrays.fill(x,1.0/n);
    return x; }

  //--------------------------------------------------------------
  // Function methods
  //--------------------------------------------------------------

  @Override
  public final Domain domain () { return _domain; }

  //--------------------------------------------------------------

  private final double f (final double[] x,
                          final int i) {
    // note changes from dennis-schnabel for zero-based indexing
    // note: association of expressions different from
    // Dennis-Schnabel
    final int n = domain().dimension();
    double sum = 0.0;
    for (int j=0;j<n;j++) { sum += cos(x[j]); }
    final double xi = x[i];
    return
      (n
        -
        sum)
      +
      (n*(i+1)*(cos(xi)-1.0))
      +
      (n*sin(xi)); }

  private final double df (final double[] x,
                           final int i,
                           final int k) {
    // note changes from dennis-schnabel for zero-based indexing
    // note: association of expressions different from
    // Dennis-Schnabel
    final int n = domain().dimension();
    final double xk = x[k];
    final double d = sin(xk);
    if (i!=k) { return d; }
    return
      (d
        -
        (n*(k+1)*sin(xk)))
      +
      (n*cos(xk)); }

  private final double df (final double[] x,
                           final int k) {
    // note: association of expressions different from
    // Dennis-Schnabel
    final int n = domain().dimension();
    double d = 0.0;
    for (int i=0;i<n;i++) { d += f(x,i)*df(x,i,k); }
    return 2.0*d; }

  //--------------------------------------------------------------

  @Override
  public final double doubleValue (final Vektor x) {
    final int n = domain().dimension();
    assert n == x.dimension();
    final double[] xx = x.unsafeCoordinates();
    double y = 0.0;
    for (int i=0;i<n;i++) {
      final double fi = f(xx,i);
      y += fi*fi; }
    return y; }

  //--------------------------------------------------------------

  @Override
  public final Vektor gradient (final Vektor x) {
    final int n = domain().dimension();
    assert n == x.dimension();
    final double[] xx = x.unsafeCoordinates();
    final double[] g = new double[n];
    for (int i=0;i<n;i++) { g[i] = df(xx,i); }
    return Vektor.unsafeMake(g); }

  //--------------------------------------------------------------

  @Override
  public final Function tangentAt (final Vektor x) {
    return AffineFunctional.make(gradient(x),doubleValue(x)); }

  //--------------------------------------------------------------
  // construction
  //--------------------------------------------------------------

  private TrigonometricFn (final int dimension) {
    super();
    _domain = Dn.get(dimension); }

  //--------------------------------------------------------------
  /** Return a {@link TrigonometricFn} test function of the given
   * <code>dimension</code> (which is forced to be a multiple of
   * 4).
   */

  public static final TrigonometricFn get (final int n) {
    return new TrigonometricFn(n); }

  //--------------------------------------------------------------
} // end class
//--------------------------------------------------------------

