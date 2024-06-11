import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.compress.utils.TestUtils;
import org.apache.commons.compress.changes.Change;
import org.apache.commons.compress.changes.ChangeSet;
import org.apache.commons.compress.changes.ChangeSetResults;
import org.apache.commons.compress.changes.ChangeSetPerformer;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Test class for ChangeSetPerformer.
 */
public class ChangeSetPerformerTest {

    @Test
    public void testPerform() throws IOException {
        // Given
        ChangeSet changeSet = new ChangeSet();

        Change addEntry = new Change(Change.TYPE_ADD, null, "testAdd.txt", null, null, false);
        Change addReplaceEntry = new Change(Change.TYPE_ADD, null, "testAddReplace.txt", null, null, true);
        Change deleteEntry = new Change(Change.TYPE_DELETE, null, "testDelete.txt", null, null, null);
        Change deleteDirEntry = new Change(Change.TYPE_DELETE_DIR, null, "testDeleteDir/", null, null, null);

        changeSet.addChange(addEntry);
        changeSet.addChange(addReplaceEntry);
        changeSet.addChange(deleteEntry);
        changeSet.addChange(deleteDirEntry);

        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);

        ByteArrayOutputStream inputStream = new ByteArrayOutputStream();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        ArchiveInputStream archiveInputStream = new ArchiveInputStream(new ByteArrayInputStream(TestUtils.getTestData("testData.jar")));
        ArchiveOutputStream archiveOutputStream = new ArchiveOutputStream(outputStream);

        // When
        ChangeSetResults results = performer.perform(archiveInputStream, archiveOutputStream);

        // Then
        Assert.assertEquals(2, results.getAddedFromChangeSet().size());
        Assert.assertEquals(1, results.getDeleted().size());
        Assert.assertFalse(results.hasBeenAdded("testAdd.txt"));
        Assert.assertTrue(results.hasBeenAdded("testAddReplace.txt"));
        Assert.assertTrue(results.hasBeenAdded("testDeleteDir/testDeleteDir/testDeleteDir/testAddReplace.txt"));
        Assert.assertFalse(results.hasBeenAdded("testDeleteDir/testDeleteDir/testDeleteDir/testDelete.txt"));
        Assert.assertTrue(results.deleted("testDelete.txt"));
        Assert.assertTrue(results.deleted("testDeleteDir/testDeleteDir/testDeleteDir/testDelete.txt"));
        Assert.assertFalse(results.deleted("testAdd.txt"));
        Assert.assertFalse(results.deleted("testAddReplace.txt"));

        byte[] expectedOutput = TestUtils.getTestData("expectedTestData.jar");
        byte[] actualOutput = outputStream.toByteArray();
        Assert.assertArrayEquals(expectedOutput, actualOutput);
    }
}