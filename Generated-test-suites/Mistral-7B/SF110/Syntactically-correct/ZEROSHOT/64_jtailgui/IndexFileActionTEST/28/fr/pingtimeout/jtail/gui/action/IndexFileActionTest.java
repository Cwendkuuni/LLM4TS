package fr.pingtimeout.jtail.gui.action;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.event.ActionEvent;
import java.io.File;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import fr.pingtimeout.jtail.gui.controller.JTailMainModel;
import fr.pingtimeout.jtail.gui.message.InformationHandler;
import fr.pingtimeout.jtail.gui.message.UIMessage;
import fr.pingtimeout.jtail.gui.model.JTailModel;
import fr.pingtimeout.jtail.io.index.FileIndexer;
import fr.pingtimeout.jtail.io.index.FileIndexerWorker;
import fr.pingtimeout.jtail.io.index.FileIndexer.IndexType;
import fr.pingtimeout.jtail.io.index.RamFileIndex;
import fr.pingtimeout.jtail.io.index.RomFileIndex;

public class IndexFileActionTest {

 private IndexFileAction indexFileAction;
 private JTailMainModel jTailMainModelMock;
 private OpenFileModel openFileModelMock;
 private FileIndexer fileIndexerMock;
 private FileIndexerWorker fileIndexerWorkerMock;

 @Before
 public void setUp() {
 jTailMainModelMock = mock(JTailMainModel.class);
 openFileModelMock = new OpenFileModel();
 indexFileAction = new IndexFileAction(jTailMainModelMock, openFileModelMock);
 fileIndexerMock = mock(FileIndexer.class);
 fileIndexerWorkerMock = mock(FileIndexerWorker.class);
 }

 @Test
 public void testActionPerformedWithNullFile() {
 openFileModelMock.setFile(null);
 indexFileAction.actionPerformed(new ActionEvent(new JButton(), ActionEvent.ACTION_PERFORMED, ""));
 verify(InformationHandler::handle, times(1)).handle(true, UIMessage.INFO_NO_FILE_SELECTED, new Object[0]);
 }

 @Test
 public void testActionPerformedWithValidFileAndIndexType() throws Exception {
 openFileModelMock.setFile(Mockito.mock(File.class));
 openFileModelMock.setIndexType(IndexType.FILE_BASED);
 when(fileIndexerMock.getClass()).thenReturn(RomFileIndex.class);
 when(fileIndexerMock.addObserver(Mockito.any(Observer.class))).thenReturn(fileIndexerMock);
 when(fileIndexerMock.getFile()).thenReturn(openFileModelMock.getFile());
 when(fileIndexerMock.getIndex()).thenReturn(new byte[0][0]);
 when(fileIndexerMock.createIndex()).thenReturn(new byte[0][0]);
 when(fileIndexerWorkerMock.execute()).thenReturn(true);

 indexFileAction.actionPerformed(new ActionEvent(new JButton(), ActionEvent.ACTION_PERFORMED, ""));
 verify(jTailMainModelMock).add(Mockito.any(JTailModel.class));
 }

 @Test(expected = FileNotFoundException.class)
 public void testActionPerformedWithInvalidFile() throws Exception {
 openFileModelMock.setFile(Mockito.mock(File.class));
 when(fileIndexerMock.getClass()).thenReturn(RamFileIndex.class);
 when(fileIndexerMock.addObserver(Mockito.any(Observer.class))).thenReturn(fileIndexerMock);
 when(fileIndexerMock.getFile()).thenThrow(new FileNotFoundException("Test error"));

 indexFileAction.actionPerformed(new ActionEvent(new JButton(), ActionEvent.ACTION_PERFORMED, ""));
 verify(InformationHandler::handle, times(1)).handle(true, UIMessage.ERROR_FILE_NOT_FOUND, openFileModelMock.getFile().getName());
 }

 @Test
 public void testUpdateWithIndexingDone() throws Exception {
 openFileModelMock.setFile(Mockito.mock(File.class));
 openFileModelMock.setIndexType(IndexType.FILE_BASED);
 when(fileIndexerMock.getClass()).thenReturn(RomFileIndex.class);
 when(fileIndexerMock.addObserver(Mockito.any(Observer.class))).thenReturn(fileIndexerMock);
 when(fileIndexerMock.getFile()).thenReturn(openFileModelMock.getFile());
 when(fileIndexerMock.getIndex()).thenReturn(new byte[0][0]);
 when(fileIndexerMock.createIndex()).thenReturn(new byte[0][0]);
 when(fileIndexerWorkerMock.execute()).thenReturn(true);

 indexFileAction.actionPerformed(new ActionEvent(new JButton(), ActionEvent.ACTION_PERFORMED, ""));
 verify(jTailMainModelMock, times(1)).add(Mockito.any(JTailModel.class));
 }
}