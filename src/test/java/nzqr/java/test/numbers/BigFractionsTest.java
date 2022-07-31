package nzqr.java.test.numbers;

import org.junit.jupiter.api.Test;

import nzqr.java.numbers.BigFractions;
import nzqr.java.test.algebra.SetTests;
//----------------------------------------------------------------
/** Test <code>BigFractions</code> set.
 * <p>
 * <pre>
 * mvn -q -Dtest=nzqr/java/test/sets/BigFractionsTest test > BigFractionsTest.txt
 * </pre>
 *
 * @author palisades dot lakes at gmail dot com
 * @version 2021-05-11
 */

public final class BigFractionsTest {

  @SuppressWarnings({ "static-method" })
  @Test
  public final void setTests () {
    SetTests.tests(BigFractions.get()); }

  //--------------------------------------------------------------
}
//--------------------------------------------------------------
