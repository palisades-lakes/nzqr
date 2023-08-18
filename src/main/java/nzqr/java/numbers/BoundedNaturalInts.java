package nzqr.java.numbers;

import java.util.Arrays;
import java.util.List;
import static nzqr.java.numbers.Numbers.unsigned;
import static nzqr.java.numbers.Numbers.loWord;
import static nzqr.java.numbers.Numbers.hiWord;

/** Utilities for <code>int[]</code>, <code>int</code>, and <code>long</code>,
 *  related to {@link BoundedNatural}.
 *  <br>
 * @author palisades dot lakes at gmail dot com
 * @version 2023-08-17
 */

//@SuppressWarnings("unchecked")
public final class BoundedNaturalInts {
    /** The value of <code>hiBit</code> is assumed to fit in an
     * <code>int</code> and to be a multiple of 32.
     * The maximum number of words, determined to hold
     * {@link #MAX_BITS}.
     */

    public final static int MAX_WORDS = (Integer.MAX_VALUE >> 5);

    /** The value of <code>hiBit</code> is assumed to fit in an
     * <code>int</code>. That means it can be at most
     * <code>{@link Integer#MAX_VALUE}</code>.
     * For convenience, I want the representation to use
     * all the bits in each of the <code>int</code> words,
     * so <code>MAX_BITS</code> should be a multiple of 32.
     */

    public static final int MAX_BITS = (MAX_WORDS << 5);

    //--------------------------------------------------------------

    static final int word (final int[] a, final int i) {
    assert 0<=i : "Negative index: " + i;
    assert i < MAX_WORDS : "word index too large " + i;
    if (hiInt(a)<=i) { return 0; }
    return a[i]; }

   static final long uword (final int[] a, final int i) {
    assert 0<=i : "Negative index: " + i;
    assert i < MAX_WORDS : "word index too large " + i;
    if (hiInt(a)<=i) { return 0L; }
    return unsigned(a[i]); }

   static final int[] setWord (final int[] a,
                               final int i,
                               final int w) {
    assert 0<=i : "Negative index: " + i;
    assert i < MAX_WORDS : "word index too large" + i;
    final int h = hiInt(a);
    if (0==w) {
      if (i>=h) { return a; }
      final int[] u = Arrays.copyOf(a,h);
      u[i] = 0;
      return u; }
    final int n = Math.max(i+1,h);
    final  int[] u = Arrays.copyOf(a,n);
    u[i] = w;
    return u; }

  private static final int[] shiftDownByWords (final int[] w,
                                               final int iShift) {
    final int nt = hiInt(w);
    if (0>=(nt-iShift)) { return new int[0]; }
    return Arrays.copyOfRange(w,iShift,nt); }

  private static final int[] shiftDownByBits (final int[] w,
                                              final int iShift,
                                              final int bShift) {
    final int nt = hiInt(w);
    final int nv = nt-iShift;
    // shifting all bits off the end, covers zero input case
    if (0>=nv) { return new int[0]; }

    final int[] vv = new int[nv];
    final int rShift = 32-bShift;
    int w0 = w[iShift];
    int i = 0;
    int j = iShift+1;
    while (j<nt) {
      final int w1 = w[j];
      final int wi = ((w1<<rShift) | (w0>>>bShift));
      w0 = w1;
      vv[i] = wi;
      i++; j++;}
    final int vvn = (w0>>>bShift);
    if (0!=vvn) { vv[i] = vvn; return vv; }
    return Arrays.copyOf(vv,nv-1); }

   static final int[] shiftDown (final int[] a,
                                       final int downShift) {
    assert 0<=downShift;
    if (0==downShift) { return a; }
    final int iShift = (downShift>>>5);
    final int bShift = (downShift&0x1F);
    if (0==bShift) { return shiftDownByWords(a,iShift); }
    return shiftDownByBits(a,iShift,bShift); }

  private static final int[] shiftUpByWords (final int[] w,
                                             final int iShift) {
    final int nt = hiInt(w);
    final int nv = nt+iShift;
    final int[] vv = new int[nv];
    System.arraycopy(w,0,vv,iShift,nt);
    return vv; }

    private static final int[] shiftUpByBits (final int[] tt,
                                              final int iShift,
                                              final int bShift) {
        final int nt = hiInt(tt);
        final int nv = nt+iShift;
        final int rShift = 32-bShift;
        final int[] vv = new int[nv];
        int w0 = tt[0];
        vv[iShift] = (w0<<bShift);
        for (int i = 1,j = iShift+1;i<nt;i++,j++) {
            final int w1 = tt[i];
            final int wj = ((w1<<bShift)|(w0>>>rShift));
            w0 = w1;
            vv[j] = wj; }
        final int vvn = (w0>>>rShift);
        if (0!=vvn) {
            final int[] vvv = Arrays.copyOf(vv,nv+1);
            vvv[nv] = vvn;
            return vvv; }
        return vv; }

//    private static final int[] shiftUpByBits (final int[] tt,
//                                              final int iShift,
//                                              final int bShift) {
//        final int nt = hiInt(tt);
//        final int nv = nt+iShift;
//        final int rShift = 32-bShift;
//        final int[] vv = new int[nv+1];
//        int w0 = tt[0];
//        vv[iShift] = (w0<<bShift);
//        for (int i = 1,j = iShift+1;i<nt;i++,j++) {
//            final int w1 = tt[i];
//            final int wj = ((w1<<bShift)|(w0>>>rShift));
//            w0 = w1;
//            vv[j] = wj; }
//        final int vvn = (w0>>>rShift);
//        if (0!=vvn) { vv[nv] = vvn; return vv; }
//        return Arrays.copyOf(vv,nv); }

    static final int[] shiftUp (final int[] a,
                                final int upShift) {
    assert 0<=upShift;
    if (0==upShift) { return a; }
    final int iShift = (upShift>>>5);
    final int bShift = (upShift&0x1f);
    if (0==bShift) { return shiftUpByWords(a,iShift); }
    return shiftUpByBits(a,iShift,bShift); }

  static final int hiInt(final int[] x) {
    final int n = x.length;
    if (0==n) { return 0; }
    int i = n-1;
    while ((0<=i) && (0==x[i])) { i--; }
    return i+1; }

    //--------------------------------------------------------------

    /** shifted fused multiply-subtract.
     * <br>
     * DANGER: read-only <code>v</code>,
     * but destroys <code>u<</code>.
     */

    private static final List fms (final int[] u,
                                   final int n0,
                                   final long x,
                                   final int[] v,
                                   final int n1,
                                   final int i0) {
        final int n2 = n0-1-i0;
        final int[] uu;
        if (n2 <= u.length) { uu = u; }
        else { uu = Arrays.copyOf(u, n2); }
        long carry = 0;
        for (int i=n2-n1,j=0;j<n1;j++,i++) {
            final long prod = (unsigned(v[j])*x) + carry;
            final long diff = unsigned(uu[i])-prod;
            uu[i] = (int) diff;
            carry = hiWord(prod);
            // TODO: is this related to possibility x*u > this,
            // so difference is negative?
            if (loWord(diff) > unsigned(~((int)prod))) { carry++; } }
        return List.of(uu, loWord(carry)); }

    /** A primitive used for division. This method adds in one
     * multiple of the divisor a back to the dividend result at a
     * specified start. It is used when qhat was estimated too
     * large, and must be adjusted.
     * <p>
     * Never called in testing, (prob less than 2^32),
     * so DANGER that changes have made it incorrect...
     */

    private static final int[] divadd (final int[] u,
                                       final int n0,
                                       final int[] v,
                                       final int n1,
                                       final int i0) {
        final int n = n0 - 1 - i0;
        final int off = n - n1;
        int[] ww = Arrays.copyOf(u,n);
        long carry = 0;
        for (int i=off,j=0;j<n1;i++,j++) {
            final long sum = unsigned(v[j]) + unsigned(ww[i]) + carry;
            ww[i] = (int) sum;
            carry = sum >>> 32; }
        return ww; }

    static final List<int[]> knuthDivision (final int[] u,
                                            final int[] v) {
        final int nv = hiInt(v);
        final int lShift = Integer.numberOfLeadingZeros(word(v,nv-1));
        final int[] d = shiftUp(v,lShift);
        final int nd = hiInt(d);
        int[] r = shiftUp(u,lShift);
        final int nr0 = hiInt(r);
        r = setWord(r,nr0,0);
        final int nr = nr0+1;
        final int nq = nr-nd;
        int[] qq = new int[1];
        //int[] qq = new int[nq];
        final long dh = uword(d,nd-1);
        final long dl = uword(d,nd-2);
        for (int j=0;j<(nq-1);j++) {
            boolean correctQhat = true;
            final int i = nr-j-1;
            final long rh = uword(r,i);
            final long rm = uword(r,i-1);
            long qhat; long qrem;
            if (rh==dh) {
                qhat=0xFFFFFFFFL; qrem=rh+rm; correctQhat=(qrem>=rh); }
            else {
                final long nChunk = (rh<<32) | rm;
                if (nChunk >= 0) {
                    qhat = loWord(nChunk/dh);
                    qrem = loWord(nChunk-(qhat*dh)); }
                else {
                    final long tmp = Ints.divWord(nChunk,dh);
                    qhat = loWord(tmp); qrem = hiWord(tmp); } }
            if (0L==qhat) { continue; }
            if (correctQhat) {
                final long nl = uword(r,i-2);
                long rs = (qrem << 32) | nl;
                long estProduct = dl*qhat;
                if (Long.compareUnsigned(estProduct, rs)>0) {
                    qhat--;
                    qrem = loWord(qrem+dh);
                    if (qrem>=dh) {
                        estProduct -= (dl);
                        rs = (qrem << 32) | nl;
                        if (Long.compareUnsigned(estProduct, rs)>0) { qhat--; } } } }
            r = setWord(r,i,0);
            final List rc = fms(r,nr,qhat,d,nd,j);
            r = (int[]) rc.get(0);
            final long borrow = (Long) rc.get(1);
            if (borrow>rh) {
                r = divadd(r,nr,d,nd,j);
                qhat--; }
            qq = setWord(qq,nq-1-j,(int) qhat);
            //qq[nq-1-j] = (int) qhat;
        }

        long qhat; long qrem;
        boolean correctQhat = true;
        final long nh = uword(r,nd);
        final long nm =  uword(r,nd-1);
        if (nh==dh) {
            qhat=0xFFFFFFFFL; qrem=nh+nm; correctQhat=(qrem>=nh); }
        else {
            final long nChunk = (nh << 32) | nm;
            if (nChunk >= 0) {
                qhat = loWord(nChunk/dh); qrem = loWord(nChunk-(qhat*dh)); }
            else {
                final long tmp = Ints.divWord(nChunk,dh);
                qhat = loWord(tmp); qrem = hiWord(tmp); } }
        if (0L!=qhat) {
            if (correctQhat) {
                final long nl = uword(r,nd-2);
                long rs = (qrem << 32) | nl;
                long estProduct = dl*qhat;
                if (Long.compareUnsigned(estProduct, rs)>0) {
                    qhat--; qrem = loWord(qrem + dh);
                    if (qrem >= dh) {
                        estProduct -= (dl);
                        rs = (qrem << 32) | nl;
                        if (Long.compareUnsigned(estProduct, rs)>0) { qhat--; } } } }
            r = setWord(r,nd,0);
            final List rc = fms(r,nr,qhat,d,nd,nq-1);
            r = (int[]) rc.get(0);
            final long borrow = (Long) rc.get(1);
            if (borrow > nh) {
                r = divadd(r,nr,d,nd,nq-1);
                qhat--; }
            qq = setWord(qq,0,(int) qhat); }

        if (0<lShift) { r = shiftDown(r,lShift); }
        return List.of(qq,r); }

    //--------------------------------------------------------------
    // disable constructor
    //--------------------------------------------------------------

    private BoundedNaturalInts () {
        throw new UnsupportedOperationException(
                "can't instantiate " + getClass()); }
}
//--------------------------------------------------------------
