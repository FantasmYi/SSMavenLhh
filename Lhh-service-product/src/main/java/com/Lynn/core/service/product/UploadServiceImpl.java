package com.Lynn.core.service.product;

import com.Lynn.common.fdfs.FastDFSUtils;
import org.springframework.stereotype.Service;

/**
 * Created by FantasmYii on 2018/3/25.
 */
@Service("uploadService")
public class UploadServiceImpl implements UploadService {
    @Override
    public String uploadPic(byte[] pic, String name, long size) {
        return FastDFSUtils.uploadPic(pic, name, size);
    }
}
