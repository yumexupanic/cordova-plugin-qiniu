package org.cordova.plugin.qiniu;

/**
 * @author yumemor
 * 
 *         七牛云配置
 */
public interface QiniuKey {
	/**
	 * 	上传Token
	 */
	//Token生成算法：http://developer.qiniu.com/docs/v6/api/overview/security.html#upload-token
	//Token在线生成地址：http://jsfiddle.net/gh/get/extjs/4.2/icattlecoder/jsfiddle/tree/master/uptoken

	String UPLOAD_TOKEN = "aTibxa7eU0wyp0ZYzDfxHksTvk9wor0I1DKgEwp1:mN-DVcJD9vZRD-yrAZ4FX6Fm9L8=:eyJzY29wZSI6InRlc3QiLCJkZWFkbGluZSI6MTQ3MjczNjE4Mn0=";
	/**
	 * 文件前缀分隔符
	 */
	String FILE_PREFIX_SEPARATOR = "/";
}
