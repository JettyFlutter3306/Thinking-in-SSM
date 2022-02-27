package cn.element.ioc.core.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * FileSystem读取本地配置文件
 * 通过指定文件路径的方式读取文件信息
 * 读取一些 txt、excel 文件输出到控制台。
 */
public class FileSystemResource implements Resource {

    private final File file;
    
    private final String path;

    public FileSystemResource(File file) {
        this.file = file;
        this.path = file.getPath();
    }

    public FileSystemResource(String path) {
        this.file = new File(path);
        this.path = path;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return new FileInputStream(file);
    }

    public final String getPath() {
        return path;
    }
}
