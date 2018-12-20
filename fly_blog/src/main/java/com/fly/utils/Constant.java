package com.fly.utils;

/**
 * Created by xiang.wei on 2018/12/13
 *
 * @author xiang.wei
 */
public class Constant {

// * Constant.uploadDir是要上传的路径
//     * Constant.uploadUrl是我另一个tomcat的项目路径
//     * Constant.uploadDir对应的就是这个Constant.uploadUrl的访问路径。

    public static String uploadDir = "/Volumes/Develop/Develop_software/apache-tomcat-8.5.16/webapps/upload";

    public static String uploadUrl = "http://localhost:10080/upload/";


    /**
     * 消息类型
     */
    public enum MsgType{
        SYSTEM,//系统消息

        POST,//评论文章消息

        COMMENT//评论你的评论消息
    }

    public static final int NORMAL_STATUS = 0;
    public static final int DELETE_STATUS = 1;

    public static final String EDIT_HTML_MODEL= "html";
    public static final String EDIT_MD_MODEL= "Markdown";

}
