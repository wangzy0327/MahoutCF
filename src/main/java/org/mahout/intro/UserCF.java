package org.mahout.intro;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class UserCF {

    /**
     * UserCF
     * @param args
     */
    public static void main(String[] args) {
        try {
            File modelFile = new File("intro.csv");
            /*
            FileDataModel要求输入文件中的字段分隔符为逗号或者制表符，
            如果你想使用其他分隔符，你可以扩展一个FileDataModel的实现，
            例如，mahout中已经提供了一个解析MoiveLens的数据集（分隔符为::）的实现GroupLensDataModel。
             */
            DataModel model = new FileDataModel(modelFile);
            //用户相似度，使用基于皮尔逊相关系数计算相似度
            UserSimilarity similarity = new PearsonCorrelationSimilarity(model);

            //选择邻居用户，使用NearestNUserNeighborhood实现UserNeighborhood接口，选择邻近的4个用户
            UserNeighborhood neighborhood = new NearestNUserNeighborhood(2, similarity, model);

            Recommender recommender = new GenericUserBasedRecommender(model, neighborhood, similarity);

            //给用户1推荐4个物品
            List<RecommendedItem> recommendations = recommender.recommend(1, 1);

            for (RecommendedItem recommendation : recommendations) {
                System.out.println(recommendation);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TasteException e) {
            e.printStackTrace();
        }
    }
}
