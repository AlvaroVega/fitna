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

public class NAServiceTests {

    public static String[] properties_file_source_text = { 
        "<?xml version=\"1.0\"?>\n",
        "<properties>\n",
        "        <Files/>\n",
        "        <Test>true</Test>\n",
        "        <Edit>true</Edit>\n",
        "        <Refactor>true</Refactor>\n",
        "        <Help/>\n",
        "        <RecentChanges>true</RecentChanges>\n",
        "        <Search/>\n",
        "        <Suites/>\n",
        "        <WhereUsed/>\n",
        "        <NADescriptors>true</NADescriptors>\n",
        "        <NATestSuites>true</NATestSuites>\n",
        "</properties>\n",
        "" };



    public static void deleteFile(String destination_path, String file_name) {

        File destination_path_dir = new File(destination_path);

        if (destination_path_dir.exists()) {

            File file = new File(destination_path+"/"+file_name);

            if (file.exists())
                file.delete();                
        }
    }
    

    public static Boolean generateTestFile(String destination_path, String file_name, 
                                           String file_content, boolean compile) {
        Boolean already_exists = false;
        File destination_path_dir = new File(destination_path);
        if (!destination_path_dir.exists())
            destination_path_dir.mkdirs();
        else
            already_exists = true;

        File file = new File(destination_path+"/"+file_name); 
       
        try {            
            if (!file.exists())
                file.createNewFile();
            
            FileOutputStream fos = new FileOutputStream(file); 
            OutputStreamWriter osw = new OutputStreamWriter(fos);
            osw.write(file_content.toString());
            osw.close();
            
        } catch (IOException e) {
            e.printStackTrace();
        }

        // java compilation ?
        if (compile) {
            com.sun.tools.javac.Main java_compiler = new com.sun.tools.javac.Main();
            java_compiler.compile(new String[]{file.getAbsolutePath()});
        }
        return already_exists;
    }


    public static Boolean generateTestFile(String destination_path, String file_name, 
                                           String[] file_content, boolean compile) {
        Boolean already_exists = false;
        File destination_path_dir = new File(destination_path);
        if (!destination_path_dir.exists())
            destination_path_dir.mkdirs();
        else
            already_exists = true;

        File file = new File(destination_path+"/"+file_name); 
       
        try {            
            if (!file.exists())
                file.createNewFile();
            
            FileOutputStream fos = new FileOutputStream(file); 
            OutputStreamWriter osw = new OutputStreamWriter(fos);
            for (int i = 0; i < file_content.length; i++) {
                osw.write(file_content[i]);
            }
            osw.close();
            
        } catch (IOException e) {
            e.printStackTrace();
        }

        // java compilation ?
        if (compile) {
            com.sun.tools.javac.Main java_compiler = new com.sun.tools.javac.Main();
            java_compiler.compile(new String[]{file.getAbsolutePath()});
        }
        return already_exists;
    }


}
