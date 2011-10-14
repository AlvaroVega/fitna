/*
 * Copyright 2011 Telefonica Investigación y Desarrollo, S.A.U
 * This file is part of FitNA
 *
 * FitNA is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Affero General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 *
 * FitNA is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Affero General Public License along
 * with Foobar. If not, see http://www.gnu.org/licenses/.
 *
 * For those usages not covered by the GNU Affero General Public License please
 * contact with: [zorzano@tid.es, iredondo@tid.es, avega@tid.es]
 */

package es.tid.litt.na;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Properties;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import es.tid.litt.na.NAGrupo.Acceso;

public class FitnesseNA {  
  private NADescriptor descriptor;
  private ArrayList<ElementoFitnesseEntrada> camposEntrada;
  private ArrayList<ElementoFitnesseSalida> camposSalida;
  private Properties props;
  
  public FitnesseNA () {
    this.descriptor = new NADescriptor();
    this.camposEntrada = new ArrayList<ElementoFitnesseEntrada>();
    this.camposSalida = new ArrayList<ElementoFitnesseSalida>();
    this.props = new Properties();    
  }

  public FitnesseNA(String rootPath, String filePath) {
    try {
      SAXParserFactory spf = SAXParserFactory.newInstance();
      SAXParser sp = spf.newSAXParser();
      DnaXMLReader xmlReader = new DnaXMLReader();
      
      File dnaFile = new File(filePath);
      sp.parse(dnaFile, xmlReader);
        
      NADescriptor resultado = xmlReader.getResult();
      String nombreDescriptor = dnaFile.getName();
      resultado.setNombre(nombreDescriptor.substring(0, nombreDescriptor.length()-4));
      
      this.descriptor = resultado;
      this.camposEntrada = new ArrayList<ElementoFitnesseEntrada>();
      this.camposSalida = new ArrayList<ElementoFitnesseSalida>();
      
      obtenerCamposEntrada();
      obtenerCamposSalida();
      
      this.props = new Properties();
      try {
        //props.load(new FileReader("E:\\wsBDDNA\\fitnesse_git_src\\src\\es\\tid\\litt\\na\\fitnesseNA.properties"));
        props.load(new FileReader(rootPath + "/conf" + System.getProperty("file.separator") + "fitnesseNA.properties"));
      } catch (FileNotFoundException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }

    } catch (Exception e){
        throw new RuntimeException(e);
    } 
  }
  
  // Método para acceder a atributo camposEntrada
  public ArrayList<ElementoFitnesseEntrada> getCamposEntrada () {
    return this.camposEntrada;
  }
  
  // Método para establecer atributo camposEntrada
  public void setCamposEntrada (ArrayList<ElementoFitnesseEntrada> camposEntrada) {
    this.camposEntrada = camposEntrada;
  }

  // Método para acceder a atributo camposSalida
  public ArrayList<ElementoFitnesseSalida> getCamposSalida () {
    return this.camposSalida;
  }
  
  // Método para establecer atributo camposSalida
  public void setCamposSalida (ArrayList<ElementoFitnesseSalida> camposSalida) {
    this.camposSalida = camposSalida;
  }

  // Método para acceder a atributo descriptor
  public NADescriptor getDescriptor () {
    return this.descriptor;
  }

  // Método para establecer atributo descriptor
  public void setDescriptor (NADescriptor descriptor) {
    this.descriptor = descriptor;
  }
  
  // Método para acceder a atributo props
  public Properties getProps () {
    return this.props;
  }
  
  // Método para establecer atributo props
  public void setProps (Properties props) {
    this.props = props;
  }

  // Método auxiliar que, dado un String, devuelve ese mismo String con la primera letra en mayúscula
  public String toUpperCaseFirstLetter (String word) {
    String firstLetter = word.substring(0, 1);
    return word.replaceFirst(firstLetter, firstLetter.toUpperCase());
  }
  
  // Método auxiliar que, para un tipo de datos contenido en un DNA, devuelve el tipo Java asociado
  public String convertirTipoDnaToJava (String tipoDna) {
    //TODO comprobar si la conversión es correcta
    if (tipoDna.equals("1") || tipoDna.equals("PIC_A") || tipoDna.equals("RFC_CHAR") ||
      tipoDna.equals("2") || tipoDna.equals("PIC_X")) {
      return "String";
    } else if (tipoDna.equals("3") || tipoDna.equals("PIC_9") || tipoDna.equals("RFC_NUM") ||
           tipoDna.equals("4") || tipoDna.equals("PIC_S9")) {
      return "long";
    } else if (tipoDna.equals("5") || tipoDna.equals("PIC_V9") || tipoDna.equals("RFC_BCD") ||
           tipoDna.equals("6") || tipoDna.equals("PIC_S9V9") || tipoDna.equals("PIC_9E")) {
      return "double";
    } else if (tipoDna.equals("RFC_INT")) {
      return "int";
    } else if (tipoDna.equals("RFC_DATE") || tipoDna.equals("RFC_TIME")) {
      return "String";
    } else {
      return "String";
    }
  }
  
  // Método que guarda en el array camposEntrada el campo correspondiente concatenándole el grupo padre de varias formas diferentes
  public void anotarCampoEntrada (NACampo campo, String grupoPadrePunto, String grupoPadreMayuscula) {
    String campoUnderscore = campo.getNombre().replace('-', '_');
    ElementoFitnesseEntrada elto = new ElementoFitnesseEntrada("", "", convertirTipoDnaToJava(campo.getTipo()), campo.getNombre());
    if (grupoPadrePunto == null) {
      elto.setNombrePunto(campoUnderscore);
      elto.setNombreMayuscula(campoUnderscore);
    } else {
      elto.setNombrePunto(grupoPadrePunto + "." + campoUnderscore);
      elto.setNombreMayuscula(grupoPadreMayuscula + toUpperCaseFirstLetter(campoUnderscore));
    }
    camposEntrada.add(elto);
  }

  // Método que guarda en el array camposEntrada los campos y grupos hijos del grupo pasado como parámetro
  public void anotarGrupoEntrada (NAGrupo grupo, String grupoPadrePunto, String grupoPadreMayuscula) {
    String grupoUnderscore = grupo.getNombre().replace('-', '_');
    for (int i=0; i<grupo.getListaCampos().size(); i++) {
      if (grupoPadrePunto == null) {
        anotarCampoEntrada(grupo.getListaCampos().get(i), grupoUnderscore, grupoUnderscore);
      } else {
        anotarCampoEntrada(grupo.getListaCampos().get(i), grupoPadrePunto + "." + grupoUnderscore, grupoPadreMayuscula + toUpperCaseFirstLetter(grupoUnderscore));
      }
    }
    
    for (int i=0; i<grupo.getListaGrupos().size(); i++) {
      if (grupoPadrePunto == null) {
        anotarGrupoEntrada(grupo.getListaGrupos().get(i), grupoUnderscore, grupoUnderscore);
      } else {
        anotarGrupoEntrada(grupo.getListaGrupos().get(i),grupoPadrePunto + "." + grupoUnderscore, grupoPadreMayuscula + toUpperCaseFirstLetter(grupoUnderscore));
      }     
    }
  }

  // Método que, dado un descriptor NA (la estructura ya parseada), lo recorre para generar el array camposEntrada con todos los campos de 
  // entrada formateados tal y como se necesitarán en la wiki y fixtures de Fitnesse
  public void obtenerCamposEntrada () {
    NACampo campoAux;
    for (int i=0; i<descriptor.getListaCampos().size(); i++){
      campoAux = descriptor.getListaCampos().get(i);
      if ((campoAux.getAcceso() != null) && (campoAux.getAcceso() == Acceso.E)){
        anotarCampoEntrada(descriptor.getListaCampos().get(i), null, null);
      }
    }
    
    NAGrupo grupoAux;
    for (int i=0; i<descriptor.getListaGrupos().size(); i++){
      grupoAux = descriptor.getListaGrupos().get(i);
      if ((grupoAux.getAcceso() != null) && (grupoAux.getAcceso() == Acceso.E)){
        for (int j=0; j<grupoAux.getListaCampos().size(); j++) {
          anotarCampoEntrada(grupoAux.getListaCampos().get(j), null, null);
        }
        for (int j=0; j<grupoAux.getListaGrupos().size(); j++) {
          anotarGrupoEntrada(grupoAux.getListaGrupos().get(j), null, null);
        }
      }
    }
  }
  
  // Método que guarda en el array camposSalida el campo correspondiente concatenándole el grupo padre de varias formas diferentes
  public void anotarCampoSalida (NACampo campo, String grupoPadrePunto, String grupoPadreMayuscula, String grupoPadreUnderscore) {
    String campoUnderscore = campo.getNombre().replace('-', '_');
    ElementoFitnesseSalida elto = new ElementoFitnesseSalida("", "", "", convertirTipoDnaToJava(campo.getTipo()), campo.getNombre());
    if (grupoPadrePunto == null) {
      elto.setNombrePunto(campoUnderscore);
      elto.setNombreMayuscula(campoUnderscore);
      elto.setNombreUnderscore(campoUnderscore);
    } else {
      elto.setNombrePunto(grupoPadrePunto + "." + campoUnderscore);
      elto.setNombreMayuscula(grupoPadreMayuscula + toUpperCaseFirstLetter(campoUnderscore));
      elto.setNombreUnderscore(grupoPadreUnderscore + "_" + campoUnderscore);
    }
    camposSalida.add(elto);
  }
  
  // Método que guarda en el array camposSalida los campos y grupos hijos del grupo pasado como parámetro
  public void anotarGrupoSalida (NAGrupo grupo, String grupoPadrePunto, String grupoPadreMayuscula, String grupoPadreUnderscore) {
    String grupoUnderscore = grupo.getNombre().replace('-', '_');
    for (int i=0; i<grupo.getListaCampos().size(); i++) {
      if (grupoPadrePunto == null) {
        anotarCampoSalida(grupo.getListaCampos().get(i), grupoUnderscore, grupoUnderscore, grupoUnderscore);
      } else {
        anotarCampoSalida(grupo.getListaCampos().get(i), grupoPadrePunto + "." + grupoUnderscore, grupoPadreMayuscula + toUpperCaseFirstLetter(grupoUnderscore), grupoPadreUnderscore + "_" + grupoUnderscore);
      }
    }
    
    for (int i=0; i<grupo.getListaGrupos().size(); i++) {
      if (grupoPadrePunto == null) {
        anotarGrupoSalida(grupo.getListaGrupos().get(i), grupoUnderscore, grupoUnderscore, grupoUnderscore);
      } else {
        anotarGrupoSalida(grupo.getListaGrupos().get(i),grupoPadrePunto + "." + grupoUnderscore, grupoPadreMayuscula + toUpperCaseFirstLetter(grupoUnderscore), grupoPadreUnderscore + "_" + grupoUnderscore);
      }     
    }
  }

  // Método que, dado un descriptor NA (la estructura ya parseada), lo recorre para generar el array camposSalida con todos los campos de 
  // salida formateados tal y como se necesitarán en la wiki y fixtures de Fitnesse
  public void obtenerCamposSalida () {
    NACampo campoAux;
    for (int i=0; i<descriptor.getListaCampos().size(); i++){
      campoAux = descriptor.getListaCampos().get(i);
      if ((campoAux.getAcceso() != null) && (campoAux.getAcceso() == Acceso.S)){
        anotarCampoSalida(descriptor.getListaCampos().get(i), null, null, null);
      }
    }
    
    NAGrupo grupoAux;
    for (int i=0; i<descriptor.getListaGrupos().size(); i++){
      grupoAux = descriptor.getListaGrupos().get(i);
      if ((grupoAux.getAcceso() != null) && (grupoAux.getAcceso() == Acceso.S)){
        for (int j=0; j<grupoAux.getListaCampos().size(); j++) {
          anotarCampoSalida(grupoAux.getListaCampos().get(j), null, null, null);
        }
        for (int j=0; j<grupoAux.getListaGrupos().size(); j++) {
          anotarGrupoSalida(grupoAux.getListaGrupos().get(j), null, null, null);
        }
      }
    }
  }

  // Este es uno de los dos métodos principales de la clase, que genera la página de wiki correspondiente al fichero descriptor que 
  // se haya indicado al llamar al constructor de la clase.
  // Recibe además como parámetros el sistema de pruebas a usar (slim o fit) y el path donde se ubicarán las clases que ejecutarán la prueba
  public String generateWikiPage (String testSystem, String path) {
    String content = MessageFormat.format(props.getProperty("cabeceraWiki"), testSystem, path,
                                          descriptor.getNombre().substring(0,2).toLowerCase(), descriptor.getNombre());
    
    //Concatenar campos de entrada
    for (int i=0; i<camposEntrada.size(); i++) {
      content = content.concat(camposEntrada.get(i).getNombrePunto() + props.getProperty("separadorWiki")); // TODO rellenar con blancos hasta longitud campo
    }

    //Concatenar campos de salida
    for (int i=0; i<camposSalida.size(); i++) {
      content = content.concat(camposSalida.get(i).getNombrePunto() + props.getProperty("idSalidaWiki") + props.getProperty("separadorWiki")); // TODO rellenar con blancos hasta longitud campo
    }   
    content = content.concat("\n");
    
    // Metemos una fila vacía en la tabla donde se meterá la primera prueba
    int numeroCeldas = camposEntrada.size() + camposSalida.size();
    for (int i=0; i<numeroCeldas; i++) {
      content = content.concat(props.getProperty("separadorWiki"));
    }
    content = content.concat(props.getProperty("separadorWiki") + "\n");
    return content;
  }
  
  // Este es uno de los dos métodos principales de la clase, que genera el código para la prueba correspondiente al fichero descriptor que 
  // se haya indicado al llamar al constructor de la clase.
  public String generateFixtureCode () {
    ElementoFitnesseEntrada eltoEntrada;
    ElementoFitnesseSalida eltoSalida;
    
    String content = MessageFormat.format(props.getProperty("cabeceraFixture"), descriptor.getNombre().substring(0,2).toLowerCase());
    content = content.concat(MessageFormat.format(props.getProperty("declaracionClase"), descriptor.getNombre()));
    content = content.concat(props.getProperty("atributosClase"));
    
    // Generar para los parámetros de salida, la declaración de los arrays que almacenarán los resultados de las pruebas
    // Guardamos también en un String la inicialización de estos arrays para ponerla después en el constructor
    String inicializacionArrays = "";
    for (int i=0; i<camposSalida.size(); i++){
      eltoSalida = camposSalida.get(i);
      content = content.concat(MessageFormat.format(props.getProperty("declaracionArrayResultados"), eltoSalida.getNombreUnderscore()));
      inicializacionArrays = inicializacionArrays.concat(MessageFormat.format(props.getProperty("inicializacionArrayResultados"), eltoSalida.getNombreUnderscore()));
    }
    content = content.concat("\n");
 
    // Generamos el constructor de la clase de pruebas
    content = content.concat(MessageFormat.format(props.getProperty("constructorClase"), descriptor.getNombre(), inicializacionArrays));
    
    // Generamos los métodos set para campos de entrada
    for (int i=0; i<camposEntrada.size(); i++){
      eltoEntrada = camposEntrada.get(i);
      content = content.concat(MessageFormat.format(props.getProperty("metodoSet"), toUpperCaseFirstLetter(eltoEntrada.getNombreMayuscula()),
                               eltoEntrada.getNombreMayuscula(), eltoEntrada.getCampoOriginal()));
    }

    // Generamos los métodos que obtendrán el resultado para los campos de salida
    String tipo= "", funcConvertirTipo = "", metodo = "";
    for (int i=0; i<camposSalida.size(); i++){
      eltoSalida = camposSalida.get(i);
      tipo = eltoSalida.getTipo();
      if (tipo.equals("String")) {
        funcConvertirTipo = "toString().trim()";
      } else if (tipo.equals("int")) {
        funcConvertirTipo = "intValue()";
      } else if (tipo.equals("long")) {
        funcConvertirTipo = "longValue()";
      } else if (tipo.equals("double")) {
        funcConvertirTipo = "doubleValue()";
      }
      if (i == camposSalida.size()-1) {
        metodo = "metodoResultadoUltimo";
      } else {
        metodo = "metodoResultado";
      }
      content = content.concat(MessageFormat.format(props.getProperty(metodo), tipo, eltoSalida.getNombreMayuscula(), 
                               eltoSalida.getNombreUnderscore(), eltoSalida.getCampoOriginal(), funcConvertirTipo));
    }

    content = content.concat("}");
    return content;
  }

  // Método main para probar los métodos principales de esta clase
  public static void main(String[] args) {
    try {
      String rootPath = System.getProperty("user.dir") + System.getProperty("file.separator") + "FitnesseRoot";
      /*          FitnesseNA fitnesseNA = new FitnesseNA("E:\\046_Lean_IT\\BDD-NA\\Fitnesse\\pruebas\\NAF0001.xml");
      FitnesseNA fitnesseNA = new FitnesseNA(rootPath, "E:\\046_Lean_IT\\BDD-NA\\Fitnesse\\pruebas\\NAF0002.xml");
      FitnesseNA fitnesseNA = new FitnesseNA(rootPath, "E:\\046_Lean_IT\\BDD-NA\\Fitnesse\\pruebas\\NAF0002C.xml");
      FitnesseNA fitnesseNA = new FitnesseNA(rootPath, "E:\\046_Lean_IT\\BDD-NA\\Fitnesse\\pruebas\\NAF0003.xml");
      FitnesseNA fitnesseNA = new FitnesseNA(rootPath, "E:\\046_Lean_IT\\BDD-NA\\Fitnesse\\pruebas\\NAF0003C.xml");
      FitnesseNA fitnesseNA = new FitnesseNA(rootPath, "E:\\046_Lean_IT\\BDD-NA\\Fitnesse\\pruebas\\NAF0004.xml");
      FitnesseNA fitnesseNA = new FitnesseNA(rootPath, "E:\\046_Lean_IT\\BDD-NA\\Fitnesse\\pruebas\\NAF0005.xml");
      FitnesseNA fitnesseNA = new FitnesseNA(rootPath, "E:\\046_Lean_IT\\BDD-NA\\Fitnesse\\pruebas\\NAF0006.xml");
      FitnesseNA fitnesseNA = new FitnesseNA(rootPath, "E:\\046_Lean_IT\\BDD-NA\\Fitnesse\\pruebas\\NAF0007.xml");
      FitnesseNA fitnesseNA = new FitnesseNA(rootPath, "E:\\046_Lean_IT\\BDD-NA\\Fitnesse\\pruebas\\NAF0008.xml");          
      FitnesseNA fitnesseNA = new FitnesseNA(rootPath, "E:\\046_Lean_IT\\BDD-NA\\Fitnesse\\pruebas\\NAF0009.xml");
      FitnesseNA fitnesseNA = new FitnesseNA(rootPath, "E:\\046_Lean_IT\\BDD-NA\\Fitnesse\\pruebas\\NAF0010.xml");
      FitnesseNA fitnesseNA = new FitnesseNA(rootPath, "E:\\046_Lean_IT\\BDD-NA\\Fitnesse\\pruebas\\NAF0010C.xml");
      FitnesseNA fitnesseNA = new FitnesseNA(rootPath, "E:\\046_Lean_IT\\BDD-NA\\Fitnesse\\pruebas\\NAF0011.xml");
      FitnesseNA fitnesseNA = new FitnesseNA(rootPath, "E:\\046_Lean_IT\\BDD-NA\\Fitnesse\\pruebas\\NAF0012.xml");
      FitnesseNA fitnesseNA = new FitnesseNA(rootPath, "E:\\046_Lean_IT\\BDD-NA\\Fitnesse\\pruebas\\NAF0013.xml");
      FitnesseNA fitnesseNA = new FitnesseNA(rootPath, "E:\\046_Lean_IT\\BDD-NA\\Fitnesse\\pruebas\\NAPRUEBA.xml");
      FitnesseNA fitnesseNA = new FitnesseNA(rootPath, "E:\\046_Lean_IT\\BDD-NA\\Fitnesse\\pruebas\\PLB0001.xml");
      FitnesseNA fitnesseNA = new FitnesseNA(rootPath, "E:\\046_Lean_IT\\BDD-NA\\Fitnesse\\pruebas\\QEF0045.xml");*/
      FitnesseNA fitnesseNA = new FitnesseNA(rootPath, "E:\\046_Lean_IT\\BDD-NA\\Fitnesse\\pruebas\\PLB0002.xml");
      //FitnesseNA fitnesseNA = new FitnesseNA(rootPath, "E:\\046_Lean_IT\\BDD-NA\\Fitnesse\\pruebas\\PLB0003.xml");
             
      String result = fitnesseNA.generateWikiPage("slim", "D:\\Fixtures");
                
      System.out.println("Página wiki:\n" +  result);
          
      result = fitnesseNA.generateFixtureCode();
          
      System.out.println("Fixture code:\n" +  result);

    } catch (Exception e){
      throw new RuntimeException(e);
    } 
  }

}
