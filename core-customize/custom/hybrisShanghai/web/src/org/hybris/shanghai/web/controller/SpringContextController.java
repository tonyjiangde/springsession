/**
 *
 */
package org.hybris.shanghai.web.controller;

import java.io.IOException;
import java.util.Base64;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.hybris.shanghai.facades.SpringContextBrowserFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;


/**
 * @author i314119
 *
 */
@Controller
public class SpringContextController
{
	private SpringContextBrowserFacade springContextBrowserFacade;

	/**
	 * @param springContextBrowserFacade
	 *           the springContextBrowserFacade to set
	 */
	@Autowired
	public void setSpringContextBrowserFacade(final SpringContextBrowserFacade springContextBrowserFacade)
	{
		this.springContextBrowserFacade = springContextBrowserFacade;
	}

	@RequestMapping(value =
	{ "/springcontext" }, method = RequestMethod.GET)
	@ResponseBody
	public String browseSpringContext(final HttpServletRequest request)
	{

		final ObjectMapper mapper = new ObjectMapper();
		try
		{
			final String json = mapper.writeValueAsString(springContextBrowserFacade.getAllSpringContext());
			return json;
		}
		catch (final JsonGenerationException e)
		{
			// YTODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		catch (final JsonMappingException e)
		{
			// YTODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		catch (final IOException e)
		{
			// YTODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}

	@RequestMapping(value =
	{ "/springcontext/{contextId}" }, method = RequestMethod.GET)
	@ResponseBody
	public String browseBeansinSpringContext(@PathVariable("contextId")
	final String contextId, final HttpServletRequest request)
	{

		final ObjectMapper mapper = new ObjectMapper();
		try
		{
			final byte[] decodedBytes = Base64.getDecoder().decode(contextId);
			final String decodedString = new String(decodedBytes);

			final String json = mapper.writeValueAsString(springContextBrowserFacade.getBeans(decodedString));
			return json;
		}
		catch (final JsonGenerationException e)
		{
			// YTODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		catch (final JsonMappingException e)
		{
			// YTODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		catch (final IOException e)
		{
			// YTODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}

	@RequestMapping(value =
	{ "/springcontext/search" }, method = RequestMethod.GET)
	@ResponseBody
	public String searchBean(@RequestParam(value = "beanid", defaultValue = StringUtils.EMPTY)
	final String beanid, @RequestParam(value = "alias", defaultValue = StringUtils.EMPTY)
	final String alias, @RequestParam(value = "class", defaultValue = StringUtils.EMPTY)
	final String beanclass, final HttpServletRequest request)
	{

		final ObjectMapper mapper = new ObjectMapper();
		try
		{

			final String json = mapper
					.writeValueAsString(springContextBrowserFacade.findContextContainingBean(beanid, alias, beanclass));
			return json;
		}
		catch (final JsonGenerationException e)
		{
			// YTODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		catch (final JsonMappingException e)
		{
			// YTODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		catch (final IOException e)
		{
			// YTODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}

	@RequestMapping(value =
	{ "/springcontext/search/{contextId}" }, method = RequestMethod.GET)
	@ResponseBody
	public String searchBean(@PathVariable("contextId")
	final String contextId, @RequestParam(value = "beanid", defaultValue = StringUtils.EMPTY)
	final String beanid, @RequestParam(value = "alias", defaultValue = StringUtils.EMPTY)
	final String alias, @RequestParam(value = "class", defaultValue = StringUtils.EMPTY)
	final String beanclass, final HttpServletRequest request)
	{

		final ObjectMapper mapper = new ObjectMapper();
		try
		{
			final byte[] decodedBytes = Base64.getDecoder().decode(contextId);
			final String decodedcontextid = new String(decodedBytes);
			final String json = mapper
					.writeValueAsString(springContextBrowserFacade.findBeaninContext(decodedcontextid, beanid, alias, beanclass));
			return json;
		}
		catch (final JsonGenerationException e)
		{
			// YTODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		catch (final JsonMappingException e)
		{
			// YTODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		catch (final IOException e)
		{
			// YTODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}

	@RequestMapping(value =
	{ "/springcontext/{contextId}/{beanId}" }, method = RequestMethod.GET)
	@ResponseBody
	public String getBeanDetailsinSpringContext(@PathVariable("contextId")
	final String contextId, @PathVariable("beanId")
	final String beanId, final HttpServletRequest request)
	{

		final ObjectMapper mapper = new ObjectMapper();
		try
		{
			byte[] decodedBytes = Base64.getDecoder().decode(contextId);
			final String decodedcontextid = new String(decodedBytes);
			decodedBytes = Base64.getDecoder().decode(beanId);
			final String decodedbeanid = new String(decodedBytes);
			final String json = mapper
					.writeValueAsString(springContextBrowserFacade.getPropertyInfos(decodedcontextid, decodedbeanid));
			return json;
		}
		catch (final JsonGenerationException e)
		{
			// YTODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		catch (final JsonMappingException e)
		{
			// YTODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		catch (final IOException e)
		{
			// YTODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}
}
