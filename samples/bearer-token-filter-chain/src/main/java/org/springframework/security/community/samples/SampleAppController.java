/*
 * Copyright 2002-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package org.springframework.security.community.samples;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.RedirectView;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

@Controller
public class SampleAppController {
	private static final Log logger = LogFactory.getLog(SampleAppController.class);

	@RequestMapping(value = {"/local/logout"})
	public View logout(HttpServletRequest request,
					   HttpServletResponse response,
					   Authentication authentication) {
		logger.info("Sample Application - Logging out locally!");
		SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
		logoutHandler.logout(request, response, authentication);
		return new RedirectView("/non-secure", true);
	}

	@RequestMapping(value = {"/non-secure"})
	public String nonSecure(HttpServletRequest request,
							Model model) {
		model.addAttribute("authorization", request.getHeader("Authorization"));
		return "logged-in";
	}

	@RequestMapping(value = {"/**"},
					consumes = MediaType.APPLICATION_JSON_VALUE,
					produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String secureRest(Authentication authentication) {
		logger.info("Sample Application - Authenticated Rest Call:" + authentication.getName());
		return "{ \"status\": \"OK\" }";
	}

	@RequestMapping(value = {"/**"})
	public String secure(HttpServletRequest request,
						 Model model) {
		logger.info("Sample Application - You are logged in!");
		return nonSecure(request, model);
	}
}
