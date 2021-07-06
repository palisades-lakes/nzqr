package nzqr.java.test.functions;

import static java.lang.Math.exp;
import static java.lang.Math.fma;
import static java.lang.Math.sqrt;

import java.util.Arrays;

import nzqr.java.functions.AffineFunctional;
import nzqr.java.functions.Dn;
import nzqr.java.functions.Domain;
import nzqr.java.functions.Function;
import nzqr.java.functions.Functional;
import nzqr.java.functions.Vektor;

/** See driverX.c in CG_DESCENT-C-6.8.
 *
 * Note: immutable.
 *
 * @author palisades dot lakes at gmail dot com
 * @version 2018-09-07
 */

public final class DriverFunction extends Functional {

  private static final int N = 100;
  private static final Domain DOMAIN = Dn.get(N);
  private static final double YMIN = -6.530786727330618E+02;
  private static final double[] XMIN =
    new double[]
      { 3.357159956708078E-9, 0.34657359046111835,
        0.5493061445090943, 0.6931471794686216, 0.8047189584878601,
        0.8958797323433025, 0.9729550736643026, 1.0397207714132342,
        1.0986122884575538, 1.1512925456097691, 1.1989476360186506,
        1.242453325354528, 1.2824746794262754, 1.3195286650746747,
        1.3540251002627595, 1.386294360629963, 1.4166066717767183,
        1.4451858791343155, 1.4722194900915297, 1.4978661373140894,
        1.522261219157847, 1.5455212266266303, 1.5677471076418017,
        1.5890269147751672, 1.6094379121612148, 1.6290482689839878,
        1.6479184332219252, 1.6661022554513876, 1.6836479153494546,
        1.7005986910422832, 1.7169936022338368, 1.7328679511783325,
        1.7482537803774196, 1.7631802619353514, 1.7776740304707255,
        1.791759469131639, 1.8054589564251988, 1.818793080129534,
        1.8317808234152315, 1.8444397273947377, 1.8567860335904935,
        1.8688348092256326, 1.8806000577645325, 1.8920948167410578,
        1.9033312445921236, 1.9143206979495502, 1.925073800623441,
        1.9356005053273144, 1.9459101490424164, 1.956011502790791,
        1.9659128164795527, 1.9756218593887884, 1.9851459567996477,
        1.994492023194955, 2.003666592409978, 2.0126758450640914,
        2.0215256335635883, 2.030221504930719, 2.0387687216833292,
        2.0471722809625934, 2.055436932082862, 2.06356719265713,
        2.0715673634338763, 2.0794415419656924, 2.087193635216984,
        2.0948273712067755, 2.1023463097730226, 2.1097538525364925,
        2.11705325213495, 2.124247620791812, 2.1313399382773506,
        2.138333059314893, 2.145229720479073, 2.152032546628155,
        2.158744056907743, 2.165366670359014, 2.1719027111609144,
        2.1783544135328454, 2.1847239263220604, 2.191013317298385,
        2.1972245771778307, 2.2033596233958854, 2.209420303650612,
        2.215408399234746, 2.2213256281746103, 2.2271736481916196,
        2.2329540594995487, 2.2386684074476753, 2.2443181850169274,
        2.2499048351737705, 2.255429753085515, 2.2608942882015426,
        2.266299746207908, 2.271647390867577, 2.2769384457640176,
        2.2821740959698515, 2.287355489661744, 2.2924837396942235,
        2.297559925126127, 2.302585092664204};

  //--------------------------------------------------------------

  public static final double[] start () {
    final double[] x = new double[N];
    Arrays.fill(x,1.0);
    return x;}

  public static final double[] trueMinimizer () {
    return XMIN.clone(); }

  public static final double trueMinimum () { return YMIN; }

  //--------------------------------------------------------------
  // Function methods
  //--------------------------------------------------------------

  @Override
  public final Domain domain () { return DOMAIN; }

  //--------------------------------------------------------------

  //  private static final double mexp (final double x) {
  //    return pow(E,x); }

  private static final double mexp (final double x) {
    return exp(x); }

  //--------------------------------------------------------------

  @Override
  public final double doubleValue (final Vektor x) {
    final int n = domain().dimension();
    assert n == x.dimension();
    double y = 0.0;
    final double[] xx = x.unsafeCoordinates();
    for (int i=0;i<n;i++) {
      final double t0 = i+1;
      final double t1 = -sqrt(t0);
      final double xi = xx[i];
      final double exi = mexp(xi);
      y +=  fma(t1,xi,exi);  }
    return y; }

  @Override
  public final Vektor gradient (final Vektor x) {
    final int n = domain().dimension();
    assert n == x.dimension();
    final double[] xx = x.unsafeCoordinates();
    final double[] g = new double[n];
    for (int i=0;i<n;i++) {
      final double t0 = i+1;
      final double t1 = - sqrt(t0);
      final double xi = xx[i];
      final double exi = mexp(xi);
      g[i] = exi + t1; } 
    return Vektor.unsafeMake(g); }

  @Override
  public final Function tangentAt (final Vektor x) {
    final int n = domain().dimension();
    assert n == x.dimension();
    final double[] xx = x.unsafeCoordinates();
    final double[] g = new double[n];
    double y = 0.0;
    for (int i=0;i<n;i++) {
      final double t0 = i+1;
      final double t1 = - sqrt(t0);
      final double xi = xx[i];
      final double exi = mexp(xi);
      final double fi =  fma(t1,xi,exi);
      y +=  fi;
      final double gi = exi + t1;
      g[i] = gi;  }
    return AffineFunctional.make(Vektor.unsafeMake(g),y); }

  //--------------------------------------------------------------
  // construction
  //--------------------------------------------------------------

  private DriverFunction () { super(); }

  public static final DriverFunction get () {
    return new DriverFunction(); }

  //--------------------------------------------------------------
}  // end class
//--------------------------------------------------------------
