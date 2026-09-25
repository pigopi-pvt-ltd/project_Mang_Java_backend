package com.pigopi.vault.interceptors;

import java.io.PrintWriter;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import com.pigopi.vault.entity.Role;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Component
public class SessionAuthInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		HttpSession session = request.getSession(false);

		System.out.println("Path : " + request.getRequestURI());
		System.out.println("Method : " + request.getMethod());
		System.out.println("Session Present : " + (session != null));

		// Check whether session exists
		if (session != null) {
			System.out.println("Session ID : " + session.getId());
			System.out.println("User ID : " + session.getAttribute("userId"));
			System.out.println("User Role : " + session.getAttribute("userRole"));
		}

		// Check login
		if (session == null || session.getAttribute("userId") == null || session.getAttribute("userRole") == null) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.setContentType("application/json");
			PrintWriter pw = response.getWriter();
			pw.write("{\"error\":\"Please Login First\"}");
			return false;
		}

		// Get user ID from session
		Long userId = (Long) session.getAttribute("userId");

		// Get Role enum from session
		Role userRole = (Role) session.getAttribute("userRole");

		// Put authentication information into request
		request.setAttribute("currentUserId", userId);
		request.setAttribute("currentUserRole", userRole);

		String path = request.getRequestURI();

		// Authorization for /api/auth routes
		if (path.startsWith("/api/users")  || path.startsWith("/api/projects")) {

			// Only GET and put or SuperAdmin is allowed
			if (!(request.getMethod().equals("GET")|| request.getMethod().equals("PUT")) && userRole != Role.SuperAdmin) {

				response.setStatus(HttpServletResponse.SC_FORBIDDEN);

				response.setContentType("application/json");

				PrintWriter pw = response.getWriter();

				pw.write("{\"error\":\"Unauthorized Route - No Permission\"}");

				return false;
			}
		}

		return true;
	}
}
