## 支持平台

* Android

## 实现

1. [注册](https://portal.qiniu.com/signup)七牛帐号，完成标准用户认证 
2. 创建空间(这里如果选择了私有空间，那么下载方式会有不同)
3. 安装插件：
```cmd
	cordova plugins add cordova-plugin-qiniu
```

## 说明
找到插件目录，修改配置文件：

**QiniuKey.java**

```java
package com.cordova.qiniu.yumemor.plugin;

/**
 * @author yy
 * 
 *         七牛云配置
 */
public interface QiniuKey {
	/**
	 * 	上传Token
	 * 	在这里配置你的Token
	 */
	String UPLOAD_TOKEN = "";
	/**
	 * 文件前缀分隔符
	 */
	String FILE_PREFIX_SEPARATOR = "/";
}

```

Token生成算法：http://developer.qiniu.com/docs/v6/api/overview/security.html#upload-token

Token在线生成地址：http://jsfiddle.net/gh/get/extjs/4.2/icattlecoder/jsfiddle/tree/master/uptoken

文件前缀分隔符代表	`前缀 + 文件前缀分隔符 + 文件名`

> 由于Android项目容易被反编译，所以这里建议只放token，不要放ak以及sk，token最好在服务器上面获取。

## 使用

在js中入如下代码启动上传：

```javascript
	var options = new Object();
	options.prefix = "hh";	//文件前缀
	options.filePath = "test"	//文件完整路径
	window.plugins.QiniuUpload.upload(options,function(re){
    alert("success:" + re);
	},function(re){
		alert("error:" + re);
	})
```

关于文件完整路径，代表的是一个Anrdoid资源路径：`data/package/files/文件名称`

当文件上传成功之后会返回文件的完整名称

下载路径生成方案：http://developer.qiniu.com/docs/v6/api/overview/dn/security.html

这个时候如果之前选择的是`私有空间`，那么需要发送文件名称到后台通过`AK`和`SK`获取私有文件下载路径，

导入七牛提供的JavaSDK，导入jar包到libs

java服务端代码：

```java
Auth auth = Auth.create(FileConfig.QNY_ACCESSKEY, FileConfig.QNY_SECRETKEY);
////http://7xo27g.com1.z0.glb.clouddn.com/demo.txt
String url = auth.privateDownloadUrl(域名+文件名称);
	
```

## 演示

>我这里是配合`cordova-plugin-audio-recorder-api`插件进行录音上传

**设备加载完成**

![image](http://7xnxsw.com1.z0.glb.clouddn.com/cordova-qiniu-plugin/Screenshot_2015-11-12-17-16-10.png)

**开始录音**

![iamge](http://7xnxsw.com1.z0.glb.clouddn.com/cordova-qiniu-plugin/Screenshot_2015-11-12-17-17-16.png)

**停止录音，开始上传**

![image](http://7xnxsw.com1.z0.glb.clouddn.com/cordova-qiniu-plugin/Screenshot_2015-11-12-17-20-40.png)

**上传成功**

![image](http://7xnxsw.com1.z0.glb.clouddn.com/cordova-qiniu-plugin/Screenshot_2015-11-12-17-17-38.png)

**查看七牛空间**

![image](http://7xnxsw.com1.z0.glb.clouddn.com/cordova-qiniu-plugin/QQ截图20151111150111.png)

## 更新内容

### 1.0.6

* 支持七牛云批量上传 
``` javascript
var options = new Object();
options.prefix = "abc";
options.filePaths = new Array();
options.filePaths.push("data:/xxxx");

window.plugins.QiniuUpload.uploads(options,function(re){
	console.log(re);
},function(re){
	console.log(re);						
});
```
* 修改返回参数为json，不需要手动转化
* 删除Result对象

### 1.0.7

解决中文文件上传问题。

Demo下载：http://7xnxsw.com1.z0.glb.clouddn.com/cordova-qiniu-plugin/captureAudio.zip