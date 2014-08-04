/*
*
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
*
*/
package cn.edu.buaa.act.service4all.core.samanager;

import java.util.Map;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import cn.edu.buaa.act.service4all.core.samanager.appliance.ApplianceManager;
import cn.edu.buaa.act.service4all.core.samanager.appliance.HostManager;

public class SchedulerJob implements Job {

	private final long max_internal = 60000;

//	private final Log logger = LogFactory.getLog( SchedulerJob.class );


	@Override
	public void execute( JobExecutionContext context )
			throws JobExecutionException {

		@SuppressWarnings("rawtypes")
		Map properties = context.getJobDetail().getJobDataMap();
//
		@SuppressWarnings("unchecked")
		Map<String, ApplianceManager> applianceMgers = (Map<String, ApplianceManager>) properties
				.get( "applianceManagers" );
//		Map<String, AppManager> appMgers = (Map<String, AppManager>) properties
//				.get( "appManagers" );

		HostManager hostMger = (HostManager) applianceMgers
				.get( SAManagerComponent.APPLIANCE_HOST );
		hostMger.scan( max_internal );

	}


//	private void displayAppliance(
//			Map<String, ApplianceManager> applianceMgers,
//			Map<String, AppManager> appMgers ) {
//		logger.info( "Display all the appliance registered!" );
//
//	}

}