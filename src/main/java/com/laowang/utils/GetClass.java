package com.laowang.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;
import org.thunlp.text.classifiers.BasicTextClassifier;
import org.thunlp.text.classifiers.ClassifyResult;
import org.thunlp.text.classifiers.LinearBigramChineseTextClassifier;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wangyonghao8 on 2018/3/21.
 */
@Component
public class GetClass {
    /**
     * 如果需要读取已经训练好的模型，再用其进行分类，可以按照本函数的代码调用分类器
     *
     */
    public Map runLoadModelAndUse(String text) {
        // 新建分类器对象
        BasicTextClassifier classifier = new BasicTextClassifier();

        // 设置分类种类，并读取模型
        try {
            classifier.setTextClassifier(new LinearBigramChineseTextClassifier(classifier.getCategorySize()));
            //windows本地开发用到的路径
            classifier.loadCategoryListFromFile(ResourceUtils.getFile("classpath:news_model/category").getPath());
            classifier.getTextClassifier().loadModel(ResourceUtils.getFile("classpath:news_model").getPath());
            //部署到服务器上用到的路径，并且服务器上必须存在目录  TODO：/root/textClassify/model
//            classifier.loadCategoryListFromFile("/root/textClassify/model/category");
//            classifier.getTextClassifier().loadModel("/root/textClassify/model");
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

		/*
		 * 上面三行代码等价于设置如下参数，然后初始化并运行：
		 *
		   String defaultArguments = ""
		 +  "-l  my_novel_model"  // 设置您的训练好的模型的路径，这里的路径只是给出样例
		 ;
		 classifier.Init(defaultArguments.split(" "));
		 classifier.runAsLinearBigramChineseTextClassifier();
		 *
		 */

        // 之后就可以使用分类器进行分类
        //String text = "再次回到世锦赛的赛场上，林丹终于变回了以前的那个超级丹.";
        int topN = 3;  // 保留最有可能的3个结果
        ClassifyResult[] result = classifier.classifyText(text, topN);
        Map<String,Double> resultClasses = new HashMap<>();
        for (int i = 0; i < topN; ++i) {
            // 输出分类编号，分类名称，以及概率值。
            resultClasses.put(classifier.getCategoryName(result[i].label),result[i].prob);
            System.out.println(result[i].label + "\t" +
                    classifier.getCategoryName(result[i].label) + "\t" +
                    result[i].prob);
        }
        return resultClasses;
    }

    /**
     * function: give one text and return its class
     * @param text
     * @return json string or ""
     */
    public String getOneTextClass(String text){
        try {
            return new ObjectMapper().writeValueAsString(this.runLoadModelAndUse(text));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * function: give some text and return their classes
     * @param text
     * @return json string or ""
     */
    public String getSomeTextClass(String[] text){
        Map<Integer,Map> result = new HashMap<>();
        for (int i=0;i<text.length;i++) {
            result.put(i, this.runLoadModelAndUse(text[i]));
        }

        try {
            return new ObjectMapper().writeValueAsString(result);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "";
        }
    }


}
