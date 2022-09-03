package nzqr.java.numbers;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.function.BiPredicate;
import java.util.function.BinaryOperator;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

import org.apache.commons.rng.UniformRandomProvider;
import org.apache.commons.rng.sampling.CollectionSampler;
import org.apache.commons.rng.sampling.distribution.ContinuousSampler;
import org.apache.commons.rng.sampling.distribution.ContinuousUniformSampler;

import nzqr.java.algebra.OneSetOneOperation;
import nzqr.java.algebra.OneSetTwoOperations;
import nzqr.java.algebra.Set;
import nzqr.java.prng.Generator;
import nzqr.java.prng.GeneratorBase;
import nzqr.java.prng.Generators;

/** The subset of the integers that can be
 * represented by <code>java.math.BigInteger</code>.
 *
 * @author palisades dot lakes at gmail dot com
 * @version 2022-09-03
 */
@SuppressWarnings({"unchecked","static-method"})
public final class BigIntegers implements Set {

  //--------------------------------------------------------------
  // operations for algebraic structures over BigFloats.
  //--------------------------------------------------------------

  private final BigInteger add (final BigInteger q0,
                                final BigInteger q1) {
    //assert contains(q0);
    //assert contains(q1);
    return q0.add(q1); }

  public final BinaryOperator<BigInteger> adder () {
    return new BinaryOperator<> () {
      @Override
      public final String toString () { return "BF.add()"; }
      @Override
      public final BigInteger apply (final BigInteger q0,
                                     final BigInteger q1) {
        return BigIntegers.this.add(q0,q1); } }; }

  //--------------------------------------------------------------

  public final BigInteger additiveIdentity () {
    return BigInteger.valueOf(0L); }

  //--------------------------------------------------------------
  // TODO: is consistency with other algebraic structure classes
  // worth the indirection?

  private final BigInteger negate (final BigInteger q) {
    //assert contains(q);
    return q.negate(); }

  public final UnaryOperator<BigInteger> additiveInverse () {
    return new UnaryOperator<> () {
      @Override
      public final String toString () { return "BF.negate()"; }
      @Override
      public final BigInteger apply (final BigInteger q) {
        return BigIntegers.this.negate(q); } }; }

  //--------------------------------------------------------------

  private final BigInteger multiply (final BigInteger q0,
                                     final BigInteger q1) {
    //assert contains(q0);
    //assert contains(q1);
    return q0.multiply(q1); }

  public final BinaryOperator<BigInteger> multiplier () {
    return new BinaryOperator<>() {
      @Override
      public final String toString () { return "BF.multiply"; }
      @Override
      public final BigInteger apply (final BigInteger q0,
                                     final BigInteger q1) {
        return BigIntegers.this.multiply(q0,q1); } }; }

  //--------------------------------------------------------------

  public final BigInteger multiplicativeIdentity () {
    return BigInteger.valueOf(1L); }

  //--------------------------------------------------------------
  // Set methods
  //--------------------------------------------------------------

  @Override
  public final boolean contains (final Object element) {
    return element instanceof BigInteger; }

  //--------------------------------------------------------------

  @Override
  public final BiPredicate equivalence () {
    return new BiPredicate<BigInteger,BigInteger>() {
      @Override
      public final boolean test (final BigInteger q0,
                                 final BigInteger q1) {
        final boolean result = q0.equals(q1);
        return result;} }; }

  public static final Generator
  generator (final int n,
             final UniformRandomProvider urp) {
    return new GeneratorBase ("rationalGenerator:" + n) {
      final Generator g = generator(urp);
      @Override
      public final Object next () {
        final BigInteger[] z = new BigInteger[n];
        for (int i=0;i<n;i++) { z[i] = (BigInteger) g.next(); }
        return z; } }; }

  // TODO: determine which generator from options.
  @Override
  public final Supplier generator (final Map options) {
    final UniformRandomProvider urp = Set.urp(options);
    final Generator g = generator(urp);
    return
      new Supplier () {
      @Override
      public final Object get () { return g.next(); } }; }

  //--------------------------------------------------------------
  // Object methods
  //--------------------------------------------------------------

  @Override
  public final int hashCode () { return 0; }

  // singleton
  @Override
  public final boolean equals (final Object that) {
    return that instanceof BigIntegers; }

  @Override
  public final String toString () { return "BF"; }

  //--------------------------------------------------------------
  // construction
  //--------------------------------------------------------------

  private BigIntegers () { }

  private static final BigIntegers SINGLETON = new BigIntegers();

  public static final BigIntegers get () { return SINGLETON; }

  //--------------------------------------------------------------

  public static final OneSetOneOperation ADDITIVE_MAGMA =
    OneSetOneOperation.magma(get().adder(),get());

  public static final OneSetOneOperation MULTIPLICATIVE_MAGMA =
    OneSetOneOperation.magma(get().multiplier(),get());

  public static final OneSetTwoOperations RING =
    OneSetTwoOperations.commutativeRing(
      get().adder(),
      get().additiveIdentity(),
      get().additiveInverse(),
      get().multiplier(),
      get().multiplicativeIdentity(),
      get());

  //--------------------------------------------------------------
}
//--------------------------------------------------------------

