package test;

import de.hbrs.ia.code.IManagePersonal;
import de.hbrs.ia.code.ManagePersonalImpl;
import de.hbrs.ia.model.SalesMan;
import de.hbrs.ia.model.SocialPerformanceRecord;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class ManagePersonalTest {

    private static MongoClient mongoClient;
    private static IManagePersonal managePersonal;

    @BeforeAll
    public static void setup() {
        mongoClient = MongoClients.create("mongodb://localhost:27017");
        managePersonal = new ManagePersonalImpl(mongoClient, "testdb");
    }

    @AfterAll
    public static void tearDown() {
        mongoClient.getDatabase("testdb").drop();
        mongoClient.close();
    }

    @Test
    void roundTrip() {
        // Step 1: Create a SalesMan object
        SalesMan salesMan = new SalesMan("John", "Doe", 1);

        // Step 2: Add the SalesMan to the database
        managePersonal.createSalesMan(salesMan);

        // Step 3: Create a SocialPerformanceRecord object
        SocialPerformanceRecord record1 = new SocialPerformanceRecord(2023);
        setCompetenceValues(record1, new int[]{90, 85, 80, 95, 88, 92}, new int[]{85, 80, 75, 90, 85, 88}, new double[]{1500, 1200, 1000, 1800, 1400, 1600});
        record1.setSalesMan(salesMan);

        // Step 4: Add the SocialPerformanceRecord to the SalesMan
        managePersonal.addSocialPerformanceRecord(record1, salesMan);

        // Step 5: Retrieve the SalesMan from the database
        SalesMan retrievedSalesMan = managePersonal.readSalesMan(1);
        assertNotNull(retrievedSalesMan);
        assertEquals("John", retrievedSalesMan.getFirstname());
        assertEquals("Doe", retrievedSalesMan.getLastname());
        assertEquals(1, retrievedSalesMan.getId());

        // Step 6: Retrieve the SocialPerformanceRecord from the database
        List<SocialPerformanceRecord> records = managePersonal.readSocialPerformanceRecord(salesMan);
        assertNotNull(records);
        assertFalse(records.isEmpty());
        SocialPerformanceRecord retrievedRecord = records.get(0);
        assertEquals(2023, retrievedRecord.getYear());
        assertEquals(90, retrievedRecord.getCompetences().iterator().next().getTargetValue());
        assertEquals(85, retrievedRecord.getCompetences().iterator().next().getActualValue());
        assertEquals(1500, retrievedRecord.getCompetences().iterator().next().getBonus());
        assertEquals(salesMan.getId(), retrievedRecord.getSalesMan().getId());
        assertEquals(salesMan.getId(), retrievedRecord.getSalesMan().getId());

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
