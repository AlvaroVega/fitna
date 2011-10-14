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

public class GenerateTestNAServiceResponder implements SecureResponder {
  public String resource;

  public Response makeResponse(FitNesseContext context, Request request) throws Exception {
    Response response = new SimpleResponse();
    resource = request.getResource();
    String filename = (String) request.getInput("filename");
    String pathname = context.rootPagePath + "/" + resource + filename;

    String[] str1 = filename.toString().split("\\.");
    if ( str1.length == 0) {
        return response;
    }

    // FitNess wiki names restrictions...
    String na_service_basename = str1[0].substring(0,1) + str1[0].substring(1).toLowerCase();

    String destination_classpath = context.rootPagePath + "/" + "classes" +
        "/com/telefonica/na/";

    String wiki_page_source_text = null;
    String fixture_code_source_text = null;

    try {
        FitnesseNA fitnesseNA = new FitnesseNA(context.rootPagePath, pathname);
        wiki_page_source_text = fitnesseNA.generateWikiPage("slim", destination_classpath);
        fixture_code_source_text = fitnesseNA.generateFixtureCode();
    } catch (Exception ex ) {
        response.redirect("/" + "FitNesse" + "." + "NaTests");
        return response;
    }
    

    String na_service_kind = NAServiceKind.getKindNAService(na_service_basename);

    String destination_anagram_path =
        context.rootPagePath + "/" + "FitNesse" + "/" + "NaTests" + "/" + na_service_kind;

    String anagram_na_service = 
        NAServiceAnagram.createNAServiceAnagram(na_service_basename, 
                                                destination_anagram_path);

    String destination_path = destination_anagram_path + "/" + anagram_na_service + 
        "/" +na_service_basename + "Test"; 

    Boolean already_exists =
        NAServiceTests.generateTestFile(destination_path, "content.txt", 
                                        wiki_page_source_text, false);
        
    if (already_exists)
        NAServiceTests.deleteFile(destination_path, "adf.txt");

    already_exists =
        NAServiceTests.generateTestFile(destination_path, "properties.xml", 
                                        NAServiceTests.properties_file_source_text, false);

    already_exists = 
        NAServiceTests.generateTestFile(destination_classpath + str1[0].substring(0,2).toLowerCase(),  
                                    str1[0] + "Test" +".java", 
                                    fixture_code_source_text, true);

    response.redirect("/" + "FitNesse" + "." + "NaTests" + "." + na_service_kind + 
                      "." + anagram_na_service + "." + na_service_basename + "Test");
    return response;
  }

  public SecureOperation getSecureOperation() {
    return new AlwaysSecureOperation();
  }



}
