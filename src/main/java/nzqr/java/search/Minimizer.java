package nzqr.java.search;

/** Interface for iterative search algorithms.
 *
 * @author palisades dot lakes at gmail dot com
 * @version 2018-09-07
 */

public interface Minimizer {
  /** 
  * @throws ClassCastException if the start class can't be handled.
  */
  
  public Object minimize (final Object start);
  
  // --------------------------------------------------------------
} // end interface
// --------------------------------------------------------------
