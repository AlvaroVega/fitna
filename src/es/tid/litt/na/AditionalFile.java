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

import java.io.*;
import java.util.Properties;

public class AditionalFile {

//     public static String[] aditional_file_source_text = { 
//         "t1_currentIdRow = 0\n",
//         "t1_colsShowed = [true]\n",
//         "" };

    public static void setAditionalFile(String page, String resource_name, 
                                        String file_name, String tableNumber, 
                                        String id, String value) throws Exception {


        String filepath = page + "/" + resource_name.replace('.', '/') + 
            "/" + file_name;

        File adf = new File(filepath);
        Properties props = new Properties();
        props.load(new FileInputStream(adf));
        if (tableNumber != null) {
            props.setProperty(/*"t" +*/ tableNumber + "_" + id, value);
        } 
        props.store(new FileOutputStream(adf), "");

    }


}
