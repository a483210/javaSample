package com.psd.test.sample.component;

import com.psd.test.sample.component.application.Application;
import com.psd.test.sample.component.application.Parser;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.powermock.api.mockito.PowerMockito.doThrow;
import static org.powermock.api.mockito.PowerMockito.mock;

/**
 * ApplicationTest
 *
 * @author Created by gold on 2021/4/17 10:34
 * @since 1.0.0
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(Application.class)
public class ApplicationTest4 {

    /**
     * 测试修改new
     */
    @Test
    public void test() throws Exception {
        Parser parser = new Parser() {
            @Override
            public <T> String mapper(T t) {
                return "测试解析器";
            }
        };

        PowerMockito.whenNew(Parser.class)
                .withAnyArguments()
                .thenReturn(parser);

        Application application = new Application();

        String result = application.mapper(1);

        assertThat(result)
                .isEqualTo("测试解析器");
    }

    /**
     * 测试未修改new
     */
    @Test
    public void testByNotChange() {
        Application application = new Application();

        String result = application.mapper(1);

        assertThat(result)
                .isEqualTo("1");
    }

    /**
     * 测试修改new，且返回异常
     */
    @Test
    public void testByFailure() throws Exception {
        Parser parser = mock(Parser.class);
        doThrow(new NullPointerException("测试空异常")).when(parser).mapper(any());

        PowerMockito.whenNew(Parser.class)
                .withAnyArguments()
                .thenReturn(parser);

        Application application = new Application();

        assertThatThrownBy(() -> {
            application.mapper(1);
        })
                .isInstanceOf(NullPointerException.class)
                .hasMessage("测试空异常");
    }
}
