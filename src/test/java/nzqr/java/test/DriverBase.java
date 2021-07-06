package nzqr.java.test;

import java.util.Arrays;

//----------------------------------------------------------------
/** See CGDescent version 6.8.
 *
 * @author palisades dot lakes at gmail dot com
 * @version 2018-07-05
 */

public abstract class DriverBase {

  public static final double XULPS = 5.0e7;
  public static final double FULPS = 2.0e5;
  public static final double GULPS = 5.0e7;

  //--------------------------------------------------------------


  public static final void restart (final double[] x) {
    Arrays.fill(x,1.0); }

  public static final double[] start (final int n) {
    final double[] x = new double[n];
    restart(x);
    return x;}

  //--------------------------------------------------------------
}
