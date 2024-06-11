import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

public class ChangeSetPerformerTest {

 private ChangeSetPerformer performer;
 private Set<Change> changeSet;
 private ArchiveInputStream inputMock;
 private ArchiveOutputStream outputMock;

 @Before
 public void setUp() {
 Change addChange1 = new Change("file1.txt", Change.TYPE_ADD, false);
 Change addChange2 = new Change("file2.txt", Change.TYPE_ADD, false);
 Change addChange3 = new Change("file3.txt", Change.TYPE_ADD, false);
 Change deleteChange1 = new Change("file1.txt", Change.TYPE_DELETE, false);
 Change deleteChange2 = new Change("file3.txt", Change.TYPE_DELETE, false);
 Change replaceChange1 = new Change("file1.txt", Change.TYPE_ADD, true);

 changeSet = new HashSet<>(Arrays.asList(addChange1, addChange2, addChange3, deleteChange1, deleteChange2, replaceChange1));

 performer = new ChangeSetPerformer(new ChangeSet(changeSet.toArray(new Change[0])));
 inputMock = Mockito.mock(ArchiveInputStream.class);
 outputMock = Mockito.mock(ArchiveOutputStream.class);
 }

 @Test
 public void testPerform_NoChanges() throws IOException {
 when(inputMock.getNextEntry()).thenReturn(null);

 ChangeSetResults results = performer.perform(inputMock, outputMock);
 assertEquals(Collections.emptyMap(), results.getAdded());
 assertEquals(Collections.emptyMap(), results.getDeleted());
 }

 @Test
 public void testPerform_AddChange() throws IOException {
 when(inputMock.getNextEntry()).thenReturn(null);
 when(inputMock.getNextEntry()).thenReturn(Mockito.mock(ArchiveEntry.class));
 when(inputMock.getNextEntry()).thenReturn(null);

 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 when(outputMock.getOutputStream()).thenReturn(outputStream);

 ChangeSetResults results = performer.perform(inputMock, outputMock);
 assertEquals(3, results.getAdded().size());
 assertEquals("file1.txt", results.getAdded().get("file1.txt"));
 assertEquals("file2.txt", results.getAdded().get("file2.txt"));
 assertEquals("file3.txt", results.getAdded().get("file3.txt"));
 }

 @Test
 public void testPerform_DeleteChange() throws IOException {
 when(inputMock.getNextEntry()).thenReturn(Mockito.mock(ArchiveEntry.class));

 ChangeSetResults results = performer.perform(inputMock, outputMock);
 assertEquals(2, results.getDeleted().size());
 assertEquals("file3.txt", results.getDeleted().get("file3.txt"));
 assertEquals("file1.txt", results.getDeleted().get("file1.txt"));
 }

 @Test
 public void testPerform_ReplaceModeAddChange() throws IOException {
 when(inputMock.getNextEntry()).thenReturn(Mockito.mock(ArchiveEntry.class));
 when(inputMock.getNextEntry()).thenReturn(null);

 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 when(outputMock.getOutputStream()).thenReturn(outputStream);

 ChangeSetResults results = performer.perform(inputMock, outputMock);
 assertEquals(2, results.getAdded().size());
 assertEquals("file1.txt", results.getAdded().get("file1.txt"));
 assertEquals("file1.txt", results.getAdded().get("file1.txt")); // replace change should not add a new entry
 }

 @Test
 public void testPerform_DeleteLater() throws IOException {
 when(inputMock.getNextEntry()).thenReturn(Mockito.mock(ArchiveEntry.class));
 ArchiveEntry deleteEntry = Mockito.mock(ArchiveEntry.class);
 when(inputMock.getNextEntry()).thenReturn(deleteEntry);
 when(inputMock.getNextEntry()).thenReturn(null);

 ChangeSetResults results = performer.perform(inputMock, outputMock);
 assertEquals(1, results.getDeleted().size());
 assertEquals("file3.txt", results.getDeleted().get("file3.txt"));
 }

 @Test
 public void testPerform_MultipleChanges() throws IOException {
 when(inputMock.getNextEntry()).thenReturn(Mockito.mock(ArchiveEntry.class));
 when(inputMock.getNextEntry()).thenReturn(Mockito.mock(ArchiveEntry.class));
 when(inputMock.getNextEntry()).thenReturn(null);

 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 when(outputMock.getOutputStream()).thenReturn(outputStream);

 ChangeSetResults results = performer.perform(inputMock, outputMock);
 assertEquals(4, results.getAdded().size());
 assertEquals("file1.txt", results.getAdded().get("file1.txt"));
 assertEquals("file2.txt", results.getAdded().get("file2.txt"));
 assertEquals("file3.txt", results.getAdded().get("file3.txt"));
 assertEquals("file1.txt", results.getDeleted().get("file1.txt"));
 }
}