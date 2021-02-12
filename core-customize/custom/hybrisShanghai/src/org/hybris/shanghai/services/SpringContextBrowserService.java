/**
 *
 */
package org.hybris.shanghai.services;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import org.hybris.shanghai.data.tree.PropertyInfo;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;


/**
 * @author i314119
 *
 */
public interface SpringContextBrowserService
{
	public LinkedHashMap<String, ApplicationContext> getAllSpringContext();

	public LinkedHashMap<String, ApplicationContext> getGlobalSpringContext();

	public LinkedHashMap<String, HashMap<String, ApplicationContext>> getAllWebSpringContext();

	public LinkedHashMap<String, BeanDefinition> getBeanDefinitions(final AbstractApplicationContext ctx);

	public LinkedHashMap<ApplicationContext, LinkedHashMap<String, BeanDefinition>> findBeanDefinition(final String beanid,
			final String beanalias, final String beanclass);

	public LinkedHashMap<ApplicationContext, LinkedHashMap<String, BeanDefinition>> getAllBeanDefinitions();

	public LinkedHashMap<ApplicationContext, BeanDefinition> getBeanDefinition(final String contextid, final String beanid);

	public LinkedHashMap<ApplicationContext, BeanDefinition> getBeanDefinition(final String beanid);

	public LinkedHashMap<ApplicationContext, LinkedHashMap<String, BeanDefinition>> findBeanDefinition(final String contextid,
			final String beanid, final String beanalias, final String beanclass);

	public LinkedHashMap<ApplicationContext, LinkedHashMap<String, BeanDefinition>> getBeanDefinitions(final String contextid);

	public LinkedHashMap<ApplicationContext, LinkedHashMap<String, Object>> getBeans(final String contextid);

	public LinkedHashMap<String, Object> getBeans(final AbstractApplicationContext ctx);

	public LinkedHashMap<ApplicationContext, LinkedHashMap<String, Object>> getAllBeans();


	public LinkedHashMap<ApplicationContext, Object> getBean(final String contextid, final String beanid);

	public LinkedHashMap<ApplicationContext, Object> getBean(final String beanid);

	public String getClassLocationForBean(final Object bean);

	public List<PropertyInfo> getPropertyInfos(final Object bean);

	public void populatePropertyValueBeanId(final ApplicationContext context, final List<PropertyInfo> propertyInfos);

	public void populatePropertyValueContextId(final ApplicationContext context, final List<PropertyInfo> propertyInfos);
}
