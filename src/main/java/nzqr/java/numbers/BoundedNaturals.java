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

/** The subset of the natural numbers that can be
 * represented by <code>BoundedNatural</code>.
 * 
 * The unbounded natural numbers are a commutative semi-ring,
 * but this set fails to be closed under '+' and '*',
 * when the operation result exceeds the bound.
 * 
 * @author palisades dot lakes at gmail dot com
 * @version 2022-09-03
 */
@SuppressWarnings({"unchecked","static-method"})
public final class BoundedNaturals implements Set {

  //--------------------------------------------------------------
  // operations for algebraic structures over BigFloats.
  //--------------------------------------------------------------

  private final BoundedNatural add (final BoundedNatural q0,
                                    final BoundedNatural q1) {
    //assert contains(q0);
    //assert contains(q1);
    return q0.add(q1); }

  public final BinaryOperator<BoundedNatural> adder () {
    return new BinaryOperator<> () {
      @Override
      public final String toString () { return "BF.add()"; }
      @Override
      public final BoundedNatural apply (final BoundedNatural q0,
                                         final BoundedNatural q1) {
        return BoundedNaturals.this.add(q0,q1); } }; }

  //--------------------------------------------------------------

  public final BoundedNatural additiveIdentity () {
    return BoundedNatural.valueOf(0L); }

  //--------------------------------------------------------------
  // TODO: is consistency with other algebraic structure classes
  // worth the indirection?

  private final BoundedNatural negate (final BoundedNatural q) {
    //assert contains(q);
    return q.negate(); }

  public final UnaryOperator<BoundedNatural> additiveInverse () {
    return new UnaryOperator<> () {
      @Override
      public final String toString () { return "BF.negate()"; }
      @Override
      public final BoundedNatural apply (final BoundedNatural q) {
        return BoundedNaturals.this.negate(q); } }; }

  //--------------------------------------------------------------

  private final BoundedNatural multiply (final BoundedNatural q0,
                                         final BoundedNatural q1) {
    //assert contains(q0);
    //assert contains(q1);
    return q0.multiply(q1); }

  public final BinaryOperator<BoundedNatural> multiplier () {
    return new BinaryOperator<>() {
      @Override
      public final String toString () { return "BF.multiply"; }
      @Override
      public final BoundedNatural apply (final BoundedNatural q0,
                                         final BoundedNatural q1) {
        return BoundedNaturals.this.multiply(q0,q1); } }; }

  //--------------------------------------------------------------

  public final BoundedNatural multiplicativeIdentity () {
    return BoundedNatural.valueOf(1L); }

  //--------------------------------------------------------------
  // Set methods
  //--------------------------------------------------------------

  @Override
  public final boolean contains (final Object element) {
    return element instanceof BoundedNatural; }

  //--------------------------------------------------------------

  @Override
  public final BiPredicate equivalence () {
    return new BiPredicate<BoundedNatural,BoundedNatural>() {
      @Override
      public final boolean test (final BoundedNatural q0,
                                 final BoundedNatural q1) {
        final boolean result = q0.equals(q1);
        //        if (! result) {
        //          System.out.println("nonNegative:" +
        //            (q0.nonNegative()==q1.nonNegative()));
        //          System.out.println("exponent:" +
        //            (q0.exponent()==q1.exponent()));
        //          System.out.println("significand:" +
        //            (q0.significand()==q1.significand()));
        //          System.out.println(q0.significand().getClass());
        //          System.out.println(q0.significand());
        //          System.out.println(q1.significand().getClass());
        //          System.out.println(q1.significand());
        //        }
        return result;} }; }

  //--------------------------------------------------------------

  public static final Generator
  fromBigIntegerGenerator (final UniformRandomProvider urp,
                           final int eMin,
                           final int eMax) {
    //assert eMin<eMax;
    final int eRan = eMax-eMin;
    final double dp = 0.9;
    return new GeneratorBase ("fromBigIntegerGenerator") {
      private final ContinuousSampler choose =
        new ContinuousUniformSampler(urp,0.0,1.0);
      private final Generator g0 =
        Generators.bigIntegerGenerator(urp);
      private final CollectionSampler edgeCases =
        new CollectionSampler(
          urp,
          List.of(
            BoundedNatural.valueOf(0L),
            BoundedNatural.valueOf(1L),
            BoundedNatural.valueOf(2L),
            BoundedNatural.valueOf(10L),
            BoundedNatural.valueOf(-1L)));
      @Override
      public Object next () {
        final boolean edge = choose.sample() > dp;
        if (edge) { return edgeCases.sample(); }
        final BigInteger bi = (BigInteger) g0.next();
        final boolean nonNegative = (0 <= bi.signum());
        final BoundedNatural significand =
          BoundedNatural.valueOf(nonNegative ? bi : bi.negate());
        final int exponent = urp.nextInt(eRan) + eMin;
        return
          BoundedNatural.valueOf(nonNegative,significand,exponent); } }; }

  public static final Generator
  fromBigIntegerGenerator (final UniformRandomProvider urp) {
    // default bounds allow multiply within int exponent range.
    return
      fromBigIntegerGenerator(
        urp,Integer.MIN_VALUE/2,Integer.MAX_VALUE/2); }

  // Is this characteristic of most inputs?
  public static final Generator
  fromDoubleGenerator (final UniformRandomProvider urp) {
    final double dp = 0.9;
    return new GeneratorBase ("fromDoubleGenerator") {
      private final ContinuousSampler choose =
        new ContinuousUniformSampler(urp,0.0,1.0);
      private final Generator g = Doubles.finiteGenerator(urp);
      private final CollectionSampler edgeCases =
        new CollectionSampler(
          urp,
          List.of(
            BoundedNatural.valueOf(0L),
            BoundedNatural.valueOf(1L),
            BoundedNatural.valueOf(2L),
            BoundedNatural.valueOf(10L),
            BoundedNatural.valueOf(-1L)));
      @Override
      public Object next () {
        final boolean edge = choose.sample() > dp;
        if (edge) { return edgeCases.sample(); }
        return BoundedNatural.valueOf(g.nextDouble()); } }; }

  // Is this characteristic of most inputs?
  public static final Generator
  generator (final UniformRandomProvider urp) {
    return fromDoubleGenerator(urp); }

  public static final Generator
  fromDoubleGenerator (final int n,
                       final UniformRandomProvider urp) {
    return new GeneratorBase ("fromDoubleGenerator:" + n) {
      final Generator g = fromDoubleGenerator(urp);
      @Override
      public final Object next () {
        final BoundedNatural[] z = new BoundedNatural[n];
        for (int i=0;i<n;i++) { z[i] = (BoundedNatural) g.next(); }
        return z; } }; }

  public static final Generator
  generator (final int n,
             final UniformRandomProvider urp) {
    return new GeneratorBase ("rationalGenerator:" + n) {
      final Generator g = generator(urp);
      @Override
      public final Object next () {
        final BoundedNatural[] z = new BoundedNatural[n];
        for (int i=0;i<n;i++) { z[i] = (BoundedNatural) g.next(); }
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
    return that instanceof BoundedNaturals; }

  @Override
  public final String toString () { return "BF"; }

  //--------------------------------------------------------------
  // construction
  //--------------------------------------------------------------

  private BoundedNaturals () { }

  private static final BoundedNaturals SINGLETON = new BoundedNaturals();

  public static final BoundedNaturals get () { return SINGLETON; }

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
