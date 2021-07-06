package nzqr.java.test.functions.scalar;

import static java.lang.Double.NEGATIVE_INFINITY;
import static java.lang.Double.NaN;
import static java.lang.Double.POSITIVE_INFINITY;
import static java.lang.Double.isFinite;
import static java.lang.Double.isNaN;
import static org.apache.commons.math3.fraction.BigFraction.ZERO;

import org.apache.commons.math3.fraction.BigFraction;

import nzqr.java.functions.Domain;
import nzqr.java.functions.Function;
import nzqr.java.functions.scalar.AffineFunctional;
import nzqr.java.functions.scalar.Polynomial;

//----------------------------------------------------------------
/** 'Exact' affine polynomial, implemented with rational 
 * numbers.
 * <p>
 * Immutable.
 *
 * @author palisades dot lakes at gmail dot com
 * @version 2018-10-09
 */

public final class QAffine extends Polynomial {

  //--------------------------------------------------------------
  // fields
  //--------------------------------------------------------------
  // Monomial basis coefficients

  private final BigFraction _a0;
  private final BigFraction _a1;

  //--------------------------------------------------------------
  // Polynomial methods
  //--------------------------------------------------------------

  @Override
  public final int degree () { return 1; }

  //--------------------------------------------------------------
  // ScalarFunctional methods
  //--------------------------------------------------------------

  @Override
  public final String safeName () {
    return "Q1." + safeString(_a0) + "." + safeString(_a1); }

  //--------------------------------------------------------------
  // Function methods
  //--------------------------------------------------------------

  @Override
  public final double doubleValue (final double x) {
    if (isNaN(x)) { return NaN; }
    if (isFinite(x)) {
      final BigFraction q = new BigFraction(x); 
      return _a1.multiply(q).add(_a0).doubleValue(); }
    return x * _a1.doubleValue(); }

  @Override
  public final double slope (final double x) {
    if (isNaN(x)) { return NaN; }
    return _a1.doubleValue(); }

  @Override
  public final Function tangentAt (final double x) {
    return AffineFunctional.make(doubleValue(x),slope(x)); }

  @Override
  public final double doubleArgmin (final Domain support) { 
    switch (_a1.compareTo(ZERO)) {
    case -1 :
      if (support.contains(POSITIVE_INFINITY)) {
        return POSITIVE_INFINITY; }
      return NaN;
    case 0 : return NaN;
    case 1 :
      if (support.contains(NEGATIVE_INFINITY)) {
        return NEGATIVE_INFINITY; }
      return NaN;
    default : // shouldn't get here
      assert false : "can't get here!"; }
    assert false : "can't get here!"; 
      return NaN; }

  //--------------------------------------------------------------
  // Object methods
  //--------------------------------------------------------------

  @Override
  public final int hashCode () {
    final int prime = 31;
    int result = 1;
    result = prime * result + _a0.hashCode();
    result = prime * result + _a1.hashCode();
    return result; }

  @Override
  public final boolean equals (Object obj) {
    if (this == obj) { return true; }
    if (obj == null) { return false; }
    if (!(obj instanceof QAffine)) { return false; }
    final QAffine other = (QAffine) obj;
    if (!_a0.equals(other._a0)) { return false; }
    if (!_a1.equals(other._a1)) { return false; }
    return true; }

  @Override
  public String toString () {
    return "Q1[" + 
      _a0.toString().replace(" ","") + " + " + 
      _a1.toString().replace(" ","") + "*x]"; }

  //--------------------------------------------------------------
  // construction
  //--------------------------------------------------------------

  private QAffine (final BigFraction a0,
                   final BigFraction a1) { 
    super(); 
    assert ! ZERO.equals(a1);
    _a0 = a0; _a1 = a1; }
  //--------------------------------------------------------------

  public static final Polynomial make (final BigFraction a0,
                                       final BigFraction a1) { 
    if (ZERO.equals(a1)) { return QConstant.make(a0); }
    return new QAffine(a0,a1); }

  public static final Polynomial make (final double a0,
                                       final double a1) { 
    if (0.0==a1) { return QConstant.make(a0); }
    return new QAffine(new BigFraction(a0),new BigFraction(a1)); }

  //--------------------------------------------------------------
} // end class
//--------------------------------------------------------------
