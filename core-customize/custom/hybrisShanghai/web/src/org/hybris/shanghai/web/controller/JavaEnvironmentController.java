/**
 *
 */
package org.hybris.shanghai.web.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import javax.servlet.http.HttpServletRequest;

import org.hybris.shanghai.facades.JavaClassLoaderFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;


/**
 * @author i314119
 *
 */
@Controller
public class JavaEnvironmentController
{
	private JavaClassLoaderFacade javaClassLoaderFacade;

	/**
	 * @param javaClassLoaderFacade
	 *           the javaClassLoaderFacade to set
	 */
	@Autowired
	public void setJavaClassLoaderFacade(final JavaClassLoaderFacade javaClassLoaderFacade)
	{
		this.javaClassLoaderFacade = javaClassLoaderFacade;
	}

	@RequestMapping(value = "/urlstree", method = RequestMethod.GET)
	public void getClassLoaderURLsTree(final HttpServletRequest request, final PrintWriter pw)
	{

		final ObjectMapper mapper = new ObjectMapper();
		try
		{
			final String json = mapper.writeValueAsString(javaClassLoaderFacade.getURLs());
			pw.write(json);
			pw.flush();
			pw.close();
		}
		catch (final JsonGenerationException e)
		{
			// YTODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (final JsonMappingException e)
		{
			// YTODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (final IOException e)
		{
			// YTODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@RequestMapping(value = "/platformclassurls", method = RequestMethod.GET)
	public void getPlatformClassLoaderURLs(final HttpServletRequest request, final PrintWriter pw)
	{

		final ObjectMapper mapper = new ObjectMapper();
		try
		{
			final String json = mapper.writeValueAsString(javaClassLoaderFacade.getPlatformURLs());
			pw.write(json);
			pw.flush();
			pw.close();
		}
		catch (final JsonGenerationException e)
		{
			// YTODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (final JsonMappingException e)
		{
			// YTODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (final IOException e)
		{
			// YTODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	@RequestMapping(value = "/webappclassurls", method = RequestMethod.GET)
	public void getWebappClassLoaderURLs(final HttpServletRequest request, final PrintWriter pw)
	{

		final ObjectMapper mapper = new ObjectMapper();
		try
		{
			final String json = mapper.writeValueAsString(javaClassLoaderFacade.getWebAppURLs());
			pw.write(json);
			pw.flush();
			pw.close();
		}
		catch (final JsonGenerationException e)
		{
			// YTODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (final JsonMappingException e)
		{
			// YTODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (final IOException e)
		{
			// YTODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@RequestMapping(value = "/findresource", params =
	{ "name" })
	public void showAttributesAJAX(@RequestParam(value = "name")
	final String name, final PrintWriter pw) throws UnsupportedEncodingException
	{
		final String cn = URLDecoder.decode(name, "UTF-8");
		final ObjectMapper mapper = new ObjectMapper();
		try
		{
			final String json = mapper.writeValueAsString(javaClassLoaderFacade.findClass(cn));
			pw.write(json);
			pw.flush();
			pw.close();
		}
		catch (final JsonGenerationException e)
		{
			// YTODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (final JsonMappingException e)
		{
			// YTODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (final IOException e)
		{
			// YTODO Auto-generated catch block
			e.printStackTrace();
		}
	}





}
