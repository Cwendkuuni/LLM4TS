package macaw.util;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import macaw.businessLayer.*;
import macaw.system.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

import org.junit.*;
import org.mockito.*;

public class DisplayableListPanelTest
{
 private DisplayableListPanel displayableListPanel;
 private SessionProperties sessionProperties;
 private DisplayableListParentForm parentForm;
 private UserInterfaceFactory userInterfaceFactory;
 private Log log;

 @Before
 public void setUp()
 {
 this.sessionProperties = mock(SessionProperties.class);
 this.parentForm = mock(DisplayableListParentForm.class);
 this.userInterfaceFactory = mock(UserInterfaceFactory.class);
 this.log = mock(Log.class);
 this.displayableListPanel = new DisplayableListPanel(this.sessionProperties, this.parentForm, "Test Panel Title", true);
 }

 @Test
 public void testDisplayableListPanelConstruction()
 {
 assertNotNull(this.displayableListPanel);
 }

 @Test
 public void testGetPanel()
 {
 JPanel panel = this.displayableListPanel.getPanel();
 assertNotNull(panel);
 }

 @Test
 public void testSetDisplayableListItemSelector()
 {
 DisplayableListItemSelector listItemSelector = mock(DisplayableListItemSelector.class);
 this.displayableListPanel.setDisplayableListItemSelector(listItemSelector);
 assertEquals(listItemSelector, this.displayableListPanel.getListItemSelector());
 }

 @Test
 public void testSetDisplayableListItemAdder()
 {
 DisplayableListItemAdder listItemAdder = mock(DisplayableListItemAdder.class);
 this.displayableListPanel.setDisplayableListItemAdder(listItemAdder);
 assertEquals(listItemAdder, this.displayableListPanel.getListItemAdder());
 }

 @Test
 public void testSetDisplayableListItemEditor()
 {
 DisplayableListItemEditor listItemEditor = mock(DisplayableListItemEditor.class);
 this.displayableListPanel.setDisplayableListItemEditor(listItemEditor);
 assertEquals(listItemEditor, this.displayableListPanel.getListItemEditor());
 }

 @Test
 public void testSetDisplayableListItemViewer()
 {
 DisplayableListItemViewer listItemViewer = mock(DisplayableListItemViewer.class);
 this.displayableListPanel.setDisplayableListItemViewer(listItemViewer);
 assertEquals(listItemViewer, this.displayableListPanel.getListItemViewer());
 }

 @Test
 public void testSetDisplayableListItemDeleter()
 {
 DisplayableListItemDeleter listItemDeleter = mock(DisplayableListItemDeleter.class);
 this.displayableListPanel.setDisplayableListItemDeleter(listItemDeleter);
 assertEquals(listItemDeleter, this.displayableListPanel.getListItemDeleter());
 }

 @Test
 public void testAddListItem()
 {
 final String listOwnerName = "Test List Owner";
 DisplayableList displayableList = mock(DisplayableList.class);
 Displayable displayable = mock(Displayable.class);
 when(this.displayableListPanel.getDisplayableList()).thenReturn(displayableList);
 when(this.displayableListPanel.getListItemAdder()).thenReturn(mock(DisplayableListItemAdder.class));

 this.displayableListPanel.addListItem();
 verify(this.displayableListPanel.getListItemAdder()).addItems(eq(listOwnerName), eq(displayableList));
 }

 @Test(expected = MacawException.class)
 public void testAddListItemMissingAdder()
 {
 final String listOwnerName = "Test List Owner";
 DisplayableList displayableList = mock(DisplayableList.class);
 this.displayableListPanel.setDisplayableList(displayableList);

 try {
 this.displayableListPanel.addListItem();
 fail("Should have thrown MacawException for missing addItems method");
 }
 catch (MacawException e) {
 // expected
 }
 }

 @Test
 public void testEditSelectedListItem()
 {
 final String listOwnerName = "Test List Owner";
 Displayable selectedListItem = mock(Displayable.class);
 DisplayableList displayableList = mock(DisplayableList.class);
 when(this.displayableListPanel.getDisplayableList()).thenReturn(displayableList);
 when(this.displayableListPanel.getSelectedListItem()).thenReturn(selectedListItem);
 when(this.displayableListPanel.getListItemEditor()).thenReturn(mock(DisplayableListItemEditor.class));

 this.displayableListPanel.editSelectedListItem();
 verify(this.displayableListPanel.getListItemEditor()).editListItem(eq(listOwnerName), eq(selectedListItem), eq(displayableList));
 }

 @Test(expected = MacawException.class)
 public void testEditSelectedListItemMissingEditor()
 {
 final String listOwnerName = "Test List Owner";
 Displayable selectedListItem = mock(Displayable.class);
 DisplayableList displayableList = mock(DisplayableList.class);
 when(this.displayableListPanel.getDisplayableList()).thenReturn(displayableList);
 when(this.displayableListPanel.getSelectedListItem()).thenReturn(selectedListItem);

 try {
 this.displayableListPanel.editSelectedListItem();
 fail("Should have thrown MacawException for missing editListItem method");
 }
 catch (MacawException e) {
 // expected
 }
}

 @Test
 public void testViewSelectedListItem()
 {
 final Displayable selectedListItem = mock(Displayable.class);
 this.displayableListPanel.setDisplayableListItemViewer(mock(DisplayableListItemViewer.class));
 this.displayableListPanel.setSelectedListItem(selectedListItem);
 this.displayableListPanel.viewSelectedListItem();
 verify(this.displayableListPanel.getListItemViewer()).viewListItem(eq(selectedListItem));
 }

 @Test(expected = MacawException.class)
 public void testViewSelectedListItemMissingViewer()
 {
 final Displayable selectedListItem = mock(Displayable.class);
 this.displayableListPanel.setSelectedListItem(selectedListItem);

 try {
 this.displayableListPanel.viewSelectedListItem();
 fail("Should have thrown MacawException for missing viewListItem method");
 }
 catch (MacawException e) {
 // expected
 }
 }

 @Test
 public void testDeleteSelectedListItems()
 {
 final String listOwnerName = "Test List Owner";
 final int numberOfItemsToDelete = 2;
 DisplayableList displayableList = mock(DisplayableList.class);
 when(this.displayableListPanel.getDisplayableList()).thenReturn(displayableList);
 when(this.displayableListPanel.getListItemDeleter()).thenReturn(mock(DisplayableListItemDeleter.class));

 JOptionPane.showConfirmDialog(eq(null), anyString(), anyString(), eq(0), eq(2));
 this.displayableListPanel.deleteSelectedListItems();
 verify(this.displayableListPanel.getListItemDeleter()).deleteListItems(eq(listOwnerName), eq(displayableList));
 }

 @Test(expected = MacawException.class)
 public void testDeleteSelectedListItemsMissingDeleter()
 {
 final String listOwnerName = "Test List Owner";
 final int numberOfItemsToDelete = 2;
 DisplayableList displayableList = mock(DisplayableList.class);
 when(this.displayableListPanel.getDisplayableList()).thenReturn(displayableList);

 try {
 this.displayableListPanel.deleteSelectedListItems();
 fail("Should have thrown MacawException for missing deleteListItems method");
 }
 catch (MacawException e) {
 // expected
 }
 }

 @Test
 public void testSelectListItems()
 {
 final String listOwnerName = "Test List Owner";
 final int numberOfItemsToSelect = 3;
 DisplayableList displayableList = mock(DisplayableList.class);
 when(this.displayableListPanel.getDisplayableList()).thenReturn(displayableList);
 when(this.displayableListPanel.getListItemSelector()).thenReturn(mock(DisplayableListItemSelector.class));

 this.displayableListPanel.selectListItems();
 verify(this.displayableListPanel.getListItemSelector()).selectListItems(eq(listOwnerName), eq(displayableList));
 }

 @Test(expected = MacawException.class)
 public void testSelectListItemsMissingSelector()
 {
 final String listOwnerName = "Test List Owner";
 final int numberOfItemsToSelect = 3;
 DisplayableList displayableList = mock(DisplayableList.class);
 when(this.displayableListPanel.getDisplayableList()).thenReturn(displayableList);

 try {
 this.displayableListPanel.selectListItems();
 fail("Should have thrown MacawException for missing selectListItems method");
 }
 catch (MacawException e) {
 // expected
 }
 }

 @Test
 public void testSetDisplayableItems()
 {
 final ArrayList<Displayable> masterList = new ArrayList<>();
 DisplayableList displayableList = mock(DisplayableList.class);
 when(this.displayableListPanel.getDisplayableList()).thenReturn(displayableList);
 this.displayableListPanel.setDisplayableItems(masterList);
 verify(this.displayableListPanel.getDisplayableList()).setDisplayItems(eq(masterList));
 }

 @Test
 public void testSave()
 {
 this.displayableListPanel.save();
 assertTrue(this.displayableListPanel.getAllListItems().isEmpty());
 }

 @Test
 public void testGetAllListItems()
 {
 final ArrayList<Displayable> allListItems = new ArrayList<>();
 allListItems.add(mock(Displayable.class));
 this.displayableListPanel.setDisplayableItems(allListItems);
 assertEquals(allListItems, this.displayableListPanel.getAllListItems());
 }

 @Test
 public void testSetListOwnerName()
 {
 final String newListOwnerName = "New List Owner";
 this.displayableListPanel.setListOwnerName(newListOwnerName);
 assertEquals(newListOwnerName, this.displayableListPanel.getListOwnerName());
 }

 @Test
 public void testActionPerformedSelect()
 {
 this.displayableListPanel.setDisplayableListItemSelector(mock(DisplayableListItemSelector.class));
 this.displayableListPanel.selectListItems();
 this.displayableListPanel.actionPerformed(mock(ActionEvent.class));
 verify(this.displayableListPanel.getListItemSelector()).selectListItems(eq("Test List Owner"), eq(this.displayableListPanel.getDisplayableList()));
 }

 @Test
 public void testActionPerformedAdd()
 {
 this.displayableListPanel.setDisplayableListItemAdder(mock(DisplayableListItemAdder.class));
 this.displayableListPanel.actionPerformed(mock(ActionEvent.class));
 verify(this.displayableListPanel.getListItemAdder()).addItems("Test List Owner", this.displayableListPanel.getDisplayableList());
 }

 @Test
 public void testActionPerformedEdit()
 {
 this.displayableListPanel.setDisplayableListItemEditor(mock(DisplayableListItemEditor.class));
 Displayable selectedListItem = mock(Displayable.class);
 this.displayableListPanel.setSelectedListItem(selectedListItem);
 this.displayableListPanel.actionPerformed(mock(ActionEvent.class));
 verify(this.displayableListPanel.getListItemEditor()).editListItem("Test List Owner", eq(selectedListItem), eq(this.displayableListPanel.getDisplayableList()));
 }

 @Test
 public void testActionPerformedView()
 {
 this.displayableListPanel.setDisplayableListItemViewer(mock(DisplayableListItemViewer.class));
 Displayable selectedListItem = mock(Displayable.class);
 this.displayableListPanel.setSelectedListItem(selectedListItem);
 this.displayableListPanel.actionPerformed(mock(ActionEvent.class));
 verify(this.displayableListPanel.getListItemViewer()).viewListItem(eq(selectedListItem));
 }

 @Test
 public void testActionPerformedDelete()
 {
 this.displayableListPanel.setDisplayableListItemDeleter(mock(DisplayableListItemDeleter.class));
 this.displayableListPanel.actionPerformed(mock(ActionEvent.class));
 verify(this.displayableListPanel.getListItemDeleter()).deleteListItems("Test List Owner", this.displayableListPanel.getDisplayableList());
 }
}