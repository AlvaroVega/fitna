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

import es.tid.litt.na.NAGrupo.Acceso;

public class NACampo {
	private String nombre, tipo, casoDefecto;
	private int longitud, occurs;
	private Acceso acceso;
	
	public NACampo() {
		this.nombre = null;
		this.tipo = null;
		this.casoDefecto = null;
		this.longitud = -1;
		this.occurs = -1;
		this.acceso = null;
	}

  public NACampo(String nombre, String tipo, String casoDefecto, int longitud, int occurs, Acceso acceso) {
    this.nombre = nombre;
    this.tipo = tipo;
    this.casoDefecto = casoDefecto;
    this.longitud = longitud;
    this.occurs = occurs;
    this.acceso = acceso;
  }

  public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getNombre() {
		return nombre;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getTipo() {
		return tipo;
	}

	public void setCasoDefecto(String casoDefecto) {
		this.casoDefecto = casoDefecto;
	}

	public String getCasoDefecto() {
		return casoDefecto;
	}

	public void setLongitud(int longitud) {
		this.longitud = longitud;
	}

	public int getLongitud() {
		return longitud;
	} 
	
	public void setOccurs(int occurs) {
		this.occurs = occurs;
	}

	public int getOccurs() {
		return occurs;
	}

	public void setAcceso(Acceso acceso) {
		this.acceso = acceso;
	}

	public Acceso getAcceso() {
		return acceso;
	}
}
