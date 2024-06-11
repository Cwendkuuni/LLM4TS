import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.junit.Test;

public class LagoonCLITest {

    @Test
    public void testMainWithValidArguments() throws IOException {
        String[] args = new String[]{"property_file.xml", "build"};

        System.setProperty("user.dir", "target");
        File sourceDir = new File("target");
        File sitemapFile = new File("property_file.xml");
        Properties properties = new Properties();
        FileInputStream fis = mock(FileInputStream.class);
        when(fis.readAllBytes()).thenReturn("targetURL=http://test.com\nsitemapFile=sitemap.xml\nsourceDir=target\npassword=test".getBytes());

        LagoonCLI.main(args);

        assertEquals("http://test.com", LagoonCLI.getProperty("targetURL"));
        assertEquals(sitemapFile, LagoonCLI.properties.getProperty("sitemapFile"));
        assertEquals(sourceDir, LagoonCLI.properties.getProperty("sourceDir"));
        assertNull(LagoonCLI.properties.getProperty("password"));
    }
}