package nzqr.java.accumulators;

/** Basre class for some exact accumulators.
 * <p>
 * Use twoAdd and twoMul to convert operations to sequence of
 * adds, so that they are exact if {@link #add(double)} is.
 *
 * @author palisades dot lakes at gmail dot com
 * @version 2019-09-06
 */
@SuppressWarnings("unchecked")
public abstract class ExactAccumulator<T extends ExactAccumulator>
implements Accumulator<T> {

  //--------------------------------------------------------------

  @Override
  public final boolean isExact () { return true; }

  @Override
  public T add2 (final double z) {
    // WARNING: WRONG: only works when
    // 2*exponent(z) >= min exponent + p -1
    // where
    //assert Double.isFinite(z);
    // preserve exactness using twoMul to convert to 2 adds.
    final double zz = z*z;
    final double e = Math.fma(z,z,-zz);
    return (T) add(zz).add(e); }

  @Override
  public T addProduct (final double z0,
                       final double z1) {
    //assert Double.isFinite(z0);
    //assert Double.isFinite(z1);
    // preserve exactness using twoMul to convert to 2 adds.
    final double zz = z0*z1;
    final double e = Math.fma(z0,z1,-zz);
    return (T) add(zz).add(e); }

  @Override
  public T addL1 (final double z0,
                  final double z1) {
    //assert Double.isFinite(z0);
    //assert Double.isFinite(z1);
    // twoAdd -> 2 adds.
    final double zz = z0-z1;
    final double dz = zz-z0;
    final double e = (z0-(zz-dz)) + ((-z1)-dz);
    if (0<=zz) {
      if (0<=e) { add(zz); add(e); }
      else if (Math.abs(e)<=Math.abs(zz)) { add(zz); add(e); }
      else { add(-zz); add(-e); } }
    else {
      if (0>e) { add(-zz); add(-e); }
      else if (Math.abs(e)<=Math.abs(zz)) { add(-zz); add(-e); }
      else { add(zz); add(e); } }
    return (T) this; }

  @Override
  public T addL2 (final double z0,
                  final double z1) {
    //assert Double.isFinite(z0);
    //assert Double.isFinite(z1);
    // twoAdd, twoMul -> 8 adds.
    // twoAdd (twoSub):
    final double zz = z0-z1;
    final double dz = zz-z0;
    final double e = (z0-(zz-dz)) + ((-z1)-dz);
    // twoMul:
    final double zzzz = zz*zz;
    final double ezzzz = Math.fma(zz,zz,-zzzz);
    add(zzzz);
    add(ezzzz);
    // twoMul:
    final double ezz = e*zz;
    final double eezz = Math.fma(e,zz,-ezz);
    add(ezz); add(ezz);
    add(eezz); add(eezz);
    // twoMul:
    final double ee = e*e;
    final double eee = Math.fma(e,e,-ee);
    add(ee);
    add(eee);
    return (T) this; }

  //--------------------------------------------------------------
  // construction
  //--------------------------------------------------------------

  protected ExactAccumulator () { super(); }

  //--------------------------------------------------------------
}
//--------------------------------------------------------------
