package nu.staldal.lagoon.test;

import static org.junit.Assert.*;
import nu.staldal.lagoon.*;
import org.xml.sax.*;
import org.xml.sax.helpers.*;
import java.io.*;
import java.util.*;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;

public class LagoonCLITest {

 @Before
 public void setUp() {
 // Initialize mocks
 properties = new Properties();
 sitemapTree = mock(XMLDocument.class);
 processor = mock(LagoonProcessor.class);
 }

 @After
 public void tearDown() {
 // Reset mocks
 reset(sitemapTree, processor);
 }

 private Properties properties;
 private XMLDocument sitemapTree;
 private LagoonProcessor processor;

 @Test
 public void testInvalidArgs() {
 String[] args = new String[0];
 LagoonCLI.main(args);
 assertEquals("Syntax:\nnu.staldal.lagoon.LagoonCLI <property_file> [<interval>|build|force]\nnu.staldal.lagoon.LagoonCLI <sitemap_file> [<interval>|build|force]", System.out.getHWB().toString());
 }

 @Test
 public void testInvalidArgsSize() {
 String[] args = new String[]{"foo"};
 LagoonCLI.main(args);
 assertEquals("Syntax:\nnu.staldal.lagoon.LagoonCLI <property_file> [<interval>|build|force]\nnu.staldal.lagoon.LagoonCLI <sitemap_file> [<interval>|build|force]", System.out.getHWB().toString());
 }

 @Test
 public void testInvalidInterval() {
 String[] args = new String[]{"properties.xml", "abc"};
 LagoonCLI.main(args);
 assertEquals("Syntax:\nnu.staldal.lagoon.LagoonCLI <property_file> [<interval>|build|force]\nnu.staldal.lagoon.LagoonCLI <sitemap_file> [<interval>|build|force]", System.out.getHWB().toString());
 }

 @Test
 public void testValidArgsWithSitemapFile() {
 String[] args = new String[]{"sitemap.xml"};
 LagoonCLI.main(args);
 verify(processor, times(1)).init(properties, sitemapTree, null, null);
 }

 @Test
 public void testValidArgsWithPropertyFile() {
 String[] args = new String[]{"config.properties"};
 LagoonCLI.main(args);
 verify(processor, times(1)).init(properties, null, null, null);
 }

 @Test
 public void testValidArgsWithBuild() {
 String[] args = new String[]{"properties.xml", "build"};
 LagoonCLI.main(args);
 verify(processor, times(1)).build(false);
 }

 @Test
 public void testValidArgsWithForce() {
 String[] args = new String[]{"properties.xml", "force"};
 LagoonCLI.main(args);
 verify(processor, times(1)).build(true);
 }

 @Test
 public void testValidArgsWithInterval() {
 String[] args = new String[]{"properties.xml", "15000"};
 LagoonCLI.main(args);
 verify(processor, times(1)).build(false);
 try {
 Thread.sleep(15001);
 } catch (InterruptedException e) {
 // Do nothing
 }
 }

 @Test(expected = LagoonException.class)
 public void testMissingPassword() {
 String[] args = new String[]{"config.properties"};
 when(processor.init(properties, null, null, null)).thenReturn(true);
 LagoonCLI.main(args);
 fail("Expected LagoonException to be thrown");
 }

 @Test(expected = AuthenticationException.class)
 public void testIncorrectPassword() {
 String[] args = new String[]{"config.properties"};
 when(processor.init(properties, null, null, null)).thenThrow(new AuthenticationException("Incorrect password"));
 LagoonCLI.main(args);
 fail("Expected AuthenticationException to be thrown");
 }

 @Test(expected = FileNotFoundException.class)
 public void testMissingPropertyFile() {
 String[] args = new String[]{"foo.xml"};
 when(processor.init(properties, null, null, null)).thenThrow(new FileNotFoundException("File not found: foo.xml"));
 LagoonCLI.main(args);
 fail("Expected FileNotFoundException to be thrown");
 }

 @Test(expected = IOException.class)
 public void testIOError() {
 String[] args = new String[]{"config.properties"};
 when(processor.init(properties, null, null, null)).thenThrow(new IOException("I/O error: test"));
 LagoonCLI.main(args);
 fail("Expected IOException to be thrown");
 }

 @Test
 public void testBuildSuccess() {
 String[] args = new String[]{"properties.xml", "build"};
 when(processor.build(false)).thenReturn(true);
 LagoonCLI.main(args);
 verify(processor, times(1)).build(false);
 }

 @Test
 public void testBuildFailure() {
 String[] args = new String[]{"properties.xml", "build"};
 when(processor.build(false)).thenReturn(false);
 LagoonCLI.main(args);
 verify(processor, times(1)).build(false);
 }

 @Test
 public void testForceBuildSuccess() {
 String[] args = new String[]{"properties.xml", "force"};
 when(processor.build(true)).thenReturn(true);
 LagoonCLI.main(args);
 verify(processor, times(1)).build(true);
 }

 @Test
 public void testForceBuildFailure() {
 String[] args = new String[]{"properties.xml", "force"};
 when(processor.build(true)).thenReturn(false);
 LagoonCLI.main(args);
 verify(processor, times(1)).build(true);
 }

 @Test
 public void testWaitingForInterval() {
 String[] args = new String[]{"properties.xml", "500"};
 LagoonCLI.main(args);
 verify(processor, times(1)).build(false);
 try {
 Thread.sleep(501);
 } catch (InterruptedException e) {
 // Do nothing
 }
 }

 private void verifyInitialization(boolean usePropertyFile, boolean expectException) {
 String[] args;
 if (usePropertyFile) {
 args = new String[]{"properties.xml"};
 } else {
 args = new String[]{"config.properties"};
 }
 LagoonCLI.main(args);
 if (expectException) {
 verify(processor, times(1)).init(properties, null, null, null);
 fail("Expected initialization to fail");
 } else {
 verify(processor, times(1)).init(properties, sitemapTree, null, null);
 }
 }

 @Test
 public void testInitializationWithSitemapFile() {
 verifyInitialization(false, false);
 }

 @Test
 public void testInitializationWithPropertyFileNoPassword() {
 verifyInitialization(true, false);
 }

 @Test(expected = AuthenticationException.class)
 public void testInitializationWithPropertyFileWithMissingPassword() {
 verifyInitialization(true, true);
 }

 @Test(expected = AuthenticationException.class)
 public void testInitializationWithPropertyFileWithIncorrectPassword() {
 String[] args = new String[]{"config.properties"};
 when(processor.init(properties, null, null, null)).thenThrow(new AuthenticationException("Incorrect password"));
 LagoonCLI.main(args);
 fail("Expected AuthenticationException to be thrown");
 }

 @Test(expected = FileNotFoundException.class)
 public void testInitializationWithPropertyFileWithMissingFile() {
 String[] args = new String[]{"foo.properties"};
 when(processor.init(properties, null, null, null)).thenThrow(new FileNotFoundException("File not found: foo.properties"));
 LagoonCLI.main(args);
 fail("Expected FileNotFoundException to be thrown");
 }

 @Test(expected = IOException.class)
 public void testInitializationWithPropertyFileWithIODisabled() {
 String[] args = new String[]{"config.properties"};
 when(processor.init(properties, null, null, null)).thenThrow(new IOException("I/O error: test"));
 LagoonCLI.main(args);
 fail("Expected IOException to be thrown");
 }

 @Test
 public void testInitializationWithPropertyFileWithValidCredentials() {
 String[] args = new String[]{"config.properties"};
 when(processor.init(properties, null, null, null)).thenReturn(true);
 LagoonCLI.main(args);
 verify(processor, times(1)).init(properties, null, null, null);
 }

 @Test
 private void testShowTime() {
 long timeBefore = System.currentTimeMillis() - 5000;
 LagoonCLI.showTime(10);
 String output = System.out.getHWB().toString();
 assertTrue(output.contains("in 5 s"));
 long timeAfter = System.currentTimeMillis();
 LagoonCLI.showTime(timeAfter - timeBefore);
 String output2 = System.out.getHWB().toString();
 assertTrue(output2.contains("in " + (timeAfter - timeBefore) + " ms"));
 }
}