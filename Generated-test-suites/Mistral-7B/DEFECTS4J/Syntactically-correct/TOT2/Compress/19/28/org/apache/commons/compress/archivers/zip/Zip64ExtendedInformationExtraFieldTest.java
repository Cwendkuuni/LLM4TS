import org.apache.commons.compress.archivers.zip.Zip64ExtendedInformationExtraField;
import org.apache.commons.compress.archivers.zip.ZipEightByteInteger;
import org.apache.commons.compress.archivers.zip.ZipLong;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

class Zip64ExtendedInformationExtraFieldTest {

 @Test
 void testConstructorWithSizes() {
 ZipEightByteInteger originalSize = new ZipEightByteInteger((short) 0x1234);
 ZipEightByteInteger compressedSize = new ZipEightByteInteger((short) 0x5678);
 Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField(originalSize, compressedSize);

 assertEquals(originalSize.getValue(), extraField.getSize().getValue());
 assertEquals(compressedSize.getValue(), extraField.getCompressedSize().getValue());
 }

 @Test
 void testParseLocalFileDataWithValidData() {
 byte[] localFileData = new byte[]{(byte) 0x00, (byte) 0x01, (byte) 0x02, (byte) 0x03,
 (byte) 0x04, (byte) 0x05, (byte) 0x06, (byte) 0x07, (byte) 0x08, (byte) 0x09, (byte) 0x0a,
 (byte) 0x0b, (byte) 0x0c, (byte) 0x0d, (byte) 0x0e, (byte) 0x0f};

 Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
 extraField.parseFromLocalFileData(localFileData, 0, localFileData.length);

 assertEquals(new ZipEightByteInteger((short) 0x1234).getValue(), extraField.getSize().getValue());
 assertEquals(new ZipEightByteInteger((short) 0x5678).getValue(), extraField.getCompressedSize().getValue());
 }

 @Test
 void testParseLocalFileDataWithInvalidData() {
 byte[] localFileData = new byte[]{};

 Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
 assertThrows(ZipException.class, () -> extraField.parseFromLocalFileData(localFileData, 0, localFileData.length));
 }

 @Test
 void testParseLocalFileDataWithPartialData() {
 byte[] localFileData = new byte[]{(byte) 0x00, (byte) 0x01, (byte) 0x02, (byte) 0x03};

 Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
 assertThrows(ZipException.class, () -> extraField.parseFromLocalFileData(localFileData, 0, localFileData.length));
 }

 @Test
 void testParseCentralDirectoryDataWithValidData() {
 byte[] centralDirectoryData = new byte[]{(byte) 0x01, (byte) 0x02, (byte) 0x03, (byte) 0x04,
 (byte) 0x05, (byte) 0x06, (byte) 0x07, (byte) 0x08, (byte) 0x09, (byte) 0x0a, (byte) 0x0b,
 (byte) 0x0c, (byte) 0x0d, (byte) 0x0e, (byte) 0x0f};

 Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
 extraField.parseFromCentralDirectoryData(centralDirectoryData, 0, centralDirectoryData.length);

 assertEquals(new ZipEightByteInteger((short) 0x1234).getValue(), extraField.getSize().getValue());
 assertEquals(new ZipEightByteInteger((short) 0x5678).getValue(), extraField.getCompressedSize().getValue());
 }

 @Test
 void testParseCentralDirectoryDataWithNoData() {
 byte[] centralDirectoryData = new byte[]{(byte) 0x01};

 Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
 extraField.parseFromCentralDirectoryData(centralDirectoryData, 0, centralDirectoryData.length);

 assertTrue(extraField.size == null);
 assertTrue(extraField.compressedSize == null);
 assertTrue(extraField.relativeHeaderOffset == null);
 assertTrue(extraField.diskStart == null);
 }

 @Test
 void testParseCentralDirectoryDataWithPartialData() {
 byte[] centralDirectoryData = new byte[]{(byte) 0x01, (byte) 0x02, (byte) 0x03, (byte) 0x04};

 Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
 extraField.parseFromCentralDirectoryData(centralDirectoryData, 0, centralDirectoryData.length);

 assertTrue(extraField.size == null);
 assertTrue(extraField.compressedSize == null);
 assertTrue(extraField.relativeHeaderOffset == null);
 assertTrue(extraField.diskStart == null);
 }

 @Test
 void testParseCentralDirectoryDataWithInvalidData() {
 byte[] centralDirectoryData = new byte[]{(byte) 0x01, (byte) 0x02, (byte) 0x03, (byte) 0x04, (byte) 0x05};

 Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
 assertThrows(ZipException.class, () -> extraField.parseFromCentralDirectoryData(centralDirectoryData, 0, centralDirectoryData.length));
 }
}