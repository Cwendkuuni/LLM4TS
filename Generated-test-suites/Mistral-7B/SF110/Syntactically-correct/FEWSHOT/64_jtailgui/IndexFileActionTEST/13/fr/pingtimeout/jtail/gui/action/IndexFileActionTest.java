package fr.pingtimeout.jtail.gui.action;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.event.*;
import java.io.*;
import java.util.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import fr.pingtimeout.jtail.controller.*;
import fr.pingtimeout.jtail.gui.action.*;
import fr.pingtimeout.jtail.gui.message.*;
import fr.pingtimeout.jtail.gui.model.*;
import fr.pingtimeout.jtail.io.*;
import fr.pingtimeout.jtail.io.index.*;

@RunWith(MockitoJUnitRunner.class)
public class IndexFileActionTest {

 private JTailMainModel jTailMainModel;
 private OpenFileModel openFileModel;
 private IndexFileAction indexFileAction;
 private JDialog dialog;
 private FileIndexer fileIndexer;
 private FileIndexerWorker fileIndexerWorker;

 @Before
 public void setUp() {
 jTailMainModel = mock(JTailMainModel.class);
 openFileModel = mock(OpenFileModel.class);
 dialog = mock(JDialog.class);
 indexFileAction = new IndexFileAction(jTailMainModel, openFileModel);
 fileIndexer = mock(FileIndexer.class);
 fileIndexerWorker = mock(FileIndexerWorker.class);
 }

 @Test
 public void testActionPerformedWithNullFile() {
 indexFileAction.actionPerformed(mock(ActionEvent.class));
 verify(jTailMainModel).add(null);
 }

 @Test
 public void testActionPerformedWithValidFileAndIndexType() {
 given(openFileModel.getFile()).willReturn(new File("test.txt"));
 given(openFileModel.getIndexType()).willReturn(IndexType.FILE_BASED);

 indexFileAction.actionPerformed(mock(ActionEvent.class));

 verify(jTailMainModel).add(argThat(m -> m instanceof JTailModel));
 verify(fileIndexerWorker).execute();
 }

 @Test
 public void testUpdate() {
 given(openFileModel.getFile()).willReturn(new File("test.txt"));
 given(openFileModel.getIndexType()).willReturn(IndexType.MEMORY_BASED);
 given(fileIndexer.getIndex()).willReturn(new HashMap<>());
 given(fileIndexer.getFile()).willReturn(new File("test.txt"));

 indexFileAction.update(fileIndexer, 100);

 verify(jTailMainModel).add(argThat(m -> m instanceof JTailModel));
 }

 @Test(expected = FileNotFoundException.class)
 public void testUpdateWithFileNotFoundException() {
 given(openFileModel.getFile()).willReturn(new File("test.txt"));
 given(openFileModel.getIndexType()).willReturn(IndexType.FILE_BASED);
 doThrow(new FileNotFoundException("Error message")).when(fileIndexer).addObserver(any());

 indexFileAction.update(fileIndexer, 100);
 }

 @Test
 public void testActionPerformedWithDialog() {
 indexFileAction.actionPerformed(mock(ActionEvent.class));
 verify(dialog).setVisible(false);
 }
}