package com.hxhy.config.util;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import com.alibaba.fastjson.JSONObject;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Binarizer;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.EncodeHintType;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

/**
 * 二维码工具类
 */

public class QRCodeUtils {
	private static final int QRCOLOR = 0xFF000000; // 默认是黑色
	private static final int BGWHITE = 0xFFFFFFFF; // 背景颜色
	// private static final String TITLE_FONT = "宋体"; // 标题字体
	// private static final int TITLE_SIZE = 30; // 标题字体大小

	/**
	 * Web： 生成二维码
	 *
	 * @param text
	 * @param title
	 * @return
	 */
	public static BufferedImage generateQRCodeForWeb(String text, String title) {
		return getQRCodeBufferedImage(text, BarcodeFormat.QR_CODE, 400, 400, getDecodeHintType());
	}

	public static BufferedImage generateQRCodeWithLogoForWeb(File file, String logoPath) {
		try {
			BufferedImage image = ImageIO.read(file);
			return addLogo(image, logoPath);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static BufferedImage generateQRCodeWithLogoAndBGForWeb(File file, String logoPath, String bgPath) {
		try {
			BufferedImage image = generateQRCodeWithLogoForWeb(file, logoPath);
			return addBg(image, bgPath);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private static BufferedImage addBg(BufferedImage qrCodeImage, String bgPath) {
		try {
			BufferedImage bgImage = ImageIO.read(new File(bgPath));
			BufferedImage image = new BufferedImage(bgImage.getWidth(), bgImage.getHeight(),
					BufferedImage.TYPE_INT_RGB);
			Graphics2D g = image.createGraphics();
			g.drawImage(bgImage, 0, 0, bgImage.getWidth(), bgImage.getHeight(), null);

			if (image != null) {
				// 读取Logo图片
				// 设置logo的大小,本人设置为二维码图片的20%,因为过大会盖掉二维码
				int widthQRCode = qrCodeImage.getWidth(null) > image.getWidth() * 2 / 10 ? (image.getWidth() * 2 / 10)
						: qrCodeImage.getWidth(null),
						heightQRCode = qrCodeImage.getHeight(null) > image.getHeight() * 2 / 10
								? (image.getHeight() * 2 / 10)
								: qrCodeImage.getWidth(null);

				// logo放在中心
				int x = (image.getWidth() - widthQRCode) / 12;
				int y = (int) ((image.getHeight() - heightQRCode) / 1.9);

				// 开始绘制图片
				g.drawImage(qrCodeImage, x, y, widthQRCode, heightQRCode, null);
				qrCodeImage.flush();
			}

			g.dispose();
			image.flush();
			return image;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Web： 生成带 LOGO 的二维码
	 * 
	 * @param text
	 * @param title
	 * @param logoPath
	 * @return
	 */
	public static BufferedImage generateQRCodeWithLogoForWeb(String text, String title, String logoPath) {
		try {
			BufferedImage image = getQRCodeBufferedImage(text, BarcodeFormat.QR_CODE, 400, 400, getDecodeHintType());
			return addLogo(image, logoPath);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 向生成的二维码中添加图片
	 * 
	 * @param bi
	 * @param logoPic
	 * @param logoConfig
	 * @param productName
	 * @return
	 */
	private static BufferedImage addLogo(BufferedImage bi, String logoPicUrl) {
		// 读取二维码图片，并构建绘图对象
		BufferedImage image = new BufferedImage(bi.getWidth(), bi.getHeight(), BufferedImage.TYPE_INT_RGB);
		try {
			Graphics2D g = image.createGraphics();
			g.drawImage(bi, 0, 0, bi.getWidth(), bi.getHeight(), null);

			if (StringUtils.isNotEmpty(logoPicUrl)) {
				// 读取Logo图片
				BufferedImage logo = generateBufferedImage(logoPicUrl);
				// 设置logo的大小,本人设置为二维码图片的20%,因为过大会盖掉二维码
				int widthLogo = logo.getWidth(null) > image.getWidth() * 3 / 10 ? (image.getWidth() * 3 / 10)
						: logo.getWidth(null),
						heightLogo = logo.getHeight(null) > image.getHeight() * 3 / 10 ? (image.getHeight() * 3 / 10)
								: logo.getWidth(null);

				// logo放在中心
				int x = (image.getWidth() - widthLogo) / 2;
				int y = (image.getHeight() - heightLogo) / 2;

				// 开始绘制图片
				g.drawImage(logo, x, y, widthLogo, heightLogo, null);
				logo.flush();
			}

			g.dispose();
			image.flush();
			return image;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 构建初始化二维码
	 * 
	 * @param bm
	 * @return
	 */
	public static BufferedImage fileToBufferedImage(BitMatrix bm) {
		BufferedImage image = null;
		try {
			int w = bm.getWidth(), h = bm.getHeight();
			image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);

			for (int x = 0; x < w; x++) {
				for (int y = 0; y < h; y++) {
					image.setRGB(x, y, bm.get(x, y) ? 0xFF000000 : 0xFFCCDDEE);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return image;
	}

	/**
	 * 生成二维码bufferedImage图片
	 * 
	 * @param content
	 *            编码内容
	 * @param barcodeFormat
	 *            编码类型
	 * @param width
	 *            图片宽度
	 * @param height
	 *            图片高度
	 * @param hints
	 *            设置参数
	 * @return
	 */
	private static BufferedImage getQRCodeBufferedImage(String content, BarcodeFormat barcodeFormat, int width,
			int height, Map<EncodeHintType, ?> hints) {
		MultiFormatWriter multiFormatWriter = null;
		BitMatrix bm = null;
		BufferedImage image = null;
		try {
			multiFormatWriter = new MultiFormatWriter();
			// 参数顺序分别为：编码内容，编码类型，生成图片宽度，生成图片高度，设置参数
			bm = multiFormatWriter.encode(content, barcodeFormat, width, height, hints);
			int w = bm.getWidth();
			int h = bm.getHeight();
			image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);

			// 开始利用二维码数据创建Bitmap图片，分别设为黑（0xFFFFFFFF）白（0xFF000000）两色
			for (int x = 0; x < w; x++) {
				for (int y = 0; y < h; y++) {
					image.setRGB(x, y, bm.get(x, y) ? QRCOLOR : BGWHITE);
				}
			}
		} catch (WriterException e) {
			e.printStackTrace();
		}
		return image;
	}

	/**
	 * 设置二维码的格式参数
	 * 
	 * @return
	 */
	public static Map<EncodeHintType, Object> getDecodeHintType() {
		// 用于设置QR二维码参数
		Map<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>();
		// 设置QR二维码的纠错级别（H为最高级别）具体级别信息
		hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
		// 设置编码方式
		hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
		hints.put(EncodeHintType.MARGIN, 0);
		hints.put(EncodeHintType.MAX_SIZE, 350);
		hints.put(EncodeHintType.MIN_SIZE, 100);

		return hints;
	}

	private static BufferedImage generateBufferedImage(String picUrl) {
		try {
			URL url = new URL(picUrl);
			return ImageIO.read(url);
			// URLConnection con = url.openConnection();
			// // 不超时
			// con.setConnectTimeout(0);
			//
			// // 不允许缓存
			// con.setUseCaches(false);
			// con.setDefaultUseCaches(false);
			// InputStream is = con.getInputStream();
			//
			// // 先读入内存
			// ByteArrayOutputStream buf = new ByteArrayOutputStream(8192);
			// byte[] b = new byte[1024];
			// int len;
			// while ((len = is.read(b)) != -1) {
			// buf.write(b, 0, len);
			// }
			// // 读图像
			// is = new ByteArrayInputStream(buf.toByteArray());
			// return ImageIO.read(is);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static JSONObject decode(String filePath) {
		BufferedImage image;
		try {
			image = ImageIO.read(new File(filePath));
			LuminanceSource source = new BufferedImageLuminanceSource(image);
			Binarizer binarizer = new HybridBinarizer(source);
			BinaryBitmap binaryBitmap = new BinaryBitmap(binarizer);
			Map<DecodeHintType, Object> hints = new HashMap<DecodeHintType, Object>();
			hints.put(DecodeHintType.CHARACTER_SET, "UTF-8");
			Result result = new MultiFormatReader().decode(binaryBitmap, hints);// 对图像进行解码
			JSONObject content = JSONObject.parseObject(result.getText());
			return content;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}

class LogoConfig {
	// logo默认边框颜色
	public static final Color DEFAULT_BORDERCOLOR = Color.WHITE;
	// logo默认边框宽度
	public static final int DEFAULT_BORDER = 2;
	// logo大小默认为照片的1/5
	public static final int DEFAULT_LOGOPART = 5;

	private final int border = DEFAULT_BORDER;
	private final Color borderColor;
	private final int logoPart;

	/**
	 * * Creates a default config with on color {@link #BLACK} and off color *
	 * {@link #WHITE}, generating normal black-on-white barcodes.
	 */
	public LogoConfig() {
		this(DEFAULT_BORDERCOLOR, DEFAULT_LOGOPART);
	}

	public LogoConfig(Color borderColor, int logoPart) {
		this.borderColor = borderColor;
		this.logoPart = logoPart;
	}

	public Color getBorderColor() {
		return borderColor;
	}

	public int getBorder() {
		return border;
	}

	public int getLogoPart() {
		return logoPart;
	}
}
