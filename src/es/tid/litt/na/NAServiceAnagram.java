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

public class NAServiceAnagram {

    static String[] wiki_page_source_text = { 
        "|!c !2 Anagrama NA| \n", 
        "|!contents -g| \n" };

    static String[] properties_file_source_text = { 
        "<?xml version=\"1.0\"?>\n",
        "<properties>\n",
        "        <Files/>\n",
        "        <Help/>\n",
        "        <RecentChanges>true</RecentChanges>\n",
        "        <Search/>\n",
        "        <Suites/>\n",
        "        <WhereUsed/>\n",
        "        <NADescriptors>true</NADescriptors>\n",
        "        <NATestSuites>true</NATestSuites>\n",
        "</properties>\n",
        "" };


    public static String createNAServiceAnagram(String na_service_name, 
                                              String path) {

        String anagram_na_service = getAnagramFromNAService(na_service_name);

        File dir = new File(path+"/"+anagram_na_service); 
        if (!dir.exists()) {
          dir.mkdirs();

          generateFileNAServiceAnagram(anagram_na_service, path,
                                       "content.txt", wiki_page_source_text);
          
          generateFileNAServiceAnagram(anagram_na_service, path,
                                       "properties.xml", properties_file_source_text);
        }

        return anagram_na_service;
    }

    public static String getAnagramFromNAService(String na_service_name) {
        // Anagram of NA service is just the first two letters of na_service_name
        if (na_service_name.length() > 1) {
            return (na_service_name.substring(0,2) + "Anagram");
        } else {
            return "NoAagram";
        }
    }

    public static void generateFileNAServiceAnagram(String anagram_na_service, 
                                                    String path,
                                                    String name_file,
                                                    String[] content_file) {

        File file = new File(path+"/"+anagram_na_service+"/"+name_file); 

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
