package nzqr.java.search;

import nzqr.java.functions.Doubles;
import nzqr.java.functions.Vektor;

/** Minimization results.
 *
 * <em>Immutable.</em>
 *
 * @author palisades dot lakes at gmail dot com
 * @version 2018-09-07
*/

public final class Status {

  private final Vektor _xmin;
  /** Minimizing position. */
  public final Vektor xmin () { return _xmin; }

  private final double _f;
  /** function value at solution */
  public final double f () { return _f; }

  private final double _gnorm;
  /** supnorm of gradient */
  public final double gnorm () { return _gnorm; }
  
  private final int _iter;
  /** number of iterations */
  public final int iter () { return _iter; }
  
  private final int _iterSub;
  /** number of subspace iterations */
  public final int iterSub () { return _iterSub; }
  
  private final int _numSub;
  /** total number of subspaces */
  public final int numSub () { return _numSub; }
  
  private final int _nFunc;
  /** number of function evaluations */
  public final int nFunc () { return _nFunc; }
  
  private final int _nGrad;
  /** number of gradient evaluations */
  public final int nGrad () { return _nGrad; }
  
  //--------------------------------------------------------------
  // methods
  //--------------------------------------------------------------

  public final boolean correctSolution (final double xulps,
                                        final double fulps,
                                        final double gulps,
                                        final Status that) {
    if (! _xmin.approximatelyEqual(xulps, that._xmin)) {
      System.out.println("wrong minimum:" + _xmin);
      return false; }
    if (! Doubles.approximatelyLess(fulps,_f,that._f)) {
      System.out.println("function value better");
      return false; }
    if (! Doubles.approximatelyLess(gulps,_gnorm,that._gnorm)) {
      System.out.println("gradient norm better");
      return false; }

    return true; }

  public final boolean approximatelyEqual (final double xulps,
                                           final double fulps,
                                           final double gulps,
                                           final Status that) {
    if (_iter != that._iter) { return false; }
    if (_iterSub != that._iterSub) { return false; }
    if (_numSub != that._numSub) { return false; }
    if (_nFunc != that._nFunc) { return false; }
    if (_nGrad != that._nGrad) { return false; }

    return correctSolution(xulps,fulps,gulps,that); }

  /** This is not 'ulps' worse that <code>that</code>.
   * TODO: is x approximately equal right here?
   */
  public final boolean notWorse (final double xulps,
                                final double fulps,
                                final double gulps,
                                final Status that) {
    if (_iter > that._iter) {
      System.out.println("more iterations");
      return false; }
    if (_iterSub > that._iterSub) {
      System.out.println("more subspace iterations");
      return false; }
    if (_numSub > that._numSub) {
      System.out.println("more subspaces");
      return false; }
    if (_nFunc > that._nFunc) {
      System.out.println("more function evaluations");
      return false; }
    if (_nGrad > that._nGrad) {
      System.out.println("more gradient evaluations");
      return false; }

    return correctSolution(xulps,fulps,gulps,that); }

  /** (this - that) for all fields.
   *  Smaller is better, except for xmin.
   */

  public final Status difference (final Status that) {
    return new Status(
      _xmin.subtract(that._xmin),
      _f-that._f,
      _gnorm-that._gnorm,
      _iter-that._iter,
      _iterSub-that._iterSub,
      _numSub-that._numSub,
      _nFunc-that._nFunc,
      _nGrad-that._nGrad); }

  //--------------------------------------------------------------
  // Object methods
  //--------------------------------------------------------------

  @Override
  public final int hashCode () {
    int c = 17;

    final long l0  = Double.doubleToLongBits(_f);
    final int c0 = (int) (l0 ^ (l0 >>> 32));
    c = (31*c) + c0;

    final long l1  = Double.doubleToLongBits(_gnorm);
    final int c1 = (int) (l1 ^ (l1 >>> 32));
    c = (31*c) + c1;
    c = (31*c) + _iter;
    c = (31*c) + _iterSub;
    c = (31*c) + _numSub;
    c = (31*c) + _nFunc;
    c = (31*c) + _nGrad;
    return c; }

  // NOTE: not useful since difficult to get exact double ==
  @Override
  public final boolean equals (final Object x) {
    if (! (x instanceof Status)) { return false; }
    final Status that = (Status) x;
    if (_f != that._f) { return false; }
    if (_gnorm != that._gnorm) { return false; }
    if (_iter != that._iter) { return false; }
    if (_iterSub != that._iterSub) { return false; }
    if (_numSub != that._numSub) { return false; }
    if (_nFunc != that._nFunc) { return false; }
    if (_nGrad != that._nGrad) { return false; }
    return true; }

  @Override
  public final String toString () {

    final String frmt =
      "status:\n" +
        "function value:       %23.15E\n" +
        "gradient inf norm:    %23.15E\n" +
        "iterations:           %3d\n" +
        "subspace iterations:  %3d\n" +
        "number of subspaces:  %3d\n" +
        "function evaluations: %3d\n" +
        "gradient evaluations: %3d\n";

    return
      String.format(
        frmt,
        Double.valueOf(_f),
        Double.valueOf(_gnorm),
        Integer.valueOf(_iter),
        Integer.valueOf(_iterSub),
        Integer.valueOf(_numSub),
        Integer.valueOf(_nFunc),
        Integer.valueOf(_nGrad)); }

  //--------------------------------------------------------------
  // construction
  //--------------------------------------------------------------

  public Status (final Vektor xmin,
                 final double f,
                 final double gnorm,
                 final int iter,
                 final int iterSub,
                 final int numSub,
                 final int nFunc,
                 final int nGrad) {
    super();
    this._xmin = xmin;
    this._f =f;
    this._gnorm = gnorm;
    this._iter = iter;
    this._iterSub = iterSub;
    this._numSub = numSub;
    this._nFunc = nFunc;
    this._nGrad = nGrad; }

  //--------------------------------------------------------------
} // end class
//--------------------------------------------------------------
