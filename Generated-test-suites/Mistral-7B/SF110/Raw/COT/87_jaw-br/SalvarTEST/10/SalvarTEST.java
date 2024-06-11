package jaw.entrada.test;

import static org.junit.Assert.*;
import jaw.entrada.*;
import java.io.*;
import java.util.*;

public class SalvarTest {

 private Salvar salvar;
 private ProcessarEntidades form;
 private List<Entidade> entidades;

 @Before
 public void setUp() {
     form = new ProcessarEntidades();
     entidades = new ArrayList<>();
     salvar = new Salvar(form, Salvar.SALVAR);
 }

 @Test
 public void testSalvarMethods() {
     // Test constructor with valid arguments
     assertNotNull(salvar);
     assertEquals(false, salvar.vela);
     assertNull(salvar.arquivo);
     assertNotNull(salvar.xml);
     assertNotNull(salvar.entidade);
     assertFalse(salvar.entidade.isEmpty());

     // Test constructor with null form and invalid tipoSalvar
     try {
         new Salvar(null, -1);
         fail("Expected IllegalArgumentException");
     } catch (IllegalArgumentException ex) {
         // Expected
     }

     // Test save method with valid file
     File file = new File("test.jaw");
     salvar.arquivo = file.getAbsolutePath();
     salvar.vela = true;
     salvar.save();
     assertTrue(file.exists());

     // Test save method with invalid file
     file = new File("test.txt");
     salvar.arquivo = file.getAbsolutePath();
     salvar.vela = true;
     salvar.save();
     assertFalse(file.exists());

     // Test save method with empty entidade list
     salvar.entidade.clear();
     salvar.save();
     assertFalse(file.exists());

     // Test saveXML method with valid file
     File xmlFile = new File("test.jaw.xml");
     salvar.arquivo = xmlFile.getAbsolutePath();
     salvar.xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<entidades>\n<entidade>\n<nome>Test</nome>\n<campo>Test</campo>\n</entidade>\n</entidades>";
     salvar.saveXML();
     assertTrue(xmlFile.exists());
     String xmlContent = readFile(xmlFile);
     assertEquals(salvar.xml, xmlContent);

     // Test saveXML method with empty entidade list
     salvar.entidade.clear();
     salvar.saveXML();
     assertFalse(xmlFile.exists());
 }

 @After
 public void tearDown() {
     for (File file : new File(System.getProperty("user.dir")).listFiles()) {
         if (file.getName().matches("test.*")) {
             file.delete();
         }
     }
 }

 private String readFile(File file) {
     StringBuilder content = new StringBuilder();
     try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
         String line;
         while ((line = reader.readLine()) != null) {
             content.append(line);
         }
     } catch (IOException e) {
         e.printStackTrace();
     }
     return content.toString();
 }
}