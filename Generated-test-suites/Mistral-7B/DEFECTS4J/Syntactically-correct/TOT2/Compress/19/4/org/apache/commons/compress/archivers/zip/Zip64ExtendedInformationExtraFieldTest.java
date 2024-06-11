import org.apache.commons.compress.archivers.zip.Zip64ExtendedInformationExtraField;
import org.apache.commons.compress.archivers.zip.ZipEightByteInteger;
import org.apache.commons.compress.archivers.zip.ZipLong;
import org.apache.commons.compress.archivers.zip.ZipException;
import org.junit.Test;
import static org.junit.Assert.*;

public class Zip64ExtendedInformationExtraFieldTest {

    @Test
    public void testConstructorWithSizes() {
        ZipEightByteInteger size = new ZipEightByteInteger(12345L);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(67890L);
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField(size, compressedSize);
        assertEquals(size.getValue(), extraField.getSize().getValue());
        assertEquals(compressedSize.getValue(), extraField.getCompressedSize().getValue());
    }

    @Test
    public void testConstructorWithAllValues() {
        ZipEightByteInteger size = new ZipEightByteInteger(12345L);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(67890L);
        ZipEightByteInteger relativeHeaderOffset = new ZipEightByteInteger(1234);
        ZipLong diskStart = new ZipLong(123456L);
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField(size, compressedSize, relativeHeaderOffset, diskStart);
        assertEquals(size.getValue(), extraField.getSize().getValue());
        assertEquals(compressedSize.getValue(), extraField.getCompressedSize().getValue());
        assertEquals(relativeHeaderOffset.getValue(), extraField.getRelativeHeaderOffset().getValue());
        assertEquals(diskStart.getValue(), extraField.getDiskStartNumber().getValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullSize() {
        new Zip64ExtendedInformationExtraField(null, new ZipEightByteInteger(12345L));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullCompressedSize() {
        new Zip64ExtendedInformationExtraField(new ZipEightByteInteger(12345L), null);
    }

    @Test(expected = ZipException.class)
    public void testParseFromLocalFileDataWithInvalidLength() {
        byte[] buffer = new byte[4];
        new Zip64ExtendedInformationExtraField().parseFromLocalFileData(buffer, 0, 4);
    }

    @Test(expected = ZipException.class)
    public void testParseFromLocalFileDataWithMissingSizeValues() {
        byte[] buffer = new byte[8];
        new Zip64ExtendedInformationExtraField().parseFromLocalFileData(buffer, 0, 8);
    }

    @Test
    public void testParseFromLocalFileDataWithValidData() {
        byte[] buffer = new byte[16];
        new ZipShort(0x0001).getBytes(buffer, 0);
        ZipEightByteInteger size = new ZipEightByteInteger(12345L);
        size.getBytes(buffer, 4);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(67890L);
        compressedSize.getBytes(buffer, 12);
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromLocalFileData(buffer, 0, 16);
        assertEquals(size.getValue(), extraField.getSize().getValue());
        assertEquals(compressedSize.getValue(), extraField.getCompressedSize().getValue());
    }

    @Test(expected = ZipException.class)
    public void testParseFromCentralDirectoryDataWithInvalidLength() {
        byte[] buffer = new byte[4];
        new Zip64ExtendedInformationExtraField().parseFromCentralDirectoryData(buffer, 0, 4);
    }

    @Test
    public void testParseFromCentralDirectoryDataWithValidData() {
        byte[] buffer = new byte[24];
        ZipEightByteInteger size = new ZipEightByteInteger(12345L);
        size.getBytes(buffer, 0);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(67890L);
        compressedSize.getBytes(buffer, 8);
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromCentralDirectoryData(buffer, 0, 24);
        assertEquals(size.getValue(), extraField.getSize().getValue());
        assertEquals(compressedSize.getValue(), extraField.getCompressedSize().getValue());
    }
}