package com.laowang.utils.api.baidu;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ClassiferFromBaidu {
    private final static Logger logger = LoggerFactory.getLogger(ClassiferFromBaidu.class.getName());
    /**
     * 获取token
     * @return token或则null
     */
    private static String getToken(){
        return AuthService.getAuth();
    }

    /**
     * 请求百度接口，获取要分类文章的分类结果
     * @param title 被分类文章的标题
     * @param content 被分类文章的内容
     * @return 分类结果bean
     */
    public static ResultTagBean getArticleTags(String title,String content){
        String token = getToken();
        if (token == null){//可能百度接口有变动了
            return null;
        }
        String url = "https://aip.baidubce.com/rpc/2.0/nlp/v1/topic?charset=UTF-8&access_token="+token;
        String urlLabel = "https://aip.baidubce.com/rpc/2.0/nlp/v1/keyword?charset=UTF-8&access_token="+AuthService.getAuth();
        String json =  "{\"title\":\""+title+"\",\"content\": \""+content+"\"}";
        Document doc;
        Connection connection;
        try {
            //1.构造百度获取分类接口请求对象，并发送post请求
            connection = Jsoup.connect(url).header("Content-type","application/json");
            connection.requestBody(json).ignoreContentType(true);
            doc = connection.post();
            //获取到分类结果，并解析出json串
            String result = doc.body().html();
            JSONObject jsonpObject = new JSONObject(result);
            JSONObject item = jsonpObject.getJSONObject("item");
            //准备封装结果bean，用于封装分类结果
            ResultTagBean resultTagBean = new ResultTagBean();
            List list = new ArrayList();
            //解析二级标签
            JSONArray lv2 = item.getJSONArray("lv2_tag_list");
            if (lv2.length()!=0 ) {
                for (int i = 0; i < lv2.length(); i++) {
                    list.add(lv2.getJSONObject(i).getString("tag"));
                }
            }
            resultTagBean.setLv2_tag(list);//设置二级标签到bean
            //解析一级标签
            JSONArray lv1 = item.getJSONArray("lv1_tag_list");
            resultTagBean.setLv1_tag(lv1.getJSONObject(0).getString("tag"));//设置一级标签到bean

            //2.获取标签
            connection = Jsoup.connect(urlLabel).header("Content-type","application/json");
            connection.requestBody(json).ignoreContentType(true);
            doc = connection.post();
            //获取到分类结果，并解析出json串
            result = doc.body().html();
            System.out.println(result);
            jsonpObject = new JSONObject(result);
            JSONArray items = jsonpObject.getJSONArray("items");
            //封装结果
            String labels = "";
            if (items != null && items.length()>0){
                for (int i=0;i<items.length();i++){
                    labels += items.getJSONObject(i).getString("tag")+",";
                }
                labels = labels.substring(0,labels.length()-1);
            }
            resultTagBean.setLabels(labels);
            //返回结果封装完毕，日志记录
            logger.info("title:"+title+";分类结果为:"+resultTagBean.toString());
            return resultTagBean;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
