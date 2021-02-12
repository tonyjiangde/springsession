/**
 *
 */
package org.hybris.shanghai.data.tree;

import java.beans.PropertyDescriptor;


/**
 * @author i314119
 *
 */
public class PropertyInfo
{
	private String propertyname;
	private String propertyclass;
	private String propertydeclaringclass;

	/**
	 * @return the propertydeclaringclass
	 */
	public String getPropertydeclaringclass()
	{
		return propertydeclaringclass;
	}

	/**
	 * @param propertydeclaringclass
	 *           the propertydeclaringclass to set
	 */
	public void setPropertydeclaringclass(final String propertydeclaringclass)
	{
		this.propertydeclaringclass = propertydeclaringclass;
	}

	/**
	 * @return the propertyclass
	 */
	public String getPropertyclass()
	{
		return propertyclass;
	}

	/**
	 * @param propertyclass
	 *           the propertyclass to set
	 */
	public void setPropertyclass(final String propertyclass)
	{
		this.propertyclass = propertyclass;
	}


	private Object propertyValue;
	private boolean isReadOnly;
	private int propertyId;
	private PropertyDescriptor propertyDescriptor;


	/**
	 * @return the propertyDescriptor
	 */
	public PropertyDescriptor getPropertyDescriptor()
	{
		return propertyDescriptor;
	}

	/**
	 * @param propertyDescriptor
	 *           the propertyDescriptor to set
	 */
	public void setPropertyDescriptor(final PropertyDescriptor propertyDescriptor)
	{
		this.propertyDescriptor = propertyDescriptor;
	}

	/**
	 * @return the propertyValueContextId
	 */
	public String getPropertyValueContextId()
	{
		return propertyValueContextId;
	}

	/**
	 * @param propertyValueContextId
	 *           the propertyValueContextId to set
	 */
	public void setPropertyValueContextId(final String propertyValueContextId)
	{
		this.propertyValueContextId = propertyValueContextId;
	}


	/**
	 * The context name for the context containing the bean.
	 */
	private String propertyValueContextName;

	/**
	 * @return the propertyValueContextName
	 */
	public String getPropertyValueContextName()
	{
		return propertyValueContextName;
	}

	/**
	 * @param propertyValueContextName
	 *           the propertyValueContextName to set
	 */
	public void setPropertyValueContextName(final String propertyValueContextName)
	{
		this.propertyValueContextName = propertyValueContextName;
	}



	/**
	 * The context id of the context containing the bean.
	 */
	private String propertyValueContextId;



	/**
	 * The bean id of the property value bean.
	 */
	private String propertyValueBeanId;

	/**
	 * @return the propertyId
	 */
	public int getPropertyId()
	{
		return propertyId;
	}

	/**
	 * @return the propertyValueBeanId
	 */
	public String getPropertyValueBeanId()
	{
		return propertyValueBeanId;
	}

	/**
	 * @param propertyValueBeanId
	 *           the propertyValueBeanId to set
	 */
	public void setPropertyValueBeanId(final String propertyValueBeanId)
	{
		this.propertyValueBeanId = propertyValueBeanId;
	}


	/**
	 * @param propertyId
	 *           the propertyId to set
	 */
	public void setPropertyId(final int propertyId)
	{
		this.propertyId = propertyId;
	}

	/**
	 * @return the propertyname
	 */
	public String getPropertyname()
	{
		return propertyname;
	}

	/**
	 * @param propertyname
	 *           the propertyname to set
	 */
	public void setPropertyname(final String propertyname)
	{
		this.propertyname = propertyname;
	}

	/**
	 * @return the propertyValue
	 */
	public Object getPropertyValue()
	{
		return propertyValue;
	}

	/**
	 * @param propertyValue
	 *           the propertyValue to set
	 */
	public void setPropertyValue(final Object propertyValue)
	{
		this.propertyValue = propertyValue;
	}

	/**
	 * @return the isReadOnly
	 */
	public boolean isReadOnly()
	{
		return isReadOnly;
	}

	/**
	 * @param isReadOnly
	 *           the isReadOnly to set
	 */
	public void setReadOnly(final boolean isReadOnly)
	{
		this.isReadOnly = isReadOnly;
	}
}
