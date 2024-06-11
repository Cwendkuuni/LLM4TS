import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import jaw.entrada.Abrir;
import java.io.File;
import java.io.IOException;

public class AbrirTest {
 private Abrir obj;
 private String xmlFile = "test.jaw.xml";
 private String serialFile = "test.jaw";

 @Before
 public void setUp() {
 obj = new Abrir(new ProcessarEntidades(), xmlFile);
 }

 @Test
 public void testAbrirXML() {
 // Testing normal scenario
 assertNotNull(obj.getForm().getEntidades());
 // Testing throwing exception when file is not xml
 obj = new Abrir(new ProcessarEntidades(), "test.txt");
 assertNull(obj.getForm().getEntidades());
 // Testing throwing exception when file does not exist
 obj = new Abrir(new ProcessarEntidades(), "nonexistentfile.jaw.xml");
 assertNull(obj.getForm().getEntidades());
 }

 @Test
 public void testAbrirSerializado() {
 // Testing normal scenario
 obj = new Abrir(new ProcessarEntidades(), serialFile);
 assertNotNull(obj.getForm().getEntidades());
 // Testing throwing exception when file is not jaw
 obj = new Abrir(new ProcessarEntidades(), "test.txt");
 assertNull(obj.getForm().getEntidades());
 // Testing throwing exception when file does not exist
 obj = new Abrir(new ProcessarEntidades(), "nonexistentfile.jaw");
 assertNull(obj.getForm().getEntidades());
 }

 @Test
 public void testTipoAbrir() throws IOException {
 // Testing opening xml file using JFileChooser
 File xmlFile = new File("test.jaw.xml");
 Abrir obj = new Abrir(new ProcessarEntidades(), Abrir.ABRIR_XML);
 FileChooserUtils.selectFile(obj.getForm(), xmlFile);
 assertNotNull(obj.getForm().getEntidades());
 // Testing opening serial file using JFileChooser
 File serialFile = new File("test.jaw");
 obj = new Abrir(new ProcessarEntidades(), Abrir.ABRIR);
 FileChooserUtils.selectFile(obj.getForm(), serialFile);
 assertNotNull(obj.getForm().getEntidades());
 }
}