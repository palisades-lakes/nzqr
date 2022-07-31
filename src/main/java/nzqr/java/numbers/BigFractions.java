package nzqr.java.numbers;

import java.math.BigInteger;
import java.util.Map;
import java.util.function.BiPredicate;
import java.util.function.BinaryOperator;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

import org.apache.commons.math3.fraction.BigFraction;

import nzqr.java.algebra.Set;
import nzqr.java.prng.Generator;
import nzqr.java.prng.Generators;

/** The set of rational numbers represented by
 * <code>BigFraction</code>
 *
 * @author palisades dot lakes at gmail dot com
 * @version 2022-07-31
 */
public final class BigFractions implements Set {

  //--------------------------------------------------------------
  // Set methods
  //--------------------------------------------------------------

  @Override
  public final boolean contains (final Object element) {
    return element instanceof BigFraction; }

  public static final boolean equalBigFractions (final BigFraction q0,
                                                 final BigFraction q1) {
    if (q0 == q1) { return true; }
    if (null == q0) {
      if (null == q1) { return true; }
      return false; }
    final BigInteger n0 = q0.getNumerator();
    final BigInteger d0 = q0.getDenominator();
    final BigInteger n1 = q1.getNumerator();
    final BigInteger d1 = q1.getDenominator();
    return n0.multiply(d1).equals(n1.multiply(d0)); }

  private static final BiPredicate<BigFraction,BigFraction>
  BIGFRACTION_EQUALS =
  new BiPredicate<>() {

    // BigFraction.equals reduces both arguments before checking
    // numerator and denominators are equal.
    // Guessing our BigFractions are usually already reduced.
    // Try n0*d1 == n1*d0 instead
    // TODO: try using BigINteger.bitLength() to decide
    // which method to use?
    //    @Override
    //    public final boolean test (final BigFraction q0,
    //                               final BigFraction q1) {
    //     return Objects.deepEquals(q0,q1); }
    @Override
    public final boolean test (final BigFraction q0,
                               final BigFraction q1) {
      return equalBigFractions(q0,q1); }
  };

  @Override
  public final BiPredicate equivalence () {
    return BIGFRACTION_EQUALS; }

  //--------------------------------------------------------------

  @Override
  public final Supplier generator (final Map options) {
    final Generator bfs = Generators.bigFractionGenerator(
      Set.urp(options));
    return
      new Supplier () {
      @Override
      public final Object get () { return bfs.next(); } }; }

  //  @Override
  //  public final Supplier generator (final UniformRandomProvider urp) {
  //    return generator(urp,Collections.emptyMap()); }

  //--------------------------------------------------------------
  // Object methods
  //--------------------------------------------------------------

  @Override
  public final int hashCode () { return 0; }

  // singleton
  @Override
  public final boolean equals (final Object that) {
    return that instanceof BigFractions; }

  @Override
  public final String toString () { return "BF"; }

  //--------------------------------------------------------------
  // construction
  //--------------------------------------------------------------

  private BigFractions () { }

  private static final BigFractions SINGLETON =
    new BigFractions();

  public static final BigFractions get () { return SINGLETON; }

  public static final BinaryOperator<BigFraction> ADD =
    new BinaryOperator<> () {
    @Override
    public final BigFraction apply (final BigFraction q0,
                                    final BigFraction q1) {
      return q0.add(q1); }
  };

  public static final UnaryOperator<BigFraction>
  ADDITIVE_INVERSE =
  new UnaryOperator<> () {
    @Override
    public final BigFraction apply (final BigFraction q) {
      return q.negate(); }
  };

  public static final BinaryOperator<BigFraction> MULTIPLY =
    new BinaryOperator<>() {
    @Override
    public final BigFraction apply (final BigFraction q0,
                                    final BigFraction q1) {
      return q0.multiply(q1); }
  };

  public static final UnaryOperator<BigFraction>
  MULTIPLICATIVE_INVERSE =
  new UnaryOperator<> () {
    @Override
    public final BigFraction apply (final BigFraction q) {
      // only a partial inverse
      if (BigFraction.ZERO.equals(q)) { return null; }
      return q.reciprocal(); }
  };

  //--------------------------------------------------------------
}
//--------------------------------------------------------------

