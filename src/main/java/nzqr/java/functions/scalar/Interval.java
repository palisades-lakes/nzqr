package nzqr.java.functions.scalar;

import static java.lang.Double.*;

import nzqr.java.functions.Domain;
import nzqr.java.functions.Doubles;

/** Base class for intervals of <code>double</code> elements.
 * <p>
 * Immutable.
 *
 * @author palisades dot lakes at gmail dot com
 * @version 2018-10-09
 */
public abstract class Interval implements Domain {

  //--------------------------------------------------------------
  // fields
  //--------------------------------------------------------------

  private final double _lower;
  public final double lower () { return _lower; }

  private final double _upper;
  public final double upper () { return _upper; }

  //--------------------------------------------------------------
  // methods
  //--------------------------------------------------------------
  /** Extend the interval to cover <code>x</code>.
   */
  public abstract Interval cover (final double x);
  
  /** Expand the interval symmetrically so that the new width is
   * <code>1+r</code> times the old width.
   */
  public abstract Interval expand (final double r);
  
  // TODO: subtract an ulp for HalfOpen?
  public final double width () { return upper() - lower(); }
  
  //--------------------------------------------------------------
  // Domain methods
  //--------------------------------------------------------------

  @Override
  public final int dimension () { return 1; }

  //--------------------------------------------------------------
  // Object methods
  //--------------------------------------------------------------

  @Override
  public final int hashCode () {
    int c = 17;

    final long l0  = Double.doubleToLongBits(_lower);
    final int c0 = (int) (l0 ^ (l0 >>> 32));
    c = (31*c) + c0;

    final long l1  = Double.doubleToLongBits(_upper);
    final int c1 = (int) (l1 ^ (l1 >>> 32));
    c = (31*c) + c1;

    return c; }

  //--------------------------------------------------------------
  // construction
  //--------------------------------------------------------------

  public Interval (final double x0,
                   final double x1) {
    super(); 
    _lower = x0; 
    _upper = x1; }

  /** return <code>[min(x0,x1),max(x0,x1))</code>. */

  public static final Interval halfOpen (final double x0,
                                         final double x1) {
    return HalfOpenInterval.make(x0,x1); }

  /** return <code>[min(x),max(x))</code>. */

  public static final Interval halfOpen (final double[] x) {
    return halfOpen(Doubles.min(x),Doubles.max(x)); }

  /** return <code>[min(x0,x1),max(x0,x1))</code>. */

  public static final Interval closed (final double x0,
                                       final double x1) {
    return ClosedInterval.make(x0,x1); }

  /** return <code>[min(x),max(x))</code>. */

  public static final Interval closed (final double[] x) {
    return closed(Doubles.min(x),Doubles.max(x)); }

  public static final Interval ALL =
    closed(NEGATIVE_INFINITY,POSITIVE_INFINITY);
  
  //--------------------------------------------------------------
} // end class
//--------------------------------------------------------------
