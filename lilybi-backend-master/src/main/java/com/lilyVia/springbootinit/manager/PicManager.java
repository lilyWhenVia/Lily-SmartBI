package com.lilyVia.springbootinit.manager;

import cn.hutool.core.io.FileUtil;
import com.lilyVia.springbootinit.common.ErrorCode;
import com.lilyVia.springbootinit.exception.BusinessException;
import com.lilyVia.springbootinit.exception.ThrowUtils;
import com.lilyVia.springbootinit.model.entity.User;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Objects;

/**
 * 用于处理图片上传保存等相关操作
 * Created by lily via on 2024/3/21 18:04
 */
public class PicManager {

    public byte[] inputStringToByte(MultipartFile pic){
        InputStream inputStream = null; // 获取输入流，可以是FileInputStream、ByteArrayInputStream等
        try {
            inputStream = pic.getInputStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        byte[] buffer = new byte[1024];
        int bytesRead;
        while (true) {
            try {
                if (!((bytesRead = inputStream.read(buffer)) != -1)) break;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            outputStream.write(buffer, 0, bytesRead);
        }

        byte[] allBytes = outputStream.toByteArray();

// 现在allBytes中包含了输入流中的所有字节数据
        return allBytes;

    }

    private void updateAvatar(MultipartFile userAvatar, User user) {
        // 非空才校验
        if (!Objects.isNull(userAvatar)){
            Boolean isPri = validateImg(userAvatar);
            ThrowUtils.throwIf(!isPri, ErrorCode.OPERATION_ERROR, "头像更新失败");
        }
        byte[] avatarBytes;
        try {
            avatarBytes = FileCopyUtils.copyToByteArray(userAvatar.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        user.setUserAvatar(avatarBytes.toString());
    }


    /**
     * 上传头像校验头像是否合法
     */
    private Boolean validateImg(MultipartFile file) {
        if (file == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "图片不能为空");
        }
        if (file.getSize() >= 20 * 1024 * 1024) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "图片大小超出最大限制");
        }
        String suffix = FileUtil.getSuffix(file.getOriginalFilename());
        final String[] ALLOW_PIC_SUFFIX = {"png", "jpg","jpeg"};
        if (!Arrays.asList(ALLOW_PIC_SUFFIX).contains(suffix)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "图片格式错误");
        }
        return true;
    }

}
