package nzqr.java.numbers;

import static nzqr.java.numbers.Numbers.hiWord;
import static nzqr.java.numbers.Numbers.unsigned;

import java.util.List;
import java.util.Map;
import java.util.function.BinaryOperator;
import java.util.function.Supplier;

import org.apache.commons.rng.UniformRandomProvider;
import org.apache.commons.rng.sampling.CollectionSampler;

import nzqr.java.algebra.OneSetOneOperation;
import nzqr.java.algebra.Set;
import nzqr.java.prng.Generator;
import nzqr.java.prng.GeneratorBase;
import nzqr.java.prng.Generators;

/** A proof-of-concept implementation of unbounded natural
 * numbers. Only implementing a commutative monoid
 * (ie just addition) for now.
 *
 * This is in contrast to {@link BoundedNatural} and
 * {@link java.math.BigInteger}, which both have bounded ranges,
 * limited, for one thing, by the fact that bits are addressable
 * by <code>int</code>.
 *
 * @author palisades dot lakes at gmail dot com
 * @version 2022-07-31
 */

@SuppressWarnings("unchecked")
public final class NaiveUnboundedNatural
implements Comparable<NaiveUnboundedNatural> {

  //--------------------------------------------------------------
  // an unbounded immutable sequence of ints
  //--------------------------------------------------------------
  // TODO: Any value to implementing Iterator<Integer> or
  // PrimitiveIterator.OfInt?
  //
  // TODO: What about zero-length sequences?
  // Is there a better choice than just null?
  //
  // TODO: could do without this class; have the U.N. be the
  // sequence. Then next/rest would be shifting down 32 bits?

  private static record Words (int word, Words next) { }

  /** Will return <code>null</code> if <code>s</code> is
   * <code>null</code>.
   */
  private static final Words reverse (final Words s) {
    if (null == s) { return null; }
    Words out = new Words(s.word,null);
    Words in = s.next;
    while (null != in) {
      out = new Words(in.word,out);
      in = in.next; }
    return out; }

  //--------------------------------------------------------------
  // fields
  //--------------------------------------------------------------
  /** Least significant word first. Never modified.
   */

  private final Words words;

  /** Singleton. */
  public static final NaiveUnboundedNatural ZERO =
    new NaiveUnboundedNatural(null);

  /** Singleton. */
  public static final NaiveUnboundedNatural ONE =
    new NaiveUnboundedNatural(new Words(1,null));

  //--------------------------------------------------------------
  // monoid operation
  //--------------------------------------------------------------

  public final NaiveUnboundedNatural add (final NaiveUnboundedNatural u) {
    Words tt = words;
    Words uu = u.words;
    Words vv = null;
    long sum = 0L;
    for (;(null!=tt)&&(null!=uu);
      tt = tt.next,uu = uu.next) {
      sum += unsigned(tt.word) + unsigned(uu.word);
      vv = new Words((int) sum,vv);
      sum = hiWord(sum); }
    if (null==tt) {
      for (;null!=uu;uu=uu.next) {
        sum += unsigned(uu.word);
        vv = new Words((int) sum,vv);
        sum = hiWord(sum); } }
    else {
      for (;null!=tt;tt=tt.next) {
        sum += unsigned(tt.word);
        vv = new Words((int) sum,vv);
        sum = hiWord(sum); } }
    if (0L!=sum) { vv = new Words(1,vv); }
    return new NaiveUnboundedNatural(reverse(vv)); }

  //--------------------------------------------------------------
  // Comparable
  //--------------------------------------------------------------

  @Override
  public final int compareTo (final NaiveUnboundedNatural u) {
    Words tt = words;
    Words uu = u.words;
    int result = 0;
    for (;(null!=tt)&&(null!=uu); tt=tt.next,uu=uu.next) {
      final int c = Integer.compareUnsigned(tt.word,uu.word);
      if (c<0) { result = -1; }
      else if (c>0) { result = 1; } }
    if (null==tt) {
      for (;null!=uu;uu=uu.next) {
        if (0!=uu.word) { return -1; } } }
    else {
      for (;null!=tt;tt=tt.next) {
        if (0!=tt.word) { return 1; } } }
    return result; }

  //--------------------------------------------------------------
  // Object methods
  //--------------------------------------------------------------

  @Override
  public final int hashCode () {
    final int prime = 31;
    int c = 1;
    final Words tt = words;
    while (null != tt) {
      c = (int) ((prime * c) + unsigned(tt.word)); }
    return c; }

  @Override
  public final boolean equals (final Object x) {
    if (x==this) { return true; }
    if (!(x instanceof NaiveUnboundedNatural)) { return false; }
    final NaiveUnboundedNatural u = (NaiveUnboundedNatural) x;
    Words tt = words;
    Words uu = u.words;
    while ((null!=tt)&&(null!=uu)) {
      final int ti = tt.word;
      final int ui = uu.word;
      if (ti!=ui) { return false; }
      tt = tt.next;
      uu = uu.next; }
    if (null==tt) {
      while (null!=uu) {
        if (0!=uu.word) { return false; }
        uu = uu.next; } }
    else {
      while (null!=tt) {
        if (0!=tt.word) { return false; }
        tt = tt.next; } }
    return true; }

  //--------------------------------------------------------------
  // "random" instances for testing, etc.
  //--------------------------------------------------------------

  /** Generate an {@link NaiveUnboundedNatural} with <code>n</code>
   * random <code>int</code> words.
   */

  public static final Generator
  randomBitsGenerator (final long n,
                       final UniformRandomProvider urp) {
    final Generator ig = Generators.intGenerator(urp);
    return new GeneratorBase ("UnboundedNaturalRandomBits:" + n) {
      @Override
      public final Object next () {
        Words w = null;
        for (long i=0;i<n;i++) { w = new Words(ig.nextInt(),w); }
        return new NaiveUnboundedNatural(w); } }; }

  /** Intended primarily for testing.
   * For now, just a relatively small number of random bits.
   * Not large enough to test unboundedness compared to
   * {@link BoundedNatural}, but large examples take too long to
   * run as unit tests, cause OOM, etc..
   */

  public static final Generator
  generator (final UniformRandomProvider urp)  {
    final Generator g0 =
      randomBitsGenerator (1L,urp);
    final Generator g1 =
      randomBitsGenerator (2L,urp);
    final Generator g2 =
      randomBitsGenerator (4L,urp);
    //    final Generator g3 =
    //      randomBitsGenerator (1L+BoundedNatural.MAX_WORDS,urp);
    final CollectionSampler gs =
      new CollectionSampler(urp,List.of(
        g0,
        g1,
        g2
        //,g3
        ));
    return new GeneratorBase ("UnboundedNaturalRandomBits") {
      @Override
      public final Object next () {
        return ((Generator) gs.sample()).next();} }; }

  //--------------------------------------------------------------
  // construction
  //-------------------------------------------------------------

  private NaiveUnboundedNatural (final Words w) { words = w; }

  public static final NaiveUnboundedNatural
  valueOf (final BoundedNatural u) {
    final int n = u.hiInt();
    Words r = null;
    for (int i=0;i<n;i++) { r = new Words(u.word(i),r); }
    return new NaiveUnboundedNatural(reverse(r)); }

  //--------------------------------------------------------------
  // mathematical structures using NaiveUnboundedNatural
  //--------------------------------------------------------------
  /** Contains all instances of {@link NaiveUnboundedNatural}.
   * Could be extended to include BoundedNatural,
   * all non-negative integer values, etc.,
   * but not necessary for proof of concept.
   */

  public static final Set SET = new Set() {
    @Override
    @SuppressWarnings("unused")
    public boolean contains (final Object element) {
      return element instanceof NaiveUnboundedNatural; }
    @Override
    public final Supplier generator (final Map options) {
      final UniformRandomProvider urp = Set.urp(options);
      final Generator g = NaiveUnboundedNatural.generator(urp);
      return
        new Supplier () {
        @Override
        public final Object get () { return g.next(); } }; }
  };

  public static final BinaryOperator<NaiveUnboundedNatural> adder () {
    return new BinaryOperator<> () {
      @Override
      public final String toString () {
        return "NaiveUnboundedNatural.add(NaiveUnboundedNatural)"; }
      @Override
      public final NaiveUnboundedNatural
      apply (final NaiveUnboundedNatural q0,
             final NaiveUnboundedNatural q1) {
        return q0.add(q1); } }; }

  public static final OneSetOneOperation MONOID =
    OneSetOneOperation.commutativeMonoid(adder(),SET,ZERO);

  //--------------------------------------------------------------
}
//--------------------------------------------------------------
