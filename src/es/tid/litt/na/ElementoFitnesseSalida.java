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

public class ElementoFitnesseSalida {
  private String nombrePunto, nombreMayuscula, nombreUnderscore;
  private String tipo;
  private String campoOriginal;
  
  public ElementoFitnesseSalida (String nombrePunto, String nombreMayuscula, String nombreUnderscore, String tipo, String campoOriginal) {
    this.nombrePunto = nombrePunto;
    this.nombreMayuscula = nombreMayuscula;
    this.nombreUnderscore = nombreUnderscore;
    this.tipo = tipo;
    this.campoOriginal = campoOriginal;
  }
  
  public void setNombrePunto(String nombrePunto) {
    this.nombrePunto = nombrePunto;
  }

  public String getNombrePunto() {
    return nombrePunto;
  }

  public void setNombreMayuscula(String nombreMayuscula) {
    this.nombreMayuscula = nombreMayuscula;
  }

  public String getNombreMayuscula() {
    return nombreMayuscula;
  }

  public void setNombreUnderscore(String nombreUnderscore) {
    this.nombreUnderscore = nombreUnderscore;
  }

  public String getNombreUnderscore() {
    return nombreUnderscore;
  }

  public void setTipo(String tipo) {
    this.tipo = tipo;
  }

  public String getTipo() {
    return tipo;
  }
  
  public void setCampoOriginal(String campoOriginal) {
    this.campoOriginal = campoOriginal;
  }

  public String getCampoOriginal() {
    return campoOriginal;
  }
}
