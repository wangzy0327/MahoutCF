package org.mahout.GroupLens;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.common.Weighting;
import org.apache.mahout.cf.taste.eval.RecommenderBuilder;
import org.apache.mahout.cf.taste.eval.RecommenderEvaluator;
import org.apache.mahout.cf.taste.impl.eval.AverageAbsoluteDifferenceRecommenderEvaluator;
import org.apache.mahout.cf.taste.impl.recommender.GenericItemBasedRecommender;
import org.apache.mahout.cf.taste.impl.recommender.svd.ALSWRFactorizer;
import org.apache.mahout.cf.taste.impl.recommender.svd.SVDRecommender;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.ItemSimilarity;
import org.apache.mahout.cf.taste.similarity.precompute.example.GroupLensDataModel;

import java.io.File;
import java.io.IOException;

public class SVDRecommendEvaluator {
    public static void main(String[] args) {
        try {
            DataModel model = new GroupLensDataModel(new File("ratings.dat"));
            RecommenderEvaluator evaluator = new AverageAbsoluteDifferenceRecommenderEvaluator();
            RecommenderBuilder recommenderBuilder = new RecommenderBuilder() {
                public Recommender buildRecommender(DataModel dataModel) throws TasteException {
                    //添加皮尔逊相关系数权重
                    ItemSimilarity similarity = new PearsonCorrelationSimilarity(dataModel, Weighting.WEIGHTED);
                    return new SVDRecommender(dataModel, new ALSWRFactorizer(dataModel, 10, 0.05, 10));
                }
            };
            //evaluate() 0.8 0.2 计算时间比较长
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
