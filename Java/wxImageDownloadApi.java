package jzf.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import com.data.JzfDataSource;
import com.util.ComUtil;
import com.wx.sdk.api.AccessTokenApi;

public class WXImageDownloadAPI {
	@Autowired
	private JzfDataSource dataSource;

	public static void main(String[] args) {
		//serverId return by WX uploadImage API
		String serverId = "xxxxx";
		//directory to save image
		String prefix = "D://";

		downloadImage(serverId, prefix);
	}


	/**
	 * download image from weichat server
	 */
	public static String downloadImage(String serverId, String prefix) {
		System.out.println("downloading start.....");
		//weixin interface: http://file.api.weixin.qq.com/cgi-bin/media/get?access_token=ACCESS_TOKEN&media_id=MEDIA_ID
		String url = "http://file.api.weixin.qq.com/cgi-bin/media/get";
		// get accessToken
		String access_token = AccessTokenApi.getAccessToken().getAccessToken();
		String param = "access_token=" + access_token + "&media_id=" + serverId;
		String urlNameString = url + "?" + param;
		URL realUrl;
		try {
			realUrl = new URL(urlNameString);
			// create connection to URL
			HttpURLConnection connection = (HttpURLConnection) realUrl
					.openConnection();
			connection.setRequestMethod("GET");
			//open connection
			connection.connect();
			//fetch data
			InputStream in = connection.getInputStream();
			//unique filename
			long dateTime = ComUtil.getSeq();
			String fileName = "upload" + File.separator + dateTime + ".jpg";
			prefix = prefix + fileName;

			//create output stream
			OutputStream out = new FileOutputStream(prefix);

			byte[] b = new byte[2048];
			int length;

			while ((length = in.read(b)) != -1) {
				out.write(b, 0, length);
			}
			out.flush();
			in.close();
			out.close();
			System.out.println("finish.....");
			return fileName;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}
		return "";

	}
}
