package com.example.wortgewant.Utility;

import android.media.MediaDataSource;

import java.io.IOException;

public class ByteArrayMediaDataSource extends MediaDataSource {

    private final byte[] data;

    public ByteArrayMediaDataSource(byte []data) {
        assert data != null;
        this.data = data;
    }
    @Override
    public int readAt(long position, byte[] bytes, int offset, int size) throws IOException {
        System.arraycopy(data, (int)position, bytes, offset, size);
        return size;
    }

    @Override
    public long getSize() throws IOException {
        return data.length;
    }

    @Override
    public void close() throws IOException {

    }
}
