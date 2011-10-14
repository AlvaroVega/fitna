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


public class SetValueAditionalFileResponder implements SecureResponder {
    public String resource;
    
    public Response makeResponse(FitNesseContext context, Request request) throws Exception {

        // ?setValueAditionalFile&tableNumber=1&id=currentIdRow&value=1
        // ?setValueAditionalFile&tableNumber=1&id=colsShowed&value=[true,true]
        // ?setValueAditionalFile&tableNumber=1&id=r1_id&value=desc
        // ?setValueAditionalFile&tableNumber=1&id=r1_desc&value=desc

        Response response = new SimpleResponse();
        resource = request.getResource();
    
        String tableNumber = (String) request.getInput("tableNumber");
        String id = (String) request.getInput("id");
        String value = (String) request.getInput("value");


        AditionalFile.setAditionalFile(context.rootPagePath, resource.toString(), 
                                       "adf.txt", tableNumber, id, value);
        response.redirect(resource);
        return response;
    }

    public SecureOperation getSecureOperation() {
        return new AlwaysSecureOperation();
    }

}
