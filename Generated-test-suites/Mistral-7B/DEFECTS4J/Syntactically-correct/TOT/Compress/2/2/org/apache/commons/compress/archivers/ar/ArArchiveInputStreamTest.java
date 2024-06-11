import org.apache.commons.compress.archivers.ArArchiveEntry;
import org.apache.commons.compress.archivers.ArArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class ArArchiveInputStreamTest {

    @Test
    public void testGetNextEntry() throws IOException {
        final InputStream testData = new ByteArrayInputStream(
                "3c21 7261 6863 0a3e6865 6174 6572 000a 0000 0201 0000 0020 0a00 4164 6464 6461 6265 6572 756e 7461 6d70 6f72 7461 6b65 7900 0a3e 6865 6174 6572 000a 416c 656d 656e 7461 7274 6572 000a 0000 0021 0a00 5468 6973 2074 6578 7420 7368 6f75 7473 6174 2073 7461 7465 5374 726f 6674 6572 7300 0a3e 6578 656e 636f 6666 696c 6520 6172 7265 6374 696f 6e20 7374 6174 7574 5468 6973 2074 6578 7420 7368 6f6d 6520 6172 7265 6374 696f 6e20 6865 6163 686f 7374 6861 7273 0a0a".getBytes(StandardCharsets.US_ASCII));
        final ArArchiveInputStream tested = new ArArchiveInputStream(testData);

        ArchiveEntry nextEntry = tested.getNextEntry();
        Assert.assertNotNull(nextEntry);
        Assert.assertEquals("name1.ext", nextEntry.getName());
        Assert.assertNotNull(nextEntry.getSize());
        Assert.assertEquals(2048L, nextEntry.getSize().longValue());

        nextEntry = tested.getNextEntry();
        Assert.assertNotNull(nextEntry);
        Assert.assertEquals("name2.ext", nextEntry.getName());
        Assert.assertNotNull(nextEntry.getSize());
        Assert.assertEquals(4096L, nextEntry.getSize().longValue());

        nextEntry = tested.getNextEntry();
        Assert.assertNull(nextEntry);

        IOUtils.closeQuietly(tested);
    }

    @Test
    public void testGetNextEntryWithInvalidHeader() throws IOException {
        final InputStream testData = new ByteArrayInputStream(
                "3c21 7261 6863 0a3e6865 6174 6572 000a 0000 0201 0000 0020 0a00 4164 6464 6461 6265 6572 756e 7461 6d70 6f72 7461 6b55 000a 3e".getBytes(StandardCharsets.US_ASCII));
        final ArArchiveInputStream tested = new ArArchiveInputStream(testData);

        try {
            tested.getNextArEntry();
            Assert.fail("expected IOException");
        } catch (IOException e) {
            // expected
        }

        IOUtils.closeQuietly(tested);
    }

    @Test
    public void testGetNextEntryWithInvalidLength() throws IOException {
        final InputStream testData = new ByteArrayInputStream(
                "3c21 7261 6863 0a3e6865 6174 6572 000a 0000 0201 0000 0020 0a00 4164 6464 6461 6265 6572 756e 7461 6d70 6f72 7461 6b65 7900 0a3e 6865 6174 6572 000a 416c 656d 656e 7461 7274 6572 000a 0000 0021 0a00 5468 6973 2074 6578 7420 7368 6f75 7473 6174 2073 7461 7465 5374 726f 6674 6572 7300 0a3e 6578 656e 636f 6666 696c 6520 6172 7265 6374 696f 6e20 7374 6174 7574 5468 6973 2074 6578 7420 7368 6f6d 556e 0a0a".getBytes(StandardCharsets.US_ASCII));
        final ArArchiveInputStream tested = new ArArchiveInputStream(testData);

        try {
            tested.getNextArEntry();
            Assert.fail("expected IOException");
        } catch (IOException e) {
            // expected
        }

        IOUtils.closeQuietly(tested);
    }

    @Test
    public void testGetNextEntryWithNullInput() {
        final ArArchiveInputStream tested = new ArArchiveInputStream(null);

        try {
            tested.getNextArEntry();
            Assert.fail("expected IOException");
        } catch (IOException e) {
            // expected
        }

        IOUtils.closeQuietly(tested);
    }
}