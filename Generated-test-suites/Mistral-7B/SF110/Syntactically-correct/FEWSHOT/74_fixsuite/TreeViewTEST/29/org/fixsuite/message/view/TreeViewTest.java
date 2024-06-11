package org.fixsuite.message.view;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import javax.swing.*;
import javax.swing.tree.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import org.fixsuite.message.info.*;

@RunWith(MockitoJUnitRunner.class)
public class TreeViewTest {

 private TreeView treeView;
 private JTree fixTreeMock;
 private JScrollPane mainScrollPaneMock;
 private JLabel searchLabelMock;
 private JTextField searchTextFieldMock;
 private JButton searchButtonMock;
 private JButton clearButtonMock;
 private JButton expandButtonMock;
 private JButton collapseButtonMock;
 private Library libraryMock;
 private FixInfoTreeNode rootNodeMock;
 private FixInfoTreeNode versionNodeMock;
 private FixInfoTreeNode messageNodeMock;
 private FixInfoTreeNode componentNodeMock;
 private FixInfoTreeNode fieldNodeMock;

 @Before
 public void setUp() {
 treeView = new TreeView(libraryMock);
 fixTreeMock = Mockito.mock(JTree.class);
 mainScrollPaneMock = Mockito.mock(JScrollPane.class);
 searchLabelMock = Mockito.mock(JLabel.class);
 searchTextFieldMock = Mockito.mock(JTextField.class);
 searchButtonMock = Mockito.mock(JButton.class);
 clearButtonMock = Mockito.mock(JButton.class);
 expandButtonMock = Mockito.mock(JButton.class);
 collapseButtonMock = Mockito.mock(JButton.class);
 libraryMock = Mockito.mock(Library.class);
 rootNodeMock = Mockito.mock(FixInfoTreeNode.class);
 versionNodeMock = Mockito.mock(FixInfoTreeNode.class);
 messageNodeMock = Mockito.mock(FixInfoTreeNode.class);
 componentNodeMock = Mockito.mock(FixInfoTreeNode.class);
 fieldNodeMock = Mockito.mock(FixInfoTreeNode.class);

 treeView.fixTree = fixTreeMock;
 treeView.mainScrollPane = mainScrollPaneMock;
 treeView.searchLabel = searchLabelMock;
 treeView.searchTextField = searchTextFieldMock;
 treeView.searchButton = searchButtonMock;
 treeView.clearButton = clearButtonMock;
 treeView.expandButton = expandButtonMock;
 treeView.collapseButton = collapseButtonMock;
 treeView.library = libraryMock;

 Mockito.when(libraryMock.getDictionaries()).thenReturn(new ArrayList<>());
 }

 @Test
 public void testInitGUI() {
 // Test if the GUI components are added correctly
 verify(mainScrollPaneMock, times(1)).setPreferredSize(argThat(new IsDimension(300, 400)));
 verify(mainScrollPaneMock, times(1)).add(fixTreeMock);
 verify(add, times(1)).add(mainScrollPaneMock);
 verify(add, times(1)).add(searchLabelMock);
 verify(searchLabelMock, times(1)).setText("Search ");
 verify(searchTextFieldMock, times(1)).setToolTipText("Search for Item (regex)");
 verify(searchTextFieldMock, times(1)).setColumns(20);
 verify(searchTextFieldMock, times(1)).addKeyListener(Mockito.any(KeyListener.class));
 verify(searchButtonMock, times(1)).addActionListener(treeView);
 verify(clearButtonMock, times(1)).addActionListener(treeView);
 verify(expandButtonMock, times(1)).addActionListener(treeView);
 verify(collapseButtonMock, times(1)).addActionListener(treeView);
 }

 @Test
 public void testLoadFIXTree() {
 // Test if the FIXTree is loaded correctly
 Mockito.when(libraryMock.getDictionaries()).thenReturn(new ArrayList<>( Arrays.asList(new DictionaryInfo("Dict1", "1.0"), new DictionaryInfo("Dict2", "1.0"))));
 Mockito.when(rootNodeMock.getUserObject()).thenReturn("Dictionaries");
 Mockito.when(versionNodeMock.getUserObject()).thenReturn("1.0");
 Mockito.when(messageNodeMock.getUserObject()).thenReturn("MessageName");
 Mockito.when(componentNodeMock.getUserObject()).thenReturn("ComponentName");
 Mockito.when(fieldNodeMock.getUserObject()).thenReturn("FieldName");

 Mockito.when(libraryMock.getDictionaries())
 .thenReturn(new ArrayList<>( Arrays.asList(
 new DictionaryInfo("Dict1", "1.0"),
 new DictionaryInfo("Dict2", "1.0"))));

 Mockito.when(rootNodeMock.getChildren()).thenReturn(new ArrayList<>( Arrays.asList(versionNodeMock, versionNodeMock)));
 Mockito.when(versionNodeMock.getChildren()).thenReturn(new ArrayList<>( Arrays.asList(messageNodeMock, componentNodeMock)));
 Mockito.when(messageNodeMock.getChildren()).thenReturn(new ArrayList<>());
 Mockito.when(componentNodeMock.getChildren()).thenReturn(new ArrayList<>( Arrays.asList(fieldNodeMock, fieldNodeMock)));
 Mockito.when(fieldNodeMock.getChildren()).thenReturn(new ArrayList<>());

 Mockito.when(fixTreeMock.getModel()).thenReturn(new DefaultTreeModel(rootNodeMock, null));

 treeView.initGUI();

 verify(fixTreeMock, times(1)).setCellRenderer(Mockito.any(TreeCellRenderer.class));
 verify(fixTreeMock, times(1)).addTreeSelectionListener(treeView);
 }

 @Test
 public void testSearch() {
 // Test if the search function works correctly
 Mockito.when(libraryMock.getDictionaries()).thenReturn(new ArrayList<>( Arrays.asList(new DictionaryInfo("Dict1", "1.0"), new DictionaryInfo("Dict2", "1.0"))));
 Mockito.when(rootNodeMock.getChildren()).thenReturn(new ArrayList<>( Arrays.asList(versionNodeMock, versionNodeMock)));
 Mockito.when(versionNodeMock.getChildren()).thenReturn(new ArrayList<>( Arrays.asList(messageNodeMock, componentNodeMock)));
 Mockito.when(messageNodeMock.getChildren()).thenReturn(new ArrayList<>( Arrays.asList(fieldNodeMock, fieldNodeMock)));
 Mockito.when(fieldNodeMock.getChildren()).thenReturn(new ArrayList<>());

 Mockito.when(fixTreeMock.getSelectionModel().getLastSelectedPathComponent()).thenReturn(fieldNodeMock);
 Mockito.when(searchTextFieldMock.getText()).thenReturn("Test");

 treeView.searchButton.doClick();

 verify(fieldNodeMock, times(1)).setHighlighted(true);
 verify(fixTreeMock, times(1)).expandPath(argThat(new IsTreePath(fieldNodeMock.getPath())));
 }

 @Test
 public void testClear() {
 // Test if the clear function works correctly
 Mockito.when(libraryMock.getDictionaries()).thenReturn(new ArrayList<>( Arrays.asList(new DictionaryInfo("Dict1", "1.0"), new DictionaryInfo("Dict2", "1.0"))));
 Mockito.when(rootNodeMock.getChildren()).thenReturn(new ArrayList<>( Arrays.asList(versionNodeMock, versionNodeMock)));
 Mockito.when(versionNodeMock.getChildren()).thenReturn(new ArrayList<>( Arrays.asList(messageNodeMock, componentNodeMock)));
 Mockito.when(messageNodeMock.getChildren()).thenReturn(new ArrayList<>( Arrays.asList(fieldNodeMock, fieldNodeMock)));
 Mockito.when(fieldNodeMock.getChildren()).thenReturn(new ArrayList<>());

 Mockito.when(fixTreeMock.getSelectionModel().getLastSelectedPathComponent()).thenReturn(fieldNodeMock);

 treeView.searchTextField.setText("Test");
 treeView.searchButton.doClick();
 treeView.clearButton.doClick();

 verify(fieldNodeMock, times(1)).setHighlighted(false);
 }

 @Test
 public void testExpandAll() {
 // Test if the expandAll function works correctly
 Mockito.when(libraryMock.getDictionaries()).thenReturn(new ArrayList<>( Arrays.asList(new DictionaryInfo("Dict1", "1.0"), new DictionaryInfo("Dict2", "1.0"))));
 Mockito.when(rootNodeMock.getChildren()).thenReturn(new ArrayList<>( Arrays.asList(versionNodeMock, versionNodeMock)));
 Mockito.when(versionNodeMock.getChildren()).thenReturn(new ArrayList<>( Arrays.asList(messageNodeMock, componentNodeMock)));
 Mockito.when(messageNodeMock.getChildren()).thenReturn(new ArrayList<>( Arrays.asList(fieldNodeMock, fieldNodeMock)));
 Mockito.when(fieldNodeMock.getChildren()).thenReturn(new ArrayList<>());

 treeView.expandAll();

 for (int i = 0; i < 4; ++i) {
 verify(fixTreeMock, times(1)).expandRow(i);
 }
 }

 @Test
 public void testCollapseAll() {
 // Test if the collapseAll function works correctly
 Mockito.when(libraryMock.getDictionaries()).thenReturn(new ArrayList<>( Arrays.asList(new DictionaryInfo("Dict1", "1.0"), new DictionaryInfo("Dict2", "1.0"))));
 Mockito.when(rootNodeMock.getChildren()).thenReturn(new ArrayList<>( Arrays.asList(versionNodeMock, versionNodeMock)));
 Mockito.when(versionNodeMock.getChildren()).thenReturn(new ArrayList<>( Arrays.asList(messageNodeMock, componentNodeMock)));
 Mockito.when(messageNodeMock.getChildren()).thenReturn(new ArrayList<>( Arrays.asList(fieldNodeMock, fieldNodeMock)));
 Mockito.when(fieldNodeMock.getChildren()).thenReturn(new ArrayList<>());

 treeView.collapseAll();

 for (int i = 3; i >= 0; --i) {
 verify(fixTreeMock, times(1)).collapseRow(i);
 }
 }
}