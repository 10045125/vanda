package com.vanda.vandalibnetwork.uploadwithprogress.http;

import java.io.File;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.vanda.vandalibnetwork.daterequest.RequestManager;
import com.vanda.vandalibnetwork.staticdata.CommonDataClass;
import com.vanda.vandalibnetwork.uploadwithprogress.http.CustomMultipartEntity.ProgressListener;

public class HttpMultipartPost extends AsyncTask<String, Integer, String> {

	private Context context;
	private File[] file;
	private ProgressDialog pd;
	private long totalSize;
	private int imgId;
	private UpdateImgId mUpdateImgId;
	private String postUrl;

	public interface UpdateImgId {
		public void updateImageId(int iamgeId);
	}

	public HttpMultipartPost(Context context, String postUrl, File[] file) {
		this.context = context;
		this.file = file;
		this.postUrl = postUrl;
	}

	@Override
	protected void onPreExecute() {
		pd = new ProgressDialog(context);
		pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		pd.setMessage("图片上传中...");
		pd.setCancelable(false);
		pd.show();
	}

	@Override
	protected String doInBackground(String... params) {
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(postUrl);
		CommonDataClass cd = null;
		try {
			CustomMultipartEntity multipartContent = new CustomMultipartEntity(
					new ProgressListener() {
						@Override
						public void transferred(long num) {
							publishProgress((int) ((num / (float) totalSize) * 100));
						}
					});

			// We use FileBody to transfer an image
			for (int i = 0; i < file.length; i++)
				if (i != 0)
					multipartContent.addPart("file", new FileBody(file[i]));
				else
					multipartContent.addPart("file", new FileBody(file[i]));
			totalSize = multipartContent.getContentLength();

			// Send it
			httpPost.setEntity(multipartContent);
			HttpResponse response = httpClient.execute(httpPost,
					RequestManager.CURRENT_CONTEXT);
			String result = EntityUtils.toString(response.getEntity(),
					RequestManager.getContentEncoding(response, "utf8"));
			Log.i("file result->", result);
			Gson gson = new Gson();
			cd = gson.fromJson(result, CommonDataClass.class);
		} catch (Exception e) {
			e.printStackTrace();
			return "error";
		}

		if (cd != null && cd.error != null) {
			return cd.error;
		}
		if (cd != null && cd.IMGID != 0) {
			imgId = cd.IMGID;
			return "ok";
		}
		return "error";
	}

	public void setmUpdateImgId(UpdateImgId l) {
		mUpdateImgId = l;
	}

	@Override
	protected void onProgressUpdate(Integer... progress) {
		pd.setProgress((int) (progress[0]));
	}

	@Override
	protected void onPostExecute(String result) {
		pd.dismiss();
		if (result.equals("error")) {
			Toast.makeText(context, "网络请求失败，请检查网络连接！", Toast.LENGTH_SHORT)
					.show();
			return;
		}
		if (result.equals("require_login")) {
			Toast.makeText(context, "您没有登录！", Toast.LENGTH_SHORT).show();
			// context.startActivity(new Intent(context, LoginActivity.class));
		}
		if (result.equals("ok")) {
			Toast.makeText(context, "恭喜，照片上传成功！", Toast.LENGTH_SHORT).show();
		}
		if (mUpdateImgId != null) {
			mUpdateImgId.updateImageId(imgId);
		}
	}

	@Override
	protected void onCancelled() {
	}

}
