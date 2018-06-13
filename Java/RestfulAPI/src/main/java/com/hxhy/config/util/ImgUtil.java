package com.hxhy.config.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import sun.misc.BASE64Decoder;

@SuppressWarnings("restriction")
public class ImgUtil {
	
	/**
	 * base64字符串转化成图片
	 * @param FilePath
	 * @param StringBase64
	 */
	public static String generateImage(String imgStr,String prefix,Integer... name){   //对字节数组字符串进行Base64解码并生成图片  
        Integer index = 0;
		if (imgStr == null) {//图像数据为空  
        	return null;  
        } 
        if(name.length > 0) index = name[0];
        String suffix = imgStr.substring(imgStr.indexOf("data:image/")+"data:image/".length(), imgStr.indexOf(";base64"));
        imgStr = imgStr.replaceAll("data:image/(.+);base64,", ""); 
/*        Pattern pattern = Pattern.compile("data:image/(.+);base64,");
        Matcher matcher = pattern.matcher(imgStr);
        if (matcher.find())
        {
        	System.out.println(matcher.group(0));
            System.out.println(matcher.group(1));
            System.out.println(matcher.group(2));
            System.out.println(matcher.group(3));
            System.out.println(matcher.group(4));
            
        }*/
		
        try {  //Base64解码  
            byte[] b = Base64.getDecoder().decode(imgStr);  
            for(int i=0;i<b.length;++i) {  
                if(b[i]<0) {//调整异常数据  
                    b[i]+=256;  
                }  
            }
            long dateTime = System.currentTimeMillis();
		 	String fileName =  "upload" + File.separator + dateTime + (index < 10 ? "-0" + index : "-" + index )+"."+suffix;
		 	prefix = prefix + fileName;
            OutputStream out = new FileOutputStream(prefix);      
            out.write(b);  
            out.flush();  
            out.close();  
            return prefix;  
        } catch (Exception e) {  
        	e.printStackTrace();
            return null;  
        }  
    }
	
	/**
	 * 删除单个文件
	 * 
	 * @param sPath
	 *            被删除文件的文件名
	 * @return 单个文件删除成功返回true，否则返回false
	 */
	public static boolean deleteFile(String sPath) {
		boolean flag = false;
		File file = new File(sPath);
		// 路径为文件且不为空则进行删除
		if (file.isFile() && file.exists()) {
			file.delete();
			flag = true;
		}
		return flag;
	}
	/**
	 * 从微信服务器下载图片
	 */
	public static String downloadImage(String serverId, String prefix,Integer... name) {
		System.out.println("开始下载.....");
		Integer index = 0;
		if(name.length > 0) index = name[0];
		String url = "http://file.api.weixin.qq.com/cgi-bin/media/get";
//	    String access_token = AccessTokenApi.getAccessToken().getAccessToken();
		String access_token = "zD-wjPjU-lGTyAFUpirFgYyZtmYPUAhoHTutOwLoy2tx8zXvxuT2WvgLYSGGUCvFqDKPWAG3lWXKdMJxttIkUfW1GnhNB4tU9_xFzKpVnLA6ZQiY_ANKRnWVfQ-Zy0G2VACaADAMRT";
		String param = "access_token=" + access_token + "&media_id=" + serverId;
		String urlNameString = url + "?" + param;
		URL realUrl;
		try {
			realUrl = new URL(urlNameString);
			// 打开和URL之间的连接
			HttpURLConnection connection = (HttpURLConnection) realUrl
					.openConnection();
			connection.setRequestMethod("GET");
			int responseCode = connection.getResponseCode();
			System.out.println("GET Response Code :: " + responseCode);
			System.out.println("访问API" + serverId);
			// 建立实际的连接
			connection.connect();
			long dateTime = System.currentTimeMillis();
			String fileName = "upload" + File.separator + dateTime + (index < 10 ? "-0" + index : "-" + index )+".jpg";
			prefix = prefix + fileName;
			OutputStream out = new FileOutputStream(prefix);

			/*
			 * BufferedReader in = new BufferedReader(new InputStreamReader(
			 * connection.getInputStream()));
			 */
			InputStream in = connection.getInputStream();
			byte[] b = new byte[2048];
			int length;

			while ((length = in.read(b)) != -1) {
				out.write(b, 0, length);
			}
			// out.flush();
			in.close();
			out.close();
			System.out.println("下载完成.....");
			return fileName;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}
		return "";

	}

}
