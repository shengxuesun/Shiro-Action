package com.yijiinfo.hik;

import com.alibaba.fastjson.JSONObject;
import com.hikvision.artemis.sdk.ArtemisHttpUtil;
import com.hikvision.artemis.sdk.config.ArtemisConfig;
import com.yijiinfo.system.model.Sms;
import com.yijiinfo.system.service.SmsService;
import com.yijiinfo.system.service.UserInfoService;
import me.zhyd.oauth.utils.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.shiro.crypto.hash.Md5Hash;

import javax.annotation.Resource;
import java.io.InputStream;
import java.util.*;

import static com.yijiinfo.MainActionApplication.ARTEMIS_PATH;

public class ArtemisPostTest {

	/**
	 * 请根据自己的appKey和appSecret更换static静态块中的三个参数. [1 host]
	 * 如果你选择的是和现场环境对接,host要修改为现场环境的ip,https端口默认为443，http端口默认为80.例如10.33.25.22:443 或者10.33.25.22:80
	 * appKey和appSecret请按照或得到的appKey和appSecret更改.
	 * TODO 调用前先要清楚接口传入的是什么，是传入json就用doPostStringArtemis方法，下载图片doPostStringImgArtemis方法
	 */
	static {
		ArtemisConfig.host = "192.168.1.138";// 代理API网关nginx服务器ip端口
		ArtemisConfig.appKey = "23732415";// 秘钥appkey
		ArtemisConfig.appSecret = "gldkaPEjryqRl1cvxpfd";// 秘钥appSecret
	}
	/**
	 * 能力开放平台的网站路径
	 * TODO 路径不用修改，就是/artemis
	 */
	private static final String ARTEMIS_PATH = "/artemis";


	/**
	 * 调用POST请求类型(application/json)接口，这里以入侵报警事件日志为例
	 * https://open.hikvision.com/docs/918519baf9904844a2b608e558b21bb6#e6798840
	 *
	 * @return
	 */
	public static String callPostStringApi(){
		/**
		 * http://10.33.47.50/artemis/api/scpms/v1/eventLogs/searches
		 * 根据API文档可以看出来，这是一个POST请求的Rest接口，而且传入的参数值为一个json
		 * ArtemisHttpUtil工具类提供了doPostStringArtemis这个函数，一共六个参数在文档里写明其中的意思，因为接口是https，
		 * 所以第一个参数path是一个hashmap类型，请put一个key-value，query为传入的参数，body为传入的json数据
		 * 传入的contentType为application/json，accept不指定为null
		 * header没有额外参数可不传,指定为null
		 *
		 */
		final String getCamsApi = ARTEMIS_PATH +"/api/scpms/v1/eventLogs/searches";
		Map<String, String> path = new HashMap<String, String>(2) {
			{
				put("http://", getCamsApi);//根据现场环境部署确认是http还是https
			}
		};

		JSONObject jsonBody = new JSONObject();

		jsonBody.put("pageNo", 1);
		jsonBody.put("pageSize", 3);
		String body = jsonBody.toJSONString();

		String result =ArtemisHttpUtil.doPostStringArtemis(path,body,null,null,"application/json",null);// post请求application/json类型参数
		return result;
	}

	/**
	 * 调用POST请求下载图片类型接口，这里以获取访客记录中的图片接口为例
	 * https://open.hikvision.com/docs/a0a1a0a24701a00aa904f7b151f97410#f11f3208
	 *
	 * @return
	 */
	public static void callPostImgStringApi(){
		/**
		 * http://10.33.47.50/api/visitor/v1/record/pictures
		 * 根据API文档可以看出来，这是一个POST请求的Rest接口，而且传入的参数值为一个json
		 * ArtemisHttpUtil工具类提供了doPostStringImgArtemis这个函数，一共六个参数在文档里写明其中的意思，因为接口是https，
		 * 所以第一个参数path是一个hashmap类型，请put一个key-value，query为传入的参数，body为传入的json数据
		 * 传入的contentType为application/json，accept不指定为null
		 * header没有额外参数可不传,指定为null
		 *
		 */
		final String VechicleDataApi = ARTEMIS_PATH +"/api/visitor/v1/record/pictures";
		Map<String,String> path = new HashMap<String,String>(2){
			{
				put("http://",VechicleDataApi);
			}
		};

		JSONObject jsonBody = new JSONObject();
		jsonBody.put("svrIndexCode", "8907fd9d-d090-43d3-bb3a-3a4b10dd7219");
		jsonBody.put("picUri", "/pic?0dd453i3c-e*046456451175m6ep=t=i2p*i=d1s*i3d0d*=*1b8i81f4747059503--bdf90a-855s5721z3b9i=1=");
		String body = jsonBody.toJSONString();
		System.out.println("body: "+body);
		HttpResponse result =ArtemisHttpUtil.doPostStringImgArtemis(path,body,null,null,"application/json",null);
		try {
			HttpResponse resp = result;
			if (200==resp.getStatusLine().getStatusCode()) {
				HttpEntity entity = resp.getEntity();
				InputStream in = entity.getContent();
				Tools.savePicToDisk(in, "d:/", "test4.jpg");
				System.out.println("下载成功");
			}else{
				System.out.println("下载出错");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		String encryptPassword = new Md5Hash("123456", "1608786173574").toString();
		System.out.println(encryptPassword);
//		String date1 = "20201209";
//		String date2 = "20201109";
//		System.out.println(Integer.parseInt(date1) < Integer.parseInt(date2));
//		String StringeResult = callPostStringApi();
//		System.out.println("StringeResult结果示例: "+StringeResult);
//		callPostImgStringApi();

//		System.out.println(StringUtils.isEmpty(null)?"empty":"不空");;

//		System.out.println(Long.parseLong("76037F40",16));
//
//		String cardNo = Long.toString(Long.parseLong("125C2263",16));
//		int cardNoLength = cardNo.length();
//		if(cardNoLength == 9){
//			cardNo = "0"+cardNo;
//		}else if(cardNoLength == 8){
//			cardNo = "00"+cardNo;
//		}
//		System.out.println(cardNo);
//
//		System.out.println(new Date(System.currentTimeMillis()));
//		final String getCamsApi = ARTEMIS_PATH+"/api/frs/v1/face/picture/check";
//		Map<String, String> path = new HashMap<String, String>(2) {
//			{
//				put("https://", getCamsApi);//根据现场环境部署确认是http还是https
//			}
//		};
//		String body = "{\"facePicBinaryData\":\"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\"}";
//		String result = ArtemisHttpUtil.doPostStringArtemis(path,body,null,null,"application/json",null);// post请求application/json类型参数
//		System.out.println("___________________"+JSONObject.parseObject(result).getObject("data",JSONObject.class).get("faceScore").toString());
//		0x1f902066 "msg":"pic data error, decompress failed","data":null
//		0x1f913016 评分失败，重新上传图片。

//		JSONObject jsonObject = JSONObject.parseObject("{\"code\":\"0\",\"msg\":\"SUCCESS\",\"data\":{\"total\":59,\"pageNo\":1,\"pageSize\":1,\"list\":[{\"personId\":\"03fc803834c34004add606cc991bdd27\",\"personName\":\"qe\",\"gender\":1,\"orgIndexCode\":\"c01ab7b0-46db-415f-aa0c-540b9d191bdd\",\"phone\":\"13333333333\",\"jobNo\":\"1555\",\"certificateType\":111,\"certificateNo\":\"12313\",\"createTime\":\"2019-06-20T14:19:42.984+08:00\",\"updateTime\":\"2019-07-23T18:41:49.708+08:00\",\"orgPath\":\"@root000000@c01ab7b0-46db-415f-aa0c-540b9d191bdd@\",\"orgPathName\":\"@默认组织@sz@\",\"personPhoto\":[{\"personPhotoIndexCode\":\"1dd742ea-f220-4d63-bffa-fea82441bbba\",\"picUri\":\"/pic?4dd569i90-e*615455166115m6ep=t=i1p*i=d1s*i=d3b*iedd0*9373cb46d-97105f--1544370za33s=1i15=\",\"serverIndexCode\":\"a4b8e993-6d6a-43b0-b3ac-3b2dddf17a45\",\"personId\":\"03fc803834c34004add606cc991bdd27\"}]}]}}");
//		int num = Integer.parseInt(jsonObject.getJSONObject("data").get("total").toString());
//		System.out.println(num);
//		List<Object> lists = jsonObject.getJSONObject("data").getObject("list",List.class);
////		System.out.println(lists.get(0).toString());
//		System.out.println(JSONObject.parseObject(lists.get(0).toString()).get("personId").toString());
	}
}


