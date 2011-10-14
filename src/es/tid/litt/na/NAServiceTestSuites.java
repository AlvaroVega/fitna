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

public class NAServiceTestSuites {

    static String[] wiki_page_source_text = { 
        "!contents \n", 
        "To incorporate a test into testsuite edit this page and add the following line: ",
        "{{{ !see .FitNesse.NaTests.MyTest }}}",
        "ending with your test class. To see all of your test classes go ",
        "[[here][FitNesse\\.NaTests?responder=search&searchString=com.telefonica.na&searchType=Search]]\n"
    };

    static String[] properties_file_source_text = { 
        "<?xml version=\"1.0\"?>\n",
        "<properties>\n",
        "        <Files/>\n",
        "        <Edit>true</Edit>\n",
        "        <Suite>true</Suite>\n",
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


    public static void createNAServiceTestSuite(String path) {

        File dir = new File(path); 
        if (!dir.exists()) {
          dir.mkdirs();

          generateFileNAServiceTestSuite(path, "content.txt", wiki_page_source_text);
          
          generateFileNAServiceTestSuite(path, "properties.xml", properties_file_source_text);
        }

    }


    public static void generateFileNAServiceTestSuite(String path,
                                                      String name_file,
                                                      String[] content_file) {

        File file = new File(path+"/"+name_file); 

        try {
            
            if (!file.exists())
                file.createNewFile();
            
            FileOutputStream fos = new FileOutputStream(file); 
            OutputStreamWriter osw = new OutputStreamWriter(fos);
            for (int i = 0; i < content_file.length; i++) {
                osw.write(content_file[i]);
            }
            osw.close();
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
