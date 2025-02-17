package org.example.codec.compress;

import org.example.exception.BrickException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * @author chenqian
 * @date 2024/12/25 19:55
 **/
public class GzipCompressor implements Compressor {
    @Override
    public byte[] compress(byte[] bytes) {
        try(ByteArrayOutputStream bos = new ByteArrayOutputStream();){
            GZIPOutputStream gzipOutputStream = new GZIPOutputStream(bos);
            gzipOutputStream.write(bytes);
            gzipOutputStream.flush();
            gzipOutputStream.finish();
            return bos.toByteArray();
        }catch (Exception e){
            throw new BrickException("gzip compress error", e);
        }
    }

    @Override
    public byte[] decompress(byte[] bytes) {
        try(ByteArrayOutputStream bos = new ByteArrayOutputStream();){
            GZIPInputStream gzipInputStream = new GZIPInputStream(new ByteArrayInputStream(bytes));
            byte[] buffer = new byte[1024];
            int len;
            while ((len = gzipInputStream.read(buffer)) != -1) {
                bos.write(buffer, 0, len);
            }
            return bos.toByteArray();
        }catch (Exception e){
            throw new BrickException("gzip decompress error", e);
        }
    }
}
