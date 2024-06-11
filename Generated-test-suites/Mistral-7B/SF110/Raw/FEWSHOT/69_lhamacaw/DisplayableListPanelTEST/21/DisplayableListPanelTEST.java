package macaw.util;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.event.ActionEvent;
import java.util.ArrayList;

import macaw.businessLayer.*;
import macaw.system.*;
import javax.swing.*;
import org.junit.Before;
import org.junit.Test;

public class DisplayableListPanelTest {

 private DisplayableListPanel displayableListPanel;
 private SessionProperties sessionProperties;
 private DisplayableListParentForm parentForm;
 private UserInterfaceFactory userInterfaceFactory;
 private DisplayableList displayableList;
 private JButton select;
 private JButton add;
 private JButton edit;
 private JButton view;
 private JButton delete;
 private JPanel panel;

 @Before
 public void setUp() {
 sessionProperties = mock(SessionProperties.class);
 parentForm = mock(DisplayableListParentForm.class);
 userInterfaceFactory = mock(UserInterfaceFactory.class);
 displayableList = mock(DisplayableList.class);
 select = mock(JButton.class);
 add = mock(JButton.class);
 edit = mock(JButton.class);
 view = mock(JButton.class);
 delete = mock(JButton.class);
 panel = mock(JPanel.class);

 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", false);
 displayableListPanel.init(sessionProperties, parentForm, "Test Panel", false, false);

 when(userInterfaceFactory.createButton(anyString())).thenAnswer(invocation -> invocation.getArguments()[0]);
 when(parentForm.commitChanges()).thenReturn(true);
 doNothing().when(displayableList).setDisplayItems(anyList());
 doNothing().when(displayableList).getListScrollPane();
 doNothing().when(displayableList).getAllItems();
 doReturn(null).when(displayableList).getSelectedItem();
 doReturn(false).when(displayableList).itemsAreSelected();
 doReturn(true).when(displayableList).isEmpty();
 }

 @Test
 public void testDisplayableListPanelInitialization() {
 assertNotNull(displayableListPanel);
 assertEquals("Test Panel", displayableListPanel.getPanelTitleText());
 assertNull(displayableListPanel.getPanel());
 }

 @Test
 public void testSetPanel() {
 displayableListPanel.buildUI();
 assertNotNull(displayableListPanel.getPanel());
 }

 @Test
 public void testActionPerformedSelect() {
 displayableListPanel.select = select;
 doNothing().when(select).doClick();
 displayableListPanel.actionPerformed(new ActionEvent(select, 0, "Test"));
 verify(displayableListPanel).selectListItems();
 }

 @Test
 public void testActionPerformedAdd() {
 displayableListPanel.add = add;
 doNothing().when(add).doClick();
 displayableListPanel.actionPerformed(new ActionEvent(add, 0, "Test"));
 verify(parentForm).commitChanges();
 verify(displayableListPanel.listItemAdder).addItems(eq("Test List Owner"), eq(displayableList));
 }

 @Test
 public void testActionPerformedEdit() {
 displayableListPanel.edit = edit;
 doNothing().when(edit).doClick();
 Displayable selectedListItem = new Displayable();
 when(displayableList.getSelectedItem()).thenReturn(selectedListItem);
 displayableListPanel.actionPerformed(new ActionEvent(edit, 0, "Test"));
 verify(parentForm).commitChanges();
 verify(displayableListItemEditor).editListItem(eq("Test List Owner"), eq(selectedListItem), eq(displayableList));
 }

 @Test
 public void testActionPerformedView() {
 displayableListPanel.view = view;
 doNothing().when(view).doClick();
 Displayable selectedListItem = new Displayable();
 when(displayableList.getSelectedItem()).thenReturn(selectedListItem);
 displayableListPanel.actionPerformed(new ActionEvent(view, 0, "Test"));
 verify(displayableListItemViewer).viewListItem(eq(selectedListItem));
 }

 @Test
 public void testActionPerformedDelete() {
 displayableListPanel.delete = delete;
 doNothing().when(delete).doClick();
 JOptionPane.showConfirmDialog(any(), anyString(), anyString(), anyInt())
 .thenReturn(JOptionPane.OK_OPTION);
 displayableListPanel.actionPerformed(new ActionEvent(delete, 0, "Test"));
 verify(parentForm).commitChanges();
 verify(displayableListItemDeleter).deleteListItems(eq("Test List Owner"), eq(displayableList));
 }
}