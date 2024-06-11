package org.apache.commons.compress.archivers.zip;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.zip.ExtraField;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipExtraField;
import org.apache.commons.compress.utils.ByteStreams;

/**
 * Test the ZipArchiveEntry class.
 */
public class ZipArchiveEntryTest {

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        assertEquals("test.txt", entry.getName());
        assertNull(entry.getExtraFields());
        assertFalse(entry.isSupportedCompressionMethod());
        assertEquals(-1, entry.getMethod());
        assertFalse(entry.isDirectory());
    }

    /**
     * Test constructor with ZipEntry.
     */
    @Test
    public void testConstructorFromZipEntry() throws ZipException {
        String name = "test.txt";
        byte[] extra = new byte[] { (byte) 0x08, 0x01, 0x02, 0x03, 0x04, 0x05 };
        ZipEntry zipEntry = new ZipEntry(name + "@" + new Date());
        zipEntry.setExtra(extra);
        zipEntry.setMethod(ZipEntry.STORED);
        ZipArchiveEntry entry = new ZipArchiveEntry(zipEntry);
        assertEquals(name, entry.getName());
        assertEquals(extra, entry.getLocalFileDataExtra());
        assertTrue(entry.isSupportedCompressionMethod());
        assertEquals(ZipEntry.STORED, entry.getMethod());
        assertFalse(entry.isDirectory());

        // create a clone
        ZipArchiveEntry clone = (ZipArchiveEntry) entry.clone();
        assertEquals(entry, clone);
        assertEquals(entry.getName(), clone.getName());
        assertEquals(entry.getLocalFileDataExtra(), clone.getLocalFileDataExtra());
        assertEquals(entry.getMethod(), clone.getMethod());
        assertFalse(entry.isDirectory());

        // test equals
        assertTrue(entry.equals(entry));
        assertTrue(entry.equals(clone));
        assertFalse(entry.equals(null));
        assertFalse(entry.equals(""));
        assertFalse(entry.equals(new Object()));
        assertFalse(entry.equals(new ZipArchiveEntry("test2.txt")));
    }

    /**
     * Test constructor from file.
     */
    @Test
    public void testConstructorFromFile() {
        String name = "test.txt";
        File file = new File(name);
        ZipArchiveEntry entry = new ZipArchiveEntry(file, name);
        assertEquals(name, entry.getName());
        assertEquals(file.length(), entry.getSize());
        assertFalse(entry.isSupportedCompressionMethod());
        assertEquals(-1, entry.getMethod());
        assertFalse(entry.isDirectory());
    }

    /**
     * Test setUnixMode.
     */
    @Test
    public void testSetUnixMode() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        entry.setUnixMode(0755);
        assertEquals(0755, entry.getUnixMode());
    }

    /**
     * Test getPlatform.
     */
    @Test
    public void testGetPlatform() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        assertEquals(0, entry.getPlatform());

        entry.setUnixMode(0755);
        assertEquals(3, entry.getPlatform());
    }

    /**
     * Test setExtraFields.
     */
    @Test
    public void testSetExtraFields() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");

        // empty array
        ZipExtraField[] extraFields = new ZipExtraField[0];
        entry.setExtraFields(extraFields);
        assertNull(entry.getExtraFields());

        // null
        extraFields = null;
        entry.setExtraFields(extraFields);
        assertNull(entry.getExtraFields());

        // one extra field
        ExtraField extraField = new ExtraField("Test", "Value");
        ZipExtraField zipExtraField = new ZipExtraField(extraField, 1);
        entry.setExtraFields(new ZipExtraField[] { zipExtraField });
        ZipExtraField[] result = entry.getExtraFields();
        assertEquals(1, result.length);
        assertEquals(zipExtraField, result[0]);

        // multiple extra fields
        extraField = new ExtraField("Test1", "Value1");
        zipExtraField = new ZipExtraField(extraField, 1);
        extraField = new ExtraField("Test2", "Value2");
        ZipExtraField zipExtraField1 = new ZipExtraField(extraField, 2);
        entry.setExtraFields(new ZipExtraField[] { zipExtraField, zipExtraField1 });
        result = entry.getExtraFields();
        assertEquals(2, result.length);
        assertEquals(zipExtraField, result[0]);
        assertEquals(zipExtraField1, result[1]);
    }

    /**
     * Test addExtraField.
     */
    @Test
    public void testAddExtraField() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");

        // empty array
        ZipExtraField[] extraFields = new ZipExtraField[0];
        entry.setExtraFields(extraFields);
        assertNull(entry.getExtraFields());

        // add an extra field
        ExtraField extraField = new ExtraField("Test", "Value");
        ZipExtraField zipExtraField = new ZipExtraField(extraField, 1);
        entry.addExtraField(zipExtraField);
        ZipExtraField[] result = entry.getExtraFields();
        assertEquals(1, result.length);
        assertEquals(zipExtraField, result[0]);

        // add an extra field of the same type
        extraField = new ExtraField("Test", "Value2");
        zipExtraField = new ZipExtraField(extraField, 1);
        entry.addExtraField(zipExtraField);
        result = entry.getExtraFields();
        assertEquals(1, result.length);
        assertEquals(zipExtraField, result[0]);

        // add an extra field of a different type
        extraField = new ExtraField("Test2", "Value");
        zipExtraField = new ZipExtraField(extraField, 2);
        entry.addExtraField(zipExtraField);
        result = entry.getExtraFields();
        assertEquals(2, result.length);
        assertEquals(zipExtraField, result[1]);
    }

    /**
     * Test removeExtraField.
     */
    @Test
    public void testRemoveExtraField() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");

        // empty array
        ZipExtraField[] extraFields = new ZipExtraField[0];
        entry.setExtraFields(extraFields);
        assertNull(entry.getExtraFields());

        // add an extra field
        ExtraField extraField = new ExtraField("Test", "Value");
        ZipExtraField zipExtraField = new ZipExtraField(extraField, 1);
        entry.addExtraField(zipExtraField);
        extraFields = entry.getExtraFields();
        assertEquals(1, extraFields.length);

        // remove the extra field
        entry.removeExtraField(new ZipShort((short) 1));
        extraFields = entry.getExtraFields();
        assertEquals(0, extraFields.length);

        // remove an extra field that does not exist
        entry.removeExtraField(new ZipShort((short) 2));
        extraFields = entry.getExtraFields();
        assertEquals(0, extraFields.length);
    }

    /**
     * Test getExtraField.
     */
    @Test
    public void testGetExtraField() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");

        // empty array
        ZipExtraField[] extraFields = new ZipExtraField[0];
        entry.setExtraFields(extraFields);
        assertNull(entry.getExtraField(new ZipShort((short) 1)));

        // add an extra field
        ExtraField extraField = new ExtraField("Test", "Value");
        ZipExtraField zipExtraField = new ZipExtraField(extraField, 1);
        entry.addExtraField(zipExtraField);
        extraFields = entry.getExtraFields();
        assertEquals(1, extraFields.length);

        // get the extra field
        ZipExtraField result = entry.getExtraField(new ZipShort((short) 1));
        assertEquals(zipExtraField, result);

        // get an extra field that does not exist
        result = entry.getExtraField(new ZipShort((short) 2));
        assertNull(result);
    }

    /**
     * Test setExtra.
     */
    @Test
    public void testSetExtra() throws IOException {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");

        // empty array
        ZipExtraField[] extraFields = new ZipExtraField[0];
        entry.setExtraFields(extraFields);
        assertNull(entry.getExtraFields());

        // set an empty extra data
        byte[] extra = new byte[0];
        entry.setExtra(extra);
        assertNull(entry.getExtraFields());

        // set an extra data with one extra field
        extraField = new ExtraField("Test", "Value");
        ZipExtraField zipExtraField = new ZipExtraField(extraField, 1);
        extra = ExtraFieldUtils.parseToByteArray(zipExtraField);
        ByteArrayInputStream input = new ByteArrayInputStream(extra);
        entry.setExtra(input);
        extraFields = entry.getExtraFields();
        assertEquals(1, extraFields.length);
        assertEquals(zipExtraField, extraFields[0]);

        // set an extra data with multiple extra fields
        extraField = new ExtraField("Test1", "Value1");
        zipExtraField = new ZipExtraField(extraField, 1);
        extra = ExtraFieldUtils.parseToByteArray(zipExtraField);
        input = new ByteArrayInputStream(extra);
        entry.setExtra(input);
        extraFields = entry.getExtraFields();
        assertEquals(1, extraFields.length);
        assertEquals(zipExtraField, extraFields[0]);

        extraField = new ExtraField("Test2", "Value2");
        zipExtraField = new ZipExtraField(extraField, 2);
        extra = ExtraFieldUtils.parseToByteArray(zipExtraField);
        input = new ByteArrayInputStream(extra);
        entry.setExtra(input);
        result = entry.getExtraFields();
        assertEquals(2, result.length);
        assertEquals(zipExtraField, result[1]);
    }

    /**
     * Test setCentralDirectoryExtra.
     */
    @Test
    public void testSetCentralDirectoryExtra() throws ZipException {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");

        // empty array
        ZipExtraField[] extraFields = new ZipExtraField[0];
        entry.setExtraFields(extraFields);
        assertNull(entry.getExtraFields());

        // set an empty central directory extra data
        byte[] extra = new byte[0];
        entry.setCentralDirectoryExtra(extra);
        assertNull(entry.getExtraFields());

        // set a central directory extra data with one extra field
        extraField = new ExtraField("Test", "Value");
        zipExtraField = new ZipExtraField(extraField, 1);
        extra = ExtraFieldUtils.parseToByteArray(zipExtraField, false);
        entry.setCentralDirectoryExtra(extra);
        extraFields = entry.getExtraFields();
        assertEquals(1, extraFields.length);
        assertEquals(zipExtraField, extraFields[0]);

        // set a central directory extra data with multiple extra fields
        extraField = new ExtraField("Test1", "Value1");
        zipExtraField = new ZipExtraField(extraField, 1);
        extra = ExtraFieldUtils.parseToByteArray(zipExtraField, false);
        entry.setCentralDirectoryExtra(extra);
        extraFields = entry.getExtraFields();
        assertEquals(1, extraFields.length);
        assertEquals(zipExtraField, extraFields[0]);

        extraField = new ExtraField("Test2", "Value2");
        zipExtraField = new ZipExtraField(extraField, 2);
        extra = ExtraFieldUtils.parseToByteArray(zipExtraField, false);
        entry.setCentralDirectoryExtra(extra);
        result = entry.getExtraFields();
        assertEquals(2, result.length);
        assertEquals(zipExtraField, result[1]);
    }

    /**
     * Test getName.
     */
    @Test
    public void testGetName() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        assertEquals("test.txt", entry.getName());

        entry.setName("test2.txt");
        assertEquals("test2.txt", entry.getName());
    }

    /**
     * Test isDirectory.
     */
    @Test
    public void testIsDirectory() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        assertFalse(entry.isDirectory());

        entry.setName("test.txt/");
        assertTrue(entry.isDirectory());
    }

    /**
     * Test hashCode.
     */
    @Test
    public void testHashCode() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        assertEquals("test.txt".hashCode(), entry.hashCode());
    }

    /**
     * Test equals.
     */
    @Test
    public void testEquals() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        assertEquals(entry, entry);

        ZipArchiveEntry entry2 = new ZipArchiveEntry("test.txt");
        assertEquals(entry, entry2);

        ZipArchiveEntry entry3 = new ZipArchiveEntry("test2.txt");
        assertNotEquals(entry, entry3);

        entry2.setName("test2.txt");
        assertNotEquals(entry, entry2);

        entry2.setName("test.txt");
        assertEquals(entry, entry2);

        entry2.setSize(100);
        assertNotEquals(entry, entry2);

        entry2.setSize(100);
        entry3.setSize(100);
        entry3.setName("test3.txt");
        assertNotEquals(entry, entry3);

        entry3.setName("test.txt");
        assertEquals(entry, entry3);

        entry3.setExtraFields(new ZipExtraField[] { new ZipExtraField(new ExtraField("Test", "Value"), 1) });
        assertNotEquals(entry, entry3);

        entry3.setExtraFields(new ZipExtraField[] { new ZipExtraField(new ExtraField("Test", "Value"), 2) });
        assertNotEquals(entry, entry3);

        entry3.setExtraFields(new ZipExtraField[] { new ZipExtraField(new ExtraField("Test", "Value2"), 1) });
        assertNotEquals(entry, entry3);

        entry3.setExtraFields(new ZipExtraField[] { new ZipExtraField(new ExtraField("Test2", "Value"), 1) });
        assertNotEquals(entry, entry3);

        entry3.setExtraFields(new ZipExtraField[] { new ZipExtraField(new ExtraField("Test", "Value"), 1), new ZipExtraField(new ExtraField("Test2", "Value"), 1) });
        assertNotEquals(entry, entry3);
    }
}