package com.choice.netty.serialization.problem;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;

/**
 * JAVA 序列化问题演示
 */
public class TestUserInfo {
    public static void main(String[] args) throws Exception {
        UserInfo userInfo = new UserInfo();
        userInfo.buildUsername("TOM").buildUserId(1);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(userInfo);
        objectOutputStream.flush();
        objectOutputStream.close();

        byte[] b1 = byteArrayOutputStream.toByteArray();
        System.out.println("JAVA序列化后的数据长度=" + b1.length);

        byte[] b2 = userInfo.codeC();
        System.out.println("使用二进制处理后长度=" + b2.length);
    }
}
