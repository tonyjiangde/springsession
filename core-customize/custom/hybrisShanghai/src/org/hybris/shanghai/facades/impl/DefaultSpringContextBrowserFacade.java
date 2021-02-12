/**
 *
 */
package org.hybris.shanghai.facades.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.hybris.shanghai.data.tree.BDetails;
import org.hybris.shanghai.data.tree.BInfo;
import org.hybris.shanghai.data.tree.Node;
import org.hybris.shanghai.data.tree.PropertyInfo;
import org.hybris.shanghai.facades.SpringContextBrowserFacade;
import org.hybris.shanghai.services.SpringContextBrowserService;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;


/**
 * @author i314119
 *
 */
public class DefaultSpringContextBrowserFacade implements SpringContextBrowserFacade
{
	private static final Logger LOG = Logger.getLogger(DefaultSpringContextBrowserFacade.class);

	private SpringContextBrowserService springContextBrowserService;

	/**
	 * @param springContextBrowserService
	 *           the springContextBrowserService to set
	 */
	@Required
	public void setSpringContextBrowserService(final SpringContextBrowserService springContextBrowserService)
	{
		this.springContextBrowserService = springContextBrowserService;
	}


	@Override
	public BDetails getPropertyInfos(final String contextid, final String beanid)
	{
		final BDetails bdetails = new BDetails();
		bdetails.setBeanid(beanid);
		bdetails.setContextid(contextid);
		final List<PropertyInfo> properties = new ArrayList<PropertyInfo>();
		bdetails.setProperties(properties);
		final LinkedHashMap<ApplicationContext, Object> beanlist = springContextBrowserService.getBean(contextid, beanid);
		if (!beanlist.isEmpty())
		{
			final Iterator<ApplicationContext> iterator = beanlist.keySet().iterator();
			while (iterator.hasNext())
			{
				final ApplicationContext ctx = iterator.next();
				final Object bean = beanlist.get(ctx);
				LOG.info("Looking at bean: " + beanid);
				if (bean != null)
				{

					final String location = springContextBrowserService.getClassLocationForBean(bean);
					bdetails.setClassloader(location);
					LOG.debug("Bean class location:" + location);

					final List<PropertyInfo> propertyInfos = springContextBrowserService.getPropertyInfos(bean);

					//Populate the beanid for the property value if a matching bean found in the container.
					springContextBrowserService.populatePropertyValueBeanId(ctx, propertyInfos);

					springContextBrowserService.populatePropertyValueContextId(ctx, propertyInfos);

					bdetails.setProperties(propertyInfos);

				}
			}
		}
		return bdetails;
	}

	@Override
	public Node findContextContainingBean(final String beanid, final String beanalias, final String beanclass)
	{
		final LinkedHashMap<ApplicationContext, LinkedHashMap<String, BeanDefinition>> result = springContextBrowserService
				.findBeanDefinition(beanid, beanalias, beanclass);
		final Node root = new Node();
		root.setLeaf(false);
		root.setExpanded(true);
		root.setText("Spring Contexts");
		final ArrayList<Node> rchildren = new ArrayList<>();
		final Iterator<ApplicationContext> iterator = result.keySet().iterator();
		ApplicationContext ctx = null;
		while (iterator.hasNext())
		{
			ctx = iterator.next();
			final Node n = new Node();
			n.setExpanded(false);
			n.setLeaf(true);
			n.setText(ctx.getId());
			n.setId(ctx.getId());
			if (ctx.getParent() != null)
			{
				n.setParent(ctx.getParent().getId());
			}
			rchildren.add(n);
		}
		root.setChildren(rchildren);
		return root;
	}

	@Override
	public ArrayList<BInfo> findBeaninContext(final String contextid, final String beanid, final String beanalias,
			final String beanclass)
	{
		BInfo bi = null;
		LOG.info("searhing beans from " + contextid);
		final ArrayList<BInfo> result = new ArrayList<BInfo>();
		final LinkedHashMap<ApplicationContext, LinkedHashMap<String, BeanDefinition>> beans = springContextBrowserService
				.findBeanDefinition(contextid, beanid, beanalias, beanclass);
		final Iterator<ApplicationContext> iterator = beans.keySet().iterator();
		while (iterator.hasNext())
		{
			final ApplicationContext ctx = iterator.next();
			final LinkedHashMap<String, BeanDefinition> bds = beans.get(ctx);
			final Iterator<String> iterator2 = bds.keySet().iterator();
			while (iterator2.hasNext())
			{
				final String beanname = iterator2.next();
				final BeanDefinition bdf = bds.get(beanname);

				bi = new BInfo();
				bi.setIsAbstract(bdf.isAbstract());
				bi.setAlias(ctx.getAliases(beanname));
				bi.setClassname(bdf.getBeanClassName());
				bi.setName(beanname);
				bi.setScope(bdf.getScope());
				bi.setIsSingleton(bdf.isSingleton());
				result.add(bi);

			}

		}
		return result;
	}

	@Override
	public ArrayList<BInfo> getBeans(final String contextid)
	{
		BInfo bi = null;
		LOG.info("getting beans from " + contextid);
		final ArrayList<BInfo> result = new ArrayList<BInfo>();
		final LinkedHashMap<ApplicationContext, LinkedHashMap<String, BeanDefinition>> beans = springContextBrowserService
				.getBeanDefinitions(contextid);
		final Iterator<ApplicationContext> iterator = beans.keySet().iterator();
		while (iterator.hasNext())
		{
			final ApplicationContext ctx = iterator.next();
			final LinkedHashMap<String, BeanDefinition> bds = beans.get(ctx);
			final Iterator<String> iterator2 = bds.keySet().iterator();
			while (iterator2.hasNext())
			{
				final String beanname = iterator2.next();
				final BeanDefinition bdf = bds.get(beanname);

				bi = new BInfo();
				bi.setIsAbstract(bdf.isAbstract());
				bi.setAlias(ctx.getAliases(beanname));
				bi.setClassname(bdf.getBeanClassName());
				bi.setName(beanname);
				bi.setScope(bdf.getScope());
				bi.setIsSingleton(bdf.isSingleton());
				result.add(bi);

			}

		}
		return result;
	}


	/*
	 * (non-Javadoc)
	 *
	 * @see org.hybris.shanghai.facades.SpringContextBrowserFacade#getAllSpringContext()
	 */
	@Override
	public Node getAllSpringContext()
	{

		final Node root = new Node();
		root.setLeaf(false);
		root.setExpanded(true);
		root.setText("Spring Contexts Hierarchy");
		final ArrayList<Node> rchildren = new ArrayList<>();

		final LinkedHashMap<String, ApplicationContext> ctxs = springContextBrowserService.getGlobalSpringContext();
		final Iterator<Entry<String, ApplicationContext>> iterator = ctxs.entrySet().iterator();
		Entry<String, ApplicationContext> entry = null;
		while (iterator.hasNext())
		{
			entry = iterator.next();
			final Node n = new Node();
			n.setExpanded(false);
			n.setLeaf(true);
			n.setText(entry.getKey());
			n.setId(entry.getValue().getId());
			if (entry.getValue().getParent() != null)
			{
				n.setParent(entry.getValue().getParent().getId());
			}
			rchildren.add(n);
		}


		final LinkedHashMap<String, HashMap<String, ApplicationContext>> webctxs = springContextBrowserService
				.getAllWebSpringContext();
		final Iterator<Entry<String, HashMap<String, ApplicationContext>>> iterator2 = webctxs.entrySet().iterator();
		Entry<String, HashMap<String, ApplicationContext>> entry2 = null;
		while (iterator2.hasNext())
		{
			entry2 = iterator2.next();
			final Node n = new Node();
			n.setExpanded(true);
			n.setLeaf(false);
			n.setText(entry2.getKey());
			final HashMap<String, ApplicationContext> hm = entry2.getValue();
			final Iterator<Entry<String, ApplicationContext>> iterator3 = hm.entrySet().iterator();
			Entry<String, ApplicationContext> entry3 = null;
			final ArrayList<Node> nchildren = new ArrayList<>();
			while (iterator3.hasNext())
			{
				entry3 = iterator3.next();
				final Node n2 = new Node();
				n2.setExpanded(false);
				n2.setLeaf(true);
				n2.setText(entry3.getKey());
				if (entry3.getValue().getParent() != null)
				{
					n2.setParent(entry3.getValue().getParent().getId());
				}
				nchildren.add(n2);
			}
			n.setChildren(nchildren);
			rchildren.add(n);
		}
		root.setChildren(rchildren);
		return root;

	}

}
