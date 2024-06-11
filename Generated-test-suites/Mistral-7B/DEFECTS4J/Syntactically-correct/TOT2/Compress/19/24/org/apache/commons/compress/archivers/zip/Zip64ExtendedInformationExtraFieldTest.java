import org.apache.commons.compress.archivers.zip.*;
import org.apache.commons.compress.archivers.zip.extension.Zip64ExtendedInformationExtraField;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class Zip64ExtendedInformationExtraFieldTest {

 @BeforeAll
 static void setupMocks() {
 ZipShort HEADER_ID = new ZipShort(0x0001);
 when(ZipShort.valueOf((byte) 0x00, (byte) 0x01)).thenReturn(HEADER_ID);
 }

 @Test
 void testCreateZip64ExtendedInformationExtraField() {
 // Given
 ZipEightByteInteger originalSize = new ZipEightByteInteger(0x100L);
 ZipEightByteInteger compressedSize = new ZipEightByteInteger(0x80L);
 ZipEightByteInteger relativeHeaderOffset = new ZipEightByteInteger(0x200L);
 ZipLong diskStart = new ZipLong(0x300L);

 // When
 Zip64ExtendedInformationExtraField zip64ExtraField = new Zip64ExtendedInformationExtraField(
 originalSize, compressedSize, relativeHeaderOffset, diskStart);

 // Then
 assertEquals(originalSize, zip64ExtraField.getSize());
 assertEquals(compressedSize, zip64ExtraField.getCompressedSize());
 assertEquals(relativeHeaderOffset, zip64ExtraField.getRelativeHeaderOffset());
 assertEquals(diskStart, zip64ExtraField.getDiskStartNumber());
 }

 @Test
 void testGetLocalFileDataLength() {
 // Given
 Zip64ExtendedInformationExtraField zip64ExtraField = new Zip64ExtendedInformationExtraField();

 // When
 int length = zip64ExtraField.getLocalFileDataLength().getValue();

 // Then
 assertEquals(2 * ZipConstants.DWORD, length); // 8 bytes for original size, 8 bytes for compressed size
 }

 @Test
 void testGetCentralDirectoryLength() {
 // Given
 Zip64ExtendedInformationExtraField zip64ExtraField = new Zip64ExtendedInformationExtraField();

 // When
 int length = zip64ExtraField.getCentralDirectoryLength().getValue();

 // Then
 assertEquals(0, length);
 }

 @Test
 void testGetLocalFileDataData() {
 // Given
 Zip64ExtendedInformationExtraField zip64ExtraField = new Zip64ExtendedInformationExtraField(
 new ZipEightByteInteger(0x100L), new ZipEightByteInteger(0x80L));

 // When
 byte[] data = zip64ExtraField.getLocalFileDataData();

 // Then
 byte[] expected = {
 (byte) 0x00, (byte) 0x64, (byte) 0x00, (byte) 0x01, // Tag
 (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, // Size of this "extra" block
 (byte) 0x00, (byte) 0x00, (byte) 0x64, (byte) 0x00, // Original size (little-endian)
 (byte) 0x00, (byte) 0x00, (byte) 0x64, (byte) 0x00,
 (byte) 0x00, (byte) 0x00, (byte) 0x64, (byte) 0x00,
 (byte) 0x00, (byte) 0x00, (byte) 0x64, (byte) 0x00,
 (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, // Compressed size (little-endian)
 (byte) 0x00 // End of data
 };
 assertArrayEquals(expected, data);
 }

 @Test
 void testGetCentralDirectoryData() {
 // Given
 Zip64ExtendedInformationExtraField zip64ExtraField = new Zip64ExtendedInformationExtraField(
 new ZipEightByteInteger(0x100L), new ZipEightByteInteger(0x80L),
 new ZipEightByteInteger(0x200L), new ZipLong(0x300L)
 );

 // When
 byte[] data = zip64ExtraField.getCentralDirectoryData();

 // Then
 byte[] expected = {
 (byte) 0x00, (byte) 0x64, (byte) 0x00, (byte) 0x01, // Tag
 (byte) 0x00, (byte) 0x00, (byte) 0x10, (byte) 0x00, // Size of this "extra" block
 (byte) 0x00, (byte) 0x00, (byte) 0x64, (byte) 0x00, // Original size (little-endian)
 (byte) 0x00, (byte) 0x00, (byte) 0x64, (byte) 0x00,
 (byte) 0x00, (byte) 0x00, (byte) 0x64, (byte) 0x00,
 (byte) 0x00, (byte) 0x00, (byte) 0x64, (byte) 0x00,
 (byte) 0x00, (byte) 0x00, (byte) 0x64, (byte) 0x00,
 (byte) 0x00, (byte) 0x00, (byte) 0x64, (byte) 0x00,
 (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, // Compressed size (little-endian)
 (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x02, // Relative header offset (little-endian)
 (byte) 0x00, (byte) 0x00, (byte) 0x03, (byte) 0x00 // Disk start number (little-endian)
 };
 assertArrayEquals(expected, data);
 }

 @Test
 void testParseFromLocalFileData() throws ZipException {
 // Given
 byte[] data = {
 (byte) 0x00, (byte) 0x64, (byte) 0x00, (byte) 0x01, // Tag
 (byte) 0x00, (byte) 0x00, (byte) 0x10, (byte) 0x00, // Size of this "extra" block
 (byte) 0x00, (byte) 0x00, (byte) 0x64, (byte) 0x00, // Original size (little-endian)
 (byte) 0x00, (byte) 0x00, (byte) 0x64, (byte) 0x00,
 (byte) 0x00, (byte) 0x00, (byte) 0x64, (byte) 0x00,
 (byte) 0x00, (byte) 0x00, (byte) 0x64, (byte) 0x00,
 (byte) 0x00, (byte) 0x00, (byte) 0x64, (byte) 0x00,
 (byte) 0x00, (byte) 0x00, (byte) 0x64, (byte) 0x00,
 (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, // Compressed size (little-endian)
 (byte) 0x00 // End of data
 };
 Zip64ExtendedInformationExtraField zip64ExtraField = new Zip64ExtendedInformationExtraField();

 // When
 zip64ExtraField.parseFromLocalFileData(data, 0, data.length);

 // Then
 assertEquals(new ZipEightByteInteger(0x100L), zip64ExtraField.getSize());
 assertEquals(new ZipEightByteInteger(0x80L), zip64ExtraField.getCompressedSize());
 }

 @Test
 void testParseFromCentralDirectoryData() throws ZipException {
 // Given
 byte[] data = {
 (byte) 0x00, (byte) 0x64, (byte) 0x00, (byte) 0x01, // Tag
 (byte) 0x00, (byte) 0x00, (byte) 0x18, (byte) 0x00, // Size of this "extra" block
 (byte) 0x00, (byte) 0x00, (byte) 0x64, (byte) 0x00, // Original size (little-endian)
 (byte) 0x00, (byte) 0x00, (byte) 0x64, (byte) 0x00,
 (byte) 0x00, (byte) 0x00, (byte) 0x64, (byte) 0x00,
 (byte) 0x00, (byte) 0x00, (byte) 0x64, (byte) 0x00,
 (byte) 0x00, (byte) 0x00, (byte) 0x64, (byte) 0x00,
 (byte) 0x00, (byte) 0x00, (byte) 0x64, (byte) 0x00,
 (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x02, // Relative header offset (little-endian)
 (byte) 0x00, (byte) 0x00, (byte) 0x03, (byte) 0x00 // Disk start number (little-endian)
 };
 Zip64ExtendedInformationExtraField zip64ExtraField = new Zip64ExtendedInformationExtraField();

 // When
 zip64ExtraField.parseFromCentralDirectoryData(data, 0, data.length);

 // Then
 assertEquals(new ZipEightByteInteger(0x100L), zip64ExtraField.getSize());
 assertEquals(new ZipEightByteInteger(0x80L), zip64ExtraField.getCompressedSize());
 assertEquals(new ZipEightByteInteger(0x200L), zip64ExtraField.getRelativeHeaderOffset());
 assertEquals(new ZipLong(0x300L), zip64ExtraField.getDiskStartNumber());
 }

 @Test
 void testParseFromCentralDirectoryDataWithMissingFields() throws ZipException {
 // Given
 byte[] data = {
 (byte) 0x00, (byte) 0x64, (byte) 0x00, (byte) 0x01, // Tag
 (byte) 0x00, (byte) 0x00, (byte) 0x10, (byte) 0x00, // Size of this "extra" block
 (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00 // Missing original and compressed size
 };
 Zip64ExtendedInformationExtraField zip64ExtraField = new Zip64ExtendedInformationExtraField();

 // When & Then
 assertThrows(ZipException.class, () -> {
 zip64ExtraField.parseFromCentralDirectoryData(data, 0, data.length);
 });
 }
}