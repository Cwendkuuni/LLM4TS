package nu.staldal.lagoon.test;

import static org.junit.Assert.*;
import nu.staldal.lagoon.*;
import org.xml.sax.SAXException;
import java.io.*;
import java.util.*;
import org.junit.Test;

public class LagoonCLITest {

 @Test
 public void testValidArguments() throws Exception {
 LagoonCLI.main(new String[]{"test.properties"});
 assertEquals("Lagoon initialized successfully", System.out.readAllLines(new ByteArrayInputStream(new byte[0]))[0]);
 }

 @Test(expected = LagoonException.class)
 public void testMissingPropertyFile() throws Exception {
 LagoonCLI.main(new String[0]);
 fail("Lagoon initialized successfully should not be printed");
 }

 @Test(expected = NumberFormatException.class)
 public void testInvalidIntervalArgument() throws Exception {
 LagoonCLI.main(new String[]{"test.properties", "-1"});
 fail("Lagoon initialized successfully should not be printed");
 }

 @Test
 public void testValidIntervalArgument() throws Exception {
 LagoonCLI.main(new String[]{"test.properties", "5000"});
 // Check if the expected messages are printed
 List<String> actual = Arrays.asList(System.out.readAllLines(new ByteArrayInputStream(new byte[0])));
 List<String> expected = Arrays.asList(
 "Initializing Lagoon...",
 "Lagoon initialized successfully"
 );
 assertEquals(expected, actual);
 }

 @Test
 public void testInvalidBuildOrForceArgument() throws Exception {
 LagoonCLI.main(new String[]{"test.properties", "invalid"});
 fail("Lagoon initialized successfully should not be printed");
 }

 @Test
 public void testValidBuildArgument() throws Exception {
 LagoonCLI.main(new String[]{"test.properties", "build"});
 // Check if the expected messages are printed
 List<String> actual = Arrays.asList(System.out.readAllLines(new ByteArrayInputStream(new byte[0])));
 List<String> expected = Arrays.asList(
 "Initializing Lagoon...",
 "Building website...",
 "...with errors...",
 "Exiting Lagoon..."
 );
 assertEquals(expected, actual);
 }

 @Test
 public void testValidForceArgument() throws Exception {
 LagoonCLI.main(new String[]{"test.properties", "force"});
 // Check if the expected messages are printed
 List<String> actual = Arrays.asList(System.out.readAllLines(new ByteArrayInputStream(new byte[0])));
 List<String> expected = Arrays.asList(
 "Initializing Lagoon...",
 "Force building website...",
 "...with errors...",
 "Exiting Lagoon..."
 );
 assertEquals(expected, actual);
 }

 @Test(expected = SAXException.class)
 public void testInvalidSitemapFile() throws Exception {
 LagoonCLI.main(new String[]{"test.xml"});
 fail("Lagoon initialized successfully should not be printed");
 }

 @Test
 public void testValidSitemapFile() throws Exception {
 LagoonCLI.main(new String[]{"test.sitemap"});
 // Check if the expected messages are printed
 List<String> actual = Arrays.asList(System.out.readAllLines(new ByteArrayInputStream(new byte[0])));
 List<String> expected = Arrays.asList(
 "Initializing Lagoon...",
 "Lagoon initialized successfully"
 );
 assertEquals(expected, actual);
 }

 @Test
 public void testMissingPasswordProperty() throws Exception {
 LagoonCLI.main(new String[]{"test.properties"});
 // Check if an error message is printed
 List<String> actual = Arrays.asList(System.err.readAllLines(new ByteArrayInputStream(new byte[0])));
 List<String> expected = Arrays.asList(
 "Error while initializing Lagoon:"
 );
 assertTrue(actual.get(0).startsWith("Error while initializing Lagoon:"));
 }

 @Test
 public void testInvalidPasswordProperty() throws Exception {
 Properties properties = new Properties();
 properties.setProperty("password", "invalid");
 LagoonCLI.main(new String[]{"test.properties"});
 // Check if an error message is printed
 List<String> actual = Arrays.asList(System.err.readAllLines(new ByteArrayInputStream(new byte[0])));
 List<String> expected = Arrays.asList(
 "Error while initializing Lagoon:"
 );
 assertTrue(actual.get(0).startsWith("Error while initializing Lagoon:"));
 }

 @Test
 public void testValidPasswordProperty() throws Exception {
 Properties properties = new Properties();
 properties.setProperty("password", "correct");
 LagoonCLI.main(new String[]{"test.properties"});
 // Check if the expected messages are printed
 List<String> actual = Arrays.asList(System.out.readAllLines(new ByteArrayInputStream(new byte[0])));
 List<String> expected = Arrays.asList(
 "Initializing Lagoon...",
 "Lagoon initialized successfully"
 );
 assertEquals(expected, actual);
 }
}