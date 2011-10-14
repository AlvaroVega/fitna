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

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import org.junit.Test;

import es.tid.litt.na.NAGrupo.Acceso;

public class FitnesseNATest {

  @Test
  public void testToUpperCaseFirstLetter() {
    FitnesseNA fitnesseNA = new FitnesseNA();
    // Inicialización parámetros prueba 1
    String word = "operando";
    String result = fitnesseNA.toUpperCaseFirstLetter(word);
    assertEquals("Prueba \"operando\"", "Operando", result);
    
    // Cambio parámetros para prueba 2
    word = "OPERANDO";
    result = fitnesseNA.toUpperCaseFirstLetter(word);
    assertEquals("Prueba \"OPERANDO\"", "OPERANDO", result);
  }

  @Test
  public void testConvertirTipoDnaToJava() {
    FitnesseNA fitnesseNA = new FitnesseNA();
    // Inicialización parámetros prueba 1
    String tipoDna = "2";
    String tipoJava = fitnesseNA.convertirTipoDnaToJava(tipoDna);
    assertEquals("Prueba \"tipoDNA 2\"", "String", tipoJava);
    
    // Cambio parámetros para prueba 2
    tipoDna = "PIC_9";
    tipoJava = fitnesseNA.convertirTipoDnaToJava(tipoDna);
    assertEquals("Prueba \"tipoDNA PIC-9\"", "long", tipoJava);

    // Cambio parámetros para prueba 3
    tipoDna = "5";
    tipoJava = fitnesseNA.convertirTipoDnaToJava(tipoDna);
    assertEquals("Prueba \"tipoDNA 5\"", "double", tipoJava);

    // Cambio parámetros para prueba 4
    tipoDna = "RFC_INT";
    tipoJava = fitnesseNA.convertirTipoDnaToJava(tipoDna);
    assertEquals("Prueba \"tipoDNA RFC_INT\"", "int", tipoJava);

    // Cambio parámetros para prueba 5
    tipoDna = "8";
    tipoJava = fitnesseNA.convertirTipoDnaToJava(tipoDna);
    assertEquals("Prueba \"tipoDNA otros\"", "String", tipoJava);
  }

  @Test
  public void testAnotarCampoEntrada() {
    FitnesseNA fitnesseNA = new FitnesseNA();

    // Inicialización parámetros prueba 1
    NACampo campo = new NACampo("orden", "2", "", 80, -1, null);
    fitnesseNA.anotarCampoEntrada(campo, null, null);
    ElementoFitnesseEntrada elto = fitnesseNA.getCamposEntrada().get(0);
    assertEquals("Nombre punto, prueba \"padre nulo\"", "orden", elto.getNombrePunto());
    assertEquals("Nombre mayúscula, prueba \"padre nulo\"", "orden", elto.getNombreMayuscula());
    assertEquals("Tipo, prueba \"padre nulo\"", "String", elto.getTipo());
    assertEquals("Campo original, prueba \"padre nulo\"", "orden", elto.getCampoOriginal());
    
    // Cambio parámetros para prueba 2
    campo = new NACampo("codigo", "RFC_INT", "", 80, -1, null);
    fitnesseNA.anotarCampoEntrada(campo, "orden", "orden");
    elto = fitnesseNA.getCamposEntrada().get(1);
    assertEquals("Nombre punto, prueba \"padre sencillo\"", "orden.codigo", elto.getNombrePunto());
    assertEquals("Nombre mayúscula, prueba \"padre sencillo\"", "ordenCodigo", elto.getNombreMayuscula());
    assertEquals("Tipo, prueba \"padre sencillo\"", "int", elto.getTipo());
    assertEquals("Campo original, prueba \"padre sencillo\"", "codigo", elto.getCampoOriginal());

    // Cambio parámetros para prueba 3
    campo = new NACampo("codigo", "3", "", 80, -1, null);
    fitnesseNA.anotarCampoEntrada(campo, "entrada.orden", "entradaOrden");
    elto = fitnesseNA.getCamposEntrada().get(2);
    assertEquals("Nombre punto, prueba \"padre compuesto\"", "entrada.orden.codigo", elto.getNombrePunto());
    assertEquals("Nombre mayúscula, prueba \"padre compuesto\"", "entradaOrdenCodigo", elto.getNombreMayuscula());
    assertEquals("Tipo, prueba \"padre compuesto\"", "long", elto.getTipo());
    assertEquals("Campo original, prueba \"padre compuesto\"", "codigo", elto.getCampoOriginal());
  }

  @Test
  public void testAnotarGrupoEntrada() {
    FitnesseNA fitnesseNA = new FitnesseNA();

    // Inicialización parámetros prueba 1
    ArrayList<NACampo> listaCampos = new ArrayList<NACampo>();
    listaCampos.add(new NACampo("orden", "2", "", 80, -1, null));
    listaCampos.add(new NACampo("contacto", "RFC_CHAR", "", 80, -1, null));
    NAGrupo grupo = new NAGrupo("entrada", -1, null, listaCampos, new ArrayList<NAGrupo>());
    fitnesseNA.anotarGrupoEntrada(grupo, null, null);
    ElementoFitnesseEntrada elto1 = fitnesseNA.getCamposEntrada().get(0);
    ElementoFitnesseEntrada elto2 = fitnesseNA.getCamposEntrada().get(1);
    assertEquals("Nombre punto elto1, prueba \"grupo con 2 campos\"", "entrada.orden", elto1.getNombrePunto());
    assertEquals("Nombre mayúscula elto1, prueba \"grupo con 2 campos\"", "entradaOrden", elto1.getNombreMayuscula());
    assertEquals("Tipo elto1, prueba \"grupo con 2 campos\"", "String", elto1.getTipo());
    assertEquals("Campo original elto1, prueba \"grupo con 2 campos\"", "orden", elto1.getCampoOriginal());
    assertEquals("Nombre punto elto2, prueba \"grupo con 2 campos\"", "entrada.contacto", elto2.getNombrePunto());
    assertEquals("Nombre mayúscula elto2, prueba \"grupo con 2 campos\"", "entradaContacto", elto2.getNombreMayuscula());
    assertEquals("Tipo elto2, prueba \"grupo con 2 campos\"", "String", elto2.getTipo());
    assertEquals("Campo original elto2, prueba \"grupo con 2 campos\"", "contacto", elto2.getCampoOriginal());
    
    // Cambio parámetros para prueba 2
    listaCampos = new ArrayList<NACampo>();
    listaCampos.add(new NACampo("contacto", "RFC_CHAR", "", 80, -1, null));
    listaCampos.add(new NACampo("prioridad", "RFC_INT", "", 80, -1, null));
    ArrayList<NAGrupo> listaGrupos = new ArrayList<NAGrupo>();
    ArrayList<NACampo> listaCamposGrupo = new ArrayList<NACampo>();
    listaCamposGrupo.add(new NACampo("codigo", "2", "", 80, -1, null));
    listaCamposGrupo.add(new NACampo("descripcion", "2", "", 80, -1, null));
    listaGrupos.add(new NAGrupo("orden", -1, null, listaCamposGrupo, new ArrayList<NAGrupo>()));
    grupo = new NAGrupo("entrada", -1, null, listaCampos, listaGrupos);
    fitnesseNA.anotarGrupoEntrada(grupo, null, null);
    elto1 = fitnesseNA.getCamposEntrada().get(2);
    elto2 = fitnesseNA.getCamposEntrada().get(3);
    ElementoFitnesseEntrada elto3 = fitnesseNA.getCamposEntrada().get(4);
    ElementoFitnesseEntrada elto4 = fitnesseNA.getCamposEntrada().get(5);
    assertEquals("Nombre punto elto1, prueba \"grupo con 2 campos y subgrupo con 2 campos\"", "entrada.contacto", elto1.getNombrePunto());
    assertEquals("Nombre mayúscula elto1, prueba \"grupo con 2 campos y subgrupo con 2 campos\"", "entradaContacto", elto1.getNombreMayuscula());
    assertEquals("Tipo elto1, prueba \"grupo con 2 campos y subgrupo con 2 campos\"", "String", elto1.getTipo());
    assertEquals("Campo original elto1, prueba \"grupo con 2 campos y subgrupo con 2 campos\"", "contacto", elto1.getCampoOriginal());
    assertEquals("Nombre punto elto2, prueba \"grupo con 2 campos y subgrupo con 2 campos\"", "entrada.prioridad", elto2.getNombrePunto());
    assertEquals("Nombre mayúscula elto2, prueba \"grupo con 2 campos y subgrupo con 2 campos\"", "entradaPrioridad", elto2.getNombreMayuscula());
    assertEquals("Tipo elto2, prueba \"grupo con 2 campos y subgrupo con 2 campos\"", "int", elto2.getTipo());
    assertEquals("Campo original elto2, prueba \"grupo con 2 campos y subgrupo con 2 campos\"", "prioridad", elto2.getCampoOriginal());
    assertEquals("Nombre punto elto3, prueba \"grupo con 2 campos y subgrupo con 2 campos\"", "entrada.orden.codigo", elto3.getNombrePunto());
    assertEquals("Nombre mayúscula elto3, prueba \"grupo con 2 campos y subgrupo con 2 campos\"", "entradaOrdenCodigo", elto3.getNombreMayuscula());
    assertEquals("Tipo elto3, prueba \"grupo con 2 campos y subgrupo con 2 campos\"", "String", elto3.getTipo());
    assertEquals("Campo original elto3, prueba \"grupo con 2 campos y subgrupo con 2 campos\"", "codigo", elto3.getCampoOriginal());
    assertEquals("Nombre punto elto4, prueba \"grupo con 2 campos y subgrupo con 2 campos\"", "entrada.orden.descripcion", elto4.getNombrePunto());
    assertEquals("Nombre mayúscula elto4, prueba \"grupo con 2 campos y subgrupo con 2 campos\"", "entradaOrdenDescripcion", elto4.getNombreMayuscula());
    assertEquals("Tipo elto4, prueba \"grupo con 2 campos y subgrupo con 2 campos\"", "String", elto4.getTipo());
    assertEquals("Campo original elto4, prueba \"grupo con 2 campos y subgrupo con 2 campos\"", "descripcion", elto4.getCampoOriginal());
  }

  @Test
  public void testObtenerCamposEntrada() {
    FitnesseNA fitnesseNA = new FitnesseNA();
    
    // Inicialización descriptor para prueba
    ArrayList<NACampo> listaCamposOrden = new ArrayList<NACampo>();
    listaCamposOrden.add(new NACampo("codigo", "2", "", 80, -1, null));
    listaCamposOrden.add(new NACampo("descripcion", "2", "", 80, -1, null));
    ArrayList<NACampo> listaCamposEntrada = new ArrayList<NACampo>();
    listaCamposEntrada.add(new NACampo("contacto", "RFC_CHAR", "", 80, -1, null));
    listaCamposEntrada.add(new NACampo("prioridad", "RFC_INT", "", 80, -1, null));
    ArrayList<NAGrupo> listaGruposEntrada = new ArrayList<NAGrupo>();
    listaGruposEntrada.add(new NAGrupo("orden", -1, null, listaCamposOrden, new ArrayList<NAGrupo>()));
    
    ArrayList<NACampo> listaCamposResultado = new ArrayList<NACampo>();
    listaCamposResultado.add(new NACampo("codigo", "3", "", 80, -1, null));
    listaCamposResultado.add(new NACampo("descripcion", "2", "", 80, -1, null));
    ArrayList<NAGrupo> listaGruposSalida = new ArrayList<NAGrupo>();
    listaGruposSalida.add(new NAGrupo("resultado", -1, null, listaCamposResultado, new ArrayList<NAGrupo>()));
    
    ArrayList<NAGrupo> listaGruposDescriptor = new ArrayList<NAGrupo>();
    listaGruposDescriptor.add(new NAGrupo("entrada", -1, Acceso.E, listaCamposEntrada, listaGruposEntrada));
    listaGruposDescriptor.add(new NAGrupo("salida", -1, Acceso.S, new ArrayList<NACampo>(), listaGruposSalida));
    
    fitnesseNA.setDescriptor(new NADescriptor("prueba", new ArrayList<NACampo>(), listaGruposDescriptor));
    
    // Ejecutar prueba y comprobar resultados
    fitnesseNA.obtenerCamposEntrada();
    ElementoFitnesseEntrada elto1 = fitnesseNA.getCamposEntrada().get(0);
    ElementoFitnesseEntrada elto2 = fitnesseNA.getCamposEntrada().get(1);
    ElementoFitnesseEntrada elto3 = fitnesseNA.getCamposEntrada().get(2);
    ElementoFitnesseEntrada elto4 = fitnesseNA.getCamposEntrada().get(3);
    assertEquals("Nombre punto elto1, prueba \"obtener campos entrada\"", "contacto", elto1.getNombrePunto());
    assertEquals("Nombre mayúscula elto1, prueba \"obtener campos entrada\"", "contacto", elto1.getNombreMayuscula());
    assertEquals("Tipo elto1, prueba \"obtener campos entrada\"", "String", elto1.getTipo());
    assertEquals("Campo original elto1, prueba \"obtener campos entrada\"", "contacto", elto1.getCampoOriginal());
    assertEquals("Nombre punto elto2, prueba \"obtener campos entrada\"", "prioridad", elto2.getNombrePunto());
    assertEquals("Nombre mayúscula elto2, prueba \"obtener campos entrada\"", "prioridad", elto2.getNombreMayuscula());
    assertEquals("Tipo elto2, prueba \"obtener campos entrada\"", "int", elto2.getTipo());
    assertEquals("Campo original elto2, prueba \"obtener campos entrada\"", "prioridad", elto2.getCampoOriginal());
    assertEquals("Nombre punto elto3, prueba \"obtener campos entrada\"", "orden.codigo", elto3.getNombrePunto());
    assertEquals("Nombre mayúscula elto3, prueba \"obtener campos entrada\"", "ordenCodigo", elto3.getNombreMayuscula());
    assertEquals("Tipo elto3, prueba \"obtener campos entrada\"", "String", elto3.getTipo());
    assertEquals("Campo original elto3, prueba \"obtener campos entrada\"", "codigo", elto3.getCampoOriginal());
    assertEquals("Nombre punto elto4, prueba \"obtener campos entrada\"", "orden.descripcion", elto4.getNombrePunto());
    assertEquals("Nombre mayúscula elto4, prueba \"obtener campos entrada\"", "ordenDescripcion", elto4.getNombreMayuscula());
    assertEquals("Tipo elto4, prueba \"obtener campos entrada\"", "String", elto4.getTipo());
    assertEquals("Campo original elto4, prueba \"obtener campos entrada\"", "descripcion", elto4.getCampoOriginal());
  }

  @Test
  public void testAnotarCampoSalida() {
    FitnesseNA fitnesseNA = new FitnesseNA();

    // Inicialización parámetros prueba 1
    NACampo campo = new NACampo();
    campo.setNombre("orden");
    campo.setTipo("2");
    fitnesseNA.anotarCampoSalida(campo, null, null, null);
    ElementoFitnesseSalida elto = fitnesseNA.getCamposSalida().get(0);
    assertEquals("Nombre punto, prueba \"padre nulo\"", "orden", elto.getNombrePunto());
    assertEquals("Nombre mayúscula, prueba \"padre nulo\"", "orden", elto.getNombreMayuscula());
    assertEquals("Nombre underscore, prueba \"padre nulo\"", "orden", elto.getNombreUnderscore());
    assertEquals("Tipo, prueba \"padre nulo\"", "String", elto.getTipo());
    assertEquals("Campo original, prueba \"padre nulo\"", "orden", elto.getCampoOriginal());
    
    // Cambio parámetros para prueba 2
    campo = new NACampo();
    campo.setNombre("codigo");
    campo.setTipo("RFC_INT");
    fitnesseNA.anotarCampoSalida(campo, "orden", "orden", "orden");
    elto = fitnesseNA.getCamposSalida().get(1);
    assertEquals("Nombre punto, prueba \"padre sencillo\"", "orden.codigo", elto.getNombrePunto());
    assertEquals("Nombre mayúscula, prueba \"padre sencillo\"", "ordenCodigo", elto.getNombreMayuscula());
    assertEquals("Nombre underscore, prueba \"padre sencillo\"", "orden_codigo", elto.getNombreUnderscore());
    assertEquals("Tipo, prueba \"padre sencillo\"", "int", elto.getTipo());
    assertEquals("Campo original, prueba \"padre sencillo\"", "codigo", elto.getCampoOriginal());

    // Cambio parámetros para prueba 3
    campo = new NACampo();
    campo.setNombre("codigo");
    campo.setTipo("3");
    fitnesseNA.anotarCampoSalida(campo, "salida.orden", "salidaOrden", "salida_orden");
    elto = fitnesseNA.getCamposSalida().get(2);
    assertEquals("Nombre punto, prueba \"padre compuesto\"", "salida.orden.codigo", elto.getNombrePunto());
    assertEquals("Nombre mayúscula, prueba \"padre compuesto\"", "salidaOrdenCodigo", elto.getNombreMayuscula());
    assertEquals("Nombre underscore, prueba \"padre sencillo\"", "salida_orden_codigo", elto.getNombreUnderscore());
    assertEquals("Tipo, prueba \"padre compuesto\"", "long", elto.getTipo());
    assertEquals("Campo original, prueba \"padre compuesto\"", "codigo", elto.getCampoOriginal());
  }

  @Test
  public void testAnotarGrupoSalida() {
    FitnesseNA fitnesseNA = new FitnesseNA();

    // Inicialización parámetros prueba 1
    ArrayList<NACampo> listaCampos = new ArrayList<NACampo>();
    listaCampos.add(new NACampo("orden", "2", "", 80, -1, null));
    listaCampos.add(new NACampo("contacto", "RFC_CHAR", "", 80, -1, null));
    NAGrupo grupo = new NAGrupo("salida", -1, null, listaCampos, new ArrayList<NAGrupo>());
    fitnesseNA.anotarGrupoSalida(grupo, null, null, null);
    ElementoFitnesseSalida elto1 = fitnesseNA.getCamposSalida().get(0);
    ElementoFitnesseSalida elto2 = fitnesseNA.getCamposSalida().get(1);
    assertEquals("Nombre punto elto1, prueba \"grupo con 2 campos\"", "salida.orden", elto1.getNombrePunto());
    assertEquals("Nombre mayúscula elto1, prueba \"grupo con 2 campos\"", "salidaOrden", elto1.getNombreMayuscula());
    assertEquals("Nombre underscore elto1, prueba \"grupo con 2 campos\"", "salida_orden", elto1.getNombreUnderscore());
    assertEquals("Tipo elto1, prueba \"grupo con 2 campos\"", "String", elto1.getTipo());
    assertEquals("Campo original elto1, prueba \"grupo con 2 campos\"", "orden", elto1.getCampoOriginal());
    assertEquals("Nombre punto elto2, prueba \"grupo con 2 campos\"", "salida.contacto", elto2.getNombrePunto());
    assertEquals("Nombre mayúscula elto2, prueba \"grupo con 2 campos\"", "salidaContacto", elto2.getNombreMayuscula());
    assertEquals("Nombre underscore elto2, prueba \"grupo con 2 campos\"", "salida_contacto", elto2.getNombreUnderscore());
    assertEquals("Tipo elto2, prueba \"grupo con 2 campos\"", "String", elto2.getTipo());
    assertEquals("Campo original elto2, prueba \"grupo con 2 campos\"", "contacto", elto2.getCampoOriginal());
    
    // Cambio parámetros para prueba 2
    listaCampos = new ArrayList<NACampo>();
    listaCampos.add(new NACampo("contacto", "RFC_CHAR", "", 80, -1, null));
    listaCampos.add(new NACampo("prioridad", "RFC_INT", "", 80, -1, null));
    ArrayList<NAGrupo> listaGrupos = new ArrayList<NAGrupo>();
    ArrayList<NACampo> listaCamposGrupo = new ArrayList<NACampo>();
    listaCamposGrupo.add(new NACampo("codigo", "2", "", 80, -1, null));
    listaCamposGrupo.add(new NACampo("descripcion", "2", "", 80, -1, null));
    listaGrupos.add(new NAGrupo("orden", -1, null, listaCamposGrupo, new ArrayList<NAGrupo>()));
    grupo = new NAGrupo("salida", -1, null, listaCampos, listaGrupos);
    fitnesseNA.anotarGrupoSalida(grupo, null, null, null);
    elto1 = fitnesseNA.getCamposSalida().get(2);
    elto2 = fitnesseNA.getCamposSalida().get(3);
    ElementoFitnesseSalida elto3 = fitnesseNA.getCamposSalida().get(4);
    ElementoFitnesseSalida elto4 = fitnesseNA.getCamposSalida().get(5);
    assertEquals("Nombre punto elto1, prueba \"grupo con 2 campos y subgrupo con 2 campos\"", "salida.contacto", elto1.getNombrePunto());
    assertEquals("Nombre mayúscula elto1, prueba \"grupo con 2 campos y subgrupo con 2 campos\"", "salidaContacto", elto1.getNombreMayuscula());
    assertEquals("Nombre underscore elto1, prueba \"grupo con 2 campos y subgrupo con 2 campos\"", "salida_contacto", elto1.getNombreUnderscore());
    assertEquals("Tipo elto1, prueba \"grupo con 2 campos y subgrupo con 2 campos\"", "String", elto1.getTipo());
    assertEquals("Campo original elto1, prueba \"grupo con 2 campos y subgrupo con 2 campos\"", "contacto", elto1.getCampoOriginal());
    assertEquals("Nombre punto elto2, prueba \"grupo con 2 campos y subgrupo con 2 campos\"", "salida.prioridad", elto2.getNombrePunto());
    assertEquals("Nombre mayúscula elto2, prueba \"grupo con 2 campos y subgrupo con 2 campos\"", "salidaPrioridad", elto2.getNombreMayuscula());
    assertEquals("Nombre underscore elto2, prueba \"grupo con 2 campos y subgrupo con 2 campos\"", "salida_prioridad", elto2.getNombreUnderscore());
    assertEquals("Tipo elto2, prueba \"grupo con 2 campos y subgrupo con 2 campos\"", "int", elto2.getTipo());
    assertEquals("Campo original elto2, prueba \"grupo con 2 campos y subgrupo con 2 campos\"", "prioridad", elto2.getCampoOriginal());
    assertEquals("Nombre punto elto3, prueba \"grupo con 2 campos y subgrupo con 2 campos\"", "salida.orden.codigo", elto3.getNombrePunto());
    assertEquals("Nombre mayúscula elto3, prueba \"grupo con 2 campos y subgrupo con 2 campos\"", "salidaOrdenCodigo", elto3.getNombreMayuscula());
    assertEquals("Nombre underscore elto3, prueba \"grupo con 2 campos y subgrupo con 2 campos\"", "salida_orden_codigo", elto3.getNombreUnderscore());
    assertEquals("Tipo elto3, prueba \"grupo con 2 campos y subgrupo con 2 campos\"", "String", elto3.getTipo());
    assertEquals("Campo original elto3, prueba \"grupo con 2 campos y subgrupo con 2 campos\"", "codigo", elto3.getCampoOriginal());
    assertEquals("Nombre punto elto4, prueba \"grupo con 2 campos y subgrupo con 2 campos\"", "salida.orden.descripcion", elto4.getNombrePunto());
    assertEquals("Nombre mayúscula elto4, prueba \"grupo con 2 campos y subgrupo con 2 campos\"", "salidaOrdenDescripcion", elto4.getNombreMayuscula());
    assertEquals("Nombre underscore elto4, prueba \"grupo con 2 campos y subgrupo con 2 campos\"", "salida_orden_descripcion", elto4.getNombreUnderscore());
    assertEquals("Tipo elto4, prueba \"grupo con 2 campos y subgrupo con 2 campos\"", "String", elto4.getTipo());
    assertEquals("Campo original elto4, prueba \"grupo con 2 campos y subgrupo con 2 campos\"", "descripcion", elto4.getCampoOriginal());
  }

  @Test
  public void testObtenerCamposSalida() {
    FitnesseNA fitnesseNA = new FitnesseNA();
    
    // Inicialización descriptor para prueba
    ArrayList<NACampo> listaCamposOrden = new ArrayList<NACampo>();
    listaCamposOrden.add(new NACampo("codigo", "2", "", 80, -1, null));
    listaCamposOrden.add(new NACampo("descripcion", "2", "", 80, -1, null));
    ArrayList<NACampo> listaCamposEntrada = new ArrayList<NACampo>();
    listaCamposEntrada.add(new NACampo("contacto", "RFC_CHAR", "", 80, -1, null));
    listaCamposEntrada.add(new NACampo("prioridad", "RFC_INT", "", 80, -1, null));
    ArrayList<NAGrupo> listaGruposEntrada = new ArrayList<NAGrupo>();
    listaGruposEntrada.add(new NAGrupo("orden", -1, null, listaCamposOrden, new ArrayList<NAGrupo>()));
    
    ArrayList<NACampo> listaCamposResultado = new ArrayList<NACampo>();
    listaCamposResultado.add(new NACampo("codigo", "3", "", 80, -1, null));
    listaCamposResultado.add(new NACampo("descripcion", "2", "", 80, -1, null));
    ArrayList<NAGrupo> listaGruposSalida = new ArrayList<NAGrupo>();
    listaGruposSalida.add(new NAGrupo("resultado", -1, null, listaCamposResultado, new ArrayList<NAGrupo>()));
    
    ArrayList<NAGrupo> listaGruposDescriptor = new ArrayList<NAGrupo>();
    listaGruposDescriptor.add(new NAGrupo("entrada", -1, Acceso.E, listaCamposEntrada, listaGruposEntrada));
    listaGruposDescriptor.add(new NAGrupo("salida", -1, Acceso.S, new ArrayList<NACampo>(), listaGruposSalida));
    
    fitnesseNA.setDescriptor(new NADescriptor("prueba", new ArrayList<NACampo>(), listaGruposDescriptor));
    
    // Ejecutar prueba y comprobar resultados
    fitnesseNA.obtenerCamposSalida();
    ElementoFitnesseSalida elto1 = fitnesseNA.getCamposSalida().get(0);
    ElementoFitnesseSalida elto2 = fitnesseNA.getCamposSalida().get(1);
    assertEquals("Nombre punto elto1, prueba \"obtener campos entrada\"", "resultado.codigo", elto1.getNombrePunto());
    assertEquals("Nombre mayúscula elto1, prueba \"obtener campos entrada\"", "resultadoCodigo", elto1.getNombreMayuscula());
    assertEquals("Nombre underscore elto1, prueba \"obtener campos entrada\"", "resultado_codigo", elto1.getNombreUnderscore());
    assertEquals("Tipo elto1, prueba \"obtener campos entrada\"", "long", elto1.getTipo());
    assertEquals("Campo original elto1, prueba \"obtener campos entrada\"", "codigo", elto1.getCampoOriginal());
    assertEquals("Nombre punto elto2, prueba \"obtener campos entrada\"", "resultado.descripcion", elto2.getNombrePunto());
    assertEquals("Nombre mayúscula elto2, prueba \"obtener campos entrada\"", "resultadoDescripcion", elto2.getNombreMayuscula());
    assertEquals("Nombre underscore elto2, prueba \"obtener campos entrada\"", "resultado_descripcion", elto2.getNombreUnderscore());
    assertEquals("Tipo elto2, prueba \"obtener campos entrada\"", "String", elto2.getTipo());
    assertEquals("Campo original elto2, prueba \"obtener campos entrada\"", "descripcion", elto2.getCampoOriginal());
  }

//   @Test
//   public void testGenerateWikiPage() {
//     try {
//       File dnaFile = new File("LEB0001pruebaDna.xml");
//       dnaFile.createNewFile();
//       PrintWriter pw = new PrintWriter(dnaFile);
//       String pruebaDnaContent = "<?xml version=\"1.0\" encoding=\"iso-8859-1\"?>\n";
//       pruebaDnaContent = pruebaDnaContent.concat("<NA_DESCRIPTOR TipoServicio=\"1\" Middleware=\"JAVA\" Cabecera=\"TdE\" Programa=\"com.telefonica.na.PruebaServicio\" Descripcion=\"Prueba de Servicio NA\">\n");
//       pruebaDnaContent = pruebaDnaContent.concat("<NA_GRUPO Nombre=\"entrada\" Acceso=\"E\">\n");
//       pruebaDnaContent = pruebaDnaContent.concat("<NA_GRUPO Nombre=\"orden\">\n");
//       pruebaDnaContent = pruebaDnaContent.concat("<NA_CAMPO Nombre=\"codigo\" Longitud=\"80\" Tipo=\"2\"/>\n");
//       pruebaDnaContent = pruebaDnaContent.concat("<NA_CAMPO Nombre=\"descripcion\" Longitud=\"80\" Tipo=\"2\"/>\n");
//       pruebaDnaContent = pruebaDnaContent.concat("</NA_GRUPO>\n");
//       pruebaDnaContent = pruebaDnaContent.concat("<NA_CAMPO Nombre=\"contacto\" Longitud=\"80\" Tipo=\"2\"/>\n");
//       pruebaDnaContent = pruebaDnaContent.concat("<NA_CAMPO Nombre=\"prioridad\" Longitud=\"80\" Tipo=\"RFC_INT\"/>\n");
//       pruebaDnaContent = pruebaDnaContent.concat("</NA_GRUPO>\n");
//       pruebaDnaContent = pruebaDnaContent.concat("<NA_GRUPO Nombre=\"salida\" Acceso=\"S\">\n");
//       pruebaDnaContent = pruebaDnaContent.concat("<NA_GRUPO Nombre=\"resultado\">\n");
//       pruebaDnaContent = pruebaDnaContent.concat("<NA_CAMPO Nombre=\"codigo\" Longitud=\"80\" Tipo=\"3\"/>\n");
//       pruebaDnaContent = pruebaDnaContent.concat("<NA_CAMPO Nombre=\"descripcion\" Longitud=\"80\" Tipo=\"2\"/>\n");
//       pruebaDnaContent = pruebaDnaContent.concat("</NA_GRUPO>\n");
//       pruebaDnaContent = pruebaDnaContent.concat("</NA_GRUPO>\n");
//       pruebaDnaContent = pruebaDnaContent.concat("</NA_DESCRIPTOR>\n");
//       pw.write(pruebaDnaContent);
//       pw.close();
      
//       String rootPath = System.getProperty("user.dir") + System.getProperty("file.separator") + "FitnesseRoot";
//       FitnesseNA fitnesseNA = new FitnesseNA(rootPath, "LEB0001pruebaDna.xml");
//       String result = fitnesseNA.generateWikiPage("slim", "C:\\Fitnesse\\classes");
//       String esperado = "!define TEST_SYSTEM {slim}\n\n";
//       esperado = esperado.concat("!path C:\\Fitnesse\\classes\n\n");
//       esperado = esperado.concat("!|com.telefonica.na.le.LEB0001pruebaDnaTest|\n");
//       esperado = esperado.concat("|contacto|prioridad|orden.codigo|orden.descripcion|resultado.codigo?|resultado.descripcion?|\n");
//       esperado = esperado.concat("|||||||\n");

//       assertEquals("Prueba generar página wiki", esperado, result);      
      
//       dnaFile.delete();
//     } catch (IOException e) {
//       e.printStackTrace();
//     }
//   }

//   @Test
//   public void testGenerateFixtureCode() {
//     try {
//       File dnaFile = new File("LEB0001pruebaDna.xml");
//       dnaFile.createNewFile();
//       PrintWriter pw = new PrintWriter(dnaFile);
//       String pruebaDnaContent = "<?xml version=\"1.0\" encoding=\"iso-8859-1\"?>\n";
//       pruebaDnaContent = pruebaDnaContent.concat("<NA_DESCRIPTOR TipoServicio=\"1\" Middleware=\"JAVA\" Cabecera=\"TdE\" Programa=\"com.telefonica.na.PruebaServicio\" Descripcion=\"Prueba de Servicio NA\">\n");
//       pruebaDnaContent = pruebaDnaContent.concat("<NA_GRUPO Nombre=\"entrada\" Acceso=\"E\">\n");
//       pruebaDnaContent = pruebaDnaContent.concat("<NA_GRUPO Nombre=\"orden\">\n");
//       pruebaDnaContent = pruebaDnaContent.concat("<NA_CAMPO Nombre=\"codigo\" Longitud=\"80\" Tipo=\"2\"/>\n");
//       pruebaDnaContent = pruebaDnaContent.concat("<NA_CAMPO Nombre=\"descripcion\" Longitud=\"80\" Tipo=\"2\"/>\n");
//       pruebaDnaContent = pruebaDnaContent.concat("</NA_GRUPO>\n");
//       pruebaDnaContent = pruebaDnaContent.concat("<NA_CAMPO Nombre=\"contacto\" Longitud=\"80\" Tipo=\"2\"/>\n");
//       pruebaDnaContent = pruebaDnaContent.concat("<NA_CAMPO Nombre=\"prioridad\" Longitud=\"80\" Tipo=\"RFC_INT\"/>\n");
//       pruebaDnaContent = pruebaDnaContent.concat("</NA_GRUPO>\n");
//       pruebaDnaContent = pruebaDnaContent.concat("<NA_GRUPO Nombre=\"salida\" Acceso=\"S\">\n");
//       pruebaDnaContent = pruebaDnaContent.concat("<NA_GRUPO Nombre=\"resultado\">\n");
//       pruebaDnaContent = pruebaDnaContent.concat("<NA_CAMPO Nombre=\"codigo\" Longitud=\"80\" Tipo=\"3\"/>\n");
//       pruebaDnaContent = pruebaDnaContent.concat("<NA_CAMPO Nombre=\"descripcion\" Longitud=\"80\" Tipo=\"2\"/>\n");
//       pruebaDnaContent = pruebaDnaContent.concat("</NA_GRUPO>\n");
//       pruebaDnaContent = pruebaDnaContent.concat("</NA_GRUPO>\n");
//       pruebaDnaContent = pruebaDnaContent.concat("</NA_DESCRIPTOR>\n");
//       pw.write(pruebaDnaContent);
//       pw.close();
      
//       String rootPath = System.getProperty("user.dir") + System.getProperty("file.separator") + "FitnesseRoot";
//       FitnesseNA fitnesseNA = new FitnesseNA(rootPath, "LEB0001pruebaDna.xml");
//       String result = fitnesseNA.generateFixtureCode();
//       String esperado = "package com.telefonica.na.le;\n\n";
//       esperado = esperado.concat("import com.telefonica.na.NAServicio;\n");
//       esperado = esperado.concat("import com.telefonica.na.NAWRException;\n\n");
//       esperado = esperado.concat("import java.util.ArrayList;\n");
//       esperado = esperado.concat("import es.tid.litt.na.util.CrossReference;\n\n");
//       esperado = esperado.concat("public class LEB0001pruebaDnaTest {\n");
//       esperado = esperado.concat("\tprivate NAServicio servicioNA;\n");
//       esperado = esperado.concat("\tprivate boolean ejecutado;\n");
//       esperado = esperado.concat("\tprivate int filaEjecucion;\n");
//       esperado = esperado.concat("\tpublic static ArrayList<String> st_resultado_codigo;\n");
//       esperado = esperado.concat("\tpublic static ArrayList<String> st_resultado_descripcion;\n\n");
//       esperado = esperado.concat("\tpublic LEB0001pruebaDnaTest () throws NAWRException {\n");
//       esperado = esperado.concat("\t\tservicioNA = new NAServicio(\"LEB0001pruebaDna\");\n");
//       esperado = esperado.concat("\t\tejecutado = false;\n");
//       esperado = esperado.concat("\t\tfilaEjecucion = 0;\n");
//       esperado = esperado.concat("\t\tst_resultado_codigo = new ArrayList<String>();\n");
//       esperado = esperado.concat("\t\tst_resultado_descripcion = new ArrayList<String>();\n");
//       esperado = esperado.concat("\t}\n\n");
//       esperado = esperado.concat("\tpublic void setContacto (String contacto) throws NAWRException {\n");
//       esperado = esperado.concat("\t\tif (contacto.startsWith(\"#\")) {\n");
//       esperado = esperado.concat("\t\t\tservicioNA.setCampo(\"contacto\", CrossReference.getReferenceValue(contacto));\n");
//       esperado = esperado.concat("\t\t} else {\n");
//       esperado = esperado.concat("\t\t\tservicioNA.setCampo(\"contacto\", contacto);\n");
//       esperado = esperado.concat("\t\t}\n");
//       esperado = esperado.concat("\t}\n\n");
//       esperado = esperado.concat("\tpublic void setPrioridad (String prioridad) throws NAWRException {\n");
//       esperado = esperado.concat("\t\tif (prioridad.startsWith(\"#\")) {\n");
//       esperado = esperado.concat("\t\t\tservicioNA.setCampo(\"prioridad\", CrossReference.getReferenceValue(prioridad));\n");
//       esperado = esperado.concat("\t\t} else {\n");
//       esperado = esperado.concat("\t\t\tservicioNA.setCampo(\"prioridad\", prioridad);\n");
//       esperado = esperado.concat("\t\t}\n");
//       esperado = esperado.concat("\t}\n\n");
//       esperado = esperado.concat("\tpublic void setOrdenCodigo (String ordenCodigo) throws NAWRException {\n");
//       esperado = esperado.concat("\t\tif (ordenCodigo.startsWith(\"#\")) {\n");
//       esperado = esperado.concat("\t\t\tservicioNA.setCampo(\"codigo\", CrossReference.getReferenceValue(ordenCodigo));\n");
//       esperado = esperado.concat("\t\t} else {\n");
//       esperado = esperado.concat("\t\t\tservicioNA.setCampo(\"codigo\", ordenCodigo);\n");
//       esperado = esperado.concat("\t\t}\n");
//       esperado = esperado.concat("\t}\n\n");
//       esperado = esperado.concat("\tpublic void setOrdenDescripcion (String ordenDescripcion) throws NAWRException {\n");
//       esperado = esperado.concat("\t\tif (ordenDescripcion.startsWith(\"#\")) {\n");
//       esperado = esperado.concat("\t\t\tservicioNA.setCampo(\"descripcion\", CrossReference.getReferenceValue(ordenDescripcion));\n");
//       esperado = esperado.concat("\t\t} else {\n");
//       esperado = esperado.concat("\t\t\tservicioNA.setCampo(\"descripcion\", ordenDescripcion);\n");
//       esperado = esperado.concat("\t\t}\n");
//       esperado = esperado.concat("\t}\n\n");
//       esperado = esperado.concat("\tpublic long resultadoCodigo () throws NAWRException {\n");
//       esperado = esperado.concat("\t\tif (!ejecutado) {\n");
//       esperado = esperado.concat("\t\t\tservicioNA.ejecutar();\n");
//       esperado = esperado.concat("\t\t\tejecutado = true;\n");
//       esperado = esperado.concat("\t\t}\n");
//       esperado = esperado.concat("\t\tst_resultado_codigo.add(filaEjecucion, servicioNA.getCampo(\"codigo\").toString().trim());\n");
//       esperado = esperado.concat("\t\treturn servicioNA.getCampo(\"codigo\").longValue();\n");
//       esperado = esperado.concat("\t}\n\n");
//       esperado = esperado.concat("\tpublic String resultadoDescripcion () throws NAWRException {\n");
//       esperado = esperado.concat("\t\tif (!ejecutado) {\n");
//       esperado = esperado.concat("\t\t\tservicioNA.ejecutar();\n");
//       esperado = esperado.concat("\t\t\tejecutado = true;\n");
//       esperado = esperado.concat("\t\t}\n");
//       esperado = esperado.concat("\t\tst_resultado_descripcion.add(filaEjecucion, servicioNA.getCampo(\"descripcion\").toString().trim());\n");
//       esperado = esperado.concat("\t\tejecutado = false;\n");
//       esperado = esperado.concat("\t\tfilaEjecucion ++;\n");
//       esperado = esperado.concat("\t\treturn servicioNA.getCampo(\"descripcion\").toString().trim();\n");
//       esperado = esperado.concat("\t}\n\n");
//       esperado = esperado.concat("}");

//       assertEquals("Prueba generar página wiki", esperado, result);  
      
//       dnaFile.delete();
//     } catch (IOException e) {
//       e.printStackTrace();
//     }
//   }

}
