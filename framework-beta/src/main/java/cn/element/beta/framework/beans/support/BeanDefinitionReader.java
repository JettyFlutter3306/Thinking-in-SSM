package cn.element.beta.framework.beans.support;

import cn.element.beta.framework.beans.config.BeanDefinition;
import cn.hutool.core.util.StrUtil;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * 对配置文件进行查找,读取,解析
 */
public class BeanDefinitionReader {
    
    private final List<String> registryBeanClasses = new ArrayList<>();
    
    private final Properties config = new Properties();

    /**
     * 固定位置配置文件中的key,相当于XML的规范
     */
    private static final String SCAN_PACKAGE = "scanPackage";

    public BeanDefinitionReader(String... locations) {
        // 通过URL定位找到对应的文件,然后转换为文件流
        InputStream is = this.getClass()
                             .getClassLoader()
                             .getResourceAsStream(locations[0].replace("classpath:", ""));

        try {
            config.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        
        doScanner(config.getProperty(SCAN_PACKAGE));
    }
    
    private void doScanner(String scanPackage) {
        // 转换为文件路径,实际上就是把.替换为/
        URL url = this.getClass()
                      .getClassLoader()
                      .getResource("/" + scanPackage.replaceAll("\\.", "/"));

        File classPath = new File(url.getFile());

        for (File file : classPath.listFiles()) {
            if (file.isDirectory()) {
                doScanner(scanPackage + "." + file.getName());
            } else {
                if (file.getName().endsWith(".class")) {
                    String className = (scanPackage + "." + file.getName().replace(".class", ""));
                    registryBeanClasses.add(className);
                }
            }
        }
    }
    
    public Properties getConfig() {
        return config;
    }
    
    public List<BeanDefinition> loadBeanDefinitions() {
        List<BeanDefinition> list = new ArrayList<>();

        try {
            for (String className : registryBeanClasses) {
                Class<?> beanClass = Class.forName(className);
                
                if (beanClass.isInterface()) {
                    continue;
                }
                
                list.add(doCreateBeanDefinition(StrUtil.lowerFirst(beanClass.getSimpleName()), beanClass.getName()));

                Class<?>[] interfaces = beanClass.getInterfaces();

                for (Class<?> clazz : interfaces) {
                    list.add(doCreateBeanDefinition(clazz.getName(), beanClass.getName()));
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return list;
    }

    /**
     * 把每一个配置信息解析成一个BeanDefinition
     */
    private BeanDefinition doCreateBeanDefinition(String factoryBeanName, String beanClassName) {
        BeanDefinition beanDefinition = new BeanDefinition();
        beanDefinition.setBeanClassName(beanClassName);
        beanDefinition.setFactoryBeanName(factoryBeanName);
        return beanDefinition;
    }
    
    
}
