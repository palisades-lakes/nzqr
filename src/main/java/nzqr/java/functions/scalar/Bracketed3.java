package nzqr.java.functions.scalar;

import nzqr.java.functions.Function;
import nzqr.java.search.StopTest;

/** Stop a 1d search if the history contains 3 <code>double</code>
 * domain values, <code>x0 &lt; x1 &lt; x2</code>, such that
 * <code>f(x0) %gt; f(x1) &lt; f(x2)</code>.
 * <p>
 * To simplify history bookkeeping It is left to the 
 * <code>objective</code> function to determine whether to re-compute,
 * or cache <code>f(x0), f(x1), f(x2)</code>.
 * 
 * @author palisades dot lakes at gmail dot com
 * @version 2021-06-07
 */

public final class Bracketed3 implements StopTest {

  @Override
  public final boolean stop (final Function f,
                             final Object history) {
    final DoubleBracket t = (DoubleBracket) history;
    return 
      (f.doubleValue(t.x0()) > f.doubleValue(t.x1()))
      &&
      (f.doubleValue(t.x1()) < f.doubleValue(t.x2())); }
  
  // --------------------------------------------------------------
} // end interface
// --------------------------------------------------------------
