package geo.google.mapping;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.apache.commons.collections.CollectionUtils.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import geo.google.datamodel.*;
import geo.google.mapping.AddressToUsAddressFunctor;

@RunWith(Parameterized.class)
public class AddressToUsAddressFunctorTest {

 private GeoAddress inputAddress;
 private GeoUsAddress expectedOutput;

 public AddressToUsAddressFunctorTest(GeoAddress input, GeoUsAddress output) {
 this.inputAddress = input;
 this.expectedOutput = output;
 }

 @Parameterized.Parameters(name = "{index}: inputAddress={0}, expectedOutput={1}")
 public static Iterable<Object[]> data() {
 return Arrays.asList(new Object[][] {
 // Test case 1
 new Object[] { new GeoAddress("1600 Amphitheatre Parkway, Mountain View, CA 94043, USA", "37.422828,-122.084081", AddressType.HOME, "1234567890"),
 new GeoUsAddress("1600 Amphitheatre Parkway", "Mountain View", "CA", "94043") },
 // Test case 2
 new Object[] { new GeoAddress("1600 Amphitheatre Parkway, Mountain View, CA 94043, USA", "37.422828,-122.084081", AddressType.HOME, "1234567890"),
 new GeoUsAddress("1600 Amphitheatre Parkway", null, null, null) },
 // Test case 3
 new Object[] { new GeoAddress("1600 Amphitheatre Parkway, Mountain View, CA 94043, USA", "37.422828,-122.084081", AddressType.HOME, "1234567890"),
 new GeoUsAddress() },
 // Test case 4
 new Object[] { new GeoAddress("", null, null, null),
 new GeoUsAddress() },
 // Test case 5
 new Object[] { new GeoAddress("1600 Amphitheatre Parkway, Mountain View, CA 94043, USA", "37.422828,-122.084081", AddressType.HOME, "1234567890"),
 new GeoUsAddress("1600 Amphitheatre Parkway", "Mountain View", "CA", "94043", new GeoCoordinate(37.422828, -122.084081)) },
 // Test case 6
 new Object[] { new GeoAddress("1600 Amphitheatre Parkway, Mountain View, CA, USA", "37.422828,-122.084081", AddressType.HOME, "1234567890"),
 new GeoUsAddress("1600 Amphitheatre Parkway", "Mountain View", "CA", null) },
 // Test case 7
 new Object[] { new GeoAddress("1600 Amphitheatre Parkway, Mountain View, CA 94043, USA", "37.422828,-122.084081", AddressType.HOME, "1234567890"),
 new GeoUsAddress("1600 Amphitheatre Parkway", "Mountain View", null, null) },
 // Test case 8
 new Object[] { new GeoAddress("1600 Amphitheatre Parkway, Mountain View, CA 94043, USA", "37.422828,-122.084081", AddressType.HOME, "1234567890"),
 new GeoUsAddress("1600 Amphitheatre Parkway", null, null, null) },
 // Test case 9
 new Object[] { new GeoAddress("1600 Amphitheatre Parkway, Mountain View, CA 94043, USA", "37.422828,-122.084081", AddressType.HOME, "1234567890"),
 new GeoUsAddress("1600 Amphitheatre Parkway", "Mountain View", "CA", "94043", null) },
 // Test case 10
 new Object[] { new GeoAddress("1600 Amphitheatre Parkway, Mountain View, CA 94043, USA", "37.422828,-122.084081", AddressType.HOME, "1234567890"),
 new GeoUsAddress("1600 Amphitheatre Parkway", "Mountain View", null, "94043") }
 });
 }

 @Test
 public void testExecute() {
 GeoUsAddress output = AddressToUsAddressFunctor.getInstance().execute(inputAddress);
 assertEquals(expectedOutput, output);
 }
}