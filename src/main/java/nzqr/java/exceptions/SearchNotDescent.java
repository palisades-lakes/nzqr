package nzqr.java.exceptions;

/** Thrown when the search direction is not a descent direction,
 * probably indicating an error in the functional evaluation.
 *
 * @author palisades dot lakes at gmail dot com
 * @version 2018-07-26
 */
public final class SearchNotDescent extends AbnormalExit  {

  private static final long serialVersionUID = 1L;

  //--------------------------------------------------------------
  // construction
  //--------------------------------------------------------------

  private SearchNotDescent (final String msg,
                            final boolean negDiag) {
    super(msg,negDiag); }

  //--------------------------------------------------------------
  /** Search direction is not a descent direction,
   * probably indicating an error in the functional evaluation.
   */

  @SuppressWarnings("unused")
  public static final SearchNotDescent
  make (final boolean negDiag,
        final Object context) {
    final StringBuilder b = new StringBuilder();
//    b.append("\n");
//    b.append(context.toString());
    return new SearchNotDescent(b.toString(),negDiag); }

  public static final SearchNotDescent
  make (final Object context) { return make(false,context); }

  //--------------------------------------------------------------
}
//--------------------------------------------------------------
