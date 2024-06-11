package org.apache.commons.compress.changes;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class ChangeSetPerformerTest {

    private ChangeSet changeSet;
    private InputStream inputStream;
    private FileOutputStream outputStream;
    private ChangeSetPerformer performer;
    private ArchiveInputStream archiveInput;
    private ArchiveOutputStream archiveOutput;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Before
    public void setUp() throws Exception {
        changeSet = new ChangeSet();
        Change addFile = new Change(Change.TYPE_ADD, "testFile.txt", null, null, false);
        Change addFileReplace = new Change(Change.TYPE_ADD, "testFile.txt", null, null, true);
        Change deleteFile = new Change(Change.TYPE_DELETE, "testFile.txt", null, null, null);
        Change deleteDirectory = new Change(Change.TYPE_DELETE_DIR, "testDir/", null, null, null);
        changeSet.addChange(addFile);
        changeSet.addChange(addFileReplace);
        changeSet.addChange(deleteFile);
        changeSet.addChange(deleteDirectory);

        inputStream = new FileInputStream(temporaryFolder.newFile());
        outputStream = new FileOutputStream(temporaryFolder.newFile());
        performer = new ChangeSetPerformer(changeSet);
        archiveInput = new ArchiveInputStream(inputStream);
        archiveOutput = new ArchiveOutputStream(outputStream);
    }

    @After
    public void tearDown() throws Exception {
        IOUtils.closeQuietly(inputStream);
        IOUtils.closeQuietly(outputStream);
        IOUtils.closeQuietly(archiveInput);
        IOUtils.closeQuietly(archiveOutput);
        temporaryFolder.delete();
    }

    @Test
    public void testPerform_NoChanges() throws IOException {
        ChangeSetResults result = performer.perform(archiveInput, archiveOutput);
        assertTrue(Collections.emptySet().equals(result.getAddedFromChangeSet()));
        assertTrue(Collections.emptySet().equals(result.getDeleted()));
        assertTrue(Collections.emptySet().equals(result.getAddedFromStream()));
    }

    @Test
    public void testPerform_AddFile() throws IOException {
        String expectedAdded = "testFile.txt";

        InputStream input = temporaryFolder.newFileOutputStream("inputFile.txt");
        IOUtils.write("Test file content.", input);
        input.close();
        Change addFile = new Change(Change.TYPE_ADD, expectedAdded, null, input, false);
        changeSet.addChange(addFile);

        changeSet = new ChangeSet(changeSet); // new instance since performer constructor requires immutable ChangeSet
        performer = new ChangeSetPerformer(changeSet);

        result = performer.perform(archiveInput, archiveOutput);
        assertTrue( Arrays.asList(expectedAdded).equals(result.getAddedFromChangeSet()) );
        assertTrue( result.getDeleted().isEmpty() );
        assertTrue( Arrays.asList(expectedAdded).equals(result.getAddedFromStream()) );

        ArchiveEntry entry = archiveInput.getNextEntry();
        assertEquals(expectedAdded, entry.getName());
    }

    @Test
    public void testPerform_AddFileReplaceMode() throws IOException {
        String expectedAdded = "testFile.txt";

        InputStream input = temporaryFolder.newFileOutputStream("inputFile.txt");
        IOUtils.write("Test file content.", input);
        input.close();
        Change addFileReplace = new Change(Change.TYPE_ADD, expectedAdded, null, input, true);
        changeSet.addChange(addFileReplace);

        performer = new ChangeSetPerformer(changeSet);

        result = performer.perform(archiveInput, archiveOutput);
        assertTrue( Arrays.asList(expectedAdded).equals(result.getAddedFromChangeSet()) );
        assertTrue( result.getDeleted().isEmpty() );
        assertTrue( Arrays.asList(expectedAdded).equals(result.getAddedFromStream()) );

        ArchiveEntry entry = archiveInput.getNextEntry();
        assertEquals(expectedAdded, entry.getName());
    }

    @Test
    public void testPerform_DeleteFile() throws IOException {
        String expectedDeleted = "testFile.txt";

        InputStream input = temporaryFolder.newFileOutputStream("testFile.txt");
        IOUtils.write("Test file content.", input);
        input.close();

        changeSet.addChange(new Change(Change.TYPE_ADD, "testFile.txt", null, null, false));
        changeSet.addChange(new Change(Change.TYPE_DELETE, expectedDeleted, null, null, null));

        performer = new ChangeSetPerformer(changeSet);

        result = performer.perform(archiveInput, archiveOutput);
        assertTrue( Collections.emptySet().equals(result.getAddedFromChangeSet()) );
        assertTrue( Collections.singleton(expectedDeleted).equals(result.getDeleted()) );
        assertTrue( Collections.emptySet().equals(result.getAddedFromStream()) );

        ArchiveEntry entry = archiveInput.getNextEntry();
        assertEquals(expectedDeleted, entry.getName());

        entry = archiveInput.getNextEntry();
        assertNull(entry);
    }

    @Test
    public void testPerform_DeleteDirectory() throws IOException {
        String expectedDeletedDir = "testDir/";

        InputStream input = temporaryFolder.newFileOutputStream("testDir/testFile.txt");
        IOUtils.write("Test file content.", input);
        input.close();

        changeSet.addChange(new Change(Change.TYPE_ADD, "testDir/testFile.txt", null, null, false));
        changeSet.addChange(new Change(Change.TYPE_DELETE_DIR, expectedDeletedDir, null, null, null));

        performer = new ChangeSetPerformer(changeSet);

        result = performer.perform(archiveInput, archiveOutput);
        assertTrue( Collections.emptySet().equals(result.getAddedFromChangeSet()) );
        assertTrue( Collections.emptySet().equals(result.getDeleted()) );
        assertTrue( Collections.emptySet().equals(result.getAddedFromStream()) );

        ArchiveEntry entry = archiveInput.getNextEntry();
        assertNull(entry);
    }

    @Test
    public void testPerform_AddFileAfterDelete() throws IOException {
        String expectedAdded = "testFile.txt";
        String expectedDeleted = "testFile.txt";

        InputStream input = temporaryFolder.newFileOutputStream("testFile.txt");
        IOUtils.write("Test file content.", input);
        input.close();

        changeSet.addChange(new Change(Change.TYPE_DELETE, expectedDeleted, null, null, null));
        changeSet.addChange(new Change(Change.TYPE_ADD, expectedAdded, null, null, false));

        performer = new ChangeSetPerformer(changeSet);

        result = performer.perform(archiveInput, archiveOutput);
        assertTrue( Collections.emptySet().equals(result.getAddedFromChangeSet()) );
        assertTrue( Collections.singleton(expectedDeleted).equals(result.getDeleted()) );
        assertTrue( Collections.emptySet().equals(result.getAddedFromStream()) );

        ArchiveEntry entry = archiveInput.getNextEntry();
        assertEquals(expectedDeleted, entry.getName());

        entry = archiveInput.getNextEntry();
        assertNull(entry);

        performer = new ChangeSetPerformer(changeSet); // new instance since performer constructor requires immutable ChangeSet

        result = performer.perform(archiveInput, archiveOutput);
        assertTrue( Arrays.asList(expectedAdded).equals(result.getAddedFromChangeSet()) );
        assertTrue( result.getDeleted().isEmpty() );
        assertTrue( Arrays.asList(expectedAdded).equals(result.getAddedFromStream()) );

        entry = archiveInput.getNextEntry();
        assertEquals(expectedAdded, entry.getName());
    }

    @Test
    public void testPerform_AddDirectoryAfterDelete() throws IOException {
        String expectedAddedDir = "testDir/";
        String expectedDeletedDir = "testDir/";

        InputStream input = temporaryFolder.newFileOutputStream("testDir/testFile.txt");
        IOUtils.write("Test file content.", input);
        input.close();

        changeSet.addChange(new Change(Change.TYPE_DELETE_DIR, expectedDeletedDir, null, null, null));
        changeSet.addChange(new Change(Change.TYPE_ADD, expectedAddedDir, null, null, null));

        performer = new ChangeSetPerformer(changeSet);

        result = performer.perform(archiveInput, archiveOutput);
        assertTrue( Collections.emptySet().equals(result.getAddedFromChangeSet()) );
        assertTrue( Collections.emptySet().equals(result.getDeleted()) );
        assertTrue( Collections.emptySet().equals(result.getAddedFromStream()) );

        ArchiveEntry entry = archiveInput.getNextEntry();
        assertNull(entry);

        performer = new ChangeSetPerformer(changeSet); // new instance since performer constructor requires immutable ChangeSet

        result = performer.perform(archiveInput, archiveOutput);
        assertTrue( Arrays.asList(expectedAddedDir).equals(result.getAddedFromChangeSet()) );
        assertTrue( result.getDeleted().isEmpty() );
        assertTrue( result.getAddedFromStream().isEmpty() );

        entry = archiveInput.getNextEntry();
        assertEquals(expectedAddedDir, entry.getName());
    }

    @Test
    public void testPerform_PerformTwice() throws IOException {
        String expectedAdded = "testFile.txt";

        InputStream input = temporaryFolder.newFileOutputStream("inputFile.txt");
        IOUtils.write("Test file content.", input);
        input.close();
        Change addFile = new Change(Change.TYPE_ADD, expectedAdded, null, input, false);
        changeSet.addChange(addFile);

        performer = new ChangeSetPerformer(changeSet);

        result = performer.perform(archiveInput, archiveOutput);
        assertTrue( Arrays.asList(expectedAdded).equals(result.getAddedFromChangeSet()) );
        assertTrue( result.getDeleted().isEmpty() );
        assertTrue( Arrays.asList(expectedAdded).equals(result.getAddedFromStream()) );

        ArchiveEntry entry = archiveInput.getNextEntry();
        assertEquals(expectedAdded, entry.getName());

        result = performer.perform(archiveInput, archiveOutput);
        assertTrue( Collections.emptySet().equals(result.getAddedFromChangeSet()) );
        assertTrue( result.getDeleted().isEmpty() );
        assertTrue( Collections.emptySet().equals(result.getAddedFromStream()) );

        entry = archiveInput.getNextEntry();
        assertNull(entry);
    }

    @Test
    public void testPerform_InvalidArchiveFormat() throws IOException {
        InputStream input = new FileInputStream(temporaryFolder.newFile("notAnArchive.txt"));
        expectedException.expect(IOException.class);
        expectedException.expectMessage("notAnArchive.txt is not a valid archive file");

        performer = new ChangeSetPerformer(changeSet);
        performer.perform(new ArchiveInputStream(input), archiveOutput);
    }

    @Test
    public void testPerform_NullChangeSet() {
        expectedException.expect(NullPointerException.class);
        expectedException.expectMessage("changeSet is marked non-null but is null");

        performer = new ChangeSetPerformer(null);
    }

    @Test
    public void testPerform_NullArchiveInput() {
        expectedException.expect(NullPointerException.class);
        expectedException.expectMessage("archiveInput is marked non-null but is null");

        performer = new ChangeSetPerformer(changeSet);
        performer.perform(null, archiveOutput);
    }

    @Test
    public void testPerform_NullArchiveOutput() {
        expectedException.expect(NullPointerException.class);
        expectedException.expectMessage("archiveOutput is marked non-null but is null");

        performer = new ChangeSetPerformer(changeSet);
        performer.perform(archiveInput, null);
    }
}