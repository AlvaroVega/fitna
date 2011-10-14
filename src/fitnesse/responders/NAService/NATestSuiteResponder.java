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
import java.text.SimpleDateFormat;
import java.util.Date;

import util.FileUtil;
import fitnesse.FitNesseContext;
import fitnesse.wiki.WikiPageAction;
import fitnesse.authentication.AlwaysSecureOperation;
import fitnesse.authentication.SecureOperation;
import fitnesse.authentication.SecureResponder;
import fitnesse.html.HtmlElement;
import fitnesse.html.HtmlPage;
import fitnesse.html.HtmlTableListingBuilder;
import fitnesse.html.HtmlTag;
import fitnesse.html.HtmlUtil;
import fitnesse.html.RawHtml;
import fitnesse.html.TagGroup;
import fitnesse.http.Request;
import fitnesse.http.Response;
import fitnesse.http.SimpleResponse;

public class NATestSuiteResponder implements SecureResponder {
  private String resource;
  private File requestedDirectory;
  private FitNesseContext context;
  private SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy, hh:mm a");

  public NATestSuiteResponder(String resource, File requestedFile) {
    this.resource = resource;
    requestedDirectory = requestedFile;
  }

  public Response makeResponse(FitNesseContext context, Request request) throws Exception {
    this.context = context;

    SimpleResponse simpleResponse = new SimpleResponse();
    if (!resource.endsWith("/"))
      setRedirectForDirectory(simpleResponse);
    else
      simpleResponse.setContent(makeDirectoryListingPage());
    return simpleResponse;
  }

  private void setRedirectForDirectory(Response response) {
    if (!resource.startsWith("/"))
      resource = "/" + resource;
    response.redirect(resource + "/");
  }

  private String makeDirectoryListingPage() throws Exception {
    HtmlPage page = context.htmlPageFactory.newPage();
    page.title.use("NA Test Suite: " + resource);
    page.header.use(HtmlUtil.makeBreadCrumbsWithPageType(resource, "/", "NA Test Suite Section"));
    page.actions.use(makeFrontPageLink());
    page.main.use(makeRightColumn());

    return page.html();
  }

  private HtmlTag makeFrontPageLink() {
    WikiPageAction action = new WikiPageAction("/FrontPage", "FrontPage");
    action.setQuery(null);
    return HtmlUtil.makeAction(action);
  }

  private String makeRightColumn() throws Exception {
    TagGroup html = new TagGroup();
    //html.add(addFiles(FileUtil.getDirectoryListing(requestedDirectory)));
    html.add(HtmlUtil.HR.html());
    //html.add(makeUploadForm());
    html.add(makeTestSuiteForm());
    return html.html();
  }

  private HtmlTag addFiles(File[] files) throws Exception {
    HtmlTableListingBuilder table = new HtmlTableListingBuilder();
    makeHeadingRow(table);
    addFileRows(files, table);

    return table.getTable();
  }

  private void addFileRows(File[] files, HtmlTableListingBuilder table) throws Exception {
    for (File file : files) {
      HtmlTag nameItem = makeLinkToFile(file);
      HtmlElement sizeItem = new RawHtml(getSizeString(file));
      HtmlElement dateItem = new RawHtml(dateFormat.format(new Date(file.lastModified())));
      TagGroup actionItem = new TagGroup();
      table.addRow(new HtmlElement[]{nameItem, sizeItem, dateItem, actionItem});
    }
  }

  private void makeHeadingRow(HtmlTableListingBuilder table) throws Exception {
    HtmlTag nameHeading = HtmlUtil.makeSpanTag("caps", "TestName");
    HtmlTag sizeHeading = HtmlUtil.makeSpanTag("caps", "Size");
    HtmlTag dateHeading = HtmlUtil.makeSpanTag("caps", "Date");
    table.addRow(new HtmlTag[]{nameHeading, sizeHeading, dateHeading/*, actionHeading*/});
  }

  private HtmlTag makeLinkToFile(File file) {
    String href = file.getName();
    if (file.isDirectory()) {
      href += "/";
      HtmlTag image = new HtmlTag("img");
      image.addAttribute("src", "/files/images/folder.gif");
      image.addAttribute("class", "left");
      HtmlTag link = HtmlUtil.makeLink(href, image);
      link.add(file.getName());
      return link;
    } else
      return HtmlUtil.makeLink(href, file.getName());
  }

  private HtmlTag makeTestSuiteForm() throws Exception {
    HtmlTag dirForm = HtmlUtil.makeFormTag("get", "/" + resource);
    dirForm.addAttribute("class", "left");
    dirForm.add(HtmlUtil.makeInputTag("hidden", "responder", "createNATestSuite"));
    dirForm.add("<!--create NA Test Suite form-->");
    dirForm.add(HtmlUtil.makeSpanTag("caps", "Create a TestSuite:"));
    dirForm.add(HtmlUtil.BR);
    dirForm.add(HtmlUtil.makeInputTag("text", "testsuitename", ""));
    dirForm.add(HtmlUtil.BR);
    dirForm.add(HtmlUtil.makeInputTag("submit", "", "Create"));
    return dirForm;
  }

  public static String getSizeString(File file) {
    if (file.isDirectory())
      return "";
    else
      return file.length() + " bytes";
  }

  public SecureOperation getSecureOperation() {
    return new AlwaysSecureOperation();
  }
}
