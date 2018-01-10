package org.mahout.GroupLens;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.eval.RecommenderBuilder;
import org.apache.mahout.cf.taste.eval.RecommenderEvaluator;
import org.apache.mahout.cf.taste.impl.eval.AverageAbsoluteDifferenceRecommenderEvaluator;
import org.apache.mahout.cf.taste.impl.neighborhood.ThresholdUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;
import org.apache.mahout.cf.taste.similarity.precompute.example.GroupLensDataModel;

import java.io.File;
import java.io.IOException;

public class ThresholdUserNeighborRE {
    public static void main(String[] args) {
        try {
            DataModel model = new GroupLensDataModel(new File("ratings.dat"));
            RecommenderEvaluator evaluator = new AverageAbsoluteDifferenceRecommenderEvaluator();
            RecommenderBuilder recommenderBuilder = new RecommenderBuilder() {
                public Recommender buildRecommender(DataModel dataModel) throws TasteException {
                    UserSimilarity similarity = new PearsonCorrelationSimilarity(dataModel);
                    //threshold:0.7     score:0.7828
                    //threshold:0.8     score:0.8272
                    //threshold:0.5     score:0.7450
                    //阈值为0.5时,精度最好
                    UserNeighborhood neighborhood = new ThresholdUserNeighborhood(0.5, similarity, dataModel);
                    return new GenericUserBasedRecommender(dataModel, neighborhood, similarity);
                }
            };
            //evaluate()的最后一个参数是0.05.这意味着仅有5%的数据用于评估。这纯粹是为了方便；评估是一个耗时的过程，使用全部数据绘花上几个小时
            double score = evaluator.evaluate(recommenderBuilder, null, model, 0.95, 0.05);
            System.out.println(score);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TasteException e) {
            e.printStackTrace();
        }
    }
}
