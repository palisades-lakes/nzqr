package nzqr.java.test.scalar;

import static nzqr.java.test.scalar.Common.affines;
import static nzqr.java.test.scalar.Common.constants;
import static nzqr.java.test.scalar.Common.cubics;
import static nzqr.java.test.scalar.Common.exact;
import static nzqr.java.test.scalar.Common.expand;
import static nzqr.java.test.scalar.Common.general;
import static nzqr.java.test.scalar.Common.hermiteKnots;
import static nzqr.java.test.scalar.Common.hermiteTestPts;
import static nzqr.java.test.scalar.Common.otherFns;
import static nzqr.java.test.scalar.Common.quadratics;

import java.util.List;
import java.util.function.BiFunction;

import org.junit.jupiter.api.Test;

import com.google.common.collect.Iterables;

import nzqr.java.functions.Domain;
import nzqr.java.functions.Function;
import nzqr.java.functions.scalar.CubicHermite;

//----------------------------------------------------------------
/** Test 2 point cubic hermite interpolation. 
 * <p>
 * <pre>
 * mvn -q -Dtest=nzqr/java/test/scalar/CubicHermiteTest test > CubicHermiteTest.txt
 * </pre>
 *
 * @author palisades dot lakes at gmail dot com
 * @version 2018-10-09
 */

public final class CubicHermiteTest {

   @SuppressWarnings({ "static-method" })
  @Test
  public final void exactTests () {
    final Domain support = expand(hermiteTestPts);
    final List<BiFunction> factories = 
      List.of(CubicHermite::interpolate);
    final Iterable<Function> functions = Iterables.concat(
      cubics, quadratics, affines, constants);
    for (final BiFunction factory : factories) {
      for (final Function f : functions) {
        //System.out.println();
        //System.out.println(f);
        for (final double[][] kn : hermiteKnots) {
          if (CubicHermite.validKnots(kn)) {
            //System.out.println(
            //  Arrays.toString(kn[0]) + 
            //  ", " + 
            //  Arrays.toString(kn[1]));
            exact(f,factory,kn,hermiteTestPts,support,
              4.0e5,2.0e9,3.0e9); } } } } }

  @SuppressWarnings({ "static-method" })
  @Test
  public final void generalTests () {
    final Domain support = expand(hermiteTestPts);
    final List<BiFunction> factories = 
      List.of(CubicHermite::interpolate);
    final Iterable<Function> functions = Iterables.concat(otherFns);
    for (final BiFunction factory : factories) {
      for (final Function f : functions) {
        for (final double[][] kn : hermiteKnots) {
          if (CubicHermite.validKnots(kn)) {
            general(f,factory,kn,support,
              1.0e0,1.0e0,1.0e0); } } } } }

  //--------------------------------------------------------------
}
//--------------------------------------------------------------
