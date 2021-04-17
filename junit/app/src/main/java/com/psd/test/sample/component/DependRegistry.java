package com.psd.test.sample.component;

import com.psd.test.sample.model.bean.DeployBean;
import com.psd.test.sample.model.bean.DeployLayer;
import javafx.util.Pair;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * DependRegistry
 *
 * @author Created by gold on 2021/4/16 14:32
 * @since 1.0.0
 */
public class DependRegistry {

    /**
     * 创建注册器
     *
     * @param deploys 部署
     */
    public static DependRegistry create(List<DeployBean> deploys) {
        return new DependRegistry(deploys.stream()
                .collect(Collectors.toMap(DeployBean::getName, it -> it)));
    }

    private final Map<String, Set<String>> dependentMap = new HashMap<>(16);
    private final Map<String, Integer> dependentLayers = new HashMap<>(16);
    private final Map<Integer, List<DeployBean>> dependentDeploys = new HashMap<>(8);

    private final Map<String, DeployBean> deploys;

    public DependRegistry(Map<String, DeployBean> deploys) {
        this.deploys = deploys;
    }

    /**
     * 解决依赖
     */
    public Pair<Map<String, Integer>, List<DeployLayer>> doDepend() {
        deploys.values().forEach(it -> {
            doDepend(it.getName());
        });

        List<DeployLayer> layers = dependentDeploys.entrySet()
                .stream()
                .map(it -> new DeployLayer(it.getKey(), it.getValue()))
                .sorted(Comparator.comparingInt(DeployLayer::getLayer))
                .collect(Collectors.toList());

        return new Pair<>(dependentLayers, layers);
    }

    private void doDepend(String name) {
        if (dependentLayers.containsKey(name)) {
            return;
        }

        DeployBean deployBean = deploys.get(name);
        if (deployBean == null) {
            throw new NullPointerException("未知异常，deploy为空");
        }

        int maxLayer = -1;

        List<String> depends = deployBean.getDepends();
        if (!CollectionUtils.isEmpty(depends)) {
            for (String depend : depends) {
                if (isDependent(name, depend)) {
                    throw new IllegalArgumentException("'$name' 和 '$depend' 之间存在循环依赖");
                }

                registerDependentService(depend, name);

                doDepend(depend);

                Integer parentLayer = dependentLayers.get(depend);
                if (parentLayer == null) {
                    throw new NullPointerException("未知异常，parentLayer为空");
                }

                maxLayer = Math.max(maxLayer, parentLayer);
            }
        }

        registerDependentLayer(maxLayer + 1, deployBean);
    }

    private void registerDependentLayer(Integer layer, DeployBean deployBean) {
        dependentDeploys.computeIfAbsent(layer, it ->
                new ArrayList<>(8)
        )
                .add(deployBean);

        dependentLayers.put(deployBean.getName(), layer);
    }

    private void registerDependentService(String name, String dependentName) {
        dependentMap.computeIfAbsent(name, it ->
                new HashSet<>(16)
        )
                .add(dependentName);
    }

    private boolean isDependent(String name, String dependentName) {
        return isDependent(name, dependentName, null);
    }

    private Boolean isDependent(String name, String dependentName, Set<String> alreadySeen) {
        if (alreadySeen != null && alreadySeen.contains(name)) {
            return false;
        }

        Set<String> dependentServices = dependentMap.get(name);
        if (dependentServices == null) {
            return false;
        }

        if (dependentServices.contains(dependentName)) {
            return true;
        }

        for (String transitiveDependency : dependentServices) {
            Set<String> already = alreadySeen != null ? alreadySeen : new HashSet<>(16);
            already.add(name);

            if (isDependent(transitiveDependency, dependentName, already)) {
                return true;
            }
        }
        return false;
    }
}
