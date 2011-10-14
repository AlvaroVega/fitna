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
package es.tid.litt.na.util;

import java.lang.reflect.*;
import java.util.ArrayList;

public class CrossReference {

    static public String getReferenceValue (String refExp) {
        // Print warning about wrong format
        refExp = refExp.substring(1, refExp.length());
        String refExpSplitted [] = refExp.split ("\\.",2);
        String baseNameClass = refExpSplitted[0];
        String fieldSplitted [] = refExpSplitted[1].split("\\(");
        String fieldName = fieldSplitted[0];
        int row = new Integer(fieldSplitted[1].substring(0, fieldSplitted[1].length()-1)).intValue();
        
        try {
            Class testClass = Class.forName ("com.telefonica.na." + 
                                             baseNameClass.substring(0,2).toLowerCase() + 
                                             "." + baseNameClass);

            Field varResult = testClass.getField("st_" + fieldName.replace(".","_"));
            
            Object value = varResult.get(null/*testClass.newInstance()*/);
            ArrayList<String> fieldValues = (ArrayList<String>) value;
            return fieldValues.get(row-1);
       	
        } catch (Exception e) {
            // return exception to Fitnesse
            System.err.println("Excepcion in CrossReference: " + e);
            e.printStackTrace();
        }
        return "";
    }

    public static void main(String[] args) {
        try {
            String comando = "#PLB0001Test.NOMBRE_PRUEBA(1)";
            String result = CrossReference.getReferenceValue(comando);
            System.err.println(comando + " = " + result);
        } catch (Exception e){
            throw new RuntimeException(e);
        }

    }
    
    
}
