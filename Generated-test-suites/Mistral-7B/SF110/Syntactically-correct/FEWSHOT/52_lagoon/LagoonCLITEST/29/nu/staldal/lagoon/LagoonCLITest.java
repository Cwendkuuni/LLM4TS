package nu.staldal.lagoon;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Properties;
import org.xml.sax.SAXException;

import nu.staldal.xtree.TreeBuilder;
import nu.staldal.lagoon.core.AuthenticationException;
import nu.staldal.lagoon.core.AuthenticationMissingException;
import nu.staldal.lagoon.core.LagoonException;
import nu.staldal.lagoon.core.LagoonProcessor;

public class LagoonCLITest {

    @Test
    public void testMainWithInvalidArguments() {
        String[] args = new String[0];
        LagoonCLI.main(args);
        assertEquals("Syntax:\nnu.staldal.lagoon.LagoonCLI <property_file> [<interval>|build|force]\nnu.stagal.lagoon.LagoonCLI <sitemap_file> [<interval>|build|force]", System.out.getMetaData().getConsoleOutput());
    }

    @Test
    public void testMainWithInvalidInterval() {
        String[] args = new String[]{"property_file", "abc"};
        LagoonCLI.main(args);
        assertEquals("Syntax:\nnu.staldal.lagoon.LagoonCLI <property_file> [<interval>|build|force]\nnu.stagal.lagoon.LagoonCLI <sitemap_file> [<interval>|build|force]", System.out.getMetaData().getConsoleOutput());
    }

    @Test
    public void testMainWithNegativeInterval() {
        String[] args = new String[]{"property_file", "-1"};
        LagoonCLI.main(args);
        assertEquals("Syntax:\nnu.staldal.lagoon.LagoonCLI <property_file> [<interval>|build|force]\nnu.stagal.lagoon.LagoonCLI <sitemap_file> [<interval>|build|force]", System.out.getMetaData().getConsoleOutput());
    }

    @Test
    public void testMainWithValidArguments() {
        String[] args = new String[]{"property_file", "1"};
        String propertyFile = "property_file";
        Properties properties = new Properties();
        FileInputStream fis = mock(FileInputStream.class);
        when(fis.available()).thenReturn(1024);
        when(fis.readAllBytes()).thenReturn("targetURL=http://localhost:8080/".getBytes());
        when(fis.close()).thenReturn(true);
        when(fis.readLine()).thenAnswer(i -> "targetURL=" + i.getIntValue());
        when(fis.getPropertyNames()).thenReturn(new String[]{"targetURL"});
        when(TreeBuilder.parseXML(Mockito.any(InputStream.class), Mockito.eq(false))).thenReturn(Mockito.mock(Element.class));
        when(new File(propertyFile).exists()).thenReturn(true);
        when(new FileInputStream(new File(propertyFile))).thenReturn(fis);
        when(new File(getProperty(propertyFile, "sitemapFile"))).thenReturn(new File("sitemap_file"));
        when(new PrintWriter(Mockito.any(OutputStream.class), Mockito.anyBoolean())).thenReturn(Mockito.mock(PrintWriter.class));
        when(new PrintWriter(Mockito.any(OutputStream.class), Mockito.anyBoolean())).thenReturn(Mockito.mock(PrintWriter.class));
        LagoonCLI.main(args);
        assertEquals("Lagoon initialized successfully", System.out.getMetaData().getConsoleOutput());
    }

    @Test
    public void testMainWithInvalidPropertyFile() {
        String[] args = new String[]{"invalid_property_file", "1"};
        LagoonCLI.main(args);
        assertEquals("Error while initializing Lagoon:\nFile not found: invalid_property_file", System.out.getMetaData().getConsoleOutput());
    }

    @Test
    public void testMainWithInvalidPassword() {
        String[] args = new String[]{"property_file", "password"};
        AuthenticationException authenticationException = mock(AuthenticationException.class);
        when(authenticationException.getMessage()).thenReturn("Incorrect password");
        when(new FileInputStream(new File("property_file"))).thenThrow(new AuthenticationException("Incorrect password"));
        LagoonCLI.main(args);
        assertEquals("Error while initializing Lagoon:\nIncorrect password", System.out.getMetaData().getConsoleOutput());
    }

    @Test
    public void testMainWithIOException() {
        String[] args = new String[]{"property_file", "1"};
        IOException ioException = mock(IOException.class);
        when(ioException.getMessage()).thenReturn("I/O error: test error message");
        when(new FileInputStream(new File("property_file"))).thenThrow(ioException);
        LagoonCLI.main(args);
        assertEquals("Error while initializing Lagoon:\nI/O error: test error message", System.out.getMetaData().getConsoleOutput());
    }

    @Test
    public void testMainWithLagoonException() {
        String[] args = new String[]{"property_file", "1"};
        LagoonException lagoonException = mock(LagoonException.class);
        when(lagoonException.getMessage()).thenReturn("Error while initializing Lagoon
    ");
        when(new FileInputStream(new File("property_file"))).thenThrow(lagoonException);
        LagoonCLI.main(args);
        assertEquals("Error while initializing Lagoon:\nError while initializing Lagoon", System.out.getMetaData().getConsoleOutput());
    }

    @Test
    public void testMainWithBuildMode() {
        String[] args = new String[]{"property_file", "build"};
        LagoonCLI.main(args);
        String expectedOutput = "Building website...\nLagoon initialized successfully\n";
        assertEquals(expectedOutput, System.out.getMetaData().getConsoleOutput());
    }

    @Test
    public void testMainWithForceBuildMode() {
        String[] args = new String[]{"property_file", "force"};
        LagoonCLI.main(args);
        String expectedOutput = "Force building website...\nLagoon initialized successfully\n";
        assertEquals(expectedOutput, System.out.getMetaData().getConsoleOutput());
    }

    @Test
    public void testMainWithInterval() {
        String[] args = new String[]{"property_file", "5000"};
        LagoonCLI.main(args);
        String expectedOutput = "Building website...\nin 5.002 s\nReady\n";
        assertEquals(expectedOutput, System.out.getMetaData().getConsoleOutput());
    }

    @Test
    public void testMainWithInput() {
        String[] args = new String[]{"property_file"};
        LagoonCLI.main(args);
        String expectedOutput = "Ready\n";
        assertEquals(expectedOutput, System.out.getMetaData().getConsoleOutput());

        String input = "b";
        String[] argsWithInput = new String[]{"property_file"};
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        LagoonCLI.main(argsWithInput);
        String expectedOutputWithBuild = "Building website...\nin 4.003 s\nReady\n";
        assertEquals(expectedOutputWithBuild, System.out.getMetaData().getConsoleOutput());

        input = "f";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        LagoonCLI.main(argsWithInput);
        String expectedOutputWithForceBuild = "Force building website...\nin 3.003 s\nReady\n";
        assertEquals(expectedOutputWithForceBuild, System.out.getMetaData().getConsoleOutput());

        input = "q";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        LagoonCLI.main(argsWithInput);
        assertEquals(expectedOutput, System.out.getMetaData().getConsoleOutput());
    }

    private String getProperty(String propertyFile, String propertyName) throws LagoonException {
        Properties properties = new Properties();
        try (FileInputStream fis = new FileInputStream(new File(propertyFile))) {
            properties.load(fis);
        }
        return properties.getProperty(propertyName).trim();
    }
}