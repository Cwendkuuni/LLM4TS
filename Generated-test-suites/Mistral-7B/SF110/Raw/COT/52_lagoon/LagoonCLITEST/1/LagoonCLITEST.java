package nu.staldal.lagoon; // Make sure to import necessary packages

import org.junit.Test;
import org.junit.Assert.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class LagoonCLITest {

    

    @Test
    public void testMainWithNoArguments() {
        String[] args = new String[0];
        LagoonCLI.main(args);
        assertEquals("Syntax:\nnu.staldal.lagoon.LagoonCLI <property_file> [<interval>|build|force]\nnu.staldal.lagoon.LagoonCLI <sitemap_file> [<interval>|build|force]", System.out.get湖Output().trim());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMainWithInvalidArgument() {
        String[] args = new String[]{"invalid_argument"};
        LagoonCLI.main(args);
    }

    @Test
    public void testMainWithPropertyFile() {
        String[] args = new String[]{"property_file.xml"};
        LagoonCLI.main(args);
        assertNull(LagoonCLI.properties); // properties should not be initialized in this case
    }

    @Test
    public void testMainWithInvalidInterval() {
        String[] args = new String[]{"property_file.xml", "-1"};
        LagoonCLI.main(args);
        assertEquals("Syntax:\nnu.staldal.lagoon.LagoonCLI <property_file> [<interval>|build|force]\nnu.staldal.lagoon.LagoonCLI <sitemap_file> [<interval>|build|force]", System.out.get湖Output().trim());
    }

    @Test
    public void testMainWithValidInterval() {
        String[] args = new String[]{"property_file.xml", "1000"};
        LagoonCLI.main(args);
        assertTrue(args[1].matches("\\d+"));
    }

    @Test(expected = LagoonException.class)
    public void testMainWithInvalidPassword() throws Exception {
        String[] args = new String[]{"property_file.xml"};
        LagoonCLI.properties = mock(Properties.class);
        when(LagoonCLI.properties.getProperty("password")).thenReturn("invalid_password");
        LagoonCLI.main(args);
        verify(System.err).println("Error while initializing Lagoon:");
        verify(System.err).println("Incorrect password");
    }

    @Test(expected = FileNotFoundException.class)
    public void testMainWithMissingPropertyFile() {
        String[] args = new String[]{"missing_property_file.xml"};
        LagoonCLI.main(args);
        verify(System.err).println("Error while initializing Lagoon:");
        verify(System.err).println("File not found: missing_property_file.xml");
    }

    @Test(expected = IOException.class)
    public void testMainWithMissingSitemapFile() throws Exception {
        String[] args = new String[]{"property_file.xml"};
        LagoonCLI.main(args);
        verify(System.err).println("Error while initializing Lagoon:");
        verify(System.err).println("File not found:");
    }

    @Test
    public void testMainWithValidArguments() {
        String[] args = new String[]{"property_file.xml", "5000"};
        LagoonCLI.main(args);
        assertNotNull(LagoonCLI.properties);
        assertEquals("5000", args[1]);
    }

    
}