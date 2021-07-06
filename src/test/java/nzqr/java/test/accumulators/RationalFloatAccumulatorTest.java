package nzqr.java.test.accumulators;

import java.util.List;

import org.junit.jupiter.api.Test;

import nzqr.java.test.Common;

//----------------------------------------------------------------
/** Test summation algorithms.
 * <p>
 * <pre>
 * mvn -q test -Dtest=xfp/java/test/accumulators/RationalFloatAccumulatorTest > RFAT.txt
 * </pre>
 *
 * @author palisades dot lakes at gmail dot com
 * @version 2019-10-07
 */

public final class RationalFloatAccumulatorTest {

  //--------------------------------------------------------------
  private static final int DIM = 517;
  private static final List<String> accumulators =
    List.of("nzqr.java.accumulators.RationalFloatAccumulator");

  @SuppressWarnings("static-method")
  @Test
  public final void tests () {
    //Debug.DEBUG=true;
    //Debug.println();
    //Debug.println(Classes.className(this));
    Common.l2Tests(
      Common.generators(DIM),
      Common.makeAccumulators(accumulators),
      EFloatAccumulator.make());
    Common.sumTests(
      Common.generators(DIM),
      Common.makeAccumulators(accumulators),
      EFloatAccumulator.make());
    Common.dotTests(
      Common.generators(DIM),
      Common.makeAccumulators(accumulators),
      EFloatAccumulator.make());
    //Debug.DEBUG=false;
  }

  //--------------------------------------------------------------
}
//--------------------------------------------------------------
