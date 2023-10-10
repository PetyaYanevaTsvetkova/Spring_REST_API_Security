package coherentsolutions.airapp.beanpostprocessor;

import lombok.RequiredArgsConstructor;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.test.util.ReflectionTestUtils;

@RequiredArgsConstructor
public class MockAnswerCallRealMethod implements Answer<Object> {
    private final Object bean;
    public Object answer(final InvocationOnMock invocation) {
        return ReflectionTestUtils.invokeMethod(
                this.bean,
                invocation.getMethod().getName(),
                ((Invocation) invocation).getRawArguments()
        );
    }

}
