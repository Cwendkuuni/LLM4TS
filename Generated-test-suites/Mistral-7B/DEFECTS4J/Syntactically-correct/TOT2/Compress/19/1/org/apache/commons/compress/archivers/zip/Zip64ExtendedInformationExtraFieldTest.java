import org.apache.commons.compress.archivers.zip.Zip64ExtendedInformationExtraField;
import org.apache.commons.compress.archivers.zip.ZipEightByteInteger;
import org.apache.commons.compress.archivers.zip.ZipLong;
import org.junit.Test;
import static org.junit.Assert.*;

public class Zip64ExtendedInformationExtraFieldTest {

    @Test
    public void testValidZip64ExtendedInformationExtraField() {
        Zip64ExtendedInformationExtraField zip64ExtendedInformationExtraField =
                new Zip64ExtendedInformationExtraField(new ZipEightByteInteger(1024),
                        new ZipEightByteInteger(512), null, null);
        assertEquals(1024, zip64ExtendedInformationExtraField.getSize().getValue());
        assertEquals(512, zip64ExtendedInformationExtraField.getCompressedSize().getValue());
        assertNull(zip64ExtendedInformationExtraField.getRelativeHeaderOffset());
        assertNull(zip64ExtendedInformationExtraField.getDiskStart());
    }

    @Test
    public void testValidZip64ExtendedInformationExtraFieldWithAllFields() {
        Zip64ExtendedInformationExtraField zip64ExtendedInformationExtraField =
                new Zip64ExtendedInformationExtraField(new ZipEightByteInteger(1024),
                        new ZipEightByteInteger(512),
                        new ZipEightByteInteger(4),
                        new ZipLong(1));
        assertEquals(1024, zip64ExtendedInformationExtraField.getSize().getValue());
        assertEquals(512, zip64ExtendedInformationExtraField.getCompressedSize().getValue());
        assertEquals(4, zip64ExtendedInformationExtraField.getRelativeHeaderOffset().getValue());
        assertEquals(1, zip64ExtendedInformationExtraField.getDiskStartNumber().getValue());
    }

    @Test
    public void testEmptyZip64ExtendedInformationExtraField() {
        Zip64ExtendedInformationExtraField zip64ExtendedInformationExtraField =
                new Zip64ExtendedInformationExtraField();
        assertEquals(0, zip64ExtendedInformationExtraField.getSize().getValue());
        assertEquals(0, zip64ExtendedInformationExtraField.getCompressedSize().getValue());
        assertNull(zip64ExtendedInformationExtraField.getRelativeHeaderOffset());
        assertNull(zip64ExtendedInformationExtraField.getDiskStart());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullSizeInConstructor() {
        new Zip64ExtendedInformationExtraField(null, new ZipEightByteInteger(512));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullCompressedSizeInConstructor() {
        new Zip64ExtendedInformationExtraField(new ZipEightByteInteger(1024), null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullRelativeHeaderOffsetInConstructor() {
        new Zip64ExtendedInformationExtraField(new ZipEightByteInteger(1024),
                new ZipEightByteInteger(512), null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullDiskStartInConstructor() {
        new Zip64ExtendedInformationExtraField(new ZipEightByteInteger(1024),
                new ZipEightByteInteger(512), null, new ZipLong(0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLfhMustHaveBothSizes() {
        new Zip64ExtendedInformationExtraField(new ZipEightByteInteger(1024), null);
    }
}