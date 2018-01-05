package org.mahout.GroupLens;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.eval.RecommenderBuilder;
import org.apache.mahout.cf.taste.eval.RecommenderEvaluator;
import org.apache.mahout.cf.taste.impl.eval.AverageAbsoluteDifferenceRecommenderEvaluator;
import org.apache.mahout.cf.taste.impl.eval.RMSRecommenderEvaluator;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;
import org.apache.mahout.common.RandomUtils;

import java.io.File;
import java.io.IOException;

public class RecommendEvaluator {
    public static void main(String[] args) {
        try {
            RandomUtils.useTestSeed();
            DataModel model = new FileDataModel(new File("ua.base"));

            RecommenderEvaluator evaluator = new AverageAbsoluteDifferenceRecommenderEvaluator();
            //RMS Evaluator 均方根
//            RecommenderEvaluator evaluator = new RMSRecommenderEvaluator();

            RecommenderBuilder builder = new RecommenderBuilder() {
                public Recommender buildRecommender(DataModel dataModel) throws TasteException {
                    UserSimilarity similarity = new PearsonCorrelationSimilarity(dataModel);
                    UserNeighborhood neighborhood = new NearestNUserNeighborhood(2, similarity, dataModel);
                    return new GenericUserBasedRecommender(dataModel, neighborhood, similarity);
                }
            };
            // SlopeOneRecommender在 Mahout0.9后被去掉了
//            RecommenderBuilder builder = new RecommenderBuilder() {
//                public Recommender buildRecommender(DataModel dataModel) throws TasteException {
//                    return new SlopeOneRecommender(dataModel);
//                }
//            };

            double score = evaluator.evaluate(builder, null, model, 0.9, 1.0);
            System.out.println(score);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (TasteException e) {
            e.printStackTrace();
        }
    }
}
