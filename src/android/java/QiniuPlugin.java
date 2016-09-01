package org.cordova.plugin.qiniu;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;

import java.net.URLDecoder;

import android.util.Log;

/**
 * @author yumemor
 * 
 *         基于七牛云储存编写的Corodva上传插件
 */
public class QiniuPlugin extends CordovaPlugin implements UpCompletionHandler {
	/**
	 * 七牛上传管理器
	 */
	private UploadManager uploadManager;
	/**
	 * cordova回调
	 */
	private CallbackContext callbackContext;
	
	@Override
	public void initialize(CordovaInterface cordova, CordovaWebView webView) {
		uploadManager = new UploadManager();
	}

	@Override
	public boolean execute(String action, JSONArray args,CallbackContext callbackContext) throws JSONException {
		this.callbackContext = callbackContext;
		try {
			Method method = this.getClass().getDeclaredMethod(action,JSONArray.class);
			if (method != null) {
				method.invoke(this, args);
				return true;
			} else {
				callbackContext.error("Action错误!");
			}
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return false;
	}

	private void uploadFile(JSONArray args) throws JSONException,UnsupportedEncodingException {
		String prefix = args.optJSONObject(0).getString("prefix");
		String filePath = args.optJSONObject(0).getString("filePath");
		String name = StrUtils.appendPrefix(prefix, StrUtils.getFileName(filePath));	//获取文件名称 添加前缀
		filePath = URLDecoder.decode(filePath, "UTF-8");	//文件路径解码
		filePath = filePath.replace("file://","");	//去掉 file:// 路径。
		uploadManager.put(filePath, name, QiniuKey.UPLOAD_TOKEN, this, null);
	}

	@Override
	public void complete(String name, ResponseInfo info, JSONObject json) {
		int status = info.statusCode;
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("name", name);
			jsonObject.put("status", status);
			jsonObject.put("msg", info.error);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		if (status == 200) {
			callbackContext.success(jsonObject);
		} else {
			callbackContext.error(jsonObject);
		}
	}

}
