package nzqr.java.test.functions;

import java.util.Arrays;

import nzqr.java.functions.AffineFunctional;
import nzqr.java.functions.Dn;
import nzqr.java.functions.Domain;
import nzqr.java.functions.Function;
import nzqr.java.functions.Functional;
import nzqr.java.functions.Vektor;

//----------------------------------------------------------------
/** Test exceptions with constant functionalN.
 * <p>
 * Note: immutable.
 * <p>
 * TODO: add translation to test other optima
 *
 * @author palisades dot lakes at gmail dot com
 * @version 2018-09-07
 */

public strictfp final class ConstantFn extends Functional {

  private final Domain _domain;
  private final double _value;
  private final Vektor _g;

  //--------------------------------------------------------------
  // methods
  //--------------------------------------------------------------

  public final double[] start (final double xi) {
    final double[] x = new double[domain().dimension()];
    Arrays.fill(x,xi);
    return x; }

  public final double[] trueMinimizer (final double xi) {
    final double[] x = new double[domain().dimension()];
    Arrays.fill(x,xi);
    return x; }

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
    return _value; }

  //--------------------------------------------------------------

  @Override
  public final Vektor gradient (final Vektor x) {
    final int n = domain().dimension();
    assert n == x.dimension();
    return _g; }
  
  //--------------------------------------------------------------

  @Override
  public final Function tangentAt (final Vektor x) {
    final int n = domain().dimension();
    assert n == x.dimension();
    return AffineFunctional.make(_g,_value); }
  
  //--------------------------------------------------------------
  // construction
  //--------------------------------------------------------------

  private ConstantFn (final int dimension,
                      final double v,
                      final double gi) {
    super();
    _domain = Dn.get(dimension);
    _value = v;
    _g = Vektor.constantVektor(dimension,gi); }

  //--------------------------------------------------------------
  /** Return a {@link ConstantFn} test function of the given
   * <code>dimension</code>.
   * Correct gradient is zero vektor; passing in
   * <code>gi</code> allows creating an
   * invalid function for testing.
   */

  public static final ConstantFn get (final int dimension,
                                      final double v,
                                      final double gi) {
    return new ConstantFn(dimension,v,gi); }

  //--------------------------------------------------------------
  /** Return a {@link ConstantFn} test function of the given
   * <code>dimension</code>.
   */

  public static final ConstantFn get (final int dimension,
                                      final double v) {
    return new ConstantFn(dimension,v,0.0); }

  //--------------------------------------------------------------
} // end class
//--------------------------------------------------------------

