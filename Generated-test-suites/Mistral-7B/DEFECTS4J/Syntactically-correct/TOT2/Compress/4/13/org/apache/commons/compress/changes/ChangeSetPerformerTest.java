import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.compress.utils.TestUtils;
import org.apache.commons.compress.changes.Change;
import org.apache.commons.compress.changes.ChangeSet;
import org.apache.commons.compress.changes.ChangeSetPerformer;
import org.apache.commons.compress.test.utils.TestFileUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Test for ChangeSetPerformer class
 */
public class ChangeSetPerformerTest {

    @Test
    public void testPerform() throws IOException {
        // given
        ChangeSet changeSet = TestUtils.createTestChangeSet();
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);

        File inputFile = TestFileUtils.createTestFile("input.tar.gz");
        FileOutputStream outputStream = new FileOutputStream("output.tar.gz");
        ArchiveInputStream inputArchive = new ArchiveInputStream(new FileInputStream(inputFile));
        ArchiveOutputStream outputArchive = new ArchiveOutputStream(outputStream);

        // when
        ChangeSetResults results = performer.perform(inputArchive, outputArchive);

        // then
        Assert.assertEquals(changeSet.size(), results.size());

        Set expectedChanges = changeSet.getChanges();
        Set actualChanges = performer.changes;

        Assert.assertEquals(expectedChanges.size(), actualChanges.size());
        Assert.assertTrue(expectedChanges.containsAll(actualChanges));
        Assert.assertTrue(actualChanges.containsAll(expectedChanges));

        // cleanup
        inputArchive.close();
        outputArchive.finish();
        outputStream.close();
        outputStream.close();
        inputFile.delete();
        new File("output.tar.gz").delete();
    }

    @Test
    public void testPerform_EmptyChangeSet() throws IOException {
        // given
        ChangeSet changeSet = new ChangeSet();
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);

        File inputFile = TestFileUtils.createTestFile("input.tar.gz");
        FileOutputStream outputStream = new FileOutputStream("output.tar.gz");
        ArchiveInputStream inputArchive = new ArchiveInputStream(new FileInputStream(inputFile));
        ArchiveOutputStream outputArchive = new ArchiveOutputStream(outputStream);

        // when
        ChangeSetResults results = performer.perform(inputArchive, outputArchive);

        // then
        Assert.assertEquals(0, results.size());

        // cleanup
        inputArchive.close();
        outputArchive.finish();
        outputStream.close();
        outputStream.close();
        inputFile.delete();
        new File("output.tar.gz").delete();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPerform_NullInput() {
        // given
        ChangeSet changeSet = TestUtils.createTestChangeSet();
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);

        // when
        performer.perform(null, new FileOutputStream("output.tar.gz"));

        // then
        // exception should be thrown
    }

    @Test(expected = IOException.class)
    public void testPerform_NullOutput() {
        // given
        ChangeSet changeSet = TestUtils.createTestChangeSet();
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);

        ArchiveInputStream inputArchive = new ArchiveInputStream(new FileInputStream("input.tar.gz"));

        // when
        performer.perform(inputArchive, null);

        // then
        // exception should be thrown

        inputArchive.close();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPerform_NullInputArchive() {
        // given
        ChangeSet changeSet = TestUtils.createTestChangeSet();
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);

        // when
        performer.perform(null, new FileOutputStream("output.tar.gz"));

        // then
        // exception should be thrown
    }

    @Test(expected = IOException.class)
    public void testPerform_NullOutputArchive() {
        // given
        ChangeSet changeSet = TestUtils.createTestChangeSet();
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);

        ArchiveInputStream inputArchive = new ArchiveInputStream(new FileInputStream("input.tar.gz"));

        // when
        performer.perform(inputArchive, null);

        // then
        // exception should be thrown

        inputArchive.close();
    }

    @Test
    public void testIsDeletedLater() {
        // given
        Set workingSet = new HashSet(Arrays.asList(new Change[] {
                new Change("test1.txt", Change.TYPE_DELETE, "test1.txt"),
                new Change("test2.txt", Change.TYPE_ADD, "test2.txt", null, null),
                new Change("test3.txt", Change.TYPE_ADD, "test3.txt", null, null)
        }));

        // when
        boolean actual = ChangeSetPerformer.isDeletedLater(workingSet, new ArchiveEntry("test1.txt"));

        // then
        Assert.assertTrue(actual);
    }

    @Test
    public void testIsDeletedLater_NoDeletedLater() {
        // given
        Set workingSet = new HashSet(Arrays.asList(new Change[] {
                new Change("test1.txt", Change.TYPE_ADD, "test1.txt", null, null),
                new Change("test2.txt", Change.TYPE_ADD, "test2.txt", null, null)
        }));

        // when
        boolean actual = ChangeSetPerformer.isDeletedLater(workingSet, new ArchiveEntry("test1.txt"));

        // then
        Assert.assertFalse(actual);
    }

    @Test
    public void testIsDeletedLater_EmptyList() {
        // given
        Set workingSet = Collections.emptySet();

        // when
        boolean actual = ChangeSetPerformer.isDeletedLater(workingSet, new ArchiveEntry("test1.txt"));

        // then
        Assert.assertFalse(actual);
    }

    @Test
    public void testCopyStream() throws IOException {
        // given
        File inputFile = TestFileUtils.createTestFile("input.txt");
        File outputFile = TestFileUtils.createTestFile("output.txt");
        InputStream inputStream = new FileInputStream(inputFile);
        OutputStream outputStream = new FileOutputStream(outputFile);

        // when
        ChangeSetPerformer.copyStream(inputStream, new FileOutputStream("output2.txt"), null);

        // then
        byte[] inputBytes = TestFileUtils.readFileToByteArray(inputFile);
        byte[] outputBytes1 = TestFileUtils.readFileToByteArray(outputFile);
        byte[] outputBytes2 = TestFileUtils.readFileToByteArray(new File("output2.txt"));

        Assert.assertArrayEquals(inputBytes, outputBytes1);
        Assert.assertArrayEquals(inputBytes, outputBytes2);

        // cleanup
        inputStream.close();
        outputStream.close();
        inputFile.delete();
        outputFile.delete();
        new File("output2.txt").delete();
    }

    @Test(expected = NullPointerException.class)
    public void testCopyStream_NullOutput() {
        // given
        File inputFile = TestFileUtils.createTestFile("input.txt");
        InputStream inputStream = new FileInputStream(inputFile);

        // when
        ChangeSetPerformer.copyStream(inputStream, null, null);

        // then
        // exception should be thrown

        inputStream.close();
        inputFile.delete();
    }
}