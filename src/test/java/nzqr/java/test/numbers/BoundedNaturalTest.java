package nzqr.java.test.numbers;

import java.math.BigInteger;

import org.junit.jupiter.api.Test;

import nzqr.java.numbers.BoundedNatural;
import nzqr.java.test.Common;

//----------------------------------------------------------------
/** Test desired properties of natural number implementations.
 * <p>
 * <pre>
 * mvn -q -Dtest=xfp/java/test/numbers/BoundedNaturalTest test > NT.txt
 * </pre>
 *
 * @author palisades dot lakes at gmail dot com
 * @version 2022-10-30
 */

public final class BoundedNaturalTest {

  @SuppressWarnings({ "static-method" })
  @Test
  public final void boundedNatural () {
    //Debug.DEBUG=false;

    final BigInteger u0 = new BigInteger("744fe560",0x10);
    final BigInteger u1 = new BigInteger("2e95eed2",0x10);
    Common.boundedNaturalTest(
      BoundedNatural::valueOf,
      BoundedNatural::valueOf,
      (z) -> z.toBigInteger(),
      u0,u1);

    final BigInteger z0 = new BigInteger(
      "744fe560022d5b34420df8e6bc5a25cbb19378b537a89241cf1d8b54b29bf2887af660876d52b97b7c37e552e259f01ba525b05b829f4be794bdf365a7941c698109f501e0a72b288c343436704cb00fbdbf690fa2296621820af9bd22d1e73446acdcce698f34a83978d538c4d4e45ad026034ae52cc34150511f3115b66942e21bf5142dfa6204c10661456230097d4a8fce0bcb64c9bc54861397d27d1a0aac41fa486f7f3e0f9f982764ddfd1841e8e694c814785997073d9e4cd140a6707ae0a73bf7bea5a489825564295462f2afb258c5d69b5dfa2dcb0a57ff270e1cc306f952156d36f64b3033f1b1e33712133d9fcb282cb07466f81bf4f4a6a54cec132e27e451b1655614d8d5a392de4fa9c892e0f5b611276c075265d64fa77f2293dd29b77900b1853da16136b8e196cd9bc8adac938593cc9e18850b8b0f64f11f3c2d4706a8fe00b38eccc971842e3ab118ff8a45dc49f8baebeef705745f5f43efa42026511ab9a43b0e4d1509fd0f7136c2b7a050cdfcf10b191d32c0ccbf6bc34a47b4980a6872200fea60d6d71597c40227539601a266c778048e2c1c0e7f85e0b7b03a4cec02abf2aba4a43409c7bb135cb88a26a2e8a1efefdeca956f18976474b6360b9abfc13982d8fc151a90b4a7cddd085af8cd68c18fd81d688c50e1fb4a7ae258af2253b655192ece12b523ea15633e1105c01bb5b5b6dcb9fd9bdc9f9011b6ffebc2db1b9f54d0c1a214fd1ecbfe74ee5520bf69444b74f2c0ae7b880f3c349f085a2cd9d19f0af8b33f5217c57b2aab46136c8942280cde57be7ba36d21cfd333a6d3ccc9b58e34eef1b8d18bd4d5f5d6dc4c9e5e548ee50dbfbbc71ce9fc9b455d81cbfd8f6327092e0ef2ce511d0b633157cb12cadfeb4f72cfea4c566d8ab98287119fa9d930b784a518b09d00f2e5bac6220988d5149e634a185ce28984a032dcc06c42b329c26aa4667b3db72a1c32a373a74aaacb52ebc202f2c5ebb0b0c37a9e22a96bc992da322f47470cdcb22a0b354395edbdca05206c19f2d65b4cc2b1693f33e620d495d5821e6dddaa9bd9acb61535f689ba2ad5f5401ebab8a250550e6d191b22e374cd7897504ad631760594a7c3f13be7a25b3ad22278c2d8f4dfd62ef5997f6aa0c6628c5518e12d1af147b06fb084471bf6a495d649f404c54215f52b2427729b15c69129c5ce1cd47e37e99ccb47ff5de8be8916632c40061fe5d4a7fa8d0543d17712172b53bf38690a39c7da393e606b87567edd3634d0bddbc448925d79cee59ccdfb4f175839f5466052337882613c1135d76d2ec11e5e53450c5bb37ca2e5073d14178535f258c5c8337afa5293bf24a504e5f1fd54f09ef5cf3b466e59bca5381f90522e88335687e931cf0f4c95f0fe9dd7400187d69d617e8260a3533a0b93ae2648bb8b67b686b50215",
      0x10);
    final BigInteger z1 = new BigInteger(
      "2e95eed2885cf8947f8dd2ebeedd33540b791bd49a724ec8cedf2d01222e9dc8ed8a4bfb370eb1485102e5600f3e950a5195e2195ab56af3b41e518870ac21c3cab340bc9b7e3ae4e35faa6ef615b544fe645155cd490121db31c81a1f27b08fd817f3292f30a0678edf413676d200dfb701dfa6c6002e522234d6956d6f4391b7c4659200b54ac3a27e3a16b290268f39a99a1143184ff09ae3f93788a5bd8181a6303a7d30fbb5c0139ae04d2152826c0b158629f8ae80cc795746ff90284db8a64aebd1bea47cac0332bca800aeaf4e457b449195feb8e7c82124f84ad72824410f76b77475ab8f8fa413aed062907c0334756274f597dcfa0aef23e7226c2847babf55df656c12acc3292c63402ed8ce325658b2eba45065f274252c7886407e464baeac3a78743f8b567974937b53b181330f30f2d46465364b6d8625a8823c418827559f7076d7da6e160799c84c28d6a575d50c75127e1aad8a174057370fcc72b8b9fc2f6955778228751fc2d5360e95a560ff2ce6646009570d4226644b3e9cb98d005523057f3a8313c533aba8164d3c7969d970c9baa1203651286ce90895a7403a50af86954a784c47a13e946af56eb2e6340d73a936f3ba8f0ea98991a3a51510609c22046d4722b2fe4c1cfc5d28c93f47f84ab70c496feb95d94fa80c93c36037e3f9249018baa6123b0000fcc4c146ac7cba5b9926fb9dd9bf47ff3dd43925fe992c796f7e60b69eaf8e687d6f737db0bcb16ac764eb46f001e1a16953ef225b4e4dbcd3d211ec0e2d17a0432a1f9b25f0dc4f2b160e124271a229a59e1874b5eaf63983d1de07bd340f9f00674664a719999c6c271c3fa8ea7cd5bd89629a79faf95849cbb80b1dec4e24b86ca667fa09c72f0474d169dccbd31524fdbfada5be787044e68cbd706858c8a55432575395d951b4219de55a17a88fb2a5ec1798389c249d001fd292493a0a2cf4cc670e26e7371f65ef42926a3efb854102122b03c0343aa0837872c463f64afbabb24c395ed2ac2a80572093ce26de9213cf26c76efe354f96e259622280eedbfaf72f4b9614760e25c4ef6efd6f93c978794c4c155412364aa4eab4d7e844fd18b139da769d57be0fb6fffc98ee033c73531269a13879d6ab4e5944e5256a4b54973be8c8a23744a01a2f172f0c3bf946451264bc1d98d8a88dd21e6042f52ce041d20b9471b26a93198662ac1538c3a5b7ed3acdcbbb91005b0c0bc9666c1d39b1cd80e3dfe3e9bc113cab746af6f955e291d4e95cb0108c4449dd2d4e23682b3498c3479d69d6b2a89abb8b6a37485d8c46c0de0783ec71ebed0a20e55cd12fc174053b478c1e2e6ab70f0498b25a1289e57f928650a1a7466faa3ad9ae1af5078e32fe76b7f4b5b4191409595e9074767db6e8efcc4cd99694c9f42fb4d43253db80278e7acf39ccc8",
      0x10);
    Common.boundedNaturalTest(
      BoundedNatural::valueOf,
      BoundedNatural::valueOf,
      (z) -> z.toBigInteger(),
      z0,z1);

    Common.boundedNaturalTest(
      BoundedNatural::valueOf,
      BoundedNatural::valueOf,
      (z) -> z.toBigInteger());

    //Debug.DEBUG=false;
  }

  //--------------------------------------------------------------
}
//--------------------------------------------------------------