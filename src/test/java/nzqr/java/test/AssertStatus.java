package nzqr.java.test;

import org.junit.jupiter.api.Assertions;

import nzqr.java.search.ResultQuality;
import nzqr.java.search.Status;

//----------------------------------------------------------------
/** Common test methods.
 *
 * @author palisades dot lakes at gmail dot com
 * @version 2018-07-12
 */

public final class AssertStatus {

  //--------------------------------------------------------------
  // methods
  //--------------------------------------------------------------

  public static final void assertStatus (final Status status,
                                         final Status expected,
                                         final double xulps,
                                         final double fulps,
                                         final double gulps,
                                         final ResultQuality quality) {
    switch (quality) {
    case SAME :
      Assertions.assertTrue(
        status.approximatelyEqual(xulps,fulps,gulps,expected),
        () -> { return "\nunexpected status\n" + status.toString()
        + "\n" + expected.toString(); } ); 
      break; 

    case NOTWORSE :
      Assertions.assertTrue(
        status.notWorse(xulps,fulps,gulps,expected),
        () -> { return "\nworse status\n" + status.toString()
        + "\n" + expected.toString(); } ); 
      break;

    case CORRECT :
      Assertions.assertTrue(
        status.correctSolution(xulps,fulps,gulps,expected),
        () -> { return "\nworse status\n" + status.toString()
        + "\n" + expected.toString(); } ); 
      break;

    default : throw new UnsupportedOperationException(); }
  }

   //--------------------------------------------------------------
  // construction
  //--------------------------------------------------------------

  private AssertStatus () {
    throw new  UnsupportedOperationException(
      "can't instantiate " + getClass().getSimpleName()); }

  //--------------------------------------------------------------
}
