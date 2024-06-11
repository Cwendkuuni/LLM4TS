import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import geo.google.datamodel.*;
import geo.google.mapping.AddressToUsAddressFunctor;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RunWith(Parameterized.class)
public class AddressToUsAddressFunctorTest {

 private GeoAddress inputAddress;
 private GeoUsAddress expectedOutput;

 public AddressToUsAddressFunctorTest(GeoAddress input, GeoUsAddress expected) {
 this.inputAddress = input;
 this.expectedOutput = expected;
 }

 @Parameters
 public static List<Object[]> data() {
 return Arrays.asList(new Object[][]{
 // Test case 1: Valid address with country, state, city, postal code, and address line
 new GeoAddress("123 Main St, Anytown, Anystate 12345, USA", new Coordinate(40.7128, -74.0060)),
 new GeoUsAddress("Anystate", "Anytown", "12345", "USA", "123 Main St", "Anytown", "40.7128", "-74.0060", 5),

 // Test case 2: Valid address with country, state, city, postal code, and no address line
 new GeoAddress("123 Anystreet, Anytown, Anystate 12345, USA", new Coordinate(40.7128, -74.0060)),
 new GeoUsAddress("Anystate", "Anytown", "12345", "USA", null, "Anytown", "40.7128", "-74.0060", 5),

 // Test case 3: Valid address with country, no state, city, postal code, and address line
 new GeoAddress("123 Main St, Anytown, USA 12345", new Coordinate(40.7128, -74.0060)),
 new GeoUsAddress("USA", "Anytown", "12345", null, "123 Main St", "Anytown", "40.7128", "-74.0060", 5),

 // Test case 4: Valid address with country, state, no city, postal code, and address line
 new GeoAddress("123 Main St, Anystate 12345, USA", new Coordinate(40.7128, -74.0060)),
 new GeoUsAddress("Anystate", null, "12345", "USA", "123 Main St", null, "40.7128", "-74.0060", 5),

 // Test case 5: Valid address with country, state, city, no postal code, and address line
 new GeoAddress("123 Main St, Anytown, Anystate, USA", new Coordinate(40.7128, -74.0060)),
 new GeoUsAddress("Anystate", "Anytown", null, "USA", "123 Main St", "Anytown", "40.7128", null, 5),

 // Test case 6: Valid address with country, state, city, postal code, and no address line
 new GeoAddress("123 Anystreet, Anytown, Anystate 12345, USA", new Coordinate(40.7128, -74.0060)),
 new GeoUsAddress("Anystate", "Anytown", "12345", "USA", null, "Anytown", "40.7128", "-74.0060", 5),

 // Test case 7: Invalid address with no country
 new GeoAddress(null, new Coordinate(40.7128, -74.0060)),
 new GeoUsAddress(null, null, null, null, null, null, null, null, 0)
 });
 }

 @Test
 public void testExecute() {
 GeoUsAddress output = AddressToUsAddressFunctor.getInstance().execute(inputAddress);
 Assert.assertEquals(expectedOutput, output);
 }
}