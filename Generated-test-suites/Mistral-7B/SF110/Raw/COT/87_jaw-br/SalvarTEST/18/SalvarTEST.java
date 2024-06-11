package jaw.entrada.test;

import static org.junit.Assert.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import jaw.entrada.Entidade;
import jaw.entrada.ProcessarEntidades;
import jaw.entrada.Salvar;
import jaw.metamodelo.MetamodeloPackage;
import jaw.gui.Main;

public class SalvarTest {

 private ProcessarEntidades form;
 private Salvar salvar;
 private List<Entidade> entidades;

 @Before
 public void setup() {
 form = new ProcessarEntidades();
 salvar = new Salvar(form, Salvar.SALVAR);
 entidades = new ArrayList<>();
 entidades.add(new Entidade("Teste1", MetamodeloPackage.ENTIDADE));
 entidades.add(new Entidade("Teste2", MetamodeloPackage.ENTIDADE));
 }

 @Test
 public void testSalvarWithValidParameters() {
 salvar.setEntidades(entidades);
 salvar.salvar(entidades, "teste.jaw");
 assertTrue(new File("teste.jaw").exists());
 }

 @Test(expected = NullPointerException.class)
 public void testSalvarWithNullEntidades() {
 salvar = new Salvar(form, Salvar.SALVAR);
 salvar.setEntidades(null);
 salvar.salvar(null, "teste.jaw");
 }

 @Test(expected = IOException.class)
 public void testSalvarWithIOException() throws IOException {
 File file = new File("teste.jaw");
 Salvar salvar = new Salvar(form, Salvar.SALVAR);
 salvar.setEntidades(entidades);
 FileWriter writer = new FileWriter(file);
 writer.write("teste");
 salvar.salvar(entidades, file.getAbsolutePath());
 writer.close();
 }

 @Test
 public void testSalvarXMLWithValidParameters() {
 salvar.setEntidades(entidades);
 salvar.xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<entidades>\n</entidades>";
 salvar.salvarXML(entidades, "teste.jaw.xml");
 assertTrue(new File("teste.jaw.xml").exists());
 }

 @Test(expected = NullPointerException.class)
 public void testSalvarXMLWithNullEntidades() {
 salvar = new Salvar(form, Salvar.SALVAR_XML);
 salvar.xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<entidades>\n</entidades>";
 salvar.setEntidades(null);
 salvar.salvarXML(null, "teste.jaw.xml");
 }

 @Test(expected = IOException.class)
 public void testSalvarXMLWithIOException() throws IOException {
 File file = new File("teste.jaw.xml");
 Salvar salvar = new Salvar(form, Salvar.SALVAR_XML);
 salvar.xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<entidades>\n</entidades>";
 FileWriter writer = new FileWriter(file);
 writer.write("teste");
 salvar.salvarXML(entidades, file.getAbsolutePath());
 writer.close();
 }

 @Test
 public void testSaveWithEmptyFileName() {
 salvar = new Salvar(form, Salvar.SALVAR);
 salvar.setEntidades(entidades);
 salvar.arquivo = "";
 salvar.save();
 assertFalse(new File("").exists());
 }

 @Test
 public void testSaveWithInvalidFileExtension() {
 salvar = new Salvar(form, Salvar.SALVAR);
 salvar.setEntidades(entidades);
 salvar.arquivo = "teste.xml";
 salvar.save();
 assertFalse(new File("teste.xml").exists());
 }

 @Test
 public void testSaveWithVelaFalseAndEmptyFileName() {
 salvar = new Salvar(form, Salvar.SALVAR);
 salvar.setEntidades(entidades);
 salvar.arquivo = "";
 salvar.vela = false;
 salver.save();
 assertFalse(new File("").exists());
 }

 @Test
 public void testSaveWithVelaFalseAndValidFileName() {
 salvar = new Salvar(form, Salvar.SALVAR);
 salvar.setEntidades(entidades);
 salvar.arquivo = "teste.jaw";
 salvar.vela = false;
 salvar.save();
 assertFalse(new File("teste.jaw").exists());
 }

 @Test
 public void testSaveWithVelaTrueAndEmptyFileName() {
 salvar = new Salvar(form, Salvar.SALVAR);
 salvar.setEntidades(entidades);
 salvar.vela = true;
 salvar.save();
 assertFalse(new File("").exists());
 }

 @Test
 public void testSaveWithVelaTrueAndInvalidFileName() {
 salvar = new Salvar(form, Salvar.SALVAR);
 salvar.setEntidades(entidades);
 salvar.arquivo = "teste.xml";
 salvar.vela = true;
 salvar.save();
 assertFalse(new File("teste.xml").exists());
 }

 @Test
 public void testSaveWithVelaTrueAndValidFileName() {
 salvar = new Salvar(form, Salvar.SALVAR);
 salvar.setEntidades(entidades);
 salvar.arquivo = "teste.jaw";
 salvar.vela = true;
 salvar.save();
 assertTrue(new File("teste.jaw").exists());
 }
}