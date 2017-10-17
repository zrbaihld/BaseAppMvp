package com.zrb.baseappmvp.bean;

/**
 * Created by zrb on 2017/6/15.
 */

public class LoginBean {

    /**
     * user_id : 用户id
     * session_id : 会话id
     * user_role :  为空则选择角色  2学校管理员 3教师 4家长（未认证的默认只有公有性模块）
     */

    private String user_id;
    private String session_id;
    private String user_role;
    /**
     * true_name : 会话id
     * head_pic : 用户头像
     * host_view_img : 图片域名
     * rongyun_token : 融云聊天标识
     */

    private String true_name;
    private String head_pic;
    private String host_view_img;
    private String rongyun_token;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getSession_id() {
        return session_id;
    }

    public void setSession_id(String session_id) {
        this.session_id = session_id;
    }

    public String getUser_role() {
        return user_role;
    }

    public void setUser_role(String user_role) {
        this.user_role = user_role;
    }

    public String getTrue_name() {
        return true_name;
    }

    public void setTrue_name(String true_name) {
        this.true_name = true_name;
    }

    public String getHead_pic() {
        return head_pic;
    }

    public void setHead_pic(String head_pic) {
        this.head_pic = head_pic;
    }

    public String getHost_view_img() {
        return host_view_img;
    }

    public void setHost_view_img(String host_view_img) {
        this.host_view_img = host_view_img;
    }

    public String getRongyun_token() {
        return rongyun_token;
    }

    public void setRongyun_token(String rongyun_token) {
        this.rongyun_token = rongyun_token;
    }
}
