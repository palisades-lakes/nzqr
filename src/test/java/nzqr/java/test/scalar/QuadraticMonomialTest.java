package nzqr.java.test.scalar;


import static nzqr.java.test.scalar.Common.affines;
import static nzqr.java.test.scalar.Common.constants;
import static nzqr.java.test.scalar.Common.cubics;
import static nzqr.java.test.scalar.Common.exact;
import static nzqr.java.test.scalar.Common.expand;
import static nzqr.java.test.scalar.Common.general;
import static nzqr.java.test.scalar.Common.otherFns;
import static nzqr.java.test.scalar.Common.quadraticKnots;
import static nzqr.java.test.scalar.Common.quadraticTestPts;
import static nzqr.java.test.scalar.Common.quadratics;

import java.util.List;
import java.util.function.BiFunction;

import org.junit.jupiter.api.Test;

import com.google.common.collect.Iterables;

import nzqr.java.functions.Domain;
import nzqr.java.functions.Function;
import nzqr.java.functions.scalar.QuadraticMonomial;

//----------------------------------------------------------------
/** Test monomial form parabolas. 
 * <p>
 * <pre>
 * mvn -q -Dtest=nzqr/java/test/scalar/QuadraticMonomialTest test > QuadraticMonomialTest.txt
 * </pre>
 *
 * @author palisades dot lakes at gmail dot com
 * @version 2018-10-08
 */

public final class QuadraticMonomialTest {

  @SuppressWarnings({ "static-method" })
  @Test
  public final void exactTests () {
    final Domain support = expand(quadraticTestPts);
    final List<BiFunction> factories = 
      List.of(QuadraticMonomial::interpolate);
    final Iterable<Function> functions = Iterables.concat(
        
      quadratics, affines, constants);
    for (final BiFunction factory : factories) {
      for (final Function f : functions) {
        //System.out.println();
        //System.out.println(f);
        for (final double[][] kn : quadraticKnots) {
          if (QuadraticMonomial.validKnots(kn)) {
//            System.out.println(
//              Arrays.toString(kn[0]) + 
//              ", " + 
//              Arrays.toString(kn[1]));
            exact(f,factory,kn,quadraticTestPts,support,
              6.0e5,3.0e7,4.0e7); } } } } }

  @SuppressWarnings({ "static-method" })
  @Test
  public final void generalTests () {
    final Domain support = expand(quadraticTestPts);
    final List<BiFunction> factories = 
      List.of(QuadraticMonomial::interpolate);
    final Iterable<Function> functions = Iterables.concat(
      cubics, otherFns);
    for (final BiFunction factory : factories) {
      for (final Function f : functions) {
        for (final double[][] kn : quadraticKnots) {
          if (QuadraticMonomial.validKnots(kn)) {
            general(f,factory,kn,support,
              1.0e0,3.0e0,4.0e4); } } } } }

  //--------------------------------------------------------------
}
//--------------------------------------------------------------


