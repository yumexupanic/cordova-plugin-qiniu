package com.cordova.qiniu.yumemor.plugin;

/**
 * @author yy
 * 
 *         七牛云配置
 */
public interface QiniuKey {
	/**
	 * 	上传Token
	 */
	//Token生成算法：http://developer.qiniu.com/docs/v6/api/overview/security.html#upload-token
	//Token在线生成地址：http://jsfiddle.net/gh/get/extjs/4.2/icattlecoder/jsfiddle/tree/master/uptoken
	String UPLOAD_TOKEN = "BB9QNdZo45ZlUI2QbSjh2NQxQFKDMQWEUPitOu9k:UpZPDG8TJ3lZe5R2HrTwbMc8sLg=:eyJzY29wZSI6InFjZGVtbyIsImRlYWRsaW5lIjoxNDQ3MzU1Mzk5fQ==";
	/**
	 * 文件前缀分隔符
	 */
	String FILE_PREFIX_SEPARATOR = "/";
}
