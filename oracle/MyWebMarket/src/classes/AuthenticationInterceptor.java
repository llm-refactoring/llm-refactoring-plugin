/*
 * $Id: AuthenticationInterceptor.java 563238 2007-08-06 19:53:57Z apetrelli $
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

import java.util.Map;

import org.apache.log4j.Logger;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.Interceptor;

public class AuthenticationInterceptor implements Interceptor {
	private static Logger logger = Logger.getLogger(AuthenticationInterceptor.class); //f:log
	
	public void destroy() {
	}

	public void init() {
	}

	public String intercept( ActionInvocation actionInvocation )
			throws Exception {
		logger.info( "Starting verifying user" ); //f:log
		Map session = actionInvocation.getInvocationContext().getSession();

		User user = (User) session.get(SystemConstants.AUTHENTICATED_USER);
		
		boolean isAuthenticated = (null != user);
		
		if ( isAuthenticated ) {
			logger.info( "Finishing verifying user -- verified" ); //f:log
			return actionInvocation.invoke();
		}
		
		logger.info( "Finishing verifying user -- unverified" ); //f:log
		return Action.LOGIN;
	}
}
