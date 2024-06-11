import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.compress.changes.Change;
import org.apache.commons.compress.changes.ChangeSet;
import org.apache.commons.compress.changes.ChangeSetPerformer;
import org.apache.commons.compress.changes.ChangeSetResults;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ChangeSetPerformerTest {

 private ChangeSet changeSet;
 private ChangeSetPerformer performer;
 private ArchiveInputStream inputMock;
 private ArchiveOutputStream outputMock;

 @BeforeEach
 public void setUp() {
 changeSet = mock(ChangeSet.class);
 performer = new ChangeSetPerformer(changeSet);
 inputMock = mock(ArchiveInputStream.class);
 outputMock = mock(ArchiveOutputStream.class);
 }

 @Test
 public void testPerform_addAndReplaceMode() throws Exception {
 // Given
 Change addChange = mock(Change.class);
 when(addChange.getType()).thenReturn(Change.TYPE_ADD);
 when(addChange.isReplaceMode()).thenReturn(true);
 Change addChange2 = mock(Change.class);
 when(addChange2.getType()).thenReturn(Change.TYPE_ADD);
 when(addChange2.isReplaceMode()).thenReturn(false);
 Change deleteChange = mock(Change.class);
 when(deleteChange.getType()).thenReturn(Change.TYPE_DELETE);
 Change addChangeWithSameName = mock(Change.class);
 when(addChangeWithSameName.getType()).thenReturn(Change.TYPE_ADD);
 when(addChangeWithSameName.isReplaceMode()).thenReturn(true);
 when(changeSet.getChanges()).thenReturn(asList(addChange, addChange2, deleteChange, addChangeWithSameName).iterator());
 ArchiveEntry entry = mock(ArchiveEntry.class);
 when(entry.getName()).thenReturn("test.txt");

 // When
 ChangeSetResults results = performer.perform(inputMock, outputMock);

 // Then
 verify(inputMock, times(2)).getNextEntry();
 verify(outputMock, times(2)).putArchiveEntry(eq(entry));
 verify(outputMock, times(2)).writeTo(anyByteArray());
 verify(outputMock, times(2)).closeArchiveEntry();
 assertEquals(3, results.getAddedFromChangeSet().size());
 assertEquals("test.txt", results.getAddedFromChangeSet().get(0).getName());
 assertNotNull(results.getAddedFromStream());
 assertEquals(0, results.getDeleted().size());
 }

 @Test
 public void testPerform_addAndNoReplaceMode() throws Exception {
 // Given
 Change addChange = mock(Change.class);
 when(addChange.getType()).thenReturn(Change.TYPE_ADD);
 when(addChange.isReplaceMode()).thenReturn(false);
 Change addChange2 = mock(Change.class);
 when(addChange2.getType()).thenReturn(Change.TYPE_ADD);
 when(addChange2.isReplaceMode()).thenReturn(false);
 Change deleteChange = mock(Change.class);
 when(deleteChange.getType()).thenReturn(Change.TYPE_DELETE);
 Change addChangeWithSameName = mock(Change.class);
 when(addChangeWithSameName.getType()).thenReturn(Change.TYPE_ADD);
 when(addChangeWithSameName.isReplaceMode()).thenReturn(true);
 when(changeSet.getChanges()).thenReturn(asList(addChange, addChange2, deleteChange, addChangeWithSameName).iterator());
 ArchiveEntry entry = mock(ArchiveEntry.class);
 when(entry.getName()).thenReturn("test.txt");

 // When
 ChangeSetResults results = performer.perform(inputMock, outputMock);

 // Then
 verify(inputMock, times(2)).getNextEntry();
 verify(outputMock, times(2)).putArchiveEntry(eq(entry));
 verify(outputMock, times(1)).writeTo(anyByteArray());
 verify(outputMock, times(2)).closeArchiveEntry();
 assertEquals(3, results.getAddedFromChangeSet().size());
 assertEquals("test.txt", results.getAddedFromChangeSet().get(0).getName());
 assertNotNull(results.getAddedFromStream());
 assertEquals(0, results.getDeleted().size());
 }

 @Test
 public void testPerform_delete() throws Exception {
 // Given
 Change addChange = mock(Change.class);
 when(addChange.getType()).thenReturn(Change.TYPE_ADD);
 Change deleteChange = mock(Change.class);
 when(deleteChange.getType()).thenReturn(Change.TYPE_DELETE);
 when(deleteChange.getTargetFile()).thenReturn("test.txt");
 Change addChangeWithSameName = mock(Change.class);
 when(addChangeWithSameName.getType()).thenReturn(Change.TYPE_ADD);
 when(addChangeWithSameName.isReplaceMode()).thenReturn(true);
 when(changeSet.getChanges()).thenReturn(asList(addChange, deleteChange, addChangeWithSameName).iterator());
 ArchiveEntry entry = mock(ArchiveEntry.class);
 when(entry.getName()).thenReturn("test.txt");

 // When
 ChangeSetResults results = performer.perform(inputMock, outputMock);

 // Then
 verify(inputMock, times(1)).getNextEntry();
 verify(outputMock, times(0)).putArchiveEntry(any(ArchiveEntry.class));
 verify(outputMock, times(0)).writeTo(anyByteArray());
 verify(outputMock, times(1)).closeArchiveEntry();
 assertEquals(1, results.getDeleted().size());
 assertEquals("test.txt", results.getDeleted().get(0));
 assertEquals(0, results.getAddedFromChangeSet().size());
 assertEquals(0, results.getAddedFromStream().size());
 }

 @Test
 public void testPerform_deleteDir() throws Exception {
 // Given
 Change addChange = mock(Change.class);
 when(addChange.getType()).thenReturn(Change.TYPE_ADD);
 Change deleteDirChange = mock(Change.class);
 when(deleteDirChange.getType()).thenReturn(Change.TYPE_DELETE_DIR);
 when(deleteDirChange.getTargetFile()).thenReturn("testDir");
 when(changeSet.getChanges()).thenReturn(asList(addChange, deleteDirChange).iterator());
 ArchiveEntry entry = mock(ArchiveEntry.class);
 when(entry.getName()).thenReturn("testDir/test.txt");

 // When
 ChangeSetResults results = performer.perform(inputMock, outputMock);

 // Then
 verify(inputMock, times(1)).getNextEntry();
 verify(outputMock, times(0)).putArchiveEntry(any(ArchiveEntry.class));
 verify(outputMock, times(0)).writeTo(anyByteArray());
 verify(outputMock, times(1)).closeArchiveEntry();
 assertEquals(0, results.getDeleted().size());
 assertEquals(0, results.getAddedFromChangeSet().size());
 assertEquals(1, results.getDeletedFromStream().size());
 assertEquals("testDir/test.txt", results.getDeletedFromStream().get(0));
 }

 @Test
 public void testPerform_copyStream() throws Exception {
 // Given
 InputStream inputStreamMock = mock(InputStream.class);
 when(inputMock.getInputStream()).thenReturn(inputStreamMock);
 ArchiveOutputStream outputMock = mock(ArchiveOutputStream.class);
 ArchiveEntry entry = mock(ArchiveEntry.class);
 when(entry.getName()).thenReturn("test.txt");
 when(entry.getSize()).thenReturn(100L);
 when(outputMock.writeTo(anyByteArray())).thenAnswer(i -> i.getArguments()[0]);

 // When
 performer.copyStream(inputStreamMock, outputMock, entry);

 // Then
 verify(outputMock, times(1)).putArchiveEntry(eq(entry));
 verify(outputMock, times(1)).writeTo(anyByteArray());
 verify(outputMock, times(1)).closeArchiveEntry();
 }

 @Test
 public void testPerform_copyStreamWithException() throws Exception {
 // Given
 InputStream inputStreamMock = mock(InputStream.class);
 doThrow(new IOException("Exception")) // IOException is a checked exception
 .when(inputStreamMock).read();
 ArchiveOutputStream outputMock = mock(ArchiveOutputStream.class);
 ArchiveEntry entry = mock(ArchiveEntry.class);
 when(entry.getName()).thenReturn("test.txt");
 when(entry.getSize()).thenReturn(100L);

 // When
 performer.copyStream(inputStreamMock, outputMock, entry);

 // Then
 verify(outputMock, never()).putArchiveEntry(any(ArchiveEntry.class));
 verify(outputMock, never()).writeTo(anyByteArray());
 verify(outputMock, never()).closeArchiveEntry();
 }

 @Test
 public void testIsDeletedLater() throws Exception {
 // Given
 Change addChange = mock(Change.class);
 when(addChange.getType()).thenReturn(Change.TYPE_ADD);
 Change deleteChange = mock(Change.class);
 when(deleteChange.getType()).thenReturn(Change.TYPE_DELETE);
 Change addChangeWithSameName = mock(Change.class);
 when(addChangeWithSameName.getType()).thenReturn(Change.TYPE_ADD);
 when(addChangeWithSameName.isReplaceMode()).thenReturn(true);
 Set<Change> workingSet = new LinkedHashSet<>();
 workingSet.add(addChange);
 workingSet.add(deleteChange);
 workingSet.add(addChangeWithSameName);

 // When
 boolean result = performer.isDeletedLater(workingSet, mock(ArchiveEntry.class));

 // Then
 assertFalse(result);
 }

 @Test
 public void testIsDeletedLater_deleteBeforeAdd() throws Exception {
 // Given
 Change addChange = mock(Change.class);
 when(addChange.getType()).thenReturn(Change.TYPE_ADD);
 Change deleteChange = mock(Change.class);
 when(deleteChange.getType()).thenReturn(Change.TYPE_DELETE);
 Change addChangeWithSameName = mock(Change.class);
 when(addChangeWithSameName.getType()).thenReturn(Change.TYPE_ADD);
 when(addChangeWithSameName.isReplaceMode()).thenReturn(true);
 Set<Change> workingSet = new LinkedHashSet<>();
 workingSet.add(deleteChange);
 workingSet.add(addChange);
 workingSet.add(addChangeWithSameName);

 // When
 boolean result = performer.isDeletedLater(workingSet, mock(ArchiveEntry.class));

 // Then
 assertTrue(result);
 }
}