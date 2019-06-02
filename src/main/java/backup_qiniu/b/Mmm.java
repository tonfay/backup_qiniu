package backup_qiniu.b;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.model.FileInfo;
import com.qiniu.util.Auth;


/**
 * 1.获取七牛文件列表
 * 2.备份原有文件 到新的bucket中
 * @author LENOVO
 *
 */
public class Mmm {
	public static void main(String[] args) {
		// 构造一个带指定Zone对象的配置类
		Configuration cfg = new Configuration(Zone.zone0());
		// ...其他参数参考类注释
		String accessKey = System.getProperty("accessKey");
		String secretKey = System.getProperty("secretKey");
		String bucket = System.getProperty("bucket");//"blog";
		String visitHost = System.getProperty("visitHost");//公网访问路径
		String savePath = System.getProperty("savePath");//保存本地路径,/结尾
		Auth auth = Auth.create(accessKey, secretKey);
		BucketManager bucketManager = new BucketManager(auth, cfg);
		//文件名前缀
		String prefix = "";
		//每次迭代的长度限制，最大1000，推荐值 1000
		int limit = 1000;
		//指定目录分隔符，列出所有公共前缀（模拟列出目录效果）。缺省值为空字符串
		String delimiter = "";
		//列举空间文件列表
		BucketManager.FileListIterator fileListIterator = bucketManager.createFileListIterator(bucket, prefix, limit, delimiter);
		while (fileListIterator.hasNext()) {
		    //处理获取的file list结果
		    FileInfo[] items = fileListIterator.next();
		    for (FileInfo item : items) {
//		        System.out.println(item.key);
		        try {
					go(item.key,visitHost,savePath);
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
//		        System.out.println(item.hash);
//		        System.out.println(item.fsize);
//		        System.out.println(item.mimeType);
//		        System.out.println(item.putTime);
//		        System.out.println(item.endUser);
		    }
		}
		
		
//		String fromBucket = "blog";
//		String fromKey = "file/2018/11/3c4928aa3c7c45bd8141b50067d8a044-v1.3.xlsx";
//		String toBucket = "blog_backup";
//		String toKey = "file/2018/11/3c4928aa3c7c45bd8141b50067d8a044-v1.3.xlsx"/*+ System.currentTimeMillis()*/;
//		Auth auth = Auth.create(accessKey, secretKey);
//		BucketManager bucketManager = new BucketManager(auth, cfg);
//		try {
//		    bucketManager.copy(fromBucket, fromKey, toBucket, toKey);
//		} catch (QiniuException ex) {
//		    //如果遇到异常，说明复制失败
//		    System.err.println(ex.code());
////		    System.err
//		}
	}
	
	private static void go(String name,String visitHost,String savePath)
			throws MalformedURLException, IOException, ProtocolException, Exception, FileNotFoundException {
		String fileName = name ;
		// new一个URL对象
		URL url = new URL(visitHost + fileName);
		System.out.println(url);
		// 打开链接
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		// 设置请求方式为"GET"
		conn.setRequestMethod("GET");
		// 超时响应时间为5秒
		conn.setConnectTimeout(5 * 1000);
		// 通过输入流获取图片数据
		InputStream inStream = conn.getInputStream();
		// 得到图片的二进制数据，以二进制封装得到数据，具有通用性
		byte[] data = readInputStream(inStream);
		// new一个文件对象用来保存图片，默认保存当前工程根目录
		
		String baseUrl = savePath;
//		System.getProperty("savePath");
		File imageFile = new File(baseUrl + fileName);
		System.out.println(imageFile);
		if(!imageFile.exists()){
			imageFile.getParentFile().mkdirs();
		}
		// 创建输出流
		FileOutputStream outStream = null;
		try {
			outStream = new FileOutputStream(imageFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 写入数据
		outStream.write(data);
		// 关闭输出流
		outStream.close();
	}

	public static byte[] readInputStream(InputStream inStream) throws Exception {
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		// 创建一个Buffer字符串
		byte[] buffer = new byte[1024];
		// 每次读取的字符串长度，如果为-1，代表全部读取完毕
		int len = 0;
		// 使用一个输入流从buffer里把数据读取出来
		while ((len = inStream.read(buffer)) != -1) {
			// 用输出流往buffer里写入数据，中间参数代表从哪个位置开始读，len代表读取的长度
			outStream.write(buffer, 0, len);
		}
		// 关闭输入流
		inStream.close();
		// 把outStream里的数据写入内存
		return outStream.toByteArray();
	}
}
