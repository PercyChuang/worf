package orj.worf.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.io.IOUtils;

public final class ZipUtils {
    private static final int bufferLen = 1024 * 8;

    /**
     * 压缩文件
     *
     * @param srcData 被压缩的数据
     * @param zipFileName 压缩文件的名字
     * @throws IOException
     */
    public static byte[] doCompress(final byte[] srcData, final String zipFileName) throws IOException {
        ZipArchiveOutputStream out = null;
        try {
            ByteArrayOutputStream bout = new ByteArrayOutputStream(bufferLen);
            out = new ZipArchiveOutputStream(bout);
            ZipArchiveEntry entry = new ZipArchiveEntry(zipFileName);
            entry.setSize(srcData.length);
            out.putArchiveEntry(entry);
            IOUtils.write(srcData, out);
            out.closeArchiveEntry();
            out.finish();
            byte[] result = bout.toByteArray();
            return result;
        } finally {
            IOUtils.closeQuietly(out);
        }
    }

    /**
     * 压缩文件
     *
     * @param srcFile 被压缩的数据
     * @param zipFileName 压缩文件的名字
     * @throws IOException
     */
    public static byte[] doCompress(final File srcFile, final String zipFileName) throws IOException {
        if (srcFile == null || !srcFile.exists() || srcFile.isDirectory()) {
            throw new IllegalArgumentException("srcFile[" + srcFile.getAbsolutePath()
                    + "] must exist and cannot be a directory.");
        }
        ZipArchiveOutputStream out = null;
        InputStream in = null;
        try {
            in = new BufferedInputStream(new FileInputStream(srcFile), bufferLen);
            ByteArrayOutputStream bout = new ByteArrayOutputStream(bufferLen);
            out = new ZipArchiveOutputStream(bout);
            ZipArchiveEntry entry = new ZipArchiveEntry(zipFileName);
            entry.setSize(srcFile.length());
            out.putArchiveEntry(entry);
            IOUtils.copy(in, out);
            out.closeArchiveEntry();
            out.finish();
            return bout.toByteArray();
        } finally {
            IOUtils.closeQuietly(in);
            IOUtils.closeQuietly(out);
        }
    }

    /**
     * 压缩文件
     *
     * @param srcFile 被压缩的原文件,不能为null
     * @param destFile 压缩后的文件,不能为null,且所在的目录必须已经创建完毕
     * @throws IOException
     */
    public static void doCompress(final File srcFile, final File destFile) throws IOException {
        if (srcFile == null || !srcFile.exists() || srcFile.isDirectory()) {
            throw new IllegalArgumentException("srcFile[" + srcFile.getAbsolutePath()
                    + "] must exist and cannot be a directory.");
        }
        if (destFile == null || destFile.exists() && destFile.isDirectory()) {
            throw new IllegalArgumentException("destFile[" + destFile == null ? "null" : destFile.getAbsolutePath()
                    + "] cannot be empty and cannot be a directory.");
        }
        ZipArchiveOutputStream out = null;
        InputStream in = null;
        try {
            in = new BufferedInputStream(new FileInputStream(srcFile), bufferLen);
            if (!destFile.exists()) {
                destFile.createNewFile();
            }
            out = new ZipArchiveOutputStream(new BufferedOutputStream(new FileOutputStream(destFile), bufferLen));
            ZipArchiveEntry entry = new ZipArchiveEntry(srcFile.getName());
            entry.setSize(srcFile.length());
            out.putArchiveEntry(entry);
            IOUtils.copy(in, out);
            out.closeArchiveEntry();
            out.finish();
        } finally {
            IOUtils.closeQuietly(in);
            IOUtils.closeQuietly(out);
        }
    }

    /**
     * 解压文件,不支持对整个文件夹的解压
     *
     * @param is 被解压数据的输入流
     * @param destFile 解压后的文件路径
     * @throws IOException
     */
    public static void doDecompressToFile(final InputStream src, final File destFile) throws IOException {
        ZipArchiveInputStream in = null;
        try {
            in = new ZipArchiveInputStream(new BufferedInputStream(src, bufferLen));
            while (in.getNextZipEntry() != null) {
                OutputStream os = null;
                try {
                    os = new BufferedOutputStream(new FileOutputStream(destFile), bufferLen);
                    IOUtils.copy(in, os);
                } finally {
                    IOUtils.closeQuietly(os);
                }
            }
        } finally {
            IOUtils.closeQuietly(in);
        }
    }

    /**
     * 解压文件
     *
     * @param srcFile 被解压的压缩文件
     * @param destDir 解压后的文件所存放的文件夹路径
     * @throws IOException
     */
    public static void doDecompress(final File srcFile, final File destDir) throws IOException {
        if (srcFile == null || !srcFile.exists() || srcFile.isDirectory()) {
            throw new IllegalArgumentException("srcFile[" + srcFile.getAbsolutePath()
                    + "] must exist and cannot be a directory.");
        }
        ZipArchiveInputStream is = null;
        try {
            is = new ZipArchiveInputStream(new BufferedInputStream(new FileInputStream(srcFile), bufferLen));
            ZipArchiveEntry entry = null;
            while ((entry = is.getNextZipEntry()) != null) {
                if (entry.isDirectory()) {
                    File directory = new File(destDir, entry.getName());
                    directory.mkdirs();
                } else {
                    OutputStream os = null;
                    try {
                        os = new BufferedOutputStream(new FileOutputStream(new File(destDir, entry.getName())),
                                bufferLen);
                        IOUtils.copy(is, os);
                    } finally {
                        IOUtils.closeQuietly(os);
                    }
                }
            }
        } finally {
            IOUtils.closeQuietly(is);
        }
    }

}
