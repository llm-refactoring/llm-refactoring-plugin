/*
 * $Id: Login.java 471756 2006-11-06 15:01:43Z husted $
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package classes;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

import org.apache.log4j.Logger;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;

public class SchedulerAction extends ExampleSupport {
	private static Logger logger = Logger.getLogger( SchedulerAction.class ); //f:log
	private Integer timeInterval;

	public Integer getTimeInterval() {
		return this.timeInterval;
	}

	public void setTimeInterval( Integer timeInterval ) {
		this.timeInterval = timeInterval;
	}

	@Override
	public String execute() throws Exception {
		return this.input();
	}

	@Override
	public String input() throws Exception {
		logger.info( "Starting input()" ); //f:log
		SchedulerFactory sf = new StdSchedulerFactory(); //f:quartz
		Scheduler sc = sf.getScheduler(); //f:quartz

		JobDetail job = sc.getJobDetail( JobKey.jobKey( "supplyMailJob", "group" ) ); //f:quartz

		if ( job != null ) { //f:quartz
			this.timeInterval = job.getJobDataMap().getIntValue( "timeInterval" ); //f:quartz
		} //f:quartz

		logger.info( "Finishing input()" ); //f:log
		return super.input();
	}

	public String schedule() throws Exception {
		logger.info( "Starting schedule()" ); //f:log

		SchedulerFactory sf = new StdSchedulerFactory(); //f:quartz
		Scheduler sc = sf.getScheduler(); //f:quartz

		JobDetail job = sc.getJobDetail( JobKey.jobKey( "supplyMailJob", "group" ) ); //f:quartz

		if ( job != null ) { //f:quartz
			sc.deleteJob( JobKey.jobKey( "supplyMailJob", "group" ) ); //f:quartz
		} //f:quartz

		job = newJob( SupplyMailJob.class ).withIdentity( "supplyMailJob", "group" ).build(); //f:quartz
		job.getJobDataMap().put( "timeInterval", this.timeInterval ); //f:quartz

		Trigger trigger = newTrigger().withIdentity( "supplyMailTrigger", "group" ).startNow().withSchedule( CronScheduleBuilder.cronSchedule( "0 0/"
				+ (this.timeInterval * 60) + " * * * ?" ) ).build(); //f:quartz

		sc.scheduleJob( job, trigger ); //f:quartz

		logger.info( "Finishing schedule()" ); //f:log
		return INPUT;
	}
}