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

public class NAServiceKind {

    public static String getKindNAService(String na_service_name) {
        
        // TO DO: connect with DIANA to know more about kind of NA service: basic, multi...
        
        String kind_na_service = "BasicServiceTest"; // "MultiServiceTest"
        return kind_na_service;
    }

}
