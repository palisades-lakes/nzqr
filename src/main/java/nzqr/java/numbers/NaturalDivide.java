package nzqr.java.numbers;

import java.math.BigInteger;
import java.util.List;
import static nzqr.java.numbers.Numbers.unsigned;

/** Division, gcd, etc., of natural numbers.
 * <br>
 * Non-instantiable.
 *
 * @author palisades dot lakes at gmail dot com
 * @version 2022-11-17
 */

public final class NaturalDivide {

  //--------------------------------------------------------------
  // division
  //--------------------------------------------------------------

  private static final int BURNIKEL_ZIEGLER_THRESHOLD = 80;
  private static final int BURNIKEL_ZIEGLER_OFFSET = 40;

  //--------------------------------------------------------------

  private static final boolean useKnuthDivision (final BoundedNatural u,
                                                 final BoundedNatural v) {
    final int nn = u.hiInt();
    final int nd = v.hiInt();
    return
      (nd < BURNIKEL_ZIEGLER_THRESHOLD)
      ||
      ((nn-nd) < BURNIKEL_ZIEGLER_OFFSET); }

  //--------------------------------------------------------------
  /** Interpret {@code d} as unsigned. */

  private static final List<BoundedNatural>
  divideAndRemainder (final BoundedNatural u,
                      final int d) {
    if (1==d) { return List.of(u,u.zero()); }

    final int nu = u.hiInt();
    final long dd = unsigned(d);
    if (1==nu) {
      final long nn = u.uword(0);
      final int q = (int) (nn/dd);
      final int r = (int) (nn-(q*dd));
      return List.of(BoundedNatural.valueOf(q),BoundedNatural.valueOf(r)); }

    BoundedNatural qq = u;

    final int shift = Integer.numberOfLeadingZeros(d);
    int r = u.word(u.hiInt()-1);
    long rr = unsigned(r);
    if (rr < dd) {
      qq = qq.setWord(qq.hiInt()-1,0); }
    else {
      final int rrdd = (int) (rr / dd);
      qq = qq.setWord(u.hiInt()-1,rrdd);
      r = (int) (rr-(rrdd*dd));
      rr = unsigned(r); }

    int xlen = u.hiInt();
    while (--xlen > 0) {
      final long nEst = (rr << 32) | u.uword(xlen-1);
      final int q;
      if (nEst >= 0) {
        q = (int) (nEst / dd);
        r = (int) (nEst - (q * dd)); }
      else {
        final long tmp = Ints.divWord(nEst,dd);
        q = (int) Numbers.loWord(tmp);
        r = (int) Numbers.hiWord(tmp); }
      qq = qq.setWord(xlen-1,q);
      rr = unsigned(r); }
    if (shift > 0) { return List.of(qq,BoundedNatural.valueOf(r%d)); }
    return List.of(qq,BoundedNatural.valueOf(r)); }

  //--------------------------------------------------------------
  private static final List<BoundedNatural>
  knuthDivision (final BoundedNatural u,
                 final BoundedNatural v) {
    final List<int[]> qr = BoundedNaturalInts.knuthDivision(u.words(),v.words());
    return List.of(
            BoundedNatural.unsafe(qr.get(0)),
            BoundedNatural.unsafe(qr.get(1))); }

  //--------------------------------------------------------------
  private static final int KNUTH_POW2_THRESH_LEN = 6;
  private static final int KNUTH_POW2_THRESH_ZEROS = 3*32;

  //--------------------------------------------------------------

  public static final List<BoundedNatural>
  divideAndRemainderKnuth (final BoundedNatural u,
                           final BoundedNatural v) {
    //assert ! v.isZero();
    if (v.isOne()) { return List.of(u,u.zero()); }
    if (u.isZero()) { return List.of(u.zero(),u.zero()); }

    final int cmp = u.compareTo(v);
    if (0==cmp) { return List.of(u.one(),u.zero()); }
    if (0>cmp) { return List.of(u.zero(),u); }

    //assert u.isValid();
    //assert v.isValid();
    if (1==v.hiInt()) { return divideAndRemainder(u,v.word(0)); }

    // Cancel common powers of 2 if above KNUTH_POW2_* thresholds
    if (u.hiInt() >= KNUTH_POW2_THRESH_LEN) {
      final int shift = Math.min(u.loBit(),v.loBit());
      if (shift >= KNUTH_POW2_THRESH_ZEROS) {
        final BoundedNatural a = u.shiftDown(shift);
        final BoundedNatural b = v.shiftDown(shift);
        final List<BoundedNatural> qr = divideAndRemainderKnuth(a,b);
        final BoundedNatural r = qr.get(1).shiftUp(shift);
        return List.of(qr.get(0),r); } }
    return knuthDivision(u,v); }

  //--------------------------------------------------------------
  /** This method implements algorithm 2 from pg. 5 of the
   * Burnikel-Ziegler paper. It divides a 3n-digit number by a
   * 2n-digit number.<br/>
   * The parameter beta is 2<sup>32</sup> so all shifts are
   * multiples of 32 bits.<br/>
   * <br/>
   * {@code this} must be a nonnegative number such that
   * {@code 2*this.hiBit() <= 3*b.hiBit()}
   */

  private static final List<BoundedNatural>
  divide3n2n (final BoundedNatural a,
              final BoundedNatural b) {
    final int n = b.hiInt() / 2;   // half the length of b in ints

    // step 1: view this as [a1,a2,a3] where each ai is n ints
    // or less; let a12=[a1,a2]
    BoundedNatural a12 = a.shiftDown(32*n);

    // step 2: view b as [b1,b2] where each bi is n ints or less
    BoundedNatural b1 = b.shiftDown(n*32);
    final BoundedNatural b2 = b.words(0,n);
    BoundedNatural r;
    BoundedNatural d;
    BoundedNatural q;
    // TODO: word shift or shifted comparison
    if (a.compareTo(b.shiftUp(32*n)) < 0) {
      // step 3a: if a1<b1, let quotient=a12/b1 and r=a12%b1
      // Doesn't need modified a12
      final List<BoundedNatural> qr = divide2n1n(a12,b1);
      q = qr.get(0);
      r = qr.get(1);
      // step 4: d=quotient*b2
      d = q.multiply(b2); }
    else {
      // step 3b: if a1>=b1, let quotient=beta^n-1
      //and r=a12-b1*2^n+b1
      q = BoundedNatural.ones(n);
      a12 = a12.add(b1);
      b1 = b1.shiftUp(32*n);
      r = a12.subtract(b1);
      // step 4: d=quotient*b2=(b2 << 32*n) - b2
      d = b2.shiftUp(32*n).subtract(b2); }
    // step 5: r = r*beta^n + a3 - d (paper says a4)
    // However, don't subtract d until after the while loop
    // so r doesn't become negative
    r = r.shiftUp(n<<5).add(a.words(0,n));
    // step 6: add b until r>=d
    while (r.compareTo(d) < 0) {
      r = r.add(b);
      q = q.subtract(a.one()); }
    return List.of(q,r.subtract(d)); }

  //--------------------------------------------------------------
  /** This method implements algorithm 1 from pg. 4 of the
   * Burnikel-Ziegler paper. It divides a 2n-digit number by an
   * n-digit number.<br/>
   * The parameter beta is 2<sup>32</sup> so all shifts are
   * multiples of 32 bits. <br/>
   * {@code this} must be a nonnegative number such that
   * {@code this.hiBit() <= 2*b.hiBit()}
   * @param b a positive number such that {@code b.hiBit()} is even
   */

  private static final List<BoundedNatural>
  divide2n1n (final BoundedNatural a,
              final BoundedNatural b) {
    final int n = b.hiInt();

    //assert a.isValid();
    //assert b.isValid();
    // step 1: base case
    if (((n%2) != 0) || (n < BURNIKEL_ZIEGLER_THRESHOLD)) {
      final List<BoundedNatural> qr = divideAndRemainderKnuth(a,b);
      return List.of(qr.get(0),qr.get(1)); }

    // step 2: view this as [a1,a2,a3,a4]
    // where each ai is n/2 ints or less
    // aUpper = [a1,a2,a3]
    final BoundedNatural aUpper = a.shiftDown(32*(n/2));
    //assert a.isValid();
    //assert aUpper.isValid();
    BoundedNatural aa = a.words(0,n/2); // this = a4
    //assert aa.isValid();

    // step 3: q1=aUpper/b, r1=aUpper%b
    final List<BoundedNatural> qr1 = divide3n2n(aUpper,b);
    //assert aa.isValid();
    //assert qr1.get(1).isValid();

    // step 4: quotient=[r1,this]/b, r2=[r1,this]%b
    aa = aa.add(qr1.get(1),32*(n/2));   // this = [r1,this]

    final List<BoundedNatural> qr2 = divide3n2n(aa,b);
    // step 5: let quotient=[q1,quotient] and return r2
    final BoundedNatural q2 = qr2.get(0).add(qr1.get(0), 32*(n/2));
    return List.of(q2,qr2.get(1)); }

  //--------------------------------------------------------------
  /** Returns a {@code BoundedNatural} containing
   * {@code blockLength} ints from {@code this} number, starting
   * at {@code index*blockLength}.<br/>
   * Used by Burnikel-Ziegler division.
   * @param index the block index
   * @param numBlocks the total number of blocks in {@code this}
   * @param blockLength length of one block in units of 32 bits
   */

  private static final BoundedNatural getBlock (final BoundedNatural u,
                                                final int index,
                                                final int numBlocks,
                                                final int blockLength) {
    final int blockStart = index * blockLength;
    if (blockStart >= u.hiInt()) { return u.zero(); }
    final int blockEnd;
    if (index == (numBlocks-1)) { blockEnd = u.hiInt(); }
    else { blockEnd = (index+1) * blockLength; }
    if (blockEnd > u.hiInt()) { return u.zero(); }
    return u.words(blockStart,blockEnd); }

  //--------------------------------------------------------------

  public static final List<BoundedNatural>
  divideAndRemainderBurnikelZiegler (final BoundedNatural u,
                                     final BoundedNatural v) {
    final int c = u.compareTo(v);
    if (0==c) { return List.of(u.one(),u.zero()); }
    if (0>c) { return List.of(u.zero(),u); }
    final int s = v.hiInt();

    // step 1: let m = min{2^k | (2^k)*BURNIKEL_ZIEGLER_THRESHOLD > s}
    final int s0 = s/BURNIKEL_ZIEGLER_THRESHOLD;
    final int m = 1 << (32-Integer.numberOfLeadingZeros(s0));

    final int j = ((s+m)-1) / m; // step 2a: j = ceil(s/m)
    final int n = j * m; // step 2b: block length in 32-bit units
    final long n32 = 32L * n; // block length in bits
    // step 3: sigma = max{T | (2^T)*B < beta^n}
    final int sigma = (int) Math.max(0, n32-v.hiBit());

    // step 4a: shift b so its length is a multiple of n
    //assert 0<=sigma;
    final BoundedNatural bShifted = v.shiftUp(sigma);
    // step 4b: shift a by the same amount
    final BoundedNatural aShifted = u.shiftUp(sigma);

    // step 5: t is the number of blocks needed to accommodate a
    // plus one additional bit
    final int t = Math.max(2,(int) ((aShifted.hiBit()+n32) / n32));

    // step 6: conceptually split a into blocks a[t-1], ..., a[0]
    // the most significant block of a
    final BoundedNatural a1 = getBlock(aShifted,t-1, t, n);

    // step 7: z[t-2] = [a[t-1], a[t-2]]
    // the second to most significant block
    BoundedNatural z = getBlock(aShifted,t-2, t, n);
    z = z.add(a1,32*n);   // z[t-2]

    // schoolbook division on blocks, dividing 2-block by 1-block
    BoundedNatural q = u.zero();
    for (int i=t-2;i>0;i--) {
      // step 8a: compute (qi,ri) such that z=b*qi+ri
      // Doesn't need modified z
      final List<BoundedNatural> qri = divide2n1n(z,bShifted);
      // step 8b: z = [ri, a[i-1]]
      z = getBlock(aShifted,i-1, t, n);   // a[i-1]
      z = z.add(qri.get(1), 32*n);
      // update q (part of step 9)
      q = q.add(qri.get(0),(i*n)<<5); }
    // final iteration of step 8: do the loop one more time
    // for i=0 but leave z unchanged
    //assert z.isValid();
    //assert bShifted.isValid();
    final List<BoundedNatural> qri = divide2n1n(z,bShifted);

    // step 9: a and b were shifted, so shift back
    return List.of(
      q.add(qri.get(0)),
      qri.get(1).shiftDown(sigma)); }

  //--------------------------------------------------------------

  public static final List<BoundedNatural>
  divideAndRemainder (final BoundedNatural u,
                      final BoundedNatural v) {
    //assert (! v.isZero());
    if (useKnuthDivision(u,v)) {
      return divideAndRemainderKnuth(u,v); }
    return divideAndRemainderBurnikelZiegler(u,v); }

  //--------------------------------------------------------------
  // gcd
  //--------------------------------------------------------------
  /** Algorithm B from Knuth section 4.5.2 */

  private static final BoundedNatural gcdKnuth (final BoundedNatural u,
                                                final BoundedNatural v) {
    //    BoundedNatural a = u.copy();
    //    BoundedNatural b = v.copy();
    BoundedNatural a = u;
    BoundedNatural b = v;
    //assert a.isValid();
    //assert b.isValid();
    //assert u.isValid();
    //assert v.isValid();
    // B1
    final int sa = a.loBit();
    final int s = Math.min(sa,b.loBit());
    if (s!=0) { a = a.shiftDown(s); b = b.shiftDown(s); }
    //assert a.isValid();
    //assert b.isValid();
    //assert u.isValid();
    //assert v.isValid();
    // B2
    int tsign = (s==sa) ? -1 : 1;
    BoundedNatural t = (0<tsign) ? a : b;
    for (int lb=t.loBit();lb>=0;lb=t.loBit()) {
      //assert a.isValid();
      //assert b.isValid();
      //assert u.isValid();
      //assert v.isValid();
      // B3 and B4
      //assert t.isValid() : Classes.className(t) + "\n" + t;
      t = t.shiftDown(lb);
      //assert t.isValid() : Classes.className(t) + "\n" + t;
      //assert a.isValid();
      //assert b.isValid() : Classes.className(b) + "\n" + b;
      //assert u.isValid();
      //assert v.isValid();
      // step B5
      if (0<tsign) { a = t; } else { b = t; }
      final int an = a.hiInt();
      final int bn = b.hiInt();
      if ((an<2) && (bn<2)) {
        final int x = a.word(an-1);
        final int y = b.word(bn-1);
        BoundedNatural r = BoundedNatural.valueOf(Ints.unsignedGcd(x,y));
        if (s > 0) { r = r.shiftUp(s); }
        //assert a.isValid();
        //assert b.isValid();
        //assert u.isValid();
        //assert v.isValid();
        return r; }
      // B6
      tsign = a.compareTo(b);
      if (0==tsign) {
        //assert a.isValid();
        //assert b.isValid();
        //assert u.isValid();
        //assert v.isValid();
        break; }
      else if (0<tsign) { a = a.subtract(b); t = a;  }
      else { b = b.subtract(a); t = b; }
      //assert a.isValid();
      //assert b.isValid();
      //assert u.isValid();
      //assert v.isValid();
    }
    //assert a.isValid();
    //assert b.isValid();
    //assert u.isValid();
    //assert v.isValid();
    if (s > 0) { a = a.shiftUp(s); }
    //assert a.isValid();
    //assert b.isValid();
    //assert u.isValid();
    //assert v.isValid();
    return a; }

  //--------------------------------------------------------------
  /** Use Euclid until the numbers are approximately the
   * same length, then use the Knuth algorithm.
   */

  public static final BoundedNatural gcd (final BoundedNatural u,
                                          final BoundedNatural v) {
    BoundedNatural a = u;
    BoundedNatural b = v;
    //assert a.isValid();
    //assert b.isValid();
    while (!b.isZero()) {
      if (Math.abs(a.hiInt()-b.hiInt()) < 2) { return gcdKnuth(a,b); }
      final List<BoundedNatural> qr = divideAndRemainder(a,b);
      //assert a.isValid();
      //assert b.isValid();
      a = b;
      b = qr.get(1); }
    return a; }

  public static final List<BoundedNatural> reduce (final BoundedNatural n0,
                                                   final BoundedNatural d0) {
    //assert n0.isValid();
    //assert d0.isValid();
    final int shift = Math.min(n0.loBit(),d0.loBit());
    final BoundedNatural n = ((shift != 0) ? n0.shiftDown(shift) : n0);
    final BoundedNatural d = ((shift != 0) ? d0.shiftDown(shift) : d0);
    //assert n.isValid();
    //assert d.isValid();
    if (n.equals(d)) { return List.of(n0.one(),n0.one()); }
    if (d.isOne()) { return List.of(n,n0.one()); }
    if (n.isOne()) { return List.of(n0.one(),d); }
    final BoundedNatural g = gcd(n,d);
    //assert g.isValid();
    //assert n.isValid();
    //assert d.isValid();
    if (g.compareTo(n.one()) > 0) {
      final BoundedNatural ng = n.divide(g);
      final BoundedNatural dg = d.divide(g);
      //assert ng.isValid();
      //assert dg.isValid();
      return List.of(ng,dg); }
    return List.of(n,d); }

  public static final List<BigInteger>
  reduce (final BigInteger n0,
          final BigInteger d0) {
    final int shift =
      Math.min(Numbers.loBit(n0),Numbers.loBit(d0));
    final BigInteger n =
      ((shift != 0) ? n0.shiftRight(shift) : n0);
    final BigInteger d =
      ((shift != 0) ? d0.shiftRight(shift) : d0);
    if (n.equals(d)) {
      return List.of(BigInteger.ONE,BigInteger.ONE); }
    if (d.equals(BigInteger.ONE)) {
      return List.of(n,BigInteger.ONE); }
    if (n.equals(BigInteger.ONE)) {
      return List.of(BigInteger.ONE,d); }
    final BigInteger g = n.gcd(d);
    if (g.compareTo(BigInteger.ONE) > 0) {
      final BigInteger ng = n.divide(g);
      final BigInteger dg = d.divide(g);
      return List.of(ng,dg); }
    return List.of(n,d); }

  //--------------------------------------------------------------
  // disable constructor
  //--------------------------------------------------------------

  private NaturalDivide () {
    throw new
    UnsupportedOperationException(
      "can't instantiate " + getClass().getCanonicalName()); }

  //--------------------------------------------------------------
}
//--------------------------------------------------------------
