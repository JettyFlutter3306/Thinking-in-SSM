package cn.element.ioc.core.io;

import cn.element.ioc.util.ClassUtils;
import cn.hutool.core.lang.Assert;

import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * 这一部分的实现是用于通过 ClassLoader 读取 ClassPath 下的文件信息，
 * 具体的读取过程主要是：classLoader.getResourceAsStream(path)
 */
public class ClassPathResource implements Resource {

    private final String path;

    private final ClassLoader classLoader;

    public ClassPathResource(String path) {
        this(path, (ClassLoader) null);
    }

    public ClassPathResource(String path, ClassLoader classLoader) {
        Assert.notNull(path, "Path must not be null");
        this.path = path;
        this.classLoader = (classLoader != null ? classLoader : ClassUtils.getDefaultClassLoader());
    }

    @Override
    public InputStream getInputStream() throws Exception {
        InputStream is = classLoader.getResourceAsStream(path);

        if (is == null) {
            throw new FileNotFoundException(path + " cannot be opened cuz it does not exist");
        }

        return is;
    }


}