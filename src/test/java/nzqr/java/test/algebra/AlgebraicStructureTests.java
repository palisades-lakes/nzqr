package nzqr.java.test.algebra;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;
import java.util.function.Predicate;
import java.util.function.Supplier;

import org.junit.jupiter.api.Test;

import com.google.common.collect.ImmutableMap;

import nzqr.java.algebra.Set;
import nzqr.java.algebra.Structure;
import nzqr.java.numbers.BigFloats;
import nzqr.java.numbers.Doubles;
import nzqr.java.numbers.Floats;
import nzqr.java.numbers.Q;
import nzqr.java.numbers.RationalFloats;
import nzqr.java.prng.PRNG;

//----------------------------------------------------------------
/** <pre>
 * mvn -q -Dtest=nzqr/java/test/algebra/AlgebraicStructureTests test > AST.txt
 * </pre>
 * @author palisades dot lakes at gmail dot com
 * @version 2021-07-08
 */

@SuppressWarnings("unchecked")
public final class AlgebraicStructureTests {
  private static final int TRYS = 31;
  static final int SPACE_TRYS = 5;

  //--------------------------------------------------------------

  private static final void
  structureTests (final Structure s,
                  final int n) {
    SetTests.tests(s,n);
    final Map<Set,Supplier> generators =
      s.generators(
        ImmutableMap.of(
          Set.URP,
          PRNG.well44497b("seeds/Well44497b-2019-01-09.txt")));
    for(final Predicate law : s.laws()) {
      for (int i=0; i<n; i++) {
        final boolean result = law.test(generators);
        assertTrue(result,
          s.toString() + " : " + law.toString()); } } }

  //--------------------------------------------------------------

  @SuppressWarnings({ "static-method" })
  @Test
  public final void tests () {

    //Debug.DEBUG=false;
    structureTests(BigFloats.ADDITIVE_MAGMA,TRYS);
    structureTests(BigFloats.MULTIPLICATIVE_MAGMA,TRYS);
    structureTests(BigFloats.RING,TRYS);

    structureTests(RationalFloats.ADDITIVE_MAGMA,TRYS);
    structureTests(RationalFloats.MULTIPLICATIVE_MAGMA,TRYS);
    structureTests(RationalFloats.FIELD,TRYS);

    structureTests(Q.FIELD,TRYS);

    structureTests(Floats.ADDITIVE_MAGMA,TRYS);
    structureTests(Floats.MULTIPLICATIVE_MAGMA,TRYS);
    structureTests(Floats.FLOATING_POINT,TRYS);

    structureTests(Doubles.ADDITIVE_MAGMA,TRYS);
    structureTests(Doubles.MULTIPLICATIVE_MAGMA,TRYS);
    structureTests(Doubles.FLOATING_POINT,TRYS);

    //Debug.DEBUG=false;
  }


  //--------------------------------------------------------------
}
//--------------------------------------------------------------
