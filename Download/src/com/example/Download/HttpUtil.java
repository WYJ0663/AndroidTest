package com.example.Download;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

public class HttpUtil {

    public final static String TAG = "HTTP";
    private final static int CONNECT_TIME = 10000;
    private final static int READ_TIME = 10000;

    /**
     * 发送post请求
     *
     * @param urlstr
     * @param map
     * @param encoding
     * @return
     * @throws IOException
     */


    public String doPost(String urlstr, Map<String, String> map, String encoding)
            throws IOException {

        StringBuilder data = new StringBuilder();
        // 数据拼接 key=value&key=value
        if (map != null && !map.isEmpty()) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                data.append(entry.getKey()).append("=");
                data.append(URLEncoder.encode(entry.getValue(), encoding));
                data.append("&");
            }
            data.deleteCharAt(data.length() - 1);
        }

        Log.i(TAG, data.toString());
        byte[] entity = data.toString().getBytes();// 生成实体数据
        URL url = new URL(urlstr);
        HttpURLConnection connection = getHttpURLConnection(urlstr, "POST");

        connection.setDoOutput(true);// 允许对外输出数据

        connection.setRequestProperty("Content-Length",
                String.valueOf(entity.length));

        OutputStream outStream = connection.getOutputStream();
        outStream.write(entity);
        if (connection.getResponseCode() == 200) {// 成功返回处理数据
            InputStream inStream = connection.getInputStream();
            byte[] number = read(inStream);
            String json = new String(number);
            return json;
        }

        return null;

    }

    public String doPost(String urlstr) throws IOException {
        return doPost(urlstr, null, "UTF-8");
    }

    public String doPost(String urlstr, Map<String, String> map)
            throws IOException {
        return doPost(urlstr, map, "UTF-8");
    }

    /**
     * 发送GET请求
     *
     * @param urlstr
     * @return
     * @throws Exception
     */
    public String doGet(String urlstr) throws Exception {
        HttpURLConnection connection = getHttpURLConnection(urlstr, "GET");

        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            InputStream inStream = connection.getInputStream();
            byte[] number = read(inStream);
            String json = new String(number);
            return json;
        }
        return null;
    }

    private HttpURLConnection getHttpURLConnection(String urlstr, String method)
            throws IOException {
        URL url = new URL(urlstr);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setConnectTimeout(CONNECT_TIME);
        connection.setReadTimeout(READ_TIME);
        connection.setRequestMethod(method);

        // 头字段
        connection.setRequestProperty("Accept", "*/*");
        connection.setRequestProperty("Accept-Charset", "UTF-8,*;q=0.5");
        connection.setRequestProperty("Accept-Encoding", "gzip,deflate");
        connection.setRequestProperty("Accept-Language", "zh-CN");
        connection.setRequestProperty("User-Agent", "Android WYJ");
        connection.setRequestProperty("Content-Type",
                "application/x-www-form-urlencoded");// 头字段

        return connection;

    }

    /**
     * 读取输入流数据 InputStream
     *
     * @param inStream
     * @return
     * @throws IOException
     */
    public static byte[] read(InputStream inStream) throws IOException {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        inStream.close();

        return outStream.toByteArray();


    }


    /**
     * 读取输入流数据 InputStream
     *
     * @param inStream
     * @return
     * @throws IOException
     */
    public static FileOutputStream readFile(InputStream inStream, File file) throws IOException {
        FileOutputStream outStream = new FileOutputStream(file);
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        inStream.close();

        return outStream;

    }

    /**
     * 发送GET请求
     *
     * @param urlstr
     * @return
     * @throws Exception
     */
    public boolean doGetFile(String urlstr, File file) throws Exception {
        HttpURLConnection connection = getHttpURLConnection(urlstr, "GET");

        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            InputStream inStream = connection.getInputStream();
            FileOutputStream number = readFile(inStream, file);
            return true;
        }
        return false;
    }


    /**
     * 发送GET请求
     *
     * @param urlstr
     * @return
     * @throws Exception
     */
    public byte[] doGetBytes(String urlstr) throws Exception {
        HttpURLConnection connection = getHttpURLConnection(urlstr, "GET");

        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            InputStream inStream = connection.getInputStream();
            byte[] number = read(inStream);
            return number;
        }
        return null;
    }

    public static Bitmap Bytes2Bimap(byte[] b) {
        if (b.length != 0) {
            return BitmapFactory.decodeByteArray(b, 0, b.length);
        } else {
            return null;
        }
    }

    /**
     * 发送文件post请求
     *
     * @param urlstr
     * @param map
     * @param files
     * @return
     * @throws IOException
     */
    public String doFilePost(String urlstr, Map<String, String> map,
                             Map<String, File> files) throws IOException {
        String BOUNDARY = "----WebKitFormBoundaryDwvXSRMl0TBsL6kW"; // 定义数据分隔线

        URL url = new URL(urlstr);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        // 发送POST请求必须设置如下两行
        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.setUseCaches(false);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Accept", "*/*");
        connection.setRequestProperty("connection", "Keep-Alive");
        connection.setRequestProperty("user-agent", "Android WYJ");
        connection.setRequestProperty("Charsert", "UTF-8");
        connection.setRequestProperty("Accept-Encoding", "gzip,deflate");
        connection.setRequestProperty("Content-Type",
                "multipart/form-data; boundary=" + BOUNDARY);

        OutputStream out = new DataOutputStream(connection.getOutputStream());
        byte[] end_data = ("--" + BOUNDARY + "--\r\n").getBytes();// 定义最后数据分隔线

        // 文件
        if (files != null && !files.isEmpty()) {
            for (Map.Entry<String, File> entry : files.entrySet()) {
                File file = entry.getValue();
                String fileName = entry.getKey();

                StringBuilder sb = new StringBuilder();
                sb.append("--");
                sb.append(BOUNDARY);
                sb.append("\r\n");
                sb.append("Content-Disposition: form-data;name=\"" + fileName
                        + "\";filename=\"" + file.getName() + "\"\r\n");
                sb.append("Content-Type: image/jpg\r\n\r\n");
                byte[] data = sb.toString().getBytes();
                out.write(data);

                DataInputStream in = new DataInputStream(new FileInputStream(
                        file));
                int bytes = 0;
                byte[] bufferOut = new byte[1024];
                while ((bytes = in.read(bufferOut)) != -1) {
                    out.write(bufferOut, 0, bytes);
                }
                out.write("\r\n".getBytes()); // 多个文件时，二个文件之间加入这个
                in.close();
            }
        }
        // 数据参数
        if (map != null && !map.isEmpty()) {

            for (Map.Entry<String, String> entry : map.entrySet()) {
                StringBuilder sb = new StringBuilder();
                sb.append("--");
                sb.append(BOUNDARY);
                sb.append("\r\n");
                sb.append("Content-Disposition: form-data; name=\""
                        + entry.getKey() + "\"");
                sb.append("\r\n");
                sb.append("\r\n");
                sb.append(entry.getValue());
                sb.append("\r\n");
                byte[] data = sb.toString().getBytes();
                out.write(data);
            }
        }
        out.write(end_data);
        out.flush();
        out.close();

        // 定义BufferedReader输入流来读取URL的响应
//		BufferedReader reader = new BufferedReader(new InputStreamReader(
//				connection.getInputStream()));
//		String line = null;
//		while ((line = reader.readLine()) != null) {
//			System.out.println(line);
//		}

        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            InputStream inStream = connection.getInputStream();
            byte[] number = read(inStream);
            String json = new String(number);
            return json;
        }

        return null;
    }

}
