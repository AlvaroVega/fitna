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

public class RemoveLineContentFileResponder implements SecureResponder {
    public String resource;
    
    public Response makeResponse(FitNesseContext context, Request request) throws Exception {

        // ?removeLineContentFile&line=value
        // ?removeLineContentFile&tableNumber=t1&fileNumber=r3

        Response response = new SimpleResponse();
        resource = request.getResource();
        
        String tableNumber = (String) request.getInput("tableNumber");
        String fileNumber = (String) request.getInput("fileNumber");

        int table_number = Integer.parseInt(tableNumber.substring(1,2));
        int file_number = Integer.parseInt(fileNumber.substring(1,2));

        String filepath = context.rootPagePath + "/" + resource.toString().replace('.', '/') + 
            "/" + "content.txt";

        File content_file = new File(filepath);
        File outFile = new File(context.rootPagePath + "/" +
                                resource.toString().replace('.', '/') + 
                                "$$$$$$$$.tmp");

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
                    for (int j = 0; j < ref2.length; j++) {
                        if (j != (file_number + 1) ) {
                            out.println(ref2[j]);
                        }
                        // else: nothing
                    }

                } else { // other tables
                    out.print(ref[i] + "!|");
                }
                
            }
        }

//         String thisLine = "";
//         while ((thisLine = in.readLine()) != null) {
//             if(!thisLine.equals(content)) {
//                 out.println(thisLine);
//             }
//         }
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
