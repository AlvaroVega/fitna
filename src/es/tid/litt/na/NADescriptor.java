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

import java.util.ArrayList;

public class NADescriptor {
	private String nombre;
	private ArrayList<NACampo> listaCampos;
	private ArrayList<NAGrupo> listaGrupos;
	
	public NADescriptor() {
		this.nombre = null;
		this.listaCampos = new ArrayList<NACampo>();
		this.listaGrupos = new ArrayList<NAGrupo>();
	}

  public NADescriptor(String nombre, ArrayList<NACampo> listaCampos, ArrayList<NAGrupo> listaGrupos) {
    this.nombre = nombre;
    this.listaCampos = listaCampos;
    this.listaGrupos = listaGrupos;
  }

  public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getNombre() {
		return nombre;
	}

	public void setListaCampos(ArrayList<NACampo> listaCampos) {
		this.listaCampos = listaCampos;
	}

	public ArrayList<NACampo> getListaCampos() {
		return listaCampos;
	}

	public void addCampo(NACampo campo) {
		this.listaCampos.add(campo);
	}
	
	public void setListaGrupos(ArrayList<NAGrupo> listaGrupos) {
		this.listaGrupos = listaGrupos;
	}

	public ArrayList<NAGrupo> getListaGrupos() {
		return listaGrupos;
	}

	public void addGrupo(NAGrupo grupo) {
		this.listaGrupos.add(grupo);
	}
}
