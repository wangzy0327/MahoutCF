package org.mahout.intro;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.eval.RecommenderBuilder;
import org.apache.mahout.cf.taste.eval.RecommenderEvaluator;
import org.apache.mahout.cf.taste.impl.eval.AverageAbsoluteDifferenceRecommenderEvaluator;
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
            DataModel model = new FileDataModel(new File("intro.csv"));

            RecommenderEvaluator evaluator = new AverageAbsoluteDifferenceRecommenderEvaluator();

//            RecommenderEvaluator evaluator = new RMSRecommenderEvaluator();


//            RecommenderBuilder builder = new RecommenderBuilder() {
//                public Recommender buildRecommender(DataModel model) throws TasteException {
//
//                }
//            };

            RecommenderBuilder builder = new RecommenderBuilder() {
                public Recommender buildRecommender(DataModel dataModel) throws TasteException {
                    UserSimilarity similarity = new PearsonCorrelationSimilarity(dataModel);
                    UserNeighborhood neighborhood = new NearestNUserNeighborhood(2, similarity, dataModel);
                    return new GenericUserBasedRecommender(dataModel, neighborhood, similarity);
                }
            };

            double score = evaluator.evaluate(builder, null, model, 0.9, 1.0);
            System.out.println(score);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (TasteException e) {
            e.printStackTrace();
        }
        /*double evaluate(RecommenderBuilder recommenderBuilder,
        DataModelBuilder dataModelBuilder,如果为空，就用默认的datamodel，除非使用了特殊的datamodel实现，需要插入到评估过程中的datamodelbuilder
        DataModel dataModel,测试的数据集
        double trainingPercentage,每一个用户用来做训练推荐的百分比
        double evaluationPercentage用来训练推荐的用户的百分比,可用于仅通过庞大数据集总的很小一部分数据来生成一个精度较低但更快的评估，希望快速测试recommender中的小更改时很有用)
        throws TasteException
        */
        /*
         输出为NaN,由日志可知，有9个案例都无法进行推荐。

         原因可能是选取的训练比例不合理。

         故增大训练比例的值，改为0.8 仍是NaN,

         改为0.9 结果输出：
         */

    }
}
