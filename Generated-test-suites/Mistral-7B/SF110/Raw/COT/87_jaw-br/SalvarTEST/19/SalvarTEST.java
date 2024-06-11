package jaw.entrada.test;

import static org.junit.Assert.*;
import jaw.entrada.Salvar;
import jaw.metamodelo.Entidade;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SalvarTest {

 private Salvar salvar;
 private List<Entidade> entidades;

 @Before
 public void setUp() {
 entidades = new ArrayList<>();
 salvar = new Salvar(null, Salvar.SALVAR);
 }

 @Test
 public void testSalvar() {
 // Testing basic functionality
 assertNull(salvar.getXml());
 assertNull(salvar.getArquivo());
 assertNotNull(salvar.getEntidade());
 salvar.salvar(entidades, new File("test.jaw"));
 assertFalse(salvar.isVela());
 assertTrue(salvar.getArquivo().endsWith(".jaw"));
 assertTrue(new File(salvar.getArquivo()).exists());
 // Testing edge cases and exceptions
 try {
 salvar.salvar(null, new File("test.jaw"));
 fail("expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected exception
 }
 try {
 salvar.salvar(entidades, null);
 fail("expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected exception
 }
 try {
 salvar.salvar(entidades, new File("test.jaw.xml"));
 fail("expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected exception
 }
 try {
 salvar.salvar(entidades, new File("test.xml"));
 fail("expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected exception
 }
 try {
 salvar.salvar(new ArrayList<>(), new File("test.jaw"));
 fail("expected UnsupportedOperationException");
 } catch (UnsupportedOperationException e) {
 // Expected exception
 }
 try {
 salvar.salvar(entidades, new File("test.jaw"));
 salvar.salvar(entidades, new File("test2.jaw"));
 fail("expected IllegalStateException");
 } catch (IllegalStateException e) {
 // Expected exception
 }
 try {
 salvar.salvar(entidades, new File("test.jaw"));
 Thread.sleep(100);
 salvar.salvar(entidades, new File("test.jaw"));
 fail("expected FileAlreadyExistsException");
 } catch (FileAlreadyExistsException e) {
 // Expected exception
 }
 try {
 salvar.salvar(entidades, new File("test.jaw"));
 salvar.salvar(entidades, new File("test.jaw"));
 salvar.salvar(entidades, new File("test.jaw"));
 salvar.salvar(entidades, new File("test.jaw"));
 salvar.salvar(entidades, new File("test.jaw"));
 salvar.salvar(entidades, new File("test.jaw"));
 fail("expected IOException");
 } catch (IOException e) {
 // Expected exception
 }
 try {
 salvar.salvar(entidades, new File("test.jaw"));
 salvar.setArquivo("");
 fail("expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected exception
 }
 try {
 salvar.salvar(entidades, new File("test.jaw"));
 salvar.setEntidade(null);
 fail("expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected exception
 }
 try {
 salvar.salvar(entidades, new File("test.jaw"));
 salvar.setVela(true);
 salvar.setArquivo("");
 fail("expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected exception
 }
 try {
 salvar.salvar(entidades, new File("test.jaw"));
 salvar.setVela(true);
 salvar.setArquivo("test.jaw");
 salvar.setArquivo("test2.jaw");
 fail("expected IllegalStateException");
 } catch (IllegalStateException e) {
 // Expected exception
 }
 try {
 salvar.salvar(entidades, new File("test.jaw"));
 salvar.setVela(true);
 salvar.setArquivo("test.jaw");
 salvar.setArquivo("test.jaw");
 fail("expected IllegalStateException");
 } catch (IllegalStateException e) {
 // Expected exception
 }
 try {
 salvar.salvar(entidades, new File("test.jaw"));
 salvar.setVela(true);
 salvar.setArquivo("test.jaw");
 salvar.setEntidade(null);
 fail("expected UnsupportedOperationException");
 } catch (UnsupportedOperationException e) {
 // Expected exception
 }
 }

 @After
 public void tearDown() {
 for (Entidade e : entidades) {
 e.setId(0);
 }
 if (salvar != null) {
 salvar.setEntidade(null);
 salvar.setVela(false);
 salvar = null;
 }
 for (File f : new File(Main.configuracao.getCaminhoDosProjetos()).listFiles()) {
 if (f.getName().startsWith("test")) {
 f.delete();
 }
 }
 }
}