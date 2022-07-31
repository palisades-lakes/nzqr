package nzqr.java.scripts;

import java.math.BigInteger;
import java.util.List;

import nzqr.java.numbers.BoundedNatural;
import nzqr.java.prng.Generator;
import nzqr.java.prng.Generators;
import nzqr.java.prng.PRNG;

/** Compare BigInteger and BoundedNatural results.
 * At least 4*2<sup>32</sup> tests to get calls to BoundedNatural.divadd.
 *
 * <pre>
 * j --source 12 -ea src/scripts/java/xfp/java/scripts/Division.java > division.txt
 * </pre>
 * @author palisades dot lakes at gmail dot com
 * @version 2019-09-04
 */
@SuppressWarnings("unchecked")
public final class Division {

  private static final void
  divideAndRemainder (final BigInteger x0,
                      final BigInteger x1) {
    if (0 != x1.signum()) {
      final BoundedNatural y0 = BoundedNatural.valueOf(x0);
      final BoundedNatural y1 = BoundedNatural.valueOf(x1);
      final BigInteger[] xqr = x0.divideAndRemainder(x1);
      final BigInteger xq = xqr[0];
      final BigInteger xr = xqr[1];
      final List<BoundedNatural> yqr = y0.divideAndRemainder(y1);
      final BigInteger yq = yqr.get(0).bigIntegerValue();
      final BigInteger yr = yqr.get(1).bigIntegerValue();

      assert xq.equals(yq) :
        "\nquotients differ!"
        + "\n" + x0.toString(0x10)
        + "\n / "
        + "\n" +  x1.toString(0x10)
        + "\n -> "
        + "\n" + xq.toString(0x10)
        + "\n" + y0.toString()
        + "\n / "
        + "\n" +  y1.toString()
        + "\n -> "
        + "\n" + yq.toString(0x10);

      assert xr.equals(yr) :
        "\nremainders differ!"
        + "\n" + x0.toString(0x10)
        + "\n / "
        + "\n" +  x1.toString(0x10)
        + "\n -> "
        + "\n" + xr.toString(0x10)
        + "\n" + y0.toString()
        + "\n / "
        + "\n" +  y1.toString()
        + "\n -> "
        + "\n" + yr.toString(0x10); } }

  //--------------------------------------------------------------

  private static final long TRYS = (1L << 32);

  public static final void main (final String[] args) {
    final Generator gn =
      Generators.bigIntegerGenerator(
        PRNG.well44497b("seeds/Well44497b-2019-01-05.txt"));
    for (long i=0;i<TRYS;i++) {
      if (0L==(i%(1L<<20))) { System.out.println(i); }
      divideAndRemainder(
        ((BigInteger) gn.next()).abs(),
        ((BigInteger) gn.next()).abs()); } }

  //--------------------------------------------------------------
}
//--------------------------------------------------------------
