package com.psd.aop.sample;

import com.psd.aop.sample.bean.AptBean;
import com.psd.aop.sample.bean.AptBeanImpl;
import com.psd.aop.sample.bean.AsmBean;
import com.psd.aop.sample.bean.AstBean;
import com.psd.aop.sample.utils.JavassistUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * application
 *
 * @author Created by gold on 2021/3/14 19:01
 */
public class MainApplication {

    public static void main(String[] args) throws Exception {
        AptBean aptBean = new AptBeanImpl();

        System.out.printf("AptBean [%s]%n", aptBean.getParams());

        AstBean astBean = new AstBean();

        System.out.printf("AstBean [%s]%n", astBean.getParams());

        AptBean javassistBean = JavassistUtils.createProxyObject(AptBean.class);

        System.out.printf("JavassistBean [%s]%n", javassistBean.getParams());

        AsmBean asmBean = new AsmBean();

        System.out.printf("AsmBean [%s]%n", asmBean.getParams());

        URL url = MainApplication.class.getResource("/");
        //为非jar包时执行

        if (url != null && "file".equals(url.getProtocol())) {
            System.out.println("\n开始执行jar");

            String mainPath = url.getPath();
            String path = getPath(mainPath, 4);

            String filePath = path + "/libs/script.sh";
            if (!new File(filePath).exists()) {
                System.out.println("script.sh文件不存在");
                return;
            }

            runCmd("chmod a+x " + filePath);
            runCmd(filePath);
        }
    }

    private static String getPath(String path, int number) {
        if (number <= 0) {
            return path;
        }

        int index = path.lastIndexOf("/");
        if (index >= 0) {
            return getPath(path.substring(0, index), number - 1);
        }

        return path;
    }

    private static void runCmd(String cmd) throws Exception {
        Process ps = Runtime.getRuntime().exec(cmd, null);

        BufferedReader br = new BufferedReader(new InputStreamReader(ps.getInputStream(), StandardCharsets.UTF_8));
        String line;
        while ((line = br.readLine()) != null) {
            System.out.println(line);
        }

        br.close();
        ps.waitFor();
    }
}