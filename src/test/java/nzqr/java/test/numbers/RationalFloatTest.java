package nzqr.java.test.numbers;

import java.util.function.BinaryOperator;

import org.junit.jupiter.api.Test;

import nzqr.java.numbers.BoundedNatural;
import nzqr.java.numbers.Numbers;
import nzqr.java.numbers.RationalFloat;
import nzqr.java.test.Common;

//----------------------------------------------------------------
/** Test desired properties of RationalFloat.
 * <p>
 * <pre>
 * mvn -q -Dtest=xfp/java/test/numbers/RationalFloatTest test > RationalFloatTest.txt
 * </pre>
 *
 * @author palisades dot lakes at gmail dot com
 * @version 2019-12-01
 */

public final class RationalFloatTest {

  private static final BinaryOperator<Comparable> dist = (q0,q1) ->
  ((RationalFloat) q0).subtract((RationalFloat) q1).abs();

  @SuppressWarnings({ "static-method" })
  @Test
  public final void testRounding () {
    //Debug.DEBUG=false;
    final RationalFloat[] f =
    {
     RationalFloat.valueOf(
       false,BoundedNatural.valueOf("2366052b8b801d",0x10),-22),
     RationalFloat.valueOf(
       true,BoundedNatural.valueOf("2366052b8b801d",0x10),-22),
     RationalFloat.valueOf(
       true,BoundedNatural.valueOf("232330747ceeab",0x10),-23),
     RationalFloat.valueOf(
       false,BoundedNatural.valueOf("232330747ceeab",0x10),-23),
     RationalFloat.valueOf(
       false,BoundedNatural.valueOf("21ab528c4dbc181",0x10),-26),
     RationalFloat.valueOf(
       true,BoundedNatural.valueOf("8d9814ae2e0074",0x10),-25),
     RationalFloat.valueOf(
       true,BoundedNatural.valueOf("2c94d1dcb123a56b9c1",0x10),-43), };
    for (final RationalFloat fi : f) {
      //Debug.println(fi.toString());
      Common.doubleRoundingTest(
        RationalFloat::valueOf, Numbers::doubleValue, dist,
        Object::toString, fi,
        Common::compareTo, Common::compareTo);
      Common.floatRoundingTest(
        RationalFloat::valueOf, Numbers::floatValue, dist,
        Object::toString, fi,
        Common::compareTo, Common::compareTo);  }
    //Debug.DEBUG=false;

    Common.doubleRoundingTests(
      RationalFloat::valueOf, RationalFloat::valueOf,
      Numbers::doubleValue, dist, Object::toString,
      Common::compareTo, Common::compareTo);

    Common.floatRoundingTests(
      RationalFloat::valueOf, RationalFloat::valueOf,
      Numbers::floatValue, dist, Object::toString,
      Common::compareTo, Common::compareTo);
  }

  //--------------------------------------------------------------
}
//--------------------------------------------------------------
