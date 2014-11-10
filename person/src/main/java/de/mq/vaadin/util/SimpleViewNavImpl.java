package de.mq.vaadin.util;

import java.util.Collection;




import org.springframework.aop.support.AopUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;


@Component
@Scope("session")
public class SimpleViewNavImpl implements ViewNav {

	private Navigator navigator;

	/* (non-Javadoc)
	 * @see de.mq.vaadin.util.ViewNav#create(com.vaadin.navigator.View, java.util.Collection)
	 */
	@Override
	public final void create(final View root, final Collection<View> views, final VaadinOperations vaadinOperations) {
		navigator = vaadinOperations.newNavigator();
		navigator.addView("", root);
		for (final View view : views) {
			//call @PostConstruct now ...
			view.toString();
			navigator.addView(viewNameFor(view), view);
		}
	}
	
	/* (non-Javadoc)
	 * @see de.mq.vaadin.util.ViewNav#navigateTo(java.lang.Class)
	 */
	@Override
	public final void navigateTo(Class<? extends View>  clazz, String ... params ) {
		Assert.isTrue(!ClassUtils.isCglibProxyClass(clazz) , "Class should not be a CglibProxyClass."  ) ;
		
		String parameter = "" ;
		if( params.length > 0 ) {
			parameter += "/";
			parameter += StringUtils.arrayToDelimitedString(params, "/");
		}
		
		navigator.navigateTo(StringUtils.uncapitalize(clazz.getSimpleName() ) + parameter );
		
	}

	private String viewNameFor(final View view) {
		if (AopUtils.isCglibProxy(view)) {
		
			return StringUtils.uncapitalize(AopUtils.getTargetClass(view).getSimpleName());
		}
		return StringUtils.uncapitalize(view.getClass().getSimpleName());
	}

	
}
