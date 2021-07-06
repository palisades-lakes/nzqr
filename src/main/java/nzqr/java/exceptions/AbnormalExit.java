package nzqr.java.exceptions;

import nzqr.java.search.Status;

/** Thrown for unsuccessful exits from search.
 *
 * @author palisades dot lakes at gmail dot com
 * @version 2018-07-26
 */
public abstract class AbnormalExit
extends RuntimeException {

  private static final long serialVersionUID = 1L;

  //--------------------------------------------------------------
  // fields
  //--------------------------------------------------------------

  private Status _status;
  public final Status getStatus () { return _status; }
  public final void setStatus (final Status status) {
    _status = status; }

  //--------------------------------------------------------------
  // construction
  //--------------------------------------------------------------

  public AbnormalExit (final String msg,
                       final boolean negDiag) {
    super(
      negDiag
      ? msg + "\nParameter eta2 may be too small\n"
        : msg); }

  public AbnormalExit (final String msg) {
    super(msg); }

  //--------------------------------------------------------------
}
//--------------------------------------------------------------
