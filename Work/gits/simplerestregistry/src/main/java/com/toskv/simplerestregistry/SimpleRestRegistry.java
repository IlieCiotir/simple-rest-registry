package com.toskv.simplerestregistry;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Servlet implementation class SimpleRestRegistry
 */
public class SimpleRestRegistry extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Map<String, RestServiceInfo> SERVICES = new HashMap<String, RestServiceInfo>();

	private Gson gson = new GsonBuilder().disableHtmlEscaping().create();

	/**
	 * Default constructor.
	 */
	public SimpleRestRegistry() {
	}

	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String name = request.getParameter("name");
		if (name == null) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}

		RestServiceInfo service = SERVICES.get(name);
		if (service == null) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}
		response.getWriter().append(service.getLocation());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		RestServiceInfo info = gson.fromJson(new InputStreamReader(request.getInputStream()), RestServiceInfo.class);
		if (info.getName() == null || info.getLocation() == null) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}

		RestServiceInfo existing = SERVICES.get(info.getName());
		if (existing != null) {
			response.sendError(HttpServletResponse.SC_CONFLICT);
			return;
		}

		info.setId(UUID.randomUUID());
		SERVICES.put(info.getName(), info);

		response.getWriter().append(gson.toJson(info));
	}

	/**
	 * @see HttpServlet#doPut(HttpServletRequest, HttpServletResponse)
	 */
	protected void doPut(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		RestServiceInfo info = gson.fromJson(new InputStreamReader(request.getInputStream()), RestServiceInfo.class);
		if (info.getName() == null || info.getLocation() == null || info.getId() == null) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}

		RestServiceInfo oldInfo = SERVICES.get(info.getName());
		if (!oldInfo.getId().equals(info.getId())) {
			response.sendError(HttpServletResponse.SC_FORBIDDEN);
			return;
		}

		SERVICES.put(info.getName(), info);

		response.getWriter().append(gson.toJson(info));

	}

	/**
	 * @see HttpServlet#doDelete(HttpServletRequest, HttpServletResponse)
	 */
	protected void doDelete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String name = request.getParameter("name");
		String id = request.getParameter("id");
		System.out.println(id);
		if (name == null || id == null) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}

		RestServiceInfo info = SERVICES.get(name);
		if (info == null) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}

		if (info.getId().equals(UUID.fromString(id))) {
			SERVICES.remove(name);
		} else {
			response.sendError(HttpServletResponse.SC_FORBIDDEN);
		}

	}

}
