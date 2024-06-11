import org.junit.Test;
import org.junit.Assert;
import static org.junit.Assert.*;
import nu.staldal.lagoon.*;
import nu.staldal.lagoon.core.*;
import java.io.*;
import static org.mockito.Mockito.*;

public class LagoonCLITest {

 private static final String SYNTAX_MSG = "Syntax:\n" +
         "nu.staldal.lagoon.LagoonCLI <property_file> [<interval>|build|force]\n" +
         "nu.staldal.lagoon.LagoonCLI <sitemap_file> [<interval>|build|force]";
 private static Properties properties;

 @Test
 public void testInvalidUsage() {
 String[] args = new String[0];
 LagoonCLI.main(args);
 String actual = System.out.getFD().readAllBytes();
 String expected = SYNTAX_MSG.getBytes();
 assertArrayEquals(expected, actual);
 }

 @Test
 public void testValidUsageWithPropertyFileAndBuild() {
 String[] args = {"propertyFile.xml", "build"};
 LagoonCLI.main(args);
 verify(System.out, times(3)).println(anyString());
 verifyNoMoreInteractions(System.out);
 }

 @Test
 public void testValidUsageWithPropertyFileAndForce() {
 String[] args = {"propertyFile.xml", "force"};
 LagoonCLI.main(args);
 verify(System.out, times(4)).println(anyString());
 verifyNoMoreInteractions(System.out);
 }

 @Test
 public void testValidUsageWithPropertyFileAndInterval() {
 String[] args = {"propertyFile.xml", "100"};
 LagoonCLI.main(args);
 verify(System.out, times(5)).println(anyString());
 verifyNoMoreInteractions(System.out);
 }

 @Test(expected = LagoonException.class)
 public void testInvalidInterval() {
 String[] args = {"propertyFile.xml", "-100"};
 LagoonCLI.main(args);
 }

 @Test
 public void testValidUsageWithSitemapFileAndBuild() {
 String[] args = {"sitemapFile.xml", "build"};
 LagoonCLI cli = createMockLagoonCLI();
 when(cli.initialize(anyString(), any(File.class), any(File.class), anyString(), any(PrintWriter.class), any(PrintWriter.class))).thenReturn(true);
 doNothing().when(cli).build(anyBoolean());
 LagoonCLI.main(args);
 verify(cli).initialize(anyString(), any(File.class), any(File.class), anyString(), any(PrintWriter.class), any(PrintWriter.class));
 verify(cli).build(false);
 }

 @Test
 public void testValidUsageWithSitemapFileAndForce() {
 String[] args = {"sitemapFile.xml", "force"};
 LagoonCLI cli = createMockLagoonCLI();
 when(cli.initialize(anyString(), any(File.class), any(File.class), anyString(), any(PrintWriter.class), any(PrintWriter.class))).thenReturn(true);
 doThrow(new LagoonException("Test exception")).when(cli).build(anyBoolean());
 LagoonCLI.main(args);
 verify(cli).initialize(anyString(), any(File.class), any(File.class), anyString(), any(PrintWriter.class), any(PrintWriter.class));
 verify(cli).build(true);
 }

 @Test
 public void testValidUsageWithSitemapFileAndInterval() {
 String[] args = {"sitemapFile.xml", "100"};
 LagoonCLI cli = createMockLagoonCLI();
 when(cli.initialize(anyString(), any(File.class), any(File.class), anyString(), any(PrintWriter.class), any(PrintWriter.class))).thenReturn(true);
 doNothing().when(cli).build(anyBoolean());
 LagoonCLI.main(args);
 verify(cli).initialize(anyString(), any(File.class), any(File.class), anyString(), any(PrintWriter.class), any(PrintWriter.class));
 }

 private LagoonCLI createMockLagoonCLI() {
 LagoonCLI cli = mock(LagoonCLI.class);
 return cli;
 }

}