package nzqr.java.functions.scalar;

import static java.lang.StrictMath.abs;

import org.apache.commons.math3.fraction.BigFraction;

import nzqr.java.functions.Dn;
import nzqr.java.functions.Domain;
import nzqr.java.functions.Functional;

/** Base class for real valued functions on <b>R</b>.
 *
 * @author palisades dot lakes at gmail dot com
 * @version 2018-10-09
 */

public abstract class ScalarFunctional extends Functional {

  //--------------------------------------------------------------
  // methods
  //--------------------------------------------------------------
  // TODO: move elsewhere?
  
  public static final String safeString (final BigFraction bf) {
    final BigFraction q = bf.reduce();
    final long n = q.getNumeratorAsLong();
    final long d = q.getDenominatorAsLong();
    if (0L == n) { return "0"; }
    if (d == n) { return "1"; }
    if (d == -n) { return "m1"; }
    if (0L > n) { return "m" + abs(n) + "_" + d; }
    return n + "_" + d; }
  
 /** Return something informative and short that can be used as
   * a component in filenames. Default method is usually not
   * informative enough.
   */
  
  public String safeName () {
    return getClass().getSimpleName(); }

  //--------------------------------------------------------------
  // Function methods
  //--------------------------------------------------------------

  @Override
  public final Domain domain () { return Dn.D1; }

  //--------------------------------------------------------------
  // construction
  //--------------------------------------------------------------

  public ScalarFunctional () { }

  //--------------------------------------------------------------
}
//--------------------------------------------------------------

