package com.laowang.utils.api.baidu;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ClassifyTextService {

    public static void main(String args[]){
        String url = "https://aip.baidubce.com/rpc/2.0/nlp/v1/topic?charset=UTF-8&access_token="+AuthService.getAuth();
        Document doc;
        Connection connection;
        try {
        String json = "{\n" +
                "\t\"title\":\"欧洲冠军联赛\",\n" +
                "\t\"content\": \"欧洲冠军联赛是欧洲足球协会联盟主办的年度足球比赛，代表欧洲俱乐部足球最高荣誉和水平，被认为是全世界最高素质、最具影响力以及最高水平的俱乐部赛事，亦是世界上奖金最高的足球赛事和体育赛事之一。\"\n" +
                "}";

            connection = Jsoup.connect(url).header("Content-type","application/json");
            connection.requestBody(json).ignoreContentType(true);
            doc = connection.post();
            //获取到分类结果，并解析出json串
            String result = doc.body().html();
            JSONObject jsonpObject = new JSONObject(result);
            JSONObject item = jsonpObject.getJSONObject("item");
            //准备bean，用于封装分类结果
            ResultTagBean resultTagBean = new ResultTagBean();
            List list = new ArrayList();
            //解析二级标签
            JSONArray lv2 = item.getJSONArray("lv2_tag_list");
            list.add(lv2.getJSONObject(0).getString("tag"));
            list.add(lv2.getJSONObject(1).getString("tag"));
            list.add(lv2.getJSONObject(2).getString("tag"));
            resultTagBean.setLv2_tag(list);//设置二级标签
            //解析一级标签
            JSONArray lv1 = item.getJSONArray("lv1_tag_list");
            resultTagBean.setLv1_tag(lv1.getJSONObject(0).getString("tag"));//设置二级标签
            //返回结果封装完毕，打印
            System.out.println(resultTagBean);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
