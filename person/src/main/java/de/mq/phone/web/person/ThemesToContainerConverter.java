package de.mq.phone.web.person;

import java.util.Arrays;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.vaadin.data.Container;
import com.vaadin.data.util.IndexedContainer;

@Component
@ConverterQualifier(value = ConverterQualifier.Type.Theme2Container)
public class ThemesToContainerConverter  implements Converter<String[], Container> {

	@Override
	public Container convert(final String[] source) {
		 final Container ic = new IndexedContainer();
	
		Arrays.stream(source).forEach(theme -> ic.addItem(theme));
		
		return ic;
	}

}
