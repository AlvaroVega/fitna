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

package fitnesse.responders.NAService;

import java.io.*;
import java.util.Properties;

import util.FileUtil;
import fitnesse.FitNesseContext;
import fitnesse.authentication.AlwaysSecureOperation;
import fitnesse.authentication.SecureOperation;
import fitnesse.authentication.SecureResponder;
import fitnesse.http.Request;
import fitnesse.http.Response;
import fitnesse.http.SimpleResponse;

import es.tid.litt.na.NAServiceAnagram;
import es.tid.litt.na.NAServiceKind;
import es.tid.litt.na.NAServiceTests;
import es.tid.litt.na.FitnesseNA;
import es.tid.litt.na.NADescriptor;
import es.tid.litt.na.AditionalFile;

import fitnesse.wiki.*;
import fitnesse.wikitext.widgets.*;
import fitnesse.wikitext.WikiWidget;

import fitnesse.http.RequestBuilder;
import fitnesse.http.ResponseParser;
import fitnesse.testutil.FitNesseUtil;

public class GenerateAditionalFileResponder implements SecureResponder {

    public String resource;
    
    public Response makeResponse(FitNesseContext context, Request request) throws Exception {

        // ?generateAditionalFile

        Response response = new SimpleResponse();
        resource = request.getResource();
    
        //String id = (String) request.getInput("id");
        //String desc = (String) request.getInput("desc");

        String filepath = context.rootPagePath + "/" + resource.toString().replace('.', '/') + 
            "/" + "content.txt";

        // row_tables matrix will store values to retrieve from content.txt:
        // - number of tables 
        // - current idRow per each table
        // - number of cols per each table
        int row_tables[][] = new int [10][2];
        int row_tables_length = 0; // = number of tables


        // Check if adf.txt exists then stop
        String aditionalfilepath = context.rootPagePath + "/" + resource.toString().replace('.', '/') + 
            "/" + "adf.txt";
        File aditional_file = new File(aditionalfilepath); 
        
        try {            
            if (!aditional_file.exists()) {
                aditional_file.createNewFile();
            }
            else {
                response.redirect(resource);
                return response;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        File content_file = new File(filepath);
        FileInputStream fis  = new FileInputStream(content_file);
        String the_content = this.convertStreamToString(fis);
        fis.close();
        String ref [] = the_content.split("\\!\\|");
        for (int i = 1; i < ref.length; i++) {
            StandardTableWidget table = new StandardTableWidget(new MockWidgetRoot(), 
                                                                "!|" + ref[i]);
            row_tables[i-1][0] = table.numberOfChildren();
            row_tables[i-1][1] = table.getColumns();
            row_tables_length = i;
        }

        for (int j = 0; j < row_tables_length; j++) {
            // TODO: make a REST invoacation to ?setValueAditionalFile
//             RequestBuilder builder = new RequestBuilder(resource.toString() + "?setValueAditionalFile");
//             //builder.setMethod("POST");
//             builder.setHostAndPort("localhost", 8181);
//             builder.addInput("tableNumber", String.valueOf(j));
//             builder.addInput("id", "currentIdRow");
//             builder.addInput("value", String.valueOf(row_tables[j][0]));
//             String text = builder.getText();
//             //System.out.println("text = " + text);
//             MyThread mythread = new MyThread(builder);
//             mythread.start();
//             mythread.sleep(5000);
            // END TODO

            // Write adf.txt something like:
            //   t1_currentIdRow = 0
            //   t1_colsShowed = [true]

            AditionalFile.setAditionalFile(context.rootPagePath, resource.toString(),
                                           "adf.txt", "t" + String.valueOf(j+1), "currentIdRow", 
                                           String.valueOf(row_tables[j][0]-2)); 

            AditionalFile.setAditionalFile(context.rootPagePath, resource.toString(),
                                           "adf.txt", "t" + String.valueOf(j+1), "colsShowed", 
                                           generateColumText(row_tables[j][1]));

            // Generate for each row: 
            //        t1_r1_id = 
            //        t1_r1_desc = 
            for (int l = 0; l < row_tables[j][0]-2; l++) {
                AditionalFile.setAditionalFile(context.rootPagePath, resource.toString(),
                                               "adf.txt", "t" + String.valueOf(j+1), 
                                               "r" + String.valueOf(l+1) + "_id", 
                                               "Prueba " + (l+1));
                AditionalFile.setAditionalFile(context.rootPagePath, resource.toString(),
                                               "adf.txt", "t" + String.valueOf(j+1), 
                                               "r" + String.valueOf(l+1) + "_desc", 
                                               "Descripcion prueba " + (l+1));
            }
        }
        
        response.redirect(resource);
        return response;
    }

    public SecureOperation getSecureOperation() {
        return new AlwaysSecureOperation();
    }

    private String generateColumText(int cols_number) {
        String aux = "[";
        for (int z = 0; z < cols_number - 1; z++) {
            aux = aux + "true," ;
        }
        aux= aux + "true]"; 
        return aux;
    }


    // Copypasted from http://www.kodejava.org/examples/266.html
    public static String convertStreamToString(InputStream is)
        throws IOException {
        /*
         * To convert the InputStream to String we use the
         * Reader.read(char[] buffer) method. We iterate until the
         * Reader return -1 which means there's no more data to
         * read. We use the StringWriter class to produce the string.
         */
        if (is != null) {
            Writer writer = new StringWriter();
 
            char[] buffer = new char[1024];
            try {
                Reader reader = new BufferedReader(
                        new InputStreamReader(is, "UTF-8"));
                int n;
                while ((n = reader.read(buffer)) != -1) {
                    writer.write(buffer, 0, n);
                }
            } finally {
                is.close();
            }
            return writer.toString();
        } else {       
            return "";
        }
    }

//     private class MyThread extends Thread {
//         private RequestBuilder m_builder;
//         public MyThread(RequestBuilder builder) {
//             super();
//             m_builder = builder;            
//         }        
//         public void run() {
//             try {
//                 ResponseParser.performHttpRequest("localhost", 8181, m_builder);
//             }
//             catch (Exception e) {
//                 e.printStackTrace();
//             }
//         }
//     };


}
