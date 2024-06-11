package macaw.util;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import macaw.businessLayer.*;
import java.awt.*;
import macaw.system.*;
import javax.swing.*;
import java.util.*;
import java.awt.event.*;

@RunWith(MockitoJUnitRunner.class)
public class DisplayableListPanelTest {

 private DisplayableListPanel displayableListPanel;
 private SessionProperties sessionProperties;
 private DisplayableListParentForm parentForm;
 private UserInterfaceFactory userInterfaceFactory;
 private DisplayableList displayableList;
 private DisplayableListItemSelector listItemSelector;
 private DisplayableListItemAdder listItemAdder;
 private DisplayableListItemEditor listItemEditor;
 private DisplayableListItemViewer listItemViewer;
 private DisplayableListItemDeleter listItemDeleter;

 @Before
 public void setup() {
 sessionProperties = Mockito.mock(SessionProperties.class);
 parentForm = Mockito.mock(DisplayableListParentForm.class);
 userInterfaceFactory = Mockito.mock(UserInterfaceFactory.class);
 displayableList = Mockito.mock(DisplayableList.class);
 listItemSelector = Mockito.mock(DisplayableListItemSelector.class);
 listItemAdder = Mockito.mock(DisplayableListItemAdder.class);
 listItemEditor = Mockito.mock(DisplayableListItemEditor.class);
 listItemViewer = Mockito.mock(DisplayableListItemViewer.class);
 listItemDeleter = Mockito.mock(DisplayableListItemDeleter.class);

 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Displayable List Panel", true);
 displayableListPanel.setDisplayableListItemSelector(listItemSelector);
 displayableListPanel.setDisplayableListItemAdder(listItemAdder);
 displayableListPanel.setDisplayableListItemEditor(listItemEditor);
 displayableListPanel.setDisplayableListItemViewer(listItemViewer);
 displayableListPanel.setDisplayableListItemDeleter(listItemDeleter);
 displayableListPanel.displayableList = displayableList;
 }

 @Test
 public void testInit() {
 assertEquals("Displayable List Panel", displayableListPanel.getPanelTitleText());
 assertTrue(displayableListPanel.isNewRecord);
 assertNull(displayableListPanel.listItemSelector);
 assertNull(displayableListPanel.listItemAdder);
 assertNull(displayableListPanel.listItemEditor);
 assertNull(displayableListPanel.listItemViewer);
 assertNull(displayableListPanel.listItemDeleter);
 assertNull(displayableListPanel.panel);
 assertNull(displayableListPanel.displayableList);
 assertNull(displayableListPanel.select);
 assertNull(displayableListPanel.add);
 assertNull(displayableListPanel.edit);
 assertNull(displayableListPanel.view);
 assertNull(displayableListPanel.delete);
 }

 @Test
 public void testBuildUI() {
 DisplayableListPanel panelUnderTest = displayableListPanel;
 assertNotNull(panelUnderTest.panel);
 assertEquals(2, panelUnderTest.panel.getComponentCount());
 }

 @Test
 public void testCreateControlButtonPanel() {
 DisplayableListPanel panelUnderTest = displayableListPanel;
 JPanel controlButtonPanel = panelUnderTest.createControlButtonPanel();
 assertNotNull(controlButtonPanel);
 assertTrue(controlButtonPanel.getComponentCount() > 0);
 }

 @Test
 public void testSetDisplayableItems() {
 DisplayableListPanel panelUnderTest = displayableListPanel;
 ArrayList<Displayable> displayables = new ArrayList<>();
 displayableListPanel.setDisplayableItems(displayables);
 assertEquals(displayables, displayableListPanel.displayableList.getAllItems());
 }

 @Test
 public void testSave() {
 DisplayableListPanel panelUnderTest = displayableListPanel;
 ArrayList<Displayable> displayables = new ArrayList<>();
 displayableListPanel.setDisplayableItems(displayables);
 panelUnderTest.save();
 assertFalse(displayableListPanel.masterList.isEmpty());
 assertTrue(displayableListPanel.masterList.equals(displayableListPanel.displayableList.getAllItems()));
 }

 @Test
 public void testSetNewRecord() {
 DisplayableListPanel panelUnderTest = displayableListPanel;
 panelUnderTest.setNewRecord(false);
 assertFalse(panelUnderTest.isNewRecord);
 }

 @Test
 public void testUpdateButtonStates() {
 DisplayableListPanel panelUnderTest = displayableListPanel;
 // test when isNewRecord is true
 panelUnderTest.isNewRecord = true;
 Mockito.when(displayableList.isEmpty()).thenReturn(true);
 Mockito.when(displayableList.itemsAreSelected()).thenReturn(false);
 Mockito.when(listItemDeleter.isEnabled()).thenReturn(false);
 Mockito.when(listItemEditor.isEnabled()).thenReturn(false);
 Mockito.when(listItemViewer.isEnabled()).thenReturn(false);
 Mockito.when(listItemAdder.isEnabled()).thenReturn(false);
 Mockito.when(listItemSelector.isEnabled()).thenReturn(false);
 panelUnderTest.updateButtonStates();
 assertFalse(panelUnderTest.add.isEnabled());
 assertFalse(panelUnderTest.delete.isEnabled());
 assertFalse(panelUnderTest.edit.isEnabled());
 assertFalse(panelUnderTest.view.isEnabled());
 assertFalse(panelUnderTest.select.isEnabled());

 // test when isNewRecord is false and list is empty
 panelUnderTest.isNewRecord = false;
 Mockito.when(displayableList.isEmpty()).thenReturn(true);
 Mockito.when(displayableList.itemsAreSelected()).thenReturn(false);
 Mockito.when(listItemDeleter.isEnabled()).thenReturn(false);
 Mockito.when(listItemEditor.isEnabled()).thenReturn(false);
 Mockito.when(listItemViewer.isEnabled()).thenReturn(false);
 Mockito.when(listItemAdder.isEnabled()).thenReturn(true);
 Mockito.when(listItemSelector.isEnabled()).thenReturn(true);
 panelUnderTest.updateButtonStates();
 assertTrue(panelUnderTest.add.isEnabled());
 assertFalse(panelUnderTest.delete.isEnabled());
 assertFalse(panelUnderTest.edit.isEnabled());
 assertFalse(panelUnderTest.view.isEnabled());
 assertFalse(panelUnderTest.select.isEnabled());

 // test when isNewRecord is false and list is not empty
 panelUnderTest.isNewRecord = false;
 Mockito.when(displayableList.isEmpty()).thenReturn(false);
 Mockito.when(displayableList.itemsAreSelected()).thenReturn(true);
 Mockito.when(listItemDeleter.isEnabled()).thenReturn(true);
 Mockito.when(listItemEditor.isEnabled()).thenReturn(true);
 Mockito.when(listItemViewer.isEnabled()).thenReturn(true);
 Mockito.when(listItemAdder.isEnabled()).thenReturn(true);
 Mockito.when(listItemSelector.isEnabled()).thenReturn(true);
 panelUnderTest.updateButtonStates();
 assertTrue(panelUnderTest.add.isEnabled());
 assertTrue(panelUnderTest.delete.isEnabled());
 assertTrue(panelUnderTest.edit.isEnabled());
 assertTrue(panelUnderTest.view.isEnabled());
 assertTrue(panelUnderTest.select.isEnabled());
 }

 @Test
 public void testSetListOwnerName() {
 DisplayableListPanel panelUnderTest = displayableListPanel;
 panelUnderTest.setListOwnerName("Test List Owner");
 assertEquals("Test List Owner", panelUnderTest.listOwnerName);
 }

 @Test
 public void testActionPerformed() {
 DisplayableListPanel panelUnderTest = displayableListPanel;
 ActionEvent actionEvent = Mockito.mock(ActionEvent.class);
 Mockito.when(displayableList.getSelectedItem()).thenReturn(Mockito.mock(Displayable.class));
 Mockito.when(listItemSelector.selectListItems(Mockito.anyString(), Mockito.any(DisplayableList.class))).thenReturn(true);
 Mockito.when(listItemAdder.addItems(Mockito.anyString(), Mockito.any(DisplayableList.class))).thenReturn(true);
 Mockito.when(listItemEditor.editListItem(Mockito.anyString(), Mockito.any(Displayable.class), Mockito.any(DisplayableList.class))).thenReturn(true);
 Mockito.when(listItemViewer.viewListItem(Mockito.any(Displayable.class))).thenReturn(true);
 Mockito.when(listItemDeleter.deleteListItems(Mockito.anyString(), Mockito.any(DisplayableList.class))).thenReturn(true);

 panelUnderTest.actionPerformed(actionEvent);
 Mockito.verify(listItemSelector, Mockito.times(1)).selectListItems(displayableListPanel.listOwnerName, displayableList);
 }

}