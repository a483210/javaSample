package com.psd.test.sample.component;

import com.psd.test.sample.component.manager.LoadManager;
import com.psd.test.sample.utils.LoadUtils;
import mockit.Mock;
import mockit.MockUp;
import org.junit.Test;

import java.lang.reflect.Type;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

/**
 * LoadManagerTest
 *
 * @author Created by gold on 2021/4/17 11:02
 * @since 1.0.0
 */
public class LoadManagerTest4 {

    /**
     * 测试修改static方法
     */
    @Test
    public void test() {
        new MockUp<LoadUtils>() {
            @Mock
            boolean isSupportedTypes(Type type) {
                return true;
            }
        };

        String result = LoadManager.get().mapper("1");

        assertThat(result)
                .isEqualTo("1");
    }

    /**
     * 测试未修改static方法
     */
    @Test
    public void testByNotChange() {
        String result = LoadManager.get().mapper(2);

        assertThat(result)
                .isEqualTo("2");
    }

    /**
     * 测试未修改static方法且不支持的类型
     */
    @Test
    public void testByNotSupported() {
        assertThatThrownBy(() -> {
            LoadManager.get().mapper("1");
        })
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("不支持");
    }
}
