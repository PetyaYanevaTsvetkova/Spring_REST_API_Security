package coherentsolutions.airapp.beanpostprocessor;

import coherentsolutions.airapp.service.AirportService;
import coherentsolutions.airapp.service.OrderService;
import coherentsolutions.airapp.service.WeatherService;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MockBeanPostProcessor implements BeanPostProcessor, Ordered {
    private final Logger LOGGER = LoggerFactory.getLogger(MockBeanPostProcessor.class);
    private static final List<Class<?>> CLASSES_FOR_MOCKING = List.of(
            AirportService.class,
            OrderService.class,
            WeatherService.class
    );

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (CLASSES_FOR_MOCKING.contains(bean.getClass())) {
            LOGGER.info("BEFORE " + beanName);
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {

        if (CLASSES_FOR_MOCKING.stream().anyMatch(c -> c.isInstance(bean))) {

            final Class<?> classToMock;
            try {
                classToMock = this.getClassForMocking(bean);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            final Class<?>[] interfacesForMocking;
            try {
                interfacesForMocking = this.getInterfacesForMocking(bean);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }

            final Object mockedBean;
            if (interfacesForMocking.length > 0) {
                mockedBean = Mockito.mock(
                        classToMock,
                        Mockito.withSettings()
                                .extraInterfaces(interfacesForMocking)
                                .defaultAnswer(new MockAnswerCallRealMethod(bean))
                );
            } else {
                mockedBean = Mockito.mock(classToMock, new MockAnswerCallRealMethod(bean));
            }

            LOGGER.info("AFTER: returned mocked bean: " + beanName);
            return mockedBean;
        }
        return bean;
    }

    private Class<?>[] getInterfacesForMocking(Object bean) throws ClassNotFoundException {
        Class c = Class.forName(bean.getClass().getName());
        return c.getInterfaces();
    }

    private Class<?> getClassForMocking(Object bean) throws ClassNotFoundException {
        return Class.forName(bean.getClass().getName());
    }

    @Override
    public int getOrder() {
        return Integer.MIN_VALUE;
    }

}

