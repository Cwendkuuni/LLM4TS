package com.allenstudio.ir.ui;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import java.awt.Image;
import java.awt.event.KeyEvent;
import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenuItem;
import javax.swing.JMenu;
import javax.swing.KeyStroke;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.allenstudio.ir.core.InspirentoMediator;
import com.allenstudio.ir.ui.InspirentoUtilities;
import com.allenstudio.ir.util.UIResources;

@RunWith(PowerMockRunner.class)
@PrepareForTest({UIResources.class, InspirentoUtilities.class})
public class MainMenuTest {

    @Test
    public void testCreateMenu() {
        InspirentoMediator mediator = Mockito.mock(InspirentoMediator.class);
        String menuString = "File";
        String[] expectedItems = {"Open", "Save", "Save As", "Separator", "New", "Import", "Export", "Separator", "Exit"};
        PowerMockito.stubStatic(UIResources.class, "getString", (String key) -> {
            if (key.startsWith("menu.") || key.startsWith("MenuItem.")) {
                return "mockMenu_" + key.replaceAll("\\.", "_");
            }
            return key;
        });
        PowerMockito.stubStatic(InspirentoUtilities.class, "tokenize", (String s) -> expectedItems);

        MainMenu menu = new MainMenu(mediator);
        JMenu actualMenu = menu.createMenu(menuString);

        int expectedSize = 10;
        int actualSize = actualMenu.getItemCount();
        assertEquals(expectedSize, actualSize);

        List<JMenuItem> actualJMenuItemList = Arrays.asList(actualMenu.getMenuComponents());
        for (int i = 0; i < expectedSize; i++) {
            String expectedKey = "menu.File" + (i + 1);
            JMenuItem actualJMenuItem = actualJMenuItemList.get(i);
            String expectedLabel = "mockMenu_MenuItem_" + (i + 1);

            if (i < 6) {
                assertNotNull(actualJMenuItem.getAccelerator());
                assertEquals(KeyEvent.VK_F + (i + 1), actualJMenuItem.getAccelerator().getKeyCode());
                assertNull(actualJMenuItem.getIcon());
            } else if (i == 6) {
                assertNull(actualJMenuItem.getAccelerator());
                assertNull(actualJMenuItem.getIcon());
                assertNotNull(actualJMenuItem instanceof JCheckBoxMenuItem);
                JCheckBoxMenuItem checkboxMenuItem = (JCheckBoxMenuItem) actualJMenuItem;
                assertNotNull(checkboxMenuItem.isSelected());
            } else {
                assertNull(actualJMenuItem.getAccelerator());
                assertNull(actualJMenuItem.getIcon());
            }

            assertEquals(expectedLabel, actualJMenuItem.getText());
        }
    }

    @Test
    public void testAddShortcutAndIcon() {
        JMenuItem item = Mockito.mock(JMenuItem.class);
        String key = "MenuItem1";

        PowerMockito.stubStatic(UIResources.class, "getString", (String key) -> "mockShortcut_" + key);
        PowerMockito.stubStatic(UIResources.class, "getString", (String key) -> "mockIcon_" + key);

        MainMenu menu = new MainMenu(null);
        menu.addShortcutAndIcon(item, key);

        assertNotNull(item.getAccelerator());
        assertEquals(KeyEvent.VK_1, item.getAccelerator().getKeyCode());

        assertNull(item.getIcon());

        PowerMockito.verifyStatic(UIResources.class).getString(eq("MenuItem1Shortcut"));
        PowerMockito.verifyStatic(UIResources.class).getString(eq("MenuItem1Icon"));
    }

    @Test
    public void testAddShortcutAndIcon_NullIcon() {
        JMenuItem item = Mockito.mock(JMenuItem.class);
        String key = "MenuItem1";

        PowerMockito.stubStatic(UIResources.class, "getString", (String key) -> "mockShortcut_" + key);

        MainMenu menu = new MainMenu(null);
        menu.addShortcutAndIcon(item, key);

        assertNotNull(item.getAccelerator());
        assertEquals(KeyEvent.VK_1, item.getAccelerator().getKeyCode());

        assertNull(item.getIcon());

        PowerMockito.verifyStatic(UIResources.class).getString(eq("MenuItem1Shortcut"));
    }

    @Test
    public void testAddShortcutAndIcon_NullKey() {
        JMenuItem item = Mockito.mock(JMenuItem.class);

        MainMenu menu = new MainMenu(null);
        menu.addShortcutAndIcon(item, null);

        assertNull(item.getAccelerator());
        assertNull(item.getIcon());
    }
}