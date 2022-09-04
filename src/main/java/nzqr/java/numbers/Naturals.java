package nzqr.java.numbers;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.function.BiPredicate;
import java.util.function.BinaryOperator;
import java.util.function.Supplier;

import org.apache.commons.rng.UniformRandomProvider;
import org.apache.commons.rng.sampling.CollectionSampler;

import nzqr.java.algebra.OneSetOneOperation;
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
 * both BigInteger and newly written classes.
 * 
 * @author palisades dot lakes at gmail dot com
 * @version 2022-09-04
 */
@SuppressWarnings({"unchecked","static-method","preview"})
public final class Naturals implements Set {

  //--------------------------------------------------------------
  // operations for algebraic structures over BigFloats.
  //--------------------------------------------------------------
  // TODO: How do (should) we handle unsigned interpretation of an 
  // int's bits?
  // TODO: cleaner handling of overflow to BigInteger or whatever

  /** UNSAFE: Assumes all arguments are non-negative. */
  @SuppressWarnings({ "preview", "boxing" })
  private static final Object add (final Byte y0,
                                   final Object x1) {
    // NOTE: instanceof pattern matching in java 18 is a preview
    // may need to be re-written as if-then-else cascade...
    switch (x1) {
    case Integer y1 : return y0 + ((long) y1);
    case Long y1 :
      try { return Math.addExact(y0,y1); }
      catch (final ArithmeticException e) {
        return BigInteger.valueOf(y1).add(BigInteger.valueOf(y0)); } 
    case Short y1 : return y0 + ((long) y1);
    case Byte y1 : return y0 + ((long) y1);
    case BigInteger y1 : return y1.add(BigInteger.valueOf(y0));
    default : throw new UnsupportedOperationException(); } }

  /** UNSAFE: Assumes all arguments are non-negative. */
  @SuppressWarnings({ "preview", "boxing" })
  private static final Object add (final Short y0,
                                   final Object x1) {
    // NOTE: instanceof pattern matching in java 18 is a preview
    // may need to be re-written as if-then-else cascade...
    switch (x1) {
    case Integer y1 : return y0 + ((long) y1);
    case Long y1 :
      try { return Math.addExact(y0,y1); }
      catch (final ArithmeticException e) {
        return BigInteger.valueOf(y1).add(BigInteger.valueOf(y0)); } 
    case Short y1 : return y0 + ((long) y1);
    case Byte y1 : return y0 + ((long) y1);
    case BigInteger y1 : return y1.add(BigInteger.valueOf(y0));
    default : throw new UnsupportedOperationException(); } }

  /** UNSAFE: Assumes all arguments are non-negative. */
  @SuppressWarnings({ "preview", "boxing" })
  private static final Object add (final Integer y0,
                                   final Object x1) {
    // NOTE: instanceof pattern matching in java 18 is a preview
    // may need to be re-written as if-then-else cascade...
    switch (x1) {
    case Integer y1 : return y0 + ((long) y1);
    case Long y1 :
      try { return Math.addExact(y0,y1); }
      catch (final ArithmeticException e) {
        return BigInteger.valueOf(y1).add(BigInteger.valueOf(y0)); } 
    case Short y1 : return y0 + ((long) y1);
    case Byte y1 : return y0 + ((long) y1);
    case BigInteger y1 : return y1.add(BigInteger.valueOf(y0));
    default : throw new UnsupportedOperationException(); } }


  /** UNSAFE: Assumes all arguments are non-negative. */
  @SuppressWarnings({ "preview", "boxing" })
  private static final Object add (final Long y0,
                                   final Object x1) {
    // NOTE: instanceof pattern matching in java 18 is a preview
    // may need to be re-written as if-then-else cascade...
    switch (x1) {
    case Integer y1 :
      try { return Math.addExact(y0,y1); }
      catch (final ArithmeticException e) {
        return BigInteger.valueOf(y1).add(BigInteger.valueOf(y0)); } 
    case Long y1 :
      try { return Math.addExact(y0,y1); }
      catch (final ArithmeticException e) {
        return BigInteger.valueOf(y1).add(BigInteger.valueOf(y0)); } 
    case Short y1 : 
      try { return Math.addExact(y0,y1); }
      catch (final ArithmeticException e) {
        return BigInteger.valueOf(y1).add(BigInteger.valueOf(y0)); } 
    case Byte y1 :     
      try { return Math.addExact(y0,y1); }
      catch (final ArithmeticException e) {
        return BigInteger.valueOf(y1).add(BigInteger.valueOf(y0)); } 
    case BigInteger y1 : return y1.add(BigInteger.valueOf(y0));
    default : throw new UnsupportedOperationException(); } }


  /** UNSAFE: Assumes all arguments are non-negative. */
  @SuppressWarnings({ "preview", "boxing" })
  private static final Object add (final BigInteger y0,
                                   final Object x1) {
    // NOTE: instanceof pattern matching in java 18 is a preview
    // may need to be re-written as if-then-else cascade...
    return switch (x1) {
    case Integer y1 -> y0.add(BigInteger.valueOf(y1));
    case Long y1 -> y0.add(BigInteger.valueOf(y1));
    case Short y1 -> y0.add(BigInteger.valueOf(y1));
    case Byte y1 -> y0.add(BigInteger.valueOf(y1));
    case BigInteger y1 -> y1.add(y0);
    default -> throw new UnsupportedOperationException(); }; }

  @SuppressWarnings("preview")
  private final Object add (final Object x0,
                            final Object x1) {
    assert contains(x0);
    assert contains(x1);
    // NOTE: instanceof pattern matching in java 18 is a preview
    // may need to be re-written as if-then-else cascade...
    return switch (x0) {
    case Integer y0 -> add(y0,x1);
    case Long y0 -> add(y0,x1);
    case Short y0 -> add(y0,x1);
    case Byte y0 -> add(y0,x1);
    case BigInteger y0 -> add(y0,x1);
    default -> throw new UnsupportedOperationException(); }; }

  //--------------------------------------------------------------

  public final BinaryOperator<Object> adder () {
    return new BinaryOperator<> () {
      @Override
      public final String toString () { return "Naturals.add()"; }
      @Override
      public final Object apply (final Object x0,
                                 final Object x1) {
        return Naturals.this.add(x0,x1); } }; }

  //--------------------------------------------------------------

  public final Object additiveIdentity () {
    return Integer.valueOf(0); }

  //--------------------------------------------------------------
  // TODO: implement these to define the commutative semi-ring.

  //  private final Object multiply (final Object x0,
  //                                         final Object x1) {
  //    //assert contains(x0);
  //    //assert contains(x1);
  //    return x0.multiply(x1); }
  //
  //  public final BinaryOperator<Object> multiplier () {
  //    return new BinaryOperator<>() {
  //      @Override
  //      public final String toString () { return "BF.multiply"; }
  //      @Override
  //      public final Object apply (final Object x0,
  //                                         final Object x1) {
  //        return Naturals.this.multiply(x0,x1); } }; }
  //
  //  public final Object multiplicativeIdentity () {
  //    return Object.valueOf(1L); }

  //--------------------------------------------------------------
  // Set methods
  //--------------------------------------------------------------

  @Override
  @SuppressWarnings({ "preview", "boxing" })
  public final boolean contains (final Object x) {
    return switch (x) {
    case Integer y -> y>=0;
    case Long y -> y>=0;
    case Short y -> y>=0;
    case Byte y -> y>=0;
    // TODO: is signum() better for all the integer classes?
    // TODO: might be useful to define signum/isNegative/... for 
    // all Number classes.
    case BigInteger y -> y.signum()>=0;
    default -> throw new UnsupportedOperationException(); }; }

  //--------------------------------------------------------------
  // Unfortunately, it looks like Number.equals() is only true
  // for args of the same class, rather than same value.
  // Would it be better/faster is everything was cast to long
  // before comparison?

  /** Test for equal value as Natural numbers.
   * 
   * UNSAFE: Assumes all arguments are non-negative. 
   */
  @SuppressWarnings({ "preview", "boxing" })
  private final boolean equals (final Integer y0,
                                final Object x1) {
    return switch (x1) {
    case Integer y1 -> y0.equals(y1);
    case Long y1 ->  y1.equals((long) y0);
    case Short y1 ->  y0.equals((int) y1);
    case Byte y1 ->  y0.equals((int) y1);
    case BigInteger y1 ->  y1.equals(BigInteger.valueOf(y0));
    default -> throw new UnsupportedOperationException(); }; }

  /** Test for equal value as Natural numbers.
   * 
   * UNSAFE: Assumes all arguments are non-negative. 
   */
  @SuppressWarnings({ "preview", "boxing" })
  private final boolean equals (final Long y0,
                                final Object x1) {
    return switch (x1) {
    case Integer y1 -> y0.equals((long) y1);
    case Long y1 ->  y0.equals(y1);
    case Short y1 ->  y0.equals((long) y1);
    case Byte y1 ->  y0.equals((long) y1);
    case BigInteger y1 ->  y1.equals(BigInteger.valueOf(y0));
    default -> throw new UnsupportedOperationException(); }; }

  /** Test for equal value as Natural numbers.
   * 
   * UNSAFE: Assumes all arguments are non-negative. 
   */
  @SuppressWarnings({ "preview", "boxing" })
  private final boolean equals (final Short y0,
                                final Object x1) {
    return switch (x1) {
    case Integer y1 -> y1.equals((int) y0);
    case Long y1 ->  y1.equals((long) y0);
    case Short y1 ->  y0.equals(y1);
    case Byte y1 ->  y0.equals((short) y1);
    case BigInteger y1 ->  y1.equals(BigInteger.valueOf(y0));
    default -> throw new UnsupportedOperationException(); }; }

  /** Test for equal value as Natural numbers.
   * 
   * UNSAFE: Assumes all arguments are non-negative. 
   */
  @SuppressWarnings({ "preview", "boxing" })
  private final boolean equals (final Byte y0,
                                final Object x1) {
    return switch (x1) {
    case Integer y1 -> y1.equals((int) y0);
    case Long y1 ->  y1.equals((long) y0);
    case Short y1 ->  y1.equals((short) y0);
    case Byte y1 ->  y0.equals(y1);
    case BigInteger y1 ->  y1.equals(BigInteger.valueOf(y0));
    default -> throw new UnsupportedOperationException(); }; }

  /** Test for equal value as Natural numbers.
   * 
   * UNSAFE: Assumes all arguments are non-negative. 
   */
  @SuppressWarnings({ "preview", "boxing" })
  private final boolean equals (final BigInteger y0,
                                final Object x1) {
    return switch (x1) {
    case Integer y1 -> y0.equals(BigInteger.valueOf(y1));
    case Long y1 -> y0.equals(BigInteger.valueOf(y1));
    case Short y1 ->  y0.equals(BigInteger.valueOf(y1));
    case Byte y1 ->  y0.equals(BigInteger.valueOf(y1));
    case BigInteger y1 -> y0.equals(y1);
    default -> throw new UnsupportedOperationException(); }; }

  /** Test for equal value as Natural numbers. */

  @SuppressWarnings("preview")
  private final boolean equals (final Object x0,
                                final Object x1) {
    assert contains(x0);
    assert contains(x1);
    return switch (x0) {
    case Integer y0 -> equals(y0,x1);
    case Long y0 -> equals(y0,x1);
    case Short y0 -> equals(y0,x1);
    case Byte y0 -> equals(y0,x1);
    case BigInteger y0 -> equals(y0,x1);
    default -> 
    throw new UnsupportedOperationException(
      x0.getClass().getName() + " " + x1.getClass().getName()); }; }

  //--------------------------------------------------------------

  /** Test for equal value as Natural numbers. */

  @Override
  public final BiPredicate equivalence () {
    return new BiPredicate<Object,Object>() {
      @Override
      public final boolean test (final Object x0,
                                 final Object x1) {
        return get().equals(x0,x1); } }; }

  //--------------------------------------------------------------

  public static final Generator
  generator (final UniformRandomProvider urp)  {
    //    final Generator g3 =
    //      randomBitsGenerator (1L+BoundedNatural.MAX_WORDS,urp);
    final CollectionSampler gs =
      new CollectionSampler(urp,List.of(
        Generators.nonNegativeBigIntegerGenerator(urp),
        Generators.nonNegativeByteGenerator(urp),
        Generators.nonNegativeShortGenerator(urp),
        Generators.nonNegativeIntGenerator(urp),
        Generators.nonNegativeLongGenerator(urp)));
    return new GeneratorBase ("NaturalsGenerator") {
      @Override
      public final Object next () {
        final Generator g = ((Generator) gs.sample());
        return g.next();} }; }

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
  public final String toString () { return "Naturals"; }

  //--------------------------------------------------------------
  // construction
  //--------------------------------------------------------------

  private Naturals () { }

  private static final Naturals SINGLETON = new Naturals();

  public static final Naturals get () { return SINGLETON; }

  //--------------------------------------------------------------
  // TODO: fill in multiply methods for the commutative semi-ring

  public static final OneSetOneOperation ADDITION_MONOID =
    OneSetOneOperation.commutativeMonoid(
      get().adder(),
      get(),
      get().additiveIdentity());

  //  public static final OneSetOneOperation MULTIPLICATIVE_MAGMA =
  //    OneSetOneOperation.magma(get().multiplier(),get());

  //  public static final OneSetTwoOperations RING =
  //    OneSetTwoOperations.commutativeSemiring(
  //      get().adder(),
  //      get().additiveIdentity(),
  //      get().multiplier(),
  //      get().multiplicativeIdentity(),
  //      get());

  //--------------------------------------------------------------
}
//--------------------------------------------------------------

