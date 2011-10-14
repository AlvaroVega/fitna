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

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class PruebaDnaReader {
	private static void imprimirCampo (NACampo campo) {
		System.out.println("Campo nombre " + campo.getNombre());
		System.out.println("Campo tipo " + campo.getTipo());
		System.out.println("Campo caso defecto " + campo.getCasoDefecto());
		System.out.println("Campo longitud " + campo.getLongitud());
		System.out.println("Campo occurs " + campo.getOccurs());
		System.out.println("Campo acceso " + campo.getAcceso());
	}

	private static void imprimirGrupo (NAGrupo grupo) {
		System.out.println("Grupo nombre " + grupo.getNombre());
		System.out.println("Grupo occurs " + grupo.getOccurs());
		System.out.println("Grupo acceso " + grupo.getAcceso());

		for (int i=0; i<grupo.getListaCampos().size(); i++) {
			imprimirCampo(grupo.getListaCampos().get(i));
		}

		for (int i=0; i<grupo.getListaGrupos().size(); i++) {
			imprimirGrupo(grupo.getListaGrupos().get(i));
		}
	}
	
	public static void main(String[] args) {
        try {
        	SAXParserFactory spf = SAXParserFactory.newInstance();
        	SAXParser sp = spf.newSAXParser();
        	DnaXMLReader xmlReader = new DnaXMLReader();
        	
        	File dnaFile = new File("E:\\046_Lean_IT\\BDD-NA\\NAExamples\\descriptores\\PLB0001.xml");
        	sp.parse(dnaFile, xmlReader);
            
        	NADescriptor resultado = xmlReader.getResult();
        	String nombreDescriptor = dnaFile.getName();
        	resultado.setNombre(nombreDescriptor.substring(0, nombreDescriptor.length()-4));
        	
        	System.out.println("Nombre descriptor " +  resultado.getNombre());

        	for (int i=0; i<resultado.getListaCampos().size(); i++) {
        		imprimirCampo (resultado.getListaCampos().get(i));
        	}

        	for (int i=0; i<resultado.getListaGrupos().size(); i++) {
        		imprimirGrupo (resultado.getListaGrupos().get(i));
        	}
        } catch (Exception e){
            throw new RuntimeException(e);
        }	
	}
}
