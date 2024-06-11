import org.apache.commons.compress.compressors.CompressorException;
import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.CompressorOutputStream;
import org.apache.commons.compress.compressors.CompressorStreamFactory;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Tests for {@link CompressorStreamFactory} class.
 */
public class CompressorStreamFactoryTest {

    @Test
    public void testCreateCompressorInputStream() throws IOException, CompressorException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CompressorOutputStream cos = new CompressorStreamFactory().createCompressorOutputStream(CompressorStreamFactory.BZIP2, out);

        ByteArrayInputStream in = new ByteArrayInputStream(new byte[10]);
        CompressorInputStream cin = new CompressorStreamFactory().createCompressorInputStream(CompressorStreamFactory.BZIP2, in);

        IOUtils.copy(cin, cos);
        cin.close();
        cos.close();

        Assert.assertEquals(10, out.size());
    }

    @Test
    public void testCreateCompressorInputStream_Gzip() throws IOException, CompressorException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CompressorOutputStream cos = new CompressorStreamFactory().createCompressorOutputStream(CompressorStreamFactory.GZIP, out);

        ByteArrayInputStream in = new ByteArrayInputStream(new byte[10]);
        CompressorInputStream cin = new CompressorStreamFactory().createCompressorInputStream(CompressorStreamFactory.GZIP, in);

        IOUtils.copy(cin, cos);
        cin.close();
        cos.close();

        Assert.assertEquals(10, out.size());
    }

    @Test(expected = CompressorException.class)
    public void testCreateCompressorInputStream_InvalidFormat() throws IOException {
        ByteArrayInputStream in = new ByteArrayInputStream(new byte[10]);
        new CompressorStreamFactory().createCompressorInputStream("invalid", in);
    }

    @Test
    public void testCreateCompressorOutputStream() throws IOException, CompressorException {
        ByteArrayInputStream in = new ByteArrayInputStream(new byte[10]);
        CompressorInputStream cin = new CompressorStreamFactory().createCompressorInputStream(CompressorStreamFactory.BZIP2, in);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CompressorOutputStream cos = new CompressorStreamFactory().createCompressorOutputStream(CompressorStreamFactory.BZIP2, out);

        IOUtils.copy(cin, cos);
        cin.close();
        cos.close();

        Assert.assertEquals(10, out.size());
    }

    @Test
    public void testCreateCompressorOutputStream_Gzip() throws IOException, CompressorException {
        ByteArrayInputStream in = new ByteArrayInputStream(new byte[10]);
        CompressorInputStream cin = new CompressorStreamFactory().createCompressorInputStream(CompressorStreamFactory.GZIP, in);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CompressorOutputStream cos = new CompressorStreamFactory().createCompressorOutputStream(CompressorStreamFactory.GZIP, out);

        IOUtils.copy(cin, cos);
        cin.close();
        cos.close();

        Assert.assertEquals(10, out.size());
    }

    @Test
    public void testCreateCompressorOutputStream_InvalidFormat() throws IOException, CompressorException {
        ByteArrayInputStream in = new ByteArrayInputStream(new byte[10]);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            new CompressorStreamFactory().createCompressorOutputStream("invalid", out);
            Assert.fail();
        } catch (CompressorException e) {
            // expected
        }
    }
}