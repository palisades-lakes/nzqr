package nzqr.java.test.functions.scalar;

import static java.lang.Double.NaN;
import static java.lang.Double.isNaN;

import org.apache.commons.math3.fraction.BigFraction;

import nzqr.java.functions.Domain;
import nzqr.java.functions.Function;
import nzqr.java.functions.scalar.AffineFunctional;
import nzqr.java.functions.scalar.Polynomial;

//----------------------------------------------------------------
/** 'Exact' constant polynomial, implemented with rational 
 * numbers.
 * <p>
 * Immutable.
 *
 * @author palisades dot lakes at gmail dot com
 * @version 2018-10-09
 */

public final class QConstant extends Polynomial {

  //--------------------------------------------------------------
  // fields
  //--------------------------------------------------------------
  // Monomial basis coefficients

  private final BigFraction _a0;

 
  //--------------------------------------------------------------
  // Polynomial methods
  //--------------------------------------------------------------
  
  @Override
  public final int degree () { return 0; }
  
 //--------------------------------------------------------------
  // ScalarFunctional methods
  //--------------------------------------------------------------
  
  @Override
  public final String safeName () {
    return "Q0." + safeString(_a0); }

  //--------------------------------------------------------------
  // Function methods
  //--------------------------------------------------------------

  @Override
  public final double doubleValue (final double x) {
    if (isNaN(x)) { return NaN; }
    return _a0.doubleValue(); }

  @Override
  public final double slope (final double x) {
    if (isNaN(x)) { return NaN; }
    return 0.0; }

  @Override
  public final Function tangentAt (final double x) {
    return AffineFunctional.make(doubleValue(x),slope(x)); }

  @Override
  public final double doubleArgmin (final Domain support) { 
    return NaN; }

  //--------------------------------------------------------------
  // Object methods
  //--------------------------------------------------------------

  @Override
  public final int hashCode () {
    final int prime = 31;
    int result = 1;
    result = prime * result + _a0.hashCode();
    return result; }

  @Override
  public final boolean equals (Object obj) {
    if (this == obj) { return true; }
    if (obj == null) { return false; }
    if (!(obj instanceof QConstant)) { return false; }
    final QConstant other = (QConstant) obj;
    if (!_a0.equals(other._a0)) { return false; }
    return true; }

  @Override
  public String toString () {
    return "Q0[" + _a0.toString().replace(" ","") + "]"; }

  //--------------------------------------------------------------
  // construction
  //--------------------------------------------------------------

  private QConstant (final BigFraction a0) { 
    super(); 
    _a0 = a0; }
  //--------------------------------------------------------------

  public static final QConstant make (final BigFraction a0) { 
    return new QConstant(a0); }

  public static final QConstant make (final double a0) { 
    return new QConstant(new BigFraction(a0)); }

  //--------------------------------------------------------------
} // end class
//--------------------------------------------------------------
