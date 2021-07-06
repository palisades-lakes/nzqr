package nzqr.java.test.scalar;

import static nzqr.java.test.scalar.Common.affineKnots;
import static nzqr.java.test.scalar.Common.affineTestPts;
import static nzqr.java.test.scalar.Common.affines;
import static nzqr.java.test.scalar.Common.constants;
import static nzqr.java.test.scalar.Common.cubics;
import static nzqr.java.test.scalar.Common.exact;
import static nzqr.java.test.scalar.Common.expand;
import static nzqr.java.test.scalar.Common.general;
import static nzqr.java.test.scalar.Common.otherFns;
import static nzqr.java.test.scalar.Common.quadratics;

import java.util.List;
import java.util.function.BiFunction;

import org.junit.jupiter.api.Test;

import com.google.common.collect.Iterables;

import nzqr.java.functions.Domain;
import nzqr.java.functions.Function;
import nzqr.java.functions.scalar.AffineFunctional;

//----------------------------------------------------------------
/** Test affine interpolants. 
 * <p>
 * <pre>
 * mvn -q -Dtest=nzqr/java/test/scalar/AffineFunctional1dTest test > AffineFunctional1dTest.txt
 * </pre>
 *
 * @author palisades dot lakes at gmail dot com
 * @version 2018-10-09
 */

public final class AffineFunctionalTest {

  @SuppressWarnings({ "static-method" })
  @Test
  public final void exactTests () {
    final Domain support = expand(affineTestPts);
    final List<BiFunction> factories = 
      List.of(AffineFunctional::interpolate);
    final Iterable<Function> functions = Iterables.concat(
      affines, constants);
    for (final BiFunction factory : factories) {
      for (final Function f : functions) {
        //System.out.println();
        //System.out.println(f);
        for (final double[][] kn : affineKnots) {
          //System.out.println(Arrays.toString(kn));
          exact(f,factory,kn,affineTestPts,support,
            1.0e0,4.0e2,3.0e2); } } } }

  @SuppressWarnings({ "static-method" })
  @Test
  public final void generalTests () {
    final Domain support = expand(affineTestPts);
    final List<BiFunction> factories = 
      List.of(AffineFunctional::interpolate);
    final Iterable<Function> functions = Iterables.concat(
      cubics,  quadratics, otherFns);
    for (final BiFunction factory : factories) {
      for (final Function f : functions) {
        for (final double[][] kn : affineKnots) {
          general(f,factory,kn,support,1.0e0,2.0e2,3.0e0); } } } }

  //--------------------------------------------------------------
}
//--------------------------------------------------------------
