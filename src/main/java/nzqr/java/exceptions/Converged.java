package nzqr.java.exceptions;

/** Thrown for successful exit from search.
 *
 * @author palisades dot lakes at gmail dot com
 * @version 2018-07-17
 */
public class Converged
extends RuntimeException {

  private static final long serialVersionUID = 1L;

  //--------------------------------------------------------------
  // fields
  //--------------------------------------------------------------

//  @SuppressWarnings("unused")
//  private final Function _lf;
  
  //--------------------------------------------------------------
  // construction
  //--------------------------------------------------------------

  private Converged (final String msg) {
    super(msg); }

  //--------------------------------------------------------------

  @SuppressWarnings("unused")
  public static final Converged
  make (final Object context) { return new Converged(""); }

  //--------------------------------------------------------------
}
//--------------------------------------------------------------
