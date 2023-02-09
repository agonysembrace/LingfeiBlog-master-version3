package com.lingfei.exception;

import com.lingfei.enums.AppHttpCodeEnum;

/**
 * @author lingfei Wang
 * @version 1.0
 * @date 2022/12/4 13:13
 * @Decription:
 */
public class SystemException extends RuntimeException{

        private int code;

        private String msg;

        public int getCode() {
            return code;
        }

        public String getMsg() {
            return msg;
        }

        public SystemException(AppHttpCodeEnum httpCodeEnum) {
            super(httpCodeEnum.getMsg());
            this.code = httpCodeEnum.getCode();
            this.msg = httpCodeEnum.getMsg();
        }


}
