import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class ChangeSetPerformerTest {

    // Test methods for each public method: constructor, perform, isDeletedLater, and copyStream
    // Add test cases for edge cases and exceptions as shown above

    // Helper methods for testing perform with InputStream and ArchiveOutputStream
    private void testInputStream(InputStream testInput, ArchiveEntry testEntry,
            ArchiveOutputStream archiveOutputStream, ChangeSetPerformer changeSetPerformer) throws IOException {
        testOutputStream(new ByteArrayInputStream(testInput.readAllBytes()), testEntry, archiveOutputStream, changeSetPerformer);
        testInput.close();
    }

    private void testOutputStream(InputStream inputStream, ArchiveEntry entry, ArchiveOutputStream archiveOutputStream,
            ChangeSetPerformer changeSetPerformer) throws IOException {
        archiveOutputStream.putArchiveEntry(entry);
        IOUtils.copy(inputStream, archiveOutputStream);
        archiveOutputStream.closeArchiveEntry();
        inputStream.close();
    }
}