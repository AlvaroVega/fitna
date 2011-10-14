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

public class AddLineContentFileResponder implements SecureResponder {
    public String resource;
    
    public Response makeResponse(FitNesseContext context, Request request) throws Exception {

        // ?addLineContentFile&line=value
        // ?addLineContentFile&tableNumber=t1&new_line=minuevalinea

        Response response = new SimpleResponse();
        resource = request.getResource();
    
        String tableNumber = (String) request.getInput("tableNumber");
        String new_line_content = (String) request.getInput("new_line");

        int table_number = Integer.parseInt(tableNumber.substring(1,2));

        String filepath = context.rootPagePath + "/" + resource.toString().replace('.', '/') + 
            "/" + "content.txt";

        File content_file = new File(filepath);
        File outFile = new File(context.rootPagePath + "/" + 
                                resource.toString().replace('.', '/') + 
                                "/" +"$$$$$$$$.tmp");

        // input
        FileInputStream fis  = new FileInputStream(content_file);
        BufferedReader in = new BufferedReader(new InputStreamReader(fis));
        String the_content = GenerateAditionalFileResponder.convertStreamToString(fis);
        fis.close();
        
        // output         
        FileOutputStream fos = new FileOutputStream(outFile);
        PrintWriter out = new PrintWriter(fos);
        
        String ref [] = the_content.split("\\!\\|");

        for (int i = 0; i < ref.length; i++) {
            if (i == 0) { // previous content of file
                out.print(ref[i] + "!|");
            } else { // current table
                if (i == table_number) {
                    String ref2 [] = ref[i].split("\n");
                    for (int j = 0; j <= ref2.length; j++) {
                        if (j == ref2.length) {
                            out.println(new_line_content);
                        } else {
                            out.println(ref2[j]);
                        }
                    }

                } else { // other tables
                    out.print(ref[i] + "!|");
                }
                
            }
        }


        in.close();
        out.flush();
        out.close();
        try {
            if (!content_file.delete())
                System.out.println("not deleted");
            if (!outFile.renameTo(content_file))
                System.out.println("not renamed");
        } catch (Exception ex) {
            System.out.println("Exception: " + ex);
        }

        response.redirect(resource);
        return response;
    }

    public SecureOperation getSecureOperation() {
        return new AlwaysSecureOperation();
    }

}
