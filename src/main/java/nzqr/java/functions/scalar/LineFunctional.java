package nzqr.java.functions.scalar;

import nzqr.java.functions.Function;
import nzqr.java.functions.Vektor;

/** A function from <b>R</b> to <b>R</b> constructed from
 * a function from <b>R</b><sup>n</sup> to <b>R</b> and a line
 * in <b>R</b><sup>n</sup>. Minimized by line searches.
 *
 * !!!!DANGER!!!! this implementation relies on shared mutable
 * state.
 * Functionals should be immutable (but this class isn't!).
 *
 * @author palisades dot lakes at gmail dot com
 * @version 2018-09-07
 */

public final class LineFunctional extends ScalarFunctional {

  //--------------------------------------------------------------
  // fields
  //--------------------------------------------------------------

  private final Function _f;

  private final Vektor _origin;
  private final Vektor _direction;
  private Vektor _x;
  public final Vektor getX () { return _x; }
  private Vektor _g;
  public final Vektor getG () { return _g; }

  //--------------------------------------------------------------
  // Function methods
  //--------------------------------------------------------------
  // WARNING: using mutable args to Function.doubleValue(), etc.,
  // means we can't memoize it.

  @Override
  public final double doubleValue (final double x) {
    _x = _direction.axpy(x,_origin);
    final double y = _f.doubleValue(_x);
    return y; }

  @Override
  public final double slope (final double x) {
    _x = _direction.axpy(x,_origin);
    _g = _f.gradient(_x);
    final double d = _direction.dot(_g);
    return d; }

  //--------------------------------------------------------------
  // Object methods
  //--------------------------------------------------------------
  // TODO: too long for toString..

  @Override
  public final String toString () {
    final StringBuilder b = new StringBuilder();
    b.append("LineFunctional:\n");
    b.append(_f.toString());
    b.append("\nat:\n");
    final String fmt = "%4d %22.16E %22.16E %22.16E %22.16E\n";
    b.append(
      String.format("%-4s %-22s %-22s %-22s %-22s\n",
        "i","gradient","position","direction","origin"));
    final int n = _f.domain().dimension();
    for (int i=0;i<n;i++) {
      b.append(String.format(fmt,
        Integer.valueOf(i),
        Double.valueOf(_g.coordinate(i)),
        Double.valueOf(_x.coordinate(i)),
        Double.valueOf(_direction.coordinate(i)),
        Double.valueOf(_origin.coordinate(i)))); }
    return b.toString(); }

  //--------------------------------------------------------------
  // construction
  //--------------------------------------------------------------

  private LineFunctional (final Function f,
                          final Vektor origin,
                          final Vektor direction) {
    _f = f;
    _origin = origin;
    _direction = direction; }

  public static final LineFunctional
  make (final Function f,
        final Vektor origin,
        final Vektor direction) {
    assert f.domain().contains(origin);
    assert f.domain().contains(direction);
    return new LineFunctional(f,origin,direction); }

  //--------------------------------------------------------------
}
//--------------------------------------------------------------

