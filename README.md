## 支持平台

* Android

## 注册账号

1. [注册](https://portal.qiniu.com/signup)七牛帐号，完成标准用户认证 
2. 创建空间(Bucket)	

## 插件使用

1.安装插件：

```shell
#推荐直接把插件下载到根目录，

git clone https://github.com/yumemor/cordova-plugin-qiniu.git

配置文件修改过后 在项目根目录添加插件, 这样不需要修改配置文件后重新编译。

cordova plugins add cordova-plugin-qiniu
```

2.生成上传凭证

Token生成算法：http://developer.qiniu.com/docs/v6/api/overview/security.html#upload-token

Token在线生成地址：http://jsfiddle.net/gh/get/extjs/4.2/icattlecoder/jsfiddle/tree/master/uptoken

使用在线生成的 UploadToken 有时间限制，最长为 12 小时。


如果想生成永久的 UploadToken 需要使用七牛提供的服务端包来生成有效期为 `10年` Token。

> 由于Android项目容易被反编译，所以这里建议只放token，不要放ak以及sk。


3.修改配置文件(src/android/java/QiniuKey.java)：

```java
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
	String UPLOAD_TOKEN = "";
	/**
	 * 文件前缀分隔符
	 */
	String FILE_PREFIX_SEPARATOR = "/";
}
```

文件分隔符代表上传到七牛空间的文件前缀:

![image](http://oct8d1mqf.bkt.clouddn.com/2016-09-01-13%3A29%3A53.jpg)

如果没有设置文件前缀，文件分隔符不生效。

3.上传文件

Js调用：

```javascript

//上传参数
var options = new Object();
options.prefix = "hh";	//文件前缀
options.filePath = "/storage/emulated/0/Android/data/io.hello.cordova.cache/2013424231.jpg"	//文件完整路径

//上传文件
navigator.qiniu.upload(options,function(re){
    alert("success:" + re);
},function(re){
    console.dir(re);
    alert("error:" + re);
})

```

关于文件完整路径，代表的是一个Anrdoid资源路径
。
cordova 部分插件返回的 FILE_URL 是

```
file:///storage/emulated/0/Android/data/io.hello.cordova.cache/2013424231.jpg
```

这种情况下，插件内部已经进行处理过 依然能够正常上传

当文件上传成功之后会返回文件的完整名称。

## 演示 [点击](http://oct8d1mqf.bkt.clouddn.com/qiniuDemo.mp4)

七牛空间:
![image](http://oct8d1mqf.bkt.clouddn.com/2016-09-01-14%3A10%3A10.jpg)

七牛没有提供下载的 SDK，因为文件下载是一个标准的 HTTP GET 过程。开发者只需理解资源 URI 的组成格式即可非常方便的构建资源 URI，并在必要的时候加上[下载凭证](http://developer.qiniu.com/article/developer/security/download-token.html)，即可使用 HTTP GET 请求获取相应资源。

## 演示项目下载

http://o7k7yxkn2.bkt.clouddn.com/qiniu.zip

需要自己配置 UploadToken