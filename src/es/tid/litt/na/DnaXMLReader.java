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

/**
 * This class implements a parser for NA services descriptors 
 */
package es.tid.litt.na;

import java.util.Stack;
import java.util.Vector;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import es.tid.litt.na.NAGrupo.Acceso;


/**
 * @author iredondo
 *
 */
public class DnaXMLReader extends DefaultHandler {

   	private String content;
   	private NADescriptor descriptor;
   	Stack<String> pila = new Stack<String>();
   	Stack<Object> pilaEltos = new Stack<Object>();
   	private boolean isValid = true; 
   	private String errorMessage = null;
   	
   	public void startDocument() throws SAXException {
   		System.out.println("Start document");
    }
 	
   	public void endDocument()throws SAXException {
   		System.out.println("End document");
   	}	

   	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
   		System.out.println("New tag:" + qName); 	
   		if (qName.equals("NA_DESCRIPTOR")) {
   			descriptor = new NADescriptor();
   			String atributo;
   	   		for (int i=0; i<attributes.getLength(); i++) {
   	   			atributo = attributes.getQName(i).toLowerCase();
   	   			if (atributo.equals("nombre")){
   	   			  descriptor.setNombre(attributes.getValue(i));
   	   			} else if (atributo.equals("tiposervicio") || atributo.equals("middleware") || atributo.equals("cabecera") 
   	   					|| atributo.equals("programa") || atributo.equals("servicio") || atributo.equals("descripcion") || atributo.equals("casoretornoid")){
   	   				continue;
   	   			} else {
   	   				throw new SAXException("Attribute " + attributes.getQName(i) + " not expected in NA_DESCRIPTOR element");
   	   			}
   	   		}  
   	   		pila.push("NA_DESCRIPTOR");
   	   		pilaEltos.push(descriptor);   	   		
   		} else if (qName.equals("NA_GRUPO")) {
   			NAGrupo grupoNuevo = new NAGrupo();
   	   		for (int i=0; i<attributes.getLength(); i++) {
   	   			if (attributes.getQName(i).toLowerCase().equals("nombre")){
   	   				grupoNuevo.setNombre(attributes.getValue(i));
   	   			} else if (attributes.getQName(i).toLowerCase().equals("occurs")){
   	   				grupoNuevo.setOccurs(new Integer(attributes.getValue(i)));
   	   			} else if (attributes.getQName(i).toLowerCase().equals("acceso")){
   	   				grupoNuevo.setAcceso(Acceso.valueOf(attributes.getValue(i)));
   	   			} else if (attributes.getQName(i).toLowerCase().equals("redefine")){
   	   				continue;
   	   			} else {
   	   				throw new SAXException("Attribute " + attributes.getQName(i) + " not expected in NA_GRUPO element");
   	   			}
   	   		}  			
   			if (pila.peek().equals("NA_DESCRIPTOR")) {
   				NADescriptor descriptor = (NADescriptor) pilaEltos.pop();
   				descriptor.addGrupo(grupoNuevo);
   				pilaEltos.push(descriptor);
   			} else if (pila.peek().equals("NA_GRUPO")) {
   				NAGrupo grupo = (NAGrupo) pilaEltos.pop();
   				grupo.addGrupo(grupoNuevo);
   				pilaEltos.push(grupo);
   			} else {
   				throw new SAXException("Element NA_GRUPO not expected inside " + pila.peek() + " element");
   			}
   			pila.push("NA_GRUPO");
   			pilaEltos.push(grupoNuevo);
   		} else if (qName.equals("NA_CAMPO")) { 
   			NACampo campoNuevo = new NACampo();
   	   		for (int i=0; i<attributes.getLength(); i++) {
   	   			if (attributes.getQName(i).toLowerCase().equals("nombre")){
   	   				campoNuevo.setNombre(attributes.getValue(i));
   	   			} else if (attributes.getQName(i).toLowerCase().equals("tipo")){
   	   				campoNuevo.setTipo(attributes.getValue(i));
   	   			} else if (attributes.getQName(i).toLowerCase().equals("casodefecto")){
   	   				campoNuevo.setCasoDefecto(attributes.getValue(i));
   	   			} else if (attributes.getQName(i).toLowerCase().equals("longitud")){
   	   				campoNuevo.setLongitud(new Integer(attributes.getValue(i)));
   	   			} else if (attributes.getQName(i).toLowerCase().equals("occurs")){
   	   				campoNuevo.setOccurs(new Integer(attributes.getValue(i)));
   	   			} else if (attributes.getQName(i).toLowerCase().equals("acceso")){
   	   				campoNuevo.setAcceso(Acceso.valueOf(attributes.getValue(i)));
   	   			} else {
   	   				throw new SAXException("Attribute " + attributes.getQName(i) + " not expected in NA_CAMPO element");
   	   			}   	   				
   	   		}
   			if (pila.peek().equals("NA_DESCRIPTOR")) {
   				NADescriptor descriptor = (NADescriptor) pilaEltos.pop();
   				descriptor.addCampo(campoNuevo);
   				pilaEltos.push(descriptor);
   			} else if (pila.peek().equals("NA_GRUPO")) {
   				NAGrupo grupo = (NAGrupo) pilaEltos.pop();
   				grupo.addCampo(campoNuevo);
   				pilaEltos.push(grupo);
   			} else {
   				throw new SAXException("Element NA_CAMPO not expected inside " + pila.peek() + " element");
   			}
   			pila.push("NA_CAMPO");
   			pilaEltos.push(campoNuevo);
   		} else {
   			throw new SAXException("Element " + qName + " not expected");
   		}
    }

    public void endElement(String uri, String localName, String qName) {
   		System.out.println("End tag:" + qName); 
   		pila.pop();
    	pilaEltos.pop();
    }   
    
    public NADescriptor getResult () {
    	return descriptor;
    }
}
	

