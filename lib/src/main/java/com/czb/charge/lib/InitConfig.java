package com.czb.charge.lib;

import com.czb.charge.lib.comm.utils.CzbLog;

/**
 * Author: hfyd
 * Date: 2018/12/20
 * Description:
 */
public class InitConfig {

    private boolean debug;

    private String token;

    public InitConfig(String token) {
        this.token = token;
    }

    public static class Builder{

        private boolean debug;

        private String token;

        public void setDebug(boolean debug) {
            this.debug = debug;
        }

        public InitConfig build(String token){
            if (token == null) {
                CzbLog.e(new RuntimeException("init error : token must not null"));
            }
            InitConfig config = new InitConfig(token);
            config.debug = this.debug;
            return config;
        }
    }
}
