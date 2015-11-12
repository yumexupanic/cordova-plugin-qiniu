package com.cordova.qiniu.yumemor.plugin;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaArgs;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.cordova.qiniu.yumemor.bean.Result;
import com.cordova.qiniu.yumemor.util.StrUtils;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;

import android.util.Log;
import android.widget.Toast;

/**
 * @author yy
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
			Method method = this.getClass().getDeclaredMethod(action,String.class,String.class);
			if (method != null) {
				method.invoke(this, args.optJSONObject(0).getString("prefix"),args.optJSONObject(0).getString("filePath"));
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

	private void uploadFile(String prefix,String filePath) {
		String name = StrUtils.appendPrefix(prefix, StrUtils.getFileName(filePath));	//获取文件名称 添加前缀
		uploadManager.put(new File(filePath), name, QiniuKey.UPLOAD_TOKEN, this, null);
	}

	@Override
	public void complete(String name, ResponseInfo info, JSONObject json) {
		Result result = new Result();
		int status = info.statusCode;
		result.setName(name);
		result.setStatus(status);
		result.setMsg(info.error);
		if (status == 200) {
			callbackContext.success(result.toString());
		} else {
			callbackContext.error(result.getMsg());
		}
	}

}
