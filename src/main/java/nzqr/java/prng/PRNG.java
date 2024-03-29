package nzqr.java.prng;

import org.apache.commons.rng.UniformRandomProvider;
import org.apache.commons.rng.simple.RandomSource;

import nzqr.java.Exceptions;

/** Providers for pseudo-random number generators.
 *
 * Static methods only; no state.
 *
 * @author palisades dot lakes at gmail dot com
 * @version 2022-07-31
 */

public final class PRNG {

  //--------------------------------------------------------------
  // providers
  //--------------------------------------------------------------
  /** http://www.iro.umontreal.ca/~panneton/WELLRNG.html
   */

  public static final UniformRandomProvider
  well44497b (final int[] seed) {
    assert 1391 == seed.length;
    return RandomSource.WELL_44497_B.create(seed); }

  public static final UniformRandomProvider
  well44497b (final String seed) {
    return well44497b(Seeds.seed(seed)); }

  public static final UniformRandomProvider
  well44497b (final Object seed) {
    if (seed instanceof int[]) {
      return well44497b((int[]) seed); }
    if (seed instanceof String)  {
      return well44497b((String) seed); }
    throw Exceptions.unsupportedOperation(
      null,"well44497b",seed); }

  //--------------------------------------------------------------

  public static final UniformRandomProvider
  mersenneTwister (final int[] seed) {
    assert 624 == seed.length;
    return RandomSource.MT.create(seed); }

  public static final UniformRandomProvider
  mersenneTwister (final String resource) {
    return mersenneTwister(Seeds.seed(resource)); }

  public static final UniformRandomProvider
  mersenneTwister (final Object seed) {
    if (seed instanceof int[]) {
      return mersenneTwister((int[]) seed); }
    if (seed instanceof String)  {
      return mersenneTwister((String) seed); }
    throw Exceptions.unsupportedOperation(
      null,"mersenneTwister",seed); }

  //--------------------------------------------------------------
  // disable constructor
  //--------------------------------------------------------------

  private PRNG () {
    throw new UnsupportedOperationException(
      "can't instantiate " + getClass()); }

  //--------------------------------------------------------------
}
//--------------------------------------------------------------
