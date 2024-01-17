package nzqr.java.numbers;


import static nzqr.java.numbers.Numbers.hiWord;
import static nzqr.java.numbers.Numbers.unsigned;

/** Multiplication of natural numbers.
 *
 * Non-instantiable.
 *
 * @author palisades dot lakes at gmail dot com
 * @version 2024-01-16
 */

@SuppressWarnings("unchecked")
final class NaturalAdd {

  //--------------------------------------------------------------

  static final int[] add (final int[] tt, final int nt,
                          final int[] uu, final int nu) {
    if (nt<nu) { return add(uu,nu,tt,nt); }
    final int[] vv = new int[nt+1];
    long sum = 0L;
    int i=0;
    for (;i<nu;i++) {
      sum += unsigned(tt[i]) + unsigned(uu[i]);
      vv[i] = (int) sum;
      sum = hiWord(sum);}
    for (;i<nt;i++) {
      if (0L==sum) { break; }
      sum += unsigned(tt[i]);
      vv[i] = (int) sum;
      sum = hiWord(sum);}
    for (;i<nt;i++) { vv[i] = tt[i]; }
    if (0L!=sum) { vv[nt] = 1; }
    return vv; }

  static final int[] subtract (final int[] tt, final int nt,
                               final int[] uu, final int nu) {
    final int[] vv = new int[nt];
    long dif = 0L;
    int i=0;
    for (;i<nu;i++) {
      dif += unsigned(tt[i])-unsigned(uu[i]);
      vv[i] = (int) dif;
      dif= (dif>>32); }
    for (;i<nt;i++) {
      if (0L==dif) { break; }
      dif += unsigned(tt[i]);
      vv[i] = (int) dif;
      dif = (dif>>32); }
    assert 0L==dif;
    for (;i<nt;i++) { vv[i] = tt[i]; }
    return vv; }

  //--------------------------------------------------------------
  // disable constructor
  //--------------------------------------------------------------

  private NaturalAdd () {
    throw new
    UnsupportedOperationException(
      "can't instantiate " + getClass().getCanonicalName()); }

  //--------------------------------------------------------------
}
//--------------------------------------------------------------
