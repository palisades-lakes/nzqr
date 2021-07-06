package nzqr.java.exceptions;

/** Thrown when there are too many major iterations without
 * [passing any of the convergence tests.
 *
 * @author palisades dot lakes at gmail dot com
 * @version 2018-07-26
 */
public final class TooManyIterations extends AbnormalExit {

  private static final long serialVersionUID = 1L;

  //--------------------------------------------------------------
  // construction
  //--------------------------------------------------------------

  private TooManyIterations (final String msg,
                             final boolean negDiag) {
    super(msg,negDiag); }

  //--------------------------------------------------------------
  /** Too many line search iterations without satisfying wolfe
   * conditions.
   */

  @SuppressWarnings("unused")
  public static final TooManyIterations
  make (final int iterations,
        final boolean negDiag,
        final Object context) {
    final StringBuilder b = new StringBuilder();
    b.append(
      String.format(
        "Search failed with too many major iterations:%d\n",
        Integer.valueOf(iterations)));
    //b.append("\n");
    //b.append(context.toString());
    return new TooManyIterations(b.toString(),negDiag); }

  public static final TooManyIterations
  make (final int iterations,
        final Object context) {
    return make(iterations,false,context); }

  //--------------------------------------------------------------
}
//--------------------------------------------------------------
