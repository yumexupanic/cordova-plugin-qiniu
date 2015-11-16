package com.cordova.qiniu.yumemor.plugin;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.cordova.qiniu.yumemor.util.StrUtils;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;

import android.util.Log;

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
	
	/**
	 * 状态(批量上传和单个上传)
	 */
	private boolean flag;
	
	/**
	 * 上传成功文件
	 */
	private JSONArray pushs;
	
	/**
	 *  上传文件个数
	 */
	private int push_count = 0;
	
	@Override
	public void initialize(CordovaInterface cordova, CordovaWebView webView) {
		pushs = new JSONArray();
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

	private void uploadFile(JSONArray args) throws JSONException {
		flag = false;
		String prefix = args.optJSONObject(0).getString("prefix");
		String filePath = args.optJSONObject(0).getString("filePath");
		String name = StrUtils.appendPrefix(prefix, StrUtils.getFileName(filePath));	//获取文件名称 添加前缀
		uploadManager.put(new File(filePath), name, QiniuKey.UPLOAD_TOKEN, this, null);
	}
	
	private void uploadArrayFile(JSONArray args) throws JSONException{
		flag = true;
		JSONObject jsons = args.optJSONObject(0);
		String prefix = jsons.getString("prefix");
		JSONArray filePaths = jsons.optJSONArray("filePaths");
		push_count = filePaths.length();
		for(int i = 0 ; i < filePaths.length() ; i++){
			String filePath = filePaths.optString(i);
			String name = StrUtils.appendPrefix(prefix, StrUtils.getFileName(filePath));	//获取文件名称 添加前缀
			uploadManager.put(new File(filePath), name, QiniuKey.UPLOAD_TOKEN, this,null);	//开始上传
		}

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
		if(flag){	//批量上传
			pushs.put(jsonObject);
			Log.e("count", pushs.length()+"");
			Log.e("tokal", push_count+"");
			if(pushs.length() >= push_count){
				boolean isError = false;
				for(int i = 0 ; i < pushs.length() ; i++){
					if(pushs.optJSONObject(i).optInt("status") != 200){
						callbackContext.error(jsonObject);
						isError = true;
						break;
					}
				}
				if(!isError){
					callbackContext.success(pushs);
				}
				pushs = new JSONArray();
				push_count = 0;
			}
		}else{	//单个上传
			if (status == 200) {
				callbackContext.success(jsonObject);
			} else {
				callbackContext.error(jsonObject);
			}
		}

	}

}
