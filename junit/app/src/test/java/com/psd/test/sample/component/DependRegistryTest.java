package com.psd.test.sample.component;

import com.psd.test.sample.model.bean.DeployBean;
import com.psd.test.sample.model.bean.DeployLayer;
import javafx.util.Pair;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

/**
 * DependRegistryTest
 *
 * @author Created by gold on 2021/4/16 15:02
 * @since 1.0.0
 */
public class DependRegistryTest {

    @Test
    public void testDoDepend() {
        List<DeployBean> deploys = new ArrayList<>();

        deploys.add(new DeployBean("app-user", ""));
        deploys.add(new DeployBean("app-live", "", Collections.singletonList("app-user")));
        deploys.add(new DeployBean("app-message", "", Collections.singletonList("app-user")));
        deploys.add(new DeployBean("app-action", "", Arrays.asList("app-user", "app-live")));
        deploys.add(new DeployBean("app-cms", "", Arrays.asList("app-user", "app-live", "app-message", "app-action")));

        Pair<Map<String, Integer>, List<DeployLayer>> pair = DependRegistry.create(deploys).doDepend();

        Map<String, Integer> dependentLayers = pair.getKey();

        assertThat(dependentLayers)
                .hasSize(5)
                .containsEntry("app-user", 0)
                .containsEntry("app-live", 1)
                .containsEntry("app-message", 1)
                .containsEntry("app-action", 2)
                .containsEntry("app-cms", 3);

        List<DeployLayer> layers = pair.getValue();

        assertThat(layers)
                .hasSize(4);

        assertThat(layers.get(0))
                .hasFieldOrPropertyWithValue("layer", 0)
                .hasFieldOrPropertyWithValue("deploys", Collections.singletonList(deploys.get(0)));

        assertThat(layers.get(1))
                .hasFieldOrPropertyWithValue("layer", 1)
                .hasFieldOrPropertyWithValue("deploys", Arrays.asList(deploys.get(2), deploys.get(1)));

        assertThat(layers.get(2))
                .hasFieldOrPropertyWithValue("layer", 2)
                .hasFieldOrPropertyWithValue("deploys", Collections.singletonList(deploys.get(3)));

        assertThat(layers.get(3))
                .hasFieldOrPropertyWithValue("layer", 3)
                .hasFieldOrPropertyWithValue("deploys", Collections.singletonList(deploys.get(4)));
    }

    @Test
    public void testDoDependByCircular() {
        List<DeployBean> deploys = new ArrayList<>();

        deploys.add(new DeployBean("app-user", "", Collections.singletonList("app-message")));
        deploys.add(new DeployBean("app-live", "", Collections.singletonList("app-user")));
        deploys.add(new DeployBean("app-message", "", Collections.singletonList("app-live")));

        assertThatThrownBy(() -> {
            DependRegistry.create(deploys).doDepend();
        })
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("循环依赖");
    }

    @Test
    public void testDoDependByNotExistsDepend() {
        List<DeployBean> deploys = new ArrayList<>();

        deploys.add(new DeployBean("app-user", ""));
        deploys.add(new DeployBean("app-live", "", Collections.singletonList("app-message")));

        assertThatThrownBy(() -> {
            DependRegistry.create(deploys).doDepend();
        })
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("空");
    }
}