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

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import util.FileUtil;
import fitnesse.FitNesseContext;
import fitnesse.authentication.AlwaysSecureOperation;
import fitnesse.authentication.SecureOperation;
import fitnesse.authentication.SecureResponder;
import fitnesse.http.Request;
import fitnesse.http.Response;
import fitnesse.http.SimpleResponse;
import fitnesse.http.UploadedFile;
import fitnesse.responders.files.UploadResponder;

import java.net.*;

public class UploadUrlResponder extends UploadResponder {

  private String rootPath;

  public Response makeResponse(FitNesseContext context, Request request) throws Exception {

    rootPath = context.rootPagePath;
    SimpleResponse response = new SimpleResponse();
    String resource = request.getResource().replace("%20", " ");
    
    // Read NA descriptor file from an URL
    String url_string = (String) request.getInput("url");


    // Set authenticator for Telefonica Edomus
    //     String user_string = (String) request.getInput("user");
    //     String passwd_string = (String) request.getInput("passwd");
    //     Authenticator.setDefault(new MyAuthenticator(user_string, passwd_string));

    // compose an URL
    java.net.URL url = null;
    try {
        url = new java.net.URL(url_string);
    } catch (java.net.MalformedURLException mex ) {
        System.out.println("MalformedURLException: " + mex.toString());
        response.redirect("/" + "NADescriptors");
        return response;
    }
    
    // Http get
    boolean success = false;
    int reconnections = 0;
    URLConnection urlConn = null;
    while (!success) {
        // Try to connect to the URL provided 
        urlConn = url.openConnection();
        urlConn.setDoInput(true);
        urlConn.setUseCaches(false);
        String contentType = urlConn.getContentType();
        int contentLength = urlConn.getContentLength();
        if ( (contentLength == -1) && (reconnections <= 3) ) {
            System.out.println("Wrong length ");
            reconnections++;
        } 
        else if ( !contentType.startsWith("text/") || (reconnections > 3 )) {
            // TODO: manage this case: exit? continue?
            System.out.println("Wrong Content-Type " + urlConn.getContentType());
            response.redirect("/" + "NADescriptors");
            return response;
        }
        else {
            success = true;
        }
    } 

    // Read content of file
    java.io.BufferedReader in = new java.io.BufferedReader(new java.io.InputStreamReader(urlConn.getInputStream()));
    String[] str1 = url.toString().split("\\/"); 
    String filename = str1[str1.length-1];
    java.io.FileOutputStream fos = new java.io.FileOutputStream(rootPath + "/NADescriptors/_" + filename);
    java.io.BufferedOutputStream bout = new BufferedOutputStream(fos, 1024);

    String linea;
    int x=0;
    while ((linea = in.readLine()) != null) {
        bout.write(linea.getBytes());
    }
    bout.close();
    in.close();
    fos.close();

    // Restore authentication
    // Authenticator.setDefault(null);

    UploadedFile uploadedFile = new UploadedFile(rootPath + "/NADescriptors/" + filename, 
                                                 "text", 
                                                 new File(rootPath + "/NADescriptors/_" + filename));
    if (uploadedFile.isUsable()) {
      File file = makeFileToCreate(uploadedFile, resource);
      writeFile(file, uploadedFile);
    }

    response.redirect("/" + resource.replace(" ", "%20"));
    return response;
  }

  private File makeFileToCreate(UploadedFile uploadedFile, String resource) {
    String relativeFilename = makeRelativeFilename(uploadedFile.getName());
    String filename = relativeFilename;
    int prefix = 1;
    File file = new File(makeFullFilename(resource, filename));
    while (file.exists()) {
      filename = makeNewFilename(relativeFilename, prefix++);
      file = new File(makeFullFilename(resource, filename));
    }
    return file;
  }

  private String makeFullFilename(String resource, String filename) {
    return rootPath + "/" + resource + filename;
  }

    
    
    static class MyAuthenticator extends Authenticator {
        private String username, password;
        
        public MyAuthenticator(String user, String pass) {
            username = user;
            password = pass;
        }
        
        protected PasswordAuthentication getPasswordAuthentication() {
            System.out.println("Requesting Host  : " + getRequestingHost());
            System.out.println("Requesting Port  : " + getRequestingPort());
            System.out.println("Requesting Prompt : " + getRequestingPrompt());
            System.out.println("Requesting Protocol: "
                               + getRequestingProtocol());
            System.out.println("Requesting Scheme : " + getRequestingScheme());
            System.out.println("Requesting Site  : " + getRequestingSite());
            return new PasswordAuthentication(username, password.toCharArray());
        }
    }
    
    
}
