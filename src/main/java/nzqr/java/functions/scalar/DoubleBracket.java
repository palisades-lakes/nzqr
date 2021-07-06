package nzqr.java.functions.scalar;

/** Three strictly increasing <code>double</code>s. Immutable.
 *
 * @author palisades dot lakes at gmail dot com
 * @version 2018-09-07
 */
public final class DoubleBracket  {

  //--------------------------------------------------------------
  // fields
  //--------------------------------------------------------------

  private final double _x0;
  public final double x0 () { return _x0; }

  private final double _x1;
  public final double x1 () { return _x1; }

  private final double _x2;
  public final double x2 () { return _x2; }

  //--------------------------------------------------------------
  // Object methods
  //--------------------------------------------------------------

  @Override
  public final int hashCode () {
    int c = 17;

    final long l0  = Double.doubleToLongBits(_x0);
    final int c0 = (int) (l0 ^ (l0 >>> 32));
    c = (31*c) + c0;

    final long l1  = Double.doubleToLongBits(_x1);
    final int c1 = (int) (l1 ^ (l1 >>> 32));
    c = (31*c) + c1;

    final long l2  = Double.doubleToLongBits(_x2);
    final int c2 = (int) (l2 ^ (l2 >>> 32));
    c = (31*c) + c2;

    return c; }

  @Override
  public final boolean equals (final Object that) {
    if (! (that instanceof DoubleBracket)) { return false; }
    final DoubleBracket db = (DoubleBracket) that;
    return
      (_x0 == db._x0) && (_x1 == db._x1) && (_x2 == db._x2); }

  @Override
  public final String toString () {
    return
      String.format(
        "DoubleBracket(%E,%E,%E)",
        Double.valueOf(_x0), 
        Double.valueOf(_x1), 
        Double.valueOf(_x2)); }

  //--------------------------------------------------------------
  // construction
  //--------------------------------------------------------------

  private DoubleBracket (final double x0,
                     final double x1,
                     final double x2) {
    super(); 
    assert (x0 < x1) && (x1 < x2);
    _x0 = x0; _x1 = x1; _x2 = x2; }

  /** <code>a</code>, <code>b>/code>, and <code>c</code> must be 
   * distinct. 
   */
  public static final DoubleBracket make (final double a,
                                      final double b,
                                      final double c) {
    assert (a != b) && (b != c) && (c != a);
    
    if (a < b) {
      if (b < c) { return new DoubleBracket(a,b,c); }
      if (a < c) { return new DoubleBracket(a,c,b); }
      return new DoubleBracket(c,a,b); }
    if (a < c) { return new DoubleBracket(b,a,c); }
    if (b < c) { return new DoubleBracket(b,c,a); }
    return new DoubleBracket(c,b,a); }

  //--------------------------------------------------------------
} // end class
//--------------------------------------------------------------
