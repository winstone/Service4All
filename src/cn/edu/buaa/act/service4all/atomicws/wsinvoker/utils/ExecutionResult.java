/**
* Service4All: A Service-oriented Cloud Platform for All about Software Development
* Copyright (C) Institute of Advanced Computing Technology, Beihang University
* Contact: service4all@act.buaa.edu.cn
*
* This library is free software; you can redistribute it and/or
* modify it under the terms of the GNU Lesser General Public
* License as published by the Free Software Foundation; either
* version 3.0 of the License, or any later version. 
*
* This library is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
* Lesser General Public License for more details. 
*
* You should have received a copy of the GNU Lesser General Public
* License along with this library; if not, write to the Free Software
* Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
* USA
*/
package cn.edu.buaa.act.service4all.atomicws.wsinvoker.utils;

import org.apache.axiom.om.OMElement;

public class ExecutionResult {
	private boolean isSuccessful;
	private OMElement responseOmElement;
	
	public boolean isSuccessful() {
		return isSuccessful;
	}

	public OMElement getResponseOmElement() {
		return responseOmElement;
	}

	public void setResponseOmElement(OMElement responseOmElement) {
		this.responseOmElement = responseOmElement;
	}

	public ExecutionResult(boolean isSuccessful, OMElement responseOmElement) {
		this.isSuccessful = isSuccessful;
		this.responseOmElement = responseOmElement;
	}

	public ExecutionResult() {
		this.isSuccessful = false;
		this.responseOmElement = null;
	}

	public void setSuccessful(boolean isSuccessful) {
		this.isSuccessful = isSuccessful;
	}

}
