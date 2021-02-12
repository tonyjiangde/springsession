/**
 *
 */
package org.hybris.shanghai.services.impl;

import de.hybris.bootstrap.config.ConfigUtil;
import de.hybris.bootstrap.config.ExtensionInfo;
import de.hybris.platform.core.Registry;

import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletContext;

import org.apache.catalina.Container;
import org.apache.log4j.Logger;
import org.hybris.shanghai.data.tree.PropertyInfo;
import org.hybris.shanghai.services.SpringContextBrowserService;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.util.StringUtils;


/**
 * @author i314119
 *
 */
public class DefaultSpringContextBrowserService implements SpringContextBrowserService
{

	private static final Logger LOG = Logger.getLogger(DefaultSpringContextBrowserService.class);


	LinkedHashMap<String, ApplicationContext> getAllSpringRootWebCtxs(final ServletContext servletCtx)
	{
		final LinkedHashMap<String, ApplicationContext> result = new LinkedHashMap();
		final Enumeration<String> en = servletCtx.getAttributeNames();
		while (en.hasMoreElements())
		{
			final String name = en.nextElement();
			final Object att = servletCtx.getAttribute(name);
			if (att instanceof org.springframework.context.ApplicationContext)
			{

				LOG.info("found spring web context:" + att.getClass().getName() + "--" + ((ApplicationContext) att).getDisplayName());
				LOG.info("has bean count:" + ((ApplicationContext) att).getBeanDefinitionCount());
				/*
				 * final String[] beanNames = ((AbstractRefreshableWebApplicationContext)
				 * att).getBeanFactory().getBeanDefinitionNames(); for (final String n : beanNames) { try { final
				 * BeanDefinition bdf = ((AbstractRefreshableWebApplicationContext) att).getBeanFactory()
				 * .getBeanDefinition(n); LOG.info("Bean:" + n + "| classname:" + bdf.getBeanClassName() + "| singleton:" +
				 * bdf.isSingleton() + "| abstract:" + bdf.isAbstract() + "| alias:" + ((ApplicationContext)
				 * att).getAliases(n)); } catch (final Exception e) { e.printStackTrace(); LOG.error("Bean:" + n +
				 * " has no definition."); }
				 *
				 * }
				 */
				/*
				 * String[] beanNamesForType =
				 * ((ApplicationContext)att).getBeanFactory().getBeanNamesForType(java.lang.Object.class, true, true);
				 *
				 * System.out.println("getBeanFactory:" + beanNamesForType.length); for(String n:beanNamesForType){ try{
				 * BeanDefinition bdf = ((ApplicationContext)att).getBeanFactory().getBeanDefinition(n);
				 * System.out.println("Bean:" +
				 * n+"| classname:"+bdf.getBeanClassName()+"| singleton:"+bdf.isSingleton()+"| abstract:"+bdf.isAbstract()
				 * +"| alias:"+((ApplicationContext)att).getAliases(n)); }catch(Exception e){
				 * System.out.println("Bean:"+n+" has no definition."); }
				 *
				 *
				 * }
				 */

				result.put(((ApplicationContext) att).getId(), (ApplicationContext) att);
				/*
				 * ApplicationContext parent = att.getParent(); while(parent!=null){
				 * System.out.println("found spring  context:" + parent.class.name+"--"+parent.getDisplayName());
				 * System.out.println("has bean count:" + parent.getBeanDefinitionCount());
				 * result.put(parent.getDisplayName(),parent); parent=parent.getParent();
				 */

			}
		}

		return result;
	}



	@Override
	public LinkedHashMap<String, ApplicationContext> getGlobalSpringContext()
	{
		final LinkedHashMap<String, ApplicationContext> result = new LinkedHashMap();
		result.put(Registry.getSingletonGlobalApplicationContext().getId(), Registry.getSingletonGlobalApplicationContext());
		result.put(Registry.getCoreApplicationContext().getId(), Registry.getCoreApplicationContext());
		return result;
	}


	@Override
	public LinkedHashMap<ApplicationContext, LinkedHashMap<String, BeanDefinition>> getBeanDefinitions(final String contextid)
	{

		final LinkedHashMap<ApplicationContext, LinkedHashMap<String, BeanDefinition>> result = new LinkedHashMap<>();
		final LinkedHashMap<String, ApplicationContext> ctxs = getAllSpringContext();
		if (ctxs.containsKey(contextid))
		{
			final AbstractApplicationContext ctx = (AbstractApplicationContext) ctxs.get(contextid);
			result.put(ctx, getBeanDefinitions(ctx));
		}
		return result;
	}


	@Override
	public LinkedHashMap<ApplicationContext, LinkedHashMap<String, Object>> getBeans(final String contextid)
	{

		final LinkedHashMap<ApplicationContext, LinkedHashMap<String, Object>> result = new LinkedHashMap<>();
		final LinkedHashMap<String, ApplicationContext> ctxs = getAllSpringContext();
		if (ctxs.containsKey(contextid))
		{
			final AbstractApplicationContext ctx = (AbstractApplicationContext) ctxs.get(contextid);
			result.put(ctx, getBeans(ctx));
		}
		return result;
	}


	@Override
	public void populatePropertyValueBeanId(final ApplicationContext context, final List<PropertyInfo> propertyInfos)
	{

		for (final Iterator iterator = propertyInfos.iterator(); iterator.hasNext();)
		{
			final PropertyInfo propertyInfo = (PropertyInfo) iterator.next();
			final Object propertyValue = propertyInfo.getPropertyValue();
			if (propertyValue != null)
			{
				//Last two parameters are boolean includeNonSingletons, boolean allowEagerInit.
				final Map<String, ? extends Object> matchingBeans = BeanFactoryUtils.beansOfTypeIncludingAncestors(context,
						propertyValue.getClass(), false, false);

				final Set<?> entrySet = matchingBeans.entrySet();
				for (final Iterator iterator2 = entrySet.iterator(); iterator2.hasNext();)
				{
					final Map.Entry<String, ?> beanEntry = (Entry<String, ?>) iterator2.next();
					//We found the bean in the container.
					if (beanEntry.getValue() == propertyValue)
					{
						final String beanId = beanEntry.getKey();
						propertyInfo.setPropertyValueBeanId(beanId);
						//Skip remaining beans.
						continue;
					}

				}
			}


		}

	}

	@Override
	public void populatePropertyValueContextId(final ApplicationContext context, final List<PropertyInfo> propertyInfos)
	{

		for (final Iterator iterator = propertyInfos.iterator(); iterator.hasNext();)
		{
			final PropertyInfo propertyInfo = (PropertyInfo) iterator.next();
			final String beanId = propertyInfo.getPropertyValueBeanId();
			if (beanId != null)
			{
				final ApplicationContext beanDefinitionContext = findContextWhereBeanIsDefined(context, beanId);
				if (beanDefinitionContext != null)
				{
					propertyInfo.setPropertyValueContextId(beanDefinitionContext.getId());
					propertyInfo.setPropertyValueContextName(beanDefinitionContext.getDisplayName());
				}

			}


		}

	}

	/**
	 * Recursively find which context the bean has been defined.
	 */
	private ApplicationContext findContextWhereBeanIsDefined(final ApplicationContext context, final String beanId)
	{
		if (context != null && !context.containsLocalBean(beanId))
		{
			return findContextWhereBeanIsDefined(context.getParent(), beanId);
		}
		else
		{
			return context;
		}

	}

	@Override
	public List<PropertyInfo> getPropertyInfos(final Object bean)
	{

		final List<PropertyInfo> properties = new ArrayList<PropertyInfo>();

		for (final Field field : bean.getClass().getDeclaredFields())
		{
			try
			{

				final PropertyInfo pi = new PropertyInfo();
				pi.setPropertyname(field.getName());
				pi.setPropertydeclaringclass(field.getType().getName());
				pi.setPropertyclass("nul");
				field.setAccessible(true);
				Object value;
				value = field.get(bean);
				if (value != null)
				{
					pi.setPropertyclass(value.getClass().getName());
				}
				properties.add(pi);
			}
			catch (final IllegalArgumentException e)
			{
				// YTODO Auto-generated catch block
				e.printStackTrace();
			}
			catch (final IllegalAccessException e)
			{
				// YTODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		/*
		 * final PropertyDescriptor[] propertyDescriptors = PropertyUtils.getPropertyDescriptors(bean);
		 *
		 * for (int i = 0; i < propertyDescriptors.length; i++) { if (propertyDescriptors[i].getReadMethod() != null) {
		 * final PropertyInfo pInfo = new PropertyInfo(); pInfo.setPropertyname(propertyDescriptors[i].getName()); try {
		 *
		 * pInfo.setPropertyValue(propertyDescriptors[i].getReadMethod().invoke(bean)); if
		 * (propertyDescriptors[i].getWriteMethod() != null) { pInfo.setReadOnly(false); } else { pInfo.setReadOnly(true);
		 * } pInfo.setPropertyId(i); pInfo.setPropertyDescriptor(propertyDescriptors[i]); properties.add(pInfo); } catch
		 * (final Exception e) { LOG.error(e); } }
		 *
		 * }
		 */

		return properties;

	}

	@Override
	public LinkedHashMap<ApplicationContext, LinkedHashMap<String, BeanDefinition>> getAllBeanDefinitions()
	{
		final LinkedHashMap<ApplicationContext, LinkedHashMap<String, BeanDefinition>> result = new LinkedHashMap<ApplicationContext, LinkedHashMap<String, BeanDefinition>>();
		final LinkedHashMap<String, ApplicationContext> ctxs = getAllSpringContext();
		final Iterator<String> iterator = ctxs.keySet().iterator();
		while (iterator.hasNext())
		{
			final String name = iterator.next();
			final ApplicationContext ctx = ctxs.get(name);
			final LinkedHashMap<String, BeanDefinition> bs = this.getBeanDefinitions((AbstractApplicationContext) ctx);
			result.put(ctx, bs);

		}
		return result;
	}

	@Override
	public LinkedHashMap<ApplicationContext, LinkedHashMap<String, Object>> getAllBeans()
	{
		final LinkedHashMap<ApplicationContext, LinkedHashMap<String, Object>> result = new LinkedHashMap<ApplicationContext, LinkedHashMap<String, Object>>();
		final LinkedHashMap<String, ApplicationContext> ctxs = getAllSpringContext();
		final Iterator<String> iterator = ctxs.keySet().iterator();
		while (iterator.hasNext())
		{
			final String name = iterator.next();
			final ApplicationContext ctx = ctxs.get(name);
			final LinkedHashMap<String, Object> bs = this.getBeans((AbstractApplicationContext) ctx);
			result.put(ctx, bs);

		}
		return result;
	}

	@Override
	public LinkedHashMap<ApplicationContext, BeanDefinition> getBeanDefinition(final String contextid, final String beanid)
	{

		final LinkedHashMap<ApplicationContext, BeanDefinition> result = new LinkedHashMap<>();
		final LinkedHashMap<String, ApplicationContext> ctxs = getAllSpringContext();
		if (ctxs.containsKey(contextid))
		{
			final AbstractApplicationContext ctx = (AbstractApplicationContext) ctxs.get(contextid);
			final LinkedHashMap<String, BeanDefinition> bs = getBeanDefinitions(ctx);
			for (final Iterator iterator = bs.keySet().iterator(); iterator.hasNext();)
			{
				final String beanname = (String) iterator.next();
				if (beanname.toLowerCase().equals(beanid.toLowerCase()))
				{
					result.put(ctx, bs.get(beanname));
					break;
				}
			}

		}
		return result;
	}


	@Override
	public LinkedHashMap<ApplicationContext, Object> getBean(final String contextid, final String beanid)
	{

		final LinkedHashMap<ApplicationContext, Object> result = new LinkedHashMap<>();
		final LinkedHashMap<String, ApplicationContext> ctxs = getAllSpringContext();
		if (ctxs.containsKey(contextid))
		{
			final AbstractApplicationContext ctx = (AbstractApplicationContext) ctxs.get(contextid);
			final LinkedHashMap<String, Object> bs = getBeans(ctx);
			for (final Iterator iterator = bs.keySet().iterator(); iterator.hasNext();)
			{
				final String beanname = (String) iterator.next();
				if (beanname.toLowerCase().equals(beanid.toLowerCase()))
				{
					result.put(ctx, bs.get(beanname));
					break;
				}
			}

		}
		return result;
	}

	@Override
	public LinkedHashMap<ApplicationContext, LinkedHashMap<String, BeanDefinition>> findBeanDefinition(final String beanid,
			final String beanalias, final String beanclass)
	{

		final LinkedHashMap<ApplicationContext, LinkedHashMap<String, BeanDefinition>> result = new LinkedHashMap<>();
		final LinkedHashMap<String, ApplicationContext> ctxs = getAllSpringContext();
		for (final Iterator iterator2 = ctxs.keySet().iterator(); iterator2.hasNext();)
		{
			final String cname = (String) iterator2.next();
			//final ApplicationContext ctx = ctxs.get(cname);
			result.putAll(findBeanDefinition(cname, beanid, beanalias, beanclass));

		}
		return result;
	}

	@Override
	public LinkedHashMap<ApplicationContext, LinkedHashMap<String, BeanDefinition>> findBeanDefinition(final String contextid,
			final String beanid, final String beanalias, final String beanclass)
	{
		final Pattern beanidpattern = Pattern.compile(beanid);
		final Pattern beanaliaspattern = Pattern.compile(beanalias);
		final Pattern beanclasspattern = Pattern.compile(beanclass);

		final LinkedHashMap<ApplicationContext, LinkedHashMap<String, BeanDefinition>> result = new LinkedHashMap<>();
		final LinkedHashMap<String, ApplicationContext> ctxs = getAllSpringContext();
		if (ctxs.containsKey(contextid))
		{
			final AbstractApplicationContext ctx = (AbstractApplicationContext) ctxs.get(contextid);
			final LinkedHashMap<String, BeanDefinition> bs = getBeanDefinitions(ctx);
			final LinkedHashMap<String, BeanDefinition> bresult = new LinkedHashMap<String, BeanDefinition>();
			for (final Iterator iterator = bs.keySet().iterator(); iterator.hasNext();)
			{
				final String beanname = (String) iterator.next();
				if (!StringUtils.isEmpty(beanid))
				{
					final Matcher m = beanidpattern.matcher(beanname);
					if (m.find())
					{
						bresult.put(beanname, bs.get(beanname));

					}
				}

				if (!StringUtils.isEmpty(beanalias))
				{

					final String[] alias = ctx.getAliases(beanname);
					for (final String a : alias)
					{
						final Matcher m = beanaliaspattern.matcher(a);
						if (m.find())
						{
							bresult.put(beanname, bs.get(beanname));

						}
						break;
					}

				}
				if (!StringUtils.isEmpty(beanclass))
				{
					if (bs.get(beanname).getBeanClassName() != null)
					{
						final Matcher m = beanclasspattern.matcher(bs.get(beanname).getBeanClassName());
						if (m.find())
						{
							bresult.put(beanname, bs.get(beanname));

						}
					}

				}
			}
			if (!bresult.isEmpty())
			{
				result.put(ctx, bresult);
			}


		}
		return result;
	}

	@Override
	public LinkedHashMap<String, BeanDefinition> getBeanDefinitions(final AbstractApplicationContext ctx)
	{

		final String[] beanNames = ctx.getBeanFactory().getBeanDefinitionNames();
		final LinkedHashMap<String, BeanDefinition> beanDefinitions = new LinkedHashMap<String, BeanDefinition>();
		LOG.info("getBeanDefinitionNames:" + beanNames.length);
		for (final String n : beanNames)
		{
			try
			{
				final BeanDefinition bdf = ctx.getBeanFactory().getMergedBeanDefinition(n);
				System.out.println("Bean:" + n + "| classname:" + bdf.getBeanClassName() + "| singleton:" + bdf.isSingleton()
						+ "| abstract:" + bdf.isAbstract() + "| alias:" + ctx.getAliases(n));


				beanDefinitions.put(n, bdf);
			}
			catch (final Exception e)
			{
				System.out.println("Bean:" + n + " has no definition.");
			}

		}
		return beanDefinitions;
	}

	@Override
	public LinkedHashMap<String, Object> getBeans(final AbstractApplicationContext ctx)
	{

		final String[] beanNames = ctx.getBeanFactory().getBeanDefinitionNames();
		final LinkedHashMap<String, Object> beans = new LinkedHashMap<String, Object>();
		LOG.info("getBeanDefinitionNames:" + beanNames.length);
		for (final String n : beanNames)
		{
			try
			{


				beans.put(n, ctx.getBean(n));
			}
			catch (final Exception e)
			{
				System.out.println("Bean:" + n + " has no definition.");
			}

		}
		return beans;
	}

	@Override
	public LinkedHashMap<String, HashMap<String, ApplicationContext>> getAllWebSpringContext()
	{
		try
		{
			final List<ExtensionInfo> allExtensions = ConfigUtil.getPlatformConfig(Registry.class).getExtensionInfosInBuildOrder();
			final LinkedHashMap<String, HashMap<String, ApplicationContext>> result = new LinkedHashMap();
			final ArrayList<ExtensionInfo> extensionsWithWebModule = new ArrayList<ExtensionInfo>();
			for (final Iterator iterator = allExtensions.iterator(); iterator.hasNext();)
			{
				final ExtensionInfo extensionInfo = (ExtensionInfo) iterator.next();

				if (extensionInfo.getWebModule() != null)
				{
					extensionsWithWebModule.add(extensionInfo);
					LOG.info("found extension having web module:" + extensionInfo.getName());

				}
			}

			for (final Iterator iterator = extensionsWithWebModule.iterator(); iterator.hasNext();)
			{

				final ExtensionInfo extensionInfo = (ExtensionInfo) iterator.next();
				final String webRoot = extensionInfo.getWebModule().getWebRoot();
				final Field privateField = org.apache.catalina.startup.Bootstrap.class.getDeclaredField("daemon");
				privateField.setAccessible(true);
				final org.apache.catalina.startup.Bootstrap daemon = (org.apache.catalina.startup.Bootstrap) privateField.get(null);
				final Field privateField2 = org.apache.catalina.startup.Bootstrap.class.getDeclaredField("catalinaDaemon");
				privateField2.setAccessible(true);
				final org.apache.catalina.startup.Catalina cata = (org.apache.catalina.startup.Catalina) privateField2.get(daemon);


				final Container engine = cata.getServer().findService("Catalina").getContainer();
				if (engine.findChild("localhost").findChild(webRoot) != null)
				{
					final ServletContext ctx = ((org.apache.catalina.core.StandardContext) engine.findChild("localhost")
							.findChild(webRoot)).getServletContext();
					final HashMap<String, ApplicationContext> springctx = getAllSpringRootWebCtxs(ctx);
					for (final Map.Entry<String, ApplicationContext> entry : springctx.entrySet())
					{
						LOG.info(entry.getKey() + " => " + entry.getValue() + " => " + entry.getValue().getParent());

					}
					result.put(webRoot, springctx);

				}
				//dumpSpringRootWebCtxs(boServletCtx,true)
			}
			LOG.info("found total web spring context count: " + result.size());
			// YTODO Auto-generated method stub
			return result;
		}
		catch (final Exception e)
		{
			return null;
		}


	}



	/*
	 * (non-Javadoc)
	 *
	 * @see org.hybris.shanghai.services.SpringContextBrowserService#getAllSpringContext()
	 */
	@Override
	public LinkedHashMap<String, ApplicationContext> getAllSpringContext()
	{
		try
		{
			final List<ExtensionInfo> allExtensions = ConfigUtil.getPlatformConfig(Registry.class).getExtensionInfosInBuildOrder();
			final LinkedHashMap<String, ApplicationContext> result = new LinkedHashMap();
			result.put(Registry.getSingletonGlobalApplicationContext().getId(), Registry.getSingletonGlobalApplicationContext());
			result.put(Registry.getCoreApplicationContext().getId(), Registry.getCoreApplicationContext());
			final ArrayList<ExtensionInfo> extensionsWithWebModule = new ArrayList<ExtensionInfo>();
			for (final Iterator iterator = allExtensions.iterator(); iterator.hasNext();)
			{
				final ExtensionInfo extensionInfo = (ExtensionInfo) iterator.next();

				if (extensionInfo.getWebModule() != null)
				{
					extensionsWithWebModule.add(extensionInfo);
					LOG.info("found extension having web module:" + extensionInfo.getName());

				}
			}

			for (final Iterator iterator = extensionsWithWebModule.iterator(); iterator.hasNext();)
			{
				final ExtensionInfo extensionInfo = (ExtensionInfo) iterator.next();
				final String webRoot = extensionInfo.getWebModule().getWebRoot();
				final Field privateField = org.apache.catalina.startup.Bootstrap.class.getDeclaredField("daemon");
				privateField.setAccessible(true);
				final org.apache.catalina.startup.Bootstrap daemon = (org.apache.catalina.startup.Bootstrap) privateField.get(null);
				final Field privateField2 = org.apache.catalina.startup.Bootstrap.class.getDeclaredField("catalinaDaemon");
				privateField2.setAccessible(true);
				final org.apache.catalina.startup.Catalina cata = (org.apache.catalina.startup.Catalina) privateField2.get(daemon);


				final Container engine = cata.getServer().findService("Catalina").getContainer();
				if (engine.findChild("localhost").findChild(webRoot) != null)
				{
					final ServletContext ctx = ((org.apache.catalina.core.StandardContext) engine.findChild("localhost")
							.findChild(webRoot)).getServletContext();
					final HashMap<String, ApplicationContext> springctx = getAllSpringRootWebCtxs(ctx);
					for (final Map.Entry<String, ApplicationContext> entry : springctx.entrySet())
					{
						LOG.info(entry.getKey() + " => " + entry.getValue() + " => " + entry.getValue().getParent());

					}
					result.putAll(springctx);

				}

				//dumpSpringRootWebCtxs(boServletCtx,true)


			}
			LOG.info("found total spring context count: " + result.size());
			// YTODO Auto-generated method stub
			return result;
		}
		catch (final Exception e)
		{
			return null;
		}

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.hybris.shanghai.services.SpringContextBrowserService#getBean(java.lang.String)
	 */
	@Override
	public LinkedHashMap<ApplicationContext, BeanDefinition> getBeanDefinition(final String beanid)
	{
		final LinkedHashMap<ApplicationContext, BeanDefinition> result = new LinkedHashMap<ApplicationContext, BeanDefinition>();
		final LinkedHashMap<ApplicationContext, LinkedHashMap<String, BeanDefinition>> allbeans = this.getAllBeanDefinitions();
		for (final Iterator iterator = allbeans.keySet().iterator(); iterator.hasNext();)
		{
			final ApplicationContext ctx = (ApplicationContext) iterator.next();
			for (final Iterator iterator2 = allbeans.get(ctx).keySet().iterator(); iterator2.hasNext();)
			{
				final String bid = (String) iterator2.next();
				if (bid.toLowerCase().equals(beanid))
				{
					result.put(ctx, allbeans.get(ctx).get(bid));
					break;
				}

			}
		}
		// YTODO Auto-generated method stub
		return result;
	}


	@Override
	public String getClassLocationForBean(final Object bean)
	{
		final Class klass = bean.getClass();
		final URL location = klass.getResource('/' + klass.getName().replace('.', '/') + ".class");

		String locationStr = null;
		//Location is null means the class might have been modified by cglib.
		if (location != null)
		{
			locationStr = location.toString();
			final int index = locationStr.indexOf(".jar!");
			if (index != -1)
			{
				locationStr = locationStr.substring(0, index + 4);
			}

		}
		return locationStr;
	}

	@Override
	public LinkedHashMap<ApplicationContext, Object> getBean(final String beanid)
	{
		final LinkedHashMap<ApplicationContext, Object> result = new LinkedHashMap<ApplicationContext, Object>();
		final LinkedHashMap<ApplicationContext, LinkedHashMap<String, Object>> allbeans = this.getAllBeans();
		for (final Iterator iterator = allbeans.keySet().iterator(); iterator.hasNext();)
		{
			final ApplicationContext ctx = (ApplicationContext) iterator.next();
			for (final Iterator iterator2 = allbeans.get(ctx).keySet().iterator(); iterator2.hasNext();)
			{
				final String bid = (String) iterator2.next();
				if (bid.toLowerCase().equals(beanid))
				{
					result.put(ctx, allbeans.get(ctx).get(bid));
					break;
				}

			}
		}
		// YTODO Auto-generated method stub
		return result;
	}

}
