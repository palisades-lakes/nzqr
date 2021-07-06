package nzqr.java.test.functions.scalar;

import static java.lang.Double.NEGATIVE_INFINITY;
import static java.lang.Double.NaN;
import static java.lang.Double.POSITIVE_INFINITY;
import static java.lang.Double.doubleToLongBits;
import static java.lang.Double.isFinite;
import static java.lang.Double.isNaN;
import static org.apache.commons.math3.fraction.BigFraction.ZERO;

import org.apache.commons.math3.fraction.BigFraction;

import nzqr.java.functions.Domain;
import nzqr.java.functions.Function;
import nzqr.java.functions.scalar.AffineFunctional;
import nzqr.java.functions.scalar.Polynomial;

//----------------------------------------------------------------
/** 'Exact' cubic polynomial implemented with rational numbers.
 * <p>
 * Note: argmin cannot be exact, since it's an irrational number.
 * <p>
 * Immutable.
 *
 * @author palisades dot lakes at gmail dot com
 * @version 2018-10-09
 */

strictfp
public final class QCubic extends Polynomial {

  //--------------------------------------------------------------
  // fields
  //--------------------------------------------------------------
  // Monomial basis coefficients

  private final BigFraction _a0;
  private final BigFraction _a1;
  private final BigFraction _a2;
  private final BigFraction _a3;

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
  // ScalarFunctional methods
  //--------------------------------------------------------------

  @Override
  public final String safeName () {
    return "Q3." + 
      safeString(_a0) + "." +
      safeString(_a1) + "." +
      safeString(_a2) + "." +
      safeString(_a3); }

  //--------------------------------------------------------------
  // Function methods
  //--------------------------------------------------------------

  @Override
  public final double doubleValue (final double x) {
    if (isFinite(x)) {
      final BigFraction q = new BigFraction(x); 
      return 
        _a3.multiply(q).add(_a2)
        .multiply(q).add(_a1)
        .multiply(q).add(_a0)
        .doubleValue(); } 
    if (isNaN(x)) { return NaN; }
    if (POSITIVE_INFINITY == x) { return _positiveLimitValue; }
    return _negativeLimitValue; }


  @Override
  public final double slope (final double x) {
    if (isFinite(x)) {
      final BigFraction q = new BigFraction(x); 
      return 
        _a3.multiply(3).multiply(q).add(_a2.multiply(2))
        .multiply(q).add(_a1)
        .doubleValue(); } 
    if (isNaN(x)) { return NaN; }
    if (POSITIVE_INFINITY == x) { return _positiveLimitSlope; }
    return _negativeLimitSlope; }

  @Override
  public final Function tangentAt (final double x) {
    return AffineFunctional.make(doubleValue(x),slope(x)); }

  @Override
  public final double doubleArgmin (final Domain support) { 
    if (support.contains(_xmin)) { return _xmin; }
    return NaN; }

  //--------------------------------------------------------------
  // Object methods
  //--------------------------------------------------------------

  @Override
  public int hashCode () {
    final int prime = 31;
    int result = 1;
    result = prime * result + _a0.hashCode();
    result = prime * result + _a1.hashCode();
    result = prime * result + _a2.hashCode();
    result = prime * result + _a3.hashCode();
    final long temp = doubleToLongBits(_xmin);
    result = prime * result + (int) (temp ^ (temp >>> 32));
    return result; }

  @Override
  public final boolean equals (Object obj) {
    if (this == obj) { return true; }
    if (obj == null) { return false; }
    if (!(obj instanceof QCubic)) { return false; }
    final QCubic other = (QCubic) obj;
    if (!_a0.equals(other._a0)) { return false; }
    if (!_a1.equals(other._a1)) { return false; }
    if (!_a2.equals(other._a2)) { return false; }
    if (!_a3.equals(other._a3)) { return false; }
    if (doubleToLongBits(_xmin) != Double
      .doubleToLongBits(other._xmin)) { return false; }
    return true; }

  @Override
  public final String toString () {
    return 
      "Q3[" + 
      _a0.toString().replace(" ","") + " + " + 
      _a1.toString().replace(" ","") + "*x + " + 
      _a2.toString().replace(" ","") + "*x^2 + " + 
      _a3.toString().replace(" ","") + "*x^3; " 
      + _xmin + "]"; }

  //--------------------------------------------------------------
  // construction
  //--------------------------------------------------------------

  private static final BigFraction 
  secondDerivative (final BigFraction a2,
                    final BigFraction a3,
                    final BigFraction q) {
    return a2.multiply(2).add(a3.multiply(6).multiply(q)); }

  //--------------------------------------------------------------

  private QCubic (final BigFraction a0,
                  final BigFraction a1,
                  final BigFraction a2,
                  final BigFraction a3) { 
    super(); 
    assert ! a3.equals(ZERO);

    _a0 = a0; _a1 = a1; _a2 = a2; _a3 = a3; 
    final BigFraction[] criticalPts = 
      roots(a1,a2.multiply(2),a3.multiply(3));

    final int a3sign = a3.compareTo(ZERO);
    if (2 > criticalPts.length) { // no local minimum
      if (0 < a3sign) { _xmin = NEGATIVE_INFINITY; }
      else if (0 > a3sign) { _xmin = POSITIVE_INFINITY; } 
      else { assert false : "can't get here!"; _xmin = NaN; } } 
    else {
      // One of these must be positive and the other negative?
      final BigFraction d20 = 
        secondDerivative(a2,a3,criticalPts[0]);
      final BigFraction d21 = 
        secondDerivative(a2,a3,criticalPts[1]);
      switch (d20.compareTo(d21)) {
      case -1 : _xmin = criticalPts[1].doubleValue(); break;
      case 1 : _xmin = criticalPts[0].doubleValue(); break;
      default : assert false : "can't get here!"; _xmin = NaN; } }

    // limiting values:
    if (0 < a3sign) {
      _positiveLimitValue = POSITIVE_INFINITY; 
      _negativeLimitValue = NEGATIVE_INFINITY; 
      _positiveLimitSlope = POSITIVE_INFINITY; 
      _negativeLimitSlope = NEGATIVE_INFINITY;  }
    else if (0 > a3sign) {
      _positiveLimitValue = NEGATIVE_INFINITY; 
      _negativeLimitValue = POSITIVE_INFINITY; 
      _positiveLimitSlope = NEGATIVE_INFINITY; 
      _negativeLimitSlope = POSITIVE_INFINITY;  } 
    else {
      assert false : "can't get here!";
    _positiveLimitValue = NaN; 
    _negativeLimitValue = NaN; 
    _positiveLimitSlope = NaN; 
    _negativeLimitSlope = NaN;  } }


  //--------------------------------------------------------------

  public static final Polynomial make (final BigFraction a0,
                                       final BigFraction a1,
                                       final BigFraction a2,
                                       final BigFraction a3) { 
    if (ZERO.equals(a3)) { return QQuadratic.make(a0,a1,a2); }
    return new QCubic(a0,a1,a2,a3); }

  public static final Polynomial make (final double a0,
                                       final double a1,
                                       final double a2,
                                       final double a3) { 
    if (0.0==a3) { return QQuadratic.make(a0,a1,a2); }
    return new QCubic(
      new BigFraction(a0),
      new BigFraction(a1),
      new BigFraction(a2),
      new BigFraction(a3)); }

  //--------------------------------------------------------------
} // end class
//--------------------------------------------------------------

