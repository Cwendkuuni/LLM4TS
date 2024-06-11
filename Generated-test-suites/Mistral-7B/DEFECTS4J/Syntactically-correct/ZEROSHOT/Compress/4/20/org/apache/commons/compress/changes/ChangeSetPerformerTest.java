package org.apache.commons.compress.changes;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.compress.utils.TestUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import static org.apache.commons.compress.utils.TestUtils.createTestInputStream;
import static org.apache.commons.compress.utils.TestUtils.createTestOutputStream;

/**
 * Test class for ChangeSetPerformer
 */
public class ChangeSetPerformerTest {

    /**
     * Test method for {@link ChangeSetPerformer#ChangeSetPerformer(ChangeSet)}
     */
    @Test
    public void testChangeSetPerformerConstruction() {
        // Given
        ChangeSet changeSet = TestUtils.createTestChangeSet();

        // When
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);

        // Then
        Assert.assertNotNull(performer);
        Assert.assertEquals(changeSet.getChanges(), performer.getChanges());
    }

    /**
     * Test method for {@link ChangeSetPerformer#perform(ArchiveInputStream, ArchiveOutputStream)}
     */
    @Test
    public void testPerformAddedWithoutReplaceMode() throws IOException {
        // Given
        ChangeSet changeSet = TestUtils.createTestChangeSetWithAdditions();
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);
        ByteArrayOutputStream outputStream = createTestOutputStream();
        ArchiveInputStream inputStream = createTestInputStream(
                "TestInputStream.txt", "testdata/testInput.tar.gz");

        // When
        ChangeSetResults results = performer.perform(inputStream, outputStream);

        // Then
        Assert.assertNotNull(results);
        Assert.assertTrue(results.hasBeenAdded("TestInputStream.txt"));
        Assert.assertFalse(results.hasBeenDeleted("TestInputStream.txt"));

        InputStream output = outputStream.getInputStream();
        byte[] actualContent = IOUtils.toByteArray(output);
        byte[] expectedContent = TestUtils.createTestInputStream("TestInputStream.txt").readAllBytes();
        Assert.assertArrayEquals(expectedContent, actualContent);
    }

    /**
     * Test method for {@link ChangeSetPerformer#perform(ArchiveInputStream, ArchiveOutputStream)}
     */
    @Test
    public void testPerformAddedWithReplaceMode() throws IOException {
        // Given
        ChangeSet changeSet = TestUtils.createTestChangeSetWithAdditionsAndReplacements();
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);
        ByteArrayOutputStream outputStream = createTestOutputStream();
        ArchiveInputStream inputStream = createTestInputStream(
                "TestInputStream.txt", "testdata/testInput.tar.gz");

        // When
        ChangeSetResults results = performer.perform(inputStream, outputStream);

        // Then
        Assert.assertNotNull(results);
        Assert.assertTrue(results.hasBeenAdded("TestInputStream.txt"));
        Assert.assertFalse(results.hasBeenDeleted("TestInputStream.txt"));

        InputStream output = outputStream.getInputStream();
        byte[] actualContent = IOUtils.toByteArray(output);
        byte[] expectedContent = TestUtils.createTestInputStream("TestReplaceInputStream.txt").readAllBytes();
        Assert.assertArrayEquals(expectedContent, actualContent);
    }

    /**
     * Test method for {@link ChangeSetPerformer#perform(ArchiveInputStream, ArchiveOutputStream)}
     */
    @Test
    public void testPerformDeleted() throws IOException {
        // Given
        ChangeSet changeSet = TestUtils.createTestChangeSetWithDeletions();
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);
        ByteArrayOutputStream outputStream = createTestOutputStream();
        ArchiveInputStream inputStream = createTestInputStream(
                "TestInputStream.txt", "testdata/testInput.tar.gz");

        // When
        ChangeSetResults results = performer.perform(inputStream, outputStream);

        // Then
        Assert.assertNotNull(results);
        Assert.assertFalse(results.hasBeenAdded("TestInputStream.txt"));
        Assert.assertTrue(results.hasBeenDeleted("TestInputStream.txt"));

        InputStream input = inputStream.getInputStream();
        byte[] actualContent = IOUtils.toByteArray(input);
        Assert.assertArrayEquals(Collections.emptyByteArray(), actualContent);

        InputStream output = outputStream.getInputStream();
        byte[] expectedContent = TestUtils.createTestInputStream("TestInputStream.txt").readAllBytes();
        byte[] actualContentOutput = IOUtils.toByteArray(output);
        Assert.assertArrayEquals(Collections.emptyByteArray(), actualContentOutput);
    }

    /**
     * Test method for {@link ChangeSetPerformer#perform(ArchiveInputStream, ArchiveOutputStream)}
     */
    @Test
    public void testPerformDeletedDirectory() throws IOException {
        // Given
        ChangeSet changeSet = TestUtils.createTestChangeSetWithDirectoryDeletion();
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);
        ByteArrayOutputStream outputStream = createTestOutputStream();
        ArchiveInputStream inputStream = createTestInputStream(
                "TestDirectory/TestDirectory/TestInputStream.txt",
                "testdata/testInput.tar.gz");

        // When
        ChangeSetResults results = performer.perform(inputStream, outputStream);

        // Then
        Assert.assertNotNull(results);
        Assert.assertFalse(results.hasBeenAdded("TestDirectory/TestDirectory/TestInputStream.txt"));
        Assert.assertTrue(results.hasBeenDeleted("TestDirectory/TestDirectory/TestInputStream.txt"));

        InputStream input = inputStream.getInputStream();
        byte[] actualContent = IOUtils.toByteArray(input);
        Assert.assertArrayEquals(Collections.emptyByteArray(), actualContent);

        InputStream output = outputStream.getInputStream();
        byte[] expectedContent = TestUtils.createTestInputStream("TestDirectory/TestDirectory/TestInputStream.txt").readAllBytes();
        byte[] actualContentOutput = IOUtils.toByteArray(output);
        Assert.assertArrayEquals(Collections.emptyByteArray(), actualContentOutput);
    }

    /**
     * Test method for {@link ChangeSetPerformer#perform(ArchiveInputStream, ArchiveOutputStream)}
     */
    @Test
    public void testPerformMultipleAdditions() throws IOException {
        // Given
        ChangeSet changeSet = TestUtils.createTestChangeSetWithMultipleAdditions();
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);
        ByteArrayOutputStream outputStream = createTestOutputStream();
        ArchiveInputStream inputStream = createTestInputStream(
                "TestInputStream1.txt", "TestInputStream2.txt",
                "testdata/testInput.tar.gz");

        // When
        ChangeSetResults results = performer.perform(inputStream, outputStream);

        // Then
        Assert.assertNotNull(results);
        Assert.assertTrue(results.hasBeenAdded("TestInputStream1.txt"));
        Assert.assertTrue(results.hasBeenAdded("TestInputStream2.txt"));

        InputStream output = outputStream.getInputStream();
        byte[] actualContent = IOUtils.toByteArray(output);
        byte[] expectedContent = TestUtils.createTestInputStream("TestInputStream1.txt", "TestInputStream2.txt")
                .readAllBytes();
        Assert.assertArrayEquals(expectedContent, actualContent);
    }

    /**
     * Test method for {@link ChangeSetPerformer#perform(ArchiveInputStream, ArchiveOutputStream)}
     */
    @Test
    public void testDoNotAddFileIfAlreadyAdded() throws IOException {
        // Given
        ChangeSet changeSet = TestUtils.createTestChangeSetWithAdditions();
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);
        ByteArrayOutputStream outputStream = createTestOutputStream();
        ArchiveInputStream inputStream = createTestInputStream(
                "TestInputStream.txt", "testdata/testInput.tar.gz");

        // When
        performer.perform(inputStream, outputStream);
        ChangeSetResults results = performer.perform(inputStream, outputStream);

        // Then
        Assert.assertNotNull(results);
        Assert.assertTrue(results.hasBeenAdded("TestInputStream.txt"));
        Assert.assertFalse(results.hasBeenAdded("TestInputStream.txt"));

        InputStream output = outputStream.getInputStream();
        byte[] actualContent = IOUtils.toByteArray(output);
        byte[] expectedContent = TestUtils.createTestInputStream("TestInputStream.txt").readAllBytes();
        Assert.assertArrayEquals(expectedContent, actualContent);
    }

    /**
     * Test method for {@link ChangeSetPerformer#perform(ArchiveInputStream, ArchiveOutputStream)}
     */
    @Test(expected = IllegalStateException.class)
    public void testShouldNotPerformAfterSetChanges() throws IOException {
        // Given
        ChangeSet changeSet = TestUtils.createTestChangeSet();
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);

        // When and Then
        performer.perform(TestUtils.createTestInputStream("testdata/testInput.tar.gz"),
                TestUtils.createTestOutputStream());
        performer.perform(TestUtils.createTestInputStream("testdata/testInput.tar.gz"),
                TestUtils.createTestOutputStream());
    }

    /**
     * Helper method to create a test ChangeSet
     *
     * @return a test ChangeSet
     */
    private ChangeSet createTestChangeSet() {
        Set<Change> changeSet = new LinkedHashSet<>();
        changeSet.add(new Change("TestInputStream.txt", null, null, Change.TYPE_ADD));
        return new ChangeSet(changeSet);
    }

    /**
     * Helper method to create a test ChangeSet with additions
     *
     * @return a test ChangeSet
     */
    private ChangeSet createTestChangeSetWithAdditions() {
        Set<Change> changeSet = new LinkedHashSet<>();
        changeSet.add(new Change("TestInputStream.txt", null, null, Change.TYPE_ADD));
        changeSet.add(new Change("TestReplaceInputStream.txt", null, null, Change.TYPE_ADD, true));
        return new ChangeSet(changeSet);
    }

    /**
     * Helper method to create a test ChangeSet with additions and replacements
     *
     * @return a test ChangeSet
     */
    private ChangeSet createTestChangeSetWithAdditionsAndReplacements() {
        Set<Change> changeSet = new LinkedHashSet<>();
        changeSet.add(new Change("TestInputStream.txt", null, null, Change.TYPE_ADD));
        changeSet.add(new Change("TestReplaceInputStream.txt", null, null, Change.TYPE_ADD, true));
        changeSet.add(new Change("TestReplaceInputStream.txt", null, null, Change.TYPE_ADD, true));
        return new ChangeSet(changeSet);
    }

    /**
     * Helper method to create a test ChangeSet with deletions
     *
     * @return a test ChangeSet
     */
    private ChangeSet createTestChangeSetWithDeletions() {
        Set<Change> changeSet = new LinkedHashSet<>();
        changeSet.add(new Change("TestInputStream.txt", null, null, Change.TYPE_DELETE));
        return new ChangeSet(changeSet);
    }

    /**
     * Helper method to create a test ChangeSet with directory deletion
     *
     * @return a test ChangeSet
     */
    private ChangeSet createTestChangeSetWithDirectoryDeletion() {
        Set<Change> changeSet = new LinkedHashSet<>();
        changeSet.add(new Change("TestDirectory/TestDirectory/TestInputStream.txt", null, null, Change.TYPE_DELETE_DIR));
        return new ChangeSet(changeSet);
    }

    /**
     * Helper method to create a test ChangeSet with multiple additions
     *
     * @return a test ChangeSet
     */
    private ChangeSet createTestChangeSetWithMultipleAdditions() {
        Set<Change> changeSet = new LinkedHashSet<>();
        changeSet.add(new Change("TestInputStream1.txt", null, null, Change.TYPE_ADD));
        changeSet.add(new Change("TestInputStream2.txt", null, null, Change.TYPE_ADD));
        return new ChangeSet(changeSet);
    }
}