package freemind.extensions;

import static org.junit.Assert.*;
import freemind.extensions.ExportHook;
import freemind.view.mindmapview.MapView;
import java.awt.Frame;
import java.io.File;
import org.junit.Before;
import org.junit.Test;

public class ExportHookTest {

  private ExportHook exportHook;
  private MapView view;

  @Before
  public void setup() {
    exportHook = new ExportHook();
    view = new MapView();
  }

  @Test
  public void testChooseFile_ValidFile() {
    final ExportHook exportHook = new ExportHook();
    final ModeController controller = Mockito.mock(ModeController.class);
    Mockito.when(controller.getFrame()).thenReturn(new Frame() {});
    final File expectedFile = new File("path/to/valid/file.png");
    final File actualFile = exportHook.chooseFile("image/*", "Description", "png", controller);
    assertEquals(expectedFile, actualFile);
  }

  @Test
  public void testChooseFile_InvalidFileExtension() {
    final ExportHook exportHook = new ExportHook();
    final ModeController controller = Mockito.mock(ModeController.class);
    final File expectedFile = new File("path/to/valid/file.txt");
    Mockito.when(controller.getFrame()).thenReturn(new Frame() {});
    final File actualFile = exportHook.chooseFile("image/*", "Description", null, controller);
    assertNull(actualFile);
  }

  @Test
  public void testGetTranslatableResourceString_ValidResourceName() {
    final ExportHook exportHook = new ExportHook();
    final String expectedString = "Expected translatable string";
    final String actualString = exportHook.getTranslatableResourceString("resource.name");
    assertEquals(expectedString, actualString);
  }

  @Test
  public void testGetTranslatableResourceString_InvalidResourceName() {
    final ExportHook exportHook = new ExportHook();
    final String expectedString = "Expected string";
    final String actualString = exportHook.getTranslatableResourceString("invalid.resource.name");
    assertEquals(expectedString, actualString);
  }

  @Test
  public void testCreateBufferedImage_ValidView() {
    exportHook.view = view;
    BufferedImage actualImage = exportHook.createBufferedImage();
    assertNotNull(actualImage);
  }

  @Test
  public void testCreateBufferedImage_InvalidView() {
    BufferedImage actualImage = exportHook.createBufferedImage();
    assertNull(actualImage);
  }
}