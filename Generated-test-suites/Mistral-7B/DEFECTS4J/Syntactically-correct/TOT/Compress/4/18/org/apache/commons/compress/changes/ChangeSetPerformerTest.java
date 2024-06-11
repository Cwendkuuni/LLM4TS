import org.apache.commons.compress.archivers.tar.TarInputStream;
import org.apache.commons.compress.archivers.tar.TarOutputStream;
import org.apache.commons.compress.archivers.zip.ZipInputStream;
import org.apache.commons.compress.archivers.zip.ZipOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import static org.junit.Assert.*;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.compressors.GzipCompressorInputStream;
import org.apache.commons.compress.compressors.GzipCompressorOutputStream;
import org.apache.commons.compress.utils.IOUtils;

import org.apache.commons.compress.changes.Change;
import org.apache.commons.compress.changes.ChangeSet;
import org.apache.commons.compress.changes.ChangeSetEntry;
import org.apache.commons.compress.changes.ChangeSetResults;
import org.apache.commons.compress.changes.ChangeSetPerformer;

@RunWith(org.mockito.runners.MockitoJUnitRunner.class)
public class ChangeSetPerformerTest {

 private ChangeSetPerformer performer;
 private ChangeSet changeSet;
 private ByteArrayOutputStream inStream;
 private ByteArrayOutputStream outStream;
 private ByteArrayInputStream inTest;
 private ArchiveInputStream in;
 private ArchiveOutputStream out;

 @Before
 public void setUp() throws Exception {
 MockitoAnnotations.initMocks(this);

 Set<Change> changes = new LinkedHashSet<>();
 changes.add(new Change("src/test/java/TestA.txt", "TestA_backup.txt", Change.TYPE_DELETE, false));
 changes.add(new Change("src/test/java/TestB.txt", "TestB_backup.txt", Change.TYPE_DELETE, false));
 changes.add(new Change("src/test/java/TestC.txt", "TestC_backup.txt", Change.TYPE_DELETE, false));
 changes.add(new Change("src/test/java/TestA.txt", "TestA.txt", Change.TYPE_ADD, true));
 changes.add(new Change("src/test/java/TestD.txt", "TestD.txt", Change.TYPE_ADD, true));

 changeSet = new ChangeSet();
 changeSet.setChanges(changes);

 performer = new ChangeSetPerformer(changeSet);

 inStream = new ByteArrayOutputStream();
 outStream = new ByteArrayOutputStream();

 inTest = new ByteArrayInputStream(getData("testData.tar.gz"));

 in = new TarInputStream(new GzipCompressorInputStream(inTest));
 out = new TarOutputStream(new GzipCompressorOutputStream(new ByteArrayOutputStream()));

 ArchiveStreamFactory streamFactory = new ArchiveStreamFactory();
 ArchiveInputStream testIn = streamFactory.createArchiveInputStream("TestInput.tar.gz", inTest);
 ArchiveOutputStream testOut = new TarOutputStream(new ByteArrayOutputStream());

 performer.perform(testIn, testOut);

 assertTrue(testOut.getNextEntry() == null);

 inTest = new ByteArrayInputStream(getData("testData.tar.gz"));
 in = new TarInputStream(new GzipCompressorInputStream(inTest));
 out = new TarOutputStream(new ByteArrayOutputStream());

 performer.perform(in, out);

 assertEquals(5, out.getNextEntry().getSize());
 }

 @Test
 public void testPerformWithEmptyChangeSet() throws Exception {
 Set<Change> emptyChanges = new LinkedHashSet<>();
 ChangeSet emptyChangeSet = new ChangeSet();
 emptyChangeSet.setChanges(emptyChanges);
 ChangeSetPerformer emptyPerformer = new ChangeSetPerformer(emptyChangeSet);

 ArchiveInputStream emptyInput = mock(ArchiveInputStream.class);
 ArchiveOutputStream emptyOutput = mock(ArchiveOutputStream.class);

 emptyPerformer.perform(emptyInput, emptyOutput);

 verify(emptyOutput, never()).putNextEntry(any(ArchiveEntry.class));
 }

 @Test
 public void testPerformWithSingleChange() throws Exception {
 Set<Change> changes = new LinkedHashSet<>();
 changes.add(new Change("TestA.txt", "TestA_backup.txt", Change.TYPE_DELETE, false));
 ChangeSet changeSet = new ChangeSet();
 changeSet.setChanges(changes);
 ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);

 ArchiveInputStream input = mock(ArchiveInputStream.class);
 ArchiveOutputStream output = mock(ArchiveOutputStream.class);

 when(input.getNextEntry()).thenReturn(mock(ArchiveEntry.class));
 when(input.readAllBytes()).thenReturn(getData("TestA.txt".getBytes()));
 when(output.putNextEntry(any(ArchiveEntry.class))).thenReturn(true);
 when(output.write(anyByteArray())).thenAnswer(i -> i.getByte()[0]);
 when(output.closeArchiveEntry()).thenReturn(true);

 performer.perform(input, output);

 verify(input, times(1)).getNextEntry();
 verify(output, times(1)).putNextEntry(any(ArchiveEntry.class));
 verify(output, times(1)).write(anyByteArray());
 verify(output, times(1)).closeArchiveEntry();
 }

 @Test
 public void testPerformWithSingleAddChange() throws Exception {
 Set<Change> changes = new LinkedHashSet<>();
 changes.add(new Change("TestA.txt", "TestA.txt", Change.TYPE_ADD, true));
 ChangeSet changeSet = new ChangeSet();
 changeSet.setChanges(changes);
 ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);

 ArchiveInputStream input = mock(ArchiveInputStream.class);
 ArchiveOutputStream output = mock(ArchiveOutputStream.class);

 when(input.getNextEntry()).thenReturn(null);
 when(output.putNextEntry(any(ArchiveEntry.class))).thenReturn(true);
 when(output.write(anyByteArray())).thenAnswer(i -> i.getByte()[0]);
 when(output.closeArchiveEntry()).thenReturn(true);

 performer.perform(input, output);

 assertEquals(0, input.read());
 verify(input, times(1)).close();
 verify(output, times(1)).putNextEntry(any(ArchiveEntry.class));
 verify(output, times(1)).write(anyByteArray());
 verify(output, times(1)).closeArchiveEntry();
 }

 @Test
 public void testPerformWithMultipleChanges() throws Exception {
 Set<Change> changes = new LinkedHashSet<>();
 changes.add(new Change("TestA.txt", "TestA_backup.txt", Change.TYPE_DELETE, false));
 changes.add(new Change("TestB.txt", "TestB_backup.txt", Change.TYPE_DELETE, false));
 changes.add(new Change("TestC.txt", "TestC_backup.txt", Change.TYPE_DELETE, false));
 changes.add(new Change("TestA.txt", "TestA.txt", Change.TYPE_ADD, true));
 changes.add(new Change("TestD.txt", "TestD.txt", Change.TYPE_ADD, true));
 ChangeSet changeSet = new ChangeSet();
 changeSet.setChanges(changes);
 ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);

 ArchiveInputStream input = mock(ArchiveInputStream.class);
 ArchiveOutputStream output = mock(ArchiveOutputStream.class);

 when(input.getNextEntry()).thenReturn(mock(ArchiveEntry.class));
 when(input.readAllBytes()).thenReturn(getData("TestA.txt".getBytes()));
 when(output.putNextEntry(any(ArchiveEntry.class))).thenReturn(true);
 when(output.write(anyByteArray())).thenAnswer(i -> i.getByte()[0]);
 when(output.closeArchiveEntry()).thenReturn(true);

 performer.perform(input, output);

 verify(input, times(1)).getNextEntry();
 verify(output, times(2)).putNextEntry(any(ArchiveEntry.class));
 verify(output, times(1)).write(anyByteArray());
 verify(output, times(1)).closeArchiveEntry();
 }

 @Test
 public void testPerformWithReplaceMode() throws Exception {
 Set<Change> changes = new LinkedHashSet<>();
 changes.add(new Change("TestA.txt", "TestA_backup.txt", Change.TYPE_DELETE, false));
 changes.add(new Change("TestA.txt", "TestA.txt", Change.TYPE_ADD, true, true));
 ChangeSet changeSet = new ChangeSet();
 changeSet.setChanges(changes);
 ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);

 ArchiveInputStream input = mock(ArchiveInputStream.class);
 ArchiveOutputStream output = mock(ArchiveOutputStream.class);

 when(input.getNextEntry()).thenReturn(mock(ArchiveEntry.class));
 when(input.readAllBytes()).thenReturn(getData("TestA.txt".getBytes()));
 when(output.putNextEntry(any(ArchiveEntry.class))).thenReturn(true);
 when(output.write(anyByteArray())).thenAnswer(i -> i.getByte()[0]);
 when(output.closeArchiveEntry()).thenReturn(true);

 performer.perform(input, output);

 verify(input, times(1)).getNextEntry();
 verify(output, times(1)).putNextEntry(any(ArchiveEntry.class));
 verify(output, times(1)).write(anyByteArray());
 verify(output, times(1)).closeArchiveEntry();
 }

 @Test(expected = IOException.class)
 public void testPerformWithNullInput() throws Exception {
 ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);
 performer.perform(null, out);
 fail("Should throw an IOException when input is null");
 }

 @Test(expected = IOException.class)
 public void testPerformWithNullOutput() throws Exception {
 ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);
 performer.perform(in, null);
 fail("Should throw an IOException when output is null");
 }

 @After
 public void tearDown() throws Exception {
 in.close();
 out.close();
 inTest.close();
 }

 private byte[] getData(String name) throws IOException {
 return IOUtils.toByteArray(getClass().getResource(name));
 }
}