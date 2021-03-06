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

import java.io.File;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.text.ParseException;
import java.util.Date;

import util.Clock;

import fitnesse.FitNesseContext;
import fitnesse.Responder;
import fitnesse.http.InputStreamResponse;
import fitnesse.http.Request;
import fitnesse.http.Response;
import fitnesse.http.SimpleResponse;
import fitnesse.responders.NotFoundResponder;
//import fitnesse.responders.files.*;

public class NAServiceResponder implements Responder {
  private static FileNameMap fileNameMap = URLConnection.getFileNameMap();
  public String resource;
  public File requestedFile;
  public Date lastModifiedDate;
  public String lastModifiedDateString;

  public static Responder makeResponder(Request request, String rootPath) throws Exception {
    String resource = request.getResource();

    if (fileNameHasSpaces(resource))
      resource = restoreRealSpacesInFileName(resource);

    File requestedFile = new File(rootPath + "/" + resource);
    if (!requestedFile.exists())
      return new NotFoundResponder();

    if (requestedFile.isDirectory())
      return new DirectoryNAServiceResponder(resource, requestedFile);
    else
      return new NAServiceResponder(resource, requestedFile);
  }

  public NAServiceResponder(String resource, File requestedFile) {
    this.resource = resource;
    this.requestedFile = requestedFile;
  }

  public Response makeResponse(FitNesseContext context, Request request) throws Exception {
    InputStreamResponse response = new InputStreamResponse();
    determineLastModifiedInfo();

    if (isNotModified(request))
      return createNotModifiedResponse();
    else {
      response.setBody(requestedFile);
      setContentType(requestedFile, response);
      response.setLastModifiedHeader(lastModifiedDateString);
    }
    return response;
  }

  public static boolean fileNameHasSpaces(String resource) {
    return resource.indexOf("%20") != 0;
  }

  public static String restoreRealSpacesInFileName(String resource) throws Exception {
    return URLDecoder.decode(resource, "UTF-8");
  }

  String getResource() {
    return resource;
  }

  private boolean isNotModified(Request request) {
    if (request.hasHeader("If-Modified-Since")) {
      String queryDateString = (String) request.getHeader("If-Modified-Since");
      try {
        Date queryDate = SimpleResponse.makeStandardHttpDateFormat().parse(queryDateString);
        if (!queryDate.before(lastModifiedDate))
          return true;
      }
      catch (ParseException e) {
        //Some browsers use local date formats that we can't parse.
        //So just ignore this exception if we can't parse the date.
      }
    }
    return false;
  }

  private Response createNotModifiedResponse() {
    Response response = new SimpleResponse();
    response.setStatus(304);
    response.addHeader("Date", SimpleResponse.makeStandardHttpDateFormat().format(Clock.currentDate()));
    response.addHeader("Cache-Control", "private");
    response.setLastModifiedHeader(lastModifiedDateString);
    return response;
  }

  private void determineLastModifiedInfo() {
    lastModifiedDate = new Date(requestedFile.lastModified());
    lastModifiedDateString = SimpleResponse.makeStandardHttpDateFormat().format(lastModifiedDate);

    try  // remove milliseconds
    {
      lastModifiedDate = SimpleResponse.makeStandardHttpDateFormat().parse(lastModifiedDateString);
    }
    catch (java.text.ParseException jtpe) {
      jtpe.printStackTrace();
    }
  }

  private void setContentType(File file, Response response) {
    String contentType = getContentType(file.getName());
    response.setContentType(contentType);
  }

  public static String getContentType(String filename) {
    String contentType = fileNameMap.getContentTypeFor(filename);
    if (contentType == null) {
      if (filename.endsWith(".css"))
        contentType = "text/css";
      else if (filename.endsWith(".jar"))
        contentType = "application/x-java-archive";
      else
        contentType = "text/plain";
    }
    return contentType;
  }
}
