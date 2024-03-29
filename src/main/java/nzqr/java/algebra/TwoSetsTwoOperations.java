package nzqr.java.algebra;
/* unused, broken, replaced by LinearSpaceLike */
//import java.util.List;
//import java.util.Map;
//import java.util.Objects;
//import java.util.function.BiFunction;
//import java.util.function.BiPredicate;
//import java.util.function.Predicate;
//import java.util.function.Supplier;
//
//import org.apache.commons.rng.UniformRandomProvider;
//
//import com.carrotsearch.hppc.IntObjectHashMap;
//import com.carrotsearch.hppc.IntObjectMap;
//
//import nzqr.java.linear.BigFractionsN;

/** Module-like structures, including linear (vector) spaces.
 * Two sets, 'elements' and 'scalars'.
 * Two operations: element 'addition' and
 * 'multiplication' of elements by scalars.
 * The scalars are (usually) an instance of some one set two
 * operation structure, like a ring or a field.
 * What kind of module-like structure an instance is is determined
 * by the laws satisfied by the element-element operation,
 * the operations on the scalar structure, and, less often,
 * by scalar-element operation.
 *
 * It is nearly always assumed that scalar multiplication
 * distributes over element addition:
 * <code>a*(v+w) = (a*v) + (a*w)</code>.
 *
 * Note that this doesn't work if we want to generalize
 * linear to affine spaces, etc.
 *
 * @author palisades dot lakes at gmail dot com
 * @version 2019-01-29
 */
@SuppressWarnings("unchecked")
public final class TwoSetsTwoOperations implements Set {

  //  // two structures
  //  private final Set _elements;
  //  private final Set _scalars;
  //
  //  //operation
  //  private final BiFunction _scale;
  //
  //  //--------------------------------------------------------------
  //  // methods
  //  //--------------------------------------------------------------
  //
  //  /** Multiply an element by a scalar. */
  //  public final BiFunction multiply () { return _scale; }
  //
  //  /** Typically a group-like structure. */
  //  public final Set elements () { return _elements; }
  //
  //  /** Typically a ring (giving a module) or a field (giving a
  //   * vector/linear space).
  //   */
  //  public final Set scalars () { return _scalars; }
  //
  //  //--------------------------------------------------------------
  //  // laws for some specific algebraic structures, for testing
  //
  //  public final List<BiPredicate> moduleLaws () {
  //    return
  //      Laws.module(
  //        multiply(),
  //        (OneSetOneOperation) elements(),
  //        (OneSetTwoOperations) scalars()); }
  //
  //  public final List<Predicate> linearspaceLaws () {
  //    return
  //      Laws.linearspace(
  //        multiply(),
  //        (OneSetOneOperation) elements(),
  //        (OneSetTwoOperations) scalars()); }
  //
  //  //--------------------------------------------------------------
  //  // Set methods
  //  //--------------------------------------------------------------
  //
  //  @Override
  //  public final boolean contains (final Object x) {
  //    return _elements.contains(x); }
  //
  //  @Override
  //  public final BiPredicate equivalence () {
  //    return _elements.equivalence(); }
  //
  //  @Override
  //  public final Supplier generator (final UniformRandomProvider prng,
  //                                   final Map options) {
  //    return _elements.generator(prng,options); }
  //
  //  //--------------------------------------------------------------
  //  // Object methods
  //  //--------------------------------------------------------------
  //
  //  @Override
  //  public final int hashCode () {
  //    return
  //      Objects.hash(
  //        multiply(),
  //        elements(),
  //        scalars()); }
  //
  //  @Override
  //  public boolean equals (Object obj) {
  //    if (this == obj) return true;
  //    if (obj == null) return false;
  //    if (getClass() != obj.getClass()) return false;
  //    final TwoSetsTwoOperations other = (TwoSetsTwoOperations) obj;
  //    if (! Objects.equals(multiply(),other.multiply())) {
  //      return false; }
  //    if (! Objects.equals(elements(),other.elements())) {
  //      return false; }
  //    if (! Objects.equals(scalars(),other.scalars())) {
  //      return false; }
  //    return true; }
  //
  //
  //  @Override
  //  public final String toString () {
  //    return "S2O2[" +
  //      //",\n" + multiply() + "," +
  //      elements() + "," + scalars() +
  //      "]"; }
  //
  //  //--------------------------------------------------------------
  //  // construction
  //  //--------------------------------------------------------------
  //
  //
  //  private TwoSetsTwoOperations (final BiFunction multiply,
  //                                final Set elements,
  //                                final Set scalars) {
  //    assert Objects.nonNull(multiply);
  //    assert Objects.nonNull(elements);
  //    assert Objects.nonNull(scalars);
  //    _scale = multiply;
  //    _elements = elements;
  //    _scalars = scalars; }
  //
  //  //--------------------------------------------------------------
  //
  //  public static final TwoSetsTwoOperations
  //  make (final BiFunction multiply,
  //        final Set elements,
  //        final Set scalars) {
  //
  //    return new TwoSetsTwoOperations(
  //      multiply,
  //      elements,
  //      scalars); }
  //
  //  //--------------------------------------------------------------
  //  // TODO: should this be its own class?
  //
  //  /** n-dimensional rational vector space, implemented with
  //   * <code>BigFraction[n]</code>.
  //   */
  //
  //  private static final TwoSetsTwoOperations makeBFn (final int n) {
  //    return
  //      TwoSetsTwoOperations.make(
  //        BigFractionsN.scaler(n),
  //        OneSetOneOperation.bigFractionsNGroup(n),
  //        OneSetTwoOperations.BIGFRACTIONS_FIELD); }
  //
  //  private static final IntObjectMap<TwoSetsTwoOperations>
  //  _bfnCache = new IntObjectHashMap();
  //
  //  /** n-dimensional rational vector space, implemented with
  //   * <code>BigFraction[]</code>.
  //   */
  //  public static final TwoSetsTwoOperations getBFn (final int dimension) {
  //    final TwoSetsTwoOperations qn0 = _bfnCache.get(dimension);
  //    if (null != qn0) { return qn0; }
  //    final TwoSetsTwoOperations qn1 = makeBFn(dimension);
  //    _bfnCache.put(dimension,qn1);
  //    return qn1; }
  //
  //  //--------------------------------------------------------------
  //  // TODO: should this be its own class?
  //
  //  /** n-dimensional rational vector space, implemented with
  //   * any known rational array.
  //   */
  //
  //  private static final TwoSetsTwoOperations makeQn (final int n) {
  //    return
  //      TwoSetsTwoOperations.make(
  //        Qn.scaler(n),
  //        OneSetOneOperation.qnGroup(n),
  //        OneSetTwoOperations.Q_FIELD); }
  //
  //  private static final IntObjectMap<TwoSetsTwoOperations>
  //  _qnCache = new IntObjectHashMap();
  //
  //  /** n-dimensional rational vector space, implemented with
  //   * <code>BigFraction[]</code>.
  //   */
  //  public static final TwoSetsTwoOperations getQn (final int dimension) {
  //    final TwoSetsTwoOperations qn0 = _qnCache.get(dimension);
  //    if (null != qn0) { return qn0; }
  //    final TwoSetsTwoOperations qn1 = makeQn(dimension);
  //    _qnCache.put(dimension,qn1);
  //    return qn1; }

  //--------------------------------------------------------------
}
