package de.hbrs.ia.code;

import static com.mongodb.client.model.Filters.eq;

import de.hbrs.ia.model.SalesMan;
import de.hbrs.ia.model.SocialPerformanceRecord;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
public class QuickStart {
    public static void main( String[] args ) {
        // Replace the placeholder with your MongoDB deployment's connection string
        String uri = "mongodb://localhost:27017/";
        String dbName = "BonusComputationSheet";
        try (MongoClient mongoClient = MongoClients.create(uri)) {

            // Create an instance of the ManagePersonalImpl class
            ManagePersonalImpl managePersonal = new ManagePersonalImpl(mongoClient, dbName);

            // Create example SalesMan records
            SalesMan salesMan1 = new SalesMan("John", "Doe", 1001);
            SalesMan salesMan2 = new SalesMan("Jane", "Smith", 1002);
            SalesMan salesMan3 = new SalesMan("Michael", "Johnson", 1003);

            // Add these SalesMan records to the MongoDB database
            managePersonal.createSalesMan(salesMan1);
            managePersonal.createSalesMan(salesMan2);
            managePersonal.createSalesMan(salesMan3);

            // Create example SocialPerformanceRecords for each SalesMan
            SocialPerformanceRecord record1 = new SocialPerformanceRecord(2023);
            record1.setSalesMan(salesMan1);
            // Set specific values for SocialCompetences
            setCompetenceValues(record1, new int[]{90, 85, 80, 95, 88, 92}, new int[]{85, 80, 75, 90, 85, 88}, new double[]{1500, 1200, 1000, 1800, 1400, 1600});

            SocialPerformanceRecord record2 = new SocialPerformanceRecord(2023);
            record2.setSalesMan(salesMan2);
            setCompetenceValues(record2, new int[]{80, 75, 70, 85, 78, 82}, new int[]{78, 72, 68, 82, 75, 79}, new double[]{1200, 1000, 900, 1600, 1300, 1400});

            SocialPerformanceRecord record3 = new SocialPerformanceRecord(2022);
            record3.setSalesMan(salesMan3);
            setCompetenceValues(record3, new int[]{85, 80, 75, 90, 83, 87}, new int[]{82, 78, 72, 88, 81, 84}, new double[]{1300, 1100, 950, 1700, 1350, 1450});

            // Add these performance records to the MongoDB database
            managePersonal.addSocialPerformanceRecord(record1, salesMan1);
            managePersonal.addSocialPerformanceRecord(record2, salesMan2);
            managePersonal.addSocialPerformanceRecord(record3, salesMan3);

            // Print confirmation messages
            System.out.println("Inserted example SalesMan and SocialPerformanceRecord data.");
        }
    }

    private static void setCompetenceValues(SocialPerformanceRecord record, int[] targetValues, int[] actualValues, double[] bonuses) {
        int index = 0;
        for (SocialPerformanceRecord.SocialCompetence competence : record.getCompetences()) {
            competence.setTargetValue(targetValues[index]);
            competence.setActualValue(actualValues[index]);
            competence.setBonus(bonuses[index]);
            index++;
        }
    }
}