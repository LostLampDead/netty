package com.choice.netty.serialization.problem;

import lombok.Data;

import java.io.Serializable;
import java.nio.ByteBuffer;

@Data
public class UserInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String username;

    private int userId;

    public UserInfo buildUsername(String username){
        this.username = username;
        return this;
    }

    public UserInfo buildUserId(int userId){
        this.userId = userId;
        return this;
    }

    public byte[] codeC(){
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        byte[] values = this.username.getBytes();
        buffer.putInt(values.length);
        buffer.put(values);
        buffer.putInt(this.userId);
        buffer.flip();
        byte[] result = new byte[buffer.remaining()];
        buffer.get(result);
        return result;
    }
}
