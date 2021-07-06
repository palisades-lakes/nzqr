package nzqr.java.search;

/** 
 * @author mcdonald dot john dot alan at gmail dot com
 * @version 2018-07-16
 */
public enum ResultQuality {

  CORRECT(
    "Correct minimizer, minimum, and gradient norm art solution."),
  NOTWORSE(
    "Correct, and number of iterations, function evaulations, etc. no greater."),
  SAME(
    "Correct, and number of iterations, function evaulations, etc. identical.");

  //--------------------------------------------------------------

  private final String _description;
  public final String getDescription () { return _description; }

  //--------------------------------------------------------------

  private ResultQuality (final String description) {
    _description = description; }

  //--------------------------------------------------------------
}

