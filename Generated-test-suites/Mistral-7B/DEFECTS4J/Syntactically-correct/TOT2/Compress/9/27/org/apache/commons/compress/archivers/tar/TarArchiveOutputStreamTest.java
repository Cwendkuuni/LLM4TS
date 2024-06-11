import org.apache.commons.compress.archivers.tar.*;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class TarArchiveOutputStreamTest {

private ByteArrayOutputStream out;
private TarArchiveOutputStream tarStream;

@Before
public void setUp() {
out = new ByteArrayOutputStream();
tarStream = new TarArchiveOutputStream(out);
}

@After
public void tearDown() throws IOException {
tarStream.close();
out.close();
}

@Test
public void testConstructor() {
TarArchiveOutputStream tarStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
}

@Test
public void testPutArchiveEntry() throws IOException {
// Test putArchiveEntry with a short file name
TarArchiveEntry entry = new TarArchiveEntry("test.txt");
tarStream.putArchiveEntry(entry);

// Test putArchiveEntry with a long file name
TarArchiveEntry longEntry = new TarArchiveEntry("test_long_file_name.txt");
tarStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
tarStream.putArchiveEntry(longEntry);

// Test putArchiveEntry without closing the previous entry
TarArchiveEntry unclosedEntry = new TarArchiveEntry("unclosed_entry.txt");
tarStream.putArchiveEntry(unclosedEntry);
assertThrows(IOException.class, () -> tarStream.putArchiveEntry(entry));

// Test putArchiveEntry with a null ArchiveEntry
assertThrows(ClassCastException.class, () -> tarStream.putArchiveEntry(null));

// Test putArchiveEntry with a null TarArchiveEntry
assertThrows(ClassCastException.class, () -> tarStream.putArchiveEntry((ArchiveEntry) null));
}

@Test
public void testCloseArchiveEntry() throws IOException {
// Test closeArchiveEntry with a null archive entry
assertNull(tarStream.closeArchiveEntry());

// Test closeArchiveEntry without putting an entry
tarStream.closeArchiveEntry();
assertFalse(tarStream.haveUnclosedEntry());

// Test closeArchiveEntry with an entry
TarArchiveEntry entry = new TarArchiveEntry("test.txt");
tarStream.putArchiveEntry(entry);
tarStream.closeArchiveEntry();
assertTrue(tarStream.haveUnclosedEntry());

// Test closeArchiveEntry twice
tarStream.closeArchiveEntry();
assertFalse(tarStream.haveUnclosedEntry());
}

@Test
public void testFinish() throws IOException {
// Test finish with an unfinished archive
assertThrows(IOException.class, () -> tarStream.finish());

// Test finish with a closed archive
tarStream.finish();
assertTrue(tarStream.finished);
}

@Test
public void testClose() throws IOException {
// Test close with an unfinished archive
assertThrows(IOException.class, () -> tarStream.close());

// Test close with an finished archive
tarStream.finish();
tarStream.close();
assertTrue(tarStream.closed);
}

@Test
public void testGetRecordSize() {
assertEquals(TarConstants.TAR_RECORD_SIZE, tarStream.getRecordSize());
}

@Test
public void testSetLongFileMode() {
// Test setting longFileMode to an invalid value
assertThrows(IllegalArgumentException.class, () -> tarStream.setLongFileMode(-1));

// Test setting longFileMode to a valid value
tarStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
assertEquals(TarArchiveOutputStream.LONGFILE_GNU, tarStream.getLongFileMode());
}

@Test
public void testWrite() throws IOException {
// Test writing data to a short file name entry
byte[] data = "test data".getBytes(StandardCharsets.UTF_8);
TarArchiveEntry entry = new TarArchiveEntry("test.txt");
tarStream.putArchiveEntry(entry);
tarStream.write(data, 0, data.length);
tarStream.closeArchiveEntry();

// Test writing data to a long file name entry
tarStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
TarArchiveEntry longEntry = new TarArchiveEntry("test_long_file_name.txt");
tarStream.putArchiveEntry(longEntry);
tarStream.write(data, 0, data.length);
tarStream.closeArchiveEntry();

// Test writing data to a closed archive
assertThrows(IOException.class, () -> tarStream.write(data, 0, data.length));
}

@Test
public void testFlush() throws IOException {
// Test flush with an unfinished archive
assertThrows(IOException.class, () -> tarStream.flush());

// Test flush with a finished archive
tarStream.finish();
tarStream.flush();
assertTrue(tarStream.finished);
}

@Test
public void testCreateArchiveEntry() throws IOException {
// Test creating an archive entry with a null File
assertThrows(NullPointerException.class, () -> tarStream.createArchiveEntry(null, "test.txt"));

// Test creating an archive entry with a null entry name
assertThrows(NullPointerException.class, () -> tarStream.createArchiveEntry(new File("test.txt"), null));

// Test creating an archive entry with an existing file
assertTrue(tarStream.createArchiveEntry(new File("test.txt"), "test.txt") instanceof TarArchiveEntry);
}
}