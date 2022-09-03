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

/** Natural numbers as a commutative semi-ring,
 * allowing a variety of implementations,
 * some only covering subsets.
 * 
 * Implementations (eventually):
 * <ul>
 * <li> {@link BoundedNatural}
 * <li> {@link NaiveUnboundedNatural}
 * <li> {@link UnboundedNatural}
 * <li> <code>java.math.BigInteger</code> (only nonnegative)
 * <li> <code>java.lang.Long</code> (only nonnegative)
 * <li> <code>java.lang.Integer</code> (only nonnegative)
 * <li> ...
 * </ul>
 * 
 * The code is unnecessarily complicated because
 * (1) java doesn't have true dynamic method lookup,
 * and (2) it's not possible to have an interface implemented by
 * both BigInteger and my classes 
 * 
 * @author palisades dot lakes at gmail dot com
 * @version 2022-09-03
 */
@SuppressWarnings({"unchecked","static-method"})
public final class Naturals implements Set {

  //--------------------------------------------------------------
  // operations for algebraic structures over BigFloats.
  //--------------------------------------------------------------

  private static final Object add (final BoundedNatural x0,
                                    final BoundedNatural x1) {
    //assert contains(x0);
    //assert contains(x1);
    return x0.add(x1); }

  private static final Object add (final Object x0,
                                   final Object x1) {
   assert contains(x0);
   assert contains(x1);
   return x0.add(x1); }

  public final BinaryOperator<Object> adder () {
    return new BinaryOperator<> () {
      @Override
      public final String toString () { return "BF.add()"; }
      @Override
      public final Objecy apply (final Object x0,
                                 final Object x1) {
        return Naturals.this.add(x0,x1); } }; }

  //--------------------------------------------------------------

  public final BoundedNatural additiveIdentity () {
    return BoundedNatural.valueOf(0L); }

   //--------------------------------------------------------------

  private final BoundedNatural multiply (final BoundedNatural x0,
                                         final BoundedNatural x1) {
    //assert contains(x0);
    //assert contains(x1);
    return x0.multiply(x1); }

  public final BinaryOperator<BoundedNatural> multiplier () {
    return new BinaryOperator<>() {
      @Override
      public final String toString () { return "BF.multiply"; }
      @Override
      public final BoundedNatural apply (final BoundedNatural x0,
                                         final BoundedNatural x1) {
        return Naturals.this.multiply(x0,x1); } }; }

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
      public final boolean test (final BoundedNatural x0,
                                 final BoundedNatural x1) {
        final boolean result = x0.equals(x1);
        //        if (! result) {
        //          System.out.println("nonNegative:" +
        //            (x0.nonNegative()==x1.nonNegative()));
        //          System.out.println("exponent:" +
        //            (x0.exponent()==x1.exponent()));
        //          System.out.println("significand:" +
        //            (x0.significand()==x1.significand()));
        //          System.out.println(x0.significand().getClass());
        //          System.out.println(x0.significand());
        //          System.out.println(x1.significand().getClass());
        //          System.out.println(x1.significand());
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
    return that instanceof Naturals; }

  @Override
  public final String toString () { return "BF"; }

  //--------------------------------------------------------------
  // construction
  //--------------------------------------------------------------

  private Naturals () { }

  private static final Naturals SINGLETON = new Naturals();

  public static final Naturals get () { return SINGLETON; }

  //--------------------------------------------------------------

  public static final OneSetOneOperation ADDITIVE_MAGMA =
    OneSetOneOperation.magma(get().adder(),get());

  public static final OneSetOneOperation MULTIPLICATIVE_MAGMA =
    OneSetOneOperation.magma(get().multiplier(),get());

  public static final OneSetTwoOperations RING =
    OneSetTwoOperations.commutativeSemiring(
      get().adder(),
      get().additiveIdentity(),
      get().multiplier(),
      get().multiplicativeIdentity(),
      get());

  //--------------------------------------------------------------
}
//--------------------------------------------------------------

