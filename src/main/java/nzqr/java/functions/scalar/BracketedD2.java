package nzqr.java.functions.scalar;

import nzqr.java.functions.Function;
import nzqr.java.search.StopTest;

/** Stop a 1d search if the history contains 2 <code>double</code>
 * domain values, <code>x0 &lt; x1</code>,  such that
 * <code>f.slope(x0) &lt; 0.0 &lt; f.slope(x1)</code>.
 * <p>
 * To simplify history bookkeeping It is left to the 
 * <code>objective</code> function to determine whether to re-compute,
 * or cache <code>df/dx(x0), df/dx(x1), f(x2)</code>.
 * 
 * @author palisades dot lakes at gmail dot com
 * @version 2021-06-07
 */

public final class BracketedD2  implements StopTest {

  @Override
  public final boolean stop (final Function f,
                             final Object history) {
    final Interval t = (Interval) history;
    return 
      (f.slope(t.lower()) < 0.0)
      &&
      (0.0 < f.slope(t.upper())); }
  
  // --------------------------------------------------------------
} // end interface
// --------------------------------------------------------------
