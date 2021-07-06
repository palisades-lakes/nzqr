package nzqr.java.search;

import nzqr.java.functions.Function;

/** General 'convergence' test for iterative searches.
 * Permits comparing the effect of differing termination rules with 
 * the same search logic.
 *
 * @author palisades dot lakes at gmail dot com
 * @version 2018-09-07
 */

public interface StopTest  {

  /** Should the search stop?
   * <p>
   * The decision is made based on some representation of the recent
   * history of the search. This can be very different for different
   * search algorithms, so nothing is assumed about how it is 
   * implemented. Implementations are expected to cast the
   * <code>history</code> to an appropriate interface/class.
   * <p>
   * @throws ClassCastException if the history class can't be handled.
   */
  
  public boolean stop (final Function objective,
                       final Object history);
  
  // --------------------------------------------------------------
} // end interface
// --------------------------------------------------------------
