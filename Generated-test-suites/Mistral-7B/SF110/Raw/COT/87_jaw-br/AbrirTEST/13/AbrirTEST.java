package jaw.entrada;
import static org.junit.Assert.*;
import jaw.entrada.Abrir;
import java.io.File;
import java.io.IOException;

public class AbrirTest {

 private Abrir abrir;
 private ProcessarEntidades form;

 @Before
 public void setUp() {
 form = new ProcessarEntidades();
 abrir = new Abrir(form, null);
 }

 @Test
 public void testAbrirNoArgumentsConstructor() {
 assertEquals("", abrir.xml);
 assertNull(abrir.form.getEntidades());
 }

 @Test
 public void testAbrirXMLConstructorWithValidXmlFile() {
 String xmlFilePath = "test_data/valid_xml_file.jaw.xml";
 abrir = new Abrir(form, xmlFilePath);
 assertTrue(abrir.xml.length() > 0);
 assertNotNull(abrir.form.getEntidades());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAbrirXMLConstructorWithInvalidXmlFile() {
 String xmlFilePath = "test_data/invalid_xml_file.txt";
 abrir = new Abrir(form, xmlFilePath);
 fail("An IllegalArgumentException should be thrown.");
 }

 @Test
 public void testAbrirSerializadoConstructorWithValidJawFile() {
 String jawFilePath = "test_data/valid_jaw_file.jaw";
 abrir = new Abrir(form, Abrir.ABRIR);
 File file = new File(jawFilePath);
 assertTrue(file.exists());
 abrir.abrirSerializado(jawFilePath);
 assertNotNull(abrir.form.getEntidades());
 }

 @Test
 public void testAbrirSerializadoConstructorWithInvalidJawFile() {
 String jawFilePath = "test_data/invalid_jaw_file.txt";
 abrir = new Abrir(form, Abrir.ABRIR);
 File file = new File(jawFilePath);
 assertFalse(file.exists());
 abrir.abrirSerializado(jawFilePath);
 assertNull(abrir.form.getEntidades());
 }

 @Test
 public void testAbrirXMLFileDialog() {
 abrir = new Abrir(form, Abrir.ABRIR_XML);
 JFileChooser fileChooser = mock(JFileChooser.class);
 when(fileChooser.showOpenDialog(anyInt())).thenReturn(JFileChooser.APPROVE_OPTION);
 when(fileChooser.getSelectedFile()).thenReturn(new File("test_data/valid_xml_file.jaw.xml"));
 ReflectionTestUtils.setFieldValue(abrir, "escolhedor", fileChooser);
 abrir.abrir();
 assertTrue(abrir.xml.length() > 0);
 assertNotNull(abrir.form.getEntidades());
 }

 @Test
 public void testAbrirSerializadoFileDialog() {
 abrir = new Abrir(form, Abrir.ABRIR);
 JFileChooser fileChooser = mock(JFileChooser.class);
 when(fileChooser.showOpenDialog(anyInt())).thenReturn(JFileChooser.APPROVE_OPTION);
 when(fileChooser.getSelectedFile()).thenReturn(new File("test_data/valid_jaw_file.jaw"));
 ReflectionTestUtils.setFieldValue(abrir, "escolhedor", fileChooser);
 abrir.abrir();
 assertNotNull(abrir.form.getEntidades());
 }

 @Test(expected = NullPointerException.class)
 public void testAbrirXMLFileDialogWithNullFileChooser() {
 abrir = new Abrir(form, Abrir.ABRIR_XML);
 ReflectionTestUtils.setFieldValue(abrir, "escolhedor", null);
 abrir.abrir();
 fail("A NullPointerException should be thrown.");
 }

 @Test
 public void testAbrirXMLFileDialogWithCanceledDialog() {
 abrir = new Abrir(form, Abrir.ABRIR_XML);
 JFileChooser fileChooser = mock(JFileChooser.class);
 when(fileChooser.showOpenDialog(anyInt())).thenReturn(JFileChooser.CANCEL_OPTION);
 ReflectionTestUtils.setFieldValue(abrir, "escolhedor", fileChooser);
 abrir.abrir();
 assertNull(abrir.xml);
 assertNull(abrir.form.getEntidades());
 }

}