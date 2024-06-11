import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.compress.ChangeSet;
import org.apache.commons.compress.Change;
import org.apache.commons.compress.utils.TestUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class ChangeSetPerformerTest {
    // Test case for constructor
    @Test
    public void testChangeSetPerformerConstruction() {
        ChangeSet changeSet = TestUtils.createTestChangeSet();
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);
        assertEquals(changeSet.getChanges(), performer.getChanges());
    }

    // Test case for perform method with no changes
    @Test
    public void testPerformNoChanges() throws IOException {
        ChangeSet changeSet = TestUtils.createTestChangeSet();
        ArchiveInputStream input = TestUtils.createTestArchiveInputStream();
        ArchiveOutputStream output = TestUtils.createTestArchiveOutputStream();
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);
        ChangeSetResults result = performer.perform(input, output);
        assertTrue(result.isEmpty());
    }

    // Test case for perform method with add change
    @Test
    public void testPerformAddChange() throws IOException {
        ChangeSet changeSet = TestUtils.createTestChangeSetWithAddChange();
        ArchiveInputStream input = TestUtils.createTestArchiveInputStream();
        ArchiveOutputStream output = TestUtils.createTestArchiveOutputStream();
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);
        ChangeSetResults result = performer.perform(input, output);
        assertFalse(result.isEmpty());
        assertEquals("testFile.txt", result.getFirstAddedFile());
    }

    // Test case for perform method with delete change
    @Test
    public void testPerformDeleteChange() throws IOException {
        ChangeSet changeSet = TestUtils.createTestChangeSetWithDeleteChange();
        ArchiveInputStream input = TestUtils.createTestArchiveInputStream();
        ArchiveOutputStream output = TestUtils.createTestArchiveOutputStream();
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);
        ChangeSetResults result = performer.perform(input, output);
        assertFalse(result.isEmpty());
        assertEquals("testFile.txt", result.getFirstDeletedFile());
    }

    // Test case for perform method with delete directory change
    @Test
    public void testPerformDeleteDirectoryChange() throws IOException {
        ChangeSet changeSet = TestUtils.createTestChangeSetWithDeleteDirectoryChange();
        ArchiveInputStream input = TestUtils.createTestArchiveInputStream();
        ArchiveOutputStream output = TestUtils.createTestArchiveOutputStream();
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);
        ChangeSetResults result = performer.perform(input, output);
        assertFalse(result.isEmpty());
        assertEquals("testDir/testFile.txt", result.getFirstDeletedFile());
    }

    // Test case for perform method with replace change
    @Test(expected = UnsupportedOperationException.class)
    public void testPerformReplaceChange() throws IOException {
        ChangeSet changeSet = TestUtils.createTestChangeSetWithReplaceChange();
        ArchiveInputStream input = TestUtils.createTestArchiveInputStream();
        ArchiveOutputStream output = TestUtils.createTestArchiveOutputStream();
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);
        performer.perform(input, output);
    }

    // Test case for perform method with add change in replace mode
    @Test
    public void testPerformAddChangeReplaceMode() throws IOException {
        ChangeSet changeSet = TestUtils.createTestChangeSetWithAddChangeReplaceMode();
        ArchiveInputStream input = TestUtils.createTestArchiveInputStream();
        ArchiveOutputStream output = TestUtils.createTestArchiveOutputStream();
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);
        ChangeSetResults result = performer.perform(input, output);
        assertFalse(result.isEmpty());
        assertEquals("testFile.txt", result.getFirstAddedFile());
    }

    // Test case for perform method with conflicting changes
    @Test(expected = IllegalArgumentException.class)
    public void testPerformConflictingChanges() throws IOException {
        ChangeSet changeSet = TestUtils.createTestChangeSetWithConflictingChanges();
        ArchiveInputStream input = TestUtils.createTestArchiveInputStream();
        ArchiveOutputStream output = TestUtils.createTestArchiveOutputStream();
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);
        performer.perform(input, output);
    }
}